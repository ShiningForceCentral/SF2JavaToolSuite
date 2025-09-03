/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.BaseLayoutComponent;
import com.sfc.sf2.core.gui.layout.LayoutBackground;
import com.sfc.sf2.core.gui.layout.LayoutGrid;
import com.sfc.sf2.core.gui.layout.LayoutScale;
import com.sfc.sf2.core.gui.layout.LayoutCoordsGridDisplay;
import com.sfc.sf2.core.gui.layout.LayoutCoordsHeader;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import com.sfc.sf2.core.gui.layout.LayoutScrollNormaliser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractLayoutPanel extends JPanel {
    
    private static final Dimension NO_OFFSET = new Dimension();
        
    protected LayoutBackground background;
    protected LayoutScale scale;
    protected LayoutGrid grid;
    protected LayoutCoordsGridDisplay coordsGrid;
    protected LayoutCoordsHeader coordsHeader;
    protected LayoutMouseInput mouseInput;
    protected LayoutScrollNormaliser scroller;
    
    private int itemsPerRow = 8;
        
    private BufferedImage currentImage;
    private boolean redraw = true;

    public AbstractLayoutPanel() {
        super();
    }

    protected abstract boolean hasData();    
    protected abstract Dimension getImageDimensions();
    protected abstract void drawImage(Graphics graphics);
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasData()) {
            //Console.logger().finest("Layout panel repainted");
            Dimension offset = getImageOffset();
            updateMouseInputs(offset);
            if (redraw) {
                Dimension dims = getImageDimensions();
                //Console.logger().finest("Layout content rebuilt");
                if (currentImage != null) { currentImage.flush(); }
                if (dims.width > 0 && dims.height > 0) {
                    currentImage = paintImage(dims);
                    Dimension size = new Dimension(currentImage.getWidth()+offset.width, currentImage.getHeight()+offset.height);
                    if (BaseLayoutComponent.IsEnabled(coordsGrid)) { coordsGrid.buildCoordsImage(dims, getDisplayScale()); }
                    if (!size.equals(this.getSize())) {
                        setSize(size);
                        setPreferredSize(size);
                        validate();
                        //Console.logger().finest("Layout panel resized");
                    }
                }
                redraw = false;
            }
            g.drawImage(currentImage, offset.width, offset.height, this);
            if (BaseLayoutComponent.IsEnabled(coordsGrid)) { coordsGrid.paintCoordsImage(g, getDisplayScale()); }
        }
    }
    
    private BufferedImage paintImage(Dimension dims) {
        //Setup image
        currentImage = new BufferedImage(dims.width, dims.height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = currentImage.getGraphics();
        //paint
        if (BaseLayoutComponent.IsEnabled(background)) { background.paintBackground(currentImage); }
        drawImage(graphics);
        graphics.dispose();
        if (BaseLayoutComponent.IsEnabled(scale))  { currentImage = scale.resizeImage(currentImage); }
        if (BaseLayoutComponent.IsEnabled(grid))  { grid.paintGrid(currentImage, getDisplayScale()); }
        //paint after resize
        graphics = currentImage.getGraphics();
        //Cleanup
        graphics.dispose();
        getParent().revalidate();
        return currentImage;
    }
    
    protected Dimension getImageOffset() {
        if (coordsGrid != null && coordsGrid.isEnabled()) {
            return coordsGrid.getOffset(getDisplayScale());
        } else {
            return NO_OFFSET;
        }
    }
    
    private void updateMouseInputs(Dimension offset) {
        if (BaseLayoutComponent.IsEnabled(coordsHeader)) { coordsHeader.updateDisplayParameters(getDisplayScale(), getPreferredSize(), offset); }
        if (BaseLayoutComponent.IsEnabled(mouseInput)) { mouseInput.updateDisplayParameters(getDisplayScale(), getPreferredSize(), offset); }
    }
    
    public int getItemsPerRow() {
        return itemsPerRow;
    }

    public void setItemsPerRow(int itemsPerRow) {
        if (this.itemsPerRow != itemsPerRow) {
            this.itemsPerRow = itemsPerRow;
            if (BaseLayoutComponent.IsEnabled(coordsHeader)) { coordsHeader.setItemsPerRow(itemsPerRow); }
            redraw();
        }
    }
    
    public boolean isRedrawing() {
        return redraw;
    }

    public void redraw() {
        this.redraw = true;
        repaint();
    }

    public int getDisplayScale() {
        return BaseLayoutComponent.IsEnabled(scale) ? scale.getScale() : 1;
    }

    public void setDisplayScale(int displayScale) {
        if (scale != null && scale.getScale() != displayScale) {
            scale.setScale(displayScale);
            redraw();
        }
    }

    public Color getBGColor() {
        return BaseLayoutComponent.IsEnabled(background) ? background.getBgColor() : Color.BLACK;
    }

    public void setBGColor(Color bgColor) {
        if (background != null && background.getBgColor() != bgColor) {
            background.setBgColor(bgColor);
            redraw();
        }
    }
    
    public boolean isShowGrid() {
        return BaseLayoutComponent.IsEnabled(grid);
    }

    public void setShowGrid(boolean showGrid) {
        if (grid != null && grid.isEnabled() != showGrid) {
            grid.setEnabled(showGrid);
            redraw();
        }
    }
}

