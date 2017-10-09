package com.github.justinespinosa.intellicob.filetype;

import com.github.justinespinosa.intellicob.language.CobolLanguage;
import com.github.justinespinosa.intellicob.resources.CobolIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SqlMpFileType extends LanguageFileType {
    public static final SqlMpFileType INSTANCE = new SqlMpFileType();

    private SqlMpFileType() {
        super(CobolLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "SqlMp";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "SqlMp source file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "sqlmp";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CobolIcons.FILE;
    }
}