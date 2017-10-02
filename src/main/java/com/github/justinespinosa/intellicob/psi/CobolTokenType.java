package com.github.justinespinosa.intellicob.psi;

import com.intellij.psi.tree.IElementType;
import com.github.justinespinosa.intellicob.language.CobolLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CobolTokenType extends IElementType {
    private boolean isKeyWord;

    public CobolTokenType(@NotNull @NonNls String debugName, boolean isKeyWord) {
        super(debugName, CobolLanguage.INSTANCE);
        this.isKeyWord = isKeyWord;
    }

    public boolean isKeyWord() {
        return isKeyWord;
    }

    @Override
    public String toString() {
        return "CobolTokenType." + super.toString();
    }
}
