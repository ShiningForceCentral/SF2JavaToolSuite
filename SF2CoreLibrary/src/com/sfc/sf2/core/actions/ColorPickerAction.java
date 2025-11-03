/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.gui.controls.ColorPicker;
import java.awt.Color;

/**
 *
 * @author TiMMy
 */
public class ColorPickerAction implements IAction {

    private ColorPicker colorPicker;
    private Color newColor;
    private Color previousColor;
    
    public ColorPickerAction(ColorPicker colorPicker, Color newColor, Color previousColor) {
        this.colorPicker = colorPicker;
        this.newColor = newColor;
        this.previousColor = previousColor;
    }
    
    @Override
    public void execute() {
        setColor(newColor);
    }

    @Override
    public void undo() {
        setColor(previousColor);
    }
    
    private void setColor(Color color) {
        colorPicker.setColor(color);
        colorPicker.repaint();
        colorPicker.fireColorChanged();
    }

    @Override
    public void dispose() { }

    @Override
    public Object[] toTableData() {
        return new Object[] { this, newColor, previousColor };
    }
}
