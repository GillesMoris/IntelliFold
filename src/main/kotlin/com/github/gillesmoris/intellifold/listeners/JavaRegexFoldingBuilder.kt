package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.util.containers.toArray

class JavaRegexFoldingBuilder() : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        println("JavaRegexFoldingBuilder")
        if (quick) return emptyArray()
        val descriptors = mutableListOf<FoldingDescriptor>()
        when (root.language.id) {
            "JAVA" -> root.accept(object : JavaRecursiveElementVisitor() {
                override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                    if (expression.methodExpression.text == "System.out.println" && expression.parent is PsiExpressionStatement) {
                        val statement = expression.parent;
                        val foldingDescriptor = FoldingDescriptor(statement.node, statement.textRange)
                        descriptors.add(foldingDescriptor)
                    }
                    super.visitMethodCallExpression(expression)
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
