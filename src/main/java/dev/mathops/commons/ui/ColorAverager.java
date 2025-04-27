package dev.mathops.commons.ui;

import java.awt.Color;

/**
 * A class that can average (in RGB space) over several colors and generate the resultant color.
 */
public final class ColorAverager {

    /** The number of colors over which we are averaging. */
    private int n;

    /** The total accumulated Red component. */
    private int redAccumulator;

    /** The total accumulated Green component. */
    private int greenAccumulator;

    /** The total accumulated Blue component. */
    private int blueAccumulator;

    /**
     * Constructs a new {@code ColorAverager}.
     */
    public ColorAverager() {

        this.n = 0;
        this.redAccumulator = 0;
        this.greenAccumulator = 0;
        this.blueAccumulator = 0;
    }

    /**
     * Resets a {@code ColorAverager} to have zero colors in its accumulator.
     */
    public void reset() {

        this.n = 0;
        this.redAccumulator = 0;
        this.greenAccumulator = 0;
        this.blueAccumulator = 0;
    }

    /**
     * Adds a color.
     *
     * @param color the color to add
     */
    public void addColor(final Color color) {

        ++this.n;
        this.redAccumulator += color.getRed();
        this.greenAccumulator += color.getGreen();
        this.blueAccumulator += color.getBlue();
    }

    /**
     * Gets the number of colors being averaged.
     *
     * @return the number of colors
     */
    public int getN() {

        return this.n;
    }

    /**
     * Returns the average color.
     *
     * @return the average color (black if no colors have been added)
     */
    public Color getAverageColor() {

        final Color result;

        if (this.n == 0) {
            result = Color.BLACK;
        } else {
            final int half = this.n / 2;
            final int red = (this.redAccumulator + half) / this.n;
            final int green = (this.greenAccumulator + half) / this.n;
            final int blue = (this.blueAccumulator + half) / this.n;

            result = new Color(red, blue, green);
        }

        return result;
    }

    /**
     * Generates a string representation of the object.
     *
     * @return the string representation
     */
    public String toString() {

        final StringBuilder builder = new StringBuilder(30);

        builder.append("ColorAverager{n=");
        builder.append(this.n);
        builder.append(",r=");
        builder.append(this.redAccumulator);
        builder.append(",g=");
        builder.append(this.greenAccumulator);
        builder.append(",b=");
        builder.append(this.blueAccumulator);
        builder.append("}");

        return builder.toString();
    }
}