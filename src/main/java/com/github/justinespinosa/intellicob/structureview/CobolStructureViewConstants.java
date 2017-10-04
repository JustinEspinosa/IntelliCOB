package com.github.justinespinosa.intellicob.structureview;

import com.github.justinespinosa.intellicob.psi.*;
import com.github.justinespinosa.intellicob.psi.impl.*;
import com.github.justinespinosa.intellicob.resources.CobolIcons;
import com.intellij.psi.PsiFile;

import java.util.HashMap;
import java.util.Map;

public class CobolStructureViewConstants {
    public static final Map<Class<?>, CobolItemDescriptor> ALLOWED_ELEMENTS = new HashMap<>();
    public static final CobolItemDescriptor DEFAULT_DESCRIPTOR = new CobolItemDescriptor(CobolIcons.VARIABLE, psiElement -> "<UNKNOWN>");

    static {
        ALLOWED_ELEMENTS.put(CobolFile.class, new CobolItemDescriptor(CobolIcons.FILE, (PsiFile psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(CobolCobolProgram_Impl.class, new CobolItemDescriptor(CobolIcons.PROGRAM, (CobolCobolProgram_ psiElement) -> psiElement.getIdentificationDivision_().getProgramId_().getProgramIdName_().getText()));
        ALLOWED_ELEMENTS.put(CobolIdentificationDivision_Impl.class, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "IDENTIFICATION"));
        ALLOWED_ELEMENTS.put(CobolEnvironmentDivision_Impl.class, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "ENVIRONMENT"));
        ALLOWED_ELEMENTS.put(CobolDataDivision_Impl.class, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "DATA"));
        ALLOWED_ELEMENTS.put(CobolFileSection_Impl.class, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "FILE"));
        ALLOWED_ELEMENTS.put(CobolWorkingStorageSection_Impl.class, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "WORKING-STORAGE"));
        ALLOWED_ELEMENTS.put(CobolExtendedStorageSection_Impl.class, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "EXTENDED-STORAGE"));
        ALLOWED_ELEMENTS.put(CobolLinkageSection_Impl.class, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "LINKAGE"));
        ALLOWED_ELEMENTS.put(CobolDataItem_Impl.class, new CobolItemDescriptor(CobolIcons.VARIABLE, (CobolDataItem_ psiElement) -> psiElement.getDataItemName_().getText()));
        ALLOWED_ELEMENTS.put(CobolProcedureDivision_Impl.class, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "PROCEDURE"));
        ALLOWED_ELEMENTS.put(CobolParagraph_Impl.class, new CobolItemDescriptor(CobolIcons.PARAGRAPH, (CobolParagraph_ psiElement) -> psiElement.getParagraphName_().getText()));
        ALLOWED_ELEMENTS.put(CobolSection_Impl.class, new CobolItemDescriptor(CobolIcons.SECTION, (CobolSection_ psiElement) -> psiElement.getParagraphName_().getText()));
        ALLOWED_ELEMENTS.put(CobolDeclarativesSection_Impl.class, new CobolItemDescriptor(CobolIcons.SECTION, (CobolDeclarativesSection_Impl psiElement) -> psiElement.getParagraphName_().getText()));
        ALLOWED_ELEMENTS.put(CobolDeclaratives_Impl.class, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "DECLARATIVES"));
    }
}
