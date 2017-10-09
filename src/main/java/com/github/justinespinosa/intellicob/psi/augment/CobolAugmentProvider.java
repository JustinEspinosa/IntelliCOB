package com.github.justinespinosa.intellicob.psi.augment;

import com.github.justinespinosa.intellicob.psi.CobolDataItemRecord_;
import com.github.justinespinosa.intellicob.psi.CobolFile;
import com.github.justinespinosa.intellicob.psi.PsiUtil;
import com.github.justinespinosa.intellicob.psi.node.DummyFileBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.augment.PsiAugmentProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.COPY_PREPROCESSOR;

public class CobolAugmentProvider extends PsiAugmentProvider {

    @NotNull
    @Override
    protected <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type) {
        if (element.getNode().getElementType() == COPY_PREPROCESSOR) {
            String code = new DummyFileBuilder().withDataItem("var").build();
            CobolFile includedCode = PsiUtil.createDummyProgram(element.getProject(), code);
            PsiElement var = includedCode.findChildByClass(CobolDataItemRecord_.class);
            return Arrays.asList((Psi) var);
        }
        return super.getAugments(element, type);
    }
}
