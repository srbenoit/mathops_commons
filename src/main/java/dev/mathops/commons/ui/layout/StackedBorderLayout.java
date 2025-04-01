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

import dev.mathops.commons.log.Log;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.List;

/**
 * A layout manager that behaves much like {@code BorderLayout}, but under which multiple components can be added to
 * each of the four edges. They will then "stack" from the edge toward the center, as if a sequence of nested containers
 * with {@code BorderLayout} layouts were created.
 */
public class StackedBorderLayout implements LayoutManager2 {

    /** A common position. */
    private static final float HALF = 0.5f;

    /** The horizontal gaps between components. */
    private final int hgap;

    /** The vertical gaps between components. */
    private final int vgap;

    /** Constant to specify components location to be the north portion of the border layout. */
    private final List<Component> north;

    /** Constant to specify components location to be the west portion of the border layout. */
    private final List<Component> west;

    /** Constant to specify components location to be the east portion of the border layout. */
    private final List<Component> east;

    /** Constant to specify components location to be the south portion of the border layout. */
    private final List<Component> south;

    /** Constant to specify components location to be the center portion of the border layout. */
    private Component center = null;

    /** The north layout constraint (top of container). */
    public static final String NORTH = "North";

    /** The south layout constraint (bottom of container). */
    public static final String SOUTH = "South";

    /** The east layout constraint (right side of container). */
    public static final String EAST = "East";

    /** The west layout constraint (left side of container). */
    public static final String WEST = "West";

    /** The center layout constraint (middle of container). */
    public static final String CENTER = "Center";

    /**
     * Constructs a new stacked border layout with no gaps between components.
     */
    public StackedBorderLayout() {

        this(0, 0);
    }

    /**
     * Constructs a stacked border layout with the specified gaps between components. The horizontal gap is specified by
     * {@code hgap} and the vertical gap is specified by {@code vgap}.
     *
     * @param theHorizontalGap the horizontal gap.
     * @param theVerticalGap   the vertical gap.
     */
    public StackedBorderLayout(final int theHorizontalGap, final int theVerticalGap) {

        this.hgap = theHorizontalGap;
        this.vgap = theVerticalGap;

        this.north = new ArrayList<>(3);
        this.east = new ArrayList<>(3);
        this.west = new ArrayList<>(3);
        this.south = new ArrayList<>(3);
    }

    /**
     * Returns the horizontal gap between components.
     *
     * @return the horizontal gap between components
     * @since 1.1
     */
    public final int getHgap() {

        return this.hgap;
    }

    /**
     * Returns the vertical gap between components.
     *
     * @return the vertical gap between components
     * @since 1.1
     */
    public final int getVgap() {

        return this.vgap;
    }

    /**
     * Adds the specified component to the layout, using the specified constraint object. For border layouts, the
     * constraint must be one of the following constants: {@code NORTH}, {@code SOUTH}, {@code EAST}, {@code WEST}, or
     * {@code CENTER}.
     * <p>
     * Most applications do not call this method directly. This method is called when a component is added to a
     * container using the {@code Container.add} method with the same argument types.
     *
     * @param comp        the component to be added.
     * @param constraints an object that specifies how and where the component is added to the layout.
     * @throws IllegalArgumentException if the constraint object is not a string, or if it is not one of the five
     *                                  specified constants.
     */
    @Override
    public final void addLayoutComponent(final Component comp, final Object constraints) {

        synchronized (comp.getTreeLock()) {
            if (constraints == null || constraints instanceof String) {
                addLayoutComponent((String) constraints, comp);
            } else {
                throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
            }
        }
    }

    /**
     * Replaced by {@code addLayoutComponent(Component, Object)}.
     */
    @Override
    public final void addLayoutComponent(final String name, final Component comp) {

        synchronized (comp.getTreeLock()) {
            final String actual = name == null ? CENTER : name;

            switch (actual) {
                case CENTER -> this.center = comp;
                case NORTH -> this.north.add(comp);
                case SOUTH -> this.south.add(comp);
                case EAST -> this.east.add(comp);
                case WEST -> this.west.add(comp);
                case BorderLayout.PAGE_START -> {
                    logBorderLayoutConstantUsed();
                    this.north.add(comp);
                }
                case BorderLayout.PAGE_END -> {
                    logBorderLayoutConstantUsed();
                    this.south.add(comp);
                }
                case BorderLayout.LINE_START -> {
                    logBorderLayoutConstantUsed();
                    this.west.add(comp);
                }
                case BorderLayout.LINE_END -> {
                    logBorderLayoutConstantUsed();
                    this.east.add(comp);
                }
                default -> throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
            }
        }
    }

    /**
     * Logs the fact that a {@code BorderLayout} constant was used as a constraint for {@code StackedBorderLayout}.
     */
    private static void logBorderLayoutConstantUsed() {

        final String msg = Res.get(Res.BORDER_LAYOUT_CONST_USED);
        Log.warning(msg, new IllegalArgumentException("ex"));
    }

    /**
     * Removes the specified component from this border layout. This method is called when a container calls its
     * {@code remove} or {@code removeAll} methods. Most applications do not call this method directly.
     *
     * @param comp the component to be removed
     */
    @Override
    public final void removeLayoutComponent(final Component comp) {

        synchronized (comp.getTreeLock()) {
            if (comp == this.center) {
                this.center = null;
            } else {
                this.north.remove(comp);
                this.south.remove(comp);
                this.east.remove(comp);
                this.west.remove(comp);
            }
        }
    }

    /**
     * Determines the minimum size of the {@code target} container using this layout manager.
     * <p>
     * This method is called when a container calls its {@code getMinimumSize} method. Most applications do not call
     * this method directly.
     *
     * @param parent the container in which to do the layout
     * @return the minimum dimensions needed to lay out the subcomponents of the specified container
     */
    @Override
    public final Dimension minimumLayoutSize(final Container parent) {

        synchronized (parent.getTreeLock()) {
            final Dimension dim = new Dimension(0, 0);

            for (final Component component : this.east) {
                final Dimension minSize = component.getMinimumSize();
                dim.width += minSize.width + this.hgap;
                dim.height = Math.max(minSize.height, dim.height);
            }

            for (final Component component : this.west) {
                final Dimension minSize = component.getMinimumSize();
                dim.width += minSize.width + this.hgap;
                dim.height = Math.max(minSize.height, dim.height);
            }

            if (this.center != null) {
                final Dimension minSize = this.center.getMinimumSize();
                dim.width += minSize.width;
                dim.height = Math.max(minSize.height, dim.height);
            }

            for (final Component component : this.north) {
                final Dimension minSize = component.getMinimumSize();
                dim.width = Math.max(minSize.width, dim.width);
                dim.height += minSize.height + this.vgap;
            }

            for (final Component component : this.south) {
                final Dimension minSize = component.getMinimumSize();
                dim.width = Math.max(minSize.width, dim.width);
                dim.height += minSize.height + this.vgap;
            }

            final Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    /**
     * Determines the preferred size of the {@code target} container using this layout manager, based on the components
     * in the container.
     * <p>
     * Most applications do not call this method directly. This method is called when a container calls its
     * {@code getPreferredSize} method.
     *
     * @param parent the container in which to do the layout
     * @return the preferred dimensions to lay out the subcomponents of the specified container
     */
    @Override
    public final Dimension preferredLayoutSize(final Container parent) {

        synchronized (parent.getTreeLock()) {
            final Dimension dim = new Dimension(0, 0);

            for (final Component component : this.east) {
                final Dimension prefSize = component.getPreferredSize();
                dim.width += prefSize.width + this.hgap;
                dim.height = Math.max(prefSize.height, dim.height);
            }

            for (final Component component : this.west) {
                final Dimension prefSize = component.getPreferredSize();
                dim.width += prefSize.width + this.hgap;
                dim.height = Math.max(prefSize.height, dim.height);
            }
            if (this.center != null) {
                final Dimension prefSize = this.center.getPreferredSize();
                dim.width += prefSize.width;
                dim.height = Math.max(prefSize.height, dim.height);
            }
            for (final Component component : this.north) {
                final Dimension prefSize = component.getPreferredSize();
                dim.width = Math.max(prefSize.width, dim.width);
                dim.height += prefSize.height + this.vgap;
            }
            for (final Component component : this.south) {
                final Dimension prefSize = component.getPreferredSize();
                dim.width = Math.max(prefSize.width, dim.width);
                dim.height += prefSize.height + this.vgap;
            }

            final Insets insets = parent.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    /**
     * Returns the maximum dimensions for this layout given the components in the specified target container.
     *
     * @param target the component which needs to be laid out
     */
    @Override
    public final Dimension maximumLayoutSize(final Container target) {

        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns the alignment along the x-axis. This specifies how the component would like to be aligned relative to
     * other components. The value should be a number between 0 and 1 where 0 represents alignment along the origin, 1
     * is aligned the furthest away from the origin, 0.5 is centered, etc.
     */
    @Override
    public final float getLayoutAlignmentX(final Container target) {

        return HALF;
    }

    /**
     * Returns the alignment along the y-axis. This specifies how the component would like to be aligned relative to
     * other components. The value should be a number between 0 and 1 where 0 represents alignment along the origin, 1
     * is aligned the furthest away from the origin, 0.5 is centered, etc.
     */
    @Override
    public final float getLayoutAlignmentY(final Container target) {

        return HALF;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager has cached information it should be discarded.
     */
    @Override
    public void invalidateLayout(final Container target) {

        // No action
    }

    /**
     * Lays out the container argument using this border layout.
     * <p>
     * This method actually reshapes the components in the specified container in order to satisfy the constraints of
     * this {@code BorderLayout} object. The {@code NORTH} and {@code SOUTH} components, if any, are placed at the top
     * and bottom of the container, respectively. The {@code WEST} and {@code EAST} components are then placed on the
     * left and right, respectively. Finally, the {@code CENTER} object is placed in any remaining space in the middle.
     * <p>
     * Most applications do not call this method directly. This method is called when a container calls its
     * {@code doLayout} method.
     *
     * @param parent the container in which to do the layout.
     * @see Container
     * @see Container#doLayout()
     */
    @Override
    public final void layoutContainer(final Container parent) {

        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            int top = insets.top;
            int bottom = parent.getHeight() - insets.bottom;
            int left = insets.left;
            int right = parent.getWidth() - insets.right;

            for (final Component component : this.north) {
                final int componentHeight = component.getHeight();
                component.setSize(right - left, componentHeight);
                final Dimension prefSize = component.getPreferredSize();
                component.setBounds(left, top, right - left, prefSize.height);
                top += prefSize.height + this.vgap;
            }

            for (final Component component : this.south) {
                final int componentHeight = component.getHeight();
                component.setSize(right - left, componentHeight);
                final Dimension prefSize = component.getPreferredSize();
                component.setBounds(left, bottom - prefSize.height, right - left, prefSize.height);
                bottom -= prefSize.height + this.vgap;
            }

            for (final Component component : this.east) {
                final int componentWidth = component.getWidth();
                component.setSize(componentWidth, bottom - top);
                final Dimension prefSize = component.getPreferredSize();
                component.setBounds(right - prefSize.width, top, prefSize.width, bottom - top);
                right -= prefSize.width + this.hgap;
            }

            for (final Component component : this.west) {
                final int componentWidth = component.getWidth();
                component.setSize(componentWidth, bottom - top);
                final Dimension prefSize = component.getPreferredSize();
                component.setBounds(left, top, prefSize.width, bottom - top);
                left += prefSize.width + this.hgap;
            }

            if (this.center != null) {
                this.center.setBounds(left, top, right - left, bottom - top);
            }
        }
    }

    /**
     * Returns a string representation of the state of this border layout.
     *
     * @return a string representation of this border layout.
     */
    @Override
    public final String toString() {

        final String hGapStr = Integer.toString(this.hgap);
        final String vGapStr = Integer.toString(this.vgap);

        final StringBuilder builder = new StringBuilder(60);

        builder.append("StackedBorderLayout[hgap=");
        builder.append(hGapStr);
        builder.append(",vgap=");
        builder.append(vGapStr);
        builder.append("]");

        return builder.toString();
    }
}
