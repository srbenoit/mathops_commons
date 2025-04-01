package dev.mathops.commons.log;

import dev.mathops.commons.installation.Installations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the classes supporting the {@code LogSettings} type.
 */
final class TestLogSettings {

    /** Commonly used string. */
    private static final String ALL = "ALL";

    /** Commonly used string. */
    private static final String LOGS = "logs";

    /**
     * Constructs a new {@code TestLogSettings}.
     */
    TestLogSettings() {

        // No action
    }

    /**
     * Tests the state of a {@code LogSettings} object after construction. All fields should be initialized to the
     * defaults from {@code ELogSetting}.
     */
    @Test
    @DisplayName("LogSettings state on construction")
    void test001() {

        final LogSettings settings = new LogSettings();

        final String logLevelName = settings.getLogLevelName();
        assertEquals(ALL, logLevelName, "Log level name default is ALL");

        final int logLevel = settings.getLogLevel();
        assertEquals(LogBase.ALL, logLevel, "Log level default is ALL");

        final boolean logToConsole = settings.isLogToConsole();
        assertTrue(logToConsole, "Log to console default is true");

        final boolean logToFiles = settings.isLogToFiles();
        assertFalse(logToFiles, "Log to file default is false");

        final String logFilePath = settings.getLogFilePath();
        assertEquals(LOGS, logFilePath, "Log file path default is logs");

        final int logFileCount = settings.getLogFileCount();
        assertEquals(Integer.MAX_VALUE, logFileCount, "Log file count default is unlimited");

        final int logFileSizeLimit = settings.getLogFileSizeLimit();
        assertEquals(Integer.MAX_VALUE, logFileSizeLimit, "Log file size default is unlimited");

        final String filenameBase = settings.getFilenameBase();
        assertEquals(Installations.ZIRCON, filenameBase, "Log filename base default is zircon");

        final boolean append = settings.isAppend();
        assertTrue(append, "Log append default us true");

        final boolean dirty = settings.isDirty();
        assertFalse(dirty, "Log settings are initially not dirty");
    }

    /**
     * Tests the state of a {@code LogSettings} object after construction and the setting of a null
     * {@code Installation}.
     */
    @Test
    @DisplayName("LogSettings state on construction with null installation")
    void test002() {

        final LogSettings settings = new LogSettings();

        // Test configuration with null installation
        LoggingSubsystem.configureSettings(settings, null);

        final String logLevelName = settings.getLogLevelName();
        assertEquals(ALL, logLevelName, "Log level name with null config is ALL");

        final int logLevel = settings.getLogLevel();
        assertEquals(LogBase.ALL, logLevel, "Log level with null config is ALL");

        final boolean logToConsole = settings.isLogToConsole();
        assertTrue(logToConsole, "Log to console with null config is true");

        final boolean logToFiles = settings.isLogToFiles();
        assertFalse(logToFiles, "Log to file with null config is false");

        final String logFilePath = settings.getLogFilePath();
        assertEquals(LOGS, logFilePath, "Log file path with null config is logs");

        final int logFileCount = settings.getLogFileCount();
        assertEquals(Integer.MAX_VALUE, logFileCount, "Log file count with null config is unlimited");

        final int logFileSizeLimit = settings.getLogFileSizeLimit();
        assertEquals(Integer.MAX_VALUE, logFileSizeLimit, "Log file size with null config is unlimited");

        final String filenameBase = settings.getFilenameBase();
        assertEquals(Installations.ZIRCON, filenameBase, "Log filename base with null config is zircon");

        final boolean append = settings.isAppend();
        assertTrue(append, "Log append with null config us true");

        final boolean dirty = settings.isDirty();
        assertFalse(dirty, "Log settings after load with null config not dirty");
    }

    /**
     * Tests the state of a {@code LogSettings} object after construction and the setting of a non-null
     * {@code Installation}.
     */
    @Test
    @DisplayName("LogSettings state on construction with installation")
    void test003() {

        final LogSettings settings = new LogSettings();

        // Test configuration with null installation
        LoggingSubsystem.configureSettings(settings, null);

        final String logLevelName = settings.getLogLevelName();
        assertEquals(ALL, logLevelName, "Log level name with null installation is ALL");

        final int logLevel = settings.getLogLevel();
        assertEquals(LogBase.ALL, logLevel, "Log level with null installation is ALL");

        final boolean logToConsole = settings.isLogToConsole();
        assertTrue(logToConsole, "Log to console with null installation is true");

        final boolean logToFiles = settings.isLogToFiles();
        assertFalse(logToFiles, "Log to file with null installation is false");

        final String logFilePath = settings.getLogFilePath();
        assertEquals(LOGS, logFilePath, "Log file path with null installation is logs");

        final int logFileCount = settings.getLogFileCount();
        assertEquals(Integer.MAX_VALUE, logFileCount, "Log file count with null installation is unlimited");

        final int logFileSizeLimit = settings.getLogFileSizeLimit();
        assertEquals(Integer.MAX_VALUE, logFileSizeLimit, "Log file size with null installation is unlimited");

        final String filenameBase = settings.getFilenameBase();
        assertEquals(Installations.ZIRCON, filenameBase, "Log filename base with null installation is zircon");

        final boolean append = settings.isAppend();
        assertTrue(append, "Log append with null installation us true");

        final boolean dirty = settings.isDirty();
        assertFalse(dirty, "Log settings after load with null installation not dirty");
    }
}
