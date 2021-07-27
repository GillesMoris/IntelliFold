package com.github.gillesmoris.intellifold.foldingbuilders

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.containers.toArray
import kotlin.math.max

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

fun mergeFoldingDescriptors(foldingDescriptors: Array<FoldingDescriptor>): Array<FoldingDescriptor> {
    foldingDescriptors.sortBy { it.range.startOffset }
    val mergedDescriptors = mutableListOf<FoldingDescriptor>()
    var index = 0
    while (index < foldingDescriptors.size) {
        val descriptor = foldingDescriptors[index]
        val mergedRangeNode = descriptor.element
        val mergedRangeStart = descriptor.range.startOffset
        var mergedRangeEnd = descriptor.range.endOffset
        var mergedRange = TextRange(mergedRangeStart, mergedRangeEnd)
        for (nextIndex in (index + 1) until foldingDescriptors.size) {
            val nextDescriptorRange = foldingDescriptors[nextIndex].range
            if (!nextDescriptorRange.intersects(mergedRange)) {
                break
            }
            mergedRangeEnd = max(mergedRangeEnd, nextDescriptorRange.endOffset)
            mergedRange = TextRange(mergedRangeStart, mergedRangeEnd)
            index += 1
        }
        mergedDescriptors.add(FoldingDescriptor(mergedRangeNode, mergedRange))
        index += 1
    }
    return mergedDescriptors.toArray(FoldingDescriptor.EMPTY)
}
