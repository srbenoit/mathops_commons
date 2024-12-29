package dev.mathops.commons.model;

import java.io.Serial;

/**
 * An exception that indicates a String representation of a typed object could not be parsed into an instance of that
 * object.
 */
public class StringParseException extends Exception {

    /** Version for serialization. */
    @Serial
    private static final long serialVersionUID = 1213910395091128836L;

    /**
     * Constructs a new {@code StringParseException}.
     */
    public StringParseException() {

        super();
    }

    /**
     * Constructs a new {@code StringParseException} with a specified message.
     *
     * @param message the message
     */
    public StringParseException(final String message) {

        super(message);
    }

    /**
     * Constructs a new {@code StringParseException} with a specified cause.
     *
     * @param cause the cause
     */
    public StringParseException(final Throwable cause) {

        super(cause);
    }

    /**
     * Constructs a new {@code StringParseException} with specified message and cause.
     *
     * @param message the message
     * @param cause   the cause
     */
    public StringParseException(final String message, final Throwable cause) {

        super(message, cause);
    }
}
