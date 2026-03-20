/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import javax.swing.JSpinner;

/**
 *
 * @author TiMMy
 */
public class SpinnerAction extends BasicAction<Object> {

    private JSpinner spinner;
    
    public SpinnerAction(JSpinner spinner, Object newValue, Object oldValue) {
        super(spinner, "Spinner Value", null, newValue, oldValue);
        this.spinner = spinner;
    }
    
    @Override
    public void execute() {
        spinner.setValue(newValue);
    }

    @Override
    public void undo() {
        spinner.setValue(oldValue);
    }
}
