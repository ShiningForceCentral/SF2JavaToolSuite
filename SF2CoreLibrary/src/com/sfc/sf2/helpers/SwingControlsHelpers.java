/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/**
 *
 * @author TiMMy
 */
public class SwingControlsHelpers {
    
    public static AbstractButton getButtonByName(ButtonGroup group, String buttonName) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.getName().equals(buttonName)) {
                return button;
            }
        }

        return null;
    }
    
    public static AbstractButton getButtonByActionCommand(ButtonGroup group, String actionCommand) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.getActionCommand().equals(actionCommand)) {
                return button;
            }
        }

        return null;
    }
}
