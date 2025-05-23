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

package dev.mathops.commons.model.codec;

import dev.mathops.commons.res.ResBundle;

/**
 * Localized resources.
 */
final class Res extends ResBundle {

    /** A resource key. */
    static final String NULL_STRING = key(1);

    /** A resource key. */
    static final String NULL_OBJECT = key(2);

    /** A resource key. */
    static final String NO_STRING_REP = key(3);

    //

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {NULL_STRING, "String to parse may not be null"},
            {NULL_OBJECT, "Object to stringify may not be null"},
            {NO_STRING_REP, "Model tree nodes do not have string representations."},
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
}
