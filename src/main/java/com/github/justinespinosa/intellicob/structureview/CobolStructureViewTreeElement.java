package com.github.justinespinosa.intellicob.structureview;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;

import static com.github.justinespinosa.intellicob.structureview.CobolStructureViewConstants.ALLOWED_ELEMENTS;
import static com.github.justinespinosa.intellicob.structureview.CobolStructureViewConstants.DEFAULT_DESCRIPTOR;

public class CobolStructureViewTreeElement implements StructureViewTreeElement {
    private final PsiElement psiElement;
    private final ItemPresentation presentation;

    public CobolStructureViewTreeElement(PsiElement psiElement) {
        this.psiElement = psiElement;
        presentation = new CobolItemPresentation(psiElement, ALLOWED_ELEMENTS.getOrDefault(psiElement.getNode().getElementType(), DEFAULT_DESCRIPTOR));
    }

    private static boolean includedInStructureView(PsiElement element) {
        return ALLOWED_ELEMENTS.keySet().contains(element.getNode().getElementType());
    }

    @Override
    public Object getValue() {
        return psiElement;
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return presentation;
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        return Arrays.stream(psiElement.getChildren())
                .filter(CobolStructureViewTreeElement::includedInStructureView)
                .map(CobolStructureViewTreeElement::new)
                .toArray(TreeElement[]::new);
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (psiElement instanceof NavigationItem) {
            ((NavigationItem) psiElement).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return psiElement instanceof NavigationItem &&
                ((NavigationItem) psiElement).canNavigate();

    }

    @Override
    public boolean canNavigateToSource() {
        return psiElement instanceof NavigationItem &&
                ((NavigationItem) psiElement).canNavigateToSource();
    }

    private static class CobolItemPresentation implements ItemPresentation {
        private PsiElement psiElement;
        private CobolItemDescriptor descriptor;

        private CobolItemPresentation(PsiElement psiElement, CobolItemDescriptor descriptor) {
            this.psiElement = psiElement;
            this.descriptor = descriptor;
        }

        @Nullable
        @Override
        public String getPresentableText() {
            return descriptor.getPresentableText(psiElement);
        }

        @Nullable
        @Override
        public String getLocationString() {
            return "";
        }

        @Nullable
        @Override
        public Icon getIcon(boolean unused) {
            return descriptor.getIcon(unused);
        }
    }
}
