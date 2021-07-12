package com.github.gillesmoris.intellifold.settings

import com.intellij.openapi.ui.Messages
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.JBColor
import com.intellij.ui.ListUtil
import com.intellij.ui.ScrollingUtil
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.DefaultListModel
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ListSelectionModel

const val NR_TITLE_INSETS = 8
val TITLE_INSETS = JBUI.insetsTop(NR_TITLE_INSETS)

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    private val regexesModel = DefaultListModel<String>()
    private val regexField = RegexPanel(regexesModel)
    val panel: JPanel = FormBuilder.createFormBuilder()
        .addComponentFillVertically(regexField, 1)
        .panel

    var regexes: List<String>
        get() {
            return regexesModel.elements().toList()
        }
        set(value) {
            regexesModel.clear()
            value.forEach {
                regexesModel.addElement(it)
            }
        }

    fun getPreferredFocusedComponent(): JComponent {
        return regexField
    }

    // Shamelessly stolen from IntelliJ Community
    // https://github.com/JetBrains/intellij-community/blob/cefe3d90e0d1371f05a7555e3fdbdcef2c70c647/platform/lang-impl/src/com/intellij/openapi/fileTypes/impl/FileTypeConfigurable.java#L630
    internal class RegexPanel(private val model: DefaultListModel<String>) : JPanel() {
        private val list = JBList(model)

        init {
            layout = BorderLayout()
            list.selectionMode = ListSelectionModel.SINGLE_SELECTION
            list.setEmptyText("No regexes")
            list.border = JBUI.Borders.empty()
            val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(list)
                .setScrollPaneBorder(JBUI.Borders.empty())
                .setPanelBorder(JBUI.Borders.customLine(JBColor.border(), 1, 1, 0, 1))
                .setAddAction { _ -> addRegex() }
                .setAddActionName("Add Regex")
                .setEditAction { _ -> editRegex() }
                .setRemoveAction { _ -> removeRegex() }
                .disableUpDownActions()
            add(decorator.createPanel(), BorderLayout.NORTH)
            val scrollPane: JScrollPane = JBScrollPane(list)
            add(scrollPane, BorderLayout.CENTER)
            scrollPane.border = JBUI.Borders.customLine(JBColor.border(), 0, 1, 1, 1)
            border = IdeBorderFactory.createTitledBorder("Regexes:", false, TITLE_INSETS).setShowLine(false)
        }

        private fun addRegex() {
            val newRegex = Messages.showInputDialog(list, "Enter regex to fold", "Add Regex", null, null, null)
            if (newRegex.isNullOrEmpty()) {
                return // canceled or empty
            }
            model.addElement(newRegex)
            ScrollingUtil.selectItem(list, newRegex)
        }

        private fun editRegex() {
            val oldRegex = list.selectedValue ?: return
            val oldIndex = list.selectedIndex
            val newRegex = Messages.showInputDialog(list, "Enter regex to fold", "Edit Regex", null, oldRegex, null)
            if (newRegex.isNullOrEmpty()) {
                return // canceled or empty
            }
            val model = model
            model.set(oldIndex, newRegex)
            ScrollingUtil.selectItem(list, newRegex)
        }

        private fun removeRegex() {
            ListUtil.removeSelectedItems(list)
        }
    }
}
