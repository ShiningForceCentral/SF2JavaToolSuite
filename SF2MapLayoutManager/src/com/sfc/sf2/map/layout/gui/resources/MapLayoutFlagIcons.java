/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.gui.resources;

import com.sfc.sf2.core.settings.CoreSettings;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import javax.swing.ImageIcon;

/**
 *
 * @author TiMMy
 */
public class MapLayoutFlagIcons {
    
    //Various images for map flags
    private static ImageIcon stepIcon;
    private static ImageIcon hideIcon;
    private static ImageIcon showIcon;
    private static ImageIcon warpIcon;
    private static ImageIcon triggerIcon;
    private static ImageIcon chestIcon;
    private static ImageIcon searchIcon;
    private static ImageIcon layerUpIcon;
    private static ImageIcon layerDownIcon;
    private static ImageIcon tableIcon;
    private static ImageIcon vaseIcon;
    private static ImageIcon barrelIcon;
    private static ImageIcon shelfIcon;
    private static ImageIcon caravanIcon;
    private static ImageIcon raftIcon;
    private static ImageIcon stairsRightIcon;
    private static ImageIcon stairsLeftIcon;
    private static ImageIcon obstructedIcon;
    
    public static void loadFlagImages() {
        if (!((CoreSettings)SettingsManager.getSettingsStore("core")).arePathsValid()) return;
        if (obstructedIcon == null) {
            ClassLoader loader = MapLayoutFlagIcons.class.getClassLoader();
            stepIcon = new ImageIcon(loader.getResource("map/flags/icons/Step.png"));
            hideIcon = new ImageIcon(loader.getResource("map/flags/icons/Hide.png"));
            showIcon = new ImageIcon(loader.getResource("map/flags/icons/Show.png"));
            warpIcon = new ImageIcon(loader.getResource("map/flags/icons/Warp.png"));
            triggerIcon = new ImageIcon(loader.getResource("map/flags/icons/Trigger.png"));
            chestIcon = new ImageIcon(loader.getResource("map/flags/icons/Chest.png"));
            searchIcon = new ImageIcon(loader.getResource("map/flags/icons/Search.png"));
            layerUpIcon = new ImageIcon(loader.getResource("map/flags/icons/LayerUp.png"));
            layerDownIcon = new ImageIcon(loader.getResource("map/flags/icons/LayerDown.png"));
            tableIcon = new ImageIcon(loader.getResource("map/flags/icons/Table.png"));
            vaseIcon = new ImageIcon(loader.getResource("map/flags/icons/Vase.png"));
            barrelIcon = new ImageIcon(loader.getResource("map/flags/icons/Barrel.png"));
            shelfIcon = new ImageIcon(loader.getResource("map/flags/icons/Shelf.png"));
            caravanIcon = new ImageIcon(loader.getResource("map/flags/icons/Caravan.png"));
            raftIcon = new ImageIcon(loader.getResource("map/flags/icons/Raft.png"));
            stairsRightIcon = new ImageIcon(loader.getResource("map/flags/icons/StairsRight.png"));
            stairsLeftIcon = new ImageIcon(loader.getResource("map/flags/icons/StairsLeft.png"));
            obstructedIcon = new ImageIcon(loader.getResource("map/flags/icons/Obstructed.png"));
        }
    }
    
    public static ImageIcon getFlagIcon(int mapFlag) {
        if ((mapFlag & MapLayoutBlock.MAP_FLAG_MASK_EXPLORE) != 0) {
            return getBlockExplorationFlagIcon(mapFlag & MapLayoutBlock.MAP_FLAG_MASK_EXPLORE);
        } else {
            return getBlockInteractionFlagIcon(mapFlag & MapLayoutBlock.MAP_FLAG_MASK_EVENTS);
        }
    }
    
    public static ImageIcon getBlockExplorationFlagIcon(int explorationFlags) {
        switch (explorationFlags) {
            case MapLayoutBlock.MAP_FLAG_OBSTRUCTED:
                return getObstructedIcon();
            case MapLayoutBlock.MAP_FLAG_STAIRS_RIGHT:
                return getStairsRightIcon();
            case MapLayoutBlock.MAP_FLAG_STAIRS_LEFT:
                return getStairsLeftIcon();
            default:
                return null;
        }
    }
    
    public static ImageIcon getBlockInteractionFlagIcon(int interactionFlags) {
        switch (interactionFlags) {
            case MapLayoutBlock.MAP_FLAG_STEP:
                return getStepIcon();
            case MapLayoutBlock.MAP_FLAG_HIDE:
                return getHideIcon();
            case MapLayoutBlock.MAP_FLAG_SHOW:
                return getShowIcon();
            case MapLayoutBlock.MAP_FLAG_WARP:
                return getWarpIcon();
            case MapLayoutBlock.MAP_FLAG_CARAVAN:
                return getCaravanIcon();
            case MapLayoutBlock.MAP_FLAG_RAFT:
                return getRaftIcon();
            case MapLayoutBlock.MAP_FLAG_TRIGGER:
            case MapLayoutBlock.MAP_FLAG_LAYER_UP:
            case MapLayoutBlock.MAP_FLAG_LAYER_DOWN:
                return getBlockTriggersFlagIcon(interactionFlags);
            default:
                return getBlockItemFlagIcon(interactionFlags);
        }
    }
    
    public static ImageIcon getBlockTriggersFlagIcon(int interactionFlags) {
        switch (interactionFlags) {
            case MapLayoutBlock.MAP_FLAG_TRIGGER:
                return getTriggerIcon();
            case MapLayoutBlock.MAP_FLAG_LAYER_UP:
                return getLayerUpIcon();
            case MapLayoutBlock.MAP_FLAG_LAYER_DOWN:
                return getLayerDownIcon();
            default:
                return null;
        }
    }
    
    public static ImageIcon getBlockItemFlagIcon(int interactionFlags) {
        switch (interactionFlags) {
            case MapLayoutBlock.MAP_FLAG_CHEST:
                return getChestIcon();
            case MapLayoutBlock.MAP_FLAG_SEARCH:
                return getSearchIcon();
            case MapLayoutBlock.MAP_FLAG_TABLE:
                return getTableIcon();
            case MapLayoutBlock.MAP_FLAG_VASE:
                return getVaseIcon();
            case MapLayoutBlock.MAP_FLAG_BARREL:
                return getBarrelIcon();
            case MapLayoutBlock.MAP_FLAG_SHELF:
                return getShelfIcon();
            default:
                return null;
        }
    }
    
    public static ImageIcon getStepIcon() { loadFlagImages(); return stepIcon; }
    public static ImageIcon getHideIcon() { loadFlagImages(); return hideIcon; }
    public static ImageIcon getShowIcon() { loadFlagImages(); return showIcon; }
    public static ImageIcon getWarpIcon() { loadFlagImages(); return warpIcon; }
    public static ImageIcon getTriggerIcon() { loadFlagImages(); return triggerIcon; }
    public static ImageIcon getChestIcon() { loadFlagImages(); return chestIcon; }
    public static ImageIcon getSearchIcon() { loadFlagImages(); return searchIcon; }
    public static ImageIcon getLayerUpIcon() { loadFlagImages(); return layerUpIcon; }
    public static ImageIcon getLayerDownIcon() { loadFlagImages(); return layerDownIcon; }
    public static ImageIcon getTableIcon() { loadFlagImages(); return tableIcon; }
    public static ImageIcon getVaseIcon() { loadFlagImages(); return vaseIcon; }
    public static ImageIcon getBarrelIcon() { loadFlagImages(); return barrelIcon; }
    public static ImageIcon getShelfIcon() { loadFlagImages(); return shelfIcon; }
    public static ImageIcon getCaravanIcon() { loadFlagImages(); return caravanIcon; }
    public static ImageIcon getRaftIcon() { loadFlagImages(); return raftIcon; }
    public static ImageIcon getStairsRightIcon() { loadFlagImages(); return stairsRightIcon; }
    public static ImageIcon getStairsLeftIcon() { loadFlagImages(); return stairsLeftIcon; }
    public static ImageIcon getObstructedIcon() { loadFlagImages(); return obstructedIcon; }
}
