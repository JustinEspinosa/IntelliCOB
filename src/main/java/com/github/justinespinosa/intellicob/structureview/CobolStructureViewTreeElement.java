package com.github.justinespinosa.intellicob.structureview;

import com.github.justinespinosa.intellicob.psi.impl.*;
import com.github.justinespinosa.intellicob.resources.CobolIcons;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;

public class CobolStructureViewTreeElement implements StructureViewTreeElement {
    private PsiElement psiElement;
    private ItemPresentation presentation;

    public CobolStructureViewTreeElement(PsiElement psiElement) {
        this.psiElement = psiElement;
        this.presentation = new ItemPresentation() {

            @Nullable
            @Override
            public String getPresentableText() {
                return CobolStructureViewTreeElement.this.getPresentableText();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return "";
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return CobolIcons.FILE;
            }
        };

    }

    private static boolean includedInStructureView(PsiElement element) {
        if (element instanceof CobolCobolProgram_Impl) {
            return true;
        }
        if (element instanceof CobolIdentificationDivision_Impl) {
            return true;
        }
        if (element instanceof CobolDataDivision_Impl) {
            return true;
        }
        if (element instanceof CobolProcedureDivision_Impl) {
            return true;
        }
        if (element instanceof CobolParagraphHeader_Impl) {
            return true;
        }
        if (element instanceof CobolDataItem_Impl) {
            return true;
        }
        return false;
    }

    private String getPresentableText() {
        if (psiElement instanceof CobolCobolProgram_Impl) {
            return "PROGRAM";
        }
        if (psiElement instanceof CobolIdentificationDivision_Impl) {
            return "IDENTIFICATION DIVISION";
        }
        if (psiElement instanceof CobolProcedureDivision_Impl) {
            return "PROCEDURE DIVISION";
        }
        if (psiElement instanceof CobolParagraphHeader_Impl) {
            return ((CobolParagraphHeader_Impl) psiElement).getParagraphName_().getText();
        }
        return psiElement.getText();
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

    }

    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }
}
