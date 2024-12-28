package dev.mathops.commons.file;

import dev.mathops.commons.res.ResBundle;

import java.util.Locale;

/**
 * Localized resources.
 */
final class Res extends ResBundle {

    /** A resource key. */
    static final String FILE_LOAD_FAIL = key(1);

    /** A resource key. */
    static final String FILE_NOT_FOUND = key(2);

    /** A resource key. */
    static final String XML_FILE_FILTER_DESC= key(3);

    //

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {FILE_LOAD_FAIL, "FileLoader failed to read file {0}"},
            {FILE_NOT_FOUND, "File not found: {0} - {1}"},
            {XML_FILE_FILTER_DESC, "XML files (.xml)"},

            //
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
