package dev.mathops.commons.model;

/**
 * The interface for classes that can translate between an instance of the class and a {@code String}.
 *
 * @param <T> the type of object this codec converts
 */
public interface Codec<T> {

    /**
     * Gets the type of object this codec converts.
     *
     * @return the type
     */
    Class<T> getType();

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * @param str the string to parse
     * @return the parsed instance
     * @throws IllegalArgumentException if {@code str} is {@code null}
     * @throws StringParseException if the string cannot be parsed as an instance of the class
     */
    T parse(String str) throws IllegalArgumentException, StringParseException;

    /**
     * Parses a string into an instance of the type class associated with this object.
     *
     * @param obj the object to stringify
     * @return the string representation
     * @throws IllegalArgumentException if {@code obj} is {@code null}
     */
    String stringify(T obj) throws IllegalArgumentException;
}
