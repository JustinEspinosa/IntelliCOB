package com.github.justinespinosa.intellicob.language;

import com.intellij.lang.Language;
import org.jetbrains.annotations.Nullable;

public class SqlMpLanguage extends Language {
    public static final SqlMpLanguage INSTANCE = new SqlMpLanguage();

    private SqlMpLanguage() {
        super("EmbeddedSqlMp");
    }

    @Nullable
    @Override
    public Language getBaseLanguage() {
        return CobolLanguage.INSTANCE;
    }
}
