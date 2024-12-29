/*
 * Copyright (C) 2022 Steve Benoit
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the  License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU  General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If  not, see
 * <https://www.gnu.org/licenses/>.
 */

package dev.mathops.commons.log;

import java.text.MessageFormat;

/**
 * Debug levels to control class diagnostic logging.
 */
public enum EDebugLevel {

    /** No extra debug output. */
    NONE(0),

    /** Severe messages only. */
    SEVERE(1),

    /** Warning and severe messages only. */
    WARNINGS(2),

    /** Informational, warning, and severe messages only. */
    INFO(3),

    /** Configuration, informational, warning, and severe messages only. */
    CONFIG(4),

    /** Trace (enter/exit), Configuration, informational, warning, and severe messages only. */
    TRACE(5),

    /** Fine, Trace (enter/exit), Configuration, informational, warning, and severe messages only. */
    FINE(6),

    /** Finest, Fine, Trace (enter/exit), Configuration, informational, warning, and severe messages only. */
    FINEST(7);

    /** The debug level. */
    public final int level;

    /**
     * The debug level.
     *
     * @param theLevel the debug level
     */
    EDebugLevel(final int theLevel) {

        this.level = theLevel;
    }

    /**
     * Generates the string representation of the level,
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final String levelString = Integer.toString(this.level);

        return MessageFormat.format("EDebugLevel[level={0}]", levelString);
    }
}
