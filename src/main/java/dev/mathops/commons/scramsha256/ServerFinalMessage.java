package dev.mathops.commons.scramsha256;

import dev.mathops.commons.CoreConstants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * The data that makes up the "server-final" message.
 *
 * <pre>
 * SERVER_SIG[32] + "," + TOKEN[30]
 * </pre>
 */
class ServerFinalMessage {

    /** A zero-length array. */
    private static final byte[] ZERO_BYTES = new byte[0];

    /** The computed server signature. */
    private final byte[] serverSig;

    /** An error message, if this is a "server-error". */
    final byte[] error;

    /** The raw message. */
    final byte[] serverFinal;

    /** The base-64 encoding of the assembled 'server-final' message. */
    final byte[] base64;

    /** The token assigned. */
    final String token;

    /**
     * Constructs a {@code ServerFinalMessage}.
     *
     * @param clientFinal the {@code ClientFinalMessage} message (used to validate that the server-first message matches
     *                    this request)
     * @param credentials the user credentials
     * @param theToken    the security token (30 URL-safe characters)
     * @throws IllegalArgumentException if there is an error in the message
     */
    ServerFinalMessage(final ClientFinalMessage clientFinal, final UserCredentials credentials, final String theToken)
            throws IllegalArgumentException {

        if (clientFinal == null) {
            throw new IllegalArgumentException("ClientFinal may not be null");
        }
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        }
        if (theToken == null || theToken.length() != 30) {
            throw new IllegalArgumentException("Invalid token");
        }

        this.serverSig = ScramUtils.hmac_sha_256(credentials.serverKey, clientFinal.authMessage);
        this.error = ZERO_BYTES;

        this.token = theToken;

        this.serverFinal = new byte[63];
        System.arraycopy(this.serverSig, 0, this.serverFinal, 0, 32);
        this.serverFinal[32] = CoreConstants.COMMA_CHAR;
        System.arraycopy(this.token.getBytes(StandardCharsets.UTF_8), 0, this.serverFinal, 33, 30);

        this.base64 = Base64.getEncoder().encode(this.serverFinal);
    }

    /**
     * Parses a {@code ServerFinalMessage} that represents an error.
     *
     * @param theError the error message
     * @throws IllegalArgumentException if the error message is null or blank
     */
    ServerFinalMessage(final String theError) throws IllegalArgumentException {

        if (theError == null || theError.isBlank()) {
            throw new IllegalArgumentException("Error message may not be null or blank");
        }

        this.serverSig = ZERO_BYTES;
        this.error = theError.getBytes(StandardCharsets.UTF_8);

        this.serverFinal = new byte[this.error.length + 2];
        this.serverFinal[0] = 'e';
        this.serverFinal[1] = '=';
        System.arraycopy(this.serverFinal, 2, this.error, 0, this.error.length);

        this.base64 = Base64.getEncoder().encode(this.serverFinal);
        this.token = CoreConstants.EMPTY;
    }

    /**
     * Constructs a {@code ServerFinalMessage} from a hex representation returned by the server in response to the
     * 'client-first' message, and verifies that its format is correct and that its client nonce matches that from the
     * 'client-first' message, to ensure it should be associated with that request.
     *
     * @param theBase64 the base-64 to parse
     * @throws IllegalArgumentException if there is an error in the message
     */
    ServerFinalMessage(final byte[] theBase64) throws IllegalArgumentException {

        this.base64 = theBase64.clone();
        this.serverFinal = Base64.getDecoder().decode(this.base64);
        this.error = ZERO_BYTES;

        final int len = this.serverFinal.length;
        if (len > 2 && this.serverFinal[0] == 'e' && this.serverFinal[1] == '=') {
            // This was a "server-error" message, so fail
            final String msg = new String(this.serverFinal, 2, len - 2, StandardCharsets.UTF_8);
            throw new IllegalArgumentException(msg);
        }

        if (this.serverFinal.length != 63) {
            throw new IllegalArgumentException("server-final message had invalid length: " +
                                               this.serverFinal.length);
        }

        if ((int) this.serverFinal[32] != CoreConstants.COMMA_CHAR) {
            throw new IllegalArgumentException("server-final message had invalid delimiters");
        }

        this.serverSig = new byte[32];
        System.arraycopy(this.serverFinal, 0, this.serverSig, 0, 32);

        this.token = new String(this.serverFinal, 33, 30, StandardCharsets.UTF_8);
    }
}
