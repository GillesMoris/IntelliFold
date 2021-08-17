package com.github.gillesmoris.intellifold.foldingbuilders

import com.github.gillesmoris.intellifold.settings.ProjectSettingsState
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.util.containers.toArray

open class AggregateFoldingBuilder(val foldingBuilders: Array<FoldingBuilderEx>) :
    FoldingBuilderEx(),
    DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val settingsState = ProjectSettingsState.getInstance(root.project).state
        if (quick || !settingsState.enabled) return emptyArray()
        val descriptors = mutableListOf<FoldingDescriptor>()
        for (foldingBuilder in foldingBuilders) {
            descriptors.addAll(foldingBuilder.buildFoldRegions(root, document, quick))
        }
        return mergeFoldingDescriptors(descriptors.toArray(FoldingDescriptor.EMPTY))
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return ""
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}
