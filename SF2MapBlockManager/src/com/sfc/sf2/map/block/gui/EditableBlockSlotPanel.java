/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.CumulativeAction;
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
import java.util.List;

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
    private TileFlags actionSetFlags;
    private boolean actionSetFlagOn;
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
            
            if (newTile != null && tile.getTileIndex() != newTile.getTileIndex()) {
                BlockTileActionData newValue = new BlockTileActionData(block, newTile.clone(), index);
                BlockTileActionData oldValue = new BlockTileActionData(block, tile, index);
                ActionManager.setAndExecuteAction(new CumulativeAction<BlockTileActionData>(this, "Set Block Tile", this::actionChangeTile, newValue, oldValue));
            }
        } else {
            if (tile == null) return;
            if (!evt.dragging()) {
                actionSetFlags = null;
                if (currentMode == BlockSlotEditMode.MODE_TOGGLE_FLIP) {
                    if (evt.mouseButton() == MouseEvent.BUTTON1) {
                        actionSetFlags = new TileFlags(TileFlags.TILE_FLAG_HFLIP);
                        actionSetFlagOn = !tile.getTileFlags().isHFlip();
                    } else if (evt.mouseButton() == MouseEvent.BUTTON2) {
                        actionSetFlags = new TileFlags(TileFlags.TILE_FLAG_BOTHFLIP);
                        actionSetFlagOn = false;
                    } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                        actionSetFlags = new TileFlags(TileFlags.TILE_FLAG_VFLIP);
                        actionSetFlagOn = !tile.getTileFlags().isVFlip();
                    }
                } else if (currentMode == BlockSlotEditMode.MODE_TOGGLE_PRIORITY) {
                    if (evt.mouseButton() == MouseEvent.BUTTON1) {
                        actionSetFlags = new TileFlags(TileFlags.TILE_FLAG_PRIORITY);
                        actionSetFlagOn = true;
                    } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
                        actionSetFlags = new TileFlags(TileFlags.TILE_FLAG_PRIORITY);
                        actionSetFlagOn = false;
                    }
                }
            }
            
            if (actionSetFlags != null) {
                boolean flagOn = false;
                if (evt.pressed() && (!actionSetFlags.equals(TileFlags.TILE_FLAG_BOTHFLIP) || !actionSetFlags.equals(TileFlags.TILE_FLAG_PRIORITY))) {
                    flagOn = (tile.getTileFlags().value() & actionSetFlags.value()) != 0;
                }
                if (actionSetFlagOn != flagOn) {
                    TileFlagsActionData newValue = new TileFlagsActionData(block, actionSetFlags, actionSetFlagOn, index);
                    TileFlagsActionData oldValue = new TileFlagsActionData(block, actionSetFlags, !actionSetFlagOn, index);
                    ActionManager.setAndExecuteAction(new CumulativeAction<TileFlagsActionData>(this, "Set Tile Flags", this::actionSetMultipleTileFlags, newValue, oldValue));
                }
            }
        }
    }
    
    private void actionChangeTile(List<BlockTileActionData> values) {
        for (BlockTileActionData value : values) {
            value.block().getMapTiles()[value.tileIndex()] = value.tile();
        }
        onBlockEdited();
    }
    
    private void actionSetMultipleTileFlags(List<TileFlagsActionData> values) {
        for (TileFlagsActionData value : values) {
            TileFlags flag = value.block().getMapTiles()[value.tileIndex()].getTileFlags();
            flag.setFlag(value.flag().value(), value.flagOn());
        }
        onBlockEdited();
    }
}
