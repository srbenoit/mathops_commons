package dev.mathops.commons.log;

import dev.mathops.commons.res.ResBundle;

import java.util.Locale;

/**
 * Contains the English-language resources for the logging package.
 */
final class Res extends ResBundle {

    /** Resource key. */
    static final String MUT_IN_NOTIFY = key(1);

    /** Resource key. */
    static final String RENAME_FAIL = key(2);

    /** Resource key. */
    static final String DELETE_FAIL = key(3);

    /** Resource key. */
    static final String COPY_FAIL = key(4);

    /** Resource key. */
    static final String COPY_ERROR = key(5);

    /** Resource key. */
    static final String LOG_FAILED = key(6);

    /** Resource key. */
    static final String EMPTY_FILENAME = key(7);

    /** Resource key. */
    static final String FILENAME_CHAR1 = key(8);

    /** Resource key. */
    static final String NO_SRC = key(9);

    /** Resource key. */
    static final String ERR_LOG_NO_INTERFACE = key(10);

    /** Resource key. */
    static final String SETTINGS_BAD_FILE_COUNT = key(11);

    /** Resource key. */
    static final String SETTINGS_BAD_FILE_SIZE = key(12);

    /** Resource key. */
    static final String SETTINGS_BAD_COUNT = key(13);

    /** Resource key. */
    static final String SETTINGS_PARSE_COUNT = key(14);

    /** Resource key. */
    static final String SETTINGS_BAD_SIZE = key(15);

    /** Resource key. */
    static final String SETTINGS_PARSE_SIZE = key(16);

    /** Resource key. */
    static final String SETTINGS_BAD_LEVEL = key(17);

    /** Resource key. */
    static final String CANT_MK_LOG_DIR = key(18);

    /** The resources - an array of key-values pairs. */
    private static final String[][] EN_US = {
            {MUT_IN_NOTIFY, "Attempt to mutate in notification"},
            {RENAME_FAIL, "Unable to rename {0} to {1} while rotating logs"},
            {DELETE_FAIL, "Unable to delete {0}"},
            {COPY_FAIL, "Unable to copy {0} to {1} while rotating logs"},
            {COPY_ERROR, "Error copying file: {0}"},
            {LOG_FAILED, "Failed to log to {0}: {1}"},
            {EMPTY_FILENAME, "Log filename base may not be empty string"},
            {FILENAME_CHAR1, "Log filename must start with a character (A-Z or a-z)"},
            {NO_SRC, "(source unavailable for {0})"},

            {ERR_LOG_NO_INTERFACE, "Unable to retrieve {0} interface"},

            {SETTINGS_BAD_FILE_COUNT, "File count must be at least 1"},
            {SETTINGS_BAD_FILE_SIZE, "File size limit must be at least 1"},
            {SETTINGS_BAD_COUNT, "Invalid log file count ''{0}'' (must be >=1)"},
            {SETTINGS_PARSE_COUNT, "Failed to parse log file count ''{0}''"},
            {SETTINGS_BAD_SIZE, "Invalid log file size limit ''{0}'' (must be >=1)"},
            {SETTINGS_PARSE_SIZE, "Failed to parse log file size limit ''{0}''"},
            {SETTINGS_BAD_LEVEL, "Invalid log level: {0}"},
            {CANT_MK_LOG_DIR, "Unable to create directory: {0}"},

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
