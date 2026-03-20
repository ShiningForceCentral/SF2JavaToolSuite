/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.IActionData;
import java.awt.Point;

/**
 *
 * @author TiMMy
 */
public class MapPointActionData implements IActionData<MapPointActionData> {
    
    private final Object mapDataItem;
    private final int itemIndex;
    private final String event;
    private final Point point;

    public MapPointActionData(Object mapDataItem, int itemIndex, String event, Point point) {
        this.mapDataItem = mapDataItem;
        this.itemIndex = itemIndex;
        this.event = event;
        this.point = point;
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

    public Point point() {
        return point;
    }

    @Override
    public boolean isInvalidated(MapPointActionData other) {
        return mapDataItem.equals(other.mapDataItem) && itemIndex == other.itemIndex && event().equals(other.event) && point.equals(other.point);
    }

    @Override
    public boolean canBeCombined(MapPointActionData other) {
        return mapDataItem.equals(other.mapDataItem) && itemIndex == other.itemIndex && event().equals(other.event);
    }

    @Override
    public MapPointActionData combine(MapPointActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("%s(%d): %d, %d", event, itemIndex, point.x, point.y);
    }
}
