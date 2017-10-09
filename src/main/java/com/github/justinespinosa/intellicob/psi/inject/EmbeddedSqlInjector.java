package com.github.justinespinosa.intellicob.psi.inject;

import com.github.justinespinosa.intellicob.language.SqlMpLanguage;
import com.github.justinespinosa.intellicob.psi.CobolTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;

public class EmbeddedSqlInjector implements LanguageInjector {
    @Override
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host.getNode().getElementType() == CobolTypes.EMBEDDED_SQL) {
            TextRange contentRange = ElementManipulators.getValueTextRange(host);
            if (!contentRange.isEmpty()) {
                injectionPlacesRegistrar.addPlace(SqlMpLanguage.INSTANCE, contentRange, null, null);
            }
        }

    }
}
