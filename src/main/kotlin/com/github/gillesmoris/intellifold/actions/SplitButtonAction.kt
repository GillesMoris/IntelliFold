package com.github.gillesmoris.intellifold.actions

import com.intellij.icons.AllIcons
import com.intellij.ide.HelpTooltip
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.ActionUtil
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.actionSystem.ex.CustomComponentAction
import com.intellij.openapi.actionSystem.impl.ActionButton
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.messages.SimpleMessageBusConnection
import com.intellij.util.ui.JBInsets
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.StartupUiUtil
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.geom.Area
import javax.swing.JComponent

/**
 * Based on com.intellij.openapi.actionSystem.SplitButtonAction
 * Modified to preserve first action from group as selected action.
 */
class SplitButtonAction(private val myActionGroup: ActionGroup) : ActionGroup(), CustomComponentAction {

    init {
        isPopup = true
    }

    override fun actionPerformed(e: AnActionEvent) {}

    override fun update(e: AnActionEvent) {
        myActionGroup.update(e)
        val presentation = e.presentation
        if (presentation.isVisible) {
            val component = presentation.getClientProperty(CustomComponentAction.COMPONENT_KEY)
            if (component is SplitButton) {
                component.update(e)
            }
        }
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return myActionGroup.getChildren(e)
    }

    override fun isDumbAware(): Boolean {
        return myActionGroup.isDumbAware
    }

    override fun createCustomComponent(presentation: Presentation, place: String): JComponent {
        return SplitButton(this, presentation, place, myActionGroup)
    }

    private class SplitButton constructor(
        action: AnAction,
        presentation: Presentation,
        place: String,
        private val myActionGroup: ActionGroup
    ) :
        ActionButton(action, presentation, place, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE) {
        private enum class MousePressType {
            Action, Popup, None
        }

        private var selectedAction: AnAction? = null
        private var actionEnabled = true
        private var mousePressType = MousePressType.None
        private var myConnection: SimpleMessageBusConnection? = null

        init {
            val actions = myActionGroup.getChildren(null)
            if (actions.isNotEmpty()) {
                selectedAction = actions[0]
                copyPresentation(selectedAction!!.templatePresentation)
            }
        }

        private fun copyPresentation(presentation: Presentation) {
            myPresentation.copyFrom(presentation)
            actionEnabled = presentation.isEnabled
            myPresentation.isEnabled = true
            myPresentation.putClientProperty(CustomComponentAction.COMPONENT_KEY, this)
        }

        override fun getPreferredSize(): Dimension {
            val size = super.getPreferredSize()
            size.width += ARROW_DOWN.iconWidth + JBUIScale.scale(7)
            return size
        }

        private fun selectedActionEnabled(): Boolean {
            return selectedAction != null && actionEnabled
        }

        override fun paintComponent(g: Graphics) {
            val look = buttonLook
            if (selectedActionEnabled() || !StartupUiUtil.isUnderDarcula()) {
                var state = popState
                if (state == PUSHED) state = POPPED
                look.paintBackground(g, this, state)
            }
            val baseRect = Rectangle(size)
            JBInsets.removeFrom(baseRect, insets)
            if (popState == PUSHED && mousePressType != MousePressType.None && selectedActionEnabled() || isToggleActionPushed) {
                val arrowWidth = ARROW_DOWN.iconWidth + JBUIScale.scale(7)
                val clip = g.clip
                val buttonClip = Area(clip)
                val execButtonRect = Rectangle(baseRect.x, baseRect.y, baseRect.width - arrowWidth, baseRect.height)
                if (mousePressType == MousePressType.Action || isToggleActionPushed) {
                    buttonClip.intersect(Area(execButtonRect))
                } else if (mousePressType == MousePressType.Popup) {
                    val arrowButtonRect =
                        Rectangle(execButtonRect.x + execButtonRect.width, baseRect.y, arrowWidth, baseRect.height)
                    buttonClip.intersect(Area(arrowButtonRect))
                }
                g.clip = buttonClip
                look.paintBackground(g, this, PUSHED)
                g.clip = clip
            }
            var x = baseRect.x + baseRect.width - JBUIScale.scale(3) - ARROW_DOWN.iconWidth
            var y = baseRect.y + (baseRect.height - ARROW_DOWN.iconHeight) / 2 + JBUIScale.scale(1)
            look.paintIcon(g, this, ARROW_DOWN, x, y)
            x -= JBUIScale.scale(4)
            if (popState == POPPED || popState == PUSHED) {
                g.color = JBUI.CurrentTheme.ActionButton.hoverSeparatorColor()
                g.fillRect(x, baseRect.y, JBUIScale.scale(1), baseRect.height)
            }
            var actionIcon = icon
            if (!selectedActionEnabled()) {
                val disabledIcon = myPresentation.disabledIcon
                actionIcon =
                    if (disabledIcon != null || actionIcon == null) disabledIcon else IconLoader.getDisabledIcon(
                        actionIcon
                    )
                if (actionIcon == null) {
                    actionIcon = getFallbackIcon(false)
                }
            }
            x = baseRect.x + (x - actionIcon!!.iconWidth) / 2
            y = baseRect.y + (baseRect.height - actionIcon.iconHeight) / 2
            look.paintIcon(g, this, actionIcon, x, y)
        }

        private val isToggleActionPushed: Boolean
            get() = selectedAction is Toggleable && Toggleable.isSelected(myPresentation)

        override fun onMousePressed(e: MouseEvent) {
            val baseRect = Rectangle(size)
            JBInsets.removeFrom(baseRect, insets)
            val arrowWidth = ARROW_DOWN.iconWidth + JBUIScale.scale(7)
            val execButtonRect = Rectangle(baseRect.x, baseRect.y, baseRect.width - arrowWidth, baseRect.height)
            val arrowButtonRect =
                Rectangle(execButtonRect.x + execButtonRect.width, baseRect.y, arrowWidth, baseRect.height)
            val p = e.point
            mousePressType =
                if (execButtonRect.contains(p)) MousePressType.Action else if (arrowButtonRect.contains(p)) MousePressType.Popup else MousePressType.None
        }

        override fun actionPerformed(event: AnActionEvent) {
            HelpTooltip.hide(this)
            if (mousePressType == MousePressType.Popup) {
                showPopupMenu(event, myActionGroup)
            } else if (selectedActionEnabled()) {
                val newEvent = AnActionEvent.createFromInputEvent(
                    event.inputEvent, myPlace, event.presentation,
                    dataContext
                )
                ActionUtil.performActionDumbAware(selectedAction, newEvent)
            }
        }

        override fun showPopupMenu(event: AnActionEvent, actionGroup: ActionGroup) {
            if (myPopupState.isRecentlyHidden) return  // do not show new popup
            val am = ActionManager.getInstance() as ActionManagerImpl
            val popupMenu = am.createActionPopupMenu(event.place, actionGroup, object : MenuItemPresentationFactory() {
                override fun processPresentation(presentation: Presentation) {
                    super.processPresentation(presentation)
                    @Suppress("UnstableApiUsage")
                    if (StringUtil.defaultIfEmpty(
                            presentation.text,
                            ""
                        ) == myPresentation.text && StringUtil.defaultIfEmpty(
                            presentation.description,
                            ""
                        ) == myPresentation.description
                    ) {
                        presentation.isEnabled = selectedActionEnabled()
                        //presentation.putClientProperty(Toggleable.SELECTED_PROPERTY, myPresentation.getClientProperty(Toggleable.SELECTED_PROPERTY));
                    }
                }
            })
            popupMenu.setTargetComponent(this)
            val menu = popupMenu.component
            menu.addPopupMenuListener(myPopupState)
            if (event.isFromActionToolbar) {
                menu.show(this, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE.width + insets.left, height)
            } else {
                menu.show(this, width, 0)
            }
            HelpTooltip.setMasterPopupOpenCondition(this) { !menu.isVisible }
        }

        override fun addNotify() {
            super.addNotify()
            myConnection = ApplicationManager.getApplication().messageBus.simpleConnect()
            myConnection!!.subscribe(AnActionListener.TOPIC, object : AnActionListener {
                override fun beforeActionPerformed(action: AnAction, dataContext: DataContext, event: AnActionEvent) {
                    if (dataContext.getData(PlatformDataKeys.CONTEXT_COMPONENT) === this@SplitButton) {
                        if (action == selectedAction) {
                            update(event)
                            repaint()
                        } else {
                            action.update(event)
                        }
                    }
                }
            })
        }

        override fun removeNotify() {
            super.removeNotify()
            if (myConnection != null) {
                myConnection!!.disconnect()
                myConnection = null
            }
        }

        fun update(event: AnActionEvent) {
            if (selectedAction != null) {
                selectedAction!!.update(event)
                copyPresentation(event.presentation)
            }
        }

        companion object {
            private val ARROW_DOWN = AllIcons.General.ButtonDropTriangle
        }
    }
}
