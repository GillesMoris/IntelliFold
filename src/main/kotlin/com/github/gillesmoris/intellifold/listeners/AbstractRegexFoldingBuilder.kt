package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

abstract class AbstractRegexFoldingBuilder : FoldingBuilderEx(), DumbAware {
    val regexes: List<Regex> = listOf(
            Regex("""^System\.out\.println$"""),
            Regex("""^console\.log$"""),
    );

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