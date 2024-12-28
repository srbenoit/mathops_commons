package dev.mathops.commons.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@code Builder} class.
 */
final class TestBuilder {

    /**
     * A test case.
     */
    @Test
    @DisplayName("Empty builder length")
    void test001() {

        final HtmlBuilder htm = new HtmlBuilder(100);

        final int length = htm.length();
        assertEquals(0, length, "Expected length to be zero");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Builder length")
    void test002() {

        final HtmlBuilder htm = new HtmlBuilder(100);
        htm.appendChar('x');

        final int length = htm.length();
        assertEquals(1, length, "Expected length to be 1");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Builder charAt(0)")
    void test003() {

        final HtmlBuilder htm = new HtmlBuilder(100);
        htm.appendChar('x');

        final char actual = htm.charAt(0);
        assertEquals('x', actual, "Expected charAt(0) to be 'x'");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Builder reset length")
    void test004() {

        final HtmlBuilder htm = new HtmlBuilder(100);
        htm.appendChar('x');
        htm.reset();

        final int length = htm.length();
        assertEquals(0, length, "Expected reset length to be zero");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Builder truncated length")
    void test005() {

        final HtmlBuilder htm = new HtmlBuilder(100);
        htm.appendChar('x');
        htm.appendChar('y');
        htm.appendChar('z');
        htm.truncate(2);

        final int length = htm.length();
        assertEquals(2, length, "Expected truncated length to be 2");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Builder truncated charAt")
    void test006() {

        final HtmlBuilder htm = new HtmlBuilder(100);
        htm.appendChar('x');
        htm.appendChar('y');
        htm.appendChar('z');
        htm.truncate(1);

        final char actual = htm.charAt(0);
        assertEquals('x', actual, "Expected truncated charAt(0) to be 'x'");
    }

    /**
     * A test case.
     */
    @Test
    @DisplayName("Builder toString")
    void test007() {

        final HtmlBuilder htm = new HtmlBuilder(100);
        htm.appendChar('x');
        htm.appendChar('y');
        htm.appendChar('z');

        final String actual = htm.toString();
        assertEquals("xyz", actual, "Expected toString() to be 'xyz'");
    }
}
