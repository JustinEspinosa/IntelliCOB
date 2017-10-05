package com.github.justinespinosa.intellicob.psi.visitor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;

public class ElementCollectorByTokenType extends ElementCollector {
    private TokenSet types;

    public ElementCollectorByTokenType(TokenSet types) {
        this.types = types;
    }

    @Override
    public boolean elementMatches(PsiElement element) {
        return types.contains(element.getNode().getElementType());
    }
}
