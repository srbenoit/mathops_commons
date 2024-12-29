package dev.mathops.commons.model.codec;

import dev.mathops.commons.model.Codec;
import dev.mathops.commons.model.ModelTreeNode;
import dev.mathops.commons.model.StringParseException;

/**
 * A codec for general {@code ModelTreeNode} objects.  Since these objects do not have a {@code String} representation,
 * any attempt to generate such a representation will throw an exception.
 */
public enum ModelTreeNodeCodec implements Codec<ModelTreeNode> {

    /** The single instance. */
    INST;

    /**
     * Gets the type of object this codec converts.
     *
     * @return the type
     */
    public Class<ModelTreeNode> getType() {

        return ModelTreeNode.class;
    }

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * @param str the string to parse
     * @return the parsed instance
     * @throws IllegalArgumentException if {@code str} is {@code null} or cannot be parsed as an instance of the class
     */
    public ModelTreeNode parse(final String str) throws IllegalArgumentException, StringParseException {

        if (str == null) {
            final String msg = Res.get(Res.NULL_STRING);
            throw new IllegalArgumentException(msg);
        }

        throw new StringParseException("Model tree nodes do not have string representations.");
    }

    /**
     * Generates a String representation of the object.
     *
     * @param obj the object to stringify
     * @return nothing
     * @throws IllegalArgumentException always
     */
    public String stringify(final ModelTreeNode obj) throws IllegalArgumentException {

        if (obj == null) {
            final String msg = Res.get(Res.NULL_OBJECT);
            throw new IllegalArgumentException(msg);
        }

        throw new IllegalArgumentException("Model tree nodes do not have string representations.");
    }
}