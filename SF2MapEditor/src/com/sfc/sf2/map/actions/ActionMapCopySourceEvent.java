/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.MapCopyEvent;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author TiMMy
 */
public class ActionMapCopySourceEvent implements IActionData<ActionMapCopySourceEvent>{
    
    private final MapCopyEvent copyEvent;
    private final int itemIndex;
    private final String event;
    private final Rectangle source;
    private final Point dest;

    public ActionMapCopySourceEvent(MapCopyEvent copyEvent, int itemIndex, String event, Rectangle source, Point dest) {
        this.copyEvent = copyEvent;
        this.itemIndex = itemIndex;
        this.event = event;
        this.source = source;
        this.dest = dest;
    }

    public MapCopyEvent copyEvent() {
        return copyEvent;
    }

    public int itemIndex() {
        return itemIndex;
    }

    public String event() {
        return event;
    }

    public Rectangle source() {
        return source;
    }

    public Point dest() {
        return dest;
    }

    @Override
    public boolean isInvalidated(ActionMapCopySourceEvent other) {
        return this.copyEvent.equals(other.copyEvent) && this.itemIndex == other.itemIndex && this.event.equals(other.event)
                && this.source.equals(other.source) && this.dest.equals(other.dest);
    }

    @Override
    public boolean canBeCombined(ActionMapCopySourceEvent other) {
        return this.copyEvent.equals(other.copyEvent) && this.itemIndex == other.itemIndex && this.event.equals(other.event);
    }

    @Override
    public ActionMapCopySourceEvent combine(ActionMapCopySourceEvent other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("%s(%d): %d, %d", event, itemIndex, source.x, source.y, source.x+source.width, source.y+source.height, dest.x, dest.y);
    }
}
