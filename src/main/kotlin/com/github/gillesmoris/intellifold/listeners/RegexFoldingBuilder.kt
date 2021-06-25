package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset

class RegexFoldingBuilder() : CustomFoldingBuilder(), DumbAware {
    override fun buildLanguageFoldRegions(
        descriptors: MutableList<FoldingDescriptor>,
        root: PsiElement,
        document: Document,
        quick: Boolean
    ) {
        println("regexFoldingBuilder")
        if (document.text.contains("Main")) {
            val printlineElement = root.children[3].children[11].children[9].children[4];
            val whiteSpaceElement = printlineElement.prevSibling;
            val textRange = TextRange(whiteSpaceElement.textOffset, printlineElement.endOffset);
            val foldingDescriptor = FoldingDescriptor(printlineElement.parent, textRange);
            descriptors.add(foldingDescriptor);
        }
    }

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String {
        return ""
    }

    override fun isRegionCollapsedByDefault(node: ASTNode): Boolean {
        return true;
    }
}
