package com.github.justinespinosa.intellicob.lexer;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class CobolLexerAdapter extends FlexAdapter {
    public CobolLexerAdapter() {
        super(new CobolLexer((Reader) null));
    }
}
