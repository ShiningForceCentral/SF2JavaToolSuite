/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.gui.controls.MultiComboBox;

/**
 *
 * @author TiMMy
 */
public class MultiComboAction extends Action<Object[]> {

    private MultiComboBox comboBox;
    
    public MultiComboAction(MultiComboBox comboBox, Object[] newIndex, Object[] oldIndex, IActionable<Object[]> action) {
        super(comboBox, "MultiCombo Value", action, newIndex, oldIndex);
        this.comboBox = comboBox;
    }
    
    @Override
    public void execute() {
        comboBox.setSelected(newValue);
        super.execute();
    }

    @Override
    public void undo() {
        comboBox.setSelected(oldValue);
        super.undo();
    }
}
