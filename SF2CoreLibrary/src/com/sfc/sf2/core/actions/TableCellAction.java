/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.gui.controls.Table;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author TiMMy
 */
public class TableCellAction extends CustomAction<TableCellActionData> {
    
    public TableCellAction(Object owner, String operation, IActionable<TableCellActionData> action, TableCellActionData newValue, TableCellActionData oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public void execute() {
        ClearTableCellEditor();
        super.execute();
    }

    @Override
    public void undo() {
        ClearTableCellEditor();
        super.undo();
    }
    
    private void ClearTableCellEditor() {
        JTable table = ((Table)owner).jTable;
        TableCellEditor editor = table.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
        table.requestFocus();
    }
}
