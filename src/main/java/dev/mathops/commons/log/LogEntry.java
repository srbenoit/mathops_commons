package dev.mathops.commons.log;

/**
 * An entry in the in-memory log.
 */
public final class LogEntry {

    /** The log message. */
    private final String message;

    /** The timestamp when the message was logged. */
    private final long millis;

    /**
     * Constructs a new {@code LogEntry}.
     *
     * @param logMsg the log message
     */
    LogEntry(final String logMsg) {

        this.message = logMsg;
        this.millis = System.currentTimeMillis();
    }

    /**
     * Gets the log message.
     *
     * @return the log message
     */
    public String getMessage() {

        return this.message;
    }

    /**
     * Gets the timestamp when the message was logged.
     *
     * @return the timestamp
     */
    public long getMillis() {

        return this.millis;
    }
}
