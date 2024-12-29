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
 * A key in a typed map.  The key carries a category, a name, and the class of values.  The pairing of category, name,
 * and class is the unique key; multiple keys with different types and the same name are considered distinct.  This
 * supports use cases where a map stores a text representation (as a String) plus a parsed representation, under the
 * same name.
 *
 * <p>
 * Key names must be valid XML {@code Name} construction, as defined in XML 1.1 (modified to eliminate code points
 * outside the range of a {@code char}):
 * <pre>
 * NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D]
 *                       | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF]
 *                       | [#xF900-#xFDCF] | [#xFDF0-#xFFFD]
 * NameChar      ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
 * Name          ::= NameStartChar (NameChar)*
 * </pre>
 *
 * <p>
 * Such a key can optionally include any number of converter objects that can each take a value in one class and convert
 * it to the target class.  The primary use-case for this is XML attributes, which are transmitted as Strings (and whose
 * string representation should be preserved and stored in a map as Strings), but represent some other type, like a
 * Color. The converter supports converting the String to a Color on {@code get}.  If the conversion is successful, the
 * converted value is stored under the same name but the new class, and can be retrieved in future calls without the
 * overhead of converting again.
 *
 * <p>
 * To support serialization, every typed value that is not of {@code String} type must have a serializer.
 *
 * @param <T> the value data type
 */
public class TypedKey<T> {

    /** A name character. */
    static final int COLON = (int) ':';

    /** A name character. */
    static final int UC_A = (int) 'A';

    /** A name character. */
    static final int UC_Z = (int) 'Z';

    /** A name character. */
    static final int SPC = (int) '_';

    /** A name character. */
    static final int LC_A = (int) 'a';

    /** A name character. */
    static final int LC_Z = (int) 'z';

    /** A name character. */
    static final int ZERO = (int) '0';

    /** A name character. */
    static final int NINE = (int) '9';

    /** A name character. */
    static final int DOT = (int) '.';

    /** A name character. */
    static final int DASH = (int) '-';

    /** A name character. */
    static final int MID_DOT = 0xB7;

    /** The start of a range of name characters. */
    static final int START1 = 0xC0;

    /** The end of a range of name characters. */
    static final int END1 = 0xD6;

    /** The start of a range of name characters. */
    static final int START2 = 0xD8;

    /** The end of a range of name characters. */
    static final int END2 = 0xF6;

    /** The start of a range of name characters. */
    static final int START3 = 0xF8;

    /** The end of a range of name characters. */
    static final int END3 = 0x2FF;

    /** The start of a range of name characters. */
    static final int START4 = 0x370;

    /** The start of a range of name characters. */
    static final int START4A = 0x300;

    /** The end of a range of name characters. */
    static final int END4 = 0x37D;

    /** The start of a range of name characters. */
    static final int START5 = 0x37F;

    /** The end of a range of name characters. */
    static final int END5 = 0x1FFF;

    /** The start of a range of name characters. */
    static final int START6 = 0x200C;

    /** The end of a range of name characters. */
    static final int END6 = 0x200D;

    /** The start of a range of name characters. */
    static final int START7 = 0x2070;

    /** The end of a range of name characters. */
    static final int END7 = 0x218F;

    /** The start of a range of name characters. */
    static final int START8 = 0x2C00;

    /** The end of a range of name characters. */
    static final int END8 = 0x2FEF;

    /** The start of a range of name characters. */
    static final int START9 = 0x3001;

    /** The end of a range of name characters. */
    static final int END9 = 0xD7FF;

    /** The start of a range of name characters. */
    static final int START10 = 0xF900;

    /** The end of a range of name characters. */
    static final int END10 = 0xFDCF;

    /** The start of a range of name characters. */
    static final int START11 = 0xFDF0;

    /** The end of a range of name characters. */
    static final int END11 = 0xFFFD;

    /** The start of a range of name characters. */
    static final int START12 = 0x203F;

    /** The end of a range of name characters. */
    static final int END12 = 0x2040;

    /** The value category. */
    private final ETypedMapCategory category;

    /** The value name. */
    private final String name;

    /** The codec. */
    private final Codec<T> codec;

    /** A pre-computed hash code. */
    private final int hash;

    /**
     * Constructs a new {@code TypedKey}.
     *
     * @param theCategory   the value category
     * @param theName       the value name
     * @param theCodec      the codec
     * @throws IllegalArgumentException if category, name, or value class is null, or if the name is not valid.
     */
    protected TypedKey(final ETypedMapCategory theCategory, final String theName, final Codec<T> theCodec)
            throws IllegalArgumentException {

        if (theCategory == null) {
            final String msg = Res.get(Res.NULL_CATEGORY);
            throw new IllegalArgumentException(msg);
        }
        if (theName == null || theName.isBlank()) {
            final String msg = Res.get(Res.NULL_NAME);
            throw new IllegalArgumentException(msg);
        }
        if (theCodec == null) {
            final String msg = Res.get(Res.NULL_CODEC);
            throw new IllegalArgumentException(msg);
        }

        validateName(theName);

        this.category = theCategory;
        this.name = theName;
        this.codec = theCodec;

        final Class<T> valueClass = theCodec.getType();

        this.hash = this.category.hashCode() + this.name.hashCode() + valueClass.hashCode();
    }

    /**
     * Validates a name.  Names must be valid XML names to support serialization in XML formats.
     *
     * @param theName the name to validate
     * @throws IllegalArgumentException if the name is not valid
     */
    private void validateName(final String theName) throws IllegalArgumentException {

        final int c0 = (int) theName.charAt(0);

        if (c0 == COLON || (c0 >= UC_A && c0 <= UC_Z) || c0 == SPC || (c0 >= LC_A && c0 <= LC_Z)
            || (c0 >= START1 && c0 <= END1)
            || (c0 >= START2 && c0 <= END2)
            || (c0 >= START3 && c0 <= END3)
            || (c0 >= START4 && c0 <= END4)
            || (c0 >= START5 && c0 <= END5)
            || (c0 >= START6 && c0 <= END6)
            || (c0 >= START7 && c0 <= END7)
            || (c0 >= START8 && c0 <= END8)
            || (c0 >= START9 && c0 <= END9)
            || (c0 >= START10 && c0 <= END10)
            || (c0 >= START11 && c0 <= END11)
        ) {
            final int len = theName.length();
            for (int i = 1; i < len; ++i) {
                final int c = (int) theName.charAt(i);

                if (c == DASH || c == DOT || (c >= ZERO && c <= NINE)
                    || c == COLON || (c >= UC_A && c <= UC_Z) || c == SPC || (c >= LC_A && c <= LC_Z) || c == MID_DOT
                    || (c >= START1 && c <= END1)
                    || (c >= START2 && c <= END2)
                    || (c >= START3 && c <= END3)
                    || (c >= START4A && c <= END4)
                    || (c >= START5 && c <= END5)
                    || (c >= START6 && c <= END6)
                    || (c >= START12 && c <= END12)
                    || (c >= START7 && c <= END7)
                    || (c >= START8 && c <= END8)
                    || (c >= START9 && c <= END9)
                    || (c >= START10 && c <= END10)
                    || (c >= START11 && c <= END11)) {
                    continue;
                }

                throw new IllegalArgumentException("Invalid name character");
            }
        } else {
            throw new IllegalArgumentException("Invalid name start character");
        }
    }

    /**
     * Gets the value category.
     *
     * @return the value category
     */
    public final ETypedMapCategory getCategory() {

        return this.category;
    }

    /**
     * Gets the key name.
     *
     * @return the key name
     */
    public final String getName() {

        return this.name;
    }

    /**
     * Gets the value class.
     *
     * @return the value class
     */
    public final Class<?> getValueClass() {

        return this.codec.getType();
    }

    /**
     * Gets the codec.
     *
     * @return the codec
     */
    public final Codec<T> getCodec() {

        return this.codec;
    }

    /**
     * Tests whether an object is compatible with the data type defined by this key.
     *
     * @param o the object to test
     * @return the object converted to type T if compatible; {@code null} if not
     */
    @SuppressWarnings("unchecked")
    public final T testObject(final Object o) {

        T result;

        if (o == null) {
            result = null;
        } else {
            final Class<T> valueClass = this.codec.getType();

            if (valueClass.isInstance(o)) {
                result = (T) o;
            } else {
                result = null;
            }
        }

        return result;
    }

    /**
     * Generates a hash code for the key.
     *
     * @return the hash code
     */
    public final int hashCode() {

        return this.hash;
    }

    /**
     * Tests whether this object is equal to another.
     *
     * @param obj the other object
     * @return {@code true} if {@code obj} is a {@code TypedMapKey} and has the same name and value class as this
     *         object; {@code false} otherwise
     */
    public final boolean equals(final Object obj) {

        final boolean equal;

        if (obj == this) {
            equal = true;
        } else if (obj instanceof final TypedKey<?> key) {
            if (this.hash == key.hashCode()) {
                equal = key.getCategory() == this.category && key.getName().equals(this.name)
                        && key.getValueClass().equals(getValueClass());
            } else {
                equal = false;
            }
        } else {
            equal = false;
        }

        return equal;
    }

    /**
     * Generates a diagnostic string representation of this object.
     *
     * @return the string representation
     */
    @Override
    public final String toString() {

        final StringBuilder builder = new StringBuilder(50);

        builder.append("TypedKey{category=");
        builder.append(this.category.name());
        builder.append(",name=');name='");
        builder.append(this.name);
        builder.append("',valueClass=");
        final String clsName = getValueClass().getName();
        builder.append(clsName);
        builder.append("}");

        return builder.toString();
    }
}
