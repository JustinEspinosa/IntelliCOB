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
import java.util.function.Predicate;

import static com.github.justinespinosa.intellicob.psi.PsiUtil.*;

public class CobolParagraphNameElement extends ASTWrapperPsiElement implements PsiReference {
    public CobolParagraphNameElement(@NotNull ASTNode node) {
        super(node);
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
        TokenSet types = TokenSet.create(CobolTypes.PARAGRAPH_, CobolTypes.SECTION_, CobolTypes.DECLARATIVES_SECTION_);

        CobolProcedureDivision_ procedureDivision = PsiUtil.getProcedureDivision(this);
        List<PsiElement> elements = findElements(types, procedureDivision);

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
        ASTNode newParagraph = createParagraphName(getProject(), newElementName);
        ASTNode newWordNode = newParagraph.findChildByType(CobolTypes.COBOLWORD);
        getNode().replaceChild(wordNode, newWordNode);

        return this;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    private boolean isVariantOf(PsiElement element) {
        return element instanceof CobolParagraphElement &&
                isCompletionVariant(((CobolParagraphElement) element).getName(), getText());

    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        return element instanceof CobolParagraphElement &&
                ((CobolParagraphElement) element).getName().equalsIgnoreCase(getText());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return resolve(this::isVariantOf);
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
