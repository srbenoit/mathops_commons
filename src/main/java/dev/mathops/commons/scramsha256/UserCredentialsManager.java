package dev.mathops.commons.scramsha256;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A single-instance manager for user credentials.
 */
public final class UserCredentialsManager {

    /** Map from normalized username to credentials. */
    private final Map<String, UserCredentials> credentials;

    /**
     * Constructs a new {@code UserCredentialsManager}.
     */
    public UserCredentialsManager() {

        this.credentials = new HashMap<>(10);
    }

    /**
     * Adds a credential.
     *
     * @param cred the credential to add
     */
    public void addCredential(final UserCredentials cred) {

        this.credentials.put(new String(cred.normalizedUsername, StandardCharsets.UTF_8), cred);
    }

    /**
     * Looks up credentials for a username.
     *
     * @param normalizedUsername the normalized username
     * @return the credentials if found; null if not
     */
    UserCredentials getCredentials(final String normalizedUsername) {

        return this.credentials.get(normalizedUsername);
    }
}
