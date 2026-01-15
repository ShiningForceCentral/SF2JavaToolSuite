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
        if (operation.startsWith("Shift")) {
            newValue = other.newValue;
        } else {
            SelectionInterval[] selection = this.newValue.selection();
            SelectionInterval[] otherSelection = other.newValue.selection();
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
                newValue = other.newValue;
                oldValue = new ActionTableData(this.oldValue.tableData(), newSelection);
            } else {
                newValue = new ActionTableData(other.newValue.tableData(), newSelection);
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

    @Override
    protected String dataToString(ActionTableData data) {
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
        for (int i = 0; i < data.selection().length; i++) {
            SelectionInterval interval = data.selection()[i];
            for (int j = interval.start(); j <= interval.end(); j++) {
                sb.append(j);
                if (i < data.selection().length - 1 || j < interval.end()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(" - Total Rows: ");
        sb.append(data.tableData().length);
        return sb.toString();
    }
}