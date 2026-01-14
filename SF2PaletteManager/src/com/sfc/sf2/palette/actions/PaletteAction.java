/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.palette.gui.PalettePane;

/**
 *
 * @author TiMMy
 */
public class PaletteAction extends Action<CRAMColor> {

    private PalettePane palettePane;
    private int index;
    
    public PaletteAction(PalettePane palettePane, int index, CRAMColor newValue, CRAMColor oldValue) {
        super(palettePane, "Palette Color", null, newValue, oldValue);
        this.palettePane = palettePane;
        this.index = index;
    }
    
    @Override
    public void execute() {
        palettePane.updateColor(index, newValue);
    }

    @Override
    public void undo() {
        palettePane.updateColor(index, oldValue);
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        PaletteAction other = (PaletteAction)action;
        return this.index == other.index;
    }
}
