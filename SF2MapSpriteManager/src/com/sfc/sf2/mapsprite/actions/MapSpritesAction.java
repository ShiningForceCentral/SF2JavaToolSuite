/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;
import com.sfc.sf2.mapsprite.MapSpriteEntries;

/**
 *
 * @author TiMMy
 */
public class MapSpritesAction extends Action<MapSpriteEntries> {
    
    public MapSpritesAction(Object owner, String operation, IActionable<MapSpriteEntries> action, MapSpriteEntries newValue, MapSpriteEntries oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        if (newValue == null && oldValue == null) return true;
        if (newValue == null || oldValue == null) return false;
        return newValue.countEntries() == oldValue.countEntries() && newValue.countMapSprites() == oldValue.countMapSprites() && newValue.countUnreferenced() == oldValue.countUnreferenced();
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        MapSpritesAction other = (MapSpritesAction)action;
        if (newValue == null && other.newValue == null) return true;
        return other.operation.equals(operation) && (operation.startsWith("Optimise") || operation.startsWith("Insert"));
    }

    @Override
    protected String dataToString(MapSpriteEntries data) {
        if (data == null) {
            return "Sprites: 0 - Uniques: 0 - Unreferenced: 0";
        } else {
            int entries = data.countEntries();
            int uniques = data.countUniques();
            int unreferenced = data.countUnreferenced();
            return String.format("Sprites: %d - Uniques: %d - Unreferenced: %d", entries, uniques, unreferenced);
        }
    }
}
