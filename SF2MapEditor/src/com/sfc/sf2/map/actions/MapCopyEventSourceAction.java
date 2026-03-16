/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.IAction;
import com.sfc.sf2.core.actions.IActionable;
import com.sfc.sf2.map.MapCopyEvent;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author TiMMy
 */
public class MapCopyEventSourceAction extends Action<ActionMapCopySourceEvent> {
    
    public MapCopyEventSourceAction(MapCopyEvent owner, String operation, IActionable<ActionMapCopySourceEvent> action, ActionMapCopySourceEvent newValue, ActionMapCopySourceEvent oldValue) {
        super(owner, operation, action, newValue, oldValue);
    }

    @Override
    public boolean isInvalidated() {
        return newValue.copyEvent().equals(oldValue.copyEvent()) && newValue.itemIndex() == oldValue.itemIndex() && newValue.event().equals(oldValue.event())
                && newValue.source().equals(oldValue.source()) && newValue.dest().equals(oldValue.dest());
    }

    @Override
    public boolean canBeCombined(IAction action) {
        if (!super.canBeCombined(action)) return false;
        MapCopyEventSourceAction other = (MapCopyEventSourceAction)action;
        if (other == null) return false;
        if (!newValue.copyEvent().equals(other.newValue.copyEvent())) return false;
        if (newValue.itemIndex() != other.newValue.itemIndex()) return false;
        return newValue.event().equals(other.newValue.event());
    }

    @Override
    protected String dataToString(ActionMapCopySourceEvent data) {
        if (data == null) {
            return String.format("%s: NULL", data.event());
        } else {
            Rectangle rect = data.source();
            Point point = data.dest();
            return String.format("%s(%d): %d, %d", data.event(), data.itemIndex(), rect.x, rect.y, rect.x+rect.width, rect.y+rect.height, point.x, point.y);
        }
    }
}
