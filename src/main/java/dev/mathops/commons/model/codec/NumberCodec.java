package dev.mathops.commons.model.codec;

import dev.mathops.commons.model.Codec;
import dev.mathops.commons.model.StringParseException;
import dev.mathops.commons.number.BigIrrational;
import dev.mathops.commons.number.BigRational;
import dev.mathops.commons.number.Irrational;
import dev.mathops.commons.number.Rational;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A codec for general {@code Number} objects.
 */
public enum NumberCodec implements Codec<Number> {

    /** The single instance. */
    INST;

    /** A character that can appear in the string representation of a number. */
    private static final int SLASH = (int) '/';

    /** A character that can appear in the string representation of a number. */
    private static final int DOT = (int) '.';

    /** A suffix that indicates an int type. */
    private static final int INT_SUFFIX = (int) 'i';

    /** A suffix that indicates a short type. */
    private static final int SHORT_SUFFIX = (int) 's';

    /** A suffix that indicates a byte type. */
    private static final int BYTE_SUFFIX = (int) 'b';

    /** A suffix that indicates a Big type. */
    private static final int BIG_SUFFIX = (int) 'B';

    /** A suffix that indicates a float type. */
    private static final int FLOAT_SUFFIX = (int) 'f';

    /** A string that can stand for the string "PI". */
    private static final String PI_CHAR = "\u03C0";

    /** A string that represents the constant PI. */
    private static final String PI_STR = "PI";

    /** A character whose presence indicates an irrational type. */
    private static final int P_CHAR = (int) 'P';

    /** A character whose presence indicates an irrational type. */
    private static final int E_CHAR = (int) 'E';

    /** A character whose presence indicates an irrational type. */
    private static final int R_CHAR = (int) 'R';

    /**
     * Gets the type of object this codec converts.
     *
     * @return the type
     */
    public Class<Number> getType() {

        return Number.class;
    }

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * <p>
     * Supported formats include:
     *
     * <pre>
     * 123b         Bytes - parsed using "Byte.valueOf()"
     * 12345s       Shorts - parsed using "Short.valueOf()"
     * 12345i       Integers - parsed using "Integer.valueOf()"
     * 12345        Long Integers - parsed using "Long.valueOf()"
     * 99...99B     BigInteger - parsed using "new BigInteger()"
     * 123.456f     Floats  - parsed using "Float.valueOf()"
     * 123.456      Doubles  - parsed using "Double.valueOf()"
     * 99...99B     BigDecimal - parsed using "new BigDecimal()"
     * 123/17       Rationals - numerator and denominator parsed using "Long.valueOf()"
     * 123/17B      BigRationals - numerator and denominator parsed using "new BigInteger()"
     * The formats below are Irrationals, parsed using "Irrational.valueOf()":
     * 3PI/4        Any string with "PI" is treated as a fraction with PI in the numerator.  There can be a long
     *              integer coefficient before PI, and optionally a denominator made up of a slash character
     *              followed by a long integer.  No characters between the PI and slash are allowed.
     * 3E/4         Same format as above, but with the constant E rather than PI.
     * 3R2/2        Any string with "R" is treated as a rational multiple of the square root of an integer.  The
     *              format has an optional long integer coefficient before the R, a required positive long integer
     *              after the R (whose square root is to be taken), and an optional denominator consisting of a
     *              slash character then a long integer.
     * Any irrational format above with "B" appended represents a BigIrrational.
     * In all irrational formats above, the Unicode PI character (U+03C0) is treated as "PI".
     * </pre>
     *
     * @param str the string to parse
     * @return the parsed instance
     * @throws IllegalArgumentException if {@code str} is {@code null}
     * @throws StringParseException     if the string cannot be parsed as an instance of the class
     */
    public Number parse(final String str) throws IllegalArgumentException, StringParseException {

        if (str == null) {
            final String msg = Res.get(Res.NULL_STRING);
            throw new IllegalArgumentException(msg);
        }

        final Number result;

        final int len = str.length();
        if (len == 0) {
            throw new StringParseException("Attempt to parse number from empty string");
        }
        final int last = (int) str.charAt(len - 1);

        final int slash = str.indexOf(SLASH); // A slash indicates a Rational or BigRational

        try {
            if (slash == -1) {
                final int dot = str.indexOf(DOT); // A dot indicates a Float, Double, or BigDecimal

                if (dot == -1) {

                    // There is no '/' and no '.'; this could be (in increasing order of likelihood):
                    // - A long integer, integer (has i suffix), short integer (has s suffix), or byte (has b suffix)
                    // - An irrational such as "2PI", "E", or "3R2" (if it contains "PI", "E", or "R")
                    // - A BigInteger (has 'B' suffix)
                    // - A BigIrrational (has 'B' suffix)

                    if (last == INT_SUFFIX) {
                        final String substring = str.substring(0, len - 1);
                        result = Integer.valueOf(substring);
                    } else if (last == SHORT_SUFFIX) {
                        final String substring = str.substring(0, len - 1);
                        result = Short.valueOf(substring);
                    } else if (last == BYTE_SUFFIX) {
                        final String substring = str.substring(0, len - 1);
                        result = Byte.valueOf(substring);
                    } else if (last == BIG_SUFFIX) {
                        final String toParse = str.substring(0, len - 1);

                        final int letter1 = toParse.indexOf(P_CHAR);
                        final int letter2 = toParse.indexOf(E_CHAR);
                        final int letter3 = toParse.indexOf(R_CHAR);

                        if (letter1 >= 0 || letter2 >= 0 || letter3 >= 0) {
                            result = BigIrrational.valueOf(toParse);
                        } else {
                            final int letter4 = toParse.indexOf(PI_CHAR);
                            if (letter4 >= 0) {
                                final String fixed = toParse.replace(PI_CHAR, PI_STR);
                                result = BigIrrational.valueOf(fixed);
                            } else {
                                // String has no letters or PI symbol
                                result = new BigInteger(toParse);
                            }
                        }
                    } else {
                        final int letter1 = str.indexOf(P_CHAR);
                        final int letter2 = str.indexOf(E_CHAR);
                        final int letter3 = str.indexOf(R_CHAR);

                        if (letter1 >= 0 || letter2 >= 0 || letter3 >= 0) {
                            result = Irrational.valueOf(str);
                        } else {
                            final int letter4 = str.indexOf(PI_CHAR);
                            if (letter4 >= 0) {
                                final String fixed = str.replace(PI_CHAR, PI_STR);
                                result = Irrational.valueOf(fixed);
                            } else {
                                // String has no letters or PI symbol
                                result = Long.valueOf(str);
                            }
                        }
                    }

                } else {

                    // There is a '.' but no '/'; this could be (in increasing order of likelihood):
                    // - A double or float (has 'f' suffix)
                    // - A BigDecimal (has "B" suffix)

                    if (last == FLOAT_SUFFIX) {
                        result = Float.valueOf(str);
                    } else if (last == BIG_SUFFIX) {
                        result = new BigDecimal(str);
                    } else {
                        result = Double.valueOf(str);
                    }
                }
            } else {

                // There is a '/'; this could be (in increasing order of likelihood):
                // - A Rational, like 1/2
                // - An Irrational like PI/2, 2E/4, or 3R2/2
                // - A BigRational (has 'B' suffix)
                // - A BigIrrational (has 'B' suffix)

                if (last == BIG_SUFFIX) {
                    final String toParse = str.substring(0, len - 1);

                    final int letter1 = toParse.indexOf(P_CHAR);
                    final int letter2 = toParse.indexOf(E_CHAR);
                    final int letter3 = toParse.indexOf(R_CHAR);

                    if (letter1 >= 0 || letter2 >= 0 || letter3 >= 0) {
                        result = BigIrrational.valueOf(toParse);
                    } else {
                        final int letter4 = str.indexOf(PI_CHAR);
                        if (letter4 >= 0) {
                            final String fixed = toParse.replace(PI_CHAR, PI_STR);
                            result = BigIrrational.valueOf(fixed);
                        } else {
                            // String has no letters or PI symbol
                            result = BigRational.valueOf(toParse);
                        }
                    }

                } else {
                    final int letter1 = str.indexOf(P_CHAR);
                    final int letter2 = str.indexOf(E_CHAR);
                    final int letter3 = str.indexOf(R_CHAR);

                    if (letter1 >= 0 || letter2 >= 0 || letter3 >= 0) {
                        result = Irrational.valueOf(str);
                    } else {
                        final int letter4 = str.indexOf(PI_CHAR);
                        if (letter4 >= 0) {
                            final String fixed = str.replace(PI_CHAR, PI_STR);
                            result = Irrational.valueOf(fixed);
                        } else {
                            // String has no letters or PI symbol
                            result = Rational.valueOf(str);
                        }
                    }
                }
            }
        } catch (final NumberFormatException ex) {
            throw new StringParseException(ex);
        }

        return result;

    }

    /**
     * Generates a String representation of the object.
     *
     * @param obj the object to stringify
     * @return the string representation
     * @throws IllegalArgumentException if {@code obj} is {@code null} or an unsupported number type
     */
    public String stringify(final Number obj) throws IllegalArgumentException {

        if (obj == null) {
            final String msg = Res.get(Res.NULL_OBJECT);
            throw new IllegalArgumentException(msg);
        }

        final String str;

        if (obj instanceof Long || obj instanceof Double || obj instanceof Rational || obj instanceof Irrational) {
            str = obj.toString();
        } else if (obj instanceof Byte) {
            str = obj + "b";
        } else if (obj instanceof Short) {
            str = obj + "s";
        } else if (obj instanceof Integer) {
            str = obj + "i";
        } else if (obj instanceof Float) {
            str = obj + "f";
        } else if (obj instanceof BigInteger || obj instanceof BigDecimal || obj instanceof BigRational
                   || obj instanceof BigIrrational) {
            str = obj + "B";
        } else {
            throw new IllegalArgumentException("Attempt to stringify unsupported Number type");
        }

        return str;
    }
}