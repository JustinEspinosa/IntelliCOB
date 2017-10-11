package com.github.justinespinosa.intellicob.lexer;


import java.io.Reader;

public class SqlMpLexerAdapter extends MultiStreamFlexAdapter {
    public SqlMpLexerAdapter() {
        super(new SqlMpLexer((Reader) null));
    }
}
