/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.gui.layout.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author TiMMy
 */
public class TestLayoutPanel extends AbstractLayoutPanel {

    public TestLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, 8);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(8, 8, 24, 24);
        coordsGrid = new LayoutCoordsGridDisplay(24, 24, true, 4, 10, 2);
        coordsHeader = new LayoutCoordsHeader(this, 24, 24);
        mouseInput = null;
        scroller = null;
    }
    
    @Override
    protected boolean hasData() {
        return true;
    }

    @Override
    protected Dimension getImageDimensions() {
        return getSize();
    }

    @Override
    protected void drawImage(Graphics graphics) {
        Dimension d = getSize();
        int halfHeight = d.height/2;
        Color c = null;
        for (int j = 0; j < d.height; j++) {
            for (int i = 0; i < d.width; i++) {
                if (j <= halfHeight) {
                    c = Color.getHSBColor(i*0.01f, 1f, (halfHeight-j)*2*0.01f);
                } else {
                    c = Color.getHSBColor(i*0.01f, (j-halfHeight)*2*0.01f, 1f);
                }
                graphics.setColor(c);
                graphics.fillRect(i, j, 1, 1);
            }
        }
    }
    
}
