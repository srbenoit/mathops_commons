package dev.mathops.commons.scramsha256;

import dev.mathops.commons.builder.SimpleBuilder;

/**
 * A pending authentication.
 */
final class PendingAuthentication {

    /** The user credentials. */
    final UserCredentials credentials;

    /** The client-first message. */
    final ClientFirstMessage clientFirst;

    /** The server-first message. */
    final ServerFirstMessage serverFirst;

    /** The time when this request times out. */
    final long timeout;

    /**
     * Constructs a new {@code PendingAuthentication}.
     *
     * @param theCredentials the user credentials
     * @param theClientFirst the client-first message
     * @param theServerFirst the server-first message
     * @param theTimeout     the timeout
     */
    PendingAuthentication(final UserCredentials theCredentials, final ClientFirstMessage theClientFirst,
                          final ServerFirstMessage theServerFirst, final long theTimeout) {

        this.credentials = theCredentials;
        this.clientFirst = theClientFirst;
        this.serverFirst = theServerFirst;
        this.timeout = theTimeout;
    }

    /**
     * Generates a string representation of this object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        return SimpleBuilder.concat("PendingAuthentication{credentials='", this.credentials, "}");
    }
}
