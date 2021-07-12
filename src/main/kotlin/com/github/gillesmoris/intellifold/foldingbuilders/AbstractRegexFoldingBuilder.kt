package com.github.gillesmoris.intellifold.foldingbuilders

import com.github.gillesmoris.intellifold.settings.ProjectSettingsState
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.util.containers.toArray

abstract class AbstractRegexFoldingBuilder : FoldingBuilderEx(), DumbAware {
    var regexes: List<Regex> = emptyList()

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val project = root.project
        val settings = ProjectSettingsState.getInstance(project)
        if (quick || !settings.state.enabled) return emptyArray()
        regexes = settings.state.list.map { Regex(it) }
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
}
