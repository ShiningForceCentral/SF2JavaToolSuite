/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.BasicAction;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.*;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.map.block.MapTile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author TiMMy
 */
public class TilesetsLayoutPanel extends AbstractLayoutPanel {
    
    private static final int DEFAULT_TILES_PER_ROW = 20;
    public static int selectedTileIndexLeft = -1;
    public static int selectedTileIndexRight = -1;
    
    private Tileset[] tilesets;
    private int selectedTileset = 0;
    
    private EditableBlockSlotPanel blockSlotPanel;
    private TileSlotPanel leftSlotTilePanel;
    private TileSlotPanel rightSlotTilePanel;
    
    public TilesetsLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2);
        scale = new LayoutScale(2);
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, true, selectedTileIndexLeft, 4, 0);
        coordsHeader = new LayoutCoordsHeader(this, PIXEL_WIDTH, PIXEL_HEIGHT, true);
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, PIXEL_WIDTH, PIXEL_HEIGHT);
        scroller = null;
        setItemsPerRow(DEFAULT_TILES_PER_ROW);
    }

    @Override
    protected boolean hasData() {
        return tilesets != null && selectedTileset >= 0 && selectedTileset < tilesets.length && tilesets[selectedTileset] != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        if (tilesets[selectedTileset] == null) {
            return new Dimension();
        } else {
            return tilesets[selectedTileset].getDimensions(getItemsPerRow());
        }
    }

    @Override
    protected void drawImage(Graphics graphics) {
        int tilesPerRow = getItemsPerRow();
        graphics.drawImage(tilesets[selectedTileset].getIndexedColorImage(), 0, 0, null);
        if (selectedTileIndexLeft >= 0) {
            Graphics2D g2 = (Graphics2D)graphics;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.RED);
            int baseX = (selectedTileIndexLeft%tilesPerRow)*PIXEL_WIDTH;
            int baseY = (selectedTileIndexLeft/tilesPerRow)*PIXEL_HEIGHT;
            g2.drawRect(baseX-1, baseY-1, PIXEL_WIDTH+2, PIXEL_HEIGHT+2);
        }
        if (selectedTileIndexRight >= 0) {
            Graphics2D g2 = (Graphics2D)graphics;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLUE);
            int baseX = (selectedTileIndexRight%tilesPerRow)*PIXEL_WIDTH;
            int baseY = (selectedTileIndexRight/tilesPerRow)*PIXEL_HEIGHT;
            g2.drawRect(baseX-1, baseY-1, PIXEL_WIDTH+2, PIXEL_HEIGHT+2);
        }
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
        selectedTileset = 0;
        setSelectedLeftSlot(-1);
        setSelectedRightSlot(-1);
        this.redraw();
    }
    
    public int getSelectedTileset() {
        return selectedTileset;
    }

    public void setSelectedTileset(int selectedTileset) {
        this.selectedTileset = selectedTileset;
        setSelectedLeftSlot(-1);
        setSelectedRightSlot(-1);
        this.redraw();
    }

    public void setSelectedTilesetById(int tilesetId) {
        if (tilesets == null) return;
        String stringId = Integer.toString(tilesetId);
        for (int i = 0; i < tilesets.length; i++) {
            if (tilesets[i].getName().equals(stringId)) {
                setSelectedTileset(i);
                return;
            }
        }
        Console.logger().warning("Could not find tileset in map layout tilesets. ID : " + tilesetId);
    }

    public EditableBlockSlotPanel getBlockSlotPanel() {
        return blockSlotPanel;
    }

    public void setBlockSlotPanel(EditableBlockSlotPanel blockSlotPanel) {
        this.blockSlotPanel = blockSlotPanel;
    }

    public TileSlotPanel getLeftSlotBlockPanel() {
        return leftSlotTilePanel;
    }

    public void setLeftSlotTilePanel(TileSlotPanel leftSlotTilePanel) {
        this.leftSlotTilePanel = leftSlotTilePanel;
    }

    public TileSlotPanel getRightSlotBlockPanel() {
        return rightSlotTilePanel;
    }

    public void setRightSlotBlockPanel(TileSlotPanel rightSlotTilePanel) {
        this.rightSlotTilePanel = rightSlotTilePanel;
    }
    
    private void setSelectedLeftSlot(int index) {
        if (leftSlotTilePanel == null) return;
        selectedTileIndexLeft = index;
        MapTile tile = index == -1 ? null : new MapTile(selectedTileset*MapBlockHelpers.TILESET_TILES + index);
        leftSlotTilePanel.setTile(tile);
        leftSlotTilePanel.redraw();
        this.redraw();
    }
    
    private void setSelectedRightSlot(int index) {
        if (rightSlotTilePanel == null) return;
        selectedTileIndexRight = index;
        MapTile tile = index == -1 ? null : new MapTile(selectedTileset*MapBlockHelpers.TILESET_TILES + index);
        rightSlotTilePanel.setTile(tile);
        rightSlotTilePanel.redraw();
        this.redraw();
    }

    private void onMouseButtonInput(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (evt.released()) return;
        int x = evt.x();
        int y = evt.y();
        int tileIndex = x+y*getItemsPerRow();
        if (tilesets[selectedTileset] == null) return;
        if (tileIndex < 0 || tileIndex >= tilesets[selectedTileset].getTiles().length) return;
        
        if (evt.mouseButton() == MouseEvent.BUTTON1) {
            if (leftSlotTilePanel != null) {
                int index = selectedTileIndexLeft == tileIndex ? -1 : tileIndex;
                ActionManager.setAndExecuteAction(new BasicAction<Integer>(this, "Tile Selection - Left", this::setSelectedLeftSlot, index, selectedTileIndexLeft));
            }
        } else if (evt.mouseButton() == MouseEvent.BUTTON3) {
            if (rightSlotTilePanel != null) {
                int index = selectedTileIndexRight == tileIndex ? -1 : tileIndex;
                ActionManager.setAndExecuteAction(new BasicAction<Integer>(this, "Tile Selection - Right", this::setSelectedRightSlot, index, selectedTileIndexRight));
            }
        }
    }
}
