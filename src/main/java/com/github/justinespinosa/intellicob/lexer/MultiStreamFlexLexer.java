package com.github.justinespinosa.intellicob.lexer;

import com.intellij.psi.tree.IElementType;

import java.io.IOException;

public interface MultiStreamFlexLexer {
    void yybegin(int state);

    int yystate();

    int getTokenStart();

    int getTokenEnd();

    int getBaseTokenEnd();

    CharSequence yytext();

    boolean yymoreStreams();

    IElementType advance() throws IOException;

    void reset(CharSequence buf, int start, int end, int initialState);
}
