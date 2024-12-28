package dev.mathops.commons.parser;

import dev.mathops.commons.builder.HtmlBuilder;

/**
 * Utility class to encode and decode hexadecimal strings.
 */
public enum HexEncoder {
    ;

    /** Hex characters with uppercase. */
    private static final char[] UC_HEX = "0123456789ABCDEF".toCharArray();

    /** Hex characters with lowercase. */
    private static final char[] LC_HEX = "0123456789abcdef".toCharArray();

    /**
     * Decodes a string of hex characters into a byte array.
     *
     * @param hex the hex characters
     * @return the decoded byte array
     * @throws IllegalArgumentException if the hex string is invalid (has odd length or contains a character that is not
     *                                  in a range '0'-'9', 'a'-'f', or 'A'-'F'. Leading "0x" or trailing 'h' characters
     *                                  are not permitted)
     */
    public static byte[] decode(final String hex) throws IllegalArgumentException {

        final int numBytes = hex.length() / 2;
        if (hex.length() != numBytes << 1) {
            final String message = Res.fmt(Res.INVALID_HEX_LEN, hex);
            throw new IllegalArgumentException(message);
        }

        final byte[] bytes = new byte[numBytes];

        for (int i = 0; i < numBytes; ++i) {
            final char chr2 = hex.charAt((i << 1) + 1);
            final int lo = decodeNibble(chr2);
            if (lo < 0) {
                final String message = Res.fmt(Res.INVALID_HEX, hex);
                throw new IllegalArgumentException(message);
            }
            final char chr1 = hex.charAt(i << 1);
            final int hi = decodeNibble(chr1);
            if (hi < 0) {
                final String message = Res.fmt(Res.INVALID_HEX, hex);
                throw new IllegalArgumentException(message);
            }
            bytes[i] = (byte) ((hi << 4) + lo);
        }

        return bytes;
    }

    /**
     * Attempts to decode a nibble from a character, which must be in the range '0'-'9', 'a'-'f', or 'A'-'F'.
     *
     * @param chr the character
     * @return the decoded nibble, -1 if invalid
     */
    private static int decodeNibble(final char chr) {

        final int nibble;

        if ((int) chr >= (int) '0' && (int) chr <= (int) '9') {
            nibble = (int) chr - (int) '0';
        } else if ((int) chr >= (int) 'a' && (int) chr <= (int) 'f') {
            nibble = (int) chr - (int) 'a' + 10;
        } else if ((int) chr >= (int) 'A' && (int) chr <= (int) 'F') {
            nibble = (int) chr - (int) 'A' + 10;
        } else {
            nibble = -1;
        }

        return nibble;
    }

    /**
     * Encodes an array of bytes as hexadecimal, using uppercase letters 'A' through 'F'.
     *
     * @param bytes the byte array
     * @return the encoded hex string
     */
    public static String encodeUppercase(final byte[] bytes) {

        final HtmlBuilder htm = new HtmlBuilder(bytes.length << 1);

        for (final byte aByte : bytes) {
            htm.add(UC_HEX[((int) aByte >> 4) & 0x0F]);
            htm.add(UC_HEX[(int) aByte & 0x0F]);
        }

        return htm.toString();
    }

    /**
     * Encodes a nibble as a single hexadecimal character, using uppercase letters 'A' through 'F'.
     *
     * @param nibble the nibble
     * @return the encoded hex character
     */
    public static char encodeNibble(final int nibble) {

        return UC_HEX[nibble & 0x0F];
    }

    /**
     * Encodes an array of bytes as hexadecimal, using lowercase letters 'A' through 'F'.
     *
     * @param bytes the byte array
     * @return the encoded hex string
     */
    public static String encodeLowercase(final byte[] bytes) {

        final HtmlBuilder htm = new HtmlBuilder(bytes.length << 1);

        for (final byte aByte : bytes) {
            htm.add(LC_HEX[((int) aByte >> 4) & 0x0F]);
            htm.add(LC_HEX[(int) aByte & 0x0F]);
        }

        return htm.toString();
    }
}
