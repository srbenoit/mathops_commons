package dev.mathops.commons.scramsha256;

import java.util.Base64;
import java.util.random.RandomGenerator;

/**
 * The "client-first" message in a SCRAM-SHA-256 authentication interchange.
 *
 * <pre>
 * NORM_USERNAME = Normalize(username)
 * CNONCE        = Random 30-byte nonce
 * CLIENT_FIRST  = "n,,n=" + NORM_USERNAME + ",r=" + CNONCE
 * </pre>
 */
final class ClientFirstMessage {

    /** The normalized username. */
    final byte[] normalizedUsername;

    /** The client nonce (30 bytes). */
    final byte[] cNonce;

    /** The raw 'client-first' message. */
    final byte[] clientFirst;

    /** The base-64 encoding of the assembled 'client-first' message. */
    final byte[] base64;

    /**
     * Constructs a new {@code ClientFirstMessage}.
     *
     * @param username the username
     * @param rnd      a source of random numbers
     * @throws IllegalArgumentException of either argument is null or the username is empty
     */
    ClientFirstMessage(final CharSequence username, final RandomGenerator rnd) {

        if (username == null) {
            throw new IllegalArgumentException("Username may not be null");
        }
        if (rnd == null) {
            throw new IllegalArgumentException("Random source may not be null");
        }

        this.normalizedUsername = ScramUtils.normalize(username);
        final int nameLen = this.normalizedUsername.length;
        if (nameLen < 1) {
            throw new IllegalArgumentException("Username may not be empty");
        }

        this.cNonce = new byte[30];
        rnd.nextBytes(this.cNonce);

        this.clientFirst = new byte[38 + nameLen];
        this.clientFirst[0] = 'n';
        this.clientFirst[1] = ',';
        this.clientFirst[2] = ',';
        this.clientFirst[3] = 'n';
        this.clientFirst[4] = '=';

        System.arraycopy(this.normalizedUsername, 0, this.clientFirst, 5, nameLen);
        this.clientFirst[5 + nameLen] = ',';
        this.clientFirst[6 + nameLen] = 'r';
        this.clientFirst[7 + nameLen] = '=';
        System.arraycopy(this.cNonce, 0, this.clientFirst, 8 + nameLen, 30);

        this.base64 = Base64.getEncoder().encode(this.clientFirst);
    }

    /**
     * Parses a {@code ClientFirstMessage} from its base64 representation.
     *
     * @param theBase64 the Base64 to parse
     * @throws IllegalArgumentException if the message format is not valid
     */
    ClientFirstMessage(final byte[] theBase64) throws IllegalArgumentException {

        this.base64 = theBase64.clone();
        this.clientFirst = Base64.getDecoder().decode(theBase64);

        final int len = this.clientFirst.length;
        if (len < 38) {
            throw new IllegalArgumentException("Invalid message data");
        }

        if (this.clientFirst[0] == 'n' && this.clientFirst[1] == ',' && this.clientFirst[2] == ','
            && this.clientFirst[3] == 'n' && this.clientFirst[4] == '=' && this.clientFirst[len - 33] == ','
            && this.clientFirst[len - 32] == 'r' && this.clientFirst[len - 31] == '=') {

            this.normalizedUsername = new byte[len - 38];
            this.cNonce = new byte[30];

            System.arraycopy(this.clientFirst, 5, this.normalizedUsername, 0, len - 38);
            System.arraycopy(this.clientFirst, len - 30, this.cNonce, 0, 30);
        } else {
            throw new IllegalArgumentException("Invalid message data");
        }
    }
}
