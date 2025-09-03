/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tileset;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author TiMMy
 */
public class TilesetsLayoutPanel extends AbstractLayoutPanel implements MouseListener, MouseMotionListener {
    
    private static final int DEFAULT_TILES_PER_ROW = 24;
    public static int selectedTileIndex0;
    public static int selectedTileIndex1;
    
    private Tileset[] tilesets;
    private int selectedTileset = 0;
    
    private EditableBlockSlotPanel blockSlotPanel;
    private TileSlotPanel leftSlotTilePanel;
    private TileSlotPanel rightSlotTilePanel;
    
    public TilesetsLayoutPanel() {
        super();
        tilesPerRow = DEFAULT_TILES_PER_ROW;
        setGridDimensions(PIXEL_WIDTH, PIXEL_HEIGHT);
        
        addMouseListener(this);
        addMouseMotionListener(this);
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
            return tilesets[selectedTileset].getDimensions(tilesPerRow);
        }
    }

    @Override
    protected void paintImage(Graphics graphics) {
        graphics.drawImage(tilesets[selectedTileset].getIndexedColorImage(), 0, 0, null);
    }

    public Tileset[] getTilesets() {
        return tilesets;
    }

    public void setTilesets(Tileset[] tilesets) {
        this.tilesets = tilesets;
    }
    
    public int getSelectedTileset() {
        return selectedTileset;
    }

    public void setSelectedTileset(int selectedTileset) {
        this.selectedTileset = selectedTileset;
        this.redraw();
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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / (getDisplayScale()*PIXEL_WIDTH);
        int y = e.getY() / (getDisplayScale()*PIXEL_HEIGHT);
        int tileIndex = x+y*tilesPerRow;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (leftSlotTilePanel != null) {
                if (tilesets[selectedTileset] == null) {
                    leftSlotTilePanel.setTile(null);
                } else {
                    leftSlotTilePanel.setTile(tilesets[selectedTileset].getTiles()[tileIndex]);
                }
                leftSlotTilePanel.revalidate();
                leftSlotTilePanel.repaint();
            }
        } else if(e.getButton() == MouseEvent.BUTTON3){
            if (rightSlotTilePanel != null) {
                if (tilesets[selectedTileset] == null) {
                    rightSlotTilePanel.setTile(null);
                } else {
                    rightSlotTilePanel.setTile(tilesets[selectedTileset].getTiles()[tileIndex]);
                }
                rightSlotTilePanel.revalidate();
                rightSlotTilePanel.repaint();
            }
        }
    }    

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
