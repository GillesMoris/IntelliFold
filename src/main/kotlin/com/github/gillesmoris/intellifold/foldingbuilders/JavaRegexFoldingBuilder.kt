package com.github.gillesmoris.intellifold.foldingbuilders

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.*

class JavaRegexFoldingBuilder() : AbstractRegexFoldingBuilder() {

    override fun buildFoldRegionsImpl(root: PsiElement, document: Document): List<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        if (root.language.isKindOf("JAVA")) {
            root.accept(object : JavaRecursiveElementVisitor() {
                override fun visitIfStatement(statement: PsiIfStatement) {
                    if (shouldFoldIfStatement(statement) == true) {
                        descriptors.add(FoldingDescriptor(statement, makeRange(statement)))
                        return
                    }
                    super.visitIfStatement(statement)
                }

                override fun visitExpressionStatement(statement: PsiExpressionStatement) {
                    val expression = statement.expression
                    if (expression is PsiMethodCallExpression && shouldFoldCallExpression(expression)) {
                        descriptors.add(FoldingDescriptor(statement, makeRange(statement)))
                        return
                    }
                    super.visitExpressionStatement(statement)
                }
            })
        }
        return descriptors
    }

    fun shouldFoldIfStatement(statement: PsiIfStatement): Boolean? {
        var shouldFold: Boolean? = null
        statement.acceptChildren(object : JavaElementVisitor() {
            override fun visitIfStatement(statement: PsiIfStatement) {
                shouldFold = when (shouldFold) {
                    false -> false
                    true -> shouldFoldIfStatement(statement) != false
                    null -> shouldFoldIfStatement(statement)
                }
            }

            override fun visitExpressionStatement(statement: PsiExpressionStatement) {
                val expression = statement.expression
                shouldFold = if (expression is PsiMethodCallExpression) {
                    when (shouldFold) {
                        false -> false
                        else -> shouldFoldCallExpression(expression)
                    }
                } else {
                    false
                }
            }

            override fun visitBlockStatement(statement: PsiBlockStatement) {
                statement.codeBlock.acceptChildren(this)
            }

            override fun visitStatement(statement: PsiStatement) {
                shouldFold = false;
            }
        })
        return shouldFold
    }

    fun shouldFoldCallExpression(node: PsiMethodCallExpression): Boolean {
        return shouldFoldCall(node.methodExpression.text);
    }

}