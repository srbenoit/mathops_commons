package dev.mathops.commons;

import dev.mathops.commons.res.ResBundle;

import java.util.Locale;

/**
 * Localized resources.
 */
final class Res extends ResBundle {

    /** A resource key. */
    static final String RND_UPPER_LIMIT_NOT_POS = key(1);

    // Used by ClassList

    /** A resource key. */
    static final String CANT_DOWNLOAD_JAR = key(2);

    /** A resource key. */
    static final String CANT_INSTANTIATE_JAR = key(3);

    /** A resource key. */
    static final String CLASS_NOT_FOUND = key(4);

    /** A resource key. */
    static final String CLASS_NOT_DEF = key(5);

    //

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {RND_UPPER_LIMIT_NOT_POS, "Upper limit must be positive"},

            {CANT_DOWNLOAD_JAR, "Could not download the JAR file {0}"},
            {CANT_INSTANTIATE_JAR, "jar file ''{0}'' could not be instantiated from file path."},
            {CLASS_NOT_FOUND, "Class ''{0}'' not found"},
            {CLASS_NOT_DEF, "Class ''{0}'' not defined"},

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
