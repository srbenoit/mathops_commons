package dev.mathops.commons.parser.xml;

import dev.mathops.commons.log.Log;
import dev.mathops.commons.parser.FilePosition;
import dev.mathops.commons.parser.ParsingException;

import java.util.List;

/**
 * A tokenizer for XML content that supports a limited subset of the XML 1.0 grammar. State is stored in the
 * {@code XmlContent} object being parsed, allowing a single parser to be used by multiple threads concurrently.
 */
enum XmlTokenizer {
    ;

    /** The minimum length of a CDATA block &lt;![CDATA[]]&gt;. */
    private static final int MIN_CDATA_LEN = 12;

    /** The start of a CDATA block. */
    private static final String CDATA_START = "<![CDATA[";

    /** The minimum length of a comment &lt;!----&gt;. */
    private static final int MIN_COMMENT_LEN = 6;

    /**
     * Breaks the content into tokens. Every character in the source will belong to one and only one token, which will
     * not overlap. This step does not validate the contents of tokens or process complex tokens (such as element tags
     * that may have attributes). In fact, references within tags like &lt; will not be processed at this step.
     *
     * @param theContent the content to parse
     * @return the list of tokens
     * @throws ParsingException if the content could not be parsed
     */
    static List<IXmlToken> tokenize(final XmlContent theContent) throws ParsingException {

        final TokenizeState state = new TokenizeState(theContent);
        final int len = theContent.length();

        final FilePosition filePos = new FilePosition();

        while (filePos.byteOffset < len) {
            final char chr = theContent.get(filePos.byteOffset);

            if (!XmlChars.isChar((int) chr)) {
                final String message = Res.get(Res.BAD_CHAR);
                throw new ParsingException(filePos.byteOffset, filePos.byteOffset + 1, message);
            }

            processChar(chr, filePos, state);
            ++filePos.byteOffset;
            if ((int) chr == (int) '\n') {
                ++filePos.lineNumber;
                filePos.column = 0;
            }
            ++filePos.column;
        }

        final FilePosition start = state.getStart();
        if (start.byteOffset < len) {
            final EXmlParseState parseState = state.getState();

            if (parseState == EXmlParseState.CHARS) {
                state.addToken(new TokChars(theContent, start.byteOffset, len, start.lineNumber, start.column));
            } else if (parseState == EXmlParseState.WHITESPACE) {
                state.addToken(new TokWhitespace(theContent, start.byteOffset, len, start.lineNumber, start.column));
            } else {
                final List<IXmlToken> tokens = state.getTokens();
                if (!tokens.isEmpty()) {
                    final int count = tokens.size();
                    final int debugStart = Math.max(0, count - 5);
                    for (int i = debugStart; i < count; ++i) {
                        final IXmlToken tok = tokens.get(i);
                        Log.info("Recent token was " + tok);
                    }
                }
                final String message = Res.get(Res.BAD_EOF);
                throw new ParsingException(start.byteOffset, len, message);
            }
        }

        return state.getTokens();
    }

    /**
     * Processes a single character of the XML.
     *
     * @param chr     the character
     * @param filePos the position at which the character was found in the XML
     * @param state   the tokenizing state
     * @throws ParsingException if the content could not be parsed
     */

    private static void processChar(final char chr, final FilePosition filePos,
                                    final TokenizeState state) throws ParsingException {

        final EXmlParseState parseState = state.getState();

        if (parseState == EXmlParseState.CHARS) {
            tokenizeChars(chr, filePos, state);
        } else if (parseState == EXmlParseState.WHITESPACE) {
            tokenizeWhitespace(chr, filePos, state);
        } else if (parseState == EXmlParseState.REFERENCE) {
            tokenizeReference(chr, filePos, state);
        } else if (parseState == EXmlParseState.COMMENT) {
            tokenizeComment(chr, filePos, state);
        } else if (parseState == EXmlParseState.TAG) {
            tokenizeTag(chr, filePos, state);
        } else if (parseState == EXmlParseState.BANGTAG) {
            tokenizeBangTag(chr, filePos, state);
        } else if (parseState == EXmlParseState.XMLDECL) {
            tokenizeXmlDecl(chr, filePos, state);
        } else if (parseState == EXmlParseState.DOCTYPE) {
            tokenizeDoctype(chr, filePos, state);
        } else {
            tokenizeCData(chr, filePos, state);
        }
    }

    /**
     * Performs one step of tokenization while in the CHARS parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     */
    private static void tokenizeChars(final char chr, final FilePosition filePos, final TokenizeState state) {

        if ((int) chr == (int) '<' || (int) chr == (int) '&' || XmlChars.isWhitespace((int) chr)) {
            final FilePosition start = state.getStart();

            if (state.getStart().byteOffset < filePos.byteOffset) {
                final XmlContent content = state.getContent();
                state.addToken(new TokChars(content, start.byteOffset, filePos.byteOffset, start.lineNumber,
                        start.column));
            }

            start.copyFrom(filePos);

            if ((int) chr == (int) '<') {
                state.setState(EXmlParseState.TAG);
            } else if ((int) chr == (int) '&') {
                state.setState(EXmlParseState.REFERENCE);
            } else {
                state.setState(EXmlParseState.WHITESPACE);
            }
        }
    }

    /**
     * Performs one step of tokenization while in the WHITESPACE parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     */
    private static void tokenizeWhitespace(final char chr, final FilePosition filePos, final TokenizeState state) {

        if (!XmlChars.isWhitespace((int) chr)) {
            final FilePosition start = state.getStart();

            if (state.getStart().byteOffset < filePos.byteOffset) {
                final XmlContent content = state.getContent();
                state.addToken(new TokWhitespace(content, start.byteOffset, filePos.byteOffset,
                        start.lineNumber, start.column));
            }

            start.copyFrom(filePos);

            if ((int) chr == (int) '<') {
                state.setState(EXmlParseState.TAG);
            } else if ((int) chr == (int) '&') {
                state.setState(EXmlParseState.REFERENCE);
            } else {
                state.setState(EXmlParseState.CHARS);
            }
        }
    }

    /**
     * Performs one step of tokenization while in the REFERENCE parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     */
    private static void tokenizeReference(final char chr, final FilePosition filePos, final TokenizeState state) {

        if ((int) chr == (int) ';') {
            final FilePosition start = state.getStart();

            final XmlContent content = state.getContent();
            state.addToken(new TokReference(content, start.byteOffset, filePos.byteOffset + 1, start.lineNumber,
                    start.column));

            start.copyFrom(filePos);
            ++start.byteOffset;

            state.setState(EXmlParseState.CHARS);
        }
    }

    /**
     * Performs one step of tokenization while in the COMMENT parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     */
    private static void tokenizeComment(final char chr, final FilePosition filePos, final TokenizeState state) {

        final FilePosition start = state.getStart();

        final XmlContent content = state.getContent();
        if (filePos.byteOffset >= (start.byteOffset + MIN_COMMENT_LEN - 1) && (int) chr == (int) '>'
            && (int) content.get(filePos.byteOffset - 1) == (int) '-'
            && (int) content.get(filePos.byteOffset - 2) == (int) '-') {

            state.addToken(new TokComment(content, start.byteOffset, filePos.byteOffset + 1, start.lineNumber,
                    start.column));

            start.copyFrom(filePos);
            ++start.byteOffset;

            state.setState(EXmlParseState.CHARS);
        }
    }

    /**
     * Performs one step of tokenization while in the TAG parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     */
    private static void tokenizeTag(final char chr, final FilePosition filePos, final TokenizeState state) {

        final FilePosition start = state.getStart();

        if ((int) chr == (int) '?' && filePos.byteOffset == (start.byteOffset + 1)) {
            state.setState(EXmlParseState.XMLDECL);
        } else if ((int) chr == (int) '!' && filePos.byteOffset == (start.byteOffset + 1)) {
            state.setState(EXmlParseState.BANGTAG);
        } else if ((int) chr == (int) '>') {
            final XmlContent content = state.getContent();

            if ((int) content.get(start.byteOffset + 1) == (int) '/') {
                state.addToken(new TokETag(content, start.byteOffset, filePos.byteOffset + 1,
                        start.lineNumber, start.column));
            } else if ((int) content.get(filePos.byteOffset - 1) == (int) '/') {
                state.addToken(new TokEmptyElement(content, start.byteOffset, filePos.byteOffset + 1,
                        start.lineNumber, start.column));
            } else {
                state.addToken(new TokSTag(content, start.byteOffset, filePos.byteOffset + 1, start.lineNumber,
                        start.column));
            }

            start.copyFrom(filePos);
            ++start.byteOffset;

            state.setState(EXmlParseState.CHARS);
        }
    }

    /**
     * Performs one step of tokenization while in the BANGTAG parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     * @throws ParsingException if the tag is unsupported
     */
    private static void tokenizeBangTag(final char chr, final FilePosition filePos,
                                        final TokenizeState state) throws ParsingException {

        if ((int) chr == (int) 'D') {
            state.setState(EXmlParseState.DOCTYPE);
        } else if ((int) chr == (int) '-') {
            state.setState(EXmlParseState.COMMENT);
        } else if ((int) chr == (int) '[') {
            state.setState(EXmlParseState.CDATA);
        } else {
            final FilePosition start = state.getStart();
            final String message = Res.get(Res.UNSUP_TAG);
            throw new ParsingException(start.byteOffset, filePos.byteOffset + 1, message);
        }
    }

    /**
     * Performs one step of tokenization while in the XMLDECL parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     */
    private static void tokenizeXmlDecl(final char chr, final FilePosition filePos, final TokenizeState state) {

        if ((int) chr == (int) '>') {
            final FilePosition start = state.getStart();

            final XmlContent content = state.getContent();
            state.addToken(new TokXmlDecl(content, start.byteOffset, filePos.byteOffset + 1, start.lineNumber,
                    start.column));

            start.copyFrom(filePos);
            ++start.byteOffset;

            state.setState(EXmlParseState.CHARS);
        }
    }

    /**
     * Performs one step of tokenization while in the DOCTYPE parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     */
    private static void tokenizeDoctype(final char chr, final FilePosition filePos, final TokenizeState state) {

        if ((int) chr == (int) '<') {
            state.incrementNesting();
        } else if ((int) chr == (int) '>') {
            if (state.getNesting() == 0) {
                final FilePosition start = state.getStart();

                final XmlContent content = state.getContent();
                state.addToken(new TokDoctype(content, start.byteOffset, filePos.byteOffset + 1, start.lineNumber,
                        start.column));

                start.copyFrom(filePos);
                ++start.byteOffset;

                state.setState(EXmlParseState.CHARS);
            } else {
                state.decrementNesting();
            }
        }
    }

    /**
     * Performs one step of tokenization while in the CDATA parse state.
     *
     * @param chr     the character being parsed
     * @param filePos the file position of the character in the XML content
     * @param state   the current parse state
     * @throws ParsingException if the start of the CDATA section is not valid
     */
    private static void tokenizeCData(final char chr, final FilePosition filePos,
                                      final TokenizeState state) throws ParsingException {

        // We enter this state after scanning "<![", start is set to the position of the "<"
        final FilePosition start = state.getStart();
        final int offset = filePos.byteOffset - start.byteOffset;

        if (offset < CDATA_START.length()) {
            if ((int) chr != (int) CDATA_START.charAt(offset)) {
                // Bad CDATA token start, such as <![foo
                final String message = Res.get(Res.BAD_CDATA);
                throw new ParsingException(start.byteOffset, filePos.byteOffset + 1, message);
            }
        } else {
            final XmlContent content = state.getContent();

            if (filePos.byteOffset >= (start.byteOffset + MIN_CDATA_LEN - 1) && (int) chr == (int) '>'
                && (int) content.get(filePos.byteOffset - 1) == (int) ']'
                && (int) content.get(filePos.byteOffset - 2) == (int) ']') {

                state.addToken(new TokCData(content, start.byteOffset, filePos.byteOffset + 1, start.lineNumber,
                        start.column));

                start.copyFrom(filePos);
                ++start.byteOffset;

                state.setState(EXmlParseState.CHARS);
            }
        }
    }
}
