/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette;

/**
 *
 * @author TiMMy
 */
public interface IPaletteGraphic {

    /**
     * Gets the palette associated with the graphic
     */
    public Palette getPalette(); 
   /**
     * Sets a new palette to the graphic
     */
    public void setPalette(Palette palette);
    /**
     * Gets the raw pixel data for the graphic
     */
    public byte[] getPixels();
    /**
     * Sets new pixel data to the graphic
     */
    public void setPixels(byte[] pixels);
}
