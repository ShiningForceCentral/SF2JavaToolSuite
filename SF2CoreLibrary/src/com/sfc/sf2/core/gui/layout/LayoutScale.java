/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * A component to support scaling panel image
 * @author TiMMy
 */
public class LayoutScale extends BaseLayoutComponent {
    
    private int displayScale = 1;
    
    public LayoutScale(int displayScale) {
        this.displayScale = displayScale;
    }

    public int getScale() {
        return displayScale;
    }

    public void setScale(int scale) {
        this.displayScale = scale;
    }
    
    public BufferedImage resizeImage(BufferedImage image) {
        if (displayScale <= 1) return image;
        BufferedImage newImage = new BufferedImage(image.getWidth()*displayScale, image.getHeight()*displayScale, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displayScale, image.getHeight()*displayScale, null);
        g.dispose();
        image.flush();
        return newImage;
    }
}
