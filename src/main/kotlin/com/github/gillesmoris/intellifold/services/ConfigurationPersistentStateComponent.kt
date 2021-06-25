package com.github.gillesmoris.intellifold.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "ConfigurationPersistentStateComponent", storages = [Storage("intellifold-plugin.xml")])
open class ConfigurationPersistentStateComponent :
    PersistentStateComponent<ConfigurationPersistentStateComponent.ConfigurationState> {

    // this is how we're going to call the component from different classes
    companion object {
        val instance: ConfigurationPersistentStateComponent
            get() = ServiceManager.getService(ConfigurationPersistentStateComponent::class.java)
    }

    private var reminderState: ConfigurationState = ConfigurationState(listOf())

    override fun getState(): ConfigurationState {
        return reminderState
    }

    override fun loadState(state: ConfigurationState) {
        reminderState = state
    }

    class ConfigurationState(var list: List<String>)
}
