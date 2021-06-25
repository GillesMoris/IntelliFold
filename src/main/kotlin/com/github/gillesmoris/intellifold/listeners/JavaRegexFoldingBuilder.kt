package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.*
import com.intellij.util.containers.toArray

class JavaRegexFoldingBuilder() : AbstractRegexFoldingBuilder() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        println("JavaRegexFoldingBuilder")
        if (quick) return emptyArray()
        val descriptors = mutableListOf<FoldingDescriptor>()
        if (root.language.isKindOf("JAVA")) {
            root.accept(object : JavaRecursiveElementVisitor() {
                override fun visitIfStatement(statement: PsiIfStatement) {
                    if (shouldFoldIf(statement) == true) {
                        descriptors.add(FoldingDescriptor(statement, makeRange(statement)))
                        return
                    }
                    super.visitIfStatement(statement)
                }

                override fun visitExpressionStatement(statement: PsiExpressionStatement) {
                    val expression = statement.expression
                    if (expression is PsiMethodCallExpression && shouldFoldCall(expression)) {
                        descriptors.add(FoldingDescriptor(statement, makeRange(statement)))
                        return
                    }
                    super.visitExpressionStatement(statement)
                }
            })
        }
        return descriptors.toArray(FoldingDescriptor.EMPTY)
    }

    fun shouldFoldIf(statement: PsiIfStatement): Boolean? {
        var shouldFold: Boolean? = null
        statement.acceptChildren(object : JavaElementVisitor() {
            override fun visitIfStatement(statement: PsiIfStatement) {
                shouldFold = when (shouldFold) {
                    false -> false
                    true -> shouldFoldIf(statement) != false
                    null -> shouldFoldIf(statement)
                }
            }

            override fun visitExpressionStatement(statement: PsiExpressionStatement) {
                val expression = statement.expression
                shouldFold = if (expression is PsiMethodCallExpression) {
                    when (shouldFold) {
                        false -> false
                        else -> shouldFoldCall(expression)
                    }
                } else {
                    false
                }
            }

            override fun visitBlockStatement(statement: PsiBlockStatement) {
                statement.acceptChildren(this)
            }

            override fun visitStatement(statement: PsiStatement) {
                shouldFold = false;
            }
        })
        return shouldFold
    }

    fun shouldFoldCall(node: PsiMethodCallExpression): Boolean {
        return node.methodExpression.text == "System.out.println" && node.parent is PsiExpressionStatement;
    }

}
