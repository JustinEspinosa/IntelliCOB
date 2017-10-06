package com.github.justinespinosa.intellicob.psi;

import com.github.justinespinosa.intellicob.filetype.CobolFileType;
import com.github.justinespinosa.intellicob.parser.CobolParserDefinition;
import com.github.justinespinosa.intellicob.psi.node.DummyFileBuilder;
import com.github.justinespinosa.intellicob.psi.visitor.ElementCollectorByTokenType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.TokenSet;

import java.util.List;

public class PsiUtil {

    private static CobolParserDefinition parserDefinition = new CobolParserDefinition();

    public static boolean isWhiteSpace(PsiElement element) {
        return parserDefinition.getWhitespaceTokens().contains(element.getNode().getElementType());
    }

    public static boolean isComment(PsiElement element) {
        return parserDefinition.getCommentTokens().contains(element.getNode().getElementType());
    }

    public static boolean isSkipped(PsiElement element) {
        return isWhiteSpace(element) || isComment(element);
    }

    public static PsiElement getNextNotSkippedSibling(PsiElement element) {
        PsiElement returnElement = element.getNextSibling();
        while (returnElement != null && isSkipped(returnElement)) {
            returnElement = returnElement.getNextSibling();
        }
        return returnElement;
    }

    public static CobolProcedureDivision_ getProcedureDivision(PsiElement element) {
        PsiElement parent = element;
        while ((parent = parent.getParent()) != null) {
            if (parent instanceof CobolProcedureDivision_) {
                return (CobolProcedureDivision_) parent;
            }
        }
        return null;
    }

    public static CobolCobolProgram_ getProgram(PsiElement element) {
        PsiElement parent = element;
        while ((parent = parent.getParent()) != null) {
            if (parent instanceof CobolCobolProgram_) {
                return (CobolCobolProgram_) parent;
            }
        }
        return null;
    }

    public static CobolDataDivision_ getDataDivision(CobolCobolProgram_ program) {
        ASTNode dataDivision = program.getNode().findChildByType(CobolTypes.DATA_DIVISION_);
        if (dataDivision != null) {
            return (CobolDataDivision_) dataDivision.getPsi();
        }
        return null;
    }

    public static List<PsiElement> findElements(TokenSet types, PsiElement parent) {
        ElementCollectorByTokenType visitor = new ElementCollectorByTokenType(types);
        parent.accept(visitor);
        return visitor.getElements();
    }

    public static ASTNode createParagraphName(Project project, String name) {
        DummyFileBuilder builder = new DummyFileBuilder();
        builder.withParagraphName(name);
        CobolFile file = createDummyProgram(project, builder.build());
        return file.getNode()
                .findChildByType(CobolTypes.COBOL_PROGRAM_)
                .findChildByType(CobolTypes.PROCEDURE_DIVISION_)
                .findChildByType(CobolTypes.PARAGRAPH_)
                .findChildByType(CobolTypes.PARAGRAPH_NAME_);
    }

    public static ASTNode createDataItemName(Project project, String name) {
        DummyFileBuilder builder = new DummyFileBuilder();
        builder.withDataItem(name);
        CobolFile file = createDummyProgram(project, builder.build());
        return file.getNode()
                .findChildByType(CobolTypes.COBOL_PROGRAM_)
                .findChildByType(CobolTypes.DATA_DIVISION_)
                .findChildByType(CobolTypes.WORKING_STORAGE_SECTION_)
                .findChildByType(CobolTypes.DATA_ITEM_RECORD_)
                .findChildByType(CobolTypes.DATA_ITEM_NAME_);
    }


    public static CobolFile createDummyProgram(Project project, String code) {
        return (CobolFile) PsiFileFactory.getInstance(project).
                createFileFromText("dummy", CobolFileType.INSTANCE, code);

    }
}
