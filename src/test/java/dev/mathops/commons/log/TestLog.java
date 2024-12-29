package dev.mathops.commons.log;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.installation.Installation;
import dev.mathops.commons.installation.Installations;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the classes supporting the Log type.
 */
final class TestLog {

    /** Commonly used string. */
    private static final String LOG_DIR = Installations.DEF_BASE_DIR + "/logs";

    /** Commonly used string. */
    private static final String CONFIG = " C ";

    /** Commonly used string. */
    private static final String INFO = " I ";

    /** Commonly used string. */
    private static final String WARN = " W ";

    /** Commonly used string. */
    private static final String SEVERE = " S ";

    /** Commonly used string. */
    private static final String ENTER = " > ";

    /** Commonly used string. */
    private static final String EXIT = " < ";

    /** The default log directory. */
    private static final File DIR = new File(LOG_DIR);

    /** The first log filename. */
    private static final String FNAME001 = "testlog_001.log";

    /** The first log file generated. */
    private static final File FILE01 = new File(DIR, FNAME001);

    /** The second log file generated. */
    private static final File FILE02 = new File(DIR, "testlog_002.log");

    /** The third log file generated. */
    private static final File FILE03 = new File(DIR, "testlog_003.log");

    /** The fourth log file generated. */
    private static final File FILE04 = new File(DIR, "testlog_004.log");

    /** The fifth log file generated. */
    private static final File FILE05 = new File(DIR, "testlog_005.log");

    /** The sixth log file generated. */
    private static final File FILE06 = new File(DIR, "testlog_006.log");

    /** The seventh log file generated. */
    private static final File FILE07 = new File(DIR, "testlog_007.log");

    /** The eighth og file generated. */
    private static final File FILE08 = new File(DIR, "testlog_008.log");

    /** The ninth log file generated. */
    private static final File FILE09 = new File(DIR, "testlog_009.log");

    /** The tenth log file generated. */
    private static final File FILE10 = new File(DIR, "testlog_010.log");

    /** A log file that should never be generated. */
    private static final File FILE11 = new File(DIR, "testlog_011.log");

    /** A log message. */
    private static final String MSG01 = "This file should get deleted";

    /** A log message. */
    private static final String MSG02 = "Something SEVERE has happened!";

    /** A log message. */
    private static final String MSG03 = "This is your final WARNING";

    /** A log message. */
    private static final String MSG04 = "This message is for INFORMATION only";

    /** A log message. */
    private static final String MSG05 = "System CONFIGURED for maximum performance";

    /** A log message. */
    private static final String MSG06 = "ENTERING the neutral zone";

    /** A log message. */
    private static final String MSG07 = "EXITING the neutral zone";

    /** A log message. */
    private static final String MSG08 = "This is another FINE message you've gotten us into";

    /** A log message. */
    private static final String MSG09 = "We log only the FINEST messages here";

    /** A log message. */
    private static final String MSG10 = "WARNING you that this is the second to the last message";

    /** A log message. */
    private static final String MSG11 = "FINE, this is the last log message";

    /** A log message. */
    private static final String TRACE = "  (" + TestLog.class.getName() + ".java:";

    /** A log message. */
    private static final String MSG12 = "Log: This should be the first line";

    /** A log message. */
    private static final String MSG13 = "Log: This should NOT appear";

    /** A log message. */
    private static final String MSG14 = "Log: This should be the second line";

    /** Settings saved before testing and restored after testing. */
    private static LogSettings savedSettings = null;

    /**
     * Sets up the test base directories.
     */
    @BeforeAll
    static void runBeforeClass() {

        deleteAll();
        // Store the current log settings, so we can restore after the test
        final LogSettings settings = LoggingSubsystem.getSettings();
        savedSettings = new LogSettings(settings);
    }

    /**
     * Cleans up the test base directories.
     */
    @AfterAll
    static void runAfterClass() {

        LoggingSubsystem.getSettings().setFrom(savedSettings);
        deleteAll();
    }

    /**
     * Deletes all files to ensure a clean state for a test.
     */
    private static void deleteAll() {

        delFile(FILE01);
        delFile(FILE02);
        delFile(FILE03);
        delFile(FILE04);
        delFile(FILE05);
        delFile(FILE06);
        delFile(FILE07);
        delFile(FILE08);
        delFile(FILE09);
        delFile(FILE10);
        delFile(FILE11);
    }

    /**
     * Deletes a single test file.
     *
     * @param file the file to delete
     */
    private static void delFile(final File file) {

        if (file.exists() && !file.delete()) {
            final String absPath = file.getAbsolutePath();
            Log.warning("Failed to delete ", absPath);
        }
    }

    /**
     * Tests that when the logging subsystem is initialized and logging to files is set to false, that no log file is
     * created.
     */
    @Test
    @DisplayName("No logging to files: no log file created")
    void test001() {

        deleteAll();
        LoggingSubsystem.setInstallation(null);

        setLogSettings();

        LoggingSubsystem.getSettings().setLogToConsole(true);
        LoggingSubsystem.getSettings().setLogToFiles(false);

        Log.finest(MSG12);
        Log.fine(", with a linefeed.");
        LoggingSubsystem.getSettings().setLogToConsole(false);
        Log.fine(MSG13);
        LoggingSubsystem.getSettings().setLogToConsole(true);
        Log.finest(MSG14);
        Log.fine(", with a linefeed..");
        LoggingSubsystem.getSettings().setLogToConsole(false);

        final File file = new File(LOG_DIR, FNAME001);
        final boolean exists = file.exists();
        assertFalse(exists, "No logging to files: a log file was created");
    }

    /**
     * Tests that when the logging subsystem is initialized with an installation and logging to files is set to false,
     * that no log file is created.
     */
    @Test
    @DisplayName("No logging to files with installation: no log file created")
    void test002() {

        deleteAll();
        final Installation installation = Installations.getMyInstallation();
        LoggingSubsystem.setInstallation(installation);

        setLogSettings();

        LoggingSubsystem.getSettings().setLogToConsole(true);
        LoggingSubsystem.getSettings().setLogToFiles(false);

        Log.finest(MSG12);
        Log.fine(", with a linefeed...");
        LoggingSubsystem.getSettings().setLogToConsole(false);
        Log.fine(MSG13);
        LoggingSubsystem.getSettings().setLogToConsole(true);
        Log.finest(MSG14);
        Log.fine(", with a linefeed....");
        LoggingSubsystem.getSettings().setLogToConsole(false);

        final File file = new File(LOG_DIR, FNAME001);
        final boolean exists = file.exists();
        assertFalse(exists, "No logging to files with installation: a log file was created");
    }

    /**
     * Tests default message logging with logging to files enabled, which should log all messages and create a new file
     * for each.
     */
    @Test
    @DisplayName("Logging with null installation: no log files created")
    void test003() {

        deleteAll();
        LoggingSubsystem.setInstallation(null);

        setLogSettings();
        logMessages();

        final boolean file1Exists = FILE01.exists();
        assertFalse(file1Exists, "Logging with null installation: file 1 created");

        final boolean file2exists = FILE02.exists();
        assertFalse(file2exists, "Logging with null installation: file 2 created");

        final boolean file3exists = FILE03.exists();
        assertFalse(file3exists, "Logging with null installation: file 3 created");

        final boolean file4exists = FILE04.exists();
        assertFalse(file4exists, "Logging with null installation: file 4 created");

        final boolean file5exists = FILE05.exists();
        assertFalse(file5exists, "Logging with null installation: file 5 created");

        final boolean file6exists = FILE06.exists();
        assertFalse(file6exists, "Logging with null installation: file 6 created");

        final boolean file7exists = FILE07.exists();
        assertFalse(file7exists, "Logging with null installation: file 7 created");

        final boolean files8exists = FILE08.exists();
        assertFalse(files8exists, "Logging with null installation: file 8 created");

        final boolean file9exists = FILE09.exists();
        assertFalse(file9exists, "Logging with null installation: file 9 created");

        final boolean file10exists = FILE10.exists();
        assertFalse(file10exists, "Logging with null installation: file 10 created");

        final boolean file11exists = FILE11.exists();
        assertFalse(file11exists, "Logging with null installation: file 11 created");
    }

    /**
     * Tests default message logging with logging to files enabled, which should log all messages and create a new file
     * for each.
     */
    @Test
    @DisplayName("Logging with installation")
    void test004() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);

        setLogSettings();
        logMessages();

        final boolean file1exists = FILE01.exists();
        assertTrue(file1exists, "Logging with installation: file 1 not created");

        final boolean file2exists = FILE02.exists();
        assertTrue(file2exists, "Logging with installation: file 2 not created");

        final boolean file3exists = FILE03.exists();
        assertTrue(file3exists, "Logging with installation: file 3 not created");

        final boolean file4exists = FILE04.exists();
        assertTrue(file4exists, "Logging with installation: file 4 not created");

        final boolean file5exists = FILE05.exists();
        assertTrue(file5exists, "Logging with installation: file 5 not created");

        final boolean file6exists = FILE06.exists();
        assertTrue(file6exists, "Logging with installation: file 6 not created");

        final boolean file7exists = FILE07.exists();
        assertTrue(file7exists, "Logging with installation: file 7 not created");

        final boolean file8exists = FILE08.exists();
        assertTrue(file8exists, "Logging with installation: file 8 not created");

        final boolean file9exists = FILE09.exists();
        assertTrue(file9exists, "Logging with installation: file 9 not created");

        final boolean file10exists = FILE10.exists();
        assertTrue(file10exists, "Logging with installation: file 10 not created");

        final boolean file11exists = FILE11.exists();
        assertFalse(file11exists, "Logging with installation: file 11 created");

        final String file1 = getFile(FILE01);
        assertEquals(MSG11 + CoreConstants.CRLF, file1, "Logging with installation: file 1 contents");

        final String str2 = getFile(FILE02);
        if (isDateInvalid(str2)) {
            fail("Logging with installation: file 2 date");
        }

        final String expect1 = WARN + MSG10 + TRACE;
        final int expect1Len = expect1.length();
        final int str2Len = str2.length();

        System.out.println("*** " + expect1);
        System.out.println("*** " + str2);

        final int msg10Len = MSG10.length();
        final int traceLen = TRACE.length();

        assertTrue(str2Len >= expect1Len, "Logging with installation: file 2 content len");
        final String actual1 = str2.substring(45, 45 + 3 + msg10Len + traceLen);
        assertEquals(expect1, actual1, "Logging with installation: file 2 contents");

        final String file03 = getFile(FILE03);
        assertEquals(MSG09 + CoreConstants.CRLF, file03, "Logging with installation: file 3 contents");
        final String file04 = getFile(FILE04);
        assertEquals(MSG08 + CoreConstants.CRLF, file04, "Logging with installation: file 4 contents");

        final String str5 = getFile(FILE05);
        if (isDateInvalid(str5)) {
            fail("Logging with installation: file 5 date");
        }
        final int msg07len = MSG07.length();
        final String actual2 = str5.substring(45, 45 + 3 + msg07len + traceLen);
        assertEquals(EXIT + MSG07 + TRACE, actual2, "Logging with installation: file 5 contents");

        final String str6 = getFile(FILE06);
        if (isDateInvalid(str6)) {
            fail("Logging with installation: file 6 date");
        }
        final int msg06len = MSG06.length();
        final String actual3 = str6.substring(45, 45 + 3 + msg06len + traceLen);
        assertEquals(ENTER + MSG06 + TRACE, actual3, "Logging with installation: file 6 contents");

        final String str7 = getFile(FILE07);
        if (isDateInvalid(str7)) {
            fail("Logging with installation: file 7 date");
        }
        final int msg05len = MSG05.length();
        final String actual4 = str7.substring(45, 45 + 3 + msg05len + traceLen);
        assertEquals(CONFIG + MSG05 + TRACE, actual4, "Logging with installation: file 7 contents");

        final String str8 = getFile(FILE08);
        if (isDateInvalid(str8)) {
            fail("Logging with installation: file 8 date");
        }
        final int msg04len = MSG04.length();
        final String actual5 = str8.substring(45, 45 + 3 + msg04len + traceLen);
        assertEquals(INFO + MSG04 + TRACE, actual5, "Logging with installation: file 8 contents");

        final String str9 = getFile(FILE09);
        if (isDateInvalid(str9)) {
            fail("Logging with installation: file 9 date");
        }
        final int msg03len = MSG03.length();
        final String actual6 = str9.substring(45, 45 + 3 + msg03len + traceLen);
        assertEquals(WARN + MSG03 + TRACE, actual6, "Logging with installation: file 9 contents");

        final String str10 = getFile(FILE10);
        if (isDateInvalid(str10)) {
            fail("Logging with installation: file 10 date");
        }
        final int msg02len = MSG02.length();
        final String actual7 = str10.substring(45, 45 + 3 + msg02len + traceLen);
        assertEquals(SEVERE + MSG02 + TRACE, actual7, "Logging with installation: file 10 contents");

        deleteAll();
    }

    /**
     * Tests logging to files of severe messages only.
     */
    @Test
    @DisplayName("Logging of severe")
    void test005() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.SEVERE_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Logging of severe: file 1 not created");

        final boolean file02exists = FILE02.exists();
        assertFalse(file02exists, "Logging of severe: file 2 created");

        final String str1 = getFile(FILE01);
        if (isDateInvalid(str1)) {
            fail("Logging of severe: file 1 date");
        }
        final int msg02len = MSG02.length();
        final int traceLen = TRACE.length();
        final String actual = str1.substring(45, 45 + 3 + msg02len + traceLen);
        assertEquals(SEVERE + MSG02 + TRACE, actual, "Logging of severe: file 1 content");

        deleteAll();
    }

    /**
     * Tests logging to files of severe and warning messages only.
     */
    @Test
    @DisplayName("Logging of severe, warning")
    void test006() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.SEVERE_BIT | LogBase.WARNING_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Logging of severe, warning: file 1 not created");

        final boolean file02exists = FILE02.exists();
        assertTrue(file02exists, "Logging of severe, warning: file 2 not created");

        final boolean file03exists = FILE03.exists();
        assertTrue(file03exists, "Logging of severe, warning: file 3 not created");

        final boolean file04exists = FILE04.exists();
        assertFalse(file04exists, "Logging of severe, warning: file 4 created");

        final String str1 = getFile(FILE01);
        if (isDateInvalid(str1)) {
            fail("Logging of severe, warning: file 1 date");
        }

        final int traceLen = TRACE.length();

        final int msg10len = MSG10.length();
        final String actual1 = str1.substring(45, 45 + 3 + msg10len + traceLen);
        assertEquals(WARN + MSG10 + TRACE, actual1, "Logging of severe, warning: file 1 content");

        final String str2 = getFile(FILE02);
        if (isDateInvalid(str2)) {
            fail("Logging of severe, warning: file 2 date");
        }
        final int msg03len = MSG03.length();
        final String actual2 = str2.substring(45, 45 + 3 + msg03len + traceLen);
        assertEquals(WARN + MSG03 + TRACE, actual2, "Logging of severe, warning: file 2 content");

        final String str3 = getFile(FILE03);
        if (isDateInvalid(str3)) {
            fail("Logging of severe/warning: file 3 date");
        }
        final int msg02len = MSG02.length();
        final String actual3 = str3.substring(45, 45 + 3 + msg02len + traceLen);
        assertEquals(SEVERE + MSG02 + TRACE, actual3, "Logging of severe, warning: file 3 content");

        deleteAll();
    }

    /**
     * Tests logging to files of info messages only.
     */
    @Test
    @DisplayName("Logging of info")
    void test007() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.INFO_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Logging of info: file 1 not created");

        final boolean file02exists = FILE02.exists();
        assertFalse(file02exists, "Logging of info: file 2 not created");

        final boolean file03exists = FILE03.exists();
        assertFalse(file03exists, "Logging of info: file 3 created");

        final String str1 = getFile(FILE01);
        if (isDateInvalid(str1)) {
            fail("Logging of info: file 1 date");
        }
        final int traceLen = TRACE.length();
        final int msg04len = MSG04.length();
        final String actual = str1.substring(45, 45 + 3 + msg04len + traceLen);
        assertEquals(INFO + MSG04 + TRACE, actual, "Logging of info: file 1 content");

        deleteAll();
    }

    /**
     * Tests logging to files of info and config messages only.
     */
    @Test
    @DisplayName("Logging of info, config")
    void test008() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.INFO_BIT | LogBase.CONFIG_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Logging of info, config: file 1 not created");

        final boolean file02exists = FILE02.exists();
        assertTrue(file02exists, "Logging of info, config: file 2 not created");

        final boolean file03exists = FILE03.exists();
        assertFalse(file03exists, "Logging of info, config: file 3 not created");

        final String str1 = getFile(FILE01);
        if (isDateInvalid(str1)) {
            fail("Logging of info, config: file 1 date");
        }

        final int traceLen = TRACE.length();

        final int msg05len = MSG05.length();
        final String actual1 = str1.substring(45, 45 + 3 + msg05len + traceLen);
        assertEquals(CONFIG + MSG05 + TRACE, actual1, "Logging of info, config: file 1 content");

        final String str2 = getFile(FILE02);
        if (isDateInvalid(str2)) {
            fail("Logging of info, config: file 2 date");
        }
        final int msg04len = MSG04.length();
        final String actual2 = str2.substring(45, 45 + 3 + msg04len + traceLen);
        assertEquals(INFO + MSG04 + TRACE, actual2, "Logging of info, config: file 2 content");

        deleteAll();
    }

    /**
     * Tests logging to files of entering messages only.
     */
    @Test
    @DisplayName("Logging of entering")
    void test009() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.ENTERING_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Logging of entering: file 1 not created");

        final boolean file02exists = FILE02.exists();
        assertFalse(file02exists, "Logging of entering: file 2 created");

        final String str1 = getFile(FILE01);
        if (isDateInvalid(str1)) {
            fail("Logging of entering: file 1 date");
        }
        final int traceLen = TRACE.length();
        final int msg06len = MSG06.length();
        final String actual = str1.substring(45, 45 + 3 + msg06len + traceLen);
        assertEquals(ENTER + MSG06 + TRACE, actual, "Logging of entering: file 1 content");

        deleteAll();
    }

    /**
     * Tests logging to files of entering and exiting messages only.
     */
    @Test
    @DisplayName("Logging of entering, exiting")
    void test010() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.ENTERING_BIT | LogBase.EXITING_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Log entering, exiting: File 1 exists");

        final boolean file02exists = FILE02.exists();
        assertTrue(file02exists, "Log entering, exiting: File 2 exists");

        final boolean file03exists = FILE03.exists();
        assertFalse(file03exists, "Log entering, exiting: File 3 does not exist");

        final String str1 = getFile(FILE01);
        if (isDateInvalid(str1)) {
            fail("Log entering, exiting: File 1 log record date mismatch");
        }
        final int traceLen = TRACE.length();
        final int msg07Len = MSG07.length();
        final String actual1 = str1.substring(45, 45 + 3 + msg07Len + traceLen);
        assertEquals(EXIT + MSG07 + TRACE, actual1, "Log entering, exiting: File 1 content");

        final String str2 = getFile(FILE02);
        if (isDateInvalid(str2)) {
            fail("Log entering,exiting: File 2 log record date mismatch");
        }
        final int msg06Len = MSG06.length();
        final String actual2 = str2.substring(45, 45 + 3 + msg06Len + traceLen);
        assertEquals(ENTER + MSG06 + TRACE, actual2, "Log entering, exiting: File 2 content");

        deleteAll();
    }

    /**
     * Tests logging to files of fine messages only.
     */
    @Test
    @DisplayName("Logging of fine")
    void test011() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.FINE_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Log fine: File 1 exists");

        final boolean file02exists = FILE02.exists();
        assertTrue(file02exists, "Log fine: File 2 exists");

        final boolean file03exists = FILE03.exists();
        assertTrue(file03exists, "Log fine: File 3 exists");

        final boolean file04exists = FILE04.exists();
        assertTrue(file04exists, "Log fine: File 4 exists");

        final boolean file05exists = FILE05.exists();
        assertFalse(file05exists, "Log fine: File 5 does not exist");

        final String file01 = getFile(FILE01);
        assertEquals(MSG11 + CoreConstants.CRLF, file01, "Log fine: File 1 content");

        final String file02 = getFile(FILE02);
        assertEquals(MSG09 + CoreConstants.CRLF, file02, "Log fine: File 2 content");

        final String file03 = getFile(FILE03);
        assertEquals(MSG08 + CoreConstants.CRLF, file03, "Log fine: File 3 content");

        final String file04 = getFile(FILE04);
        assertEquals(MSG01 + CoreConstants.CRLF, file04, "Log fine: File 4 content");

        deleteAll();
    }

    /**
     * Tests logging to files of fine and finest messages only.
     */
    @Test
    @DisplayName("Logging of fine, finest")
    void test012() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.FINE_BIT | LogBase.FINEST_BIT);

        logMessages();

        final boolean file01exists = FILE01.exists();
        assertTrue(file01exists, "Log fine, finest: File 1 exists");

        final boolean file02exists = FILE02.exists();
        assertTrue(file02exists, "Log fine, finest: File 2 exists");

        final boolean file03exists = FILE03.exists();
        assertTrue(file03exists, "Log fine, finest: File 3 exists");

        final boolean file04exists = FILE04.exists();
        assertTrue(file04exists, "Log fine, finest: File 4 exists");

        final boolean file05exists = FILE05.exists();
        assertFalse(file05exists, "Log fine, finest: File 5 does not exist");

        final String file01 = getFile(FILE01);
        assertEquals(MSG11 + CoreConstants.CRLF, file01, "Log fine, finest: File 1 content");

        final String file02 = getFile(FILE02);
        assertEquals(MSG09 + CoreConstants.CRLF, file02, "Log fine, finest: File 2 content");

        final String file03 = getFile(FILE03);
        assertEquals(MSG08 + CoreConstants.CRLF, file03, "Log fine, finest: File 3 content");

        final String file04 = getFile(FILE04);
        assertEquals(MSG01 + CoreConstants.CRLF, file04, "Log fine, finest: File 4 content");

        deleteAll();
    }

    /**
     * Tests logging to files with no types logged.
     */
    @Test
    @DisplayName("Logging of none")
    void test013() {

        deleteAll();
        final Installation installation = Installations.get().getInstallation(null, null);
        LoggingSubsystem.setInstallation(installation);
        setLogSettings();

        LoggingSubsystem.getSettings().setLogLevels(LogBase.NONE);

        logMessages();

        final boolean exists = FILE01.exists();
        assertFalse(exists, "Logging of none: log file created");
    }

    /**
     * Adjusts the log settings for a logger.
     */
    private static void setLogSettings() {

        final LogSettings settings = LoggingSubsystem.getSettings();

        settings.setLogLevels(LogBase.ALL);
        settings.setLogToConsole(false);
        settings.setLogToFiles(true);
        settings.setLogFileCount(10);
        settings.setLogFileSizeLimit(10);
        settings.setAppend(false);
        settings.setFilenameBase("testlog");
    }

    /**
     * Write the suite of test messages to the logger.
     */
    private static void logMessages() {

        Log.fine(MSG01);
        Log.severe(MSG02);
        Log.warning(MSG03);
        Log.info(MSG04);
        Log.config(MSG05);
        Log.entering(MSG06);
        Log.exiting(MSG07);
        Log.fine(MSG08);
        Log.finer(MSG09);
        Log.warning(MSG10);
        Log.fine(MSG11);
    }

    /**
     * Loads a text file, storing the file contents in a {@code String}. Lines in the returned file are separated by
     * single '\n' characters regardless of the line terminator in the source file. The last line will end with a '\n'
     * character, whether there was one in the input file.
     *
     * @param file the file to read
     * @return the loaded file contents, or {@code null} if unable to load
     */
    private static String getFile(final File file) {

        String result = null;

        try (final InputStream input = new FileInputStream(file)) {
            final int fileLen = (int) file.length();
            final StringBuilder builder = new StringBuilder(fileLen);

            try (final BufferedReader rdr = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {

                for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                    builder.append(line);
                    builder.append(CoreConstants.CRLF);
                }
            }

            result = builder.toString();
        } catch (final IOException ex) {
            // No action
        }

        return result;
    }

    /**
     * Tests that the first # characters of a log record is in a valid date form.
     *
     * @param charSeq the character sequence to test
     * @return {@code true} if the string starts with a valid date format; {@code false} if not
     */
    private static boolean isDateInvalid(final CharSequence charSeq) {

        final char ch0 = charSeq.charAt(0);
        final char ch1 = charSeq.charAt(1);
        final char ch3 = charSeq.charAt(3);
        final char ch4 = charSeq.charAt(4);
        final char ch6 = charSeq.charAt(6);
        final char ch7 = charSeq.charAt(7);
        final char ch9 = charSeq.charAt(9);
        final char ch10 = charSeq.charAt(10);
        final char ch12 = charSeq.charAt(12);
        final char ch13 = charSeq.charAt(13);
        final char ch15 = charSeq.charAt(15);
        final char ch16 = charSeq.charAt(16);
        final char ch17 = charSeq.charAt(17);

        return !(Character.isDigit(ch0)
               && Character.isDigit(ch1)
               && (int) charSeq.charAt(2) == (int) '/'
               && Character.isDigit(ch3)
               && Character.isDigit(ch4)
               && (int) charSeq.charAt(5) == (int) ' '
               && Character.isDigit(ch6)
               && Character.isDigit(ch7)
               && (int) charSeq.charAt(8) == (int) ':'
               && Character.isDigit(ch9)
               && Character.isDigit(ch10)
               && (int) charSeq.charAt(11) == (int) ':'
               && Character.isDigit(ch12)
               && Character.isDigit(ch13)
               && (int) charSeq.charAt(14) == (int) '.'
               && Character.isDigit(ch15)
               && Character.isDigit(ch16)
               && Character.isDigit(ch17));
    }
}
