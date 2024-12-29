package dev.mathops.commons.log;

import dev.mathops.commons.installation.Installation;

import java.util.Properties;

/**
 * Provides VM-wide control of the logging subsystem. There is a single log flow for the VM. Until an Installation has
 * been set in this class, log messages go solely to the console and all levels of event are logged. Once an
 * installation is set, logging preferences are read from that installation's base directory and file-based logging may
 * begin. Note that there can be multiple Installations on a single server computer, but each must run in its own VM,
 * and a single web server (such as Apache Tomcat) which runs in a VM may present only one Installation's websites.
 */
public final class LoggingSubsystem {

    /** The singleton instance. */
    private static final LoggingSubsystem INSTANCE = new LoggingSubsystem();

    /** The installation. */
    private Installation installation = null;

    /** The log settings. */
    private final LogSettings settings;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private LoggingSubsystem() {

        this.settings = new LogSettings();

        configureSettings(this.settings, null);
    }

    /**
     * Sets the installation. On this call, the logging preferences (if any are found) are loaded from the
     * installation's base directory and applied, which may begin file-based logging or alter the levels logged.
     *
     * @param theInstallation the installation ({@code null} to un-set the installation and revert back to console-only
     *                        logging)
     */
    public static void setInstallation(final Installation theInstallation) {

        synchronized (INSTANCE) {
            if (INSTANCE.innerGetInstallation() == null) {
                if (theInstallation != null) {
                    INSTANCE.innerSetInstallation(theInstallation);
                    configureSettings(INSTANCE.innerGetSettings(), theInstallation);
                }
            } else if (!INSTANCE.innerGetInstallation().equals(theInstallation)) {
                INSTANCE.innerSetInstallation(theInstallation);
                configureSettings(INSTANCE.innerGetSettings(), theInstallation);
            }
        }
    }

    /**
     * Accessor to encapsulate installation.
     *
     * @return the installation
     */
    private Installation innerGetInstallation() {

        return this.installation;
    }

    /**
     * Mutator to encapsulate installation.
     *
     * @param theInstallation the new installation
     */
    private void innerSetInstallation(final Installation theInstallation) {

        this.installation = theInstallation;
    }

    /**
     * Accessor to encapsulate settings.
     *
     * @return the settings
     */
    private LogSettings innerGetSettings() {

        return this.settings;
    }

    /**
     * Gets the installation.
     *
     * @return the installation
     */
    static Installation getInstallation() {

        synchronized (INSTANCE) {
            return INSTANCE.innerGetInstallation();
        }
    }

    /**
     * Configures the {@code LogSettings} based on the Installation.
     *
     * @param theSettings     the setting to configure
     * @param theInstallation the installation (could be {@code null})
     */
    public static void configureSettings(final LogSettings theSettings,
                                         final Installation theInstallation) {

        if (theInstallation == null) {
            theSettings.configure(null);
        } else {
            final Properties properties = theInstallation.getProperties();
            theSettings.configure(properties);
        }
        theSettings.setDirty(false);

        if (!theSettings.isAppend()) {
            Log.getWriter().rotateLogs();
        }
    }

    /**
     * Gets the log settings.
     *
     * @return the log settings
     */
    public static LogSettings getSettings() {

        return INSTANCE.innerGetSettings();
    }
}
