/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.mapsprite;

import com.sfc.sf2.core.gui.controls.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JOptionPane;

/**
 *
 * @author TiMMy
 */
public class MapSpriteEntries {
    
    private final HashMap<Integer, MapSprite> mapSprites;
    private final ArrayList<Integer> entries;
    private ArrayList<MapSprite> unreferenced = null;
    
    private int validEntriesCount;
    private int uniqueEntriesCount;
    
    public MapSpriteEntries(int initialSize) {
        mapSprites = new HashMap<>();
        entries = new ArrayList<>(initialSize);
        validEntriesCount = 0;
        uniqueEntriesCount = 0;
    }
    
    private MapSpriteEntries(MapSpriteEntries other) {
        this.mapSprites = (HashMap<Integer, MapSprite>)other.mapSprites.clone();
        this.entries = (ArrayList<Integer>)other.entries.clone();
        if (other.unreferenced != null) {
            this.unreferenced = (ArrayList<MapSprite>)other.unreferenced.clone();
        }
        validEntriesCount = 0;
        uniqueEntriesCount = 0;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i) >= 0) {
                validEntriesCount++;
            }
            if (entries.get(i) == i) {
                uniqueEntriesCount++;
            }
        }
    }
    
    public int countEntries() {
        return validEntriesCount;
    }
    
    public int countUniques() {
        return validEntriesCount;
    }
    
    public int countMapSprites() {
        return mapSprites.size();
    }
    
    public int countUnreferenced() {
        if (unreferenced == null) {
            return 0;
        } else {
            return unreferenced.size();
        }
    }

    public int[] getEntriesArray() {
        int[] array = new int[entries.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = entries.get(i);
        }
        return array;
    }

    public MapSprite[] getMapSpritesArray() {
        MapSprite[] array = new MapSprite[mapSprites.size()];
        array = (MapSprite[])mapSprites.values().toArray(array);
        return array;
    }
    
    public MapSprite[] getUnreferencedArray() {
        if (unreferenced == null) {
            return null;
        } else {
            MapSprite[] array = new MapSprite[unreferenced.size()];
            array = (MapSprite[])unreferenced.toArray(array);
            return array;
        }
    }
    
    public boolean hasData(int index) {
        return entries.size() > index && entries.get(index) != -1;
    }
    
    public boolean isDuplicateEntry(int index) {
        return entries.size() > index && entries.get(index) != index;
    }
    
    public MapSprite getMapSprite(int index) {
        return mapSprites.get(entries.get(index));
    }
    
    public void addEntry(int index, MapSprite mapSprite) {
        if (mapSprite.getCombinedIndex() < 0) {
            int nextFreeIndex = 0;
            while (mapSprites.containsKey(nextFreeIndex)) {
                nextFreeIndex++;
            }
            mapSprite.setCombinedIndex(nextFreeIndex);
        }
        boolean duplicate = mapSprites.containsKey(mapSprite.getCombinedIndex());
        if (!duplicate) {
            mapSprites.put(mapSprite.getCombinedIndex(), mapSprite);
        }
        addEntry(index, mapSprite.getCombinedIndex());
    }
    
    public void addEntry(int index, int reference) {
        while (index >= entries.size()) {
            entries.add(-1);
        }
        entries.set(index, reference);
        validEntriesCount++;
        if (index == reference) {
            uniqueEntriesCount++;
        }
    }

    public void setUnreferenced(ArrayList<MapSprite> unreferenced) {
        this.unreferenced = unreferenced;
    }

    public void setUnreferenced(Collection<MapSprite> unreferenced) {
        this.unreferenced = new ArrayList<>(unreferenced);
    }

    @Override
    public MapSpriteEntries clone() {
        return new MapSpriteEntries(this);
    }
    
    /**
     * Inserts unreferenced sprites into the next available slot(s) in the entries
     */
    public boolean insertUnreferenced() {
        if (unreferenced == null || unreferenced.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No unreferenced sprites to insert.");
            return false;
        }
        
        int nextFreeIndex = 0;
        HashMap<Integer, Integer> inserted = new HashMap<>();
        for (int i = 0; i < unreferenced.size(); i++) {
            while (nextFreeIndex < entries.size() && entries.get(nextFreeIndex) != -1) {
                nextFreeIndex++;
            }
            MapSprite sprite = unreferenced.get(i);
            int origIndex = sprite.getCombinedIndex();
            sprite.setCombinedIndex(nextFreeIndex);
            addEntry(nextFreeIndex, sprite);
            inserted.put(origIndex, nextFreeIndex);
            unreferenced.remove(i);
            i--;
        }
        
        if (inserted.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No unreferenced sprites could be inserted.");
            return false;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(inserted.size());
            sb.append(" Unreferenced MapSprites inserted:\n");
            for (Entry<Integer, Integer> entry : inserted.entrySet()) {
                sb.append(String.format("- Unreferenced %03d-%d inserted into %03d-%d\n", entry.getKey()/3, entry.getKey()%3, entries.get(entry.getValue())/3, entries.get(entry.getValue())%3));
            }
            Console.logger().info(sb.toString());
            JOptionPane.showMessageDialog(null, sb.toString());
            return true;
        }
    }
    
    /**
     * Finds duplicate mapsprites and turns them into references
     * @param optimisePerRow if TRUE, optimises based on a whole 'row' of frames (3 .bin spritefiles).
     * If FALSE, optimisePerRow optimises based on 2 sprite frames (one .bin spritefile).
     */
    public boolean optimiseEntries(boolean optimisePerRow) {
        ArrayList<Integer> optimised = new ArrayList<>();
        boolean match;
        int step = optimisePerRow ? 3 : 1;
        for (int i = 0; i < entries.size(); i+=step) {
            match = true;
            for (int s = 0; s < step; s++) {
                if (i+s >= entries.size() || entries.get(i+s) != i+s) {
                    match = false;
                }
            }
            if (match) {    //Keys == values, therefore not a reference (may duplicate sprites) 
                for (int j = i+step; j < entries.size(); j+=step) {
                    match = true;
                    for (int s = 0; s < step; s++) {
                        if (j+s >= entries.size() || entries.get(j+s) != j+s || !mapSprites.get(entries.get(i+s)).equals(mapSprites.get(entries.get(j+s)))) {
                            match = false;
                        }
                    }
                    if (match) { //Is a duplicate
                        int ref = entries.get(j);
                        for (int s = 0; s < step; s++) {
                            entries.set(j+s, entries.get(i+s));
                            optimised.add(j+s);
                        }
                        //Also check if anything references this sprite and make it point to the new reference point
                        for (int k = j+step; k < entries.size(); k+=step) {
                            match = true;
                            for (int s = 0; s < step; s++) {
                                if (k+s >= entries.size() || entries.get(k+s) != ref+s) {
                                    match = false;
                                }
                            }
                            if (match) {
                                for (int s = 0; s < step; s++) {
                                    entries.set(k+s, entries.get(i+s));
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (optimised.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sprites require optimisation.");
            return false;
        } else {
            optimised.sort(null);
            StringBuilder sb = new StringBuilder();
            sb.append(optimised.size());
            sb.append(" MapSprites optimised:\n");
            for (int i = 0; i < optimised.size(); i++) {
                sb.append(String.format("- MapSprite %03d-%d now references MapSprite %03d-%d\n", optimised.get(i)/3, optimised.get(i)%3, entries.get(optimised.get(i))/3, entries.get(optimised.get(i))%3));
            }
            Console.logger().info(sb.toString());
            JOptionPane.showMessageDialog(null, sb.toString());
            return true;
        }
    }
}
