package com.github.gillesmoris.intellifold.actions

import com.github.gillesmoris.intellifold.settings.AppSettingsState
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.IconLoader
import com.intellij.util.FileContentUtil

const val ENABLE_TEXT = "Enable Folding"
const val DISABLE_TEXT = "Disable Folding"

class ToggleFoldingAction : AnAction(ENABLE_TEXT, "", nonActiveIcon) {

    override fun actionPerformed(event: AnActionEvent) {
        AppSettingsState.instance.state.enabled = !AppSettingsState.instance.state.enabled
        FileContentUtil.reparseOpenedFiles()
    }

    override fun update(e: AnActionEvent) {
        val enabled = AppSettingsState.instance.state.enabled;
        e.presentation.text = if (enabled) DISABLE_TEXT else ENABLE_TEXT
        e.presentation.icon = if (enabled) activeIcon else nonActiveIcon
    }

    companion object {
        val nonActiveIcon = IconLoader.getIcon("/icons/intellifold.svg", ToggleFoldingAction::class.java)
        val activeIcon = IconLoader.getIcon("/icons/intellifold_live.svg", ToggleFoldingAction::class.java)
    }
}
