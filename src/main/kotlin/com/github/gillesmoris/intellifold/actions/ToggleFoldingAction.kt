package com.github.gillesmoris.intellifold.actions

import com.github.gillesmoris.intellifold.services.ConfigurationPersistentStateComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.IconLoader
import com.intellij.util.FileContentUtil

const val ENABLE_TEXT = "Enable Folding"
const val DISABLE_TEXT = "Disable Folding"

val nonActiveIcon = IconLoader.getIcon("/icons/intellifold.svg")
val activeIcon = IconLoader.getIcon("/icons/intellifold_live.svg")

class ToggleFoldingAction : AnAction(ENABLE_TEXT, "", nonActiveIcon) {

    var enabled = false

    override fun actionPerformed(event: AnActionEvent) {
        enabled = !enabled
        ConfigurationPersistentStateComponent.instance.state.enabled = enabled
        FileContentUtil.reparseOpenedFiles()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.text = if (enabled) DISABLE_TEXT else ENABLE_TEXT
        e.presentation.icon = if (enabled) activeIcon else nonActiveIcon
    }
}
