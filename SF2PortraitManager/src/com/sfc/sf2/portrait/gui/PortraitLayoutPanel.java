/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.controls.Console;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.portrait.Portrait;
import com.sfc.sf2.portrait.models.PortraitDataTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author wiz
 */
public class PortraitLayoutPanel extends AbstractLayoutPanel implements MouseListener {
    
    private Portrait portrait;
    
    private PortraitDataTableModel eyeAnimTable;
    private PortraitDataTableModel mouthAnimTable;
    
    private boolean blinking = false;
    private boolean speaking = false;
    
    private int selectedEyeTile = -1;
    private int selectedMouthTile = -1;
        
    public PortraitLayoutPanel() {
        super();
        setGridDimensions(8, 8);
        setCoordsDimensions(8, 8, 0);
        addMouseListener(this);
    }

    @Override
    protected boolean hasData() {
        return portrait != null && portrait.getTileset() != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        int width = Portrait.PORTRAIT_TILES_FULL_WIDTH*PIXEL_WIDTH;
        int height = Portrait.PORTRAIT_TILES_HEIGHT*PIXEL_HEIGHT;
        return new Dimension(width, height);
    }

    @Override
    protected void buildImage(Graphics graphics) {
        portrait.clearIndexedColorImage();
        graphics.drawImage(portrait.getIndexedColorImage(true, blinking, speaking), 0, 0, null);
        graphics.setColor(Color.YELLOW);
        if (selectedEyeTile >= 0 && selectedEyeTile < eyeAnimTable.getRowCount()) {
            int[] item = eyeAnimTable.getRow(selectedEyeTile);
            graphics.drawRect(item[0]*PIXEL_WIDTH, item[1]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            graphics.drawRect(item[2]*PIXEL_WIDTH, item[3]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }
        if (selectedMouthTile >= 0 && selectedMouthTile < mouthAnimTable.getRowCount()) {
            int[] item = mouthAnimTable.getRow(selectedMouthTile);
            graphics.drawRect(item[0]*PIXEL_WIDTH, item[1]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            graphics.drawRect(item[2]*PIXEL_WIDTH, item[3]*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }
    }
    
    public Portrait getPortrait() {
        return portrait;
    }

    public void setPortrait(Portrait portrait) {
        this.portrait = portrait;
        redraw();
    }
    
    public PortraitDataTableModel getEyeAnimTable() {
        return eyeAnimTable;
    }

    public void setEyeAnimTable(PortraitDataTableModel eyeAnimTable) {
        this.eyeAnimTable = eyeAnimTable;
    }

    public PortraitDataTableModel getMouthAnimTable() {
        return mouthAnimTable;
    }

    public void setMouthAnimTable(PortraitDataTableModel mouthAnimTable) {
        this.mouthAnimTable = mouthAnimTable;
    }

    public int getSelectedEyeTile() {
        return selectedEyeTile;
    }

    public void setSelectedEyeTile(int selectedEyeTile) {
        this.selectedEyeTile = selectedEyeTile;
        this.selectedMouthTile = -1;
        redraw();
    }

    public int getSelectedMouthTile() {
        return selectedMouthTile;
    }

    public void setSelectedMouthTile(int selectedMouthTile) {
        this.selectedMouthTile = selectedMouthTile;
        this.selectedEyeTile = -1;
        redraw();
    }

    public boolean getBlinking() {
        return blinking;
    }

    public void setBlinking(boolean blinking) {
        if (this.blinking != blinking) {
            this.blinking = blinking;
            redraw();
        }
    }

    public boolean getSpeaking() {
        return speaking;
    }

    public void setSpeaking(boolean speaking) {
        if (this.speaking != speaking) {
            this.speaking = speaking;
            redraw();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mousePressed(MouseEvent e) {
        Dimension coordsPadding = getCoordsPadding();
        int x = e.getX() - coordsPadding.width;
        int y = e.getY() - coordsPadding.height;
        x /= (getDisplayScale() * PIXEL_WIDTH);
        y /= (getDisplayScale() * PIXEL_HEIGHT);
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                if (selectedEyeTile >= 0) {
                    int[] item = eyeAnimTable.getRow(selectedEyeTile);
                    if (x < 6) {
                        item[0] = x;
                        item[1] = y;
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 0);
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 1);
                    } else {
                        item[2] = x;
                        item[3] = y;
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 2);
                        eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 3);
                    }
                    redraw();
                    revalidate();
                    repaint();
                    
                }
                if (selectedMouthTile >= 0) {
                    int[] item = mouthAnimTable.getRow(selectedMouthTile);
                    if (x < 6) {
                        item[0] = x;
                        item[1] = y;
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 0);
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 1);
                    } else {
                        item[2] = x;
                        item[3] = y;
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 2);
                        mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 3);
                    }
                    redraw();
                    revalidate();
                    repaint();
                }
                break;
            case MouseEvent.BUTTON2:
                break;
            case MouseEvent.BUTTON3:
                break;
            default:
                break;
        }
        redraw();
        Console.logger().finest("Portrait press "+e.getButton()+" -- "+x+" - "+y);
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
       
    }
}
