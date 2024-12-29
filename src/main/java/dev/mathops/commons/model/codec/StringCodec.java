package dev.mathops.commons.model.codec;

import dev.mathops.commons.model.Codec;

/**
 * An identity codec for use when the value type is a {@code String}.
 */
public enum StringCodec implements Codec<String> {

    /** The single instance. */
    INST;

    /**
     * Gets the type of object this codec converts.
     *
     * @return the type
     */
    public Class<String> getType() {

        return String.class;
    }

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * @return the input string
     * @throws IllegalArgumentException if {@code str} is {@code null}
     */
    public String parse(final String str) throws IllegalArgumentException {

        if (str == null) {
            final String msg = Res.get(Res.NULL_STRING);
            throw new IllegalArgumentException(msg);
        }

        return str;
    }

    /**
     * Generates a String representation of the object.
     *
     * @param obj the object to stringify
     * @return the input object
     * @throws IllegalArgumentException if {@code obj} is {@code null}
     */
    public String stringify(final String obj) throws IllegalArgumentException {

        if (obj == null) {
            final String msg = Res.get(Res.NULL_OBJECT);
            throw new IllegalArgumentException(msg);
        }

        return obj;
    }
}