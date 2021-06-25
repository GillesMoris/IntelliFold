package com.github.gillesmoris.intellifold.listeners

import com.github.gillesmoris.intellifold.services.ConfigurationPersistentStateComponent
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.javascript.psi.*
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.util.containers.toArray

class JavaScriptRegexFoldingBuilder() : AbstractRegexFoldingBuilder() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        println("JavaScriptRegexFoldingBuilder")
        if (quick) return emptyArray()
        if (!ConfigurationPersistentStateComponent.instance.state.enabled) return emptyArray()
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
        return descriptors.toArray(FoldingDescriptor.EMPTY)
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
                shouldFold = false;
            }
        })
        return shouldFold
    }

    fun shouldFoldCallExpression(node: JSCallExpression): Boolean {
        return shouldFoldCall(node.methodExpression!!.text);
    }

}
