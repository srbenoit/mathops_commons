package dev.mathops.commons.parser.xml;

import dev.mathops.commons.builder.SimpleBuilder;
import dev.mathops.commons.parser.ICharSpan;

/**
 * An error message along with the span object with which the error is associated.
 */
public final class XmlContentError {

    /** The span. */
    public final ICharSpan span;

    /** The error message. */
    public final String msg;

    /**
     * Constructs a new {@code XmlContentError}.
     *
     * @param theSpan the span
     * @param theMsg  the error message
     */
    public XmlContentError(final ICharSpan theSpan, final String theMsg) {

        this.span = theSpan;
        this.msg = theMsg;
    }

    /**
     * Generates a string representation of the error.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final int lineNumber = this.span.getLineNumber();
        final int column = this.span.getColumn();
        final int start = this.span.getStart();
        final int end = this.span.getEnd();

        final String lineNumberStr = Integer.toString(lineNumber);
        final String columnStr = Integer.toString(column);
        final String startStr = Integer.toString(start);
        final String endStr = Integer.toString(end);

        return SimpleBuilder.concat(this.msg, " at line ", lineNumberStr, ", column ", columnStr, ", [", startStr, ":",
                endStr, "]");
    }
}
