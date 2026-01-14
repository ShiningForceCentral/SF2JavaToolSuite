/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import javax.swing.JSlider;

/**
 *
 * @author TiMMy
 */
public class SliderAction extends Action<Integer> {

    private JSlider slider;
    
    public SliderAction(JSlider slider, int newValue, int oldValue) {
        super(slider, "Slider Value", null, newValue, oldValue);
        this.slider = slider;
    }
    
    @Override
    public void execute() {
        slider.setValue(newValue);
    }

    @Override
    public void undo() {
        slider.setValue(oldValue);
    }
}
