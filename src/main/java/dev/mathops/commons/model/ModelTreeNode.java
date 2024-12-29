/*
 * Copyright (C) 2022 Steve Benoit
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the  License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU  General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If  not, see
 * <https://www.gnu.org/licenses/>.
 */

package dev.mathops.commons.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A node in a model tree. Every tree node tracks its parent node and a previous and next sibling node in a
 * doubly-linked list of siblings, as well as its attribute/property/content map.
 *
 * <p>
 * Any node may have an ordered list of child nodes.  This is achieved by adding two CONTENT value objects, one with
 * name "firstChild", one with name "lastChild", each storing a {@code ModelTreeNode} (the head and tail of the list).
 */
public class ModelTreeNode extends TypedMap {

    /** The NODE key used to store the parent node. */
    public static final NodeKey PARENT = new NodeKey("parent");

    /** The NODE key used to store the head of the doubly-linked list of child nodes. */
    public static final NodeKey FIRST_CHILD = new NodeKey("firstChild");

    /** The NODE key used to store the tail of the doubly-linked list of child nodes. */
    public static final NodeKey LAST_CHILD = new NodeKey("lastChild");

    /** The NODE key used to store the previous sibling node. */
    public static final NodeKey PREVIOUS_SIBLING = new NodeKey("previousSibling");

    /** The NODE key used to store the next sibling node. */
    public static final NodeKey NEXT_SIBLING = new NodeKey("nextSibling");

    /** The type of child nodes this node can hold. */
    private final EAllowedChildren allowedChildren;

    /**
     * Constructs a new {@code ModelTreeNode} that can hold any type of child node.
     */
    public ModelTreeNode() {

        super();

        this.allowedChildren = EAllowedChildren.ELEMENT_AND_DATA;
    }

    /**
     * Constructs a new {@code ModelTreeNode}.
     *
     * @param theAllowedChildren the types of child nodes this node can hold
     */
    public ModelTreeNode(final EAllowedChildren theAllowedChildren) {

        super();

        if (theAllowedChildren == null) {
            throw new IllegalArgumentException("Allowed children specifier may not be null");
        }

        this.allowedChildren = theAllowedChildren;
    }

    /**
     * Gets the types of child nodes this node can hold.
     *
     * @return the allowed children
     */
    public final EAllowedChildren getAllowedChildren() {

        return this.allowedChildren;
    }

    /**
     * Gets the parent node.
     *
     * @return the parent node; null if the root node
     */
    public final ModelTreeNode getParent() {

        return getNode(PARENT);
    }

    /**
     * Sets the parent node.
     *
     * @param newParent the new parent node; {code null} if the root node
     */
    public final void setParent(final ModelTreeNode newParent) {

        putNode(PARENT, newParent);
    }

    /**
     * Gets the first child node.
     *
     * @return the first child node; null if this node has no child nodes
     */
    public final ModelTreeNode getFirstChild() {

        return getNode(FIRST_CHILD);
    }

    /**
     * Gets the last child node.
     *
     * @return the last  child node; null if this node has no child nodes
     */
    public final ModelTreeNode getLastChild() {

        return getNode(LAST_CHILD);
    }

    /**
     * Adds a child node at the end of this node's list of children.
     *
     * @param node the child node to add
     */
    public final void addChild(final ModelTreeNode node) {

        node.setParent(this);

        final ModelTreeNode tail = getNode(LAST_CHILD);

        node.remove(NEXT_SIBLING);

        if (tail == null) {
            node.remove(PREVIOUS_SIBLING);
            putNode(FIRST_CHILD, node);
        } else {
            node.putNode(PREVIOUS_SIBLING, tail);
            tail.putNode(NEXT_SIBLING, node);
        }

        putNode(LAST_CHILD, node);
    }

    /**
     * Retrieves the ordered list of child elements as a {@code List}.
     *
     * @return the list
     */
    public final List<ModelTreeNode> getChildren() {

        List<ModelTreeNode> result;

        ModelTreeNode node = getFirstChild();
        if (node == null) {
            result = new ArrayList<>(0);
        } else {
            result = new ArrayList<>(10);
            while (node != null) {
                result.add(node);
                node = node.getNextSibling();
            }
        }

        return result;
    }

    /**
     * Gets the previous sibling node.
     *
     * @return the previous sibling; null for the first sibling in the linked list
     */
    public final ModelTreeNode getPreviousSibling() {

        return getNode(PREVIOUS_SIBLING);
    }

    /**
     * Sets the previous sibling node.
     *
     * @param newPreviousSibling the new previous sibling; null for the first sibling in the linked list
     */
    public final void setPreviousSibling(final ModelTreeNode newPreviousSibling) {

        putNode(PREVIOUS_SIBLING, newPreviousSibling);
    }

    /**
     * Gets the next sibling node.
     *
     * @return the next sibling; null for the last sibling in the linked list
     */
    public final ModelTreeNode getNextSibling() {

        return getNode(NEXT_SIBLING);
    }

    /**
     * Sets the next sibling node.
     *
     * @param newNextSibling the new next sibling; null for the last sibling in the linked list
     */
    public final void setNextSibling(final ModelTreeNode newNextSibling) {

        putNode(NEXT_SIBLING, newNextSibling);
    }
}
