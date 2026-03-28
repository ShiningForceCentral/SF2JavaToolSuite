/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.io;

/**
 *
 * @author TiMMy
 */
public class MapEntryData {
    
    private int mapId;
    private String tilesetsPath;
    private String blocksPath;
    private String layoutPath;
    private String areasPath;
    private String flagEventsPath;
    private String stepEventsPath;
    private String roofEventsPath;
    private String warpEventsPath;
    private String chestItemsPath;
    private String otherItemsPath;
    private String animationsPath;

    public MapEntryData(int mapId) {
        this.mapId = mapId;
    }

    public int getMapId() {
        return mapId;
    }
    
    public String getTilesetsPath() {
        return tilesetsPath;
    }

    public void setTilesetsPath(String tilesetsPath) {
        this.tilesetsPath = tilesetsPath;
    }

    public String getBlocksPath() {
        return blocksPath;
    }

    public void setBlocksPath(String blocksPath) {
        this.blocksPath = blocksPath;
    }

    public String getLayoutPath() {
        return layoutPath;
    }

    public void setLayoutPath(String layoutPath) {
        this.layoutPath = layoutPath;
    }

    public String getAreasPath() {
        return areasPath;
    }

    public void setAreasPath(String areasPath) {
        this.areasPath = areasPath;
    }

    public String getFlagEventsPath() {
        return flagEventsPath;
    }

    public void setFlagEventsPath(String flagEventsPath) {
        this.flagEventsPath = flagEventsPath;
    }

    public String getStepEventsPath() {
        return stepEventsPath;
    }

    public void setStepEventsPath(String stepEventsPath) {
        this.stepEventsPath = stepEventsPath;
    }

    public String getRoofEventsPath() {
        return roofEventsPath;
    }

    public void setRoofEventsPath(String roofEventsPath) {
        this.roofEventsPath = roofEventsPath;
    }

    public String getWarpEventsPath() {
        return warpEventsPath;
    }

    public void setWarpEventsPath(String warpEventsPath) {
        this.warpEventsPath = warpEventsPath;
    }

    public String getChestItemsPath() {
        return chestItemsPath;
    }

    public void setChestItemsPath(String chestItemsPath) {
        this.chestItemsPath = chestItemsPath;
    }

    public String getOtherItemsPath() {
        return otherItemsPath;
    }

    public void setOtherItemsPath(String otherItemsPath) {
        this.otherItemsPath = otherItemsPath;
    }

    public String getAnimationsPath() {
        return animationsPath;
    }

    public void setAnimationsPath(String animationsPath) {
        this.animationsPath = animationsPath;
    }
    
    public boolean IsEmpty() {
        return tilesetsPath == null && blocksPath == null && layoutPath == null;
    }
}
