package dev.mathops.commons.file;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Locale;

/**
 * A filter to limit file views to only files with specified extensions (not case-sensitive).
 */
public final class ExtensionFileFilter extends FileFilter implements java.io.FileFilter {

    /** An {code ExtensionFileFilter} that accepts XML files (*.xml). */
    public static final ExtensionFileFilter XML_FILTER = new ExtensionFileFilter(".xml",
            Res.get(Res.XML_FILE_FILTER_DESC));

    /** The file extension, in lowercase, like ".xml". */
    private final String extension;

    /** The description, like "XML Files (.xml)". */
    private final String description;

    /**
     * Constructs a new {@code ExtensionFileFilter}.
     *
     * @param theExtension   the extension, such as ".xml" or ".txt"
     * @param theDescription the description, such as "XML Files (.xml)"
     */
    public ExtensionFileFilter(final String theExtension, final String theDescription) {

        super();

        this.extension = theExtension.toLowerCase(Locale.ROOT);
        this.description = theDescription;
    }

    /**
     * Tests whether the specified abstract pathname should be included in a pathname list.
     *
     * @param file The abstract pathname to be tested
     * @return {@code true} if and only if {@code pathname} should be included
     */
    @Override
    public boolean accept(final File file) {

        return file.isDirectory() || file.getName().toLowerCase(Locale.ROOT).endsWith(this.extension);
    }

    /**
     * Gets the description of this filter.
     *
     * @return the description
     */
    @Override
    public String getDescription() {

        return this.description;
    }
}
