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
public class NumHelpers {
    
    /**
     * Gets a value that is safely outside of the gap range
     */
    public static int getValueWithValidGap(int value, int oldValue, int gapStart, int gapEnd) {
        if (value > gapStart && value < gapEnd) {
            if (value > oldValue) value = gapEnd;
            else if (value < oldValue) value = gapStart;
        }
        return value;
    }
}
