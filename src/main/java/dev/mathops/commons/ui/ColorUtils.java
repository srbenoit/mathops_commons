package dev.mathops.commons.ui;

/**
 * Color utilities.
 */
public enum ColorUtils {
    ;

    /** A bias applied to luminance values when calculating contrast. */
    private static final double LUMINANCE_BIAS = 0.05;

    /** The maximum error in the luminance. */
    private static final double MAX_LUMINANCE_ERROR = 0.00001;

    /**
     * Converts a color from sRGB to OkLab.
     *
     * @param srgb  a 3-element array with the R, G and B components of the sRGB color
     * @param okLab a 3-element array that will be populated with the L, a, and b components of the OkLab color
     */
    public static void sRGBToOkLab(final double[] srgb, final double[] okLab) {

        final double l1 = 0.4122214708 * srgb[0] + 0.5363325363 * srgb[1] + 0.0514459929 * srgb[2];
        final double m1 = 0.2119034982 * srgb[0] + 0.6806995451 * srgb[1] + 0.1073969566 * srgb[2];
        final double s1 = 0.0883024619 * srgb[0] + 0.2817188376 * srgb[1] + 0.6299787005 * srgb[2];

        final double l2 = Math.cbrt(l1);
        final double m2 = Math.cbrt(m1);
        final double s2 = Math.cbrt(s1);

        okLab[0] = 0.2104542553 * l2 + 0.7936177850 * m2 - 0.0040720468 * s2;
        okLab[1] = 1.9779984951 * l2 - 2.4285922050 * m2 + 0.4505937099 * s2;
        okLab[2] = 0.0259040371 * l2 + 0.7827717662 * m2 - 0.8086757660 * s2;
    }

    /**
     * Converts a color from OkLab to sRGB.  Note that some OkLab values fall outside the sRGB gamut and will result in
     * values outside the range 0.0 to 1.0.  Callers can either clamp values to 1.0, or (better) call
     * {@code rescaleToSRGBGamut} with the sRGB values to rescale the length of the (a, b) vector in OkLab space to fall
     * within the sRGB gamut.
     *
     * @param okLab a 3-element array with the L, a, and b components of the OkLab color
     * @param srgb  a 3-element array that will be populated with the R, G and B components of the sRGB color
     */
    public static void okLabToSRGB(final double[] okLab, final double[] srgb) {

        final double l1 = okLab[0] + 0.3963377774 * okLab[1] + 0.2158037573 * okLab[2];
        final double m1 = okLab[0] - 0.1055613458 * okLab[1] - 0.0638541728 * okLab[2];
        final double s1 = okLab[0] - 0.0894841775 * okLab[1] - 1.2914855480 * okLab[2];

        final double l2 = l1 * l1 * l1;
        final double m2 = m1 * m1 * m1;
        final double s2 = s1 * s1 * s1;

        srgb[0] = 4.0767416621 * l2 - 3.3077115913 * m2 + 0.2309699292 * s2;
        srgb[1] = -1.2684380046 * l2 + 2.6097574011 * m2 - 0.3413193965 * s2;
        srgb[2] = -0.0041960863 * l2 - 0.7034186147 * m2 + 1.7076147010 * s2;
    }

    /**
     * Given sRGB coordinates, ensures they fall within the range [0.0, 1.0].  If they do, no action is taken.  If they
     * do not, they are converted to OkLCH, then the Chroma is reduced until the sRGB equivalent lies within its gamut.
     *
     * @param srgb a 3-element array with the R, G, and B color components of the sRGB color to normalize
     */
    public static void normalizeSRGB(final double[] srgb) {

        // FIXME: for now, this is a simple clamp

        srgb[0] = srgb[0] < 0.0 ? 0.0 : Math.min(srgb[0], 1.0);
        srgb[1] = srgb[1] < 0.0 ? 0.0 : Math.min(srgb[1], 1.0);
        srgb[2] = srgb[2] < 0.0 ? 0.0 : Math.min(srgb[2], 1.0);
    }

    /**
     * Performs the relative luminance computation for an sRGB color as defined in
     * <a href='https://www.w3.org/TR/WCAG22/#dfn-relative-luminance'>https://www.w3.org/TR/WCAG22/</a>.
     *
     * @param srgb a 3-element array with the R, G, and B color components of the sRGB color (each from 0.0 to 1.0)
     * @return the relative luminance (from 0.0 to 1.0)
     */
    public static double computeLuminance(final double[] srgb) {

        final double sR = (double) srgb[0];
        final double sG = (double) srgb[1];
        final double sB = (double) srgb[2];

        final double newR = sR <= 0.04045 ? sR / 12.92 : Math.pow((sR + 0.055) / 1.055, 2.4);
        final double newG = sG <= 0.04045 ? sG / 12.92 : Math.pow((sG + 0.055) / 1.055, 2.4);
        final double newB = sB <= 0.04045 ? sB / 12.92 : Math.pow((sB + 0.055) / 1.055, 2.4);

        return 0.2126 * newR + 0.7152 * newG + 0.0722 * newB;
    }

    /**
     * Computes the contrast ratio of two colors using the formula defined in WCAG.  To be "accessible", foreground and
     * background need contrast of at least 4.5.
     *
     * <p>
     * If the darker color's luminance is 0.18333, the lighter color must have luminance 1.0 (this is the upper bound
     * for a workable dark color).
     *
     * <p>
     * If the darker color's luminance is 0 (black), the lighter color must have luminance .175 or greater.
     *
     * @param lighter the lighter color's relative luminance
     * @param darker  the darker color's relative luminance
     * @return the contrast (from 1.0 to 21.0), where 4.5 is required for WCAG AAA.
     */
    public static double computeContrast(final double lighter, final double darker) {

        return (lighter + LUMINANCE_BIAS) / (darker + LUMINANCE_BIAS);
    }
}
