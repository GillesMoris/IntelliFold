package com.github.gillesmoris.intellifold.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "AppSettingsState", storages = [Storage("intellifold-plugin.xml")])
open class AppSettingsState :
        PersistentStateComponent<AppSettingsState.ConfigurationState> {

    // this is how we're going to call the component from different classes
    companion object {
        val instance: AppSettingsState
            get() = ServiceManager.getService(AppSettingsState::class.java)
    }

    private var reminderState: ConfigurationState = ConfigurationState(false, listOf())

    override fun getState(): ConfigurationState {
        return reminderState
    }

    override fun loadState(state: ConfigurationState) {
        reminderState = ConfigurationState(false, state.list)
    }

    class ConfigurationState(var enabled: Boolean = false, var list: List<String>)
}
