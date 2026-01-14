/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.portrait.gui;

import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent.GridMousePressedEvent;
import static com.sfc.sf2.graphics.Tile.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Tile.PIXEL_WIDTH;
import com.sfc.sf2.portrait.Portrait;
import static com.sfc.sf2.portrait.Portrait.PORTRAIT_TILES_FULL_WIDTH;
import static com.sfc.sf2.portrait.Portrait.PORTRAIT_TILES_HEIGHT;
import static com.sfc.sf2.portrait.Portrait.PORTRAIT_TILES_WIDTH;
import com.sfc.sf2.portrait.models.PortraitDataTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author wiz
 */
public class PortraitLayoutPanel extends AbstractLayoutPanel {
    
    private Portrait portrait;
    
    private PortraitDataTableModel eyeAnimTable;
    private PortraitDataTableModel mouthAnimTable;
    
    private boolean blinking = false;
    private boolean speaking = false;
    
    private int selectedEyeTile = -1;
    private int selectedMouthTile = -1;
    private int clickIndicator;
        
    public PortraitLayoutPanel() {
        super();
        background = new LayoutBackground(Color.LIGHT_GRAY, PIXEL_WIDTH/2);
        scale = new LayoutScale();
        grid = new LayoutGrid(PIXEL_WIDTH, PIXEL_HEIGHT, PORTRAIT_TILES_WIDTH*PIXEL_WIDTH, -1);
        coordsGrid = new LayoutCoordsGridDisplay(PIXEL_WIDTH, PIXEL_HEIGHT, false);
        coordsHeader = null;
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, PIXEL_WIDTH, PIXEL_HEIGHT);
        scroller = null;
    }

    @Override
    protected boolean hasData() {
        return portrait != null && portrait.getTileset() != null;
    }

    @Override
    protected Dimension getImageDimensions() {
        int width = PORTRAIT_TILES_FULL_WIDTH*PIXEL_WIDTH;
        int height = PORTRAIT_TILES_HEIGHT*PIXEL_HEIGHT;
        return new Dimension(width, height);
    }

    @Override
    protected void drawImage(Graphics graphics) {
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
    
    private void onMouseButtonInput(GridMousePressedEvent evt) {
        if (evt.released()) {
            clickIndicator = 0;
            return;
        }
        if (evt.x() < 0 || evt.x() >= PORTRAIT_TILES_FULL_WIDTH || evt.y() < 0 || evt.y() >= PORTRAIT_TILES_HEIGHT) return;
        if (evt.x() < 6 && clickIndicator >= 1 || evt.x() >= 6 && clickIndicator <= -1) return; //Cannot drag between left/right regions
        clickIndicator = evt.x() < 6 ? -1 : 1;
        if (selectedEyeTile >= 0) {
            int[] item = eyeAnimTable.getRow(selectedEyeTile);
            if (evt.x() < 6) {
                item[0] = evt.x();
                item[1] = evt.y();
                eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 0);
                eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 1);
            } else {
                item[2] = evt.x();
                item[3] = evt.y();
                eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 2);
                eyeAnimTable.fireTableCellUpdated(selectedEyeTile, 3);
            }
            redraw();
            revalidate();
            repaint();

        }
        else if (selectedMouthTile >= 0) {
            int[] item = mouthAnimTable.getRow(selectedMouthTile);
            if (evt.x() < 6) {
                item[0] = evt.x();
                item[1] = evt.y();
                mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 0);
                mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 1);
            } else {
                item[2] = evt.x();
                item[3] = evt.y();
                mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 2);
                mouthAnimTable.fireTableCellUpdated(selectedMouthTile, 3);
            }
            redraw();
            revalidate();
            repaint();
        }
        //Console.logger().finest("Portrait press "+e.mouseButton()+" -- "+e.x()+" - "+e.y());
    }
}
