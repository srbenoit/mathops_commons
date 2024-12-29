package dev.mathops.commons.model.codec;

import dev.mathops.commons.model.Codec;
import dev.mathops.commons.model.StringParseException;

/**
 * A codec for general {@code Long} objects.
 */
public enum LongCodec implements Codec<Long> {

    /** The single instance. */
    INST;

    /**
     * Gets the type of object this codec converts.
     *
     * @return the type
     */
    public Class<Long> getType() {

        return Long.class;
    }

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * @param str the string to parse
     * @return the parsed instance
     * @throws IllegalArgumentException if {@code str} is {@code null} or cannot be parsed as an instance of the class
     */
    public Long parse(final String str) throws IllegalArgumentException, StringParseException {

        if (str == null) {
            final String msg = Res.get(Res.NULL_STRING);
            throw new IllegalArgumentException(msg);
        }

        try {
            return Long.valueOf(str);
        } catch (final NumberFormatException ex) {
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
    public String stringify(final Long obj) throws IllegalArgumentException {

        if (obj == null) {
            final String msg = Res.get(Res.NULL_OBJECT);
            throw new IllegalArgumentException(msg);
        }

        return obj.toString();
    }
}