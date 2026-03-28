/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import com.sfc.sf2.helpers.ColorHelpers;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public class ViewSettings implements AbstractSettings, IViewSettings {

    private int itemsPerRow;
    private int renderScale;
    private Color bgColor = Color.LIGHT_GRAY;
    
    private final int defaultItemsPerRow;
    private final int defaultRenderScale;
    private final Color defaultBGColor;

    public ViewSettings() {
        this(16, RenderScaleHelpers.RENDER_SCALE_1X, Color.LIGHT_GRAY);
    }

    public ViewSettings(int renderScale) {
        this(16, renderScale, Color.LIGHT_GRAY);
    }

    public ViewSettings(int itemsPerRow, int renderScale) {
        this(itemsPerRow, renderScale, Color.LIGHT_GRAY);
    }

    public ViewSettings(int defaultItemsPerRow, int defaultRenderScale, Color defaultBGColor) {
        this.defaultItemsPerRow = defaultItemsPerRow;
        this.defaultRenderScale = defaultRenderScale;
        this.defaultBGColor = defaultBGColor;
    }

    @Override
    public int getItemsPerRow() {
        return itemsPerRow;
    }

    public void setItemsPerRow(int itemsPerRow) {
        this.itemsPerRow = itemsPerRow;
    }

    @Override
    public int getRenderScale() {
        return renderScale;
    }

    public void setRenderScale(int renderScale) {
        this.renderScale = renderScale;
    }

    @Override
    public Color getBGColor() {
        return bgColor;
    }

    public void setBGColor(Color bgColor) {
        this.bgColor = bgColor;
    }
    
    
    @Override
    public void initialiseNewUser() {
        renderScale = defaultRenderScale;
        itemsPerRow = defaultItemsPerRow;
        bgColor = defaultBGColor;
    }
    
    @Override
    public void decodeSettings(HashMap<String, String> data) {
        if (data.containsKey("itemsPerRow")) {
            itemsPerRow = Integer.parseInt(data.get("itemsPerRow"));
        }
        if (data.containsKey("renderScale")) {
            renderScale = RenderScaleHelpers.stringToIndex(data.get("renderScale"));
            if (renderScale <= 0) renderScale = 1;
        }
        if (data.containsKey("bgColor")) {
            bgColor = ColorHelpers.parseColorString(data.get("bgColor"));
        }
    }

    @Override
    public void encodeSettings(HashMap<String, String> data) {
        data.put("itemsPerRow", Integer.toString(itemsPerRow));
        data.put("renderScale", RenderScaleHelpers.indexToString(renderScale));
        data.put("bgColor", ColorHelpers.toHexString(bgColor));
    }
}