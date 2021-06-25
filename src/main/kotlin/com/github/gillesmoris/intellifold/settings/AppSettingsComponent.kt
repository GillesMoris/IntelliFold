package com.github.gillesmoris.intellifold.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    val panel: JPanel
    private val regexField = JBTextField()

    fun getPreferredFocusedComponent(): JComponent {
        return regexField
    }

    init {
        panel = FormBuilder.createFormBuilder()
            .addComponent(JBLabel("Enter one Regex per line: "))
            .addComponent( regexField, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}