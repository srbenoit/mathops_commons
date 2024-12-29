package dev.mathops.commons.model.codec;

import dev.mathops.commons.model.Codec;
import dev.mathops.commons.model.StringParseException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * A codec for general {@code LocalDate} objects.
 */
public enum LocalDateCodec implements Codec<LocalDate> {

    /** The single instance. */
    INST;

    /**
     * Gets the type of object this codec converts.
     *
     * @return the type
     */
    public Class<LocalDate> getType() {

        return LocalDate.class;
    }

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * @param str the string to parse
     * @return the parsed instance
     * @throws IllegalArgumentException if {@code str} is {@code null} or cannot be parsed as an instance of the class
     */
    public LocalDate parse(final String str) throws IllegalArgumentException, StringParseException {

        if (str == null) {
            final String msg = Res.get(Res.NULL_STRING);
            throw new IllegalArgumentException(msg);
        }

        try {
            return LocalDate.parse(str);
        } catch (final DateTimeParseException ex) {
            throw new StringParseException(ex);
        }
    }

    /**
     * Generates a String representation of the object.
     *
     * @param obj the object to stringify
     * @return the string representation
     * @throws IllegalArgumentException if {@code obj} is {@code null}
     */
    public String stringify(final LocalDate obj) throws IllegalArgumentException {

        if (obj == null) {
            final String msg = Res.get(Res.NULL_OBJECT);
            throw new IllegalArgumentException(msg);
        }

        return obj.toString();
    }
}