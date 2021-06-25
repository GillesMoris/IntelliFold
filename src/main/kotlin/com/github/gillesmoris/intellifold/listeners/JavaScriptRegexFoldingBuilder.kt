package com.github.gillesmoris.intellifold.listeners

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.lang.javascript.psi.*
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.containers.toArray

class JavaScriptRegexFoldingBuilder() : AbstractRegexFoldingBuilder() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        println("JavaScriptRegexFoldingBuilder")
        if (quick) return emptyArray()
        val descriptors = mutableListOf<FoldingDescriptor>()
        if (root.language.isKindOf("JavaScript")) {
            root.accept(object : JSRecursiveWalkingElementVisitor() {
                override fun visitJSIfStatement(node: JSIfStatement) {
                    if (shouldFoldIf(node) == true) {
                        descriptors.add(FoldingDescriptor(node, makeRange(node)))
                        return
                    }
                    super.visitJSIfStatement(node)
                }

                override fun visitJSExpressionStatement(node: JSExpressionStatement) {
                    val expression = node.expression
                    if (expression is JSCallExpression && shouldFoldCall(expression)) {
                        descriptors.add(FoldingDescriptor(node, makeRange(node)))
                        return
                    }
                    super.visitJSExpressionStatement(node)
                }
            })
        }
        return descriptors.toArray(FoldingDescriptor.EMPTY)
    }

    fun shouldFoldIf(node: JSIfStatement): Boolean? {
        var shouldFold: Boolean? = null
        node.acceptChildren(object : JSElementVisitor() {
            override fun visitJSIfStatement(node: JSIfStatement) {
                shouldFold = when (shouldFold) {
                    false -> false
                    true -> shouldFoldIf(node) != false
                    null -> shouldFoldIf(node)
                }
            }

            override fun visitJSExpressionStatement(node: JSExpressionStatement) {
                val expression = node.expression
                shouldFold = if (expression is JSCallExpression) {
                    when (shouldFold) {
                        false -> false
                        else -> shouldFoldCall(expression)
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

    fun shouldFoldCall(node: JSCallExpression): Boolean {
        return node.methodExpression!!.text == "console.log" && node.parent is JSExpressionStatement;
    }

    companion object {
        fun makeRange(node: JSElement): TextRange {
            var start: PsiElement = node
            while (start.prevSibling is PsiWhiteSpace) {
                start = start.prevSibling
            }
            return TextRange(start.startOffset, node.endOffset)
        }
    }

}
