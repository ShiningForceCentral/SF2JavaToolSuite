/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io.asm;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author TiMMy
 */
public class EntriesAsmData {
    private final ArrayList<String> entries = new ArrayList<>();
    private final ArrayList<Path> paths = new ArrayList<>();
    private final HashMap<Integer, Integer> hashMap = new HashMap<>();
    private final ArrayList<Integer> entryIndices = new ArrayList<>();
    private final ArrayList<Integer> uniqueEntryIndices = new ArrayList<>();
    
    private String headerName;
    private String pointerListName;
    private boolean isDoubleList;
    
    public String getHeaderName() {
        return headerName;
    }
    
    public void setHeadername(String headerName) {
        this.headerName = headerName;
    }
    
    public String getPointerListName() {
        return pointerListName;
    }
    
    public void setPointerListName(String pointerListName) {
        this.pointerListName = pointerListName;
    }
    
    public boolean getIsDoubleList() {
        return isDoubleList;
    }
    
    public void setIsDoubleList(boolean isDoubleList) {
        this.isDoubleList = isDoubleList;
    }
    
    public int entriesCount() {
        return entryIndices.size();
    }
    
    public int uniqueEntriesCount() {
        return uniqueEntryIndices.size();
    }
    
    public String getEntry(int index) {
        if (index >= 0 && index < entryIndices.size()) {
            return entries.get(entryIndices.get(index));
        } else {
            return null;
        }
    }
    
    public int getEntryValue(int index) {
        if (index >= 0 && index < entryIndices.size()) {
            return entryIndices.get(index);
        } else {
            return -1;
        }
    }
    
    public String getUniqueEntries(int index) {
        if (index >= 0 && index < uniqueEntryIndices.size()) {
            return entries.get(uniqueEntryIndices.get(index));
        } else {
            return null;
        }
    }
    
    public Path getPathForEntry(int index) {
        if (index >= 0 && index < entryIndices.size()) {
            return paths.get(entryIndices.get(index));
        } else {
            return null;
        }
    }
    
    public Path getPathForEntry(String entry) {
        int hash = entry.hashCode();
        if (hashMap.containsKey(hash)) {
            return paths.get(hashMap.get(hash));
        } else {
            return null;
        }
    }
    
    public Path getPathForUnique(int index) {
        if (index >= 0 && index < uniqueEntryIndices.size()) {
            return paths.get(uniqueEntryIndices.get(index));
        } else {
            return null;
        }
    }
    
    public void addEntry(String entry) {
        int hash = entry.hashCode();
        if (hashMap.containsKey(hash)) {
            int index = hashMap.get(hash);
            entries.add(entry);
            entryIndices.add(index);
            paths.add(null);
        } else {
            int index = entries.size();
            entries.add(entry);
            paths.add(null);
            hashMap.put(hash, index);
            entryIndices.add(index);
            uniqueEntryIndices.add(index);
        }
    }
    
    public void addPath(String entry, Path path) {
        int hash = entry.hashCode();
        if (!hashMap.containsKey(hash)) {
            addEntry(entry);
        }
        int index = hashMap.get(hash);
        Path existing = paths.get(index);
        if (existing == null) {
            paths.set(index, path);
        }
    }
    
    public boolean isEntryShared(int index) {
        int count = 0;
        for (int i = 0; i < entryIndices.size(); i++) {
            if (entryIndices.get(i) == index) {
                count++;
                if (count >= 2) {
                    return true;    //Multiple matches found
                }
            }
        }
        return false;
    }
    
    public int[] getSharedEntries(int index) {
        ArrayList<Integer> sharedList = new ArrayList<>();
        for (int i = 0; i < entryIndices.size(); i++) {
            if (entryIndices.get(i) == index) {
                sharedList.add(i);
            }
        }
        int[] shared = new int[sharedList.size()];
        for (int i = 0; i < shared.length; i++) {
            shared[i] = sharedList.get(i).intValue();
        }
        return shared;
    }
}
