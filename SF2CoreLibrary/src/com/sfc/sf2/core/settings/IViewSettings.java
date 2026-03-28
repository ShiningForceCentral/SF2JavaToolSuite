/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import java.awt.Color;

/**
 *
 * @author TiMMy
 */
public interface IViewSettings {


    public int getItemsPerRow();
    public void setItemsPerRow(int itemsPerRow);

    public int getRenderScale();
    public void setRenderScale(int renderScale);

    public Color getBGColor();
    public void setBGColor(Color bgColor);
}