/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette;

import com.sfc.sf2.core.INameable;
import java.awt.Color;
import java.awt.image.IndexColorModel;
import java.util.Arrays;

/**
 *
 * @author TiMMy
 */
public class Palette implements INameable {
    
    private String name;
    private CRAMColor[] colors;
    
    private boolean firstColorTransparent;
    private IndexColorModel icm;
    
    public Palette() {
        this.name = "New Palette";
        this.colors = null;
    }
    
    public Palette(CRAMColor[] colors, boolean firstColorTransparent) {
        setName("New Palette");
        setColors(colors, firstColorTransparent);
    }
    
    public Palette(String name, CRAMColor[] colors, boolean firstColorTransparent) {
        setName(name);
        setColors(colors, firstColorTransparent);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFirstColorTransparent() {
        return firstColorTransparent;
    }

    public CRAMColor[] getColors() {
        return colors;
    }

    public void setColors(CRAMColor[] palette, boolean firstColorTransparent) {
        this.colors = palette;
        this.firstColorTransparent = firstColorTransparent;
        if (firstColorTransparent) {
            ensureUniqueTransparencyColor();
        }
        rebuildIcm();
    }
    
    public int getColorsCount() {
        return colors.length;
    }

    public IndexColorModel getIcm() {
        return icm;
    }
    
    public void rebuildIcm() {
        icm = buildICM(colors, firstColorTransparent);
    }
    
    /*
        Managing edge case of transparent {@code Color} being identical to an opaque {@code Color} in the palette,
        preventing image rendering to use opaque color where needed.
        In such case, now applying standard magenta as transparency color.
    */
    private void ensureUniqueTransparencyColor() {
        Color zero = colors[0].CRAMColor();
        Color color;
        for (int i=1;i<colors.length;i++) {
            color = colors[i].CRAMColor();
            if (zero.getRed()==color.getRed() && zero.getGreen()==color.getGreen() && zero.getBlue()==color.getBlue()) {
                colors[0] = new CRAMColor(0xFF00FF00);
                return;
            }
        }
        if (zero.getAlpha() > 0) {
            int rgb = zero.getRGB() | 0xFF;
            colors[0] = CRAMColor.fromPremadeCramColor(new Color(rgb));
        }
    }
    
    /*
        Gets {@code Color} array from an existing {@code Index Color Model}
    */
    public static CRAMColor[] fromICM(IndexColorModel icm) {
        CRAMColor[] colors = new CRAMColor[16];
        if(icm.getMapSize()>16){
            System.out.println("fromICM - Image format has more than 16 indexed colors. Palette may not load correctly.");
        }
        byte[] reds = new byte[icm.getMapSize()];
        byte[] greens = new byte[icm.getMapSize()];
        byte[] blues = new byte[icm.getMapSize()];
        icm.getReds(reds);
        icm.getGreens(greens);
        icm.getBlues(blues);
        for(int i=0;i<16;i++) {
            colors[i] = new CRAMColor((int)(reds[i]&0xff),(int)(greens[i]&0xff),(int)(blues[i]&0xff), 255);
        }
        return colors;
    }
    
    /*
        Creates new {@code Index Color Model} from {@code Color} array
    */
    public static IndexColorModel buildICM(CRAMColor[] colors) {
        return buildICM(colors, true);
    }
    
    /*
        Creates new {@code Index Color Model} from {@code Color} array
    */
    public static IndexColorModel buildICM(CRAMColor[] colors, boolean firstColorTransparent) {
        byte[] reds = new byte[colors.length];
        byte[] greens = new byte[colors.length];
        byte[] blues = new byte[colors.length];
        byte[] alphas = new byte[colors.length];
        for(int i=0; i<colors.length; i++) {
            reds[i] = (byte)colors[i].CRAMColor().getRed();
            greens[i] = (byte)colors[i].CRAMColor().getGreen();
            blues[i] = (byte)colors[i].CRAMColor().getBlue();
            alphas[i] = (byte)0xFF;
        }
        alphas[0] = firstColorTransparent ? 0 : (byte)0xFF;
        IndexColorModel icm = new IndexColorModel(4,colors.length,reds,greens,blues,alphas);
        return icm;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Palette)) return super.equals(obj);
        Palette palette = (Palette)obj;
        if (!Arrays.equals(this.colors, palette.colors)) return false;
        return true;
    }
    
    public Palette Clone() {
        Palette newPalette = new Palette(colors.clone(), firstColorTransparent);
        newPalette.rebuildIcm();
        return newPalette;
    }
}
