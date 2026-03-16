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
public class SetBlockTileFlagsAction extends Action<ActionSetBlockTileFlags> {

    public SetBlockTileFlagsAction(Object owner, String operation, IActionable<ActionSetBlockTileFlags> action, ActionSetBlockTileFlags newValue, ActionSetBlockTileFlags oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        if (!newValue.tile().equals(oldValue.tile())) return false;
        if (newValue.index() != oldValue.index()) return false;
        return newValue.flags().equals(oldValue.flags());
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        SetBlockTileFlagsAction other = (SetBlockTileFlagsAction)action;
        return newValue.tile().equals(other.newValue.tile());
    }

    @Override
    protected String dataToString(ActionSetBlockTileFlags data) {
        if (data.tile() == null) {
            return "NULL";
        } else {
            return String.format("Tile(%s) - H: {%s}, V: {%s}, P: {%s}", data.index(), data.flags().isHFlip(), data.flags().isVFlip(), data.flags().isPriority());
        }
    }
    
}
