/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;

/**
 *
 * @author TiMMy
 */
public class ChangeBlockAction extends Action<ActionBlockChange> {

    public ChangeBlockAction(Object owner, String operation, IActionable<ActionBlockChange> action, ActionBlockChange newValue, ActionBlockChange oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    public ChangeBlockAction(Object owner, String operation, IActionable<ActionBlockChange> redoAction, ActionBlockChange redoValue, IActionable<ActionBlockChange> undoAction, ActionBlockChange undoValue) {
        super(owner, operation, redoAction, redoValue, undoAction, undoValue);
    }
    
    @Override
    public boolean canBeCombined(IAction action) {
        return false;
    }
}
