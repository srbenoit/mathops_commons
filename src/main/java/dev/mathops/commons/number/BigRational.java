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
import java.math.BigInteger;

/**
 * An immutable rational number with {@code BigInteger} numerator and denominator.
 */
public class BigRational extends Number {

    /** Version for serialization. */
    @Serial
    private static final long serialVersionUID = 1058200750131148198L;

    /** The character that separates parts of a rational. */
    private static final int SLASH = (int) '/';

    /** The numerator. */
    public final BigInteger numerator;

    /** The denominator (not zero or negative). */
    public final BigInteger denominator;

    /**
     * Constructs a new {@code BigRational}.
     *
     * @param theNumerator   the numerator
     * @param theDenominator the denominator
     * @throws IllegalArgumentException if the denominator is zero
     */
    public BigRational(final BigInteger theNumerator, final BigInteger theDenominator) throws IllegalArgumentException {

        super();

        if (theNumerator == null || theDenominator == null) {
            final String msg = Res.get(Res.NULL_NUMERATOR_DENOMINATOR);
            throw new IllegalArgumentException(msg);
        }
        if (BigInteger.ZERO.equals(theDenominator)) {
            final String msg = Res.get(Res.ZERO_DENOMINATOR);
            throw new IllegalArgumentException(msg);
        }

        final BigInteger num;
        final BigInteger den;
        if (theDenominator.signum() < 0) {
            num = theNumerator.negate();
            den = theDenominator.negate();
        } else {
            num = theNumerator;
            den = theDenominator;
        }

        final BigInteger gcd = num.gcd(den);
        if (BigInteger.ONE.equals(gcd)) {
            this.numerator = num;
            this.denominator = den;
        } else {
            this.numerator = num.divide(gcd);
            this.denominator = den.divide(gcd);
        }
    }

    /**
     * Constructs a new {@code BigRational} with an integer value (and denominator 1).
     *
     * @param theValue the value
     */
    public BigRational(final BigInteger theValue) {

        super();

        this.numerator = theValue;
        this.denominator = BigInteger.ONE;
    }

    /**
     * Constructs a new {@code Rational} by parsing a string.
     *
     * @param str the string to parse
     * @return the parsed {@code Rational}
     * @throws IllegalArgumentException if the denominator is zero
     */
    public static BigRational valueOf(final String str) throws IllegalArgumentException {

        final BigRational result;

        final int slash = str.indexOf(SLASH);

        if (slash == -1) {
            final String trimmed = str.trim();
            result = new BigRational(new BigInteger(trimmed));
        } else {
            final String preSlash = str.substring(0, slash);
            final String preTrimmed = preSlash.trim();
            final BigInteger numerator = new BigInteger(preTrimmed);
            final String postSlash = str.substring(slash + 1);
            final String postTrimmed = postSlash.trim();
            final BigInteger denominator = new BigInteger(postTrimmed);
            result = new BigRational(numerator, denominator);
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

        return this.numerator.divide(this.denominator).intValue();
    }

    /**
     * Returns the value of the specified number as a {@code long}.
     *
     * @return the numeric value represented by this object after conversion to type {@code long}.
     */
    @Override
    public final long longValue() {

        return this.numerator.divide(this.denominator).longValue();
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

        return this.numerator.doubleValue() / this.denominator.doubleValue();
    }

    /**
     * Generates the String representation of the number.
     *
     * @return the string representation.
     */
    @Override
    public final String toString() {

        final String result;

        if (BigInteger.ONE.equals(this.denominator)) {
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

        return this.numerator.hashCode() + this.denominator.hashCode();
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
        } else if (obj instanceof final BigRational r) {
            equal = r.numerator.equals(this.numerator) && r.denominator.equals(this.denominator);
        } else {
            equal = false;
        }

        return equal;
    }

    /**
     * Returns the reciprocal of this number.
     *
     * @return the reciprocal
     * @throws ArithmeticException if this number is zero
     */
    public final BigRational reciprocal() {

        if (BigInteger.ZERO.equals(this.numerator)) {
            final String msg = Res.get(Res.RECIPROCAL_OF_ZERO);
            throw new ArithmeticException(msg);
        }

        return new BigRational(this.denominator, this.numerator);
    }

    /**
     * Implements readObject to prevent serialization.
     *
     * @param in the input stream
     * @throws NotSerializableException always
     */
    @Serial
    private void readObject(final ObjectInputStream in) throws NotSerializableException {
        final String className = BigRational.class.getName();
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
        final String className = BigRational.class.getName();
        throw new NotSerializableException(className);
    }
}
