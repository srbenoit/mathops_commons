package dev.mathops.commons.installation;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the Installation and Installations classes.
 */
final class TestInstallation {

    /** A test base directory. */
    private static final File BAD_BASE_DIR1 = new File(CoreConstants.QUOTE);

    /** A test base directory. */
    private static final File BASE_DIR = new File("/opt");

    /** A test base directory. */
    private static final File TEST_BASE_DIR1 = new File(BASE_DIR, "testbasedir1");

    /** A test base directory. */
    private static final File TEST_BASE_DIR2 = new File(BASE_DIR, "testbasedir2");

    /** A test base directory. */
    private static final File TEST_BASE_DIR3 = new File(BASE_DIR, "testbasedir3");

    /** A test config file. */
    private static final String TEST_CFG_FILE = "bogus.properties";

    /**
     * Sets up the test base directories.
     */
    @BeforeAll
    static void runBeforeClass() {

        makeDirs(TEST_BASE_DIR1);
        makeDirs(TEST_BASE_DIR2);
        makeDirs(TEST_BASE_DIR3);

        try (final FileOutputStream pw = new FileOutputStream(new File(TEST_BASE_DIR2,
                Installations.DEF_CFG_FILE_NAME))) {
            final byte[] bytes = ("ccc=ddd" + CoreConstants.CRLF).getBytes(StandardCharsets.UTF_8);
            pw.write(bytes);
        } catch (final IOException ex) {
            Log.warning(ex);
        }

        try (final FileOutputStream pw = new FileOutputStream(new File(TEST_BASE_DIR3, TEST_CFG_FILE))) {
            final byte[] bytes = ("z=y" + CoreConstants.CRLF).getBytes(StandardCharsets.UTF_8);
            pw.write(bytes);
        } catch (final IOException ex) {
            Log.warning(ex);
        }
    }

    /**
     * Cleans up the test base directories.
     */
    @AfterAll
    static void runAfterClass() {

        deleteFile(new File(TEST_BASE_DIR2, Installations.DEF_CFG_FILE_NAME));
        deleteFile(new File(TEST_BASE_DIR3, TEST_CFG_FILE));

        deleteFile(TEST_BASE_DIR1);
        deleteFile(TEST_BASE_DIR2);
        deleteFile(TEST_BASE_DIR3);

        final Installation installation = Installations.get().getInstallation(null, null);
        Installations.setMyInstallation(installation);
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get()")
    void testInstallationsGet() {

        final Installations installation = Installations.get();
        assertNotNull(installation, "testInstallationsGet 1");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir, file) with bad directory")
    void testInstallationGet1() {

        // Expect an InvalidPathException
        try {
            Installations.get().getInstallation(BAD_BASE_DIR1, TEST_CFG_FILE);
            fail("testInstallationGet1");
        } catch (final InvalidPathException ex) {
            Installations.get();
        }
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir, file)")
    void testInstallationGet2() {

        // Base directory that exists, but missing config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR1, TEST_CFG_FILE);

        assertNotNull(installation, "testInstallationGet 2");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir, file) retrieved directory")
    void testInstallationGet3() {

        // Base directory that exists, but missing config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR1, TEST_CFG_FILE);

        final File baseDir = installation.getBaseDir();
        assertEquals(TEST_BASE_DIR1, baseDir, "testInstallationGet 3");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir, file) retrieved file")
    void testInstallationGet4() {

        // Base directory that exists, but missing config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR1, null);

        final String cfgFile = installation.getCfgFile();
        assertEquals(Installations.DEF_CFG_FILE_NAME, cfgFile, "testInstallationGet 4");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir, file) loaded flag")
    void testInstallationGet5() {

        // Base directory that exists, but missing config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR1, TEST_CFG_FILE);

        final boolean loaded = installation.isLoaded();
        assertFalse(loaded, "testInstallationGet 5");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir)")
    void testInstallationGet6() {

        // Base directory that exists and uses standard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR2, null);

        assertNotNull(installation, "testInstallationGet 6");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir) retrieved directory")
    void testInstallationGet7() {

        // Base directory that exists and uses standard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR2, null);

        final File baseDir = installation.getBaseDir();
        assertEquals(TEST_BASE_DIR2, baseDir, "testInstallationGet 7");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir) retrieved file")
    void testInstallationGet8() {

        // Base directory that exists and uses standard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR2, null);

        final String cfgFile = installation.getCfgFile();
        assertEquals(Installations.DEF_CFG_FILE_NAME, cfgFile, "testInstallationGet 8");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("Installations.get(dir) loaded flag")
    void testInstallationGet9() {

        // Base directory that exists and uses standard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR2, null);

        final boolean loaded = installation.isLoaded();
        assertTrue(loaded, "testInstallationGet 9");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("getProperty() value")
    void testInstallationGet12() {

        // Base directory that exists and uses standard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR2, null);
        final String prop = installation.getProperties().getProperty("ccc");

        assertEquals("ddd", prop, "testInstallationGet 12");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("getInstallation(dir, file) with nonstandard file")
    void testInstallationGet13() {

        // Base directory that exists and uses nonstandard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR3, TEST_CFG_FILE);

        assertNotNull(installation, "testInstallationGet 13");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("getInstallation(dir, file) with nonstandard file, retrieved directory")
    void testInstallationGet14() {

        // Base directory that exists and uses nonstandard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR3, TEST_CFG_FILE);

        final File baseDir = installation.getBaseDir();
        assertEquals(TEST_BASE_DIR3, baseDir, "testInstallationGet 14");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("getInstallation(dir, file) with nonstandard file, retrieved file")
    void testInstallationGet15() {

        // Base directory that exists and uses nonstandard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR3, TEST_CFG_FILE);

        final String cfgFile = installation.getCfgFile();
        assertEquals(TEST_CFG_FILE, cfgFile, "testInstallationGet 15");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("getInstallation(dir, file) with nonstandard file, loaded flag")
    void testInstallationGet16() {

        // Base directory that exists and uses nonstandard config file
        final Installation installation = Installations.get().getInstallation(TEST_BASE_DIR3, TEST_CFG_FILE);

        final boolean loaded = installation.isLoaded();
        assertTrue(loaded, "testInstallationGet 16");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("getInstallation(dir, file) with nonstandard file, get property")
    void testInstallationGet19() {

        // Base directory that exists and uses nonstandard config file
        final Installation installation =
                Installations.get().getInstallation(TEST_BASE_DIR3, TEST_CFG_FILE);
        final String prop = installation.getProperties().getProperty("z");

        assertEquals("y", prop, "testInstallationGet 19");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("???")
    void testThreadLocal1() {

        final Installations installations = Installations.get();

        installations.getInstallation(TEST_BASE_DIR3, TEST_CFG_FILE);

        final Installation installation = Installations.getMyInstallation();
        assertNull(installation, "testThreadLocal 1");
    }

    /**
     * Test case.
     */
    @Test
    @DisplayName("???")
    void testThreadLocal2() {

        final Installations installations = Installations.get();

        final Installation inst = installations.getInstallation(TEST_BASE_DIR3, TEST_CFG_FILE);
        Installations.setMyInstallation(inst);

        final Installation installation = Installations.getMyInstallation();
        assertSame(inst, installation, "testThreadLocal 2");
    }

    /**
     * Deletes a file.
     *
     * @param file the file
     */
    private static void deleteFile(final File file) {

        if (file.exists() && !file.delete()) {
            Log.warning("Failed to delete file");
        }
    }

    /**
     * Creates a directory path.
     *
     * @param path the directory path
     */
    private static void makeDirs(final File path) {

        if (!path.mkdirs()) {
            Log.warning("Failed to create directory");
        }
    }
}
