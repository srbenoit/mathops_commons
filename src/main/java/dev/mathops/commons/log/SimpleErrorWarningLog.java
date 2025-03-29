package dev.mathops.commons.log;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a simple implementation of an error and warning log that may serve as a base class.
 */
public class SimpleErrorWarningLog implements IErrorWarningLog {

    /** A list of errors encountered during initialization. */
    private final List<String> errors;

    /** A list of warnings encountered during initialization. */
    private final List<String> warnings;

    /**
     * Constructs a new {@code SimpleErrorWarningLog}.
     */
    public SimpleErrorWarningLog() {

        this.errors = new ArrayList<>(1);
        this.warnings = new ArrayList<>(1);
    }

    /**
     * Adds an error to the list of errors encountered during initialization.
     *
     * @param message the error message
     */
    @Override
    public final void addError(final String message) {

        this.errors.add(message);
    }

    /**
     * Adds a warning to the list of warnings encountered during initialization.
     *
     * @param message the warning message
     */
    @Override
    public final void addWarning(final String message) {

        this.warnings.add(message);
    }

    /**
     * Tests whether any errors have been added to the error log.
     *
     * @return {@code true} if at least one error has been added
     */
    @Override
    public final boolean isError() {

        return !this.errors.isEmpty();
    }

    /**
     * Tests whether any warnings have been added to the warning log.
     *
     * @return {@code true} if at least one warning has been added
     */
    @Override
    public final boolean isWarning() {

        return !this.warnings.isEmpty();
    }

    /**
     * Gets the full list of errors that have been added to the error log.
     *
     * @return the array of errors; an empty array if none have been added
     */
    @Override
    public final String[] getErrors() {

        final int size = this.errors.size();
        return this.errors.toArray(new String[size]);
    }

    /**
     * Gets the full list of errors that have been added to the error log.
     *
     * @return the list of errors; an empty array if none have been added
     */
    @Override
    public final String getErrorsHtml() {

        final int size = this.errors.size();
        final StringBuilder builder = new StringBuilder(size * 40);
        for (final String err : this.errors) {
            builder.append(err);
            builder.append("</br>");
        }

        return builder.toString();
    }

    /**
     * Gets the full list of warnings that have been added to the warning log.
     *
     * @return the array of warnings; an empty array if none have been added
     */
    @Override
    public final String[] getWarnings() {

        final int size = this.errors.size();
        return this.warnings.toArray(new String[size]);
    }

    /**
     * Logs a warning to the system log and to the warnings list of an {@code ILogicProcess}.
     *
     * @param message the message to log
     */
    @Override
    public final void indicateWarning(final String message) {

        Log.warning(message);
        addWarning(message);
    }

    /**
     * Logs an error to the system log and to the errors list of an {@code ILogicProcess}.
     *
     * @param message the message to log
     */
    @Override
    public final void indicateError(final String message) {

        Log.severe(message);
        addError(message);
    }

    /**
     * Logs the failure to obtain an interface to the system log and to the errors list of an {@code ILogicProcess}.
     *
     * @param name the name of the interface being queried
     */
    @Override
    public final void indicateNoInterface(final String name) {

        final String msg = Res.fmt(Res.ERR_LOG_NO_INTERFACE, name);

        Log.severe(msg);
        addError(msg);
    }

    /**
     * Logs a failure with an exception to the system log and to the errors list of an {@code ILogicProcess}.
     *
     * @param message the message to log
     * @param ex      the exception that was thrown
     */
    @Override
    public final void indicateException(final String message, final Throwable ex) {

        Log.severe(message, ex);
        final String msg = ex.getMessage();
        addError(message + ": " + msg);
    }
}
