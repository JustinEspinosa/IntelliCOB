package com.github.justinespinosa.intellicob.psi.inject;

import com.github.justinespinosa.intellicob.psi.CobolTypes;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;

public class CobolCopyInjector implements LanguageInjector {


    @Override
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host.getNode().getElementType() == CobolTypes.COPY_PREPROCESSOR) {
            //TODO Does not work: does not seem to be the right approach
            /*String code = new DummyFileBuilder().withDataItem("my-var").build();
            CobolFile file = PsiUtil.createDummyProgram(host.getProject(), code);
            host.replace(file);*/
        }

    }

}
