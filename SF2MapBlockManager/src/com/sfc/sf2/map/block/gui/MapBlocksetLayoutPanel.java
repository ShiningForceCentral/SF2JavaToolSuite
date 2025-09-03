/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.graphics.Block;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.graphics.Tile;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author wiz
 */
public class MapBlocksetLayoutPanel extends AbstractLayoutPanel implements MouseListener, MouseMotionListener {
    
    private static final int DEFAULT_BLOCKS_PER_ROW = 8;
    
    public static int selectedBlockIndexLeft;
    public static int selectedBlockIndexRight;
    
    private BlockSlotPanel leftSlotBlockPanel;
    private BlockSlotPanel rightSlotBlockPanel;
    
    private MapBlockset blockset;
    private boolean showPriority = false;

    public MapBlocksetLayoutPanel() {
        super();
        tilesPerRow = DEFAULT_BLOCKS_PER_ROW;
        setGridDimensions(MapBlock.PIXEL_WIDTH, MapBlock.PIXEL_HEIGHT);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected boolean hasData() {
        return blockset != null && blockset.getBlocks().length > 0;
    }

    @Override
    protected Dimension getImageDimensions() {
        return blockset.getDimensions(getBlocksPerRow());
    }

    @Override
    protected void paintImage(Graphics graphics) {
        graphics.drawImage(blockset.getIndexedColorImage(), 0, 0, null);
        if (showPriority) {
            MapBlock[] blocks = blockset.getBlocks();
            for (int i=0; i < blocks.length; i++) {
                int baseX = (i%tilesPerRow)*PIXEL_WIDTH;
                int baseY = (i/tilesPerRow)*PIXEL_HEIGHT;
                Tile[] tiles = blocks[i].getTiles();
                for (int t = 0; t < tiles.length; t++) {
                    if (tiles[t].isHighPriority()) {
                        graphics.setColor(Color.BLACK);
                        graphics.fillRect(baseX+(t%Block.TILE_WIDTH)*Tile.PIXEL_WIDTH+2, baseY+(t/Block.TILE_WIDTH)*Tile.PIXEL_HEIGHT+2, 4, 4);
                        graphics.setColor(Color.YELLOW);
                        graphics.fillRect(baseX+(t%Block.TILE_WIDTH)*Tile.PIXEL_WIDTH+3, baseY+(t/Block.TILE_WIDTH)*Tile.PIXEL_HEIGHT+3, 2, 2);
                    }
                }
            }
        }
    }
    
    @Override
    protected void paintOverGrid(Graphics graphics, int scale) {
        super.paintOverGrid(graphics, scale);
        if (selectedBlockIndexLeft >= 0) {
            Graphics2D g2 = (Graphics2D)graphics;
            g2.setStroke(new BasicStroke(2*scale));
            g2.setColor(Color.YELLOW);
            int baseX = (selectedBlockIndexLeft%tilesPerRow)*PIXEL_WIDTH*scale;
            int baseY = (selectedBlockIndexLeft/tilesPerRow)*PIXEL_HEIGHT*scale;
            g2.drawRect(baseX-2*scale, baseY-2*scale, PIXEL_WIDTH*scale+4*scale, PIXEL_HEIGHT*scale+4*scale);
        }
    }

    public MapBlockset getBlockset() {
        return blockset;
    }

    public void setBlockset(MapBlockset blockset) {
        this.blockset = blockset;
        selectedBlockIndexLeft = selectedBlockIndexRight = -1;
        this.redraw();
    }
    
    public int getBlocksPerRow() {
        return tilesPerRow;
    }

    public void setBlocksPerRow(int blocksPerRow) {
        setItemsPerRow(blocksPerRow);
    }
    
    public boolean getShowPriority() {
        return showPriority;
    }

    public void setShowPriority(boolean showPriority) {
        this.showPriority = showPriority;
        this.redraw();
    }

    public BlockSlotPanel getLeftSlotBlockPanel() {
        return leftSlotBlockPanel;
    }
    
    public int getLeftSelectedIndex() {
        return selectedBlockIndexLeft;
    }
    
    public void setLeftSelectedIndex(int index) {
        if (leftSlotBlockPanel!=null) {
            if (index < 3 || index >= blockset.getBlocks().length) {
                selectedBlockIndexLeft = -1;
                leftSlotBlockPanel.setBlock(null);
            } else {
                selectedBlockIndexLeft = index;
                leftSlotBlockPanel.setBlock(blockset.getBlocks()[index]);
            }
            this.redraw();
        }
    }
    
    public int getRightSelectedIndex() {
        return selectedBlockIndexRight;
    }
    
    public void setRightSelectedIndex(int index) {
        if (rightSlotBlockPanel!=null) {
            if (index < 3 || index >= blockset.getBlocks().length) {
                selectedBlockIndexRight = -1;
                rightSlotBlockPanel.setBlock(null);
            } else {
                selectedBlockIndexRight = index;
                rightSlotBlockPanel.setBlock(blockset.getBlocks()[index]);
            }
            this.redraw();
        }
    }

    public void setLeftSlotBlockPanel(BlockSlotPanel leftSlotBlockPanel) {
        this.leftSlotBlockPanel = leftSlotBlockPanel;
    }

    public BlockSlotPanel getRightSlotBlockPanel() {
        return rightSlotBlockPanel;
    }

    public void setRightSlotBlockPanel(BlockSlotPanel rightSlotBlockPanel) {
        this.rightSlotBlockPanel = rightSlotBlockPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / (getDisplayScale()*PIXEL_WIDTH);
        int y = e.getY() / (getDisplayScale()*PIXEL_HEIGHT);
        int blockIndex = x+y*getBlocksPerRow();
        if (blockIndex < 0 || blockIndex >= blockset.getBlocks().length) {
            return;
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (selectedBlockIndexLeft == blockIndex) {
                setLeftSelectedIndex(-1);
            } else {
                setLeftSelectedIndex(blockIndex);
            }
            this.revalidate();
            this.repaint();
        }else if (e.getButton() == MouseEvent.BUTTON3) {
            if (selectedBlockIndexRight == blockIndex) {
                setRightSelectedIndex(-1);
            } else {
                setRightSelectedIndex(blockIndex);
            }
            this.revalidate();
            this.repaint();
        }
        //System.out.println("Blockset press "+e.getButton()+" "+x+" - "+y);
    }

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
}
