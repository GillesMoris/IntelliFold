package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.util.containers.toArray

class RegexFoldingBuilder() : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        println("regexFoldingBuilder")
        if (quick) return emptyArray()
        val descriptors = mutableListOf<FoldingDescriptor>()
        if (document.text.contains("Main")) {
            val printlineElement = root.children[3].children[11].children[9].children[4]
            val whiteSpaceElement = printlineElement.prevSibling
            val textRange = TextRange(whiteSpaceElement.textOffset, printlineElement.endOffset)
            val foldingDescriptor = FoldingDescriptor(printlineElement.parent, textRange)
            descriptors.add(foldingDescriptor);
        }
        return descriptors.toArray(FoldingDescriptor.EMPTY)
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return ""
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}
