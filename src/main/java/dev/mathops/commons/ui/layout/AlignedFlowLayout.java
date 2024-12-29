/*
 * Copyright (C) 2022 Steve Benoit
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <https://www.gnu.org/licenses/>.
 */

package dev.mathops.commons.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * A layout manager that behaves much like {@code FlowLayout}, except that the vertical alignment of components with
 * different height can be specified.
 */
public final class AlignedFlowLayout implements LayoutManager {

    /** This value indicates that each row of components should be left-justified. */
    public static final int LEFT = 0;

    /** This value indicates that each row of components should be centered. */
    public static final int CENTER = 1;

    /** This value indicates that each row of components should be right-justified. */
    public static final int RIGHT = 2;

    /** This value indicates that each row of components should be vertically top-justified. */
    public static final int TOP = 3;

    /** This value indicates that each row of components should be vertically middle-justified. */
    public static final int MIDDLE = 4;

    /** This value indicates that each row of components should be vertically bottom-justified. */
    public static final int BOTTOM = 5;

    /** This value indicates that each row of components should be vertically baseline-justified. */
    public static final int BASELINE = 6;

    /** An empty array. */
    private static final int[] EMPTY_ARRAY = new int[0];

    /**
     * The property that determines how components are aligned horizontally in each row. It can be one of the following
     * values:
     * <ul>
     * <li>{@code LEFT}
     * <li>{@code RIGHT}
     * <li>{@code CENTER}
     * </ul>
     *
     * @see #getAlignment
     */
    private final int alignment;

    /**
     * The flow layout manager allows a separation of components with gaps. The horizontal gap will specify the space
     * between components and between the components and the borders of the {@code Container}.
     *
     * @see #getHGap()
     */
    private final int hGap;

    /**
     * The flow layout manager allows a separation of components with gaps. The vertical gap will specify the space
     * between rows and between the rows and the borders of the {@code Container}.
     *
     * @see #getHGap()
     */
    private final int vGap;

    /**
     * The property that determines how components are aligned vertically in each row. It can be one of the following
     * values:
     * <ul>
     * <li>{@code TOP}
     * <li>{@code MIDDLE}
     * <li>{@code BOTTOM}
     * <li>{@code BASELINE}
     * </ul>
     */
    private final int verticalAlign;

    /**
     * Constructs a new {@code AlignedFlowLayout} with a centered alignment, a default 5-unit horizontal and vertical
     * gap, and vertical centering.
     */
    public AlignedFlowLayout() {

        this(CENTER, 5, 5, MIDDLE);
    }

    /**
     * Constructs a new {@code AlignedFlowLayout} with the specified alignment, a default 5-unit horizontal and vertical
     * gap, and vertical centering.
     *
     * <p>
     * The value of the alignment argument must be one of {@code AlignedFlowLayout.LEFT},
     * {@code AlignedFlowLayout.RIGHT}, {@code AlignedFlowLayout.CENTER}.
     *
     * @param theAlignment the alignment value
     */
    public AlignedFlowLayout(final int theAlignment) {

        this(theAlignment, 5, 5, MIDDLE);
    }

    /**
     * Creates a new flow layout manager with the indicated alignment, horizontal and vertical gaps, and vertical
     * alignment.
     *
     * <p>
     * The value of the {@code align} argument must be one of {@code AlignedFlowLayout.LEFT},
     * {@code AlignedFlowLayout.RIGHT}, {@code AlignedFlowLayout.CENTER}.
     *
     * <p>
     * The value of the {@code valign} argument must be one of {@code AlignedFlowLayout.TOP},
     * {@code AlignedFlowLayout.MIDDLE}, {@code AlignedFlowLayout.BOTTOM}, or {@code AlignedFlowLayout.BASELINE}.
     *
     * @param theAlignment     the alignment value
     * @param theHGap          the horizontal gap between components and between the components and the borders of the
     *                         {@code Container}
     * @param theVGap          the vertical gap between components and between the components and the borders of the
     *                         {@code Container}
     * @param theVerticalAlign the vertical alignment
     */
    public AlignedFlowLayout(final int theAlignment, final int theHGap, final int theVGap, final int theVerticalAlign) {

        this.hGap = theHGap;
        this.vGap = theVGap;
        this.alignment = theAlignment;
        this.verticalAlign = theVerticalAlign;
    }

    /**
     * Gets the alignment for this layout. Possible values are {@code FlowLayout.LEFT}, {@code FlowLayout.RIGHT},
     * {@code FlowLayout.CENTER} .
     *
     * @return the alignment value for this layout
     * @see java.awt.FlowLayout#setAlignment
     */
    public int getAlignment() {

        return this.alignment;
    }

    /**
     * Gets the horizontal gap between components and between the components and the borders of the {@code Container}
     *
     * @return the horizontal gap between components and between the components and the borders of the
     *         {@code Container}
     * @see java.awt.FlowLayout#setHgap
     */
    public int getHGap() {

        return this.hGap;
    }

    /**
     * Gets the vertical gap between components and between the components and the borders of the {@code Container}.
     *
     * @return the vertical gap between components and between the components and the borders of the {@code Container}
     * @see java.awt.FlowLayout#setVgap
     */
    public int getVGap() {

        return this.vGap;
    }

    /**
     * Adds the specified component to the layout. Not used by this class.
     *
     * @param name the name of the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(final String name, final Component comp) {

        // No action
    }

    /**
     * Removes the specified component from the layout. Not used by this class.
     *
     * @param comp the component to remove
     * @see Container#removeAll
     */
    @Override
    public void removeLayoutComponent(final Component comp) {

        // No action
    }

    /**
     * Returns the preferred dimensions for this layout given the <i>visible</i> components in the specified target
     * container.
     *
     * @param parent the container that needs to be laid out
     * @return the preferred dimensions to lay out the subcomponents of the specified container
     * @see Container
     * @see #minimumLayoutSize
     * @see Container#getPreferredSize
     */
    @Override
    public Dimension preferredLayoutSize(final Container parent) {

        final boolean useBaseline = this.verticalAlign == BASELINE;

        synchronized (parent.getTreeLock()) {
            final Dimension dim = new Dimension(0, 0);
            final int numMembers = parent.getComponentCount();
            boolean firstVisible = true;
            int maxAscent = 0;
            int maxDescent = 0;

            for (int i = 0; i < numMembers; i++) {
                final Component component = parent.getComponent(i);
                if (component.isVisible()) {
                    final Dimension preferredSize = component.getPreferredSize();
                    dim.height = Math.max(dim.height, preferredSize.height);
                    if (firstVisible) {
                        firstVisible = false;
                    } else {
                        dim.width += this.hGap;
                    }
                    dim.width += preferredSize.width;
                    if (useBaseline) {
                        final int baseline = component.getBaseline(preferredSize.width, preferredSize.height);
                        if (baseline >= 0) {
                            maxAscent = Math.max(maxAscent, baseline);
                            maxDescent = Math.max(maxDescent, preferredSize.height - baseline);
                        }
                    }
                }
            }
            final Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right + (this.hGap << 1);
            dim.height += insets.top + insets.bottom + (this.vGap << 1);
            return dim;
        }
    }

    /**
     * Returns the minimum dimensions needed to lay out the <i>visible</i> components contained in the specified target
     * container.
     *
     * @param parent the container that needs to be laid out
     * @return the minimum dimensions to lay out the subcomponents of the specified container
     * @see #preferredLayoutSize
     * @see Container
     * @see Container#doLayout
     */
    @Override
    public Dimension minimumLayoutSize(final Container parent) {

        final boolean useBaseline = this.verticalAlign == BASELINE;
        final Dimension dim = new Dimension(0, 0);

        final int twiceHGap = this.hGap << 1;
        final int twiceVGap = this.vGap << 1;

        int maxAscent = 0;
        int maxDescent = 0;

        synchronized (parent.getTreeLock()) {
            final int numMembers = parent.getComponentCount();
            boolean firstVisible = true;

            for (int i = 0; i < numMembers; i++) {
                final Component component = parent.getComponent(i);
                if (component.isVisible()) {
                    final Dimension minimumSize = component.getMinimumSize();
                    dim.height = Math.max(dim.height, minimumSize.height);
                    if (firstVisible) {
                        firstVisible = false;
                    } else {
                        dim.width += this.hGap;
                    }
                    dim.width += minimumSize.width;
                    if (useBaseline) {
                        final int baseline = component.getBaseline(minimumSize.width, minimumSize.height);
                        if (baseline >= 0) {
                            maxAscent = Math.max(maxAscent, baseline);
                            maxDescent = Math.max(maxDescent, dim.height - baseline);
                        }
                    }
                }
            }

            final Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right + twiceHGap;
            dim.height += insets.top + insets.bottom + twiceVGap;
            return dim;
        }
    }

    /**
     * Lays out the container. This method lets each <i>visible</i> component take its preferred size by reshaping the
     * components in the target container in order to satisfy the alignment of this {@code FlowLayout} object.
     *
     * @param parent the specified component being laid out
     * @see Container
     * @see Container#doLayout
     */
    @Override
    public void layoutContainer(final Container parent) {

        final boolean useBaseline = this.verticalAlign == BASELINE;

        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            final int maxWidth = parent.getWidth() - (insets.left + insets.right + (this.hGap << 1));

            final int numMembers = parent.getComponentCount();
            final boolean ltr = parent.getComponentOrientation().isLeftToRight();

            final int[] ascent = useBaseline ? new int[numMembers] : EMPTY_ARRAY;
            final int[] descent = useBaseline ? new int[numMembers] : EMPTY_ARRAY;

            int x = 0;
            int y = insets.top + this.vGap;
            int rowHeight = 0;
            int start = 0;

            // Lay out members in rows, wrapping when width exceeds container's width,
            // and when each row is formed, perform horizontal/vertical alignment
            for (int i = 0; i < numMembers; ++i) {
                final Component component = parent.getComponent(i);

                if (component.isVisible()) {
                    final Dimension dimension = component.getPreferredSize();
                    component.setSize(dimension.width, dimension.height);

                    if (useBaseline) {
                        final int baseline = component.getBaseline(dimension.width, dimension.height);
                        if (baseline >= 0) {
                            ascent[i] = baseline;
                            descent[i] = dimension.height - baseline;
                        } else {
                            ascent[i] = -1;
                        }
                    }

                    if ((x == 0) || ((x + dimension.width) <= maxWidth)) {
                        if (x > 0) {
                            x += this.hGap;
                        }
                        x += dimension.width;
                        rowHeight = Math.max(rowHeight, dimension.height);
                    } else {
                        // Need to start a new row, so align the row we've just finished
                        rowHeight = moveComponents(parent, insets.left + this.hGap, y, maxWidth - x,
                                rowHeight, start, i, ltr, ascent, descent);

                        x = dimension.width;
                        y += this.vGap + rowHeight;
                        rowHeight = dimension.height;
                        start = i;
                    }
                }
            }

            moveComponents(parent, insets.left + this.vGap, y, maxWidth - x, rowHeight, start,
                    numMembers, ltr, ascent, descent);
        }
    }

    /**
     * Centers the elements in the specified row, if there is any slack.
     *
     * @param target    the component which needs to be moved
     * @param x         the x coordinate of the left edge of the layout space
     * @param y         the y coordinate of the top edge of the layout space
     * @param width     the remaining width (max width minus width of components in row)
     * @param rowHeight the height of the tallest component in the row
     * @param rowStart  the beginning of the row
     * @param rowEnd    the ending of the row
     * @param ltr       true if component layout is "left to right"
     * @param ascent    ascent for the components (only valid if verticalAlign is BASELINE)
     * @param descent   descent for the components (only valid if verticalAlign is BASELINE)
     * @return actual row height (baseline alignment can expand a row)
     */
    private int moveComponents(final Container target, final int x, final int y, final int width,
                               final int rowHeight, final int rowStart, final int rowEnd, final boolean ltr,
                               final int[] ascent, final int[] descent) {

        int xx = x;

        switch (this.alignment) {
            case LEFT:
                xx += ltr ? 0 : width;
                break;
            case RIGHT:
                xx += ltr ? width : 0;
                break;
            case CENTER:
            default:
                xx += width / 2;
                break;
        }

        int actualRowHeight = rowHeight;

        int maxAscent = 0;
        int nonBaselineHeight = 0;
        int baselineOffset = 0;
        if (this.verticalAlign == BASELINE) {
            int maxDescent = 0;

            for (int i = rowStart; i < rowEnd; i++) {
                final Component component = target.getComponent(i);
                if (component.isVisible()) {
                    if (ascent[i] >= 0) {
                        maxAscent = Math.max(maxAscent, ascent[i]);
                        maxDescent = Math.max(maxDescent, descent[i]);
                    } else {
                        final int height = component.getHeight();
                        nonBaselineHeight = Math.max(height, nonBaselineHeight);
                    }
                }
            }
            actualRowHeight = Math.max(maxAscent + maxDescent, nonBaselineHeight);
            baselineOffset = (actualRowHeight - maxAscent - maxDescent) / 2;
        }

        for (int i = rowStart; i < rowEnd; i++) {
            final Component component = target.getComponent(i);
            if (component.isVisible()) {
                int cy = y; // "top"

                if (this.verticalAlign == BOTTOM) {
                    cy = y + actualRowHeight - component.getHeight();
                } else if (this.verticalAlign == MIDDLE) {
                    cy = y + (actualRowHeight - component.getHeight()) / 2;
                } else if (this.verticalAlign == BASELINE) {
                    if (ascent[i] >= 0) {
                        cy = y + baselineOffset + maxAscent - ascent[i];
                    } else {
                        cy = y + (actualRowHeight - component.getHeight()) / 2;
                    }
                }

                if (ltr) {
                    component.setLocation(xx, cy);
                } else {
                    final int targetWidth = target.getWidth();
                    final int componentWidth = component.getWidth();
                    component.setLocation(targetWidth - xx - componentWidth, cy);
                }
                xx += component.getWidth() + this.hGap;
            }
        }

        return actualRowHeight;
    }

    /**
     * Returns a string representation of this {@code FlowLayout} object and its values.
     *
     * @return a string representation of this layout
     */
    @Override
    public String toString() {

        final String str = switch (this.alignment) {
            case LEFT -> ",align=left";
            case RIGHT -> ",align=right";
            default -> ",align=center";
        };

        return getClass().getName() + "[hgap=" + this.hGap + ",vgap=" + this.vGap + str + "]";
    }
}
