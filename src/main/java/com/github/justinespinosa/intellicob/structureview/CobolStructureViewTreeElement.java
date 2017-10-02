package com.github.justinespinosa.intellicob.structureview;

import com.github.justinespinosa.intellicob.psi.impl.*;
import com.github.justinespinosa.intellicob.resources.CobolIcons;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CobolStructureViewTreeElement implements StructureViewTreeElement {

    private static Map<Class<?>, Function<? extends PsiElement, String>> ALLOWED_ELEMENTS = new HashMap<>();

    static {
        ALLOWED_ELEMENTS.put(PsiFile.class, (PsiFile psiElement) -> psiElement.getName());
        ALLOWED_ELEMENTS.put(CobolCobolProgram_Impl.class, (CobolCobolProgram_Impl psiElement) -> psiElement.getIdentificationDivision_().getProgramId_().getProgramIdName_().getText());
        ALLOWED_ELEMENTS.put(CobolIdentificationDivision_Impl.class, psiElement -> "PROCEDURE");
        ALLOWED_ELEMENTS.put(CobolDataDivision_Impl.class, psiElement -> "DATA");
        ALLOWED_ELEMENTS.put(CobolFileSection_Impl.class, psiElement -> "FILE");
        ALLOWED_ELEMENTS.put(CobolWorkingStorageSection_Impl.class, psiElement -> "WORKING-STORAGE");
        ALLOWED_ELEMENTS.put(CobolExtendedStorageSection_Impl.class, psiElement -> "EXTENDED-STORAGE");
        ALLOWED_ELEMENTS.put(CobolLinkageSection_Impl.class, psiElement -> "LINKAGE");
        ALLOWED_ELEMENTS.put(CobolDataItem_Impl.class, (CobolDataItem_Impl psiElement) -> psiElement.getDataItemName_().getText());
        ALLOWED_ELEMENTS.put(CobolProcedureDivision_Impl.class, psiElement -> "PROCEDURE");
        ALLOWED_ELEMENTS.put(CobolParagraph_Impl.class, (CobolParagraph_Impl psiElement) -> psiElement.getParagraphName_().getText());
        ALLOWED_ELEMENTS.put(CobolSection_Impl.class, (CobolSection_Impl psiElement) -> psiElement.getParagraphName_().getText());
    }

    private final PsiElement psiElement;
    private final ItemPresentation presentation;


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
                return CobolIcons.STRUCTURE;
            }
        };

    }

    private static boolean includedInStructureView(PsiElement element) {
        return ALLOWED_ELEMENTS.keySet().contains(element.getClass());
    }

    private String getPresentableText() {
        Function<PsiElement, String> textFunc = (Function<PsiElement, String>) ALLOWED_ELEMENTS
                .getOrDefault(psiElement.getClass(), psiElement1 -> "<UNKNOWN>");

        return textFunc.apply(psiElement);
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
