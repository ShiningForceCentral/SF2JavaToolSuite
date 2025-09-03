/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;

/**
 * A component to handle mouse clicks in common ways. Handles click and drag as similar operations
 * Be sure to call the {@code setupListeners} function (could not be in constructor).
 * @author TiMMy
 */
public class LayoutMouseInput extends BaseMouseCoordsComponent {
        
    public LayoutMouseInput(AbstractLayoutPanel panel, GridMousePressedListener mouseButtonListener, int gridX, int gridY) {
        super(panel, gridX, gridY);
        setupListeners(mouseButtonListener, null);
    }
}
