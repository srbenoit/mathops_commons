package dev.mathops.commons.number;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test cases for the {code BigRational} class.
 */
final class BigRationalTest {

    /** An assertion message. */
    static final String NUMERATOR_NOT_AS_EXPECTED = "Numerator not as expected";

    /** An assertion message. */
    static final String DENOMINATOR_NOT_AS_EXPECTED = "Denominator not as expected";

    /** An assertion message. */
    static final String INTEGER_VALUE_NOT_AS_EXPECTED = "Integer value not as expected";

    /** An assertion message. */
    static final String LONG_VALUE_NOT_AS_EXPECTED = "Long value not as expected";

    /** An assertion message. */
    static final String FLOAT_VALUE_NOT_AS_EXPECTED = "Float value not as expected";

    /** An assertion message. */
    static final String DOUBLE_VALUE_NOT_AS_EXPECTED = "Double value not as expected";

    /** An assertion message. */
    static final String STRING_VALUE_NOT_AS_EXPECTED = "String value not as expected";

    /** An assertion message. */
    static final String VALUES_EXPECTED_TO_BE_EQUAL = "Valued expected to be equal";

    /** An assertion message. */
    static final String VALUES_EXPECTED_TO_BE_UNEQUAL = "Valued expected to be unequal";

    /** An assertion message. */
    static final String HASH_CODE_NOT_AS_EXPECTED = "Hash code not as expected";

    /** A test string for parsing of BigRational. */
    static final String TEST_RATIONAL_STRING_1 = "27672560985/60482444799";

    /** A test string for parsing of BigRational. */
    static final String TEST_RATIONAL_STRING_2 = " 27672560985 ";

    /** A test integer value. */
    static final int TEST_INT_1 = 123456789;

    /** A test long value. */
    static final long TEST_LONG_1 = 123456789123456L;

    /** A test long value. */
    static final long TEST_LONG_2 = 123456789L;

    /** A test long value. */
    static final long TEST_LONG_3 = 3L;

    /** A test long value. */
    static final long TEST_LONG_4 = 823L;

    /** A test long value. */
    static final long TEST_LONG_5 = 4526L;

    /** A test long value. */
    static final long TEST_LONG_6 = 4527L;

    /**
     * Constructs a new {code BigRationalTest}.
     */
    BigRationalTest() {

        // No action
    }

    /** A test case. */
    @Test
    void constructorLL1() {

        final BigInteger numerator = BigInteger.valueOf(TEST_LONG_3 * 5L * 17L * 193L * 227L * 2477L);
        final BigInteger denominator = BigInteger.valueOf(-TEST_LONG_3 * 7L * 23L * 193L * 263L * 2467L);
        final BigRational r = new BigRational(numerator, denominator);

        // Common factors are 3, 193
        final BigInteger common = BigInteger.valueOf(TEST_LONG_3 * 193L);
        final BigInteger expectNumerator = numerator.negate().divide(common);
        final BigInteger expectDenominator = denominator.negate().divide(common);

        assertEquals(expectNumerator, r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(expectDenominator, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void constructorLL2() {

        final BigInteger numerator = BigInteger.valueOf(TEST_LONG_3 * 5L * 17L * 193L * 227L * 2477L);
        final BigInteger denominator = BigInteger.valueOf(TEST_LONG_3 * 193L);
        final BigRational r = new BigRational(numerator, denominator);

        // Common factors are 3, 193
        final BigInteger common = BigInteger.valueOf(TEST_LONG_3 * 193L);
        final BigInteger expectNumerator = numerator.divide(common);
        final BigInteger expectDenominator = denominator.divide(common);

        assertEquals(expectNumerator, r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(expectDenominator, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void constructorLL3() {

        final BigInteger denominator = BigInteger.valueOf(-515L);
        final BigRational r = new BigRational(BigInteger.ZERO, denominator);

        assertEquals(BigInteger.ZERO, r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.ONE, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void constructorL1() {

        final BigInteger numerator = BigInteger.valueOf(TEST_LONG_3 * 5L * 17L * 193L * 227L * 2477L);
        final BigRational r = new BigRational(numerator);

        assertEquals(numerator, r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.ONE, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf1() {

        final BigRational r = BigRational.valueOf("1/2");

        assertEquals(BigInteger.ONE, r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.TWO, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf2() {

        final BigRational r = BigRational.valueOf("  -1 / 2  ");

        final BigInteger expected = BigInteger.valueOf(-1L);
        assertEquals(expected, r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.TWO, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf3() {

        final BigRational r1 = BigRational.valueOf(TEST_RATIONAL_STRING_1);
        final BigInteger numerator = BigInteger.valueOf(27672560985L);
        final BigInteger denominator = BigInteger.valueOf(60482444799L);
        final BigRational r2 = new BigRational(numerator, denominator);

        assertEquals(r2.numerator, r1.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(r2.denominator, r1.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf4() {

        final BigRational r1 = BigRational.valueOf(TEST_RATIONAL_STRING_2);
        final BigInteger integer = BigInteger.valueOf(27672560985L);
        final BigRational r2 = new BigRational(integer);

        assertEquals(r2.numerator, r1.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(r2.denominator, r1.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf5() {

        final BigRational r1 = BigRational.valueOf(" 0/515 ");

        assertEquals(BigInteger.ZERO, r1.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.ONE, r1.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void intValue() {

        final BigInteger value1 = BigInteger.valueOf(TEST_LONG_2);
        final BigRational r1 = new BigRational(value1);
        final int actual1 = r1.intValue();
        assertEquals(TEST_INT_1, actual1, INTEGER_VALUE_NOT_AS_EXPECTED);

        final BigInteger value2 = BigInteger.valueOf(TEST_LONG_1);
        final BigRational r2 = new BigRational(value2);
        final int actual2 = r2.intValue();
        assertEquals((int) TEST_LONG_1, actual2, INTEGER_VALUE_NOT_AS_EXPECTED);

        final BigInteger numerator3 = BigInteger.valueOf(10L);
        final BigInteger denominator3 = BigInteger.valueOf(TEST_LONG_3);
        final BigRational r3 = new BigRational(numerator3, denominator3);
        final int actual3 = r3.intValue();
        assertEquals(3, actual3, INTEGER_VALUE_NOT_AS_EXPECTED);

        final BigInteger numerator4 = BigInteger.valueOf(-10L);
        final BigInteger denominator4 = BigInteger.valueOf(TEST_LONG_3);
        final BigRational r4 = new BigRational(numerator4, denominator4);
        final int actual4 = r4.intValue();
        assertEquals(-3, actual4, INTEGER_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void longValue() {

        final BigInteger value1 = BigInteger.valueOf(TEST_LONG_2);
        final BigRational r1 = new BigRational(value1);
        final long actual1 = r1.longValue();
        assertEquals(TEST_LONG_2, actual1, LONG_VALUE_NOT_AS_EXPECTED);

        final BigInteger value2 = BigInteger.valueOf(TEST_LONG_1);
        final BigRational r2 = new BigRational(value2);
        final long actual2 = r2.longValue();
        assertEquals(TEST_LONG_1, actual2, LONG_VALUE_NOT_AS_EXPECTED);

        final BigInteger numerator3 = BigInteger.valueOf(10L);
        final BigInteger denominator3 = BigInteger.valueOf(TEST_LONG_3);
        final BigRational r3 = new BigRational(numerator3, denominator3);
        final long actual3 = r3.longValue();
        assertEquals(TEST_LONG_3, actual3, LONG_VALUE_NOT_AS_EXPECTED);

        final BigInteger numerator4 = BigInteger.valueOf(-10L);
        final BigInteger denominator4 = BigInteger.valueOf(TEST_LONG_3);
        final BigRational r4 = new BigRational(numerator4, denominator4);
        final long actual4 = r4.longValue();
        assertEquals(-TEST_LONG_3, actual4, LONG_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void floatValue() {

        final double d1 = 12345.0 / 67890.0;
        final BigInteger num1 = BigInteger.valueOf(12345L);
        final BigInteger den1 = BigInteger.valueOf(67890L);
        final BigRational r1 = new BigRational(num1, den1);
        final float actual1 = r1.floatValue();
        assertEquals((float) d1, actual1, FLOAT_VALUE_NOT_AS_EXPECTED);

        final double d2 = -67890.0 / 12345.0;
        final BigInteger num2 = BigInteger.valueOf(-67890L);
        final BigInteger den2 = BigInteger.valueOf(12345L);
        final BigRational r2 = new BigRational(num2, den2);
        final float actual2 = r2.floatValue();
        assertEquals((float) d2, actual2, FLOAT_VALUE_NOT_AS_EXPECTED);

        final double d3 = (double) Long.MAX_VALUE;
        final BigInteger num3 = BigInteger.valueOf(Long.MAX_VALUE);
        final BigInteger den3 = BigInteger.valueOf(1L);
        final BigRational r3 = new BigRational(num3, den3);
        final float actual3 = r3.floatValue();
        assertEquals((float) d3, actual3, FLOAT_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void doubleValue() {

        final double d1 = 12345.0 / 67890.0;
        final BigInteger num1 = BigInteger.valueOf(12345L);
        final BigInteger den1 = BigInteger.valueOf(67890L);
        final BigRational r1 = new BigRational(num1, den1);
        final double actual1 = r1.doubleValue();
        assertEquals(d1, actual1, DOUBLE_VALUE_NOT_AS_EXPECTED);

        final double d2 = -67890.0 / 12345.0;
        final BigInteger num2 = BigInteger.valueOf(-67890L);
        final BigInteger den2 = BigInteger.valueOf(12345L);
        final BigRational r2 = new BigRational(num2, den2);
        final double actual2 = r2.doubleValue();
        assertEquals(d2, actual2, DOUBLE_VALUE_NOT_AS_EXPECTED);

        final double d3 = (double) Long.MAX_VALUE;
        final BigInteger num3 = BigInteger.valueOf(Long.MAX_VALUE);
        final BigInteger den3 = BigInteger.valueOf(1L);
        final BigRational r3 = new BigRational(num3, den3);
        final double actual3 = r3.doubleValue();
        assertEquals(d3, actual3, DOUBLE_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testToString() {
        final BigInteger num1 = BigInteger.valueOf(12345L);
        final BigInteger den1 = BigInteger.valueOf(-67890L);
        final BigRational r1 = new BigRational(num1, den1);
        final String actual1 = r1.toString();
        assertEquals("-823/4526", actual1, STRING_VALUE_NOT_AS_EXPECTED);

        final BigInteger num2 = BigInteger.valueOf(5L);
        final BigInteger den2 = BigInteger.valueOf(1L);
        final BigRational r2 = new BigRational(num2, den2);
        final String actual2 = r2.toString();
        assertEquals("5", actual2, STRING_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testHashCode() {

        final BigInteger numerator = BigInteger.valueOf(TEST_LONG_4);
        final BigInteger denominator = BigInteger.valueOf(TEST_LONG_5);
        final BigRational r1 = new BigRational(numerator, denominator);
        final int expect = numerator.hashCode() + denominator.hashCode();

        final int actual = r1.hashCode();
        assertEquals(expect, actual, HASH_CODE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testEquals() {

        final BigInteger num1 = BigInteger.valueOf(-TEST_LONG_4);
        final BigInteger den1 = BigInteger.valueOf(TEST_LONG_5);
        final BigRational r1 = new BigRational(num1, den1);
        final BigInteger num2 = BigInteger.valueOf(TEST_LONG_4);
        final BigInteger den2 = BigInteger.valueOf(-TEST_LONG_5);
        final BigRational r2 = new BigRational(num2, den2);
        final BigInteger num3 = BigInteger.valueOf(TEST_LONG_4);
        final BigInteger den3 = BigInteger.valueOf(TEST_LONG_6);
        final BigRational r3 = new BigRational(num3, den3);
        final BigInteger num4 = BigInteger.valueOf(TEST_LONG_4 * -2L);
        final BigInteger den4 = BigInteger.valueOf(TEST_LONG_6 * -2L);
        final BigRational r4 = new BigRational(num4, den4);

        assertEquals(r1, r2, VALUES_EXPECTED_TO_BE_EQUAL);

        assertNotEquals(r1, r3, VALUES_EXPECTED_TO_BE_UNEQUAL);
        assertNotEquals(r1, r4, VALUES_EXPECTED_TO_BE_UNEQUAL);

        assertNotEquals(r2, r3, VALUES_EXPECTED_TO_BE_UNEQUAL);
        assertNotEquals(r2, r4, VALUES_EXPECTED_TO_BE_UNEQUAL);

        assertEquals(r3, r4, VALUES_EXPECTED_TO_BE_EQUAL);
    }

    /** A test case. */
    @Test
    void reciprocal() {

        final BigInteger numerator = BigInteger.valueOf(-TEST_LONG_4);
        final BigInteger denominator = BigInteger.valueOf(TEST_LONG_5);
        final BigRational r1 = new BigRational(numerator, denominator);
        final BigRational reciprocal = r1.reciprocal();

        final BigInteger expectNumerator = BigInteger.valueOf(-TEST_LONG_5);
        assertEquals(expectNumerator, reciprocal.numerator, NUMERATOR_NOT_AS_EXPECTED);
        final BigInteger expectDenominator = BigInteger.valueOf(TEST_LONG_4);
        assertEquals(expectDenominator, reciprocal.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }
}