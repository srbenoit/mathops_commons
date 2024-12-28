package dev.mathops.commons.installation;

import dev.mathops.commons.res.ResBundle;

import java.util.Locale;

/**
 * Localized resources.
 */
final class Res extends ResBundle {

    // Resources used by Installation

    /** A resource key. */
    static final String CFG_FILE_NONEXISTENT = key(1);

    /** A resource key. */
    static final String CANT_READ_CFG_FILE = key(2);

    /** A resource key. */
    static final String CANT_CREATE_DIR = key(3);

    /** A resource key. */
    static final String PATH_IS_NOT_DIR = key(4);


    // Resources used by PathList

    /** A resource key. */
    static final String PATH_LIST_READ_ERR= key(5);

    /** A resource key. */
    static final String PATH_LIST_NO_BASE= key(6);

    /** A resource key. */
    static final String PATH_LIST_WRITE_ERR= key(7);

    //

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {

            {CFG_FILE_NONEXISTENT, "Configuration file ''{0}'' does not exist",},
            {CANT_READ_CFG_FILE, "Unable to load configuration file ''{0}''",},
            {CANT_CREATE_DIR, "Unable to create directory ''{0}''",},
            {PATH_IS_NOT_DIR, "Path ''{0}'' exists but is not a directory",},

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
