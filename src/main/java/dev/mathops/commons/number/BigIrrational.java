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

import dev.mathops.commons.log.Log;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.math.BigInteger;

/**
 * A constant irrational value consisting of a fraction (with integer numerator and denominator) and an irrational
 * factor which can be PI or E or the square root of a positive integer. No effort is made in the case of a square root
 * to ensure the root is actually irrational.
 */
public final class BigIrrational extends Number {

    /** Version for serialization. */
    @Serial
    private static final long serialVersionUID = 4139505768172978960L;

    /** A BigInteger with value -1. */
    private static final BigInteger BIG_NEGATIVE_ONE = BigInteger.valueOf(-1L);

    /** The factor. */
    public final EIrrationalFactor factor;

    /** The number whose root is to be taken (0 if there is no root). */
    public final long base;

    /** The coefficient numerator. */
    public final BigInteger numerator;

    /** The coefficient denominator. */
    public final BigInteger denominator;

    /**
     * Constructs a new {@code BigIrrational}.
     *
     * @param theFactor      the factor (may not be SQRT)
     * @param theNumerator   the numerator
     * @param theDenominator the denominator (must be positive, nonzero)
     */
    public BigIrrational(final EIrrationalFactor theFactor, final BigInteger theNumerator,
                         final BigInteger theDenominator) {
        super();

        if (theFactor == null) {
            final String msg = Res.get(Res.NULL_FACTOR);
            throw new IllegalArgumentException(msg);
        }
        if (theFactor == EIrrationalFactor.SQRT) {
            final String msg = Res.get(Res.ROOT_FACTOR_WITHOUT_BASE);
            throw new IllegalArgumentException(msg);
        }
        if (theDenominator.signum() <= 0) {
            final String msg = Res.get(Res.DENOMINATOR_NOT_POSITIVE);
            throw new IllegalArgumentException(msg);
        }

        this.factor = theFactor;
        this.base = 0L;
        this.numerator = theNumerator;
        this.denominator = theDenominator;
    }

    /**
     * Constructs a new {@code BigIrrational}.
     *
     * @param theFactor      the factor (must be SQRT)
     * @param theBase        the base (must be positive, nonzero)
     * @param theNumerator   the numerator
     * @param theDenominator the denominator (must be positive, nonzero)
     */
    public BigIrrational(final EIrrationalFactor theFactor, final long theBase, final BigInteger theNumerator,
                         final BigInteger theDenominator) {
        super();

        if (theFactor == null) {
            final String msg = Res.get(Res.NULL_FACTOR);
            throw new IllegalArgumentException(msg);
        }
        if (theFactor != EIrrationalFactor.SQRT) {
            final String msg = Res.get(Res.BASE_ONLY_ON_ROOT_FACTOR);
            throw new IllegalArgumentException(msg);
        }
        if (theDenominator.signum() <= 0) {
            final String msg = Res.get(Res.DENOMINATOR_NOT_POSITIVE);
            throw new IllegalArgumentException(msg);
        }
        if (theBase <= 0L) {
            final String msg = Res.get(Res.BASE_NOT_POSITIVE);
            throw new IllegalArgumentException(msg);
        }

        this.factor = theFactor;
        this.base = theBase;
        this.numerator = theNumerator;
        this.denominator = theDenominator;
    }

    /**
     * Attempts to parse a big irrational from a string.
     *
     * <p>
     * Supported formats include:
     *
     * <pre>
     * 3PI/4  Any string with "PI" is treated as a fraction with PI in the numerator.  There can
     *        be an integer coefficient before PI, and optionally a denominator made up of a
     *        slash character followed by an integer.  No characters between the PI and slash are
     *        allowed.
     * 3E/4   Same format as above, but with the constant "E" rather than "PI".
     * 3R2/2  Any string with "R" is treated as a rational multiple of the square root of an
     *        integer.  The format has an optional integer coefficient before the R, a required
     *        integer after the R (whose square root is to be taken), and an optional denominator
     *        consisting of a slash character then an integer.
     * </pre>
     *
     * @param str the string
     * @return the parsed {@code Irrational}.
     * @throws NumberFormatException if the string is not in a valid format
     */
    public static BigIrrational valueOf(final String str) throws NumberFormatException {

        final BigIrrational result;

        final int piPos = str.indexOf(Irrational.PI);
        if (piPos == -1) {
            final int ePos = str.indexOf(Irrational.E);
            if (ePos == -1) {
                final int rPos = str.indexOf(Irrational.R);
                if (rPos == -1) {
                    final String msg = Res.fmt(Res.BAD_RATIONAL_NUMBER, str);
                    throw new NumberFormatException(msg);
                }
                result = valueOfSqrt(str, rPos);
            } else {
                result = valueOfE(str, ePos);
            }
        } else {
            result = valueOfPi(str, piPos);
        }

        return result;
    }

    /**
     * Attempts to parse a big irrational from a string that contains "PI".
     *
     * @param str   the string
     * @param piPos the index of the "PI" substring
     * @return the parsed {@code Irrational}.
     * @throws NumberFormatException if the string is not in a valid format
     */
    private static BigIrrational valueOfPi(final String str, final int piPos) throws NumberFormatException {

        final BigIrrational result;

        final int len = str.length();

        if (piPos == len - 2) {
            if (piPos == 0) {
                // Format: "PI"
                result = new BigIrrational(EIrrationalFactor.PI, BigInteger.ONE, BigInteger.ONE);
            } else {
                // Format: "10PI"
                final String numeratorStr = str.substring(0, piPos);
                final BigInteger numerator = "-".equals(numeratorStr) ? BIG_NEGATIVE_ONE : new BigInteger(numeratorStr);
                result = new BigIrrational(EIrrationalFactor.PI, numerator, BigInteger.ONE);
            }
        } else if ((int) str.charAt(piPos + 2) == Irrational.SLASH) {
            final String denominatorStr = str.substring(piPos + 3);
            final BigInteger denominator = new BigInteger(denominatorStr);
            if (piPos == 0) {
                // Format: "PI/4"
                result = new BigIrrational(EIrrationalFactor.PI, BigInteger.ONE, denominator);
            } else {
                // Format: "5PI/4"
                final String numeratorStr = str.substring(0, piPos);
                final BigInteger numerator = "-".equals(numeratorStr) ? BIG_NEGATIVE_ONE : new BigInteger(numeratorStr);
                result = new BigIrrational(EIrrationalFactor.PI, numerator, denominator);
            }
        } else {
            final String msg = Res.fmt(Res.BAD_RATIONAL_NUMBER, str);
            throw new NumberFormatException(msg);
        }

        return result;
    }

    /**
     * Attempts to parse a big irrational from a string that contains "E".
     *
     * @param str  the string
     * @param ePos the index of the "E" substring
     * @return the parsed {@code Irrational}.
     * @throws NumberFormatException if the string is not in a valid format
     */
    private static BigIrrational valueOfE(final String str, final int ePos) throws NumberFormatException {

        final BigIrrational result;

        final int len = str.length();

        if (ePos == len - 1) {
            if (ePos == 0) {
                // Format: "E"
                result = new BigIrrational(EIrrationalFactor.E, BigInteger.ONE, BigInteger.ONE);
            } else {
                // Format: "10E"
                final String numStr = str.substring(0, ePos);
                final BigInteger numerator = "-".equals(numStr) ? BIG_NEGATIVE_ONE : new BigInteger(numStr);
                result = new BigIrrational(EIrrationalFactor.E, numerator, BigInteger.ONE);
            }
        } else if ((int) str.charAt(ePos + 1) == Irrational.SLASH) {
            final String denominatorStr = str.substring(ePos + 2);
            final BigInteger denominator = new BigInteger(denominatorStr);
            if (ePos == 0) {
                // Format: "E/4"
                result = new BigIrrational(EIrrationalFactor.E, BigInteger.ONE, denominator);
            } else {
                // Format: "5E/4"
                final String numeratorStr = str.substring(0, ePos);
                final BigInteger numerator = "-".equals(numeratorStr) ? BIG_NEGATIVE_ONE : new BigInteger(numeratorStr);
                result = new BigIrrational(EIrrationalFactor.E, numerator, denominator);
            }
        } else {
            final String msg = Res.fmt(Res.BAD_RATIONAL_NUMBER, str);
            throw new NumberFormatException(msg);
        }

        return result;
    }

    /**
     * Attempts to parse a big irrational from a string that contains "R".
     *
     * @param str  the string
     * @param rPos the index of the "R" substring
     * @return the parsed {@code Irrational}.
     * @throws NumberFormatException if the string is not in a valid format
     */
    private static BigIrrational valueOfSqrt(final String str, final int rPos) throws NumberFormatException {

        final BigIrrational result;

        final int slashPos = str.indexOf(Irrational.SLASH);

        if (slashPos == -1) {
            final String baseStr = str.substring(rPos + 1);
            final long base = Long.parseLong(baseStr);

            if (rPos == 0) {
                // Format: R3
                result = new BigIrrational(EIrrationalFactor.SQRT, base, BigInteger.ONE, BigInteger.ONE);
            } else {
                // Format: 2R3
                final String numStr = str.substring(0, rPos);
                final BigInteger numerator = "-".equals(numStr) ? BIG_NEGATIVE_ONE : new BigInteger(numStr);
                result = new BigIrrational(EIrrationalFactor.SQRT, base, numerator, BigInteger.ONE);
            }
        } else if (slashPos > rPos) {
            final String baseStr = str.substring(rPos + 1, slashPos);
            final long base = Long.parseLong(baseStr);
            final String denominatorStr = str.substring(slashPos + 1);
            final BigInteger denominator = new BigInteger(denominatorStr);

            if (rPos == 0) {
                // Format: R3/4
                result = new BigIrrational(EIrrationalFactor.SQRT, base, BigInteger.ONE, denominator);
            } else {
                // Format: 2R3/4
                final String numStr = str.substring(0, rPos);
                final BigInteger numerator = "-".equals(numStr) ? BIG_NEGATIVE_ONE : new BigInteger(numStr);
                result = new BigIrrational(EIrrationalFactor.SQRT, base, numerator, denominator);
            }
        } else {
            final String msg = Res.fmt(Res.BAD_RATIONAL_NUMBER, str);
            throw new NumberFormatException(msg);
        }

        return result;
    }

    /**
     * Returns the value of the specified number as an {@code int}.
     *
     * @return the numeric value represented by this object after conversion to type {@code int}.
     */
    @Override
    public int intValue() {

        final double value = doubleValue();
        return (int) Math.round(value);
    }

    /**
     * Returns the value of the specified number as a {@code long}.
     *
     * @return the numeric value represented by this object after conversion to type {@code long}.
     */
    @Override
    public long longValue() {

        final double value = doubleValue();
        return Math.round(value);
    }

    /**
     * Returns the value of the specified number as a {@code float}.
     *
     * @return the numeric value represented by this object after conversion to type {@code float}.
     */
    @Override
    public float floatValue() {

        return (float) doubleValue();
    }

    /**
     * Returns the value of the specified number as a {@code double}.
     *
     * @return the numeric value represented by this object after conversion to type {@code double}.
     */
    @Override
    public double doubleValue() {

        return switch (this.factor) {
            case PI -> Math.PI * this.numerator.doubleValue() / this.denominator.doubleValue();
            case E -> Math.E * this.numerator.doubleValue() / this.denominator.doubleValue();
            case SQRT -> Math.sqrt((double) this.base) * this.numerator.doubleValue() / this.denominator.doubleValue();
        };
    }

    /**
     * Gets the numeric value of the factor.
     *
     * @return the numeric value
     */
    public double getFactorValue() {

        return switch (this.factor) {
            case PI -> Math.PI;
            case E -> Math.E;
            case SQRT -> Math.sqrt((double) this.base);
        };
    }

    /**
     * Gets the string representation of the factor.
     *
     * @return the string
     */
    public String getFactorString() {

        return switch (this.factor) {
            case PI -> Irrational.PI_STR;
            case E -> "e";
            case SQRT -> Irrational.SURD_STR + this.base;
        };
    }

    /**
     * Generates a hash code for this value.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {

        final long value = ((long) this.factor.hashCode() << 24) + (this.base << 16);

        return Long.hashCode(value) + this.numerator.hashCode() + this.denominator.hashCode();
    }

    /**
     * Tests whether this value is equal to another. To be equal, the other value must be an {@code Irrational} that
     * evaluates to the same double value.
     *
     * @param obj the other value
     * @return true if equal; false if not
     */
    @Override
    public boolean equals(final Object obj) {

        final boolean equal;

        if (obj == this) {
            equal = true;
        } else if (obj instanceof final BigIrrational irrational) {
            equal = doubleValue() == irrational.doubleValue();
        } else {
            equal = false;
        }

        return equal;
    }

    /**
     * Generates the string representation of the number.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final StringBuilder result = new StringBuilder(16);

        switch (this.factor) {
            case PI:
                if (!this.numerator.equals(BigInteger.ONE)) {
                    result.append(this.numerator);
                }
                result.append(Irrational.PI);
                if (!this.denominator.equals(BigInteger.ONE)) {
                    result.append(Irrational.SLASH_CHAR);
                    result.append(this.denominator);
                }
                break;

            case E:
                if (!this.numerator.equals(BigInteger.ONE)) {
                    result.append(this.numerator);
                }
                result.append(Irrational.E);
                if (!this.denominator.equals(BigInteger.ONE)) {
                    result.append(Irrational.SLASH_CHAR);
                    result.append(this.denominator);
                }
                break;

            case SQRT:
                if (!this.numerator.equals(BigInteger.ONE)) {
                    result.append(this.numerator);
                }
                result.append(Irrational.R);
                result.append(this.base);
                if (!this.denominator.equals(BigInteger.ONE)) {
                    result.append(Irrational.SLASH_CHAR);
                    result.append(this.denominator);
                }
                break;

            default:
                final String msg = Res.get(Res.UNSUPPORTED_FACTOR);
                Log.warning(msg);
                break;
        }

        return result.toString();
    }

    /**
     * Implements readObject to prevent serialization.
     *
     * @param in the input stream
     * @throws NotSerializableException always
     */
    @Serial
    private void readObject(final ObjectInputStream in) throws NotSerializableException {
        final String className = BigIrrational.class.getName();
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
        final String className = BigIrrational.class.getName();
        throw new NotSerializableException(className);
    }
}
