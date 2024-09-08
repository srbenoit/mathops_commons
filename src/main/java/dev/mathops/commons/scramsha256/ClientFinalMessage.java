package dev.mathops.commons.scramsha256;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.builder.SimpleBuilder;
import dev.mathops.commons.log.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * The data that makes up the "client-final" message.
 *
 * <p>
 * c=biws,r=[c-nonce][s-nonce],p=[base-64-of-proof]
 *
 * <pre>
 * SALTED_PASSWORD = Hi(Normalize(password), SALT, ITER_COUNT)
 * CLIENT_KEY      = HMAC(SALTED_PASSWORD, "Client Key")
 * STORED_KEY      = H(CLIENT_KEY)
 * AUTH_MESSAGE    = CLIENT_FIRST_MESSAGE + "," + SERVER_FIRST_MESSAGE + "," + CNONCE + SNONCE
 * CLIENT_SIG      = HMAC(STORED_KEY, AUTH_MESSAGE)
 * CLIENT_PROOF    = CLIENT_KEY XOR CLIENT_SIG
 * CLIENT_FINAL    = CNONCE + SNONCE + "," + CLIENT_PROOF
 * </pre>
 */
final class ClientFinalMessage {

    /** The size of the client proof array. */
    private static final int PROOF_SIZE = 32;

    /** The size of the Base-64 representation of the client proof array ( 4 * ceil(32 / 3) = 44) . */
    private static final int PROOF_BASE64_LEN = 44;

    /** A commonly used byte array. */
    private static final byte[] CLIENT_KEY_BYTES = ScramUtils.CLIENT_KEY_STR.getBytes(StandardCharsets.UTF_8);

    /** A commonly used byte array. */
    private static final byte[] LEADING = "c=biws,r=".getBytes(StandardCharsets.UTF_8);

    /** A commonly used byte array. */
    private static final byte[] MID = ",p=".getBytes(StandardCharsets.UTF_8);

    /** The authentication message. */
    final byte[] authMessage;

    /** The client signature. */
    private final byte[] clientSig;

    /** The client proof. */
    private final byte[] clientProof;

    /** The assembled 'client-final' message. */
    private final byte[] clientFinal;

    /** The base-64 encoding of the assembled 'client-final' message. */
    final byte[] base64;

    /**
     * Constructs a new {@code ClientFinalMessage}.
     *
     * @param password    the entered password
     * @param clientFirst the client-first message
     * @param serverFirst the server-first message
     */
    ClientFinalMessage(final CharSequence password, final ClientFirstMessage clientFirst,
                       final ServerFirstMessage serverFirst) {

        final byte[] normPassword = ScramUtils.normalize(password);
        final byte[] saltedPassword = ScramUtils.hi(normPassword, serverFirst.salt, serverFirst.iterCount);
        final byte[] clientKey = ScramUtils.hmac_sha_256(saltedPassword, CLIENT_KEY_BYTES);
        final byte[] storedKey = ScramUtils.sha_256(clientKey);

        this.authMessage = assembleAuthMessage(clientFirst, serverFirst);

        this.clientSig = storedKey == null ? ScramUtils.ZERO_BYTES : ScramUtils.hmac_sha_256(storedKey,
                this.authMessage);

        this.clientProof = new byte[PROOF_SIZE];
        if (clientKey != null && this.clientSig != null) {
            for (int i = 0; i < PROOF_SIZE; ++i) {
                this.clientProof[i] = (byte) ((int) clientKey[i] ^ (int) this.clientSig[i]);
            }
        }

        byte[] theClientFinal;
        byte[] theBase64;

        try (final ByteArrayOutputStream output = new ByteArrayOutputStream(100)) {
            output.write(LEADING);
            output.write(clientFirst.cNonce);
            output.write(serverFirst.sNonce);
            output.write(MID);
            final byte[] proofBase64 = Base64.getEncoder().encode(this.clientProof);
            output.write(proofBase64);

            theClientFinal = output.toByteArray();
            theBase64 = Base64.getEncoder().encode(theClientFinal);
        } catch (final IOException ex) {
            Log.warning(ex);
            theClientFinal = ScramUtils.ZERO_BYTES;
            theBase64 = ScramUtils.ZERO_BYTES;
        }

        this.clientFinal = theClientFinal;
        this.base64 = theBase64;
    }

    /**
     * Parses a {@code ClientFirstMessage} from its base-64 representation.
     *
     * @param theBase64   the base64 to parse
     * @param clientFirst the client-first message
     * @param serverFirst the server-first message
     * @param credentials the user credentials
     * @throws IllegalArgumentException if the message format is not valid
     */
    ClientFinalMessage(final byte[] theBase64, final ClientFirstMessage clientFirst,
                       final ServerFirstMessage serverFirst, final UserCredentials credentials)
            throws IllegalArgumentException {

        this.base64 = theBase64.clone();
        this.clientFinal = Base64.getDecoder().decode(theBase64);

        final int expect = LEADING.length + ScramUtils.NONCE_LEN + ScramUtils.NONCE_LEN + MID.length + PROOF_BASE64_LEN;
        if (this.clientFinal.length != expect) {
            throw new IllegalArgumentException("client-final message has invalid length");
        }

        final int cNonceStart = LEADING.length;
        final int sNonceStart = cNonceStart + ScramUtils.NONCE_LEN;
        final int midStart = sNonceStart + ScramUtils.NONCE_LEN;

        if (ScramUtils.isSame(LEADING, 0, this.clientFinal, 0, LEADING.length)
            && ScramUtils.isSame(MID, 0, this.clientFinal, midStart, MID.length)) {

            if (ScramUtils.isSame(clientFirst.cNonce, 0, this.clientFinal, cNonceStart, ScramUtils.NONCE_LEN)) {
                if (ScramUtils.isSame(serverFirst.sNonce, 0, this.clientFinal, sNonceStart, ScramUtils.NONCE_LEN)) {

                    final int proofStart = midStart + MID.length;

                    final byte[] proofBase64 = new byte[PROOF_BASE64_LEN];
                    System.arraycopy(this.clientFinal, proofStart, proofBase64, 0, PROOF_BASE64_LEN);
                    this.clientProof = Base64.getDecoder().decode(proofBase64);

                    this.authMessage = assembleAuthMessage(clientFirst, serverFirst);

                    this.clientSig = ScramUtils.hmac_sha_256(credentials.storedKey, this.authMessage);

                    final byte[] clientKey = new byte[PROOF_SIZE];
                    if (this.clientSig != null) {
                        for (int i = 0; i < PROOF_SIZE; ++i) {
                            clientKey[i] = (byte) ((int) this.clientSig[i] ^ (int) this.clientProof[i]);
                        }
                    }

                    final byte[] test = ScramUtils.sha_256(clientKey);
                    if (!Arrays.equals(test, credentials.storedKey)) {
                        throw new IllegalArgumentException("Authentication failed");
                    }

                } else {
                    throw new IllegalArgumentException("client-final message has invalid server nonce");
                }
            } else {
                throw new IllegalArgumentException("client-final message has invalid client nonce");
            }
        } else {
            throw new IllegalArgumentException("client-final message has invalid delimiters");
        }
    }

    /**
     * Generates an authentication message.
     *
     * <pre>
     * AuthMessage          := client-first-message-bare + "," +
     *                         server-first-message + "," +
     *                         client-final-message-without-proof
     * client-first-message-bare = "n=" saslname ",r=" c-nonce
     * server-first-message      = "r=" c-nonce s-nonce ",s=" salt-base-64 ",i=" posit-number
     * client-final-message-without-proof = "c=biws,r=" c-nonce s-nonce
     * </pre>
     *
     * @param clientFirst the client-first message
     * @param serverFirst the server-first message
     * @return the assembled message
     */
    private static byte[] assembleAuthMessage(final ClientFirstMessage clientFirst,
                                              final ServerFirstMessage serverFirst) {

        final int len = clientFirst.clientFirst.length - 3 + serverFirst.serverFirst.length + LEADING.length
                        + ScramUtils.NONCE_LEN + ScramUtils.NONCE_LEN + 2;

        final byte[] theAuthMessage = new byte[len];

        System.arraycopy(clientFirst.clientFirst, 3, theAuthMessage, 0, clientFirst.clientFirst.length - 3);
        int pos = clientFirst.clientFirst.length - 3;
        theAuthMessage[pos] = (byte) CoreConstants.COMMA_CHAR;
        ++pos;
        System.arraycopy(serverFirst.serverFirst, 0, theAuthMessage, pos, serverFirst.serverFirst.length);
        pos += serverFirst.serverFirst.length;
        theAuthMessage[pos] = (byte) CoreConstants.COMMA_CHAR;
        ++pos;
        System.arraycopy(LEADING, 0, theAuthMessage, pos, LEADING.length);
        pos += LEADING.length;
        System.arraycopy(clientFirst.cNonce, 0, theAuthMessage, pos, ScramUtils.NONCE_LEN);
        pos += ScramUtils.NONCE_LEN;
        System.arraycopy(serverFirst.sNonce, 0, theAuthMessage, pos, ScramUtils.NONCE_LEN);
        pos += ScramUtils.NONCE_LEN;
        if (pos != theAuthMessage.length) {
            Log.warning("Length mismatch!");
        }

        return theAuthMessage;
    }

    /**
     * Generates a string representation of this object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final String msgStr = new String(this.clientFinal, StandardCharsets.UTF_8);
        final String base64Str = new String(this.base64, StandardCharsets.UTF_8);

        return SimpleBuilder.concat("ClientFinalMessage{encoded=", msgStr, ", base64=", base64Str, "}");
    }
}
