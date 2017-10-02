package com.github.justinespinosa.intellicob.psi;

import com.github.justinespinosa.intellicob.language.CobolLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CobolElementType extends IElementType {
    public CobolElementType(@NotNull @NonNls String debugName) {
        super(debugName, CobolLanguage.INSTANCE);
    }
}
