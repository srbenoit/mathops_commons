package dev.mathops.commons.ui;

import dev.mathops.commons.log.Log;

import java.awt.Color;

/**
 * A palette for use in an application.  Palettes response to "light mode" and "dark mode" by basing their color choices
 * on the background color brightness.
 *
 * <p>
 * All foreground colors have a WCAG-compliant (AAA level) contrast with the background of at least 4.5:1.  If the basis
 * background is not dark or light enough to achieve this, it is adjusted.  We actually shoot for 4.7:1 in case an
 * accessibility checker uses a slightly different calculation of contrast or luminance.
 */
public final class AppPalette {

    /** The maximum value of an integer component of a color. */
    private static final double MAX_INT_COMPONENT = 255.0;

    /** A bias applied to luminance values when calculating contrast. */
    private static final double LUMINANCE_BIAS = 0.05;

    /** The minimum contrast that will be used. */
    private static final double MIN_CONTRAST = 4.7;

    /** The basis background color. */
    private final Color background;

    /**
     * A slightly accented background color (lighter in dark mode, darker in light mode) with hue to match the basis
     * background color.
     */
    private final Color accentedBackground1;

    /**
     * A more accented background color (lighter in dark mode, darker in light mode) with hue to match the basis
     * background color.
     */
    private final Color accentedBackground2;

    /** The "normal" foreground color (typically white in dark mode, black in light mode). */
    private final Color foreground;

    /** The first accent color - intended to bring attention to text or lines. */
    private final Color accent1;

    /** The second accent color - this color can be used to indicate an error condition. */
    private final Color accent2;

    /** The third accent color - this color can be used for mathematical expressions, numbers, and constants. */
    private final Color accent3;

    /** The fourth accent color - an extra color to add visual interest or indicate success condition. */
    private final Color accent4;

    /** The fifth accent color - an extra color to add visual interest. */
    private final Color accent5;

    /**
     * Constructs a new {@code AppPalette}.
     *
     * @param theBackground the basis background color (assumed to be in the sRGB color space - alpha is ignored)
     * @param whichPalette  the palette selector
     */
    public AppPalette(final Color theBackground, final EAppPalette whichPalette) {

        final double[] bgRgb = {(double) theBackground.getRed() / MAX_INT_COMPONENT,
                (double) theBackground.getGreen() / MAX_INT_COMPONENT,
                (double) theBackground.getBlue() / MAX_INT_COMPONENT};
        final double[] newRgb = new double[3];

        // Compute the background luminance (luminance is not perceived lightness - a color with luminance about 0.2
        // seems "half of full lightness", so we use that as the cutoff to detect "dark / light mode").
        final double bgLuminance = ColorUtils.computeLuminance(bgRgb);

        if (bgLuminance > 0.2) {
            // Light mode - the "second background accent" is the critical luminance level - we want this to be 0.85 or
            // greater, so we force the first accent to be 0.875 or higher, and the background to be 0.90 or higher.
            final double minLuminance;

            if (bgLuminance >= 0.62) {
                // This will keep the darkest accent above 0.5 luminance
                final int alpha = theBackground.getAlpha();
                this.background = alpha == 255 ? theBackground : new Color((float) bgRgb[0], (float) bgRgb[1],
                        (float) bgRgb[2]);

                final double accent1Luminance = bgLuminance * 0.9;
                setRgbLuminance(bgRgb, newRgb, accent1Luminance);
                this.accentedBackground1 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);

                final double accent2Luminance = accent1Luminance * 0.9;
                setRgbLuminance(bgRgb, newRgb, accent2Luminance);
                this.accentedBackground2 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);
                minLuminance = accent2Luminance;
            } else {
                // Given background is not light enough
                setRgbLuminance(bgRgb, newRgb, 0.62);
                this.background = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);

                setRgbLuminance(bgRgb, newRgb, 0.556);
                this.accentedBackground1 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);

                setRgbLuminance(bgRgb, newRgb, 0.5);
                this.accentedBackground2 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);
                minLuminance = 0.5;
            }

            this.foreground = Color.BLACK;
            this.accent1 = makeLightModeAccent1(minLuminance, whichPalette);
            this.accent2 = makeLightModeAccent2(minLuminance, whichPalette);
            this.accent3 = makeLightModeAccent3(minLuminance, whichPalette);
            this.accent4 = makeLightModeAccent4(minLuminance, whichPalette);
            this.accent5 = makeLightModeAccent5(minLuminance, whichPalette);
        } else {
            // Dark mode - the "second background accent" is the critical luminance level - we want this to be 0.08 or
            // less, so we force the first accent to be 0.065 or less, and the background to be 0.05 or less.

            if (bgLuminance <= 0.05) {
                final int alpha = theBackground.getAlpha();
                this.background = alpha == 255 ? theBackground : new Color((float) bgRgb[0], (float) bgRgb[1],
                        (float) bgRgb[2]);

                final double accent1Luminance = bgLuminance * 1.3;
                setRgbLuminance(bgRgb, newRgb, accent1Luminance);
                this.accentedBackground1 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);

                final double accent2Luminance = accent1Luminance * 1.23;
                setRgbLuminance(bgRgb, newRgb, accent2Luminance);
                this.accentedBackground2 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);
            } else {
                // Given background is not dark enough
                setRgbLuminance(bgRgb, newRgb, 0.05);
                this.background = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);

                setRgbLuminance(bgRgb, newRgb, 0.065);
                this.accentedBackground1 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);

                setRgbLuminance(bgRgb, newRgb, 0.08);
                this.accentedBackground2 = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);
            }

            this.foreground = Color.WHITE;
            this.accent1 = makeDarkModeAccent1(bgLuminance, whichPalette);
            this.accent2 = makeDarkModeAccent2(bgLuminance, whichPalette);
            this.accent3 = makeDarkModeAccent3(bgLuminance, whichPalette);
            this.accent4 = makeDarkModeAccent4(bgLuminance, whichPalette);
            this.accent5 = makeDarkModeAccent5(bgLuminance, whichPalette);
        }
    }

    /**
     * Gets the basis background color.
     *
     * @return the background color
     */
    public Color getBackground() {

        return this.background;
    }

    /**
     * Gets the slightly accented background color (lighter in dark mode, darker in light mode) with hue to match the
     * basis background color.
     *
     * @return the accented background color
     */
    public Color getAccentedBackground1() {

        return this.accentedBackground1;
    }

    /**
     * Gets the more accented background color (lighter in dark mode, darker in light mode) with hue to match the basis
     * background color.
     *
     * @return the accented background color
     */
    public Color getAccentedBackground2() {

        return this.accentedBackground2;
    }

    /**
     * Gets the "normal" foreground color (typically white in dark mode, black in light mode).
     *
     * @return the foreground color
     */
    public Color getForeground() {

        return this.foreground;
    }

    /**
     * Gets the first accent color - intended to bring attention to text or lines.
     *
     * @return the accent color
     */
    public Color getAccent1() {

        return this.accent1;
    }

    /**
     * Gets the second accent color - this color can be used to indicate an error condition.
     *
     * @return the accent color
     */
    public Color getAccent2() {

        return this.accent2;
    }

    /**
     * Gets the third accent color - this color can be used for mathematical expressions, numbers, and constants.
     *
     * @return the accent color
     */
    public Color getAccent3() {

        return this.accent3;
    }

    /**
     * Gets the fourth accent color - an extra color to add visual interest.
     *
     * @return the accent color
     */
    public Color getAccent4() {

        return this.accent4;
    }

    /**
     * Gets the fifth accent color - an extra color to add visual interest.
     *
     * @return the accent color
     */
    public Color getAccent5() {

        return this.accent5;
    }

    /**
     * Makes the "light mode" accent color 1 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeLightModeAccent1(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        switch (whichPalette) {
            case BASIC -> rgb[2] = 0.8; // Blue
        }

        return makeLightModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "light mode" accent color 2 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeLightModeAccent2(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        switch (whichPalette) {
            case BASIC -> rgb[0] = 0.8; // Red
        }

        return makeLightModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "light mode" accent color 3 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeLightModeAccent3(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        Log.info("Making accent 3");

        switch (whichPalette) {
            case BASIC -> {
                rgb[1] = 1.0; // Greenish blue
                rgb[2] = 1.0;
            }
        }

        return makeLightModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "light mode" accent color 4 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeLightModeAccent4(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        Log.info("Making accent 4");

        switch (whichPalette) {
            case BASIC -> {
                rgb[0] = 0.5; // Red
                rgb[1] = 0.5; // Green
            }
        }

        return makeLightModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "light mode" accent color 5 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeLightModeAccent5(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        Log.info("Making accent 5");

        switch (whichPalette) {
            case BASIC -> {
                rgb[0] = 1.0; // Purple
                rgb[2] = 1.0;
            }
        }

        return makeLightModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "dark mode" accent color - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeDarkModeAccent1(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        switch (whichPalette) {
            case BASIC -> {
                rgb[0] = 1.0; // Yellow
                rgb[1] = 1.0;
                rgb[2] = 0.6;
            }
        }

        return makeDarkModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "dark mode" accent color - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeDarkModeAccent2(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        switch (whichPalette) {
            case BASIC -> {
                rgb[0] = 1.0; // Red
                rgb[1] = 0.6;
                rgb[2] = 0.6;
            }
        }

        return makeDarkModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "light mode" accent color 3 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeDarkModeAccent3(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        switch (whichPalette) {
            case BASIC -> {
                rgb[0] = 0.6;
                rgb[1] = 1.0; // Greenish blue
                rgb[2] = 1.0;
            }
        }

        return makeDarkModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "light mode" accent color 4 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeDarkModeAccent4(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        switch (whichPalette) {
            case BASIC -> {
                rgb[0] = 0.6;
                rgb[1] = 1.0; // Green
                rgb[2] = 0.6;
            }
        }

        return makeDarkModeColor(bgLuminance, rgb);
    }

    /**
     * Makes the "light mode" accent color 5 - this assumes a light background.
     *
     * @param bgLuminance  the background luminance
     * @param whichPalette the palette choice
     * @return the accent 1 color
     */
    private static Color makeDarkModeAccent5(final double bgLuminance, final EAppPalette whichPalette) {

        final double[] rgb = new double[3];

        switch (whichPalette) {
            case BASIC -> {
                rgb[0] = 1.0; // Purple
                rgb[1] = 0.6;
                rgb[2] = 1.0;
            }
        }

        return makeDarkModeColor(bgLuminance, rgb);
    }

    /**
     * Creates a "light mode" accent color that is dark enough to contrast sufficiently with the light background.
     *
     * @param bgLuminance the background luminance
     * @param rgb         the target RGB color - luminosity may be adjusted to achieve target contrast level
     * @return the contrasting accent color
     */
    private static Color makeLightModeColor(final double bgLuminance, final double[] rgb) {

        final Color result;

        final double darkLuminance = ColorUtils.computeLuminance(rgb);
        final double contrast = ColorUtils.computeContrast(bgLuminance, darkLuminance);

        if (contrast < MIN_CONTRAST) {
            // Solve for dark luminance to achieve minimum contrast
            final double neededLuminance = (bgLuminance + LUMINANCE_BIAS) / MIN_CONTRAST - LUMINANCE_BIAS;
            final double[] newRgb = new double[3];
            setRgbLuminance(rgb, newRgb, neededLuminance);
//            Log.info("  Darkening color to luminance = " + neededLuminance);
            result = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);
        } else {
            result = new Color((float) rgb[0], (float) rgb[1], (float) rgb[2]);
        }

        return result;
    }

    /**
     * Creates a "dark mode" accent color that is light enough to contrast sufficiently with the dark background.
     *
     * @param bgLuminance the background luminance
     * @param rgb         the target RGB color - luminosity may be adjusted to achieve target contrast level
     * @return the contrasting accent color
     */
    private static Color makeDarkModeColor(final double bgLuminance, final double[] rgb) {

        final Color result;

        final double lightLuminance = ColorUtils.computeLuminance(rgb);
        final double contrast = ColorUtils.computeContrast(lightLuminance, bgLuminance);

        if (contrast < MIN_CONTRAST) {
            // Solve for light luminance to achieve minimum contrast
            final double neededLuminance = MIN_CONTRAST * (bgLuminance + LUMINANCE_BIAS) - LUMINANCE_BIAS;
            final double[] newRgb = new double[3];
//            Log.info("  Lightening color to luminance = " + neededLuminance);
            setRgbLuminance(rgb, newRgb, neededLuminance);
            result = new Color((float) newRgb[0], (float) newRgb[1], (float) newRgb[2]);
        } else {
            result = new Color((float) rgb[0], (float) rgb[1], (float) rgb[2]);
        }

        return result;
    }

    /**
     * Derives the RGB components of a color that has the same appearance as an original RGB color (hue and saturation)
     * but that has a target relative luminance.
     *
     * @param origRgb   an array of three doubles that hold the RGB components of the original RGB color
     * @param newRgb    an array of three doubles that will be populated with the RGB components of the new RGB color
     * @param luminance the target relative luminance
     */
    private static void setRgbLuminance(final double[] origRgb, final double[] newRgb, final double luminance) {

        System.arraycopy(origRgb, 0, newRgb, 0, 3);

        int loops = 0;

        double cur = ColorUtils.computeLuminance(newRgb);
        double delta = cur - luminance;
        double absDelta = Math.abs(delta);
        final double[] ok = new double[3];

        while (absDelta > 0.001 && loops < 1000) {
            ColorUtils.sRGBToOkLab(newRgb, ok);

            if (delta > 0.1) {
                // Need to darken fast
                ok[0] -= 0.01;
                ok[0] -= 0.001;
            } else if (delta > 0.0) {
                // Need to darken slowly
                ok[0] -= 0.001;
            } else if (delta < -0.1) {
                // Need to lighten fast
                ok[0] += 0.001;
            } else {
                // Need to lighten slowly
                ok[0] += 0.001;
            }
            ColorUtils.okLabToSRGB(ok, newRgb);
            ColorUtils.normalizeSRGB(newRgb);

            cur = ColorUtils.computeLuminance(newRgb);
            delta = cur - luminance;
            absDelta = Math.abs(delta);

            ++loops;
        }

//        Log.info("Color luminance was " + cur + " after " + loops + " loops, target was " + luminance);
    }
}
