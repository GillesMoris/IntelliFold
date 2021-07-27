package com.github.gillesmoris.intellifold.foldingbuilders

import com.github.gillesmoris.intellifold.utils.foldingDescriptorsFromStartAndEnds
import com.github.gillesmoris.intellifold.utils.foldingDescriptorsToStartAndEnds
import org.junit.Assert
import org.junit.jupiter.api.Test

class UtilsTests {

    @Test
    fun testMergeSingleDescriptor() {
        val startAndEnds = intArrayOf(0, 5)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(startAndEnds, mergedStartAndEnds)
    }

    @Test
    fun testMergeTwoNonOverlappingDescriptors() {
        val startAndEnds = intArrayOf(0, 5, 10, 15)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(startAndEnds, mergedStartAndEnds)
    }

    @Test
    fun testMergeTwoOverlappingDescriptors() {
        val startAndEnds = intArrayOf(0, 10, 5, 15)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(intArrayOf(0, 15), mergedStartAndEnds)
    }

    @Test
    fun testMergeTwoAdjacentDescriptors() {
        val startAndEnds = intArrayOf(0, 5, 5, 10)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(intArrayOf(0, 10), mergedStartAndEnds)
    }

    @Test
    fun testMergeThreeNonOverlappingDescriptors() {
        val startAndEnds = intArrayOf(0, 5, 10, 15, 20, 25)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(startAndEnds, mergedStartAndEnds)
    }

    @Test
    fun testMergeThreeOverlappingDescriptors() {
        val startAndEnds = intArrayOf(0, 10, 5, 15, 10, 20)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(intArrayOf(0, 20), mergedStartAndEnds)
    }

    @Test
    fun testMergeThreeAdjacentDescriptors() {
        val startAndEnds = intArrayOf(0, 5, 5, 10, 10, 15)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(intArrayOf(0, 15), mergedStartAndEnds)
    }

    @Test
    fun testMergeMixedDescriptorsInOrder() {
        val startAndEnds = intArrayOf(0, 5, 5, 10, 15, 20, 25, 40, 30, 35, 45, 50)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(intArrayOf(0, 10, 15, 20, 25, 40, 45, 50), mergedStartAndEnds)
    }

    @Test
    fun testMergeMixedDescriptorsOutOfOrder() {
        val startAndEnds = intArrayOf(45, 50, 15, 20, 0, 5, 30, 35, 25, 40, 5, 10)
        val descriptors = foldingDescriptorsFromStartAndEnds(startAndEnds)
        val mergedDescriptors = mergeFoldingDescriptors(descriptors)
        val mergedStartAndEnds = foldingDescriptorsToStartAndEnds(mergedDescriptors)
        Assert.assertArrayEquals(intArrayOf(0, 10, 15, 20, 25, 40, 45, 50), mergedStartAndEnds)
    }
}
