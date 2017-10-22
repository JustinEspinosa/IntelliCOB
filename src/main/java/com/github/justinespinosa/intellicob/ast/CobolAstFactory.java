package com.github.justinespinosa.intellicob.ast;

import com.github.justinespinosa.intellicob.psi.node.ForeignComposite;
import com.github.justinespinosa.intellicob.psi.node.ForeignLeaf;
import com.intellij.core.CoreASTFactory;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.TokenWrapper;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CobolAstFactory extends CoreASTFactory {

    @Nullable
    @Override
    public CompositeElement createComposite(IElementType type) {
        if (type instanceof IFileElementType) {
            return new FileElement(type, null);
        }

        if (type instanceof TokenWrapper) {
            return new ForeignComposite((TokenWrapper) type);
        }

        return new CompositeElement(type);
    }

    @Nullable
    @Override
    public LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
        final Language lang = type.getLanguage();
        final ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(lang);
        if (parserDefinition != null) {
            if (parserDefinition.getCommentTokens().contains(type)) {
                return createComment(type, text);
            }
        }

        if (type instanceof TokenWrapper) {
            return new ForeignLeaf((TokenWrapper) type, text);
        }

        return new LeafPsiElement(type, text);

    }
}
