/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.core.AbstractEnums;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class MapEnums extends AbstractEnums {
        
    private final LinkedHashMap<String, Integer> items;
    private final LinkedHashMap<String, Integer> maps;
    private final LinkedHashMap<String, Integer> music;

    public MapEnums(LinkedHashMap<String, Integer> items, LinkedHashMap<String, Integer> maps, LinkedHashMap<String, Integer> music) {
        this.items = items;
        this.maps = maps;
        this.music = music;
    }

    public LinkedHashMap<String, Integer> getItems() {
        return items;
    }

    public LinkedHashMap<String, Integer> getMaps() {
        return maps;
    }

    public LinkedHashMap<String, Integer> getMusic() {
        return music;
    }

    @Override
    public String toString() {
        return String.format("Items: %d, Maps: %d, Music: %d", items.size(), maps.size(), music.size());
    }
}
