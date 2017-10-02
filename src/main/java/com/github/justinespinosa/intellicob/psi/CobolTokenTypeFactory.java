package com.github.justinespinosa.intellicob.psi;

import com.intellij.psi.tree.IElementType;

import java.util.*;


public class CobolTokenTypeFactory {

    private static final Map<String, IElementType> KNOWN_TOKENS = new TreeMap<>();

    private static final Set<String> NOT_KEY_WORD = new TreeSet<>(Arrays.asList(
            "COBOLWORD", "COMMENT", "DOT", "COMMENT_ENTRY", "COMPUTER_NAME", "STRING", "INTEGER_",
            "OBJECT_COMPUTER_NAME", "PICTURE_STRING", "PSEUDOTEXT", "NUMBER_", "CODE_INDICATOR", "COMMENT_INDICATOR",
            "PAGE_INDICATOR", "PREPROCESSOR_INDICATOR", "PREPROCESSOR", "COPY_PREPROCESSOR", "REPLACE_PREPROCESSOR",
            "GUARDIAN_FILE"
    ));


    public static IElementType createTokenType(String text) {
        String tokenText = createTokenText(text);
        IElementType tokenType = new CobolTokenType(text, !NOT_KEY_WORD.contains(text));
        KNOWN_TOKENS.put(tokenText, tokenType);
        return tokenType;
    }

    public static IElementType getToken(CharSequence text) {
        return KNOWN_TOKENS.getOrDefault(text.toString().toUpperCase(), CobolTypes.COBOLWORD);
    }

    private static String createTokenText(String text) {
        return text.toUpperCase().replaceAll("_", "-");
    }

}
