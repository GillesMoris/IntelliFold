package com.github.gillesmoris.intellifold.foldingbuilders

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

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
