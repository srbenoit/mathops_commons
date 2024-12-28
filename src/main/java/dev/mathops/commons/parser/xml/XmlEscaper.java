package dev.mathops.commons.parser.xml;

import dev.mathops.commons.builder.HtmlBuilder;

/**
 * A utility class to escape strings for use in XML (as an attribute value, for example) and recover the original
 * strings from such escaped string values.
 */
public enum XmlEscaper {
    ;

    /** The quote escape. */
    public static final String QUOT = "&quot;";

    /** The apostrophe escape. */
    public static final String APOS = "&apos;";

    /** The less-than escape. */
    public static final String LEFT = "&lt;";

    /** The greater-than escape. */
    public static final String RIGHT = "&gt;";

    /** The ampersand escape. */
    public static final String AMP = "&amp;";

    /** The initial size of the buffer. */
    private static final int INIT_BUFFER_SIZE = 100;

    /** Offset of a hex value in the escape "&#x12ab". */
    private static final int HEX_VALUE_OFFSET = 3;

    /** Offset of a hex value in the escape "&#1234". */
    private static final int DEC_VALUE_OFFSET = 2;

    /** Base of hex numbers. */
    private static final int HEX_BASE = 16;

    /**
     * Escapes a string for use in an XML attribute.
     *
     * @param orig the original string to escape
     * @return the escaped string
     */
    public static String escape(final String orig) {

        final char[] chars = orig.toCharArray();

        final HtmlBuilder builder = new HtmlBuilder(chars.length * 11 / 10);

        for (final char chr : chars) {
            if ((int) chr == (int) '\"') {
                builder.add(QUOT);
            } else if ((int) chr == (int) '\'') {
                builder.add(APOS);
            } else if ((int) chr == (int) '<') {
                builder.add(LEFT);
            } else if ((int) chr == (int) '>') {
                builder.add(RIGHT);
            } else if ((int) chr == (int) '&') {
                builder.add(AMP);
            } else {
                builder.add(chr);
            }
        }

        return builder.toString();
    }

    /**
     * Unescapes a string that was processed by {@code escape}.
     *
     * @param escaped the string to unescape
     * @return the original (unescaped) string
     */
    public static String unescape(final String escaped) {

        final int len = escaped.length();

        final HtmlBuilder builder = new HtmlBuilder(len);

        int pos = 0;

        while (pos < len) {
            final char chr = escaped.charAt(pos);

            if ((int) chr == (int) '&') {

                if (isEscape(escaped, pos, QUOT)) {
                    builder.add('\"');
                    pos += QUOT.length();
                } else if (isEscape(escaped, pos, APOS)) {
                    builder.add('\'');
                    pos += APOS.length();
                } else if (isEscape(escaped, pos, LEFT)) {
                    builder.add('<');
                    pos += LEFT.length();
                } else if (isEscape(escaped, pos, RIGHT)) {
                    builder.add('>');
                    pos += RIGHT.length();
                } else if (isEscape(escaped, pos, AMP)) {
                    builder.add('&');
                    pos += AMP.length();
                } else {
                    pos = unescapeSingle(builder, escaped, pos);
                }
            } else {
                builder.add(chr);
                ++pos;
            }
        }

        return builder.toString();
    }

    /**
     * Processes a single escape sequence. This method must be called from within a block that is synchronized on
     * {@code STR}.
     *
     * @param builder the {@code HtmlBuilder} to which to append
     * @param escaped the string to unescape
     * @param start   the position of the '&' at the start of the escape
     * @return the position after the escape
     */
    private static int unescapeSingle(final HtmlBuilder builder, final String escaped, final int start) {

        int pos = start;
        final int semicolon = escaped.indexOf((int) ';', pos + 1);

        if (semicolon == -1 || (int) escaped.charAt(pos + 1) != (int) '#') {
            final char chr = escaped.charAt(pos);
            builder.add(chr);
            ++pos;
        } else if ((int) escaped.charAt(pos + 2) == (int) 'x') {
            boolean valid = true;

            for (int i = pos + HEX_VALUE_OFFSET; valid && i < semicolon; ++i) {
                final char chr = escaped.charAt(i);
                valid = XmlChars.isHex((int) chr);
            }

            if (valid) {
                final String codePointStr = escaped.substring(pos + HEX_VALUE_OFFSET, semicolon);
                final int codePoint = Integer.parseInt(codePointStr, HEX_BASE);
                final char[] chars = Character.toChars(codePoint);
                builder.add(chars);
            }

            pos = semicolon + 1;
        } else {
            boolean valid = true;

            for (int i = pos + DEC_VALUE_OFFSET; valid && i < semicolon; ++i) {
                final char chr = escaped.charAt(i);
                valid = XmlChars.isDigit((int) chr);
            }

            if (valid) {
                final String codePointStr = escaped.substring(pos + DEC_VALUE_OFFSET, semicolon);
                final int codePoint = Integer.parseInt(codePointStr);
                final char[] chars = Character.toChars(codePoint);
                builder.add(chars);
            }

            pos = semicolon + 1;
        }

        return pos;
    }

    /**
     * Tests whether a character sequence has a given escape sequence at a specified position.
     *
     * @param escaped the character sequence
     * @param pos     the position
     * @param esc     the escape sequence to test for
     * @return {@code true} if the code sequence does contain the escape at the given position; {@code false} otherwise
     */
    private static boolean isEscape(final CharSequence escaped, final int pos, final CharSequence esc) {

        final int len = esc.length();
        boolean hit = escaped.length() >= (pos + len);

        for (int i = 1; hit && i < len; ++i) {
            hit = (int) escaped.charAt(pos + i) == (int) esc.charAt(i);
        }

        return hit;
    }
}
