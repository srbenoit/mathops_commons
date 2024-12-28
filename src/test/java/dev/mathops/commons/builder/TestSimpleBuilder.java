package dev.mathops.commons.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@code SimpleBuilder} class.
 */
final class TestSimpleBuilder {

    /**
     * A test case.
     */
    @Test
    @DisplayName("String value after concat()")
    void test001() {

        final Integer int1 = Integer.valueOf(1);
        final Character charb = Character.valueOf('B');
        final Double double2 = Double.valueOf(2.0);
        final char[] fooChars = "foo".toCharArray();

        final String str = SimpleBuilder.concat("A", int1, charb, double2, Boolean.TRUE, Boolean.FALSE, null, fooChars);

        assertEquals("A1B2.0truefalsefoo", str, "Expected concat() to be 'A1B2.0truefalsefoo' (" + str + ")");
    }
}
