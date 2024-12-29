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

package dev.mathops.commons.model;

/**
 * A convenience subclass of {@code TypedKey} whose category is PROPERTY, to make construction of those objects simpler
 * (it is expected that there will be many such objects).
 *
 * @param <T> the value data type
 */
public final class PropKey<T> extends TypedKey<T> {

    /**
     * Constructs a new {@code PropKey}.
     *
     * @param theName  the value name
     * @param theCodec the codec
     * @throws IllegalArgumentException if category, name, or value class is null, or if the name is not valid.
     */
    public PropKey(final String theName, final Codec<T> theCodec) throws IllegalArgumentException {

        super(ETypedMapCategory.PROPERTY, theName, theCodec);
    }
}
