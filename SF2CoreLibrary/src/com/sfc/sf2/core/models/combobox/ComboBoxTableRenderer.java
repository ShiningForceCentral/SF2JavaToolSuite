/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models.combobox;

import java.awt.Component;
import java.awt.FontMetrics;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author TiMMy
 */
public class ComboBoxTableRenderer extends DefaultTableCellRenderer {

    private static final String NOT_EMPTY = " ";
    private static final Icon ICON = UIManager.getIcon("Tree.expandedIcon");
    
    public ComboBoxTableRenderer() {
        setHorizontalTextPosition(SwingConstants.LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value == null || value.toString().isEmpty()) {
            //Hack to ensure that the dropdown arrow is not on the left of the cell when data is empty
            value = NOT_EMPTY;
        }
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table.isCellEditable(row, column)) {
            setIcon(ICON);
            FontMetrics met = super.getFontMetrics(super.getFont());
            int width = met.stringWidth(super.getText());
            super.setIconTextGap(table.getColumnModel().getColumn(column).getWidth()-width-18);
        } else {
            setIcon(null);
        }
        return comp;
    }
}
