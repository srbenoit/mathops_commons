package dev.mathops.commons.parser.xml;

import dev.mathops.commons.builder.HtmlBuilder;
import dev.mathops.commons.parser.ParsingException;

/**
 * A token that represents the end tag of a nonempty element, beginning with &lt;/ and ending with &gt;, with no &lt; or
 * &gt; characters between.
 */
final class TokETag extends AbstractXmlToken {

    /** The tag name. */
    private String name = null;

    /**
     * Constructs a new {@code TokETag}.
     *
     * @param theContent    the content this token belongs to
     * @param theStart      the index of the '<' character
     * @param theEnd        the index after '>' character
     * @param theLineNumber the line number of the start of the span
     * @param theColumn     the column of the start of the span
     */
    TokETag(final XmlContent theContent, final int theStart, final int theEnd,
            final int theLineNumber, final int theColumn) {

        super(theContent, theStart, theEnd, theLineNumber, theColumn);
    }

    /**
     * Gets the tag name.
     *
     * @return the name
     */
    public String getName() {

        return this.name;
    }

    /**
     * Generates the string representation of the TokETag token.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final HtmlBuilder builder = printContent("ETag: ");

        printValue(this.name, "Name", builder);

        return builder.toString();
    }

    /**
     * Validates the contents of the TokETag token, which may store internal structure that can be used when the token
     * in assembled into a node.
     *
     * @throws ParsingException if the contents are invalid
     */
    @Override
    public void validate() throws ParsingException {

        final XmlContent content = getContent();
        final int start = getStart();

        int pos = start + 2;
        final char chr = content.get(pos);
        boolean valid = XmlChars.isNameStartChar((int) chr);
        ++pos;

        // Validate and store the name
        final int end = getEnd();
        while (valid && pos < (end - 1)) {

            final char c = content.get(pos);
            if (XmlChars.isWhitespace((int) c)) {
                break;
            }

            valid = XmlChars.isNameChar((int) c);
            ++pos;
        }

        final String tagName = content.substring(start + 2, pos);

        if (!valid) {
            final String chars = content.substring(start, end);
            final String message = Res.fmt(Res.BAD_ENDTAG, chars, tagName);
            throw new ParsingException(start, pos, message);
        }

        this.name = tagName;
    }

    /**
     * Tests whether the TokETag token generates one or more nodes (or part of a node) in the XML node structure.
     *
     * @return {@code true} if the token contributes to the XML node structure; {@code false} if not
     */
    @Override
    public boolean hasNodeStructure() {

        return true;
    }

    /**
     * Tests whether the TokETag token represents an XML element.
     *
     * @return {@code false} if the token represents an element; {@code true} if not
     */
    @Override
    public boolean isNonElement() {

        return false;
    }
}
