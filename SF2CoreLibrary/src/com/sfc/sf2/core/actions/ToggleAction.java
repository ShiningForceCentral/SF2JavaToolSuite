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
public class ToggleAction implements IAction {

    private JToggleButton toggle;
    private boolean on;
    
    public ToggleAction(JToggleButton toggle, boolean on) {
        this.toggle = toggle;
        this.on = on;
        
    }
    
    @Override
    public void execute() {
        toggle.setSelected(on);
    }

    @Override
    public void undo() {
        toggle.setSelected(!on);
    }

    @Override
    public void dispose() { }

    @Override
    public Object[] toTableData() {
        return new Object[] { this.getClass().toString(), on, !on };
    }
}
