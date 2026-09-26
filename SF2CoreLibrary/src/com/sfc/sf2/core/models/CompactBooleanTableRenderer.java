package com.sfc.sf2.core.models;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * Centers the checkbox in its cell so the clickable glyph is obvious.
 *
 * @author TiMMy
 */
public class CompactBooleanTableRenderer extends JCheckBox implements TableCellRenderer {

    public CompactBooleanTableRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorderPainted(false);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setSelected(Boolean.TRUE.equals(value));
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }
}
