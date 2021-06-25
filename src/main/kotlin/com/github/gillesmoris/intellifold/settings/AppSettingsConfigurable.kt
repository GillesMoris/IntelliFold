package com.github.gillesmoris.intellifold.settings

import com.github.gillesmoris.intellifold.services.ConfigurationPersistentStateComponent
import com.intellij.openapi.options.Configurable
import com.intellij.util.FileContentUtil
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class AppSettingsConfigurable : Configurable {
    private var mySettingsComponent: AppSettingsComponent? = null

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String? {
        return "Intellifold"
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
        val regexes = mySettingsComponent!!.regexes
        return state.list != regexes
    }

    override fun apply() {
        val oldRegexes = ConfigurationPersistentStateComponent.instance.state.list
        val newRegexes = mySettingsComponent!!.regexes
        ConfigurationPersistentStateComponent.instance.state.list = newRegexes
        if (ConfigurationPersistentStateComponent.instance.state.enabled && oldRegexes != newRegexes) {
            FileContentUtil.reparseOpenedFiles()
        }
    }

    override fun reset() {
        val state = ConfigurationPersistentStateComponent.instance.state
        mySettingsComponent?.regexes = state.list
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}