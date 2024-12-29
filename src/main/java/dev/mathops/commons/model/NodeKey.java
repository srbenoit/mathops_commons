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

import dev.mathops.commons.model.codec.ModelTreeNodeCodec;

/**
 * A convenience subclass of {@code TypedKey} whose category is NODE, to make construction of those objects simpler.
 */
public final class NodeKey extends TypedKey<ModelTreeNode> {

    /**
     * Constructs a new {@code NodeKey}.
     *
     * @param theName       the value name
     * @throws IllegalArgumentException if category, name, or value class is null, or if the name is not valid.
     */
    public NodeKey(final String theName) throws IllegalArgumentException {

        super(ETypedMapCategory.NODE, theName, ModelTreeNodeCodec.INST);
    }
}
