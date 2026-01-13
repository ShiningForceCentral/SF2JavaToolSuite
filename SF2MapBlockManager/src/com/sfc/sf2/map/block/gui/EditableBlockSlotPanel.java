/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import com.sfc.sf2.core.gui.layout.LayoutGrid;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Block.TILE_WIDTH;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.map.block.MapTile;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 *
 * @author TiMMy
 */
public class EditableBlockSlotPanel extends BlockSlotPanel {
    
    public enum BlockSlotEditMode {
        MODE_PAINT_TILE,
        MODE_TOGGLE_PRIORITY,
        MODE_TOGGLE_FLIP,
    }
    
    public enum ActiveMode {
        None,
        On,
        Off,
    }
    
    private TileSlotPanel leftTileSlotPanel;
    private TileSlotPanel rightTileSlotPanel;
    
    private BlockSlotEditMode currentMode = BlockSlotEditMode.MODE_PAINT_TILE;
    private boolean showPriorityFlag;
    
    private ActionListener blockEditedListener;
    
    public EditableBlockSlotPanel() {
        super();
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, PIXEL_WIDTH, PIXEL_HEIGHT);
        setRenderScaleIndex(4);
    }
    
    @Override
    protected void drawImage(Graphics graphics) {
        super.drawImage(graphics);
        if (showPriorityFlag) {
            MapBlockHelpers.drawTilePriorities(graphics, block, tilesets, 0, 0);
        }
    }

    public void setBlockEditedListener(ActionListener blockEditedListener) {
        this.blockEditedListener = blockEditedListener;
    }
    
    public BlockSlotEditMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(BlockSlotEditMode currentMode) {
        this.currentMode = currentMode;
    }
    
    public boolean getShowPriority() {
        return showPriorityFlag;
    }

    public void setShowPriority(boolean showPriorityFlag) {
        this.showPriorityFlag = showPriorityFlag;
        redraw();
    }
    
    private void onBlockEdited() {
        block.clearIndexedColorImage();
        redraw();
        if (blockEditedListener != null) {
            blockEditedListener.actionPerformed(new ActionEvent(this, block.getIndex(), null));
        }
    }
    
    public TileSlotPanel getLeftTileSlotPanel() {
        return leftTileSlotPanel;
    }

    public void setLeftTileSlotPanel(TileSlotPanel leftTileSlotPanel) {
        this.leftTileSlotPanel = leftTileSlotPanel;
    }
    
    public TileSlotPanel getRightTileSlotPanel() {
        return rightTileSlotPanel;
    }

    public void setRightTileSlotPanel(TileSlotPanel rightTileSlotPanel) {
        this.rightTileSlotPanel = rightTileSlotPanel;
    }
    
    private void onMouseButtonInput(GridMousePressedEvent evt) {
        if (evt.released()) return;
        if (block == null) return;
        int index = evt.x()+evt.y()*TILE_WIDTH;
        switch (currentMode) {
            case MODE_PAINT_TILE:
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    MapTile leftSlotTile = leftTileSlotPanel.getTile();
                    if (leftSlotTile != null) {
                        MapTile[] tiles = block.getMapTiles();
                        tiles[index] = leftSlotTile.clone();
                        onBlockEdited();
                    }
                }
                else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    MapTile rightSlotTile = rightTileSlotPanel.getTile();
                    if (rightSlotTile != null) {
                        MapTile[] tiles = block.getMapTiles();
                        tiles[index] = rightSlotTile.clone();
                        onBlockEdited();
                    }
                }
                break;
            case MODE_TOGGLE_FLIP:
                MapTile tile = block.getMapTiles()[index];
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    tile = tile.clone();
                    tile.getTileFlags().toggleFlag(TileFlags.TILE_FLAG_HFLIP);
                }
                else if (evt.mouseButton() == MouseEvent.BUTTON2) {
                    TileFlags flags = block.getMapTiles()[index].getTileFlags();
                    if (flags.isHFlip() || flags.isVFlip()) {
                        tile = tile.clone();
                        flags = tile.getTileFlags();
                        flags.removeFlag(TileFlags.TILE_FLAG_HFLIP);
                        flags.removeFlag(TileFlags.TILE_FLAG_VFLIP);
                    }
                }
                else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    tile = tile.clone();
                    tile.getTileFlags().toggleFlag(TileFlags.TILE_FLAG_VFLIP);
                }
                block.getMapTiles()[index] = tile;
                onBlockEdited();
                break;
            case MODE_TOGGLE_PRIORITY:
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    block.getMapTiles()[index].getTileFlags().addFlag(TileFlags.TILE_FLAG_PRIORITY);
                    onBlockEdited();
                } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    block.getMapTiles()[index].getTileFlags().removeFlag(TileFlags.TILE_FLAG_PRIORITY);
                    onBlockEdited();
                }
                break;
        }
    }
}
