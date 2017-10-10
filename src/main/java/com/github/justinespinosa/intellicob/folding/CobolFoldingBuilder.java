package com.github.justinespinosa.intellicob.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.*;
import static com.github.justinespinosa.intellicob.structureview.CobolStructureViewConstants.ALLOWED_ELEMENTS;
import static com.github.justinespinosa.intellicob.structureview.CobolStructureViewConstants.DEFAULT_DESCRIPTOR;

public class CobolFoldingBuilder implements FoldingBuilder {

    private static final TokenSet FOLDABLES = TokenSet.create(IDENTIFICATION_DIVISION_, ENVIRONMENT_DIVISION_,
            DATA_DIVISION_, FILE_SECTION_, WORKING_STORAGE_SECTION_, EXTENDED_STORAGE_SECTION_,
            LINKAGE_SECTION_, DATA_ITEM_RECORD_, DATA_ITEM_CHILD_RECORD_, PROCEDURE_DIVISION_,
            DECLARATIVES_, DECLARATIVES_SECTION_, SECTION_, PARAGRAPH_);

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        process(node, descriptors);
        return descriptors.stream().toArray(FoldingDescriptor[]::new);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return ALLOWED_ELEMENTS.getOrDefault(node.getElementType(), DEFAULT_DESCRIPTOR).getPresentableText(node.getPsi());
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }

    private ASTNode process(ASTNode node, List<FoldingDescriptor> descriptors) {
        Arrays.stream(node.getChildren(null))
                .map(child -> process(child, descriptors))
                .filter(child -> FOLDABLES.contains(child.getElementType()))
                .forEach(child -> descriptors.add(new FoldingDescriptor(child, child.getTextRange())));

        return node;
    }
}
