package com.github.justinespinosa.intellicob.psi;

import com.github.justinespinosa.intellicob.filetype.CobolFileType;
import com.github.justinespinosa.intellicob.psi.node.DummyFileBuilder;
import com.github.justinespinosa.intellicob.psi.visitor.ElementCollectorByTokenType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.TokenSet;

import java.util.List;

public class PsiUtil {

    public static CobolProcedureDivision_ getProcedureDivision(PsiElement element) {
        PsiElement parent = element;
        while ((parent = parent.getParent()) != null) {
            if (parent instanceof CobolProcedureDivision_) {
                return (CobolProcedureDivision_) parent;
            }
        }
        return null;
    }

    public static List<PsiElement> findElements(TokenSet types, PsiElement parent) {
        ElementCollectorByTokenType visitor = new ElementCollectorByTokenType(types);
        parent.accept(visitor);
        return visitor.getElements();
    }

    public static ASTNode createParagraph(Project project, String name) {
        DummyFileBuilder builder = new DummyFileBuilder();
        builder.withParagraphName(name);
        CobolFile file = createDummyProgram(project, builder.build());
        return file.getNode()
                .findChildByType(CobolTypes.COBOL_PROGRAM_)
                .findChildByType(CobolTypes.PROCEDURE_DIVISION_)
                .findChildByType(CobolTypes.PARAGRAPH_)
                .findChildByType(CobolTypes.PARAGRAPH_NAME_);
    }

    public static CobolFile createDummyProgram(Project project, String code) {
        return (CobolFile) PsiFileFactory.getInstance(project).
                createFileFromText("dummy", CobolFileType.INSTANCE, code);

    }
}
