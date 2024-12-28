package dev.mathops.commons.unicode;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.builder.HtmlBuilder;
import dev.mathops.commons.file.FileLoader;
import dev.mathops.commons.log.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Performs lookups of Unicode block names based on the version 8.0.0 "Blocks.txt" file from the Unicode Character
 * Database.
 */
public final class UnicodeBlocks {

    /** The name of the file to read. */
    private static final String FILENAME = "Blocks.txt";

    /** The number of named Unicode blocks. */
    private static final int NUM_BLOCKS = 222;

    /** The singleton instance. */
    private static UnicodeBlocks instance = null;

    /** A map from normalized block name to code point range. */
    private final Map<String, CodePointRange> blocks;

    /** A map from normalized block name to non-normalized name. */
    private final Map<String, String> names;

    /** A map from partially normalized name (without spaces) to normalized name. */
    private final Map<String, String> noSpaceNames;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private UnicodeBlocks() {

        this.blocks = new HashMap<>(NUM_BLOCKS);
        this.names = new HashMap<>(NUM_BLOCKS);
        this.noSpaceNames = new HashMap<>(NUM_BLOCKS);

        loadBlocksFile();
    }

    /**
     * Attempts to load and populate the blocks map by reading the Blocks.txt file, which should be from the latest
     * Unicode character database.
     */
    private void loadBlocksFile() {

        final Class<? extends UnicodeBlocks> cls = getClass();
        final String[] lines = FileLoader.loadFileAsLines(cls, FILENAME, true);
        try {
            for (final String line : lines) {
                if (!line.isEmpty() && (int) line.charAt(0) != (int) '#') {
                    processLine(line);
                }
            }
        } catch (final NumberFormatException ex) {
            Log.warning("Invalid block specification in ", FILENAME, ex);
            this.blocks.clear();
        }
    }

    /**
     * Processes a single line of the input blocks file, after it has been discovered that the line is not a comment or
     * empty.
     *
     * @param line the line to process
     */
    private void processLine(final String line) {

        final int semi = line.indexOf((int) ';');
        final int dots = line.indexOf("..");

        if (semi != -1 && dots != -1) {
            final String firstStr = line.substring(0, dots);
            final int first = Integer.parseInt(firstStr, 16);

            final String lastStr = line.substring(dots + 2, semi);
            final int last = Integer.parseInt(lastStr, 16);

            final String name = line.substring(semi + 2);
            final String normalized = normalizeBlockName(name);

            this.blocks.put(normalized, new CodePointRange(first, last));
            this.names.put(normalized, name);
            final String stripped = stripSpaces(name);
            this.noSpaceNames.put(stripped, normalized);
        }
    }

    /**
     * Normalizes a block name by converting to lowercase and removing all whitespace, hyphens, and underbars.
     *
     * @param name the name to normalize
     * @return the normalized name
     */
    public static String normalizeBlockName(final CharSequence name) {

        final int len = name.length();
        final HtmlBuilder builder = new HtmlBuilder(len);

        for (int i = 0; i < len; ++i) {
            final char chr = name.charAt(i);

            if ((int) chr == (int) '-' || (int) chr == (int) '_' || (int) chr == (int) ' ' || (int) chr == (int) '\t') {
                continue;
            }

            final char lc = Character.toLowerCase(chr);
            builder.add(lc);
        }

        return builder.toString();
    }

    /**
     * Partially normalizes a block name by removing all whitespaces (used in XML Schema regular expression block name
     * matching). Case is not affected.
     *
     * @param name the name to normalize
     * @return the partially normalized name
     */
    public static String stripSpaces(final CharSequence name) {

        final int len = name.length();
        final HtmlBuilder builder = new HtmlBuilder(len);

        for (int i = 0; i < len; ++i) {
            final char chr = name.charAt(i);

            if ((int) chr == (int) ' ' || (int) chr == (int) '\t') {
                continue;
            }

            builder.add(chr);
        }

        return builder.toString();
    }

    /**
     * Gets the singleton instance, loading the blocks file if not already loaded.
     *
     * @return the singleton instance
     */
    public static UnicodeBlocks getInstance() {

        synchronized (CoreConstants.INSTANCE_SYNCH) {

            if (instance == null) {
                instance = new UnicodeBlocks();
            }

            return instance;
        }
    }

    /**
     * Tests whether a code point falls within the code point range specified for a block.
     *
     * @param codePoint the code point to test
     * @param name      the normalized or no-space block name
     * @return {@code true} if the block name was found and the character is in the block
     */
    public boolean isInBlock(final int codePoint, final String name) {

        CodePointRange range = this.blocks.get(name);

        if (range == null) {
            final String normalized = this.noSpaceNames.get(name);
            if (normalized != null) {
                range = this.blocks.get(normalized);
            }
        }

        return range != null && range.isInRange(codePoint);
    }

    /**
     * Tests whether string is a normalized block name.
     *
     * @param noSpaceName the normalized block name
     * @return {@code true} if the block name was found
     */
    public boolean isValidNoSpaceName(final String noSpaceName) {

        return this.noSpaceNames.containsKey(noSpaceName);
    }
}
