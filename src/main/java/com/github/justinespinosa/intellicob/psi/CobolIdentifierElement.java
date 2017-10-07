package com.github.justinespinosa.intellicob.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.justinespinosa.intellicob.psi.CobolTypes.*;
import static com.github.justinespinosa.intellicob.psi.PsiUtil.*;

public class CobolIdentifierElement extends ASTWrapperPsiElement implements PsiReference {
    public CobolIdentifierElement(@NotNull ASTNode node) {
        super(node);
    }

    private static boolean nameMatches(CobolDataItemElement dataItem, CobolIdentifierElement identifier) {
        if (dataItem == null || identifier == null) {
            return false;
        }
        return dataItem.getName().equalsIgnoreCase(identifier.getIdentifierName());
    }

    private static Stream<CobolDataItemElement> flatDataItem(CobolDataItemElement[] dataStream) {
        return Arrays.stream(dataStream)
                .flatMap(dataItemElement -> Stream.concat(flatDataItem(dataItemElement.getChildItems()), Stream.of(dataItemElement)));
    }

    private static CobolDataItemElement[] searchMatchingChildren(CobolDataItemElement[] dataItem, CobolIdentifierElement identifier) {
        return Arrays.stream(dataItem)
                .flatMap(item -> flatDataItem(item.getChildItems()))
                .filter(item -> nameMatches(item, identifier))
                .toArray(CobolDataItemElement[]::new);
    }

    private static boolean upperStructureReferenceMatches(CobolDataItemElement dataItem, CobolIdentifierElement identifier) {
        CobolDataItemElement[] searchDataItems = new CobolDataItemElement[]{dataItem};
        CobolIdentifierElement searchIdentifier = identifier;

        while (searchIdentifier.hasChildIdentifier()) {
            searchIdentifier = searchIdentifier.getChildIdentifier();
            searchDataItems = searchMatchingChildren(searchDataItems, searchIdentifier);
            if (searchDataItems.length == 0) {
                return false;
            }
        }
        return true;
    }

    private static CobolDataItemElement searchMatchingParent(CobolDataItemElement dataItem, CobolIdentifierElement identifier) {
        CobolDataItemElement searchDataItem = dataItem;

        while (searchDataItem.hasParentItem()) {
            searchDataItem = searchDataItem.getParentItem();
            if (nameMatches(searchDataItem, identifier)) {
                return searchDataItem;
            }
        }
        return null;
    }


    private static boolean subStructureReferenceMatches(CobolDataItemElement dataItem, CobolIdentifierElement identifier) {
        CobolDataItemElement searchDataItem = dataItem;
        CobolIdentifierElement searchIdentifier = identifier;

        while (searchIdentifier.hasParentIdentifier()) {
            searchIdentifier = searchIdentifier.getParentIdentifier();
            searchDataItem = searchMatchingParent(searchDataItem, searchIdentifier);
            if (searchDataItem == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean structureReferenceMatches(CobolDataItemElement dataItem, CobolIdentifierElement identifier) {
        if (!nameMatches(dataItem, identifier)) {
            return false;
        }
        return subStructureReferenceMatches(dataItem, identifier);

    }

    private CobolIdentifierElement getNextIdentifier(Function<PsiElement, PsiElement> nextElementFunction) {
        PsiElement nextSibling = nextElementFunction.apply(this);
        if (nextSibling instanceof CobolIdentifierSeparator_) {
            PsiElement parent = nextElementFunction.apply(nextSibling);
            if (parent instanceof CobolIdentifierElement) {
                return (CobolIdentifierElement) parent;
            }
        }
        return null;

    }

    public CobolIdentifierElement getParentIdentifier() {
        return getNextIdentifier(PsiUtil::getNextNotSkippedSibling);
    }

    public CobolIdentifierElement getChildIdentifier() {
        return getNextIdentifier(PsiUtil::getPreviousNotSkippedSibling);
    }


    public boolean hasParentIdentifier() {
        return getParentIdentifier() != null;
    }

    public boolean hasChildIdentifier() {
        return getChildIdentifier() != null;
    }

    public String getIdentifierName() {
        return getText();
    }

    @Override
    public PsiElement getElement() {
        return this;
    }

    @Override
    public TextRange getRangeInElement() {
        return new TextRange(0, getTextLength());
    }

    private PsiElement[] resolve(Predicate<PsiElement> predicate) {
        TokenSet types = TokenSet.create(DATA_ITEM_66_, DATA_ITEM_77_, DATA_ITEM_88_, DATA_ITEM_RECORD_, DATA_ITEM_CHILD_RECORD_);

        CobolDataDivision_ dataDivision = PsiUtil.getDataDivision(PsiUtil.getProgram(this));
        List<PsiElement> elements = findElements(types, dataDivision);

        return elements.stream()
                .filter(predicate)
                .toArray(PsiElement[]::new);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return Arrays.stream(resolve(this::isReferenceTo))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return this.getText();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {

        ASTNode wordNode = getNode().findChildByType(CobolTypes.COBOLWORD);
        ASTNode newDataItemName = createDataItemName(getProject(), newElementName);
        ASTNode newWordNode = newDataItemName.findChildByType(CobolTypes.COBOLWORD);
        getNode().replaceChild(wordNode, newWordNode);

        return this;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        if (element instanceof CobolDataItemElement) {
            return structureReferenceMatches((CobolDataItemElement) element, this);
        }
        return false;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return resolve(this::isVariantOf);
    }

    public boolean isVariantOf(PsiElement element) {
        if (element instanceof CobolDataItemElement) {
            CobolDataItemElement dataItemElement = (CobolDataItemElement) element;
            if (isCompletionVariant(dataItemElement.getName(), getIdentifierName())) {
                if (upperStructureReferenceMatches(dataItemElement, this)) {
                    return subStructureReferenceMatches(dataItemElement, this);
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSoft() {
        return false;
    }

    @Override
    public PsiReference getReference() {
        return this;
    }
}
