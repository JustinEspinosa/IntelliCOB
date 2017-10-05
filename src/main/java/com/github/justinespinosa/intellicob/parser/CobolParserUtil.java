package com.github.justinespinosa.intellicob.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.INTEGER_;


public class CobolParserUtil extends GeneratedParserUtilBase {

    private static boolean isValidVariableLevel(String text) {
        if (text.length() != 2) {
            return false;
        }
        int asInteger = 0;
        try {
            asInteger = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }

        if (asInteger <= 88) {
            return true;
        }

        return false;
    }


    public static boolean variableLevel(PsiBuilder builder_, int level) {
        String text = builder_.getTokenText();

        if (isValidVariableLevel(text)) {
            int asInteger = Integer.parseInt(text);
            consumeToken(builder_, INTEGER_);
            return true;
        }
        return false;
    }

    public static boolean lowerVariableLevel(PsiBuilder builder_, int level) {
        String text = builder_.getTokenText();

        if (isValidVariableLevel(text)) {
            int asInteger = Integer.parseInt(text);
            consumeToken(builder_, INTEGER_);
            return true;
        }
        return false;
    }


}
