package dev.mathops.commons.parser.json;

import dev.mathops.commons.CoreConstants;
import dev.mathops.commons.builder.HtmlBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * A parsed JSON object, which is a collection of named properties, each of which having a value that is one of the
 * following.
 *
 * <ul>
 * <li>a {@code JSONObject}
 * <li>an array of values
 * <li>a {@code String}
 * <li>a {@code Double}
 * <li>a {@code Boolean}
 * <li>{@code null}
 * </ul>
 */
public final class JSONObject {

    /** Map from property name to value. */
    private final Map<String, Object> properties;

    /**
     * Constructs a new {@code JSONObject}.
     */
    public JSONObject() {

        this.properties = new HashMap<>(10);
    }

    /**
     * Sets the value of a property.
     *
     * @param name  the property name
     * @param value the value
     */
    public void setProperty(final String name, final Object value) {

        this.properties.put(name, value);
    }

    /**
     * Gets the value of a property.
     *
     * @param name the property name
     * @return the value
     */
    public Object getProperty(final String name) {

        return this.properties.get(name);
    }

    /**
     * Gets the string value of a property.
     *
     * @param name the property name
     * @return the value, if the value was present and is a string; null if not
     */
    public String getStringProperty(final String name) {

        final Object value = this.properties.get(name);

        return value instanceof String ? (String) value : null;
    }

    /**
     * Gets the number value of a property.
     *
     * @param name the property name
     * @return the value, if the value was present and is a double; null if not
     */
    public Double getNumberProperty(final String name) {

        final Object value = this.properties.get(name);

        return value instanceof Double ? (Double) value : null;
    }

    /**
     * Gets the Boolean value of a property.
     *
     * @param name the property name
     * @return the value, if the value was present and is a Boolean; null if not
     */
    public Boolean getBooleanProperty(final String name) {

        final Object value = this.properties.get(name);

        return value instanceof Boolean ? (Boolean) value : null;
    }

    /**
     * Escapes a string, so it can be emitted in a JSON formatted file.
     *
     * @param source the source string to escape
     * @param target the {@code HtmlBuilder} to which to append the escaped string (surrounding quotes are not emitted)
     */
    private static void escapeJSONString(final String source, final HtmlBuilder target) {

        for (final char ch : source.toCharArray()) {

            if ((int) ch == (int) '\b') {
                target.add("\\b");
            } else if ((int) ch == (int) '\f') {
                target.add("\\f");
            } else if ((int) ch == (int) '\n') {
                target.add("\\n");
            } else if ((int) ch == (int) '\r') {
                target.add("\\r");
            } else if ((int) ch == (int) '\t') {
                target.add("\\t");
            } else if ((int) ch == (int) '"') {
                target.add("\\\"");
            } else if ((int) ch == (int) '\\') {
                target.add("\\\\");
            } else {
                target.add(ch);
            }
        }
    }

    /**
     * Generates the JSON representation of the object.
     *
     * @return the JSON representation
     */
    public String toJSONCompact() {

        final HtmlBuilder htm = new HtmlBuilder(100);

        htm.add('{');
        boolean comma = false;
        for (final Map.Entry<String, Object> entry : this.properties.entrySet()) {
            if (comma) {
                htm.add(CoreConstants.COMMA_CHAR);
            }
            htm.add('"');
            final String key = entry.getKey();
            escapeJSONString(key, htm);
            htm.add('"').add(':');
            final Object value = entry.getValue();
            emitValueCompact(value, htm);
            comma = true;
        }
        htm.add('}');

        return htm.toString();
    }

    /**
     * Emits a single JSON object.
     *
     * @param value the value to emit
     * @param htm   the {@code HtmlBuilder} to which to emit
     */
    private static void emitValueCompact(final Object value, final HtmlBuilder htm) {

        switch (value) {
            case final JSONObject obj -> {
                final String json = obj.toJSONCompact();
                htm.add(json);
            }
            case final Object[] array -> {
                htm.add('[');
                boolean comma = false;
                for (final Object o : array) {
                    if (comma) {
                        htm.add(CoreConstants.COMMA_CHAR);
                    }
                    emitValueCompact(o, htm);
                    comma = true;
                }
                htm.add(']');
            }
            case final String str -> {
                htm.add('"');
                escapeJSONString(str, htm);
                htm.add('"');
            }
            case final Double ignored -> {
                final String valueStr = value.toString();
                htm.add(valueStr);
            }
            case final Boolean ignored -> {
                final String valueStr = value.toString();
                htm.add(valueStr);
            }
            case null, default -> htm.add("null");
        }
    }

    /**
     * Generates the JSON representation of the object.
     *
     * @param indent the indentation level (the leading left brace is not indented - but all subsequent lines are
     *               indented "indent + 1" steps)
     * @return the JSON representation
     */
    public String toJSONFriendly(final int indent) {

        final HtmlBuilder htm = new HtmlBuilder(100);

        htm.add("{ ");
        boolean comma = false;
        for (final Map.Entry<String, Object> entry : this.properties.entrySet()) {
            if (comma) {
                htm.addln(CoreConstants.COMMA_CHAR);
                htm.indent(indent + 2);
            }
            htm.add('"');
            final String key = entry.getKey();
            escapeJSONString(key, htm);
            htm.add("\": ");
            final Object value = entry.getValue();
            emitValueFriendly(value, htm, indent + 2);
            comma = true;
        }
        htm.add('}');

        return htm.toString();
    }

    /**
     * Emits a single JSON object.
     *
     * @param value  the value to emit
     * @param htm    the {@code HtmlBuilder} to which to emit
     * @param indent the indentation level (the value itself is not indented - but array contents and object contents
     *               are indented "indent + 1" steps)
     */
    private static void emitValueFriendly(final Object value, final HtmlBuilder htm, final int indent) {

        switch (value) {
            case final JSONObject obj -> {
                final String friendly = obj.toJSONFriendly(indent);
                htm.add(friendly);
            }
            case final Object[] array -> {
                htm.addln('[');
                htm.indent(indent + 2);

                boolean comma = false;
                for (final Object o : array) {
                    if (comma) {
                        htm.addln(CoreConstants.COMMA_CHAR);
                        htm.indent(indent + 2);
                    }
                    emitValueFriendly(o, htm, indent + 2);
                    comma = true;
                }
                htm.add(']');
            }
            case final String str -> {
                htm.add('"');
                escapeJSONString(str, htm);
                htm.add('"');
            }
            case final Double ignored -> {
                final String valueStr = value.toString();
                htm.add(valueStr);
            }
            case final Boolean ignored -> {
                final String valueStr = value.toString();
                htm.add(valueStr);
            }
            case null, default -> htm.add("null");
        }
    }
}
