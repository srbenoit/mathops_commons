package dev.mathops.commons.log;

import dev.mathops.commons.CoreConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Handles the actual mechanics of writing log entries for a logger, including formatting log entries with dates.
 */
public class LogBase extends Synchronized {

    /** Bit flag to enable severe error logging. */
    public static final int SEVERE_BIT = 0x0001;

    /** Bit flag to enable warnings logging. */
    public static final int WARNING_BIT = 0x0002;

    /** Bit flag to enable informational logging. */
    public static final int INFO_BIT = 0x0004;

    /** Bit flag to enable configuration info logging. */
    public static final int CONFIG_BIT = 0x0008;

    /** Bit flag to enable entering message logging. */
    public static final int ENTERING_BIT = 0x0010;

    /** Bit flag to enable exiting message logging. */
    public static final int EXITING_BIT = 0x0020;

    /** Bit flag to enable fine message logging. */
    public static final int FINE_BIT = 0x0040;

    /** Bit flag to enable fine message logging. */
    public static final int FINEST_BIT = 0x0080;

    /** Bit flag to disable all logging. */
    public static final int NONE = 0;

    /** Bit flag to enable all levels of logging. */
    public static final int ALL = 0x00FF;

    /** Indentation to make follow-on lines align with lines that have log info. */
    private static final String INDENT = "                           ";

    /** Initial allocation size for string builder for log lines. */
    private static final int INIT_BUILDER_SIZE = 200;

    /** Formatter that provides compact format that includes milliseconds. */
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss.SSS", Locale.US);

    /** The thread-local log context. */
    private static final ThreadLocalLogContext LOG_CONTEXT = new ThreadLocalLogContext();

    /** A character used in filenames. */
    private static final char DOT = '.';


    /** The writer that will write log records to configured outputs. */
    private final LogWriter logWriter;

    /** The name of this package, with trailing dot. */
    private final String pkg;

    /**
     * Constructs a new {@code LogBase}.
     */
    LogBase() {

        super();

        this.logWriter = new LogWriter();

        final String clsName = LogBase.class.getName();
        final String simple = LogBase.class.getSimpleName();
        final int classNameLength = clsName.length();
        final int simpleNameLength = simple.length();
        this.pkg = clsName.substring(0, classNameLength - simpleNameLength);
    }

    /**
     * Gets the {@code LogWriter} used by this logger.
     *
     * @return the {@code LogWriter}
     */
    final LogWriter getLogWriter() {

        return this.logWriter;
    }

    /**
     * Gets the log settings.
     *
     * @return the log settings
     */
    final LogSettings getSettings() {

        return this.logWriter.getSettings();
    }

    /**
     * Logs a record with the format (where '*' is filled by severity character).
     *
     * <pre>
     * MM/DD HH:mm:ss.SSS * [content] ([className]:[methodName] line [lineNumber])
     *                      [any Throwables objects with stack trace]
     * </pre>
     *
     * @param severity the severity character to include in the log message
     * @param args     the list of arguments that make up the log message
     */
    final void log(final char severity, final Object... args) {

        final LogContext ctx = LOG_CONTEXT.get();
        final StringBuilder builder = new StringBuilder(100);

        final LocalDateTime now = LocalDateTime.now();
        final String formattedNow = now.format(DATE_FMT);
        builder.append(formattedNow);
        builder.append(CoreConstants.SPC_CHAR);
        if (ctx == null) {
            builder.append(INDENT);
        } else {
            builder.append(ctx);
        }
        builder.append(severity);
        builder.append(CoreConstants.SPC_CHAR);
        appendContent(builder, args);
        builder.append(CoreConstants.SPC_CHAR);
        appendSource(builder);
        addExceptionInfo(builder, INDENT, args);

        final String msg = builder.toString();
        this.logWriter.writeMessage(msg, true);
    }

    /**
     * Builds the content of the log message by concatenating the string representations of all non-{@code Throwable}
     * arguments.
     *
     * @param builder the {@code StringBuilder} to which to append
     * @param args    the arguments to concatenate
     */
    private void appendContent(final StringBuilder builder, final Object... args) {

        for (final Object arg : args) {
            if (arg == null) {
                builder.append("null");
            } else if (!(arg instanceof Throwable)) {
                if (arg instanceof Object[]) {
                    appendContent(builder, (Object[]) arg);
                } else {
                    final String argStr = arg.toString();
                    builder.append(argStr);
                }
            }
        }
    }

    /**
     * Builds the exception portion of the log message by concatenating the information and stack trace of all
     * {@code Throwable} arguments, in the order in which they appear in the arguments list.
     *
     * @param builder the {@code StringBuilder} to which to append
     * @param indent  the level to which to indent each line
     * @param args    the arguments to concatenate
     */
    private void addExceptionInfo(final StringBuilder builder, final String indent, final Object... args) {

        for (final Object arg : args) {
            if (arg instanceof Throwable thrown) {
                while (thrown != null) {
                    builder.append(CoreConstants.CRLF);
                    final Class<? extends Throwable> cls = thrown.getClass();
                    final String clsName = cls.getSimpleName();
                    builder.append(indent);
                    builder.append(clsName);

                    if (thrown.getLocalizedMessage() != null) {
                        final String locMsg = thrown.getLocalizedMessage();
                        builder.append(": ");
                        builder.append(locMsg);
                    }

                    final StackTraceElement[] stack = thrown.getStackTrace();

                    for (final StackTraceElement stackTraceElement : stack) {
                        builder.append(CoreConstants.CRLF);
                        final String stackItemStr = stackTraceElement.toString();
                        builder.append(indent);
                        builder.append(stackItemStr);
                    }

                    thrown = thrown.getCause();

                    if (thrown != null) {
                        builder.append(CoreConstants.CRLF);
                        builder.append(indent);
                        builder.append("CAUSED BY:");
                    }
                }
            }
        }
    }

    /**
     * Appends the source information to the log message, in the format.
     *
     * <pre>
     *   ([className]: [lineNumber])
     * </pre>
     * <p>
     * or
     *
     * <pre>
     *   (source unavailable for [className])
     * </pre>
     *
     * @param builder the {@code StringBuilder} to which to append
     */
    private void appendSource(final StringBuilder builder) {

        final IllegalArgumentException except = new IllegalArgumentException();
        final StackTraceElement[] stack = except.getStackTrace();
        boolean scanning = true;

        for (final StackTraceElement stackTraceElement : stack) {

            String clsname = stackTraceElement.getClassName();

            if (clsname.startsWith("jdk.internal.reflect.")
                || clsname.startsWith("java.lang.reflect.")
                || clsname.startsWith("org.junit.")) {
                continue;
            }

            if (clsname.endsWith(".java")) {
                final int classNameLen = clsname.length();
                clsname = clsname.substring(0, classNameLen - 5);
            }

            final int lastDot = clsname.lastIndexOf((int) '.');
            final String pkgname = lastDot == -1 ? clsname : clsname.substring(0, lastDot + 1);
            final String name = clsname.substring(lastDot + 1);

            if (pkgname.equals(this.pkg) && name.startsWith("Log")) {
                continue;
            }

            final int lineNumber = stackTraceElement.getLineNumber();
            final String lineNumberStr = Integer.toString(lineNumber);
            final String className = stackTraceElement.getClassName();
            builder.append(" (");
            builder.append(className);
            builder.append(".java:");
            builder.append(lineNumberStr);
            builder.append(")");
            scanning = false;
            break;
        }

        if (scanning) {
            final Class<? extends LogBase> cls = getClass();
            final String clsName = cls.getName();
            final String msg = Res.fmt(Res.NO_SRC, clsName);
            builder.append(msg);
        }
    }

    /**
     * Generates a string from a list of objects.
     *
     * @param args the objects
     * @return the resulting string
     */
    final String listToString(final Object... args) {

        final StringBuilder builder = new StringBuilder(100);

        appendContent(builder, args);

        return builder.toString();
    }

    /**
     * Sets the log context host, path, and remote address.
     *
     * @param theHost          the host
     * @param thePath          the path
     * @param theRemoteAddress the remote address
     */
    public static void setHostPath(final String theHost, final String thePath, final String theRemoteAddress) {

        if (theHost == null || thePath == null || theRemoteAddress == null) {
            LOG_CONTEXT.remove();
        } else {
            final LogContext ctx = new LogContext(theRemoteAddress);
            LOG_CONTEXT.set(ctx);
        }
    }

    /**
     * Sets the session and user ID associated with a log thread.
     *
     * @param sessionId the session ID
     * @param userId    the user ID
     */
    public static void setSessionInfo(final String sessionId, final String userId) {

        final LogContext ctx = LOG_CONTEXT.get();
        if (ctx != null) {
            ctx.setSession(sessionId, userId);
        }
    }
}
