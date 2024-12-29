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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the classes supporting the LogWriter type.
 */
final class TestLogWriter {

    /** A log message. */
    private static final String MSG01 = "This is log message 1, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG02 = "This is log message 2, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG03 = "This is log message 3, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG04 = "This is log message 4, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG05 = "This is log message 5, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG06 = "This is log message 6, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG07 = "This is log message 7, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG08 = "This is log message 8, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG09 = "This is log message 9, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG10 = "This is log message 10, exceeding 10 bytes";

    /** A log message. */
    private static final String MSG11 = "This is log message 11, exceeding 10 bytes";

    /** A log message. */
    private static final String LINE1 = "Line 1";

    /** A log message. */
    private static final String LINE2 = "Line 2";

    /** A log message. */
    private static final String LINE3 = "Line 3";

    /** A log message. */
    private static final String LINE4 = "Line 4";

    /** A log message. */
    private static final String LINE5 = "Line 5";

    /** A log message. */
    private static final String LINE6 = "Line 6";

    /** The temporary installation path. */
    private static File installDir = null;

    /** Settings saved before testing and restored after testing. */
    private static LogSettings savedSettings = null;

    /**
     * Sets up the test base directories.
     */
    @BeforeAll
    static void runBeforeClass() {

        // Store the current log settings, so we can restore after the test
        final LogSettings settings = LoggingSubsystem.getSettings();
        savedSettings = new LogSettings(settings);

        final String tmpDirProperty = System.getProperty("java.io.tmpdir");
        final File tempDir = new File(tmpDirProperty);
        installDir = new File(tempDir, "zircon_temp_inst");
        if (installDir.exists() || installDir.mkdirs()) {

            final StringBuilder builder = new StringBuilder(300);

            builder.append("log-dir=$BASE_DIR/logs");
            builder.append(CoreConstants.CRLF);
            builder.append("log-levels=ALL");
            builder.append(CoreConstants.CRLF);
            builder.append("log-to-console=true");
            builder.append(CoreConstants.CRLF);
            builder.append("log-to-file=true");
            builder.append(CoreConstants.CRLF);
            builder.append("log-file-count=40");
            builder.append(CoreConstants.CRLF);
            builder.append("log-file-size-limit=20000000");
            builder.append(CoreConstants.CRLF);
            builder.append("log-file-name-base=zircon");
            builder.append(CoreConstants.CRLF);

            try (final FileOutputStream fw = new FileOutputStream(new File(installDir, "installation.properties"))) {
                final byte[] bytes = builder.toString().getBytes(StandardCharsets.UTF_8);
                fw.write(bytes);
            } catch (final IOException ex) {
                Log.warning(ex);
            }
        } else {
            final String absolutePath = installDir.getAbsolutePath();
            Log.warning("Unable to create ", absolutePath);
        }
    }

    /**
     * Cleans up the test base directories.
     */
    @AfterAll
    static void runAfterClass() {

        LoggingSubsystem.getSettings().setFrom(savedSettings);

        if (installDir.exists()) {
            final File[] files = installDir.listFiles();
            if (files != null) {
                for (final File file : files) {
                    if (file.exists() && !file.delete()) {
                        final String absolutePath = file.getAbsolutePath();
                        Log.warning("Failed to delete ", absolutePath);
                    }
                }
            }
            if (!installDir.delete()) {
                final String absolutePath = installDir.getAbsolutePath();
                Log.warning("Unable to delete ", absolutePath);
            }
        }
    }

    /**
     * Writes some messages to the console. This test always passes, but logs written to the console should be checked.
     */
    @Test
    @DisplayName("Log to console: There should have been two lines written")
    void test001() {

        final LogWriter writer = new LogWriter();
        final LogSettings settings = writer.getSettings();

        writer.writeConsole("Log to console: This should be the first line", false);
        writer.writeConsole(", with a newline", true);
        settings.setLogToConsole(false);
        writer.writeConsole("Log to console: This should NOT appear", true);
        settings.setLogToConsole(true);
        writer.writeConsole("Log to console: This should be the second line", false);
        writer.writeConsole(", with a linefeed", true);

        assertNotNull(settings, "Always succeeds");
    }

    /**
     * Test logging to a list.
     */
    @Test
    @DisplayName("Log to list")
    void test002() {

        final LogWriter writer = new LogWriter();

        writer.getSettings().setLogToConsole(false);
        writer.startList(5);
        final int count1 = writer.getNumInList();
        assertEquals(0, count1, "Log to list: list did not start empty");
        writer.writeMessage(LINE1, true);
        final int count2 = writer.getNumInList();
        assertEquals(1, count2, "Log to list: bad list size after 1 message");
        writer.writeMessage(LINE2, true);
        final int count3 = writer.getNumInList();
        assertEquals(2, count3, "Log to list: bad list size after 2 messages");
        writer.writeMessage(LINE3, true);
        final int count4 = writer.getNumInList();
        assertEquals(3, count4, "Log to list: bad list size after 3 messages");
        writer.writeMessage(LINE4, true);
        final int count5 = writer.getNumInList();
        assertEquals(4, count5, "Log to list: bad list size after 4 messages");
        writer.writeMessage(LINE5, true);
        final int count6 = writer.getNumInList();
        assertEquals(5, count6, "Log to list: bad list size after 5 messages");
        writer.addToList(LINE6);
        final int count7 = writer.getNumInList();
        assertEquals(5, count7, "Log to list: bad list size after 6 messages");
        writer.stopList();
        final int count8 = writer.getNumInList();
        assertEquals(5, count8, "Log to list: bad list size after stopping");

        final String msg0 = writer.getListMessage(0).getMessage();
        final String msg1 = writer.getListMessage(1).getMessage();
        final String msg2 = writer.getListMessage(2).getMessage();
        final String msg3 = writer.getListMessage(3).getMessage();
        final String msg4 = writer.getListMessage(4).getMessage();

        assertEquals(LINE2, msg0, "Log to list: list message 1 content");
        assertEquals(LINE3, msg1, "Log to list: list message 2 content");
        assertEquals(LINE4, msg2, "Log to list: list message 3 content");
        assertEquals(LINE5, msg3, "Log to list: list message 4 content");
        assertEquals(LINE6, msg4, "Log to list: list message 5 content");
        writer.clearList();
        final int count9 = writer.getNumInList();
        assertEquals(0, count9, "Log to list: bad list size after clearing");
    }

    /**
     * Test writing log messages to files and advancing to new files based on size cap.
     */
    @Test
    @DisplayName("Log to files")
    void test003() {

        final Installation inst = Installations.get().getInstallation(installDir, null);
        LoggingSubsystem.setInstallation(inst);

        final LogWriter writer = new LogWriter();

        writer.getSettings().setLogToFiles(true);
        final File logDir = writer.determineLogDir();
        writer.getSettings().setLogToFiles(false);

        writer.getSettings().setLogToConsole(false);
        writer.getSettings().setFilenameBase("testlog");
        writer.getSettings().setLogFileSizeLimit(10);
        writer.getSettings().setLogFileCount(10);

        writer.getSettings().setLogToFiles(true);
        writer.writeMessage(MSG01, true);
        writer.writeMessage(MSG02, true);
        writer.writeMessage(MSG03, true);
        writer.writeMessage(MSG04, true);
        writer.writeMessage(MSG05, true);
        writer.writeMessage(MSG06, true);
        writer.writeMessage(MSG07, true);
        writer.writeMessage(MSG08, true);
        writer.writeMessage(MSG09, true);
        writer.writeMessage(MSG10, true);
        writer.writeMessage(MSG11, true);
        writer.getSettings().setLogToFiles(false);

        // We should now have 10 files, named testlog_001.log --> testlog_010.log
        // containing the most recent 10 logged messages, most recent in the _001 log file

        final File log01 = new File(logDir, "testlog_001.log");
        final File log02 = new File(logDir, "testlog_002.log");
        final File log03 = new File(logDir, "testlog_003.log");
        final File log04 = new File(logDir, "testlog_004.log");
        final File log05 = new File(logDir, "testlog_005.log");
        final File log06 = new File(logDir, "testlog_006.log");
        final File log07 = new File(logDir, "testlog_007.log");
        final File log08 = new File(logDir, "testlog_008.log");
        final File log09 = new File(logDir, "testlog_009.log");
        final File log10 = new File(logDir, "testlog_010.log");
        final File log11 = new File(logDir, "testlog_011.log");

        final boolean exists01 = log01.exists();
        final boolean exists02 = log02.exists();
        final boolean exists03 = log03.exists();
        final boolean exists04 = log04.exists();
        final boolean exists05 = log05.exists();
        final boolean exists06 = log06.exists();
        final boolean exists07 = log07.exists();
        final boolean exists08 = log08.exists();
        final boolean exists09 = log09.exists();
        final boolean exists10 = log10.exists();
        final boolean exists11 = log11.exists();

        assertTrue(exists01, "Log to files: log file 1 did not exist");
        assertTrue(exists02, "Log to files: log file 2 did not exist");
        assertTrue(exists03, "Log to files: log file 3 did not exist");
        assertTrue(exists04, "Log to files: log file 4 did not exist");
        assertTrue(exists05, "Log to files: log file 5 did not exist");
        assertTrue(exists06, "Log to files: log file 6 did not exist");
        assertTrue(exists07, "Log to files: log file 7 did not exist");
        assertTrue(exists08, "Log to files: log file 8 did not exist");
        assertTrue(exists09, "Log to files: log file 9 did not exist");
        assertTrue(exists10, "Log to files: log file 10 did not exist");
        assertFalse(exists11, "Log to files: log file 11 exists");

        final String file01 = getFile(log01);
        final String file02 = getFile(log02);
        final String file03 = getFile(log03);
        final String file04 = getFile(log04);
        final String file05 = getFile(log05);
        final String file06 = getFile(log06);
        final String file07 = getFile(log07);
        final String file08 = getFile(log08);
        final String file09 = getFile(log09);
        final String file10 = getFile(log10);

        assertEquals(MSG11 + CoreConstants.CRLF, file01, "Log to files: log file 1 content");
        assertEquals(MSG10 + CoreConstants.CRLF, file02, "Log to files: log file 2 content");
        assertEquals(MSG09 + CoreConstants.CRLF, file03, "Log to files: log file 3 content");
        assertEquals(MSG08 + CoreConstants.CRLF, file04, "Log to files: log file 4 content");
        assertEquals(MSG07 + CoreConstants.CRLF, file05, "Log to files: log file 5 content");
        assertEquals(MSG06 + CoreConstants.CRLF, file06, "Log to files: log file 6 content");
        assertEquals(MSG05 + CoreConstants.CRLF, file07, "Log to files: log file 7 content");
        assertEquals(MSG04 + CoreConstants.CRLF, file08, "Log to files: log file 8 content");
        assertEquals(MSG03 + CoreConstants.CRLF, file09, "Log to files: log file 9 content");
        assertEquals(MSG02 + CoreConstants.CRLF, file10, "Log to files: log file 10 content");

        final boolean test01 = !log01.exists() || log01.delete();
        assertTrue(test01, "Log to files: delete 1");

        final boolean test02 = !log02.exists() || log02.delete();
        assertTrue(test02, "Log to files: delete 2");

        final boolean test03 = !log03.exists() || log03.delete();
        assertTrue(test03, "Log to files: delete 3");

        final boolean test04 = !log04.exists() || log04.delete();
        assertTrue(test04, "Log to files: delete 4");

        final boolean test05 = !log05.exists() || log05.delete();
        assertTrue(test05, "Log to files: delete 5");

        final boolean test06 = !log06.exists() || log06.delete();
        assertTrue(test06, "Log to files: delete 6");

        final boolean test07 = !log07.exists() || log07.delete();
        assertTrue(test07, "Log to files: delete 7");

        final boolean test08 = !log08.exists() || log08.delete();
        assertTrue(test08, "Log to files: delete 8");

        final boolean test09 = !log09.exists() || log09.delete();
        assertTrue(test09, "Log to files: delete 9");

        final boolean test10 = !log10.exists() || log10.delete();
        assertTrue(test10, "Log to files: delete 10");
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
            final long length = file.length();
            final StringBuilder builder = new StringBuilder((int) length);

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
}
