package com.github.justinespinosa.intellicob.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

import static com.github.justinespinosa.intellicob.psi.PsiUtil.createParagraphName;

public class CobolParagraphElement extends ASTWrapperPsiElement implements PsiNamedElement {
    public CobolParagraphElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        ASTNode keyNode = getNode().findChildByType(CobolTypes.PARAGRAPH_NAME_);
        if (keyNode == null) {
            return "";
        }
        return keyNode.getText();
    }

    @Override
    public PsiElement setName(String newName) {
        ASTNode keyNode = getNode().findChildByType(CobolTypes.PARAGRAPH_NAME_);
        ASTNode newKeyNode = createParagraphName(getProject(), newName);
        getNode().replaceChild(keyNode, newKeyNode);
        return this;
    }
}
