package dev.mathops.commons.log;

import dev.mathops.commons.CoreConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Rotates log files. Renames each indexed archive log file to the next larger index, discarding the file with maximum
 * index file if one exists, then renames the active log file to the first index archive. This is a utility class, not
 * intended to be instantiated, with only static methods.
 */
final class LogRotator {

    /** File extension for log files. */
    private static final String EXTENSION = ".log";

    /** Base for decimal numbers. */
    private static final int DEC_BASE = 10;

    /** Buffer size for file copies. */
    private static final int BUF_SIZE = 2048;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private LogRotator() {

        super();
    }

    /**
     * Rotates the log files when the active log file reaches its file size limit.
     *
     * @param logDir       the log directory
     * @param filenameBase the base of log filenames
     * @param maxNumFiles  the maximum number of files
     * @param curFile      the currently active log file
     * @return error text on failure
     */
    static String rotateLogs(final File logDir, final String filenameBase, final long maxNumFiles, final File curFile) {

        final StringBuilder err = new StringBuilder(100);

        // see how many numbered log files there are
        int onFile = 1;
        while ((long) onFile < maxNumFiles) {
            final String filename = makeFilename(filenameBase, onFile);
            final File file = new File(logDir, filename);
            if (!file.exists()) {
                break;
            }
            ++onFile;
        }

        // "onFile" will be one larger than the highest numbered log file found

        // starting at index of last file and working downward, rename files
        final String filename0 = makeFilename(filenameBase, onFile);
        File dstFile = new File(logDir, filename0);
        while (onFile > 1) {
            final String filename1 = makeFilename(filenameBase, onFile - 1);
            final File srcFile = new File(logDir, filename1);
            renameFile(srcFile, dstFile, err);
            dstFile = srcFile;
            --onFile;
        }

        // Move the active log file
        if (curFile.exists()) {
            if (!curFile.renameTo(dstFile)) {
                final String dstPath = dstFile.getAbsolutePath();
                final String curPath = curFile.getAbsolutePath();
                final String msg1 = Res.fmt(Res.RENAME_FAIL, curPath, dstPath);
                err.append(msg1);
                err.append(CoreConstants.CRLF);

                if (copyFile(curFile, dstFile, err)) {
                    if (!curFile.delete()) {
                        final String msg2 = Res.fmt(Res.DELETE_FAIL, curPath);
                        err.append(msg2);
                        err.append(CoreConstants.CRLF);
                    }
                } else {
                    final String msg3 = Res.fmt(Res.COPY_FAIL, curPath, dstPath);
                    err.append(msg3);
                    err.append(CoreConstants.CRLF);
                }
            }
        }

        return err.length() > 0 ? err.toString() : null;
    }

    /**
     * Builds the filename of the archived log file for a given index.
     *
     * @param filenameBase the base of log filenames
     * @param index        the index
     * @return the log file name
     */
    private static String makeFilename(final String filenameBase, final int index) {

        final int hundreds = index / DEC_BASE / DEC_BASE;
        final int tens = (index / DEC_BASE) % DEC_BASE;
        final int ones = index % DEC_BASE;

        return filenameBase + "_" + hundreds + tens + ones + EXTENSION;
    }

    /**
     * Attempts to rename a source file to a destination file.
     *
     * @param srcFile the source file
     * @param dstFile the destination file
     * @param err     a {@code HtmlBuilder} to which to log errors
     */
    private static void renameFile(final File srcFile, final File dstFile, final StringBuilder err) {

        if (dstFile.exists() && !dstFile.delete()) {
            final String sourcePath = srcFile.getPath();
            final String dstPath = dstFile.getPath();
            final String msg = Res.fmt(Res.COPY_FAIL, sourcePath, dstPath);
            err.append(msg);
            err.append(CoreConstants.CRLF);
        } else if (!srcFile.renameTo(dstFile)) {
            final String sourcePath = srcFile.getPath();
            final String dstPath = dstFile.getPath();
            final String msg = Res.fmt(Res.RENAME_FAIL, sourcePath, dstPath);
            err.append(msg);
            err.append(CoreConstants.CRLF);
        }
    }

    /**
     * Attempts to copy a file.
     *
     * @param source the source file
     * @param dest   the destination file
     * @param err    a {@code HtmlBuilder} to which to log errors
     * @return {@code true} if copy succeeded
     */
    private static boolean copyFile(final File source, final File dest, final StringBuilder err) {

        boolean ok = false;

        final byte[] buffer = new byte[BUF_SIZE];
        try (final FileInputStream fis = new FileInputStream(source);
             final FileOutputStream fos = new FileOutputStream(dest)) {

            int len = fis.read(buffer);
            while (len > 0) {
                fos.write(buffer, 0, len);
                len = fis.read(buffer);
            }
            ok = true;
        } catch (final IOException ex) {
            final String exMessage = ex.getMessage();
            final String msg = Res.fmt(Res.COPY_ERROR, exMessage);
            err.append(msg);
            err.append(CoreConstants.CRLF);
        }

        return ok;
    }
}
