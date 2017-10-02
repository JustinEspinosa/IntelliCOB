package com.github.justinespinosa.intellicob.structureview;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class CobolStructureViewModel extends TextEditorBasedStructureViewModel {

    public CobolStructureViewModel(PsiFile psiFile) {
        super(psiFile);
    }

    @NotNull
    @Override
    public StructureViewTreeElement getRoot() {
        return new CobolStructureViewTreeElement(getPsiFile());
    }

}
