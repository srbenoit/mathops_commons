/*
 * Copyright (C) 2022 Steve Benoit
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the  License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU  General Public License for more
 * details.
 *
 *  You should have received a copy of the GNU General Public License along with this program. If  not, see
 * <https://www.gnu.org/licenses/>.
 */

package dev.mathops.commons.number;

import dev.mathops.commons.res.ResBundle;

/**
 * Localized resources.
 */
final class Res extends ResBundle {

    // Used by Rational, BigRational

    /** A resource key. */
    static final String ZERO_DENOMINATOR = key(1);

    /** A resource key. */
    static final String NULL_NUMERATOR_DENOMINATOR = key(2);

    /** A resource key. */
    static final String RECIPROCAL_OF_ZERO = key(3);

    // Used by Irrational, BigIrrational

    /** A resource key. */
    static final String NULL_FACTOR = key(4);

    /** A resource key. */
    static final String ROOT_FACTOR_WITHOUT_BASE = key(5);

    /** A resource key. */
    static final String DENOMINATOR_NOT_POSITIVE = key(6);

    /** A resource key. */
    static final String BASE_ONLY_ON_ROOT_FACTOR = key(7);

    /** A resource key. */
    static final String BASE_NOT_POSITIVE = key(8);

    /** A resource key. */
    static final String BAD_RATIONAL_NUMBER = key(9);

    /** A resource key. */
    static final String UNSUPPORTED_FACTOR = key(10);

    // Used by NumberParser

    /** A resource key. */
    static final String ATTEMPTING_TO_PARSE_IRRATIONAL = key(11);

    //

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {ZERO_DENOMINATOR, "Denominator may not be zero."},
            {NULL_NUMERATOR_DENOMINATOR, "Numerator and denominator may not be null."},
            {RECIPROCAL_OF_ZERO, "Cannot take reciprocal of zero."},

            {NULL_FACTOR, "Factor may not be null."},
            {ROOT_FACTOR_WITHOUT_BASE, "Square root factor must include base."},
            {DENOMINATOR_NOT_POSITIVE, "Denominator must be positive, nonzero."},
            {BASE_ONLY_ON_ROOT_FACTOR, "Only square root factor may include base."},
            {BASE_NOT_POSITIVE, "Base must be positive, nonzero."},
            {BAD_RATIONAL_NUMBER, "Invalid irrational number: {0}"},
            {UNSUPPORTED_FACTOR, "Unsupported Irrational factor."},

            {ATTEMPTING_TO_PARSE_IRRATIONAL, "Attempting to parse [{0}] as irrational."},

    };

    /** The singleton instance. */
    private static final Res instance = new Res();

    /**
     * Private constructor to prevent direct instantiation.
     */
    private Res() {

        super(EN_US);
    }

    /**
     * Gets the message with a specified key using the current locale.
     *
     * @param key the message key
     * @return the best-matching message, an empty string if none is registered (never {@code null})
     */
    static String get(final String key) {

        return instance.getMsg(key);
    }

    /**
     * Retrieves the message with a specified key, then uses a {@code MessageFormat} to format that message pattern with
     * a collection of arguments.
     *
     * @param key       the message key
     * @param arguments the arguments, as for {@code MessageFormat}
     * @return the formatted string (never {@code null})
     */
    static String fmt(final String key, final Object... arguments) {

        return instance.formatMsg(key, arguments);
    }
}
