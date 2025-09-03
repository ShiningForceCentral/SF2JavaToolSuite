/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.settings;

import com.sfc.sf2.core.settings.AbstractSettings;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class MapBlockSettings implements AbstractSettings {

    private int blocksetBlocksPerRow;
    private int blocksetScale;
    private int tilesetTilesPerRow;
    private int tilesetScale;
    
    private Color blocksetBGColor;
    private Color tilesetBGColor;
    private Color blockBGColor;
    
    public int getBlocksetBlocksPerRow() {
        return blocksetBlocksPerRow;
    }
    
    public void setBlocksetBlocksPerRow(int blocksetBlocksPerRow) {
        this.blocksetBlocksPerRow = blocksetBlocksPerRow;
    }
    
    public int getBlocksetScale() {
        return blocksetScale;
    }
    
    public void setBlocksetScale(int blocksetScale) {
        this.blocksetScale = blocksetScale;
    }
    
    public int getTilesetTilesPerRow() {
        return tilesetTilesPerRow;
    }
    
    public void setTilesetTilesPerRow(int tilesetTilesPerRow) {
        this.tilesetTilesPerRow = tilesetTilesPerRow;
    }
    
    public int getTilesetScale() {
        return tilesetScale;
    }
    
    public void setTilesetScale(int tilesetScale) {
        this.tilesetScale = tilesetScale;
    }
    
    public Color getBlocksetBGColor() {
        return blocksetBGColor;
    }
    
    public void setBlocksetBGColor(Color blocksetBGColor) {
        this.blocksetBGColor = blocksetBGColor;
    }
    
    public Color getTilesetBGColor() {
        return tilesetBGColor;
    }
    
    public void setTilesetBGColor(Color tilesetBGColor) {
        this.tilesetBGColor = tilesetBGColor;
    }
    
    public Color getBlockBGColor() {
        return blockBGColor;
    }
    
    public void setBlockBGColor(Color blockBGColor) {
        this.blockBGColor = blockBGColor;
    }
    
    @Override
    public void initialiseNewUser() {
        blocksetBlocksPerRow = 8;
        blocksetScale = 1;
        tilesetTilesPerRow = 16;
        tilesetScale = 1;
        Color defaultColor = new Color(200, 0, 200);
        blocksetBGColor = defaultColor;
        tilesetBGColor = defaultColor;
        blockBGColor = defaultColor;
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("blocksetBlocksPerRow")) {
            blocksetBlocksPerRow = Integer.parseInt(data.get("blocksetBlocksPerRow"));
        }
        if (data.containsKey("blocksetScale")) {
            blocksetScale = Integer.parseInt(data.get("blocksetScale"));
            if (blocksetScale <= 0) blocksetScale = 1;
        }
        if (data.containsKey("tilesetTilesPerRow")) {
            tilesetTilesPerRow = Integer.parseInt(data.get("tilesetTilesPerRow"));
        }
        if (data.containsKey("tilesetScale")) {
            tilesetScale = Integer.parseInt(data.get("tilesetScale"));
            if (tilesetScale <= 0) tilesetScale = 1;
        }
        if (data.containsKey("blocksetBGColor")) {
            String[] colorSplit = data.get("blocksetBGColor").split(",");
            blocksetBGColor = new Color(Integer.parseInt(colorSplit[0].trim()), Integer.parseInt(colorSplit[1].trim()), Integer.parseInt(colorSplit[2].trim()));
        }
        if (data.containsKey("tilesetBGColor")) {
            String[] colorSplit = data.get("tilesetBGColor").split(",");
            tilesetBGColor = new Color(Integer.parseInt(colorSplit[0].trim()), Integer.parseInt(colorSplit[1].trim()), Integer.parseInt(colorSplit[2].trim()));
        }
        if (data.containsKey("blockBGColor")) {
            String[] colorSplit = data.get("blockBGColor").split(",");
            tilesetBGColor = new Color(Integer.parseInt(colorSplit[0].trim()), Integer.parseInt(colorSplit[1].trim()), Integer.parseInt(colorSplit[2].trim()));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("blocksetBlocksPerRow", Integer.toString(blocksetBlocksPerRow));
        data.put("blocksetScale", Integer.toString(blocksetScale));
        data.put("tilesetTilesPerRow", Integer.toString(tilesetTilesPerRow));
        data.put("tilesetScale", Integer.toString(tilesetScale));
        data.put("blocksetBGColor", String.format("%d, %d, %d", blocksetBGColor.getRed(), blocksetBGColor.getGreen(), blocksetBGColor.getBlue()));
        data.put("tilesetBGColor", String.format("%d, %d, %d", tilesetBGColor.getRed(), tilesetBGColor.getGreen(), tilesetBGColor.getBlue()));
        data.put("blockBGColor", String.format("%d, %d, %d", blockBGColor.getRed(), blockBGColor.getGreen(), blockBGColor.getBlue()));
    }
}
