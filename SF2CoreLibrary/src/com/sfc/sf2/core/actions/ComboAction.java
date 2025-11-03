/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import javax.swing.JComboBox;

/**
 *
 * @author TiMMy
 */
public class ComboAction implements IAction {

    private JComboBox comboBox;
    private int selectedIndex;
    private int previousIndex;
    
    public ComboAction(JComboBox comboBox, int selectedIndex, int previousIndex) {
        this.comboBox = comboBox;
        this.selectedIndex = selectedIndex;
        this.previousIndex = previousIndex;
    }
    
    @Override
    public void execute() {
        comboBox.setSelectedIndex(selectedIndex);
    }

    @Override
    public void undo() {
        comboBox.setSelectedIndex(previousIndex);
    }

    @Override
    public void dispose() { }

    @Override
    public Object[] toTableData() {
        return new Object[] { this, selectedIndex, previousIndex };
    }
}
