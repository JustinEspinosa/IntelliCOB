package com.github.justinespinosa.intellicob.parser;

import com.github.justinespinosa.intellicob.lexer.MultiStreamFlexAdapter;
import com.github.justinespinosa.intellicob.lexer.MultiStreamFlexLexer;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;
import java.util.regex.Pattern;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.INTEGER_;


public class CobolParserUtil extends GeneratedParserUtilBase {

    private static final Pattern VARIABLE_LEVEL = Pattern.compile("[0-4][0-9]");
    private static final Key<Stack<Integer>> PREVIOUS_VARIABLE_LEVEL = new Key<>("com.github.justinespinosa.intellicob.PREVIOUS_VARIABLE_LEVEL");

    public static PsiBuilder adapt_builder_(IElementType root, PsiBuilder builder, PsiParser parser, TokenSet[] extendsSets) {
        ErrorState state = new ErrorState();
        ErrorState.initState(state, builder, root, extendsSets);
        return new MultiStreamBuilder(builder, state, parser);
    }

    private static boolean isValidChildVariableLevel(String text) {
        if (text.length() != 2) {
            return false;
        }
        return VARIABLE_LEVEL.matcher(text).matches();
    }

    private static boolean handleVariableLevel(PsiBuilder builder_, String level) {
        String text = builder_.getTokenText();

        if (level.equals(text)) {
            consumeToken(builder_, INTEGER_);
            return true;
        }
        return false;
    }

    private static boolean handleLevelStack(PsiBuilder builder_, int itemLevel) {
        Stack<Integer> previousLevels = builder_.getUserData(PREVIOUS_VARIABLE_LEVEL);
        if (previousLevels != null) {
            int previousLevel = previousLevels.peek();
            if (previousLevel >= itemLevel) {
                previousLevels.pop();
                return false;
            }
            if (previousLevel < itemLevel) {
                previousLevels.push(itemLevel);
                return true;
            }
        }
        return false;
    }

    public static boolean variableLevel77(PsiBuilder builder_, int level) {
        return handleVariableLevel(builder_, "77");
    }

    public static boolean variableLevel88(PsiBuilder builder_, int level) {
        return handleVariableLevel(builder_, "88");
    }

    public static boolean variableLevel66(PsiBuilder builder_, int level) {
        return handleVariableLevel(builder_, "66");
    }

    public static boolean rootVariableLevel(PsiBuilder builder_, int level) {
        String text = builder_.getTokenText();

        if ("01".equals(text)) {
            Stack<Integer> stack = new Stack<>();
            stack.push(1);
            builder_.putUserData(PREVIOUS_VARIABLE_LEVEL, stack);
            consumeToken(builder_, INTEGER_);
            return true;

        }
        return false;
    }

    public static boolean childVariableLevel(PsiBuilder builder_, int level) {
        String text = builder_.getTokenText();

        if (isValidChildVariableLevel(text)) {
            int itemLevel = Integer.parseInt(text);
            if (handleLevelStack(builder_, itemLevel)) {
                consumeToken(builder_, INTEGER_);
                return true;
            }
        }
        return false;
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
        public String getTokenText() {
            if (getFlex().yymoreStreams()) {
                return getFlex().yytext().toString();
            }
            return super.getTokenText();
        }
    }


}
