package com.github.gillesmoris.intellifold.actions

import com.github.gillesmoris.intellifold.services.ConfigurationPersistentStateComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.util.FileContentUtil

const val ENABLE_TEXT = "Enable Folding"
const val DISABLE_TEXT = "Disable Folding"

class ToggleFoldingAction : AnAction(ENABLE_TEXT) {

    var enabled = false

    override fun actionPerformed(event: AnActionEvent) {
        enabled = !enabled
        ConfigurationPersistentStateComponent.instance.state.enabled = enabled
        FileContentUtil.reparseOpenedFiles()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.text = if (enabled) DISABLE_TEXT else ENABLE_TEXT
    }
}
