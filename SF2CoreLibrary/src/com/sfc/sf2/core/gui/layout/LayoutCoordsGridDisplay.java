/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.settings.SettingsManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Displays item coordinates on the top and left axis of the panel image
 * @author TiMMy
 */
public class LayoutCoordsGridDisplay extends BaseLayoutComponent {
    private static final int PADDING_LEFT = 8;
    private static final int PADDING_TOP = 8;
    private static final int PADDING_SCALE = 2;
    
    private BufferedImage coordsImageLeft;
    private BufferedImage coordsImageTop;
    private int leftSize = -1;
    private int topSize = -1;
    private int leftPadding = 0;
    private int topPadding = 0;
    private int fontIncrease = 0;
    private boolean cumulative = false;
    
    /**
     * @param topSize The width of a column of items
     * @param leftSize The height of a row of items
     * @param cumulativeNumbers Whether horizontal numbers represent the row (non-cumulative) or the total number of items so far (cumulative)
     */
    public LayoutCoordsGridDisplay(int topSize, int leftSize, boolean cumulativeNumbers) {
        this(topSize, leftSize, cumulativeNumbers, 0, 0, 0);
    }
    
    /**
     * @param topSize The width of a column of items
     * @param leftSize The height of a row of items
     * @param cumulativeNumbers Whether horizontal numbers represent the row (non-cumulative) or the total number of items so far (cumulative)
     * @param topPadding Extra vertical size for the top bar (to make it thicker)
     * @param leftPadding Extra horizontal size for the left bar (to make it thicker). Useful for large numbers
     * @param fontIncrease Make font larger (fontSize + {@code fontIncrease})
     */
    public LayoutCoordsGridDisplay(int topSize, int leftSize, boolean cumulativeNumbers, int topPadding, int leftPadding, int fontIncrease) {
        this.topSize = topSize;
        this.leftSize = leftSize;
        this.cumulative = cumulativeNumbers;
        this.topPadding = topPadding;
        this.leftPadding = leftPadding;
        this.fontIncrease = fontIncrease;
    }
    
    public Dimension getOffset(int displayScale) {
        int h = topSize <= 0 ? 0 : topPadding+PADDING_TOP+displayScale*PADDING_SCALE;
        int w = leftSize <= 0 ? 0 : leftPadding+PADDING_LEFT+displayScale*PADDING_SCALE;
        return new Dimension(w, h);
    }
    
    public void paintCoordsImage(Graphics graphics, int displayScale) {
        if (topSize > 0) {
            int padding = leftSize <= 0 ? 0 : leftPadding+PADDING_LEFT+PADDING_SCALE*displayScale;
            graphics.drawImage(coordsImageTop, padding, 0, null);
        }
        if (leftSize > 0) {
            int padding = topSize <= 0 ? 0 : topPadding+PADDING_LEFT+PADDING_SCALE*displayScale;
            graphics.drawImage(coordsImageLeft, 0, padding, null);
        }
    }
    
    public void buildCoordsImage(Dimension displayArea, int displayScale) {
        if (topSize > 0) {
            coordsImageTop = paintCoordsAxis(true, displayArea.width, topSize, displayScale, fontIncrease, 1);
        }
        if (leftSize > 0) {
            int numberScale = cumulative ? displayArea.width/topSize : 1;
            coordsImageLeft = paintCoordsAxis(false, displayArea.height, leftSize, displayScale, fontIncrease, numberScale);
        }
    }
    
    private BufferedImage paintCoordsAxis(boolean topCoords, int imageSize, int coordsSize, int displayScale, int fontIncrease, int numberScale) {
        imageSize *= displayScale;
        coordsSize *= displayScale;
        int padding = (topCoords ? PADDING_TOP+topPadding : PADDING_LEFT+leftPadding) + PADDING_SCALE*displayScale;
        BufferedImage image = new BufferedImage(topCoords ? imageSize : padding, topCoords ? padding : imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        int fontSize = 4+2*displayScale+2*fontIncrease;
        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.WHITE : Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int count = imageSize/coordsSize;
        float halfPadding = padding*0.5f;
        float offset = coordsSize*0.5f + 2 + displayScale;
        for (int i = 0; i <= count; i++) {
            String item = Integer.toString(i*numberScale);
            float textWidth = (float)fontMetrics.getStringBounds(item, g2).getWidth();
            float x = topCoords ? i*coordsSize+offset-1 - textWidth : halfPadding - textWidth*0.5f;
            float y = topCoords ? halfPadding+1 + displayScale*0.75f : i*coordsSize+offset;
            g2.drawString(item, x, y);
        }
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.BLACK : Color.WHITE);
        for (int i = 0; i <= count; i++) {
            if (topCoords) {
                g2.drawLine(i*coordsSize, 0, i*coordsSize, padding);
            } else {
                g2.drawLine(0, i*coordsSize, padding, i*coordsSize);
            }
        }
        if (topCoords) {
            g2.drawLine(imageSize-1, 0, imageSize-1, padding);
        } else {
            g2.drawLine(0, imageSize-1, padding, imageSize-1);
        }
        g2.dispose();
        return image;
    }
}
