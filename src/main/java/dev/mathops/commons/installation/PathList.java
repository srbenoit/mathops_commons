package dev.mathops.commons.installation;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * A class to read and store the list of path names where files are stored.
 */
public final class PathList {

    /** The filename of the paths properties file. */
    private static final String PATHS_FILE_NAME = "paths.properties";

    /** Equals character. */
    private static final String EQUALS = "=";

    /** Left brace character. */
    private static final String LEFT_BRACE = "{";

    /** Right brace character. */
    private static final String RIGHT_BRACE = "}";

    /** Object on which to synchronize creation of singleton instance. */
    private static final Object SYNCH = new Object();

    /** Date formatter for path list file header. */
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' HH:mm a", Locale.US);

    /** The singleton instance. */
    private static PathList instance = null;

    /** The base installation directory. */
    private final File baseDir;

    /** A map from a path enumeration to the corresponding path. */
    private final Map<EPath, File> paths;

    /**
     * Private constructor to prevent instantiation.
     *
     * @param theBaseDir the directory in which to find the "paths.properties" file
     */
    private PathList(final File theBaseDir) {

        super();

        this.baseDir = theBaseDir;
        this.paths = new EnumMap<>(EPath.class);

        final Properties defaults = loadDefaults();

        final File file = new File(theBaseDir, PATHS_FILE_NAME);
        final Properties props = new Properties(defaults);

        if (file.exists()) {
            try (final InputStream input = new FileInputStream(file)) {
                props.load(input);
            } catch (final IOException ex) {
                final String absolutePath = file.getAbsolutePath();
                final String errMsg = Res.fmt(Res.PATH_LIST_READ_ERR, absolutePath);
                final String msg = ex.getMessage();
                Log.warning(errMsg, msg);
            }
        }

        for (final EPath path : EPath.values()) {
            final String key = path.getKey();
            final String value = props.getProperty(key);
            final File propsFile = makeFile(value);
            this.paths.put(path, propsFile);
        }
    }

    /**
     * Gets the paths map.
     *
     * @return the paths map
     */
    public Map<EPath, File> getPaths() {

        return this.paths;
    }

    /**
     * Loads the default path properties from the {@code EPath} enumerated values.
     *
     * @return the loaded properties file
     */
    private static Properties loadDefaults() {

        final Properties defaults = new Properties();

        final EPath[] values = EPath.values();
        for (final EPath value : values) {
            String defPath = value.getDefaultPath();

            if (defPath != null) {
                // See if there is a replacement string, and apply it
                for (final EPath ePath : values) {
                    final String key = ePath.getKey();
                    final String replacement = defaults.getProperty(key);
                    if (replacement != null) {
                        final String str = LEFT_BRACE + key + RIGHT_BRACE;
                        defPath = defPath.replace(str, replacement);
                    }
                }

                final String valueKey = value.getKey();
                defaults.setProperty(valueKey, defPath);
            }
        }

        return defaults;
    }

    /**
     * Initializes the singleton instance of {@code PathList}.
     *
     * @param baseDir the directory in which to find the "paths.properties" file
     */
    public static void init(final File baseDir) {

        synchronized (SYNCH) {
            instance = new PathList(baseDir);
        }
    }

    /**
     * Gets the singleton instance of {@code PathList}.
     *
     * @return the singleton instance
     */
    public static PathList getInstance() {

        synchronized (SYNCH) {
            if (instance == null) {
                instance = new PathList(Installations.DEF_BASE_DIR);
            }
            return instance;
        }
    }

    /**
     * Generates a file based on a path.
     *
     * @param path the path
     * @return the corresponding {@code File} or {@code null} if the path is null
     */
    private File makeFile(final String path) {

        String actualPath = path;
        File result = null;

        if (actualPath != null) {
            final EPath[] values = EPath.values();
            for (final EPath value : values) {
                final File replacement = this.paths.get(value);
                if (replacement != null) {
                    final String testStr = LEFT_BRACE + value.getKey() + RIGHT_BRACE;
                    final String normalized = normalize(replacement);
                    actualPath = actualPath.replace(testStr, normalized);
                }
            }

            result = new File(actualPath);
        }

        return result;
    }

    /**
     * Generates the normalized path of a file, which is an absolute path using '/' as a file separator.
     *
     * @param file the file
     * @return the normalized path, of the form "/a/b/c/name"
     */
    private static String normalize(final File file) {

        String path = file.getName();
        File parent = file.getParentFile();

        if (parent != null) {
            final StringBuilder builder = new StringBuilder(50);
            while (parent != null) {
                builder.setLength(0);
                final String parentName = parent.getName();
                builder.append(parentName).append(CoreConstants.SLASH).append(path);
                path = builder.toString();
                parent = parent.getParentFile();
            }
        }

        return path;
    }

    /**
     * Retrieves the base directory.
     *
     * @return the base directory
     */
    public File getBaseDir() {

        return this.baseDir;
    }

    /**
     * Retrieves the directory configured for a particular system path.
     *
     * @param path the path whose value to retrieve
     * @return the corresponding directory
     */
    public File get(final EPath path) {

        return this.paths.get(path);
    }

    /**
     * Sets new paths, which causes the paths file to be rewritten, and cached paths to be updated so future queries
     * will retrieve the new paths.
     *
     * @param newPaths the new paths
     */
    public void setPaths(final Map<EPath, ? extends File> newPaths) {

        if (newPaths.get(EPath.BASE_DIR) == null) {
            final String errMsg = Res.get(Res.PATH_LIST_NO_BASE);
            throw new IllegalArgumentException(errMsg);
        }

        this.paths.clear();
        this.paths.putAll(newPaths);

        final StringBuilder builder = new StringBuilder(500);

        final Class<? extends PathList> myClass = getClass();
        final String myClassName = myClass.getName();
        final LocalDateTime now = LocalDateTime.now();
        final String formattedNow = now.format(DATE_FMT);
        builder.append("# Created by ");
        builder.append(myClassName);
        builder.append(" on ");
        builder.append(formattedNow);
        builder.append(CoreConstants.CRLF);

        final EPath[] values = EPath.values();
        for (final EPath value : values) {
            if (value == EPath.BASE_DIR) {
                continue;
            }
            final File theFile = newPaths.get(value);
            if (theFile != null) {
                final String key = value.getKey();
                if (theFile.getAbsolutePath().startsWith("C:\\")) {
                    final String normalized = normalize(theFile);
                    builder.append(key);
                    builder.append(EQUALS);
                    builder.append(normalized);
                    builder.append(CoreConstants.CRLF);
                } else {
                    final String absolutePath = theFile.getAbsolutePath();
                    builder.append(key);
                    builder.append(EQUALS);
                    builder.append(absolutePath);
                    builder.append(CoreConstants.CRLF);
                }
            }
        }

        builder.append(CoreConstants.CRLF);

        final File newBaseDir = newPaths.get(EPath.BASE_DIR);
        final File file = new File(newBaseDir, PATHS_FILE_NAME);

        try (final FileOutputStream output = new FileOutputStream(file)) {
            final String str = builder.toString();
            final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            output.write(bytes);
        } catch (final IOException ex) {
            final String absolutePath = file.getAbsolutePath();
            final String errMsg = Res.fmt(Res.PATH_LIST_WRITE_ERR, absolutePath);
            final String msg = ex.getMessage();
            Log.warning(errMsg, msg);
        }
    }
}
