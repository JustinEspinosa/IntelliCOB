package com.github.justinespinosa.intellicob.lexer;

import com.github.justinespinosa.intellicob.language.CobolLanguage;
import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageASTFactory;
import com.intellij.lang.TokenWrapper;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILeafElementType;
import org.jetbrains.annotations.NotNull;

public class ForeignTokenType extends TokenWrapper implements ILeafElementType {
    public ForeignTokenType(IElementType tokenType, CharSequence value) {
        super(tokenType, value);
    }

    private static ASTFactory factory() {
        return LanguageASTFactory.INSTANCE.forLanguage(CobolLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public ASTNode createLeafNode(@NotNull CharSequence leafText) {
        return factory().createLeaf(this, leafText);
    }

}
