package com.github.justinespinosa.intellicob.filetype;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collector;

import static java.util.stream.Collectors.joining;

public class CobolFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(CobolFileType.INSTANCE, Arrays.asList("cob","cobol").stream().collect(joining(FileTypeConsumer.EXTENSION_DELIMITER)));
    }
}
