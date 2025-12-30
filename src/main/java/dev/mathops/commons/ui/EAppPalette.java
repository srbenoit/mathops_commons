package dev.mathops.commons.ui;

/**
 * A set of pre-defined application palettes.
 */
public enum EAppPalette {

    /** A basic color palette with "ANSI-like" colors */
    BASIC("Basic"),
    ;

    /** The label. */
    private final String label;

    /**
     * Constructs a new {@code EAppPalette}.
     *
     * @param theLabel the label
     */
    EAppPalette(final String theLabel) {

        this.label = theLabel;
    }

    /**
     * Gets a string representation of the object (the label).
     *
     * @return the label
     */
    @Override
    public String toString() {

        return this.label;
    }
}
