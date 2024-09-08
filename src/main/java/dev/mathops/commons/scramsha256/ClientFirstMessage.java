package dev.mathops.commons.scramsha256;

import dev.mathops.commons.builder.SimpleBuilder;
import dev.mathops.commons.parser.HexEncoder;

import java.nio.charset.StandardCharsets;
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

    /** A commonly used byte array. */
    private static final byte[] LEADING = "n,,n=".getBytes(StandardCharsets.UTF_8);

    /** A commonly used byte array. */
    private static final byte[] MID = ",r=".getBytes(StandardCharsets.UTF_8);

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

        if (username == null || username.isEmpty()) {
            final String msg = Res.get(Res.CF_NO_USERNAME);
            throw new IllegalArgumentException(msg);
        }
        if (rnd == null) {
            final String msg = Res.get(Res.CF_NO_RANDOM);
            throw new IllegalArgumentException(msg);
        }

        this.normalizedUsername = ScramUtils.normalize(username);
        final int nameLen = this.normalizedUsername.length;
        if (nameLen < 1) {
            final String msg = Res.get(Res.CF_NO_USERNAME);
            throw new IllegalArgumentException(msg);
        }

        this.cNonce = new byte[ScramUtils.NONCE_LEN];
        rnd.nextBytes(this.cNonce);

        this.clientFirst = new byte[LEADING.length + nameLen + MID.length + ScramUtils.NONCE_LEN];
        System.arraycopy(LEADING, 0, this.clientFirst, 0, LEADING.length);
        int pos = LEADING.length;
        System.arraycopy(this.normalizedUsername, 0, this.clientFirst, pos, nameLen);
        pos += nameLen;
        System.arraycopy(MID, 0, this.clientFirst, pos, MID.length);
        pos += MID.length;
        System.arraycopy(this.cNonce, 0, this.clientFirst, pos, ScramUtils.NONCE_LEN);

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

        final int minLen = LEADING.length + 1 + MID.length + ScramUtils.NONCE_LEN;
        final int len = this.clientFirst.length;
        if (len < minLen) {
            final String msg = Res.get(Res.CF_BAD_MESSAGE);
            throw new IllegalArgumentException(msg);
        }

        final int midStart = len - ScramUtils.NONCE_LEN - MID.length;

        if (ScramUtils.isSame(LEADING, 0, this.clientFirst, 0, LEADING.length)
            && ScramUtils.isSame(MID, 0, this.clientFirst, midStart, MID.length)) {

            final int nameLen = len - LEADING.length - MID.length - ScramUtils.NONCE_LEN;
            this.normalizedUsername = new byte[nameLen];
            this.cNonce = new byte[ScramUtils.NONCE_LEN];

            System.arraycopy(this.clientFirst, LEADING.length, this.normalizedUsername, 0, nameLen);
            System.arraycopy(this.clientFirst, midStart + MID.length, this.cNonce, 0, ScramUtils.NONCE_LEN);
        } else {
            final String msg = Res.get(Res.CF_BAD_MESSAGE);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Generates a string representation of this object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final String usernameStr = new String(this.normalizedUsername, StandardCharsets.UTF_8);
        final String nonceStr = HexEncoder.encodeLowercase(this.cNonce);
        final String msgStr = new String(this.clientFirst, StandardCharsets.UTF_8);
        final String base64Str = new String(this.base64, StandardCharsets.UTF_8);

        return SimpleBuilder.concat("ClientFirstMessage{normalizedUsername='", usernameStr, "', cNonce=", nonceStr,
                ", encoded=", msgStr, ", base64=", base64Str, "}");
    }
}
