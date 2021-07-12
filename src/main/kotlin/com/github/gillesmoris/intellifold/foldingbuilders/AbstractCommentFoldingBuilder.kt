package com.github.gillesmoris.intellifold.foldingbuilders

import com.github.gillesmoris.intellifold.settings.ProjectSettingsState
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.util.containers.toArray

abstract class AbstractCommentFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val settingsState = ProjectSettingsState.getInstance(root.project).state
        if (quick || !settingsState.enabled || !settingsState.commentFoldingEnabled) return emptyArray()
        val descriptors = mutableListOf<FoldingDescriptor>()
        visitComments(root) { comment: PsiComment ->
            descriptors.add(FoldingDescriptor(comment, makeRange(comment)))
        }
        return descriptors.toArray(FoldingDescriptor.EMPTY)
    }

    protected abstract fun visitComments(root: PsiElement, visitor: (PsiComment) -> Unit)

    override fun getPlaceholderText(node: ASTNode): String {
        return ""
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}
