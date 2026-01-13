/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class TableCellAction extends Action<ActionTableCellData> {
    
    AbstractTableModel tableModel;
    
    public TableCellAction(AbstractTableModel tableModel, IActionable<ActionTableCellData> action, ActionTableCellData newValue, ActionTableCellData oldValue) {
        super(tableModel, "Table Cell Value", action, newValue, oldValue);
        
        this.tableModel = tableModel;
    }

    @Override
    public boolean isInvalidated() {
        return newValue.data().equals(oldValue.data());
    }

    @Override
    protected String dataToString(ActionTableCellData data) {
        return String.format("[%s(%d)]: %s", tableModel.getColumnName(data.column()), data.row(), data.data().toString());
    }
}
