/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 *
 * @author TiMMy
 */
public class RadioButtonAction extends Action<JRadioButton> {
        
    public RadioButtonAction(ButtonGroup group, JRadioButton newValue, JRadioButton oldValue) {
        super(group, "Radio Button Value", null, newValue, oldValue);
    }
    
    @Override
    public void execute() {
        newValue.setSelected(true);
    }

    @Override
    public void undo() {
        if (oldValue == null) {
            newValue.setSelected(false);
        } else {
            oldValue.setSelected(true);
        }
    }
}
