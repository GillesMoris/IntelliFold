package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.openapi.project.DumbAware

abstract class AbstractRegexFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun getPlaceholderText(node: ASTNode): String {
        return ""
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}