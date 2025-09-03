/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.helpers.GraphicsHelpers;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Handles rendering flat or checkered background image under panel image
 * @author TiMMy
 */
public class LayoutBackground extends BaseLayoutComponent {
    
    private Color bgColor;
    private int checkerPatternGridSize;

    /**
     * Creates a background with a flat color
     */
    public LayoutBackground(Color bgColor) {
        this.bgColor = bgColor;
        checkerPatternGridSize = -1;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * Creates a background with a checker pattern. Scaled with image scale
     * @param checkerPatternGridSize The size, in pixels to render the checkers (recommended to set as half of your grid units).<br>Set to 0 to have a flat background
     */
    public LayoutBackground(Color bgColor, int checkerPatternGridSize) {
        this.bgColor = bgColor;
        this.checkerPatternGridSize = checkerPatternGridSize;
    }
    
    public void paintBackground(BufferedImage image) {
        if (checkerPatternGridSize == -1) {
            GraphicsHelpers.drawFlatBackgroundColor(image, bgColor);
        } else {
            GraphicsHelpers.drawBackgroundTransparencyPattern(image, bgColor, checkerPatternGridSize);
        }
    }
}
