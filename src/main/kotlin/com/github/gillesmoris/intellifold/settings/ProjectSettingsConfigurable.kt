package com.github.gillesmoris.intellifold.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.util.FileContentUtil
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class ProjectSettingsConfigurable(private val project: Project) : Configurable {
    private var mySettingsComponent: ProjectSettingsComponent? = null

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "IntelliFold"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent?.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent {
        mySettingsComponent = ProjectSettingsComponent(project)
        return mySettingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val state = ProjectSettingsState.getInstance(project).state
        val regexes = mySettingsComponent!!.regexes
        return state.list != regexes
    }

    override fun apply() {
        val settings = ProjectSettingsState.getInstance(project)
        val oldRegexes = settings.state.list
        val newRegexes = mySettingsComponent!!.regexes
        settings.state.list = newRegexes
        if (settings.state.enabled && oldRegexes != newRegexes) {
            FileContentUtil.reparseOpenedFiles()
        }
    }

    override fun reset() {
        val state = ProjectSettingsState.getInstance(project).state
        mySettingsComponent?.regexes = state.list
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
