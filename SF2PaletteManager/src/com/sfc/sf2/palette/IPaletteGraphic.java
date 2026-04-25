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
    public Palette getPalette();
    public void setPalette(Palette palette);
    public byte[] getPixels();
    public void setPixels(byte[] pixels);
}
