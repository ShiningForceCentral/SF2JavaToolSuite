/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;
import java.awt.Point;

/**
 *
 * @author TiMMy
 */
public class MapDataPointAction extends Action<ActionMapDataPoint> {
    
    public MapDataPointAction(Object owner, String operation, IActionable<ActionMapDataPoint> action, ActionMapDataPoint newValue, ActionMapDataPoint oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        return newValue.mapDataItem().equals(oldValue.mapDataItem()) && newValue.itemIndex() == oldValue.itemIndex() && newValue.event().equals(oldValue.event()) && newValue.point().equals(oldValue.point());
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        MapDataPointAction other = (MapDataPointAction)action;
        if (other == null) return false;
        if (!newValue.mapDataItem().equals(other.newValue.mapDataItem())) return false;
        if (newValue.itemIndex() != other.newValue.itemIndex()) return false;
        return newValue.event().equals(other.newValue.event());
    }

    @Override
    protected String dataToString(ActionMapDataPoint data) {
        if (data == null) {
            return String.format("%s: NULL", data.event());
        } else {
            Point point = data.point();
            return String.format("%s(%d): %d, %d", data.event(), data.itemIndex(), point.x, point.y);
        }
    }
}
