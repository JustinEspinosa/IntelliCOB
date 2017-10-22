package com.github.justinespinosa.intellicob.psi.node;

import com.intellij.lang.TokenWrapper;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

public class ForeignLeaf extends LeafPsiElement {
    private TokenWrapper type;

    public ForeignLeaf(@NotNull TokenWrapper type, CharSequence text) {
        super(type.getDelegate(), text);
        this.type = type;
    }

    public String toString() {
        return "ForeignLeaf" + "(" + getElementType().toString() + ")";
    }

    @NotNull
    @Override
    public String getText() {
        return type.getValue();
    }

}
