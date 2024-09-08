package dev.mathops.commons.scramsha256;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.builder.SimpleBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.random.RandomGenerator;

/**
 * The data that makes up the "server-first" (or "server-error") message.
 *
 * <pre>
 * CNONCE       = Data from 'client-first' message
 * SNONCE       = Random 30-byte nonce
 * SALT         = Salt value from credentials associated with 'username' from 'client-first'
 * ITER_COUNT   = Iteration count from credentials associated with 'username' from 'client-first'
 * SERVER_FIRST = CNONCE[30] + SNONCE[30] + "," + SALT[30] + ",i=" + ITER_COUNT[4]
 * </pre>
 */
public final class ServerFirstMessage {

    /** The server nonce (30 bytes). */
    final byte[] sNonce;

    /** The salt (30 bytes). */
    final byte[] salt;

    /** The iteration count (from 4096 to 99999). */
    final int iterCount;

    /** An error message, if this is a "server-error". */
    final byte[] error;

    /** The raw message. */
    final byte[] serverFirst;

    /** The base-64 encoding of the assembled 'server-first' message. */
    final byte[] base64;

    /** The token that will ultimately be assigned if authentication succeeds. */
    final String token;

    /**
     * Parses a {@code ServerFirstMessage} from a client-first message and the user credentials associated with the
     * username in that message.
     *
     * @param theClientFirst the client-first message
     * @param theCredentials the user credentials
     * @param rnd            a source of random numbers
     * @throws IllegalArgumentException if either argument is null or the credentials do not match the username in the
     *                                  client-first message
     */
    ServerFirstMessage(final ClientFirstMessage theClientFirst, final UserCredentials theCredentials,
                       final RandomGenerator rnd) throws IllegalArgumentException {

        if (theClientFirst == null) {
            throw new IllegalArgumentException("ClientFirst message may not be null");
        }
        if (theCredentials == null) {
            throw new IllegalArgumentException("User credentials may not be null");
        }
        if (rnd == null) {
            throw new IllegalArgumentException("Random source may not be null");
        }
        if (theCredentials.normalizedUsername == null) {
            throw new IllegalArgumentException("User credentials did not contain normalized username");
        }
        if (theCredentials.salt == null) {
            throw new IllegalArgumentException("User credentials did not contain salt");
        }
        if (theClientFirst.normalizedUsername == null) {
            throw new IllegalArgumentException("Client first message did not contain normalized username");
        }
        if (theClientFirst.cNonce == null) {
            throw new IllegalArgumentException("Client first message did not contain client NONCE");
        }
        if (!Arrays.equals(theCredentials.normalizedUsername, theClientFirst.normalizedUsername)) {
            throw new IllegalArgumentException("User name in credentials does not match that in client-first");
        }

        this.sNonce = new byte[30];
        rnd.nextBytes(this.sNonce);

        this.salt = new byte[24];
        System.arraycopy(theCredentials.salt, 0, this.salt, 0, 24);

        this.iterCount = theCredentials.iterCount;
        this.error = ScramUtils.ZERO_BYTES;

        this.serverFirst = new byte[96];
        this.serverFirst[0] = 'r';
        this.serverFirst[1] = '=';
        System.arraycopy(theClientFirst.cNonce, 0, this.serverFirst, 2, 30);
        System.arraycopy(this.sNonce, 0, this.serverFirst, 32, 30);
        this.serverFirst[62] = ',';
        this.serverFirst[63] = 's';
        this.serverFirst[64] = '=';
        System.arraycopy(this.salt, 0, this.serverFirst, 65, 24);
        this.serverFirst[89] = ',';
        this.serverFirst[90] = 'i';
        this.serverFirst[91] = '=';
        this.serverFirst[92] = (byte) ('0' + this.iterCount / 1000);
        this.serverFirst[93] = (byte) ('0' + this.iterCount % 1000 / 100);
        this.serverFirst[94] = (byte) ('0' + this.iterCount % 100 / 10);
        this.serverFirst[95] = (byte) ('0' + this.iterCount % 10);

        this.base64 = Base64.getEncoder().encode(this.serverFirst);
        this.token = CoreConstants.newId(30);
    }

    /**
     * Parses a {@code ServerFirstMessage} that represents an error.
     *
     * @param theError the error message
     * @throws IllegalArgumentException if the error message is null or blank
     */
    ServerFirstMessage(final String theError) throws IllegalArgumentException {

        if (theError == null || theError.isBlank()) {
            throw new IllegalArgumentException("Error message may not be null or blank");
        }

        this.sNonce = ScramUtils.ZERO_BYTES;
        this.salt = ScramUtils.ZERO_BYTES;
        this.iterCount = 0;
        this.error = theError.getBytes(StandardCharsets.UTF_8);

        this.serverFirst = new byte[this.error.length + 2];
        this.serverFirst[0] = 'e';
        this.serverFirst[1] = '=';
        System.arraycopy(this.serverFirst, 2, this.error, 0, this.error.length);

        this.base64 = Base64.getEncoder().encode(this.serverFirst);
        this.token = CoreConstants.EMPTY;
    }

    /**
     * Constructs a {@code ServerFirstMessage} from a hex representation returned by the server in response to the
     * 'client-first' message, and verifies that its format is correct and that its client nonce matches that from the
     * 'client-first' message, to ensure it should be associated with that request.
     *
     * @param theBase64   the base-64 to parse
     * @param clientFirst the {@code ClientFirstMessage} message
     * @throws IllegalArgumentException if there is an error in the message
     */
    ServerFirstMessage(final byte[] theBase64, final ClientFirstMessage clientFirst) throws IllegalArgumentException {

        this.base64 = theBase64.clone();
        this.serverFirst = Base64.getDecoder().decode(this.base64);
        final int len = this.serverFirst.length;

        if (len > 2 && (int) this.serverFirst[0] == (int) 'e' && (int) this.serverFirst[1] == (int) '=') {
            this.error = new byte[len - 2];
            System.arraycopy(this.serverFirst, 2, this.error, 0, len - 2);

            this.sNonce = ScramUtils.ZERO_BYTES;
            this.salt = ScramUtils.ZERO_BYTES;
            this.iterCount = 0;
            this.token = CoreConstants.EMPTY;
        } else {
            if (this.serverFirst.length != 96) {
                throw new IllegalArgumentException("server-first message had invalid length: "
                                                   + this.serverFirst.length);
            }

            if ((int) this.serverFirst[0] == 'r' && (int) this.serverFirst[1] == '='
                && (int) this.serverFirst[62] == ',' && (int) this.serverFirst[63] == 's'
                && (int) this.serverFirst[64] == '=' && (int) this.serverFirst[89] == ','
                && (int) this.serverFirst[90] == 'i' && (int) this.serverFirst[91] == '='
                && (int) this.serverFirst[92] >= '0' && (int) this.serverFirst[92] <= '9'
                && (int) this.serverFirst[93] >= '0' && (int) this.serverFirst[93] <= '9'
                && (int) this.serverFirst[94] >= '0' && (int) this.serverFirst[94] <= '9'
                && (int) this.serverFirst[95] >= '0' && (int) this.serverFirst[95] <= '9') {

                // Verify that the client-nonce in the server's message matches ours
                for (int i = 0; i < 30; ++i) {
                    if ((int) this.serverFirst[2 + i] != (int) clientFirst.cNonce[i]) {
                        throw new IllegalArgumentException("server-first message had invalid client nonce");
                    }
                }
            } else {
                throw new IllegalArgumentException("server-first message had invalid delimiters");
            }

            this.sNonce = new byte[30];
            this.salt = new byte[24];

            System.arraycopy(this.serverFirst, 32, this.sNonce, 0, 30);
            System.arraycopy(this.serverFirst, 65, this.salt, 0, 24);

            this.iterCount = ((int) this.serverFirst[92] - '0') * 1000 + ((int) this.serverFirst[93] - '0') * 100
                             + ((int) this.serverFirst[94] - '0') * 10 + (int) this.serverFirst[95] - '0';
            this.error = ScramUtils.ZERO_BYTES;

            if (this.iterCount < 4096) {
                throw new IllegalArgumentException("server-first message had invalid iteration count: "
                                                   + this.iterCount);
            }
            this.token = CoreConstants.EMPTY;
        }
    }

    /**
     * Generates a string representation of this object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final String msgStr = new String(this.serverFirst, StandardCharsets.UTF_8);
        final String base64Str = new String(this.base64, StandardCharsets.UTF_8);

        return SimpleBuilder.concat("ServerFirstMessage{encoded=", msgStr, ", base64=", base64Str, "}");
    }
}
