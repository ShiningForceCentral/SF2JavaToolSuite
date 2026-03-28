/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.models.SelectionInterval;

/**
 *
 * @author TiMMy
 */
public class TableActionData implements IActionData<TableActionData> {
    private Object[] tableData;
    private SelectionInterval[] selection;

    public TableActionData(Object[] tableData, SelectionInterval[] selection) {
        this.tableData = tableData;
        this.selection = selection;
    }

    public Object[] tableData() {
        return tableData;
    }

    public SelectionInterval[] selection() {
        return selection;
    }
    

    @Override
    public boolean canBeCombined(TableActionData other) {
        return selection.length == other.selection.length;
    }

    @Override
    public TableActionData combine(TableActionData other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInvalidated(TableActionData other) {
        if (tableData.length != other.tableData.length) return false;
        for (int i = 0; i < tableData.length; i++) {
            if (!tableData[i].equals(other.tableData[i])) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selection.length; i++) {
            SelectionInterval interval = selection[i];
            for (int j = interval.start(); j <= interval.end(); j++) {
                sb.append(j);
                if (i < selection.length - 1 || j < interval.end()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(" - Total Rows: ");
        sb.append(tableData.length);
        return sb.toString();
    }
}
