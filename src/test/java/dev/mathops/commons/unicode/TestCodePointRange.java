package dev.mathops.commons.unicode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the CodePointRange class.
 */
final class TestCodePointRange {

    /** Test case. */
    @Test
    @DisplayName("Constructor min value")
    void testConstructor1() {

        final CodePointRange range = new CodePointRange(100, 200);

        assertEquals(100, range.min, "Constructor 1");
    }

    /** Test case. */
    @Test
    @DisplayName("Constructor max value")
    void testConstructor2() {

        final CodePointRange range = new CodePointRange(100, 200);

        assertEquals(200, range.max, "Constructor 2");
    }

    /** Test case. */
    @Test
    @DisplayName("IsInRange below lower bound")
    void testIsInRange1() {

        final CodePointRange range = new CodePointRange(100, 200);

        final boolean inRange = range.isInRange(99);
        assertFalse(inRange, "IsInRange 1");
    }

    /** Test case. */
    @Test
    @DisplayName("IsInRange equals lower bound")
    void testIsInRange2() {

        final CodePointRange range = new CodePointRange(100, 200);

        final boolean inRange = range.isInRange(100);
        assertTrue(inRange, "IsInRange 2");
    }

    /** Test case. */
    @Test
    @DisplayName("IsInRange equals upper bound")
    void testIsInRange3() {

        final CodePointRange range = new CodePointRange(100, 200);

        final boolean inRange = range.isInRange(200);
        assertTrue(inRange, "IsInRange 3");
    }

    /** Test case. */
    @Test
    @DisplayName("IsInRange above upper bound")
    void testIsInRange4() {

        final CodePointRange range = new CodePointRange(100, 200);

        final boolean inRange = range.isInRange(201);
        assertFalse(inRange, "IsInRange 4");
    }
}
