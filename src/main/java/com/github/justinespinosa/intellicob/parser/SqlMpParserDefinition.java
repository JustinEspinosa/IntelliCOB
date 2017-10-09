package com.github.justinespinosa.intellicob.parser;

import com.github.justinespinosa.intellicob.language.SqlMpLanguage;
import com.github.justinespinosa.intellicob.lexer.SqlMpLexerAdapter;
import com.github.justinespinosa.intellicob.psi.sqlmp.SqlMpFile;
import com.github.justinespinosa.intellicob.psi.sqlmp.SqlMpTypes;
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

import static com.intellij.psi.TokenType.WHITE_SPACE;


public class SqlMpParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create();
    public static final TokenSet STRINGS = TokenSet.create(SqlMpTypes.STRING_LITERAL);

    public static final IFileElementType FILE = new IFileElementType(SqlMpLanguage.INSTANCE);

    @Override
    @NotNull
    public Lexer createLexer(Project project) {
        return new SqlMpLexerAdapter();
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
        return new SqlMpParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new SqlMpFile(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return SqlMpTypes.Factory.createElement(node);
    }
}