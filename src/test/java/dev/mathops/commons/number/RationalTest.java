package dev.mathops.commons.number;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test cases for the {code Rational} class.
 */
final class RationalTest {

    /**
     * Constructs a new {code RationalTest}.
     */
    RationalTest() {

        // No action
    }

    /** A test case. */
    @Test
    void constructorLL1() {

        final long numerator = 3L * 5L * 17L * 193L * 227L * 2477L;
        final long denominator = -3L * 7L * 23L * 193L * 263L * 2467L;
        final Rational r = new Rational(numerator, denominator);

        // Common factors are 3, 193
        final long common = 3L * 193L;
        final long expectNumerator = -numerator / common;
        final long expectDenominator = -denominator / common;

        assertEquals(expectNumerator, r.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(expectDenominator, r.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void constructorLL2() {

        final long numerator = 3L * 5L * 17L * 193L * 227L * 2477L;
        final long denominator = 3L * 193L;
        final Rational r = new Rational(numerator, denominator);

        // Common factors are 3, 193
        final long common = 3L * 193L;
        final long expectNumerator = numerator / common;
        final long expectDenominator = denominator / common;

        assertEquals(expectNumerator, r.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(expectDenominator, r.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void constructorLL3() {

        final Rational r = new Rational(0L, -515L);

        assertEquals(0L, r.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(1L, r.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void constructorL1() {

        final long numerator = 3L * 5L * 17L * 193L * 227L * 2477L;
        final Rational r = new Rational(numerator);

        assertEquals(numerator, r.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(1L, r.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf1() {

        final Rational r = Rational.valueOf("1/2");

        assertEquals(1L, r.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(2L, r.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf2() {

        final Rational r = Rational.valueOf("  -1 / 2  ");

        assertEquals(-1L, r.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(2L, r.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf3() {

        final Rational r1 = Rational.valueOf(BigRationalTest.TEST_RATIONAL_STRING_1);
        final Rational r2 = new Rational(27672560985L, 60482444799L);

        assertEquals(r2.numerator, r1.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(r2.denominator, r1.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf4() {

        final Rational r1 = Rational.valueOf(BigRationalTest.TEST_RATIONAL_STRING_2);
        final Rational r2 = new Rational(27672560985L);

        assertEquals(r2.numerator, r1.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(r2.denominator, r1.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf5() {

        final Rational r1 = Rational.valueOf(" 0/515 ");

        assertEquals(0L, r1.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(1L, r1.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void intValue() {

        final Rational r1 = new Rational(BigRationalTest.TEST_LONG_2);
        final int actual1 = r1.intValue();
        assertEquals(BigRationalTest.TEST_INT_1, actual1, BigRationalTest.INTEGER_VALUE_NOT_AS_EXPECTED);

        final Rational r2 = new Rational(BigRationalTest.TEST_LONG_1);
        final int actual2 = r2.intValue();
        assertEquals((int) (BigRationalTest.TEST_LONG_1), actual2, BigRationalTest.INTEGER_VALUE_NOT_AS_EXPECTED);

        final Rational r3 = new Rational(10L, 3L);
        final int actual3 = r3.intValue();
        assertEquals(3, actual3, BigRationalTest.INTEGER_VALUE_NOT_AS_EXPECTED);

        final Rational r4 = new Rational(-10L, 3L);
        final int actual4 = r4.intValue();
        assertEquals(-3, actual4, BigRationalTest.INTEGER_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void longValue() {

        final Rational r1 = new Rational(BigRationalTest.TEST_LONG_2);
        final long actual1 = r1.longValue();
        assertEquals(BigRationalTest.TEST_LONG_2, actual1, BigRationalTest.LONG_VALUE_NOT_AS_EXPECTED);

        final Rational r2 = new Rational(BigRationalTest.TEST_LONG_1);
        final long actual2 = r2.longValue();
        assertEquals(BigRationalTest.TEST_LONG_1, actual2, BigRationalTest.LONG_VALUE_NOT_AS_EXPECTED);

        final Rational r3 = new Rational(10L, 3L);
        final long actual3 = r3.longValue();
        assertEquals(BigRationalTest.TEST_LONG_3, actual3, BigRationalTest.LONG_VALUE_NOT_AS_EXPECTED);

        final Rational r4 = new Rational(-10L, 3L);
        final long actual4 = r4.longValue();
        assertEquals(-BigRationalTest.TEST_LONG_3, actual4, BigRationalTest.LONG_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void floatValue() {

        final double d1 = 12345.0 / 67890.0;
        final Rational r1 = new Rational(12345L, 67890L);
        final float actual1 = r1.floatValue();
        assertEquals((float) d1, actual1, BigRationalTest.FLOAT_VALUE_NOT_AS_EXPECTED);

        final double d2 = -67890.0 / 12345.0;
        final Rational r2 = new Rational(-67890L, 12345L);
        final float actual2 = r2.floatValue();
        assertEquals((float) d2, actual2, BigRationalTest.FLOAT_VALUE_NOT_AS_EXPECTED);

        final double d3 = (double) Long.MAX_VALUE;
        final Rational r3 = new Rational(Long.MAX_VALUE, 1L);
        final float actual3 = r3.floatValue();
        assertEquals((float) d3, actual3, BigRationalTest.FLOAT_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void doubleValue() {

        final double d1 = 12345.0 / 67890.0;
        final Rational r1 = new Rational(12345L, 67890L);
        final double actual1 = r1.doubleValue();
        assertEquals(d1, actual1, BigRationalTest.DOUBLE_VALUE_NOT_AS_EXPECTED);

        final double d2 = -67890.0 / 12345.0;
        final Rational r2 = new Rational(-67890L, 12345L);
        final double actual2 = r2.doubleValue();
        assertEquals(d2, actual2, BigRationalTest.DOUBLE_VALUE_NOT_AS_EXPECTED);

        final double d3 = (double) Long.MAX_VALUE;
        final Rational r3 = new Rational(Long.MAX_VALUE, 1L);
        final double actual3 = r3.doubleValue();
        assertEquals(d3, actual3, BigRationalTest.DOUBLE_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testToString() {

        final Rational r1 = new Rational(12345L, -67890L);
        final String actual1 = r1.toString();
        assertEquals("-823/4526", actual1, BigRationalTest.STRING_VALUE_NOT_AS_EXPECTED);

        final Rational r2 = new Rational(5L, 1L);
        final String actual2 = r2.toString();
        assertEquals("5", actual2, BigRationalTest.STRING_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testHashCode() {

        final Rational r1 = new Rational(BigRationalTest.TEST_LONG_4, BigRationalTest.TEST_LONG_5);
        final int expect = (int) (BigRationalTest.TEST_LONG_4 + Long.rotateLeft(BigRationalTest.TEST_LONG_5, 16));

        final int actual = r1.hashCode();
        assertEquals(expect, actual, BigRationalTest.HASH_CODE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testEquals() {

        final Rational r1 = new Rational(-BigRationalTest.TEST_LONG_4, BigRationalTest.TEST_LONG_5);
        final Rational r2 = new Rational(BigRationalTest.TEST_LONG_4, -BigRationalTest.TEST_LONG_5);
        final Rational r3 = new Rational(BigRationalTest.TEST_LONG_4, BigRationalTest.TEST_LONG_6);
        final Rational r4 = new Rational(BigRationalTest.TEST_LONG_4 * -2L, BigRationalTest.TEST_LONG_6 * -2L);

        assertEquals(r1, r2, BigRationalTest.VALUES_EXPECTED_TO_BE_EQUAL);

        assertNotEquals(r1, r3, BigRationalTest.VALUES_EXPECTED_TO_BE_UNEQUAL);
        assertNotEquals(r1, r4, BigRationalTest.VALUES_EXPECTED_TO_BE_UNEQUAL);

        assertNotEquals(r2, r3, BigRationalTest.VALUES_EXPECTED_TO_BE_UNEQUAL);
        assertNotEquals(r2, r4, BigRationalTest.VALUES_EXPECTED_TO_BE_UNEQUAL);

        assertEquals(r3, r4, BigRationalTest.VALUES_EXPECTED_TO_BE_EQUAL);
    }

    /** A test case. */
    @Test
    void reciprocal() {

        final Rational r1 = new Rational(-BigRationalTest.TEST_LONG_4, BigRationalTest.TEST_LONG_5);
        final Rational reciprocal = r1.reciprocal();

        assertEquals(-BigRationalTest.TEST_LONG_5, reciprocal.numerator, BigRationalTest.NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigRationalTest.TEST_LONG_4, reciprocal.denominator, BigRationalTest.DENOMINATOR_NOT_AS_EXPECTED);
    }
}