package com.github.justinespinosa.intellicob.psi;

import com.github.justinespinosa.intellicob.filetype.CobolFileType;
import com.github.justinespinosa.intellicob.language.CobolLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CobolFile extends PsiFileBase {
    public CobolFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, CobolLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return CobolFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Cobol File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }

}
