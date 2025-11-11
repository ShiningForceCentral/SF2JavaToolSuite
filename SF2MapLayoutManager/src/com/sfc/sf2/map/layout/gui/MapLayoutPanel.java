/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.layout.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.map.layout.MapLayout;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import com.sfc.sf2.map.layout.gui.resources.MapLayoutFlagIcons;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 *
 * @author TiMMy
 */
public class MapLayoutPanel extends AbstractLayoutPanel {
    private static final int DEFAULT_BLOCKS_PER_ROW = MapLayout.BLOCK_WIDTH;
    private static final Dimension MAP_DIMENSIONS = new Dimension(BLOCK_WIDTH*PIXEL_WIDTH, BLOCK_HEIGHT*PIXEL_HEIGHT);
        
    protected MapLayout layout;
    
    private boolean showExplorationFlags = false;
    private boolean showInteractionFlags = false;
    private boolean showPriority = false;

    public MapLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/3);
        scale = new LayoutScale(1);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, false, 0, 0, 1);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT, false);
        mouseInput = null;
        scroller = new LayoutScrollNormaliser(this);
        setItemsPerRow(DEFAULT_BLOCKS_PER_ROW);
        
        setSize(MAP_DIMENSIONS);
    }

    @Override
    public Dimension getMinimumSize() {
        return MAP_DIMENSIONS;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.width < MAP_DIMENSIONS.width || preferredSize.height < MAP_DIMENSIONS.height) {
            setPreferredSize(MAP_DIMENSIONS);
            return MAP_DIMENSIONS;
        }
        return preferredSize;
    }

    @Override
    protected boolean hasData() {
        return layout != null && layout.getBlocks() != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        return MAP_DIMENSIONS;
    }

    @Override
    protected void drawImage(Graphics graphics) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                graphics.drawImage(blocks[x+y*BLOCK_WIDTH].getMapBlock().getIndexedColorImage(layout.getTilesets()), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
            }
        }
        if (getShowExplorationFlags() || getShowInteractionFlags()) {
            drawFlags(blocks, graphics);
        }
        if (showPriority) {
            for (int y=0; y < BLOCK_HEIGHT; y++) {
                for (int x=0; x < BLOCK_WIDTH; x++) {
                    MapBlockHelpers.drawTilePriorities(graphics, blocks[x+y*BLOCK_WIDTH].getMapBlock(), layout.getTilesets(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT);
                }
            }
        }
    }
    
    protected void drawFlags(MapLayoutBlock[] blocks, Graphics graphics) {
        boolean showExplorationFlags = getShowExplorationFlags();
        boolean showInteractionFlags = getShowInteractionFlags();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                if (showExplorationFlags) {
                    ImageIcon icon = MapLayoutFlagIcons.getBlockExplorationFlagIcon(blocks[x+y*BLOCK_WIDTH].getExplorationFlags());
                    if (icon != null) {
                        graphics.drawImage(icon.getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null); 
                    }
                }
                if (showInteractionFlags) {
                    ImageIcon icon = MapLayoutFlagIcons.getBlockInteractionFlagIcon(blocks[x+y*BLOCK_WIDTH].getEventFlags());
                    if (icon != null) {
                        graphics.drawImage(icon.getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null); 
                    }
                }
            }
        }
    }
    
    public void centerOnMapPoint(int mapX, int mapY) {
        if (BaseLayoutComponent.IsEnabled(scroller)) {
            int scaleOffset = 15/getDisplayScale();
            scroller.scrollToPosition((mapX-scaleOffset)*PIXEL_WIDTH*getDisplayScale(), (mapY-scaleOffset)*PIXEL_HEIGHT*getDisplayScale());
        }
    }

    public MapLayout getMapLayout() {
        return layout;
    }

    public void setMapLayout(MapLayout layout) {
        this.layout = layout;
        redraw();
    }

    public boolean getShowExplorationFlags() {
        return showExplorationFlags;
    }

    public void setShowExplorationFlags(boolean showExplorationFlags) {
        this.showExplorationFlags = showExplorationFlags;
        redraw();
    }
    
    public boolean getShowInteractionFlags() {
        return showInteractionFlags;
    }

    public void setShowInteractionFlags(boolean showInteractionFlags) {
        this.showInteractionFlags = showInteractionFlags;
        redraw();
    }

    public boolean getShowPriority() {
        return showPriority;
    }

    public void setShowPriority(boolean showPriority) {
        this.showPriority = showPriority;
        redraw();
    }
}
