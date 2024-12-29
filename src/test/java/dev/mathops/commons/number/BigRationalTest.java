package dev.mathops.commons.number;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test cases for the {code BigRational} class.
 */
class BigRationalTest {

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
    final void constructorLL1() {

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
    final void constructorLL2() {

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
    final void constructorLL3() {

        final BigRational r = new BigRational(BigInteger.ZERO, BigInteger.valueOf(-515L));

        assertEquals(BigInteger.ZERO, r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.ONE, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    final void constructorL1() {

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

        assertEquals(BigInteger.valueOf(-1L), r.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.TWO, r.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf3() {
        final BigRational r1 = BigRational.valueOf(TEST_RATIONAL_STRING_1);
        final BigRational r2 = new BigRational(BigInteger.valueOf(27672560985L), BigInteger.valueOf(60482444799L));

        assertEquals(r2.numerator, r1.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(r2.denominator, r1.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void valueOf4() {
        final BigRational r1 = BigRational.valueOf(TEST_RATIONAL_STRING_2);
        final BigRational r2 = new BigRational(BigInteger.valueOf(27672560985L));

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
        final BigRational r1 = new BigRational(BigInteger.valueOf(TEST_LONG_2));
        assertEquals(TEST_INT_1, r1.intValue(), INTEGER_VALUE_NOT_AS_EXPECTED);

        final BigRational r2 = new BigRational(BigInteger.valueOf(TEST_LONG_1));
        assertEquals((int) TEST_LONG_1, r2.intValue(), INTEGER_VALUE_NOT_AS_EXPECTED);

        final BigRational r3 = new BigRational(BigInteger.valueOf(10L), BigInteger.valueOf(TEST_LONG_3));
        assertEquals(3, r3.intValue(), INTEGER_VALUE_NOT_AS_EXPECTED);

        final BigRational r4 = new BigRational(BigInteger.valueOf(-10L), BigInteger.valueOf(TEST_LONG_3));
        assertEquals(-3, r4.intValue(), INTEGER_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void longValue() {
        final BigRational r1 = new BigRational(BigInteger.valueOf(TEST_LONG_2));
        assertEquals(TEST_LONG_2, r1.longValue(), LONG_VALUE_NOT_AS_EXPECTED);

        final BigRational r2 = new BigRational(BigInteger.valueOf(TEST_LONG_1));
        assertEquals(TEST_LONG_1, r2.longValue(), LONG_VALUE_NOT_AS_EXPECTED);

        final BigRational r3 = new BigRational(BigInteger.valueOf(10L), BigInteger.valueOf(TEST_LONG_3));
        assertEquals(TEST_LONG_3, r3.longValue(), LONG_VALUE_NOT_AS_EXPECTED);

        final BigRational r4 = new BigRational(BigInteger.valueOf(-10L), BigInteger.valueOf(TEST_LONG_3));
        assertEquals(-TEST_LONG_3, r4.longValue(), LONG_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void floatValue() {
        final double d1 = 12345.0 / 67890.0;
        final BigRational r1 = new BigRational(BigInteger.valueOf(12345L), BigInteger.valueOf(67890L));
        assertEquals((float) d1, r1.floatValue(), FLOAT_VALUE_NOT_AS_EXPECTED);

        final double d2 = -67890.0 / 12345.0;
        final BigRational r2 = new BigRational(BigInteger.valueOf(-67890L), BigInteger.valueOf(12345L));
        assertEquals((float) d2, r2.floatValue(), FLOAT_VALUE_NOT_AS_EXPECTED);

        final double d3 = (double) Long.MAX_VALUE;
        final BigRational r3 = new BigRational(BigInteger.valueOf(Long.MAX_VALUE), BigInteger.valueOf(1L));
        assertEquals((float) d3, r3.floatValue(), FLOAT_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void doubleValue() {
        final double d1 = 12345.0 / 67890.0;
        final BigRational r1 = new BigRational(BigInteger.valueOf(12345L), BigInteger.valueOf(67890L));
        assertEquals(d1, r1.doubleValue(), DOUBLE_VALUE_NOT_AS_EXPECTED);

        final double d2 = -67890.0 / 12345.0;
        final BigRational r2 = new BigRational(BigInteger.valueOf(-67890L), BigInteger.valueOf(12345L));
        assertEquals(d2, r2.doubleValue(), DOUBLE_VALUE_NOT_AS_EXPECTED);

        final double d3 = (double) Long.MAX_VALUE;
        final BigRational r3 = new BigRational(BigInteger.valueOf(Long.MAX_VALUE), BigInteger.valueOf(1L));
        assertEquals(d3, r3.doubleValue(), DOUBLE_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testToString() {
        final BigRational r1 = new BigRational(BigInteger.valueOf(12345L), BigInteger.valueOf(-67890L));
        assertEquals("-823/4526", r1.toString(), STRING_VALUE_NOT_AS_EXPECTED);

        final BigRational r2 = new BigRational(BigInteger.valueOf(5L), BigInteger.valueOf(1L));
        assertEquals("5", r2.toString(), STRING_VALUE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testHashCode() {
        BigInteger numerator = BigInteger.valueOf(TEST_LONG_4);
        BigInteger denominator = BigInteger.valueOf(TEST_LONG_5);
        final BigRational r1 = new BigRational(numerator, denominator);
        final int expect = numerator.hashCode() + denominator.hashCode();

        assertEquals(expect, r1.hashCode(), HASH_CODE_NOT_AS_EXPECTED);
    }

    /** A test case. */
    @Test
    void testEquals() {

        final BigRational r1 = new BigRational(BigInteger.valueOf(-TEST_LONG_4), BigInteger.valueOf(TEST_LONG_5));
        final BigRational r2 = new BigRational(BigInteger.valueOf(TEST_LONG_4), BigInteger.valueOf(-TEST_LONG_5));
        final BigRational r3 = new BigRational(BigInteger.valueOf(TEST_LONG_4), BigInteger.valueOf(TEST_LONG_6));
        final BigRational r4 = new BigRational(BigInteger.valueOf(TEST_LONG_4 * -2L),
                BigInteger.valueOf(TEST_LONG_6 * -2L));

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
        final BigRational r1 = new BigRational(BigInteger.valueOf(-TEST_LONG_4), BigInteger.valueOf(TEST_LONG_5));
        final BigRational reciprocal = r1.reciprocal();

        assertEquals(BigInteger.valueOf(-TEST_LONG_5), reciprocal.numerator, NUMERATOR_NOT_AS_EXPECTED);
        assertEquals(BigInteger.valueOf(TEST_LONG_4), reciprocal.denominator, DENOMINATOR_NOT_AS_EXPECTED);
    }
}