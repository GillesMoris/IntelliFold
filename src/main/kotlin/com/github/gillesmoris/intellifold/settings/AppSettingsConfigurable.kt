package com.github.gillesmoris.intellifold.settings

import com.github.gillesmoris.intellifold.services.ConfigurationPersistentStateComponent
import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class AppSettingsConfigurable : Configurable {
    private var mySettingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String? {
        return "SDK: Application Settings Example"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent?.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = AppSettingsComponent()
        return mySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val state = ConfigurationPersistentStateComponent.instance.state
        val regexField = mySettingsComponent!!.regexField
        return regexField.text.trim() != state.list.joinToString("\n")
    }

    override fun apply() {
        val regexField = mySettingsComponent!!.regexField
        ConfigurationPersistentStateComponent.instance.state.list = regexField.text.trim().lines()
    }

    override fun reset() {
        val state = ConfigurationPersistentStateComponent.instance.state
        mySettingsComponent?.regexField?.text = state.list.joinToString("\n")
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}