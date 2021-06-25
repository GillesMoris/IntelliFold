package com.github.gillesmoris.intellifold.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.IconLoader

class FoldActionGroup: ActionGroup() {

    private val toggleFoldAction = ToggleFoldingAction()
    private val configureFoldAction = ConfigureFoldingAction()
    private val nonActiveIcon = IconLoader.getIcon("/icons/intellifold.svg")
    private val activeIcon = com.intellij.execution.runners.ExecutionUtil.getLiveIndicator(nonActiveIcon)

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return arrayOf(
            toggleFoldAction,
            configureFoldAction
        )
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.icon = if (toggleFoldAction.enabled) activeIcon else nonActiveIcon
    }
}