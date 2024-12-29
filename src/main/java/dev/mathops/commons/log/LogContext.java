package dev.mathops.commons.log;

import dev.mathops.commons.CoreConstants;

/**
 * A logging context that includes the host, path, remote IP address, and session associated with a log. This is stored
 * in a thread-local at the time a thread begins processing a transaction, and is cleared at the end of that transaction
 * so the thread may be cached and re-used.
 */
class LogContext {

    /** The column at which to make messages align vertically. */
    private static final int MESSAGE_INDENT = 27;

    /** A character used in log messages. */
    private static final char COLON = ':';

    /** The remote address. */
    private final String remoteAddress;

    /** The log session ID. */
    private String sessionId = null;

    /** The log user ID. */
    private String userId = null;

    /**
     * Constructs a new {@code LogContext}.
     *
     * @param theRemoteAddr the remote address
     */
    LogContext(final String theRemoteAddr) {

        this.remoteAddress = theRemoteAddr;
    }

    /**
     * Sets the session ID and user ID in the context.
     *
     * @param theSessionId the session ID
     * @param theUserId    the user ID
     */
    final void setSession(final String theSessionId, final String theUserId) {

        this.sessionId = theSessionId;
        this.userId = theUserId;
    }

    /**
     * Sets the session ID in the context.
     *
     * @param theSessionId the session ID
     */
    public final void setSessionId(final String theSessionId) {

        this.sessionId = theSessionId;
    }

    /**
     * Gets the session ID in the context.
     *
     * @return the session ID
     */
    public final String getSessionId() {

        return this.sessionId;
    }

    /**
     * Sets the user ID in the context.
     *
     * @param theUserId the user ID
     */
    public final void setUserId(final String theUserId) {

        this.userId = theUserId;
    }

    /**
     * Gets the user ID in the context.
     *
     * @return the user ID
     */
    public final String getUserId() {

        return this.userId;
    }

    /**
     * Generates a string representation of the log context.
     *
     * @return the string representation (25 characters minimum)
     */
    @Override
    public final String toString() {

        final StringBuilder builder = new StringBuilder(20);

        // user ID should not exceed 9, and IP address should not exceed 15, so this string should
        // never exceed 25 including the ':'. We pad all to 27 to make log messages line up.

        builder.append(this.remoteAddress);

        if (this.userId != null) {
            builder.append(COLON).append(this.userId);
        }

        while (builder.length() < MESSAGE_INDENT) {
            builder.append(CoreConstants.SPC);
        }

        return builder.toString();
    }
}
