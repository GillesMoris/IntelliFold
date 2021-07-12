package com.github.gillesmoris.intellifold.foldingbuilders.js

import com.github.gillesmoris.intellifold.foldingbuilders.AbstractCommentFoldingBuilder
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement

class JavaScriptCommentFoldingBuilder : AbstractCommentFoldingBuilder() {
    override fun visitComments(root: PsiElement, visitor: (PsiComment) -> Unit) {
        if (root.language.isKindOf("JavaScript")) {
            root.accept(object : JSRecursiveWalkingElementVisitor() {
                override fun visitComment(comment: PsiComment) {
                    visitor.invoke(comment)
                    super.visitComment(comment)
                }
            })
        }
    }
}
