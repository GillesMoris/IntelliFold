package com.github.gillesmoris.intellifold.actions

import com.github.gillesmoris.intellifold.settings.AppSettingsConfigurable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class ConfigureFoldingAction : AnAction("Configure Folding") {
    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.project, AppSettingsConfigurable::class.java)
    }
}
