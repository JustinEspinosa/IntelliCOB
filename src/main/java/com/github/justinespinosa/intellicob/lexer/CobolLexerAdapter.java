package com.github.justinespinosa.intellicob.lexer;


public class CobolLexerAdapter extends MultiStreamFlexAdapter {

    public CobolLexerAdapter() {
        super(new CobolLexer(null));
    }


}
