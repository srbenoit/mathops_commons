package dev.mathops.commons.parser.xml;

import dev.mathops.commons.res.ResBundle;

import java.util.Locale;

/**
 * Contains resources for the parser XML package.
 */
final class Res extends ResBundle {

    /** A resource key. */
    static final String MISS_ATTR = key(1);

    /** A resource key. */
    static final String INT_ERR = key(2);

    /** A resource key. */
    static final String LONG_ERR = key(3);

    /** A resource key. */
    static final String FLOAT_ERR = key(4);

    /** A resource key. */
    static final String BOOLEAN_ERR = key(5);

    /** A resource key. */
    static final String BAD_TAG = key(6);

    /** A resource key. */
    static final String BAD_ENDTAG = key(7);

    /** A resource key. */
    static final String MISS_EQ = key(8);

    /** A resource key. */
    static final String BAD_ATSPEC = key(9);

    /** A resource key. */
    static final String BAD_ATNAME = key(10);

    /** A resource key. */
    static final String UNSUP_TAG = key(11);

    /** A resource key. */
    static final String BAD_CDATA = key(12);

    /** A resource key. */
    static final String BAD_ENCCHAR = key(13);

    /** A resource key. */
    static final String BAD_ENCCHAR1 = key(14);

    /** A resource key. */
    static final String NO_OPEN = key(15);

    /** A resource key. */
    static final String BAD_CLOSE = key(16);

    /** A resource key. */
    static final String BAD_CHAR = key(17);

    /** A resource key. */
    static final String BAD_EOF = key(18);

    /** A resource key. */
    static final String XML_FILE_FILTER_DESC = key(19);

    /** A resource key. */
    static final String BAD_XML_DECL = key(20);

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {MISS_ATTR, "Missing {0} attribute in {1}"},
            {INT_ERR, "Unable to convert value to integer"},
            {LONG_ERR, "Unable to convert value to long"},
            {FLOAT_ERR, "Unable to convert value to float"},
            {BOOLEAN_ERR, "Unable to convert value to boolean"},
            {BAD_TAG, "Invalid tag name in tag {0}: {1}"},
            {BAD_ENDTAG, "Invalid tag name in end tag {0}: {1}"},
            {MISS_EQ, "Missing '=' after attribute name in tag {0}."},
            {BAD_ATSPEC, "Invalid {0} attribute specification in tag [ {1} ]"},
            {BAD_ATNAME, "Invalid attribute name in tag {0}: {1}"},
            {UNSUP_TAG, "Unsupported tag"},
            {BAD_CDATA, "Invalid CDATA start tag"},
            {BAD_ENCCHAR, "'{0}' not valid character in encoding"},
            {BAD_ENCCHAR1, "'{0}' not valid first character in encoding"},
            {NO_OPEN, "Closing tag with no matching opening tag"},
            {BAD_CLOSE, "Closing tag does not match currently open {0} tag"},
            {BAD_CHAR, "Invalid XML character"},
            {BAD_EOF, "Unexpected end of XML file"},

            {XML_FILE_FILTER_DESC, "XML files (.xml)"},
            {BAD_XML_DECL, "Invalid XML declaration; expecting <?xml version='...' encoding='...' standalone='...'?>"},
    };

    /** The singleton instance. */
    private static final Res instance = new Res();

    /**
     * Private constructor to prevent direct instantiation.
     */
    private Res() {

        super(Locale.US, EN_US);
    }

    /**
     * Gets the message with a specified key using the current locale.
     *
     * @param key the message key
     * @return the best-matching message, an empty string if none is registered (never {@code null})
     */
    static String get(final String key) {

        return instance.getMsg(key);
    }

    /**
     * Retrieves the message with a specified key, then uses a {@code MessageFormat} to format that message pattern with
     * a collection of arguments.
     *
     * @param key       the message key
     * @param arguments the arguments, as for {@code MessageFormat}
     * @return the formatted string (never {@code null})
     */
    static String fmt(final String key, final Object... arguments) {

        return instance.formatMsg(key, arguments);
    }
}
