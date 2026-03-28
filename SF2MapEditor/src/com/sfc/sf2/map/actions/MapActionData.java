/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.core.actions.IActionData;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapEnums;

/**
 *
 * @author TiMMy
 */
public class MapActionData implements IActionData<MapActionData> {
    private Map map;
    private MapEnums mapEnums;

    public MapActionData(Map map, MapEnums mapEnums) {
        this.map = map;
        this.mapEnums = mapEnums;
    }

    public Map map() {
        return map;
    }

    public MapEnums mapEnums() {
        return mapEnums;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MapActionData)) return super.equals(obj);
        MapActionData other = (MapActionData)obj;
        return map == other.map && mapEnums == other.mapEnums;
    }

    @Override
    public boolean isInvalidated(MapActionData other) {
        return map.equals(other.map) && mapEnums.equals(other.mapEnums);
    }

    @Override
    public boolean canBeCombined(MapActionData other) {
        return isInvalidated(other);
    }

    @Override
    public MapActionData combine(MapActionData other) {
        return other;
    }

    @Override
    public String toString() {
        return String.format("Map: %s - Enums: {%s}", map == null ? "NULL" : map.getName(), mapEnums == null ? "NULL" : mapEnums.toString());
    }
}
