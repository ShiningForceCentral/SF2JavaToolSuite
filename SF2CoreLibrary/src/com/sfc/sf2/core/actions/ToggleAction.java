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
public class ToggleAction extends BasicAction<Boolean> {

    private JToggleButton toggle;
    
    public ToggleAction(JToggleButton toggle, boolean newValue) {
        super(toggle, "Toggle Value", null, newValue, !newValue);
        this.toggle = toggle;
    }

    @Override
    public boolean isInvalidated() {
        return newValue.equals(oldValue);
    }
    
    @Override
    public void execute() {
        if (!toggle.isEnabled()) return;
        toggle.setSelected(newValue);
        super.execute();
    }

    @Override
    public void undo() {
        if (!toggle.isEnabled()) return;
        toggle.setSelected(oldValue);
        super.undo();
    }
}
