package com.github.gillesmoris.intellifold.actions

import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.SplitButtonAction
import com.intellij.openapi.actionSystem.ex.CustomComponentAction
import javax.swing.JComponent

class SplitFoldActionGroup :
    DelegateActionGroup(SplitButtonAction(DefaultActionGroup(ToggleFoldingAction(), ConfigureFoldingAction()))),
    CustomComponentAction {
    override fun createCustomComponent(presentation: Presentation, place: String): JComponent {
        if (delegate is CustomComponentAction) {
            return delegate.createCustomComponent(presentation, place)
        }
        return super.createCustomComponent(presentation, place)
    }
}
