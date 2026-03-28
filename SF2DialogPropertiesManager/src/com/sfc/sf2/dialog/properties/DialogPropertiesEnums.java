/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.dialog.properties;

import com.sfc.sf2.core.AbstractEnums;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.mapsprite.MapSprite;
import com.sfc.sf2.portrait.Portrait;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class DialogPropertiesEnums extends AbstractEnums {
    
    private final LinkedHashMap<String, Integer> mapSprites;
    private final LinkedHashMap<String, Integer> portraits;
    private final LinkedHashMap<String, Integer> sfx;
    
    private HashMap<Integer, MapSprite> mapSpriteImages;
    private HashMap<Integer, Portrait> portraitImages;

    public DialogPropertiesEnums(LinkedHashMap<String, Integer> mapSprites, LinkedHashMap<String, Integer> portraits, LinkedHashMap<String, Integer> sfx) {
        this.mapSprites = mapSprites;
        this.portraits = portraits;
        this.sfx = sfx;
    }

    public void setImages(HashMap<Integer, MapSprite> mapSpriteImages, HashMap<Integer, Portrait> portraitImages) {
        this.mapSpriteImages = mapSpriteImages;
        this.portraitImages = portraitImages;
    }
    
    public LinkedHashMap<String, Integer> getMapSprites() {
        return mapSprites;
    }

    public LinkedHashMap<String, Integer> getPortraits() {
        return portraits;
    }

    public LinkedHashMap<String, Integer> getSfx() {
        return sfx;
    }
    
    public BufferedImage getMapSpriteFor(String name) {
        int index = -1;
        if (mapSprites.containsKey(name)) { 
            index = mapSprites.get(name);
            index = index*3+2;  //Get the "down" facing mapsprite
        }
        if (mapSpriteImages.containsKey(index)) {
            MapSprite mapSprite = mapSpriteImages.get(index);
            Tileset frame = null;
            if (mapSprite == null) {
                index -= 2;
                if (mapSpriteImages.containsKey(index)) {
                    mapSprite = mapSpriteImages.get(index);
                }
            }
            if (mapSprite != null) {
                frame = mapSprite.getFrame(true);
                if (frame == null) {
                    frame = mapSprite.getFrame(false);
                }
            }
            if (frame != null) return frame.getIndexedColorImage(2);
        }
        return null;
    }
    
    public BufferedImage getPortraitFor(String name) {
        int index = -1;
        if (portraits.containsKey(name)) index = portraits.get(name);
        if (portraitImages.containsKey(index)) {
            Portrait portrait = portraitImages.get(index);
            if (portrait != null) {
                return portrait.getIndexedColorImage(false, false, false);
            }
        }
        return null;
    }
}
