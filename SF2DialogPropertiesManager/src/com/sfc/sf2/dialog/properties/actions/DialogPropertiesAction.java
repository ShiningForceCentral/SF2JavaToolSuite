/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;
import com.sfc.sf2.dialog.properties.DialogProperty;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesAction extends Action<DialogProperty[]> {
    
    public DialogPropertiesAction(Object owner, String operation, IActionable<DialogProperty[]> action, DialogProperty[] newValue, DialogProperty[] oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean canBeCombined(IAction action) {
        return newValue.length == ((DialogPropertiesAction)action).newValue.length;
    }
}
