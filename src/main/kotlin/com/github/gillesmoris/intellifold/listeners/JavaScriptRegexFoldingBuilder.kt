package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSExpressionStatement
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.util.containers.toArray

class JavaScriptRegexFoldingBuilder() : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        println("JavaScriptRegexFoldingBuilder")
        if (quick) return emptyArray()
        val descriptors = mutableListOf<FoldingDescriptor>()
        if (root.language.isKindOf("JavaScript")) {
            root.accept(object : JSRecursiveWalkingElementVisitor() {
                override fun visitJSCallExpression(node: JSCallExpression) {
                    if (node.methodExpression!!.text == "console.log" && node.parent is JSExpressionStatement) {
                        val statement = node.parent;
                        val foldingDescriptor = FoldingDescriptor(statement.node, statement.textRange)
                        descriptors.add(foldingDescriptor)
                    }
                    super.visitJSCallExpression(node)
                }
            })
        }
        return descriptors.toArray(FoldingDescriptor.EMPTY)
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return ""
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return true
    }
}
