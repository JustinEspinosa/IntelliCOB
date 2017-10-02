package com.github.justinespinosa.intellicob.parser;

import com.github.justinespinosa.intellicob.language.CobolLanguage;
import com.github.justinespinosa.intellicob.lexer.CobolLexerAdapter;
import com.github.justinespinosa.intellicob.psi.CobolFile;
import com.github.justinespinosa.intellicob.psi.CobolTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.*;
import static com.intellij.psi.TokenType.WHITE_SPACE;


public class CobolParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE, CODE_INDICATOR, COMMENT_INDICATOR,
            PAGE_INDICATOR, PREPROCESSOR_INDICATOR);
    public static final TokenSet COMMENTS = TokenSet.create(COMMENT, PREPROCESSOR, COPY_PREPROCESSOR,
            REPLACE_PREPROCESSOR, EMBEDDED_SQL);
    public static final TokenSet STRINGS = TokenSet.create(STRING);

    public static final IFileElementType FILE = new IFileElementType(CobolLanguage.INSTANCE);

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new CobolLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRINGS;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new CobolParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new CobolFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return CobolTypes.Factory.createElement(node);
    }
}