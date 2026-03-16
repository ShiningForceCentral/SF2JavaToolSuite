/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;

/**
 *
 * @author TiMMy
 */
public class SetBlockTileAction extends Action<ActionSetBlockTile> {

    public SetBlockTileAction(Object owner, String operation, IActionable<ActionSetBlockTile> action, ActionSetBlockTile newValue, ActionSetBlockTile oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        if (!newValue.block().equals(oldValue.block())) return false;
        if (newValue.index() != oldValue.index()) return false;
        return newValue.tile().equals(oldValue.tile());
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        SetBlockTileAction other = (SetBlockTileAction)action;
        return newValue.block().equals(other.newValue.block()) && newValue.index() == other.newValue.index();
    }

    @Override
    protected String dataToString(ActionSetBlockTile data) {
        return String.format("Block: %s Index: {%s} - Tile: {%s}", data.block().getIndex(), data.index(), data.tile().getTileIndex());
    }
}
