package com.github.gillesmoris.intellifold.foldingbuilders.js

import com.github.gillesmoris.intellifold.foldingbuilders.AbstractRegexFoldingBuilder
import com.github.gillesmoris.intellifold.foldingbuilders.makeRange
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.javascript.psi.JSBlockStatement
import com.intellij.lang.javascript.psi.JSCallExpression
import com.intellij.lang.javascript.psi.JSElementVisitor
import com.intellij.lang.javascript.psi.JSExpressionStatement
import com.intellij.lang.javascript.psi.JSIfStatement
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor
import com.intellij.lang.javascript.psi.JSStatement
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement

class JavaScriptRegexFoldingBuilder : AbstractRegexFoldingBuilder() {

    override fun buildFoldRegionsImpl(root: PsiElement, document: Document): List<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        if (root.language.isKindOf("JavaScript")) {
            root.accept(object : JSRecursiveWalkingElementVisitor() {
                override fun visitJSIfStatement(node: JSIfStatement) {
                    if (shouldFoldIfStatement(node) == true) {
                        descriptors.add(FoldingDescriptor(node, makeRange(node)))
                        return
                    }
                    super.visitJSIfStatement(node)
                }

                override fun visitJSExpressionStatement(node: JSExpressionStatement) {
                    val expression = node.expression
                    if (expression is JSCallExpression && shouldFoldCallExpression(expression)) {
                        descriptors.add(FoldingDescriptor(node, makeRange(node)))
                        return
                    }
                    super.visitJSExpressionStatement(node)
                }
            })
        }
        return descriptors
    }

    fun shouldFoldIfStatement(node: JSIfStatement): Boolean? {
        var shouldFold: Boolean? = null
        node.acceptChildren(object : JSElementVisitor() {
            override fun visitJSIfStatement(node: JSIfStatement) {
                shouldFold = when (shouldFold) {
                    false -> false
                    true -> shouldFoldIfStatement(node) != false
                    null -> shouldFoldIfStatement(node)
                }
            }

            override fun visitJSExpressionStatement(node: JSExpressionStatement) {
                val expression = node.expression
                shouldFold = if (expression is JSCallExpression) {
                    when (shouldFold) {
                        false -> false
                        else -> shouldFoldCallExpression(expression)
                    }
                } else {
                    false
                }
            }

            override fun visitJSBlock(node: JSBlockStatement) {
                node.acceptChildren(this)
            }

            override fun visitJSStatement(node: JSStatement) {
                shouldFold = false
            }
        })
        return shouldFold
    }

    fun shouldFoldCallExpression(node: JSCallExpression): Boolean {
        return shouldFoldCall(node.methodExpression!!.text)
    }
}
