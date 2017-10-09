package com.github.justinespinosa.intellicob.psi.sqlmp;

import com.github.justinespinosa.intellicob.language.SqlMpLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SqlMpTokenType extends IElementType {
    public SqlMpTokenType(@NotNull @NonNls String debugName) {
        super(debugName, SqlMpLanguage.INSTANCE);
    }
}
