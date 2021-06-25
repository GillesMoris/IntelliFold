package com.github.gillesmoris.intellifold.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext

open class DelegateActionGroup(val delegate: ActionGroup) : ActionGroup() {
    init {
        copyFrom(delegate)
        isEnabledInModalContext = delegate.isEnabledInModalContext
    }

    override fun isPopup(): Boolean {
        return delegate.isPopup
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return delegate.getChildren(e)
    }

    override fun update(e: AnActionEvent) {
        delegate.update(e)
    }

    override fun canBePerformed(context: DataContext): Boolean {
        return delegate.canBePerformed(context)
    }

    override fun actionPerformed(e: AnActionEvent) {
        delegate.actionPerformed(e)
    }

    override fun isDumbAware(): Boolean {
        return delegate.isDumbAware
    }

    override fun isTransparentUpdate(): Boolean {
        return delegate.isTransparentUpdate
    }

    override fun isInInjectedContext(): Boolean {
        return delegate.isInInjectedContext
    }

    override fun hideIfNoVisibleChildren(): Boolean {
        return delegate.hideIfNoVisibleChildren()
    }

    override fun disableIfNoVisibleChildren(): Boolean {
        return delegate.disableIfNoVisibleChildren()
    }
}
