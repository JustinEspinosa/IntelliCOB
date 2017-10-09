package com.github.justinespinosa.intellicob.lexer;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class SqlMpLexerAdapter extends FlexAdapter {
    public SqlMpLexerAdapter() {
        super(new SqlMpLexer((Reader) null));
    }
}
