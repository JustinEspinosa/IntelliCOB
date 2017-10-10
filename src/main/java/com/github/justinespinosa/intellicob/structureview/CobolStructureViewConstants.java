package com.github.justinespinosa.intellicob.structureview;

import com.github.justinespinosa.intellicob.psi.CobolCobolProgram_;
import com.github.justinespinosa.intellicob.psi.CobolDataItemElement;
import com.github.justinespinosa.intellicob.psi.CobolParagraphElement;
import com.github.justinespinosa.intellicob.resources.CobolIcons;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;

import java.util.HashMap;
import java.util.Map;

import static com.github.justinespinosa.intellicob.parser.CobolParserDefinition.FILE;
import static com.github.justinespinosa.intellicob.psi.CobolTypes.*;

public class CobolStructureViewConstants {
    public static final Map<IElementType, CobolItemDescriptor> ALLOWED_ELEMENTS = new HashMap<>();
    public static final CobolItemDescriptor DEFAULT_DESCRIPTOR = new CobolItemDescriptor(CobolIcons.VARIABLE, psiElement -> "<UNKNOWN>");

    static {
        ALLOWED_ELEMENTS.put(FILE, new CobolItemDescriptor(CobolIcons.FILE, (PsiFile psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(COBOL_PROGRAM_, new CobolItemDescriptor(CobolIcons.PROGRAM, (CobolCobolProgram_ psiElement) -> psiElement.getIdentificationDivision_().getProgramId_().getProgramIdName_().getText()));
        ALLOWED_ELEMENTS.put(IDENTIFICATION_DIVISION_, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "IDENTIFICATION"));
        ALLOWED_ELEMENTS.put(ENVIRONMENT_DIVISION_, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "ENVIRONMENT"));
        ALLOWED_ELEMENTS.put(DATA_DIVISION_, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "DATA"));
        ALLOWED_ELEMENTS.put(FILE_SECTION_, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "FILE"));
        ALLOWED_ELEMENTS.put(WORKING_STORAGE_SECTION_, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "WORKING-STORAGE"));
        ALLOWED_ELEMENTS.put(EXTENDED_STORAGE_SECTION_, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "EXTENDED-STORAGE"));
        ALLOWED_ELEMENTS.put(LINKAGE_SECTION_, new CobolItemDescriptor(CobolIcons.SECTION, psiElement -> "LINKAGE"));
        ALLOWED_ELEMENTS.put(DATA_ITEM_66_, new CobolItemDescriptor(CobolIcons.VARIABLE, (CobolDataItemElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(DATA_ITEM_77_, new CobolItemDescriptor(CobolIcons.VARIABLE, (CobolDataItemElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(DATA_ITEM_88_, new CobolItemDescriptor(CobolIcons.VARIABLE, (CobolDataItemElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(DATA_ITEM_RECORD_, new CobolItemDescriptor(CobolIcons.VARIABLE, (CobolDataItemElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(DATA_ITEM_CHILD_RECORD_, new CobolItemDescriptor(CobolIcons.VARIABLE, (CobolDataItemElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(PROCEDURE_DIVISION_, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "PROCEDURE"));
        ALLOWED_ELEMENTS.put(PARAGRAPH_, new CobolItemDescriptor(CobolIcons.PARAGRAPH, (CobolParagraphElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(SECTION_, new CobolItemDescriptor(CobolIcons.SECTION, (CobolParagraphElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(DECLARATIVES_SECTION_, new CobolItemDescriptor(CobolIcons.SECTION, (CobolParagraphElement psiElement) -> psiElement.getName()));
        ALLOWED_ELEMENTS.put(DECLARATIVES_, new CobolItemDescriptor(CobolIcons.DIVISION, psiElement -> "DECLARATIVES"));
    }
}
