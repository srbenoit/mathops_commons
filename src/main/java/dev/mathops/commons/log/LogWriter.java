package dev.mathops.commons.log;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.installation.Installation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * The class responsible for writing the records generated by an {@code Log} to some configured output.
 *
 * <p>
 * The writer can log to the system console, to an internal {@code List} of log messages, or to an archived file. When
 * writing to an archived file, there is a maximum file size and a maximum number of files. Log messages are always
 * written to the [fileNameBase].log file. When a logged message results in that file exceeding the maximum file size,
 * the logs are rotated, with [fileNameBase].log moving to [fileNameBase]_001.log, and so forth, up to the maximum
 * number of log files.
 */
public final class LogWriter extends LogEntryList {

    /** File extension for log files. */
    private static final String EXTENSION = ".log";

    /** A character that can appear in filenames. */
    private static final char SLASH = '/';

    /** A character that can appear in filenames. */
    private static final char BACKSLASH = '\\';

    /** A character that can appear in filenames. */
    private static final char CAP_A = 'A';

    /** A character that can appear in filenames. */
    private static final char CAP_Z = 'Z';

    /** A character that can appear in filenames. */
    private static final char COLON = ':';

    /** System output print stream that can support Unicode. */
    private final PrintStream sysOut;

    /** The log settings. */
    private final LogSettings settings;

    /** The current log file. */
    private File curFile = null;

    /**
     * Constructs a new {@code LogWriter}.
     */
    public LogWriter() {

        super();

        this.sysOut = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        this.settings = LoggingSubsystem.getSettings();

        if (LoggingSubsystem.getInstallation() != null && this.settings.isLogToFiles()) {
            final File logDir = determineLogDir();
            final String filenameBase = this.settings.getFilenameBase();
            this.curFile = new File(logDir, filenameBase + EXTENSION);

            if (this.curFile.exists() && !this.settings.isAppend()) {
                rotateLogs();
            }
        }
    }

    /**
     * Gets the log settings.
     *
     * @return the log settings
     */
    public LogSettings getSettings() {

        return this.settings;
    }

    /**
     * Writes the message to the console (with or without adding a linefeed), if console logging is enabled.
     *
     * @param msg      the message to write
     * @param linefeed {@code true} to include a linefeed
     */
    public void writeConsole(final String msg, final boolean linefeed) {

        synchronized (getSynch()) {
            if (this.settings.isLogToConsole()) {
                if (linefeed) {
                    this.sysOut.println(msg);
                } else {
                    this.sysOut.print(msg);
                }
                this.sysOut.flush();
            }
        }
    }

    /**
     * Writes the message to the log output.
     *
     * @param msg      the message to write
     * @param linefeed {@code true} to include a linefeed; {@code false} to omit
     */
    public void writeMessage(final String msg, final boolean linefeed) {

        synchronized (getSynch()) {
            writeConsole(msg, linefeed);
            addToList(msg);

            if (LoggingSubsystem.getInstallation() != null && this.settings.isLogToFiles()) {

                if (this.curFile == null) {
                    final File logDir = determineLogDir();
                    final String filenameBase = this.settings.getFilenameBase();
                    this.curFile = new File(logDir, filenameBase + EXTENSION);
                }

                try {
                    try (final FileOutputStream out = new FileOutputStream(this.curFile, true)) {
                        final byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
                        out.write(msgBytes);
                        if (linefeed) {
                            final byte[] lineEndBytes = CoreConstants.CRLF.getBytes(StandardCharsets.UTF_8);
                            out.write(lineEndBytes);
                        }
                    }
                } catch (final IOException ex) {
                    // Turn off file logging to prevent infinite loop when logging error
                    this.settings.setLogToFiles(false);
                    final String curFilePath = this.curFile.getPath();
                    final Class<? extends IOException> exClass = ex.getClass();
                    final String simpleName = exClass.getSimpleName();
                    final String logMsg = Res.fmt(Res.LOG_FAILED, curFilePath, simpleName);
                    writeMessage(logMsg, true);
                    this.settings.setLogToFiles(true);
                }

                if (this.settings.getLogFileSizeLimit() > 0
                    && this.curFile.length() > (long) this.settings.getLogFileSizeLimit()) {
                    rotateLogs();
                }
            }
        }
    }

    /**
     * Rotates the log files.
     */
    void rotateLogs() {

        synchronized (getSynch()) {
            if (this.curFile != null && this.curFile.exists()) {
                final File logDir = determineLogDir();
                final String filenameBase = this.settings.getFilenameBase();
                final int logFileCount = this.settings.getLogFileCount();
                final String error = LogRotator.rotateLogs(logDir, filenameBase, (long) logFileCount, this.curFile);

                if (error != null) {
                    // Turn off file logging to prevent infinite loop when logging error
                    this.settings.setLogToFiles(false);
                    writeMessage(error, true);
                    this.settings.setLogToFiles(true);
                }
            }
        }
    }

    /**
     * Retrieves the log file path property from the log settings and the base directory from the installation (if
     * needed) and builds the path where log files should be written.
     *
     * @return the log directory, or {@code null} if there is no installation or file logging is not enabled in the log
     *         settings, or the setting value was missing or zero-length
     */
    public File determineLogDir() {

        final Installation installation = LoggingSubsystem.getInstallation();
        File result = null;

        if (installation != null && this.settings.isLogToFiles()) {
            final String path = this.settings.getLogFilePath();

            if (path != null && !path.isEmpty()) {
                final int chr0 = (int) path.charAt(0);

                if (chr0 == (int) SLASH || chr0 == (int) BACKSLASH) {
                    result = new File(path);
                } else if (path.length() > 1 && chr0 >= (int) CAP_A && chr0 <= (int) CAP_Z
                           && (int) path.charAt(1) == (int) COLON) {
                    result = new File(path);
                } else {
                    final File baseDir = installation.getBaseDir();
                    result = new File(baseDir, path);
                }

                if (!result.exists() && !result.mkdirs()) {
                    final String absPath = result.getAbsolutePath();
                    final String msg = Res.fmt(Res.CANT_MK_LOG_DIR, absPath);
                    writeConsole(msg, true);
                }
            }
        }

        return result;
    }
}
