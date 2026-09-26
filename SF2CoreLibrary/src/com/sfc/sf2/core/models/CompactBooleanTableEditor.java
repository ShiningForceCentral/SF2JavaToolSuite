package com.sfc.sf2.core.models;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

/**
 * Only toggles a boolean cell when the click lands on the checkbox itself,
 * not on empty space in the cell. Clicking the rest of the row still selects it.
 *
 * @author TiMMy
 */
public class CompactBooleanTableEditor extends AbstractCellEditor implements TableCellEditor {

    private static final int CHECKBOX_HALF = 9;

    private final JCheckBox checkBox = new JCheckBox();

    public CompactBooleanTableEditor() {
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.setBorderPainted(false);
        checkBox.setOpaque(true);
        checkBox.addActionListener(e -> stopCellEditing());
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;
            Object source = me.getSource();
            if (!(source instanceof JTable)) {
                return false;
            }
            JTable table = (JTable) source;
            int row = table.rowAtPoint(me.getPoint());
            int col = table.columnAtPoint(me.getPoint());
            if (row < 0 || col < 0) {
                return false;
            }
            Rectangle cell = table.getCellRect(row, col, false);
            int cx = cell.x + cell.width / 2;
            int cy = cell.y + cell.height / 2;
            return Math.abs(me.getX() - cx) <= CHECKBOX_HALF && Math.abs(me.getY() - cy) <= CHECKBOX_HALF;
        }
        return true;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        checkBox.setSelected(Boolean.TRUE.equals(value));
        checkBox.setBackground(table.getSelectionBackground());
        checkBox.setForeground(table.getSelectionForeground());
        return checkBox;
    }

    @Override
    public Object getCellEditorValue() {
        return checkBox.isSelected();
    }
}
