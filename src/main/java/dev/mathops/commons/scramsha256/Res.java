package dev.mathops.commons.scramsha256;

import dev.mathops.commons.res.ResBundle;

import java.util.Locale;

/**
 * Localized resources.
 */
final class Res extends ResBundle {

    // Used by UserCredentials

    /** A resource key. */
    static final String CRED_ROLE_NULL = key(1);

    /** A resource key. */
    static final String CRED_USERNAME_NULL = key(2);

    /** A resource key. */
    static final String CRED_BAD_SALT = key(3);

    /** A resource key. */
    static final String CRED_BAD_STORED_KEY = key(4);

    /** A resource key. */
    static final String CRED_BAD_SERVER_KEY = key(5);

    /** A resource key. */
    static final String CRED_BAD_ITERATION_COUNT = key(6);

    // Used by ClientFirstMessage

    /** A resource key. */
    static final String CF_NO_USERNAME = key(7);

    /** A resource key. */
    static final String CF_NO_RANDOM = key(8);

    /** A resource key. */
    static final String CF_BAD_MESSAGE = key(9);

    //

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {CRED_ROLE_NULL, "Role may not be null or empty"},
            {CRED_USERNAME_NULL, "Username may not be null or empty"},
            {CRED_BAD_SALT, "24-byte salt must be provided"},
            {CRED_BAD_STORED_KEY, "32-byte stored key must be provided"},
            {CRED_BAD_SERVER_KEY, "32-byte server key must be provided"},
            {CRED_BAD_ITERATION_COUNT, "Iterations must be in [4096, 9999]"},

            {CF_NO_USERNAME, "Username may not be null or empty"},
            {CF_NO_RANDOM, "Random source may not be null"},
            {CF_BAD_MESSAGE, "Invalid message data"},

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
