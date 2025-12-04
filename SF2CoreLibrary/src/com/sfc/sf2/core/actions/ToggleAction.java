/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import javax.swing.JToggleButton;

/**
 *
 * @author TiMMy
 */
public class ToggleAction extends Action<Boolean> {

    private JToggleButton toggle;
    
    public ToggleAction(JToggleButton toggle, boolean newValue, IActionable<Boolean> action) {
        super(toggle, "Toggle Value", action, newValue, !newValue);
        this.toggle = toggle;
    }
    
    @Override
    public void execute() {
        toggle.setSelected(newValue);
        super.execute();
    }

    @Override
    public void undo() {
        toggle.setSelected(oldValue);
        super.undo();
    }

    @Override
    public boolean isInvalidated() {
        return newValue.equals(oldValue);
    }
}
