package com.github.justinespinosa.intellicob.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

public class MultiStreamFlexAdapter extends LexerBase {
    private static final Logger LOGGER = Logger.getInstance(MultiStreamFlexAdapter.class);
    private final MultiStreamFlexLexer flexLexer;
    private IElementType tokenType;
    private CharSequence text;
    private int tokenStart;
    private int tokenEnd;
    private int bufferEnd;
    private int state;
    private int previousState;
    private boolean partialParsing = false;
    private int previousTokenStart;
    private IElementType previousTokenType;
    private boolean failed;

    public MultiStreamFlexAdapter(MultiStreamFlexLexer flex) {
        flexLexer = flex;
    }

    public MultiStreamFlexLexer getFlex() {
        return flexLexer;
    }

    @Override
    public void start(final CharSequence buffer, int startOffset, int endOffset, final int initialState) {
        text = buffer;
        previousTokenStart = tokenStart = tokenEnd = startOffset;
        bufferEnd = endOffset;
        flexLexer.reset(text, startOffset, endOffset, initialState);
        previousTokenType = tokenType = null;
        previousState = initialState;
        if (startOffset > 0 || endOffset < buffer.length() - 1) {
            partialParsing = true;
        } else {
            partialParsing = false;
        }
    }

    /*
     Hack against intellij's partial parsing detection of
     'lexer not progressing'
     */
    private int doGetState() {
      /*  if (partialParsing && previousState == state && previousTokenStart == tokenStart && tokenType == previousTokenType) {
            return state + 8192;
        }*/
        return state;
    }

    @Override
    public int getState() {
        locateToken();
        return doGetState();
    }

    @Override
    public IElementType getTokenType() {
        locateToken();
        return tokenType;
    }

    @Override
    public int getTokenStart() {
        locateToken();
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        locateToken();
        return tokenEnd;
    }

    @Override
    public void advance() {
        locateToken();
        tokenType = null;
    }

    @Override
    public CharSequence getBufferSequence() {
        return text;
    }

    @Override
    public int getBufferEnd() {
        return bufferEnd;
    }

    protected void locateToken() {
        if (tokenType != null) {
            return;
        }

        previousTokenStart = tokenStart;
        tokenStart = tokenEnd;
        if (failed) {
            return;
        }

        try {
            previousState = state;
            state = flexLexer.yystate();
            previousTokenType = tokenType;

            boolean isForeignBefore = flexLexer.yymoreStreams();
            tokenType = flexLexer.advance();
            boolean isForeignAfter = flexLexer.yymoreStreams();
            if (isForeignBefore && isForeignAfter) {
                tokenType = wrapTokenType(tokenType);
            }
            //getBaseTokenEnd
            tokenEnd = flexLexer.getBaseTokenEnd();
        } catch (ProcessCanceledException e) {
            throw e;
        } catch (Throwable e) {
            failed = true;
            tokenType = TokenType.BAD_CHARACTER;
            tokenEnd = bufferEnd;
            LOGGER.warn(flexLexer.getClass().getName(), e);
        }
    }

    private IElementType wrapTokenType(IElementType tokenType) {
        return new ForeignTokenType(tokenType, flexLexer.yytext());
    }

    @Override
    public String toString() {
        return "MultiStreamFlexAdapter for " + flexLexer.getClass().getName();
    }

}
