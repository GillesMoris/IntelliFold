package com.github.gillesmoris.intellifold.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

const val ENABLE_TEXT = "Enable Folding"
const val DISABLE_TEXT = "Disable Folding"

class ToggleFoldingAction : AnAction(ENABLE_TEXT) {

    private var enabled = false

    override fun actionPerformed(event: AnActionEvent) {
        enabled = !enabled
    }

    override fun update(e: AnActionEvent) {
        e.presentation.text = if (enabled) DISABLE_TEXT else ENABLE_TEXT
    }
}
