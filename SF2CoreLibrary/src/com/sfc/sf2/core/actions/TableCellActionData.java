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
public class TableCellActionData implements IActionData<TableCellActionData> {
    private AbstractTableModel tableModel;
    private final int row;
    private final int column;
    private final Object data;

    public TableCellActionData(AbstractTableModel tableModel, int row, int column, Object data) {
        this.tableModel = tableModel;
        this.row = row;
        this.column = column;
        this.data = data;
    }

    public int row() {
        return row;
    }

    public int column() {
        return column;
    }

    public Object data() {
        return data;
    }

    @Override
    public boolean isInvalidated(TableCellActionData other) {
        return row == other.row && column == other.column && data.equals(other.data);
    }

    @Override
    public boolean canBeCombined(TableCellActionData other) {
        return row == other.row && column == other.column;
    }

    @Override
    public TableCellActionData combine(TableCellActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("[%s(%d)]: %s", tableModel.getColumnName(column), row, data == null ? "NULL" : data.toString());
    }
}
