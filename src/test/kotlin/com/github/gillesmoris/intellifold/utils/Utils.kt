package com.github.gillesmoris.intellifold.utils

import DummyAstNode
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.util.containers.toArray

fun foldingDescriptorsFromStartAndEnds(startAndEnds: IntArray): Array<FoldingDescriptor> {
    val descriptors = mutableListOf<FoldingDescriptor>()
    for (index in startAndEnds.indices step 2) {
        val textRange = TextRange(startAndEnds[index], startAndEnds[index + 1])
        descriptors.add(FoldingDescriptor(DummyAstNode(), textRange))
    }
    return descriptors.toArray(FoldingDescriptor.EMPTY)
}

fun foldingDescriptorsToStartAndEnds(foldingDescriptors: Array<FoldingDescriptor>): IntArray {
    val startAndEnds = mutableListOf<Int>()
    for (descriptor in foldingDescriptors) {
        startAndEnds.add(descriptor.range.startOffset)
        startAndEnds.add(descriptor.range.endOffset)
    }
    return startAndEnds.toIntArray()
}
