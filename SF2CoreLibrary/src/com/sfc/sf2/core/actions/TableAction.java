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
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        TableAction other = (TableAction)action;
        if (operation.equals("Delete Rows")) {
            if (oldValue.selection().length != other.oldValue.selection().length) return false;
        } else {
            if (newValue.selection().length != other.newValue.selection().length) return false;
        }
        return true;
    }

    @Override
    public void combine(IAction action) {
        TableAction other = (TableAction)action;
        if (other == null) return;
        if (operation.startsWith("Delete")) {
            deleteCombine(other);
        } else if (operation.startsWith("Shift")) {
            shiftCombine(other);
        } else {
            addCombine(other);
        }
    }
    
    private void addCombine(TableAction other) {
        SelectionInterval[] selection = this.newValue.selection();
        SelectionInterval[] otherSelection = other.newValue.selection();
        for (int i = 0; i < selection.length; i++) {
            for (int o = 0; o < otherSelection.length; o++) {
                if (otherSelection != null && ((selection[i].start()-1 >= otherSelection[o].start() && selection[i].start()-1 <= otherSelection[o].end())
                    || (selection[i].end()+1 >= otherSelection[o].start() && selection[i].end()+1 <= otherSelection[o].end()))) {
                    int start = otherSelection[o].start() < selection[i].start() ? otherSelection[o].start() : selection[i].start();
                    int end = otherSelection[o].end() > selection[i].end() ? otherSelection[o].end() : selection[i].end();
                    otherSelection[o] = new SelectionInterval(start, end);
                }
            }
        }
        newValue = new ActionTableData(other.newValue.tableData(), otherSelection);
    }
    
    private void shiftCombine(TableAction other) {
        newValue = other.newValue;
    }
    
    private void deleteCombine(TableAction other) {
        SelectionInterval[] selection = other.oldValue.selection();
        SelectionInterval[] otherSelection = this.oldValue.selection();
        for (int i = 0; i < selection.length; i++) {
            for (int o = 0; o < otherSelection.length; o++) {
                if (otherSelection != null && ((selection[i].start()-1 >= otherSelection[o].start() && selection[i].start()-1 <= otherSelection[o].end())
                    || (selection[i].end()+1 >= otherSelection[o].start() && selection[i].end()+1 <= otherSelection[o].end()))) {
                    int start = otherSelection[o].start() < selection[i].start() ? otherSelection[o].start() : selection[i].start();
                    int end = otherSelection[o].end() > selection[i].end() ? otherSelection[o].end() : selection[i].end();
                    otherSelection[o] = new SelectionInterval(start, end);
                }
            }
        }
        newValue = other.newValue;
        oldValue = new ActionTableData(this.oldValue.tableData(), otherSelection);
    }

    @Override
    public boolean isInvalidated() {
        if (newValue.tableData().length != oldValue.tableData().length) return false;
        for (int i = 0; i < newValue.tableData().length; i++) {
            if (!newValue.tableData()[i].equals(oldValue.tableData()[i])) return false;
        }
        return true;
    }

    @Override
    protected String dataToString(ActionTableData data) {  
        return String.format("Rows: %d", data.tableData().length);
    }
}