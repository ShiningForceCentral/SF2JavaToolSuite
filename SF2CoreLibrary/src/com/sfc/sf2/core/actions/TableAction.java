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
public class TableAction extends Action<ActionTableData> {
    
    public TableAction(Object owner, String operation, IActionable<ActionTableData> action, ActionTableData newValue, ActionTableData oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    public TableAction(Object owner, String operation, IActionable<ActionTableData> redoAction, ActionTableData redoValue, IActionable<ActionTableData> undoAction, ActionTableData undoValue) {
        super(owner, operation, redoAction, redoValue, undoAction, undoValue);
    }

    @Override
    public void combine(IAction action) {
        TableAction other = (TableAction)action;
        if (other == null) return;
        SelectionInterval[] selection = newValue.selection();
        SelectionInterval[] otherSelection = other.newValue.selection();
        for (int i = 0; i < selection.length; i++) {
            for (int o = 0; o < otherSelection.length; o++) {
                if (otherSelection != null && ((selection[i].start()-1 >= otherSelection[o].start() && selection[i].start()-1 <= otherSelection[o].end())
                    || (selection[i].end()+1 >= otherSelection[o].start() && selection[i].end()+1 <= otherSelection[o].end()))) {
                    int start = otherSelection[o].start() < selection[i].start() ? otherSelection[o].start() : selection[i].start();
                    int end = otherSelection[o].end() > selection[i].end() ? otherSelection[o].end() : selection[i].end();
                    selection[i] = new SelectionInterval(start, end);
                    otherSelection[o] = null;
                }
            }
            if (i > 0) {
                if (selection[i-1] != null && ((selection[i].start()-1 >= selection[i-1].start() && selection[i].start()-1 <= selection[i-1].end())
                    || (selection[i].end()+1 >= selection[i-1].start() && selection[i].end()+1 <= selection[i-1].end()))) {
                    int start = selection[i-1].start() < selection[i].start() ? selection[i-1].start() : selection[i].start();
                    int end = selection[i-1].end() > selection[i].end() ? selection[i-1].end() : selection[i].end();
                    selection[i-1] = new SelectionInterval(start, end);
                    selection[i] = null;
                }
            }
        }
    }

    @Override
    public boolean isInvalidated() {
        if (newValue.tableData().length != oldValue.tableData().length) return false;
        for (int i = 0; i < newValue.tableData().length; i++) {
            if (!newValue.tableData()[i].equals(oldValue.tableData()[i])) return false;
        }
        return true;
    }
}