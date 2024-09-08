package dev.mathops.commons.scramsha256;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.builder.SimpleBuilder;

import java.nio.charset.StandardCharsets;

/**
 * Credentials associated with a username. These credentials are loaded from the "logins" table and carry the
 * information needed to perform SCRAM-SHA-256 authentication.
 */
public final class UserCredentials {

    /** A commonly used byte array. */
    private static final byte[] CLIENT_KEY_BYTES = ScramUtils.CLIENT_KEY_STR.getBytes(StandardCharsets.UTF_8);

    /** A commonly used byte array. */
    private static final byte[] SERVER_KEY_BYTES = "Server Key".getBytes(StandardCharsets.UTF_8);

    /** The user's role. */
    public final String role;

    /** The username as provided. */
    public final String username;

    /** The normalized username. */
    final byte[] normalizedUsername;

    /** A 30-byte salt, randomly selected for each user. */
    public final byte[] salt;

    /** The iteration count (from 4096 to 9999). */
    final int iterCount;

    /** A 32-byte stored key. */
    final byte[] storedKey;

    /** A 32-byte server key. */
    final byte[] serverKey;

    /**
     * Constructs a new {@code UserCredentials}.
     *
     * @param theRole      the user's role
     * @param theUsername  the username
     * @param theSalt      the salt (24 bytes)
     * @param theStoredKey the stored key (32 bytes)
     * @param theServerKey the server key (32 bytes)
     * @param iterations   the number of iterations
     * @throws IllegalArgumentException if any argument is null, the username or password is empty, or the number of
     *                                  iterations is outside the allowed range
     */
    public UserCredentials(final String theRole, final String theUsername, final byte[] theSalt,
                           final byte[] theStoredKey, final byte[] theServerKey, final int iterations)
            throws IllegalArgumentException {

        if (theRole == null || theRole.isEmpty()) {
            final String msg = Res.get(Res.CRED_ROLE_NULL);
            throw new IllegalArgumentException(msg);
        }
        if (theUsername == null || theUsername.isEmpty()) {
            final String msg = Res.get(Res.CRED_USERNAME_NULL);
            throw new IllegalArgumentException(msg);
        }
        if (theSalt == null || theSalt.length != ScramUtils.SALT_LEN) {
            final String msg = Res.get(Res.CRED_BAD_SALT);
            throw new IllegalArgumentException(msg);
        }
        if (theStoredKey == null || theStoredKey.length != ScramUtils.STORED_KEY_LEN) {
            final String msg = Res.get(Res.CRED_BAD_STORED_KEY);
            throw new IllegalArgumentException(msg);
        }
        if (theServerKey == null || theServerKey.length != ScramUtils.SERVER_KEY_LEN) {
            final String msg = Res.get(Res.CRED_BAD_SERVER_KEY);
            throw new IllegalArgumentException(msg);
        }
        if (iterations < ScramUtils.MIN_ITERATIONS || iterations > ScramUtils.MAX_ITERATIONS) {
            final String msg = Res.get(Res.CRED_BAD_ITERATION_COUNT);
            throw new IllegalArgumentException(msg);
        }

        this.role = theRole;
        this.username = theUsername;
        this.normalizedUsername = ScramUtils.normalize(theUsername);
        this.salt = theSalt.clone();
        this.iterCount = iterations;
        this.storedKey = theStoredKey.clone();
        this.serverKey = theServerKey.clone();
    }

    /**
     * Constructs a new {@code UserCredentials} with randomly generated salt.  This constructs the stored key and server
     * key that can then be cached.
     *
     * @param theRole     the user's role
     * @param theUsername the username
     * @param password    the password
     * @param iterations  the number of iterations
     * @throws IllegalArgumentException if any argument is null or the username or password is empty, or the number of
     *                                  iterations is outside the allowed range
     */
    public UserCredentials(final String theRole, final String theUsername, final CharSequence password,
                           final int iterations)
            throws IllegalArgumentException {

        if (theRole == null || theRole.isEmpty()) {
            final String msg = Res.get(Res.CRED_ROLE_NULL);
            throw new IllegalArgumentException(msg);
        }
        if (theUsername == null || theUsername.isEmpty()) {
            final String msg = Res.get(Res.CRED_USERNAME_NULL);
            throw new IllegalArgumentException(msg);
        }
        if (iterations < ScramUtils.MIN_ITERATIONS || iterations > ScramUtils.MAX_ITERATIONS) {
            final String msg = Res.get(Res.CRED_BAD_ITERATION_COUNT);
            throw new IllegalArgumentException(msg);
        }

        this.role = theRole;
        this.username = theUsername;
        this.normalizedUsername = ScramUtils.normalize(theUsername);
        this.salt = CoreConstants.newId(ScramUtils.SALT_LEN).getBytes(StandardCharsets.UTF_8);
        this.iterCount = iterations;

        final byte[] normalizedPassword = ScramUtils.normalize(password);
        final byte[] saltedPassword = ScramUtils.hi(normalizedPassword, this.salt, iterations);
        final byte[] clientKey = ScramUtils.hmac_sha_256(saltedPassword, CLIENT_KEY_BYTES);

        this.storedKey = ScramUtils.sha_256(clientKey);
        this.serverKey = ScramUtils.hmac_sha_256(saltedPassword, SERVER_KEY_BYTES);
    }

    /**
     * Generates a string representation of this object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        return SimpleBuilder.concat("UserCredentials{role='", this.role, "', username='", this.username, "}");
    }
}
