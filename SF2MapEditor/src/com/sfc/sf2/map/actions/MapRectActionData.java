/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.IActionData;
import java.awt.Rectangle;

/**
 *
 * @author TiMMy
 */
public class MapRectActionData implements IActionData<MapRectActionData> {
    
    private final Object mapDataItem;
    private final int itemIndex;
    private final String event;
    private final Rectangle rect;

    public MapRectActionData(Object mapDataItem, int itemIndex, String event, Rectangle rect) {
        this.mapDataItem = mapDataItem;
        this.itemIndex = itemIndex;
        this.event = event;
        this.rect = rect;
    }

    public Object mapDataItem() {
        return mapDataItem;
    }

    public int itemIndex() {
        return itemIndex;
    }

    public String event() {
        return event;
    }

    public Rectangle rect() {
        return rect;
    }

    @Override
    public boolean isInvalidated(MapRectActionData other) {
        return mapDataItem.equals(other.mapDataItem) && itemIndex == other.itemIndex && event().equals(other.event) && rect.equals(other.rect);
    }

    @Override
    public boolean canBeCombined(MapRectActionData other) {
        return mapDataItem.equals(other.mapDataItem) && itemIndex == other.itemIndex && event().equals(other.event);
    }

    @Override
    public MapRectActionData combine(MapRectActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("%s(%d): %d, %d, %d, %d", event, itemIndex, rect.x, rect.y, rect.x+rect.width, rect.y+rect.height);
    }
}
