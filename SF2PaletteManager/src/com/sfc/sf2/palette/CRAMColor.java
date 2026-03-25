/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette;

import java.awt.Color;

/**
 * Color overload that conforms to the CRAM format: https://segaretro.org/Sega_Mega_Drive/Palettes_and_CRAM
 * 
 * @author TiMMy
 */
public class CRAMColor {
    
    private Color rawColor;
    private Color cramColor;
    
    private CRAMColor() { }
    
    public CRAMColor(Color color) {
        setColor(color);
    }
    
    public CRAMColor(int rgb) {
        setColor(new Color(rgb));
    }
    
    public CRAMColor(int r, int g, int b) {
        setColor(new Color(r, g, b, 255));
    }
    
    public CRAMColor(int r, int g, int b, int a) {
        setColor(new Color(r, g, b, a));
    }
    
    private void setColor(Color color) {
        this.rawColor = color;
        this.cramColor = PaletteDecoder.conformColorToCram(color);
    }
    
    public Color rawColor() {
        return rawColor;
    }
    
    public Color CRAMColor() {
        return cramColor;
    }
    
    //Static helpers
    public static CRAMColor BLACK = fromPremadeCramColor(Color.BLACK);
    public static CRAMColor LIGHT_GRAY = fromPremadeCramColor(Color.LIGHT_GRAY);
    public static CRAMColor WHITE = fromPremadeCramColor(Color.WHITE);
    
    public static CRAMColor fromPremadeCramColor(int r, int g, int b, int a) {
        return fromPremadeCramColor(new Color(r, g, b, a));
    }
    
    public static CRAMColor fromPremadeCramColor(Color color) {
        CRAMColor cram = new CRAMColor();
        cram.rawColor = cram.cramColor = color;
        return cram;
    }
    
    public static CRAMColor[] convertToCram(Color[] colors) {
        CRAMColor[] newColors = new CRAMColor[colors.length];
        for (int i = 0; i < colors.length; i++) {
            newColors[i] = new CRAMColor(colors[i]);
        }
        return newColors;
    }
    
    @Override
    public String toString() {
        int r = cramColor.getRed();
        int g = cramColor.getGreen();
        int b = cramColor.getBlue();
        return String.format("CRAM: [%02d, %02d, %02d] - Color: [r=%d, g=%d, b=%d]", r, g, b, PaletteDecoder.brightnessToCramIndex(r), PaletteDecoder.brightnessToCramIndex(g), PaletteDecoder.brightnessToCramIndex(b));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CRAMColor)) return super.equals(obj);
        CRAMColor other = (CRAMColor)obj;
        return this.cramColor.equals(other.cramColor);
    }
}
