package com.github.justinespinosa.intellicob.psi.sqlmp;

import com.github.justinespinosa.intellicob.filetype.SqlMpFileType;
import com.github.justinespinosa.intellicob.language.CobolLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SqlMpFile extends PsiFileBase {
    public SqlMpFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, CobolLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SqlMpFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "SqlMp File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }

}
