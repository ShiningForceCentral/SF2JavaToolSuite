/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.portrait.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;

/**
 *
 * @author TiMMy
 */
public class PortraitAction extends Action<ActionPortraitData> {
    
    public PortraitAction(Object owner, String operation, IActionable<ActionPortraitData> action, ActionPortraitData newValue, ActionPortraitData oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        if (newValue.row() != oldValue.row()) return false;
        for (int i = 0; i < 4; i++) {
            if (newValue.data()[i] != oldValue.data()[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        PortraitAction other = (PortraitAction)action;
        return this.newValue.row() == other.newValue.row();
    }

    @Override
    protected String dataToString(ActionPortraitData data) {
        if (data == null || data.data() == null || data.row() == -1) {
            return null;
        } else {
            int[] rowData = data.data();
            return String.format("(Row %d): %d, %d - %d, %d", data.row(), rowData[0], rowData[1], rowData[2], rowData[3]);
        }
    }
}
