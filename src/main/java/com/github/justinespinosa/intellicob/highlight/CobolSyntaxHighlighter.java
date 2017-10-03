package com.github.justinespinosa.intellicob.highlight;

import com.github.justinespinosa.intellicob.lexer.CobolLexerAdapter;
import com.github.justinespinosa.intellicob.psi.CobolTokenType;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.*;
import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.intellij.psi.TokenType.BAD_CHARACTER;

public class CobolSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey PREPROCESSOR_KEY = createTextAttributesKey("PREPROCESSOR", DefaultLanguageHighlighterColors.METADATA);
    public static final TextAttributesKey NUMBER_KEY = createTextAttributesKey("NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey STRING_KEY = createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey DOT_KEY = createTextAttributesKey("DOT", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEYWORD_KEY = createTextAttributesKey("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey COMMENT_KEY = createTextAttributesKey("COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BAD_CHARACTER_KEY = createTextAttributesKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD_KEY};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    private static final Map<IElementType, TextAttributesKey[]> COLORDATA = new HashMap<>();

    static {
        addColorData(DOT, DOT_KEY);
        addColorData(COMMENT, COMMENT_KEY);
        addColorData(COMMENT_INDICATOR, COMMENT_KEY);
        addColorData(STRING_LITERAL, STRING_KEY);
        addColorData(PICTURE_STRING, STRING_KEY);
        addColorData(INTEGER_, NUMBER_KEY);
        addColorData(NUMBER_, NUMBER_KEY);
        addColorData(COMMENT_ENTRY, COMMENT_KEY);
        addColorData(BAD_CHARACTER, BAD_CHARACTER_KEY);
        addColorData(PREPROCESSOR_INDICATOR, PREPROCESSOR_KEY);
        addColorData(PREPROCESSOR, PREPROCESSOR_KEY);
        addColorData(COPY_PREPROCESSOR, PREPROCESSOR_KEY);
        addColorData(REPLACE_PREPROCESSOR, PREPROCESSOR_KEY);
    }


    private static void addColorData(IElementType type, TextAttributesKey key) {
        COLORDATA.put(type, new TextAttributesKey[]{key});
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new CobolLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType instanceof CobolTokenType && ((CobolTokenType) tokenType).isKeyWord()) {
            return KEYWORD_KEYS;
        }

        return COLORDATA.getOrDefault(tokenType, EMPTY_KEYS);
    }
}
