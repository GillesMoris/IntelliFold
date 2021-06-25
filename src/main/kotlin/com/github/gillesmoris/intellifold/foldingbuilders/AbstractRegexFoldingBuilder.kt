package com.github.gillesmoris.intellifold.foldingbuilders

import com.github.gillesmoris.intellifold.services.ConfigurationPersistentStateComponent
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.containers.toArray

abstract class AbstractRegexFoldingBuilder : FoldingBuilderEx(), DumbAware {
    val regexes: List<Regex> = listOf(
            Regex("""^System\.out\.println$"""),
            Regex("""^console\.log$"""),
    );

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        if (quick) return emptyArray()
        if (!ConfigurationPersistentStateComponent.instance.state.enabled) return emptyArray()
        val descriptors = buildFoldRegionsImpl(root, document)
        return descriptors.toArray(FoldingDescriptor.EMPTY)
    }

    abstract fun buildFoldRegionsImpl(root: PsiElement, document: Document): List<FoldingDescriptor>

    fun shouldFoldCall(call: String): Boolean {
        return regexes.any { it.matches(call) }
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return ""
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }

    companion object {
        fun makeRange(node: PsiElement): TextRange {
            // Fold everything up to the previous newline
            var startOffset: Int? = null
            var start: PsiElement = node
            while (start.prevSibling is PsiWhiteSpace) {
                val prev = start.prevSibling as PsiWhiteSpace
                val newLineIndex = prev.text.indexOf('\n')
                if (newLineIndex >= 0) {
                    startOffset = prev.startOffset + newLineIndex
                    break
                }
                start = prev
            }
            if (startOffset == null) {
                startOffset = start.startOffset
            }
            return TextRange(startOffset, node.endOffset)
        }
    }
}