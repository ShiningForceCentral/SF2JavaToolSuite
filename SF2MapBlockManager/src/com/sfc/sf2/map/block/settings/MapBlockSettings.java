/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.block.settings;

import com.sfc.sf2.core.settings.AbstractSettings;
import com.sfc.sf2.helpers.ColorHelpers;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class MapBlockSettings implements AbstractSettings {

    private int blocksPerRow;
    private int blockScale;
    private Color blockBGColor;
    
    private final int defaultBlocksPerRow;
    private final int defaultBlockScale;
    private final Color defaultBlockBGColor;

    public MapBlockSettings() {
        defaultBlocksPerRow = 10;
        defaultBlockScale = RenderScaleHelpers.DEFAULT_RENDER_SCALE_INDEX;
        defaultBlockBGColor = new Color(200, 0, 200);
    }

    public MapBlockSettings(int defaultBlocksPerRow, int defaultBlockScale, Color defaultBlockBGColor) {
        this.defaultBlocksPerRow = defaultBlocksPerRow;
        this.defaultBlockScale = defaultBlockScale;
        this.defaultBlockBGColor = defaultBlockBGColor;
    }

    public int getBlocksPerRow() {
        return blocksPerRow;
    }

    public void setBlocksPerRow(int blocksPerRow) {
        this.blocksPerRow = blocksPerRow;
    }

    public int getBlockScale() {
        return blockScale;
    }

    public void setBlockScale(int blockScale) {
        this.blockScale = blockScale;
    }

    public Color getBlockBGColor() {
        return blockBGColor;
    }

    public void setBlockBGColor(Color blockBGColor) {
        this.blockBGColor = blockBGColor;
    }
    
    
    @Override
    public void initialiseNewUser() {
        blockScale = defaultBlockScale;
        blocksPerRow = defaultBlocksPerRow;
        blockBGColor = defaultBlockBGColor;
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("blockScale")) {
            blockScale = RenderScaleHelpers.stringToIndex(data.get("blockScale"));
            if (blockScale <= 0) blockScale = 1;
        }
        if (data.containsKey("blocksPerRow")) {
            blocksPerRow = Integer.parseInt(data.get("blocksPerRow"));
        }
        if (data.containsKey("blockBGColor")) {
            blockBGColor = ColorHelpers.parseColorString(data.get("blockBGColor"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("blockScale", RenderScaleHelpers.indexToString(blockScale));
        data.put("blocksPerRow", Integer.toString(blocksPerRow));
        data.put("blockBGColor", ColorHelpers.toHexString(blockBGColor));
    }
}
