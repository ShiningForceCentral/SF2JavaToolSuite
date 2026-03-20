/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.CustomAction;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import com.sfc.sf2.core.gui.layout.LayoutGrid;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Block.TILE_WIDTH;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.TileFlags;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import com.sfc.sf2.map.block.MapTile;
import com.sfc.sf2.map.block.actions.BlockTileActionData;
import com.sfc.sf2.map.block.actions.TileFlagsActionData;
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
        setRenderScaleIndex(RenderScaleHelpers.stringToIndex("4x"));
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
        MapTile tile = block.getMapTiles()[index];
        if (currentMode == BlockSlotEditMode.MODE_PAINT_TILE) {
            MapTile newTile = null;
            if (evt.mouseButton() == MouseEvent.BUTTON1) {
                newTile = leftTileSlotPanel.getTile();
            } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                newTile = rightTileSlotPanel.getTile();
            }
            
            if (newTile != null) {
                BlockTileActionData newValue = new BlockTileActionData(block, index, newTile.clone());
                BlockTileActionData oldValue = new BlockTileActionData(block, index, tile);
                ActionManager.setAndExecuteAction(new CustomAction<BlockTileActionData>(this, "Set Block Tile", this::ActionChangeTile, newValue, oldValue));
            }
        } else {
            if (tile == null) return;
            TileFlags newFlags = null;
            if (currentMode == BlockSlotEditMode.MODE_TOGGLE_FLIP) {
                newFlags = tile.getTileFlags().clone();
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    newFlags.toggleFlag(TileFlags.TILE_FLAG_HFLIP);
                } else if (evt.mouseButton() == MouseEvent.BUTTON2) {
                    newFlags.removeFlag(TileFlags.TILE_FLAG_HFLIP);
                    newFlags.removeFlag(TileFlags.TILE_FLAG_VFLIP);
                } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    newFlags.toggleFlag(TileFlags.TILE_FLAG_VFLIP);
                }
            } else if (currentMode == BlockSlotEditMode.MODE_TOGGLE_PRIORITY) {
                newFlags = tile.getTileFlags().clone();
                if (evt.mouseButton() == MouseEvent.BUTTON1) {
                    newFlags.addFlag(TileFlags.TILE_FLAG_PRIORITY);
                } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                    newFlags.removeFlag(TileFlags.TILE_FLAG_PRIORITY);
                }
            }
            
            if (newFlags != null) {
                TileFlagsActionData newValue = new TileFlagsActionData(tile, index, newFlags);
                TileFlagsActionData oldValue = new TileFlagsActionData(tile, index, tile.getTileFlags());
                ActionManager.setAndExecuteAction(new CustomAction<TileFlagsActionData>(this, "Set Tile Flags", this::ActionSetTileFlags, newValue, oldValue));
            }
        }
    }
    
    private void ActionChangeTile(BlockTileActionData value) {
        value.block().getMapTiles()[value.index()] = value.tile();
        onBlockEdited();
    }
    
    private void ActionSetTileFlags(TileFlagsActionData value) {
        value.tile().setTileFlags(value.flags());
        onBlockEdited();
    }
}
