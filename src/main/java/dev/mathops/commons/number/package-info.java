/**
 * Types representing numbers.
 *
 * <p>These classes reside in the commons package rather than the math package so they can
 * be used more easily as values in model tree nodes (the stock stringifier for {@code Number} types can include these
 * classes).  If these resided in the math package, that package would have to replace the {@code Number} stringifier
 * with an enhanced version.
 */
package dev.mathops.commons.number;
