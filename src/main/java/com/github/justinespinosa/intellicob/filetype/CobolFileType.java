package com.github.justinespinosa.intellicob.filetype;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.github.justinespinosa.intellicob.language.CobolLanguage;
import com.github.justinespinosa.intellicob.resources.CobolIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CobolFileType extends LanguageFileType {
    public static final CobolFileType INSTANCE = new CobolFileType();

    private CobolFileType() {
        super(CobolLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Cobol";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Cobol source file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "cob";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CobolIcons.FILE;
    }
}