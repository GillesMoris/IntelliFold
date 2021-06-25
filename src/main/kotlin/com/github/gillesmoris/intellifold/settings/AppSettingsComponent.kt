package com.github.gillesmoris.intellifold.settings

import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    val regexField = JBTextArea(null, "", 10, 1);
    val panel: JPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Enter one Regex per line: ", regexField, 1, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel

    fun getPreferredFocusedComponent(): JComponent {
        return regexField
    }
}