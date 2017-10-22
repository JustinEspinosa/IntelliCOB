package com.github.justinespinosa.intellicob.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.github.justinespinosa.intellicob.psi.PsiUtil.createDataItemName;

public class CobolDataItemElement extends ASTWrapperPsiElement implements PsiNamedElement {
    public CobolDataItemElement(@NotNull ASTNode node) {
        super(node);
    }

    public CobolDataItemElement getParentItem() {
        PsiElement parent = getParent();
        if (parent instanceof CobolDataItemElement) {
            return (CobolDataItemElement) parent;
        }
        return null;
    }

    public CobolDataItemElement[] getChildItems() {
        return Arrays.stream(getChildren())
                .filter(CobolDataItemElement.class::isInstance)
                .map(CobolDataItemElement.class::cast)
                .toArray(CobolDataItemElement[]::new);
    }

    public boolean hasParentItem() {
        return getParentItem() != null;
    }

    @Override
    public String getName() {
        ASTNode nameComposite = getNode().findChildByType(CobolTypes.DATA_ITEM_NAME_);
        if (nameComposite == null) {
            return "";
        }
        ASTNode nameToken = nameComposite.findChildByType(CobolTypes.COBOLWORD);
        if (nameToken == null) {
            return "";
        }
        return nameComposite.getText();
    }

    @Override
    public PsiElement setName(String newName) {
        ASTNode nameComposite = getNode().findChildByType(CobolTypes.DATA_ITEM_NAME_);
        ASTNode newNameComposite = createDataItemName(getProject(), newName);
        getNode().replaceChild(nameComposite, newNameComposite);
        return this;
    }
}
