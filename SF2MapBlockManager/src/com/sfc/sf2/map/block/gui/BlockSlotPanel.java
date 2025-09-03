/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractBasicPanel;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.map.block.MapBlock;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author wiz
 */
public class BlockSlotPanel extends AbstractBasicPanel {
    
    protected MapBlock block;
    private BufferedImage overrideImage;    //Required to render a non-block
    
    public BlockSlotPanel() {
        setDisplayScale(2);
        setGridDimensions(8, 8);
    }

    @Override
    protected boolean hasData() {
        return block != null || overrideImage != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return new Dimension(PIXEL_WIDTH, PIXEL_HEIGHT);
    }

    @Override
    protected void paintImage(Graphics graphics) {
        if (overrideImage != null) {
            graphics.drawImage(overrideImage, 0, 0, null);
        } else if (block != null) {
            graphics.drawImage(block.getIndexedColorImage(), 0, 0, null);
        }
    }
    
    public MapBlock getBlock() {
        return block;
    }
    
    public int getBlockIndex() {
        return block == null ? -1 : block.getIndex();
    }

    public void setBlock(MapBlock block) {
        this.block = block;
        overrideImage = null;
        redraw();
        repaint();
    }
    
    public BufferedImage getOverrideImage() {
        return overrideImage;
    }

    public void setOverrideImage(BufferedImage overrideImage) {
        this.overrideImage = overrideImage;
        block = null;
        redraw();
        repaint();
    }
}
