package com.github.justinespinosa.intellicob.structureview;

import com.intellij.psi.PsiElement;

import javax.swing.*;
import java.util.function.Function;

public class CobolItemDescriptor {
    private Function<PsiElement, String> presentableTextFunction;
    private Icon icon;

    public CobolItemDescriptor(Icon icon, Function<? extends PsiElement, String> presentableTextFunction) {
        this.icon = icon;
        this.presentableTextFunction = (Function<PsiElement, String>) presentableTextFunction;
    }

    public String getPresentableText(PsiElement psiElement) {
        return presentableTextFunction.apply(psiElement);
    }

    public Icon getIcon(boolean unused) {
        return icon;
    }
}
