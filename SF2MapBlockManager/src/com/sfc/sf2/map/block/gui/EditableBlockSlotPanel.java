/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.map.block.MapBlock;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author TiMMy
 */
public class EditableBlockSlotPanel extends BlockSlotPanel implements MouseListener, MouseMotionListener {
    public enum BlockSlotEditMode {
        MODE_PAINT_TILE,
        MODE_TOGGLE_PRIORITY,
        MODE_TOGGLE_FLIP,
    }
    
    private MapBlocksetLayoutPanel mapBlocksetLayout;
    private TileSlotPanel leftTileSlotPanel;
    private TileSlotPanel rightTileSlotPanel;
    
    private BlockSlotEditMode currentMode = BlockSlotEditMode.MODE_PAINT_TILE;
    private boolean showPriorityFlag;
    
    BufferedImage priorityImage;
    
    public EditableBlockSlotPanel() {
        setDisplayScale(4);

        addMouseListener(this);
        addMouseMotionListener(this);
    }
    @Override
    protected void paintImage(Graphics graphics) {
        super.paintImage(graphics);
        if (priorityImage == null) {
            Graphics2D g2 = (Graphics2D)graphics;
            if (showPriorityFlag) {
                Tile[] tiles = block.getTiles();
                for (int t = 0; t < tiles.length; t++) {
                    if (tiles[t].isHighPriority()) {
                        g2.setColor(Color.BLACK);
                        g2.fillRect((t%3)*8+2, (t/3)*8+2, 4, 4);
                        g2.setColor(Color.YELLOW);
                        g2.fillRect((t%3)*8+3, (t/3)*8+3, 2, 2);
                    }
                }
            }
        }
    }
    
    public void setMapBlocksetLayout(MapBlocksetLayoutPanel mapBlocksetLayout) {
        this.mapBlocksetLayout = mapBlocksetLayout;
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
        priorityImage = null;
        redraw();
    }

    @Override
    public void setBlock(MapBlock block) {
        priorityImage = null;
        super.setBlock(block);
    }
    
    private void onBlockUpdated() {
        block.clearIndexedColorImage(true);
        mapBlocksetLayout.getBlockset().clearIndexedColorImage(false);
        mapBlocksetLayout.redraw();
        mapBlocksetLayout.revalidate();
        mapBlocksetLayout.repaint();
        this.priorityImage = null;
        redraw();
        repaint();
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

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mouseDragged(MouseEvent e) { }
    @Override
    public void mouseMoved(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) {
        if (block == null)
            return;
        
        int x = e.getX() / (getWidth() / 3);
        int y = e.getY() / (getHeight() / 3);
        if (x < 0 || x >= 3 || y < 0 || y >= 3) {
            return;
        }
        switch (currentMode) {
            case MODE_PAINT_TILE:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Tile leftSlotTile = leftTileSlotPanel.getTile();
                    if (leftSlotTile != null) {
                        Tile[] tiles = block.getTiles();
                        tiles[x + y*3] = leftSlotTile.clone(tiles[x + y*3].isHighPriority(), leftSlotTile.ishFlip(), leftSlotTile.isvFlip(), leftSlotTile.getPalette());
                        onBlockUpdated();
                    }
                }
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    Tile rightSlotTile = rightTileSlotPanel.getTile();
                    if (rightSlotTile != null) {
                        Tile[] tiles = block.getTiles();
                        tiles[x + y*3] = rightSlotTile.clone(tiles[x + y*3].isHighPriority(), rightSlotTile.ishFlip(), rightSlotTile.isvFlip(), rightSlotTile.getPalette());
                        onBlockUpdated();
                    }
                }
                break;
            case MODE_TOGGLE_FLIP:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    block.getTiles()[x + y*3] = Tile.hFlip(block.getTiles()[x + y*3]);
                    onBlockUpdated();
                }
                else if (e.getButton() == MouseEvent.BUTTON2) {
                    if (block.getTiles()[x + y*3].ishFlip() || block.getTiles()[x + y*3].isvFlip()) {
                        block.getTiles()[x + y*3] = Tile.clearFlip(block.getTiles()[x + y*3]);
                        onBlockUpdated();
                    }
                }
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    block.getTiles()[x + y*3] = Tile.vFlip(block.getTiles()[x + y*3]);
                    onBlockUpdated();
                }
                break;
            case MODE_TOGGLE_PRIORITY:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    block.getTiles()[x + y*3].setHighPriority(!block.getTiles()[x + y*3].isHighPriority());
                    onBlockUpdated();
                }
                break;
        }
    }
}
