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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A store that maps typed keys to values of the corresponding type.
 */
public class TypedMap {

    /**
     * Map data.  Data is stored in groups of three object (the key, the string representation, and the object). For the
     * use case for this object (many tree nodes, but few mapped data values per node), the overhead of hash maps for
     * object and string representation is too great.
     */
    private final List<Object> data;

    /**
     * Constructs a new {@code TypedMap}.
     */
    public TypedMap() {

        this.data = new ArrayList<>(9);
    }

    /**
     * Gets the data array object at a specified index.
     *
     * @param index the index
     * @return the data object
     */
    private Object getData(final int index) {

        return this.data.get(index);
    }

    /**
     * Sets the object value associated with a key.  This operation clears any cached String representation associated
     * with the key.
     *
     * @param <T>   the value type
     * @param key   the key
     * @param value the value
     */
    public final <T> void put(final TypedKey<T> key, final T value) {

        if (key == null) {
            throw new IllegalArgumentException("Key may not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value may not be null");
        }

        final int i = indexOfKey(key);
        if (i == -1) {
            this.data.add(key);
            this.data.add(null);
            this.data.add(value);
        } else {
            this.data.set(i + 1, null);
            this.data.set(i + 2, value);
        }
    }

    /**
     * Copies an attribute value (of any type) from a source map to this map.
     *
     * @param source the source map from which to copy
     * @param key    the key of the value to copy (if the source map does not have an attribute with this key, no action
     *               is taken)
     */
    public final void copyAttributeFrom(final TypedMap source, final AttrKey<?> key) {

        if (source == null) {
            throw new IllegalArgumentException("Source map may not be null");
        }
        if (key == null) {
            throw new IllegalArgumentException("Value key may not be null");
        }

        final int sourceIndex = source.indexOfKey(key);
        if (sourceIndex > -1) {
            final Object stringValue = source.getData(sourceIndex + 1);
            final Object typedValue = source.getData(sourceIndex + 2);
            store(key, stringValue, typedValue);
        }
    }

    /**
     * Copies all attribute values (of any type) from a source map to this map.
     *
     * @param source the source map from which to copy
     */
    public final void copyAllAttributesFrom(final TypedMap source) {

        if (source == null) {
            throw new IllegalArgumentException("Source map may not be null");
        }

        final int count = source.size() * 3;

        for (int i = 0; i < count; i += 3) {
            final Object first = source.getData(i);
            if (first instanceof final AttrKey<?> key) {
                final Object stringValue = source.getData(i + 1);
                final Object typedValue = source.getData(i + 2);
                store(key, stringValue, typedValue);
            }
        }
    }

    /**
     * Stores an entry in the data array, adding a new entry if the key does not already exist in the data array, or
     * updating the data value if it does.
     *
     * @param key         the key
     * @param stringValue the string value
     * @param objectValue the typed value
     */
    private void store(final TypedKey<?> key, final Object stringValue, final Object objectValue) {

        final int i = indexOfKey(key);

        if (i == -1) {
            // This object does not already have the attribute - add it
            this.data.add(key);
            this.data.add(stringValue);
            this.data.add(objectValue);
        } else {
            // This object has the attribute - update it
            this.data.set(i + 1, stringValue);
            this.data.set(i + 2, objectValue);
        }
    }

    /**
     * Sets the string representation associated with a key.  This operation clears any cached parsed object associated
     * with the key.
     *
     * @param key the key
     * @param str the string representation
     */
    public final void putString(final TypedKey<?> key, final String str) {

        if (key == null) {
            throw new IllegalArgumentException("Key may not be null");
        }
        if (str == null) {
            throw new IllegalArgumentException("String representation may not be null");
        }

        store(key, str, null);
    }

    /**
     * Sets the node associated with a key.  Nodes to not have associated string representations.
     *
     * @param key  the key
     * @param node the node
     */
    public final void putNode(final TypedKey<ModelTreeNode> key, final ModelTreeNode node) {

        if (key == null) {
            throw new IllegalArgumentException("Key may not be null");
        }
        if (node == null) {
            throw new IllegalArgumentException("Node may not be null");
        }

        store(key, null, node);
    }

    /**
     * Gets the value associated with a particular key.
     *
     * @param <T> the value type
     * @param key the key
     * @return the associated object; {@code null} if there is no value associated with the key
     * @throws StringParseException if a string representation was present, the object itself was not present, but the
     *                              string representation could not be parsed
     */
    public final <T> T get(final TypedKey<T> key) throws StringParseException {

        if (key == null) {
            throw new IllegalArgumentException("Key may not be null");
        }

        T result = null;

        final int i = indexOfKey(key);

        if (i >= 0) {
            final Object value = this.data.get(i + 2);
            if (value == null) {
                final Object representation = this.data.get(i + 1);
                if (representation instanceof final String s) {
                    result = key.getCodec().parse(s);
                    this.data.set(i + 2, result);
                }
            } else {
                result = key.testObject(value);
            }
        }

        return result;
    }

    /**
     * Gets the string representation associated with a particular key.
     *
     * @param <T> the value type
     * @param key the key
     * @return the associated string representation; {@code null} if there is no value associated with the key
     */
    public final <T> String getString(final TypedKey<T> key) {

        if (key == null) {
            throw new IllegalArgumentException("Key may not be null");
        }

        String result = null;

        final int i = indexOfKey(key);

        if (i >= 0) {
            final Object representation = this.data.get(i + 1);
            if (representation instanceof final String s) {
                result = s;
            } else {
                final Object value = this.data.get(i + 2);
                final T t = key.testObject(value);
                if (t != null) {
                    result = key.getCodec().stringify(t);
                    this.data.set(i + 1, result);
                }
            }
        }

        return result;
    }

    /**
     * Gets the tree node associated with a particular key.
     *
     * @param key the key
     * @return the associated node; {@code null} if there is no node associated with the key
     */
    public final ModelTreeNode getNode(final TypedKey<ModelTreeNode> key) {

        if (key == null) {
            throw new IllegalArgumentException("Key may not be null");
        }

        ModelTreeNode result = null;

        final int i = indexOfKey(key);

        if (i >= 0) {
            final Object value = this.data.get(i + 2);
            if (value instanceof final ModelTreeNode node) {
                result = node;
            }
        }

        return result;
    }

    /**
     * Removes the value associated with a particular key.
     *
     * @param key the key
     * @return {@code true} if a value was found and removed
     */
    public final boolean remove(final TypedKey<?> key) {

        final int i = indexOfKey(key);

        if (i >= 0) {
            this.data.remove(i); // Key
            this.data.remove(i); // String representation
            this.data.remove(i); // Typed value
        }

        return i >= 0;
    }

    /**
     * Tests whether the map contains a mapping from a specified key to a value.
     *
     * @param key the key
     * @return {@code true} if the map has a value associated with the specified key
     */
    public final boolean containsKey(final TypedKey<?> key) {

        return indexOfKey(key) >= 0;
    }

    /**
     * Adds all "attribute" keys to a target list.
     *
     * @param target the list to which to add attribute keys
     */
    public final void getAttributeKeys(final Collection<? super AttrKey<?>> target) {

        final int len = this.data.size();

        for (int i = 0; i < len; i += 3) {
            final Object o = this.data.get(i);
            if (o instanceof final AttrKey<?> firstKey) {
                target.add(firstKey);
            }
        }
    }

    /**
     * Adds all "property" keys to a target list.
     *
     * @param target the list to which to add property keys
     */
    public final void getPropertyKeys(final Collection<? super PropKey<?>> target) {

        final int len = this.data.size();

        for (int i = 0; i < len; i += 3) {
            final Object o = this.data.get(i);
            if (o instanceof final PropKey<?> firstKey) {
                target.add(firstKey);
            }
        }
    }

    /**
     * Adds all "data" keys to a target list.
     *
     * @param target the list to which to add data keys
     */
    public final void getDataKeys(final Collection<? super DataKey<?>> target) {

        final int len = this.data.size();

        for (int i = 0; i < len; i += 3) {
            final Object o = this.data.get(i);
            if (o instanceof final DataKey<?> firstKey) {
                target.add(firstKey);
            }
        }
    }

    /**
     * Adds all "node" keys to a target list.
     *
     * @param target the list to which to add node keys
     */
    public final void getNodeKeys(final Collection<? super NodeKey> target) {

        final int len = this.data.size();

        for (int i = 0; i < len; i += 3) {
            final Object o = this.data.get(i);
            if (o instanceof final NodeKey firstKey) {
                target.add(firstKey);
            }
        }
    }

    /**
     * Gets the number of mappings in this map.
     *
     * @return the number of mappings
     */
    public final int size() {

        return this.data.size() / 3;
    }

    /**
     * Finds the index in the array of a key.
     *
     * @param key the key
     * @return the index (either -1 if the key was not found, or an integer multiple of 3)
     */
    private int indexOfKey(final TypedKey<?> key) {

        int index = -1;

        final int len = this.data.size();
        for (int i = 0; i < len; i += 3) {
            final Object o = this.data.get(i);
            if (o instanceof final TypedKey<?> firstKey && firstKey.hashCode() == key.hashCode() && firstKey.equals(
                    key)) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Generates a diagnostic string representation of the map.
     *
     * @return the string representation
     */
    @Override
    public final String toString() {

        final StringBuilder builder = new StringBuilder(100);

        final Class<? extends TypedMap> cls = getClass();
        final String className = cls.getName();
        builder.append(className);
        builder.append('{');

        final int len = this.data.size();

        for (int i = 0; i < len; i += 3) {
            final Object key = this.data.get(i);
            if (key instanceof final TypedKey<?> typedKey) {
                final String keyName = typedKey.getName();
                builder.append(keyName);
                builder.append('=');

                final Object obj2 = this.data.get(i + 2);
                if (obj2 != null) {
                    builder.append(obj2);
                }

                final Object obj1 = this.data.get(i + 1);
                if (obj1 != null) {
                    builder.append('[');
                    builder.append(obj1);
                    builder.append(']');
                }
            }
        }

        builder.append('}');

        return builder.toString();
    }
}
