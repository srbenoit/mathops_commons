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

package dev.mathops.commons.model;

import dev.mathops.commons.res.ResBundle;

/**
 * Localized resources.
 */
final class Res extends ResBundle {

    /** A resource key. */
    static final String NULL_CATEGORY = key(1);

    /** A resource key. */
    static final String NULL_NAME = key(2);

    /** A resource key. */
    static final String NULL_VALUE_CLASS = key(3);

    /** A resource key. */
    static final String NULL_CODEC = key(4);

    //

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {NULL_CATEGORY, "Value category may not be null"},
            {NULL_NAME, "Name may not be null or blank"},
            {NULL_VALUE_CLASS, "Value class may not be null"},
            {NULL_CODEC, "Codec may not be null"},
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
