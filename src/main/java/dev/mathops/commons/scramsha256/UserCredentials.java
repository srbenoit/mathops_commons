package dev.mathops.commons.scramsha256;

import dev.mathops.commons.CoreConstants;

import java.nio.charset.StandardCharsets;

/**
 * Credentials associated with a username. These credentials are loaded from the "logins" table and carry the
 * information needed to perform SCRAM-SHA-256 authentication.
 */
public final class UserCredentials {

    /** A commonly used byte array. */
    private static final byte[] CLIENT_KEY_BYTES = "Client Key".getBytes(StandardCharsets.UTF_8);

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
     *                                  iterations is less than 4096 or greater than 99999
     */
    UserCredentials(final String theRole, final String theUsername, final byte[] theSalt, final byte[] theStoredKey,
                    final byte[] theServerKey, final int iterations) throws IllegalArgumentException {

        if (theRole == null || theRole.isEmpty()) {
            throw new IllegalArgumentException("Role may not be null or empty");
        }
        if (theUsername == null || theUsername.isEmpty()) {
            throw new IllegalArgumentException("Username may not be null or empty");
        }
        if (theSalt == null || theSalt.length != 24) {
            throw new IllegalArgumentException("24-byte salt must be provided");
        }
        if (theStoredKey == null || theStoredKey.length != 32) {
            throw new IllegalArgumentException("32-byte stored key must be provided");
        }
        if (theServerKey == null || theServerKey.length != 32) {
            throw new IllegalArgumentException("32-byte server key must be provided");
        }
        if (iterations < 4096 || iterations > 99999) {
            throw new IllegalArgumentException("Iterations must be in [4096, 99999]");
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
     *                                  iterations is less than 4096 or greater than 99999
     */
    UserCredentials(final String theRole, final String theUsername, final CharSequence password, final int iterations)
            throws IllegalArgumentException {

        if (theRole == null || theRole.isEmpty()) {
            throw new IllegalArgumentException("Role may not be null or empty");
        }
        if (theUsername == null || theUsername.isEmpty()) {
            throw new IllegalArgumentException("Username may not be null or empty");
        }
        if (iterations < 4096 || iterations > 99999) {
            throw new IllegalArgumentException("Iterations must be in [4096, 99999]");
        }

        this.role = theRole;
        this.username = theUsername;
        this.normalizedUsername = ScramUtils.normalize(theUsername);
        this.salt = CoreConstants.newId(24).getBytes(StandardCharsets.UTF_8);
        this.iterCount = iterations;

        final byte[] normalizedPassword = ScramUtils.normalize(password);
        final byte[] saltedPassword = ScramUtils.hi(normalizedPassword, this.salt, iterations);
        final byte[] clientKey = ScramUtils.hmac_sha_256(saltedPassword, CLIENT_KEY_BYTES);

        this.storedKey = ScramUtils.sha_256(clientKey);
        this.serverKey = ScramUtils.hmac_sha_256(saltedPassword, SERVER_KEY_BYTES);
    }
}
