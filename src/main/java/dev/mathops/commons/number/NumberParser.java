/**************************************************************************************************
 * Copyright (C) 2024 Steve Benoit
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * <p>
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <<a href="https://www.gnu.org/licenses/">https://www.gnu.org/licenses/</a>>.
 *************************************************************************************************/
package dev.mathops.commons.number;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A utility class that can parse a number from a string, and that supports the {@code Rational}, {@code BigRational},
 * and {@code Irrational} numeric types, in addition to {@code Long}, {@code BigInteger}, and {@code Double}.
 *
 * <p>
 * Supported formats include:
 *
 * <pre>
 * 12345        Long Integers - parsed using "Long.valueOf()"
 * 99...99      BigInteger - parsed using "BigInteger.valueOf()"
 * 123.456      Doubles  - parsed using "Double.valueOf()"
 * 123/17       Rationals - numerator and denominator parsed using "Long.valueOf()"
 * 3PI/4        Any string with "PI" is treated as a fraction with PI in the numerator.  There can be a long integer
 *              coefficient before PI, and optionally a denominator made up of a slash character followed by a long
 *              integer.  No characters between the PI and slash are allowed.
 * 3E/4         Same format as above, but with the constant E rather than PI.
 * 3R2/2        Any string with "R" is treated as a rational multiple of the square root of an integer.  The format has
 *              an optional long integer coefficient before the R, a required positive long integer after the R (whose
 *              square root is to be taken), and an optional denominator consisting of a slash character then a long
 *              integer.
 * </pre>
 */
public enum NumberParser {
    ;

    /** A character that can appear in numeric representations. */
    static final int SLASH = (int) '/';

    /** A character that can appear in numeric representations. */
    static final int DOT = (int) '.';

    /**
     * Attempts to parse a number from a string.
     *
     * @param str the string
     * @return the number
     * @throws NumberFormatException if the string is not in a valid format
     */
    public static Number parse(final String str) throws NumberFormatException {

        Number result;

        final int slash = str.indexOf(SLASH);

        if (slash == -1) {
            final int dot = str.indexOf(DOT);

            if (dot == -1) {

                // There is no '/' character and no '.' character, so this could be (in increasing order of likelihood)
                // A long integer
                // An irrational such as "2PI", "E", or "3R2" (if it contains "PI", "E", or "R")
                // A BigInteger
                // A BigIrrational

                try {
                    result = Long.valueOf(str);
                } catch (final NumberFormatException ex1) {
                    try {
                        result = new BigInteger(str);
                    } catch (final NumberFormatException ex2) {
                        final String toParse = str.replace(Irrational.PI_STR, Irrational.PI);
//                        final String msg = Res.fmt(Res.ATTEMPTING_TO_PARSE_IRRATIONAL, toParse);
//                        Log.info(msg);

                        try {
                            result = Irrational.valueOf(toParse);
                        } catch (final NumberFormatException ex3) {
                            result = BigIrrational.valueOf(toParse);
                        }
                    }
                }

            } else {

                // There is a '.' character but no '/' character, so this could be (in increasing order of likelihood)
                // A double (if it contains a '.')
                // A BigDecimal (if it contains a '.')

                try {
                    result = Double.valueOf(str);
                } catch (final NumberFormatException ex1) {
                    result = new BigDecimal(str);
                }
            }
        } else {
            // There is a '/' character, so this could be (in increasing order of likelihood)
            // A Rational, like 1/2
            // An Irrational like PI/2, 2E/4, or 3R2/2
            // A BigRational
            // A BigIrrational

            try {
                result = Rational.valueOf(str);
            } catch (final NumberFormatException ex1) {
                try {
                    result = BigRational.valueOf(str);
                } catch (final NumberFormatException ex2) {
                    final String toParse = str.replace(Irrational.PI_STR, Irrational.PI);
//                    final String msg = Res.fmt(Res.ATTEMPTING_TO_PARSE_IRRATIONAL, toParse);
//                    Log.info(msg);

                    try {
                        result = Irrational.valueOf(toParse);
                    } catch (final NumberFormatException ex3) {
                        result = BigIrrational.valueOf(toParse);
                    }
                }
            }
        }

        return result;
    }
}
