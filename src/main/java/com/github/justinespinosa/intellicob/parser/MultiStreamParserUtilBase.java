package com.github.justinespinosa.intellicob.parser;

import com.github.justinespinosa.intellicob.lexer.MultiStreamFlexAdapter;
import com.github.justinespinosa.intellicob.lexer.MultiStreamFlexLexer;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nullable;

import static com.github.justinespinosa.intellicob.psi.PsiUtil.getElementType;
import static com.github.justinespinosa.intellicob.psi.PsiUtil.isSkipped;

public class MultiStreamParserUtilBase extends GeneratedParserUtilBase {

    public static PsiBuilder adapt_builder_(IElementType root, PsiBuilder builder, PsiParser parser, TokenSet[] extendsSets) {
        GeneratedParserUtilBase.ErrorState state = new GeneratedParserUtilBase.ErrorState();
        GeneratedParserUtilBase.ErrorState.initState(state, builder, root, extendsSets);
        return new MultiStreamBuilder(builder, state, parser);
    }

    public static class MultiStreamBuilder extends Builder {

        public MultiStreamBuilder(PsiBuilder builder, ErrorState state_, PsiParser parser_) {
            super(builder, state_, parser_);
        }

        public MultiStreamFlexLexer getFlex() {
            return ((MultiStreamFlexAdapter) getLexer()).getFlex();
        }

        @Nullable
        @Override
        public IElementType getTokenType() {
            eof();
            return getElementType(super.getTokenType());
        }

        @Override
        public boolean eof() {
            if (super.eof()) {
                return true;
            }

            while (isSkipped(super.getTokenType())) {
                super.advanceLexer();
            }

            return super.eof();
        }

        @Override
        public void advanceLexer() {
            eof();
            super.advanceLexer();
        }
    }

}
