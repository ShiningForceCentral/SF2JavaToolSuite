/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models;

import com.sfc.sf2.core.settings.SettingsManager;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author TiMMy
 */
public class JDisableableTable extends JTable {
    
    private static Color DISABLED_COLOR;
    private static Color SELECTED_DISABLED_COLOR;
    private static Color UNFOCUSED_DISABLED_COLOR;

    private static void setupColors() {
        if (DISABLED_COLOR != null) return;
        boolean isDarkMode = SettingsManager.getGlobalSettings().getIsDarkTheme();
        DISABLED_COLOR = isDarkMode ? Color.GRAY : Color.GRAY;
        SELECTED_DISABLED_COLOR = isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY;
        UNFOCUSED_DISABLED_COLOR = isDarkMode ? Color.GRAY : Color.GRAY;
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);
        setupColors();
        boolean editable = (boolean)getModel().isCellEditable(row, column);
        boolean inFocus = this.hasFocus();
        if (isRowSelected(row)) {
            if (inFocus) {
                comp.setForeground(editable ? getSelectionForeground() : SELECTED_DISABLED_COLOR);
            } else {
                comp.setForeground(editable ? getForeground() : UNFOCUSED_DISABLED_COLOR);
            }
            comp.setBackground(getSelectionBackground());
        } else {
            comp.setForeground(editable ? getForeground() : DISABLED_COLOR);
            comp.setBackground(getBackground());
        }
        return comp;
    }
}
