/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.gui.controls.Table;
import com.sfc.sf2.core.models.AbstractTableModel;

/**
 *
 * @author TiMMy
 */
public class TableCellAction extends Action<ActionTableCellData> {
    
    private AbstractTableModel tableModel;
    
    public TableCellAction(AbstractTableModel tableModel, Table table, IActionable<ActionTableCellData> action, ActionTableCellData newValue, ActionTableCellData oldValue) {
        super(table, "Table Cell Value", action, newValue, oldValue);
        
        this.tableModel = tableModel;
    }

    @Override
    public boolean isInvalidated() {
        return newValue.row() == oldValue.row() && newValue.column() == oldValue.column() && newValue.data().equals(oldValue.data());
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        TableCellAction other = (TableCellAction)action;
        if (newValue.row() != other.newValue.row() || newValue.column() != other.newValue.column()) return false;
        return true;
    }

    @Override
    protected String dataToString(ActionTableCellData data) {
        return String.format("[%s(%d)]: %s", tableModel.getColumnName(data.column()), data.row(), data.data().toString());
    }
}
