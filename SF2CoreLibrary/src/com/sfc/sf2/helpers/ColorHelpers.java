/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import java.awt.Color;

/**
 *
 * @author TiMMy
 */
public class ColorHelpers {
    public static String toHexString(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }
    
    public static Color parseColorString(String s) {
        if (s == null || s.length() == 0) return null;
        if (s.contains(",")) {
            String[] split = s.split(",");
            return new Color(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()), Integer.parseInt(split[2].trim()));
        } else {
            return Color.decode(s);
        }
    }
}
