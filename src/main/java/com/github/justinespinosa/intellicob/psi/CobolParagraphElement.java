package com.github.justinespinosa.intellicob.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class CobolParagraphElement extends CobolPsiElementImpl {
    public CobolParagraphElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        ASTNode keyNode = getNode().findChildByType(CobolTypes.PARAGRAPH_NAME_);
        return keyNode.getText();
    }

    @Override
    public PsiElement setName(String newName) {
        ASTNode keyNode = getNode().findChildByType(CobolTypes.PARAGRAPH_NAME_);
        //TODO
        getNode().replaceChild(keyNode, keyNode);
        return this;
    }
}
