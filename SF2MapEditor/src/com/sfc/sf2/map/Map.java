/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.block.MapBlockset;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.MapLayoutBlock;

/**
 *
 * @author wiz
 */
public class Map {
    private MapBlockset blockset;
    private MapLayout layout;
    private MapArea[] areas;
    private MapFlagCopyEvent[] flagCopies;
    private MapCopyEvent[] stepCopies;
    private MapCopyEvent[] roofCopies;
    private MapWarpEvent[] warps;
    private MapItem[] chestItems;
    private MapItem[] otherItems;
    private MapAnimation animation;

    public Map(MapBlockset blockset, MapLayout layout, MapArea[] areas, MapFlagCopyEvent[] flagCopies, MapCopyEvent[] stepCopies, MapCopyEvent[] roofCopies, MapWarpEvent[] warps, MapItem[] chestItems, MapItem[] otherItems, MapAnimation animation) {
        this.blockset = blockset;
        this.layout = layout;
        this.areas = areas;
        this.flagCopies = flagCopies;
        this.stepCopies = stepCopies;
        this.roofCopies = roofCopies;
        this.warps = warps;
        this.chestItems = chestItems;
        this.otherItems = otherItems;
        this.animation = animation;
    }

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
    }

    public MapLayout getLayout() {
        return layout;
    }

    public void setLayout(MapLayout layout) {
        this.layout = layout;
    }

    public MapArea[] getAreas() {
        return areas;
    }

    public void setAreas(MapArea[] areas) {
        this.areas = areas;
    }

    public MapFlagCopyEvent[] getFlagCopies() {
        return flagCopies;
    }

    public void setFlagCopies(MapFlagCopyEvent[] flagCopies) {
        this.flagCopies = flagCopies;
    }

    public MapCopyEvent[] getStepCopies() {
        return stepCopies;
    }

    public void setStepCopies(MapCopyEvent[] stepCopies) {
        this.stepCopies = stepCopies;
    }

    public MapCopyEvent[] getRoofCopies() {
        return roofCopies;
    }

    public void setRoofCopies(MapCopyEvent[] roofCopies) {
        this.roofCopies = roofCopies;
    }

    public MapWarpEvent[] getWarps() {
        return warps;
    }

    public void setWarps(MapWarpEvent[] warps) {
        this.warps = warps;
    }

    public MapItem[] getChestItems() {
        return chestItems;
    }

    public void setChestItems(MapItem[] chestItems) {
        this.chestItems = chestItems;
    }

    public MapItem[] getOtherItems() {
        return otherItems;
    }

    public void setOtherItems(MapItem[] otherItems) {
        this.otherItems = otherItems;
    }

    public MapAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(MapAnimation animation) {
        this.animation = animation;
    }

    public void setActionFlag(int x, int y, int value) {
        MapLayoutBlock block = this.layout.getBlocks()[y*BLOCK_WIDTH+x];
        int origFlags = block.getFlags();
        int newValue = value;
        if ((origFlags & MapLayoutBlock.MAP_FLAG_STEP) != 0 && newValue == MapLayoutBlock.MAP_FLAG_SHOW) {
            newValue = MapLayoutBlock.MAP_FLAG_STEP;
        }
        int newFlags = (origFlags & MapLayoutBlock.MAP_FLAG_HIDE)+(newValue & 0x3FFF);
        block.setFlags(newFlags);
    }
}
