package com.github.justinespinosa.intellicob.psi.sqlmp;

import com.intellij.psi.tree.IElementType;

import java.util.Map;
import java.util.TreeMap;


public class SqlMpTokenTypeFactory {

    private static final Map<String, IElementType> KNOWN_TOKENS = new TreeMap<>();

    public static IElementType createTokenType(String text) {
        IElementType tokenType = new SqlMpTokenType(text);
        KNOWN_TOKENS.put(text, tokenType);
        return tokenType;
    }

    public static IElementType getToken(CharSequence text) {
        return KNOWN_TOKENS.getOrDefault(text.toString().toUpperCase(), SqlMpTypes.WORD);
    }

    public static IElementType getTokenHost(CharSequence text) {
        return KNOWN_TOKENS.getOrDefault(text.toString().toUpperCase(), SqlMpTypes.HOST_WORD);
    }


}
