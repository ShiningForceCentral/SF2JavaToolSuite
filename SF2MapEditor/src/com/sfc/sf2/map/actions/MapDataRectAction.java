/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;
import java.awt.Rectangle;

/**
 *
 * @author TiMMy
 */
public class MapDataRectAction extends Action<ActionMapDataRect> {
    
    public MapDataRectAction(Object owner, String operation, IActionable<ActionMapDataRect> action, ActionMapDataRect newValue, ActionMapDataRect oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        return newValue.mapDataItem().equals(oldValue.mapDataItem()) && newValue.itemIndex() == oldValue.itemIndex() && newValue.variableId().equals(oldValue.variableId()) && newValue.rect().equals(oldValue.rect());
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        MapDataRectAction other = (MapDataRectAction)action;
        if (other == null) return false;
        if (!newValue.mapDataItem().equals(other.newValue.mapDataItem())) return false;
        if (newValue.itemIndex() != other.newValue.itemIndex()) return false;
        return newValue.variableId().equals(other.newValue.variableId());
    }

    @Override
    protected String dataToString(ActionMapDataRect data) {
        if (data == null) {
            return String.format("%s: NULL", data.variableId());
        } else {
            Rectangle rect = data.rect();
            return String.format("%s(%d): %d, %d", data.variableId(), data.itemIndex(), rect.x, rect.y, rect.x+rect.width, rect.y+rect.height);
        }
    }
}
