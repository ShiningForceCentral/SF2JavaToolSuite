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
public class SpinnerAction implements IAction {

    private JSpinner spinner;
    private int newValue;
    private int previousValue;
    
    public SpinnerAction(JSpinner spinner, int newValue, int previousValue) {
        this.spinner = spinner;
        this.newValue = newValue;
        this.previousValue = previousValue;
    }
    
    @Override
    public void execute() {
        spinner.setValue(newValue);
    }

    @Override
    public void undo() {
        spinner.setValue(previousValue);
    }

    @Override
    public void dispose() { }

    @Override
    public Object[] toTableData() {
        return new Object[] { spinner.getClass().toString(), newValue, previousValue };
    }
}
