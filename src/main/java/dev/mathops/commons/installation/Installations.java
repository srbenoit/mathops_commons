package dev.mathops.commons.installation;

import dev.mathops.commons.CoreConstants;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages installations. Each installation is associated with the path of a single configuration file. The installation
 * associated with a running thread is stored in a thread local, so it can be accessed by any code executing under that
 * thread.
 */
public final class Installations {

    /** The default name of the configuration file. */
    public static final String DEF_CFG_FILE_NAME = "installation.properties";

    /** Commonly used string. */
    public static final String ZIRCON = "zircon";

    /** The default base directory path. */
    public static final String MATHOPS_PATH = "/opt/zircon";

    /** The default base directory. */
    public static final File DEF_BASE_DIR = new File(MATHOPS_PATH);

    /** The single instance of this object. */
    private static Installations inst = null;

    /** Installations, by configuration file. */
    private final Map<String, Installation> installationMap;

    /** A thread-local variable to store a thread's installation. */
    private static final ThreadLocalInstallation THREAD_LOCAL = new ThreadLocalInstallation();

    /**
     * Constructs a new {@code Installations}.
     */
    private Installations() {

        this.installationMap = new ConcurrentHashMap<>(3);
    }

    /**
     * Gets the single instance of this object.
     *
     * @return the instance
     */
    public static Installations get() {

        synchronized (CoreConstants.INSTANCE_SYNCH) {
            if (inst == null) {
                inst = new Installations();
            }

            return inst;
        }
    }

    /**
     * Gets the installation currently associated with the running thread.
     *
     * @return the installation (could be {@code null})
     */
    public static Installation getMyInstallation() {

        return THREAD_LOCAL.get();
    }

    /**
     * Sets the installation currently associated with the running thread.
     *
     * @param myInstallation the installation
     */
    public static void setMyInstallation(final Installation myInstallation) {

        THREAD_LOCAL.set(myInstallation);
    }

    /**
     * Gets the single instance corresponding to a specified configuration file.
     *
     * @param theBaseDir the base directory specified in command-line arguments
     * @param theCfgFile the name of the configuration file ({@code null} to use the default file name)
     * @return the instance
     * @throws InvalidPathException if the base directory does not represent a legitimate path
     */
    public Installation getInstallation(final File theBaseDir, final String theCfgFile) throws InvalidPathException {

        final File actualBaseDir = theBaseDir == null ? DEF_BASE_DIR : theBaseDir;
        final String actualCfgFile = theCfgFile == null ? DEF_CFG_FILE_NAME : theCfgFile;

        final File file = new File(actualBaseDir, actualCfgFile);
        final String path = file.getAbsolutePath();

        synchronized (this.installationMap) {
            Installation installation = this.installationMap.get(path);

            if (installation == null) {
                installation = new Installation(actualBaseDir, actualCfgFile);
                this.installationMap.put(path, installation);
            }

            return installation;
        }
    }

    /**
     * Gets the number of loaded installations.
     *
     * @return the number of installations
     */
    public int getNumInstallations() {

        synchronized (this.installationMap) {
            return this.installationMap.size();
        }
    }
}
