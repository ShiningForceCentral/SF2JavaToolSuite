/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.models.SelectionInterval;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class TableAction extends CustomAction<TableActionData> {
    
    public TableAction(Object owner, String operation, IActionable<TableActionData> action, TableActionData newValue, TableActionData oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    public TableAction(Object owner, String operation, IActionable<TableActionData> redoAction, TableActionData redoValue, IActionable<TableActionData> undoAction, TableActionData undoValue) {
        super(owner, operation, redoAction, redoValue, undoAction, undoValue);
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
    public boolean canBeCombined(IAction other) {
        if (!super.canBeCombined(other)) return false;
        TableAction otherTA = (TableAction)other;
        if (operation.equals("Delete Rows")) {
            return oldValue.selection().length == otherTA.oldValue.selection().length;
        } else {
            return newValue.selection().length == otherTA.newValue.selection().length;
        }
    }

    @Override
    public void combine(IAction other) {
        TableAction otherTA = (TableAction)other;        
        if (operation.startsWith("Shift")) {
            newValue = otherTA.newValue;
        } else {
            SelectionInterval[] selection = this.newValue.selection();
            SelectionInterval[] otherSelection = otherTA.newValue.selection();
            ArrayList combinedSelection = new ArrayList();
            boolean foundMatch;
            for (int o = 0; o < otherSelection.length; o++) {
                foundMatch = false;
                for (int s = 0; s < selection.length; s++) {
                    if ((otherSelection[o].start() >= selection[s].start() && otherSelection[o].start() <= selection[s].end()+1)
                    || (otherSelection[o].end() >= selection[s].start()-1 && otherSelection[o].end() <= selection[s].end())) {
                        foundMatch = true;
                        int start = selection[s].start() < otherSelection[o].start() ? selection[s].start() : otherSelection[o].start();
                        int end = selection[s].end() > otherSelection[o].end() ? selection[s].end() : otherSelection[o].end();
                        combinedSelection.add(new SelectionInterval(start, end));
                    }
                }
                if (!foundMatch) {
                    combinedSelection.add(otherSelection[o]);
                }
            }
            SelectionInterval[] newSelection = new SelectionInterval[combinedSelection.size()];
            newSelection = (SelectionInterval[])combinedSelection.toArray(newSelection);
            if (operation.startsWith("Delete")) {
                newValue = otherTA.newValue;
                oldValue = new TableActionData(this.oldValue.tableData(), newSelection);
            } else {
                newValue = new TableActionData(otherTA.newValue.tableData(), newSelection);
            }
        }
    }

    @Override
    protected String dataToString(TableActionData data) {
        StringBuilder sb = new StringBuilder();
        if (operation.startsWith("Delete")) {
            sb.append("Deleted: ");
        } else if (operation.startsWith("Shift")) {
            sb.append("Shifted: ");
        } else if (operation.startsWith("Add")) {
            sb.append("Added: ");
        } else if (operation.startsWith("Clone")) {
            sb.append("Cloned: ");
        }
        sb.append(data.toString());
        return sb.toString();
    }
}