/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models;

import com.sfc.sf2.core.settings.SettingsManager;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author TiMMy
 */
public class JDisableableTable extends JTable {
    private static Color DISABLED_COLOR;
    private static Color SELECTED_DISABLED_COLOR;
    private static Color UNFOCUSED_DISABLED_COLOR;
    private static MatteBorder BORDER;
    
    private boolean drawBorder = false;
    private boolean horizontalScrolling = false;

    private static void setupColors() {
        if (DISABLED_COLOR != null) return;
        boolean isDarkMode = SettingsManager.getGlobalSettings().getIsDarkTheme();
        DISABLED_COLOR = isDarkMode ? Color.GRAY : Color.GRAY;
        SELECTED_DISABLED_COLOR = isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY;
        UNFOCUSED_DISABLED_COLOR = isDarkMode ? Color.GRAY : Color.GRAY;
        BORDER = new MatteBorder(1, 0, 1, 0, UNFOCUSED_DISABLED_COLOR);
    }

    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    /**
     * This is a fix for default JTable behaviour where they will not show the horizontal scroll bar even if the table is too small.
     * In most cases, though, you prefer the table to resize to fit the viewport.
     */
    public void setHorizontalScrolling(boolean horizontalScrolling) {
        this.horizontalScrolling = horizontalScrolling;
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        JComponent comp = (JComponent)super.prepareRenderer(renderer, row, column);
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
        if (drawBorder) {
            comp.setBorder(BORDER);
        }
        return comp;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        if (horizontalScrolling) {
            return getPreferredSize().width < getParent().getParent().getWidth();
        } else {
            return true;
        }
    }
}
