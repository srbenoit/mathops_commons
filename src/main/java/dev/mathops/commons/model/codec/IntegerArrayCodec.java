package dev.mathops.commons.model.codec;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.model.Codec;
import dev.mathops.commons.model.StringParseException;

/**
 * A codec for arrays of {@code int}.
 */
public enum IntegerArrayCodec implements Codec<int[]> {

    /** The single instance. */
    INST;

    /** An empty array of integers. */
    private static final int[] EMPTY_INT_ARRAY = new int[0];

    /**
     * Gets the type of object this codec converts.
     *
     * @return the type
     */
    public Class<int[]> getType() {

        return int[].class;
    }

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * @param str the string to parse
     * @return the parsed instance
     * @throws IllegalArgumentException if {@code str} is {@code null} or cannot be parsed as an instance of the class
     */
    public int[] parse(final String str) throws IllegalArgumentException, StringParseException {

        if (str == null) {
            final String msg = Res.get(Res.NULL_STRING);
            throw new IllegalArgumentException(msg);
        }

        try {
            final int[] result;

            if (str.isBlank()) {
                result = EMPTY_INT_ARRAY;
            } else {
                final String[] parts = str.split(",");
                final int len = parts.length;
                result = new int[len];
                for (int i = 0; i < len; ++i) {
                    result[i] = Integer.parseInt(parts[i]);
                }
            }
            return result;
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
    public String stringify(final int[] obj) throws IllegalArgumentException {

        if (obj == null) {
            final String msg = Res.get(Res.NULL_OBJECT);
            throw new IllegalArgumentException(msg);
        }

        final String result;

        final int len = obj.length;
        if (len == 0) {
            result = CoreConstants.EMPTY;
        } else if (len == 1) {
            result = Integer.toString(obj[0]);
        } else {
            final StringBuilder builder = new StringBuilder(10 * len);
            builder.append(obj[0]);
            for (int i = 1; i < len; ++i) {
                builder.append(",");
                builder.append(obj[i]);
            }
            result = builder.toString();
        }

        return result;
    }
}