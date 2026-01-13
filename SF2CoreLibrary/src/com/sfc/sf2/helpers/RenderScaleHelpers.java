/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

/**
 *
 * @author TiMMy
 */
public class RenderScaleHelpers {
    
    public static final String[] RENDER_SCALE_STRINGS = new String[] { "1/8x", "1/4x", "1/2x", "1x", "2x", "3x", "4x", "6x" };
    public static final float[] RENDER_SCALE = new float[] { 1/8f, 1/4f, 1/2f, 1f, 2f, 3f, 4f, 6f };
    public static final int DEFAULT_RENDER_SCALE = 3;   //"1"
    
    public static int stringToIndex(String scale) {
        if (!scale.endsWith("x")) {
            scale = scale + "x";
        }        
        for (int i = 0; i < RENDER_SCALE_STRINGS.length; i++) {
            if (RENDER_SCALE_STRINGS[i].equals(scale)) {
                return i;
            }
        }
        return DEFAULT_RENDER_SCALE;
    }
    
    public static float stringToRenderScale(String scale) {
        return RENDER_SCALE[stringToIndex(scale)];
    }
    
    public static float indexToRenderScale(int index) {
        if (index < 0) index = 0;
        if (index >= RENDER_SCALE.length) index = RENDER_SCALE.length - 1;
        return RENDER_SCALE[index];
    }
    
    public static String indexToString(int index) {
        if (index < 0) index = 0;
        if (index >= RENDER_SCALE.length) index = RENDER_SCALE.length - 1;
        return RENDER_SCALE_STRINGS[index];
    }
}
