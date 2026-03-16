/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.actions;

import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapEnums;

/**
 *
 * @author TiMMy
 */
public class ActionMapData {
    private Map map;
    private MapEnums mapEnums;

    public ActionMapData(Map map, MapEnums mapEnums) {
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
        if (obj instanceof ActionMapData) {
            ActionMapData other = (ActionMapData)obj;
            return map == other.map && mapEnums == other.mapEnums;
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public String toString() {
        return String.format("Map: %s - Enums: {%s}", map == null ? "NULL" : map.getName(), mapEnums == null ? "NULL" : mapEnums.toString());
    }
}
