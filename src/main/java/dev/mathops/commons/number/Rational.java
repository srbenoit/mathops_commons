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

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

/**
 * An immutable rational number with {@code long} numerator and denominator.
 */
public class Rational extends Number {

    /** Version for serialization. */
    @Serial
    private static final long serialVersionUID = 1058200750131148197L;

    /** The character that separates parts of a rational. */
    private static final int SLASH = (int) '/';

    /** The numerator. */
    public final long numerator;

    /** The denominator (not zero or negative). */
    public final long denominator;

    /**
     * Constructs a new {@code Rational}.
     *
     * @param theNumerator   the numerator
     * @param theDenominator the denominator
     * @throws IllegalArgumentException if the denominator is zero
     */
    public Rational(final long theNumerator, final long theDenominator) throws IllegalArgumentException {

        super();

        if (theDenominator == 0L) {
            final String msg = Res.get(Res.ZERO_DENOMINATOR);
            throw new IllegalArgumentException(msg);
        }

        final long num;
        final long den;
        if (theDenominator < 0L) {
            num = -theNumerator;
            den = -theDenominator;
        } else {
            num = theNumerator;
            den = theDenominator;
        }

        final long abs = Math.abs(num);
        final long gcd = gcdByEuclidAlgorithm(abs, den);

        this.numerator = num / gcd;
        this.denominator = den / gcd;
    }

    /**
     * Constructs a new {@code Rational} with an integer value (and denominator 1).
     *
     * @param theValue the value
     */
    public Rational(final long theValue) {

        super();

        this.numerator = theValue;
        this.denominator = 1L;
    }

    /**
     * Constructs a new {@code Rational} by parsing a string.
     *
     * @param str the string to parse
     * @return the parsed {@code Rational}
     * @throws NumberFormatException if the denominator is zero
     */
    public static Rational valueOf(final String str) throws NumberFormatException {

        final Rational result;

        final int slash = str.indexOf(SLASH);

        if (slash == -1) {
            final String trimmed = str.trim();
            final long parsed = Long.parseLong(trimmed);
            result = new Rational(parsed);
        } else {
            final String preSlash = str.substring(0, slash);
            final String numeratorStr = preSlash.trim();
            final long numerator = Long.parseLong(numeratorStr);
            final String postSlash = str.substring(slash + 1);
            final String denominatorStr = postSlash.trim();
            final long denominator = Long.parseLong(denominatorStr);
            result = new Rational(numerator, denominator);
        }

        return result;
    }

    /**
     * Returns the value of the specified number as an {@code int}.
     *
     * @return the numeric value represented by this object after conversion to type {@code int}.
     */
    @Override
    public final int intValue() {

        return (int) longValue();
    }

    /**
     * Returns the value of the specified number as a {@code long}.
     *
     * @return the numeric value represented by this object after conversion to type {@code long}.
     */
    @Override
    public final long longValue() {

        return this.numerator / this.denominator;
    }

    /**
     * Returns the value of the specified number as a {@code float}.
     *
     * @return the numeric value represented by this object after conversion to type {@code float}.
     */
    @Override
    public final float floatValue() {

        return (float) doubleValue();
    }

    /**
     * Returns the value of the specified number as a {@code double}.
     *
     * @return the numeric value represented by this object after conversion to type {@code double}.
     */
    @Override
    public final double doubleValue() {

        return (double) this.numerator / (double) this.denominator;
    }

    /**
     * Generates the String representation of the number.
     *
     * @return the string representation.
     */
    @Override
    public final String toString() {

        final String result;

        if (this.denominator == 1L) {
            result = String.valueOf(this.numerator);
        } else {
            result = this.numerator + "/" + this.denominator;
        }

        return result;
    }

    /**
     * Computes the hash code of the number.
     *
     * @return the hash code
     */
    @Override
    public final int hashCode() {

        return (int) (this.numerator + Long.rotateLeft(this.denominator, 16));
    }

    /**
     * Tests whether this object is equal to another.
     *
     * @param obj the other object
     * @return true if this object is equal to {@code o}; false if not
     */
    @Override
    public final boolean equals(final Object obj) {

        final boolean equal;

        if (obj == this) {
            equal = true;
        } else if (obj instanceof final Rational r) {
            equal = r.numerator == this.numerator && r.denominator == this.denominator;
        } else {
            equal = false;
        }

        return equal;
    }

    /**
     * Computes the GCD of two long values using Euclid's algorithm.
     *
     * @param n1 the first value
     * @param n2 the second value
     * @return the GCD
     */
    private static long gcdByEuclidAlgorithm(final long n1, final long n2) {

        return n2 == 0L ? n1 : gcdByEuclidAlgorithm(n2, n1 % n2);
    }

    /**
     * Returns the reciprocal of this number.
     *
     * @return the reciprocal
     * @throws ArithmeticException if this number is zero
     */
    public final Rational reciprocal() {

        if (this.numerator == 0L) {
            final String msg = Res.get(Res.RECIPROCAL_OF_ZERO);
            throw new ArithmeticException(msg);
        }

        return new Rational(this.denominator, this.numerator);
    }

    /**
     * Implements readObject to prevent serialization.
     *
     * @param in the input stream
     * @throws NotSerializableException always
     */
    @Serial
    private void readObject(final ObjectInputStream in) throws NotSerializableException {
        final String className = Rational.class.getName();
        throw new NotSerializableException(className);
    }

    /**
     * Implements writeObject to prevent serialization.
     *
     * @param out the output stream
     * @throws NotSerializableException always
     */
    @Serial
    private void writeObject(final ObjectOutputStream out) throws NotSerializableException {
        final String className = Rational.class.getName();
        throw new NotSerializableException(className);
    }
}
