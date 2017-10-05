package com.github.justinespinosa.intellicob.psi;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CobolNamesValidator implements NamesValidator {

    private static final Pattern COBOLWORD = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9-]*");

    @Override
    public boolean isKeyword(@NotNull String name, Project project) {
        IElementType type = CobolTokenTypeFactory.getToken(name);
        if (type instanceof CobolTokenType) {
            return ((CobolTokenType) type).isKeyWord();
        }
        return false;
    }

    @Override
    public boolean isIdentifier(@NotNull String name, Project project) {
        return COBOLWORD.matcher(name).matches();
    }

}
