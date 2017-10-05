package com.github.justinespinosa.intellicob.psi.visitor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;

import java.util.ArrayList;
import java.util.List;

public abstract class ElementCollector extends PsiRecursiveElementWalkingVisitor {
    private final List<PsiElement> elements = new ArrayList<>();

    @Override
    public void elementFinished(PsiElement element) {
        super.visitElement(element);
        if (elementMatches(element)) {
            elements.add(element);
        }
    }

    public List<PsiElement> getElements() {
        return elements;
    }

    public abstract boolean elementMatches(PsiElement element);
}
