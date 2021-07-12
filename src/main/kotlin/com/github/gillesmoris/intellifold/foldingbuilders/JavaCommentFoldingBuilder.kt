package com.github.gillesmoris.intellifold.foldingbuilders

import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement

class JavaCommentFoldingBuilder : AbstractCommentFoldingBuilder() {
    override fun visitComments(root: PsiElement, visitor: (PsiComment) -> Unit) {
        if (root.language.isKindOf("JAVA")) {
            root.accept(object : JavaRecursiveElementVisitor() {
                override fun visitComment(comment: PsiComment) {
                    visitor.invoke(comment)
                    super.visitComment(comment)
                }
            })
        }
    }
}
