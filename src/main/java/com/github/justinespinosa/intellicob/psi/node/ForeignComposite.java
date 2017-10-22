package com.github.justinespinosa.intellicob.psi.node;

import com.intellij.lang.TokenWrapper;
import com.intellij.psi.impl.source.tree.CompositeElement;
import org.jetbrains.annotations.NotNull;

public class ForeignComposite extends CompositeElement {
    public ForeignComposite(@NotNull TokenWrapper type) {
        super(type.getDelegate());
    }
}
