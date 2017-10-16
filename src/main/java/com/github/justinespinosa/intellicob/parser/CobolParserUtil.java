package com.github.justinespinosa.intellicob.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.util.Key;

import java.util.Stack;
import java.util.regex.Pattern;


public class CobolParserUtil extends MultiStreamParserUtilBase {

    private static final Pattern VARIABLE_LEVEL = Pattern.compile("[0-4][0-9]");
    private static final Key<Stack<Integer>> PREVIOUS_VARIABLE_LEVEL = new Key<>("com.github.justinespinosa.intellicob.PREVIOUS_VARIABLE_LEVEL");


    private static boolean isValidChildVariableLevel(String text) {
        if (text.length() != 2) {
            return false;
        }
        return VARIABLE_LEVEL.matcher(text).matches();
    }

    private static boolean handleVariableLevel(PsiBuilder builder_, String level) {
        String text = builder_.getTokenText();

        if (level.equals(text)) {
            consumeToken(builder_, builder_.getTokenType());
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
            consumeToken(builder_, builder_.getTokenType());
            return true;

        }
        return false;
    }

    public static boolean childVariableLevel(PsiBuilder builder_, int level) {
        String text = builder_.getTokenText();

        if (isValidChildVariableLevel(text)) {
            int itemLevel = Integer.parseInt(text);
            if (handleLevelStack(builder_, itemLevel)) {
                consumeToken(builder_, builder_.getTokenType());
                return true;
            }
        }
        return false;
    }


}
