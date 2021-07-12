package com.github.gillesmoris.intellifold.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(name = "com.github.gillesmoris.intellifold.settings", storages = [Storage("intellifold-plugin.xml")])
open class AppSettingsState : PersistentStateComponent<AppSettingsState.ConfigurationState> {

    companion object {
        fun getInstance(project: Project): AppSettingsState {
            return ServiceManager.getService(project, AppSettingsState::class.java)
        }
    }

    private var state: ConfigurationState = ConfigurationState()

    override fun getState(): ConfigurationState {
        return state
    }

    override fun loadState(state: ConfigurationState) {
        this.state = state
    }

    data class ConfigurationState(var enabled: Boolean = false, var list: List<String> = listOf())
}
