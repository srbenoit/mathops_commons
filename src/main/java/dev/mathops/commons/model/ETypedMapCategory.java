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

/**
 * Categories of value that can be specified in a {@code TypedMap}.
 */
public enum ETypedMapCategory {

    /** Attribute (persisted). */
    ATTRIBUTE,

    /** Property (ephemeral, not persisted). */
    PROPERTY,

    /** Data (controls persistence, may include data that is persisted). */
    DATA,

    /** Nodes (used to store child nodes - have no string representation). */
    NODE
}
