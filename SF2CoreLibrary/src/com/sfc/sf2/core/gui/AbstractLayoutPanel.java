/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.gui.layout.*;
import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationListener;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractLayoutPanel extends JPanel implements AnimationListener {
    
    private static final Dimension NO_OFFSET = new Dimension();
        
    protected LayoutBackground background;
    protected LayoutScale scale;
    protected LayoutGrid grid;
    protected LayoutCoordsGridDisplay coordsGrid;
    protected LayoutCoordsHeader coordsHeader;
    protected LayoutMouseInput mouseInput;
    protected LayoutScrollNormaliser scroller;
    protected LayoutAnimator animator;
    
    private int itemsPerRow = 8;
        
    private BufferedImage currentImage;
    private boolean redraw = true;

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
                    if (BaseLayoutComponent.IsEnabled(coordsGrid)) { coordsGrid.buildCoordsImage(dims, getRenderScale()); }
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
            if (BaseLayoutComponent.IsEnabled(coordsGrid)) { coordsGrid.paintCoordsImage(g, getRenderScale()); }
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
        if (BaseLayoutComponent.IsEnabled(grid))  { grid.paintGrid(currentImage, getRenderScale()); }
        //paint after resize
        graphics = currentImage.getGraphics();
        //Cleanup
        graphics.dispose();
        getParent().revalidate();
        return currentImage;
    }
    
    public void centerOnMapPoint(int pixelX, int pixelY) {
        if (BaseLayoutComponent.IsEnabled(scroller)) {
            scroller.scrollToPosition(pixelX, pixelY);
        }
    }
    
    public void scrollToIndex(int index, int totalItems) {
        scrollToCoord(index%itemsPerRow, index/itemsPerRow, totalItems); 
    }
    
    public void scrollToCoord(int coordX, int coordY, int totalItems) {
        if (!BaseLayoutComponent.IsEnabled(scroller)) return;
        int total = totalItems/itemsPerRow;
        float percentX = (float)(coordX-2)/itemsPerRow;   //Percent - Subtract 2 to ensure focal point is not top-left corner
        float percentY = (float)(coordY-2)/total;        
        if (percentX < 0f) percentX = 0f;   //Clamp
        if (percentX > 1f) percentX = 1f;
        if (percentY < 0f) percentY = 0f;
        if (percentY > 1f) percentY = 1f;
        
        //Only scroll if the item is outside of the visible window
        Dimension panelSize = this.getParent().getSize();
        Dimension imageSize = this.getImageDimensions();
        float xPercent = (float)panelSize.width / imageSize.width;
        float yPercent = (float)panelSize.height / imageSize.height;
        float x = scroller.getScrollPercent(true);
        float y = scroller.getScrollPercent(false);
        float xMax = x + xPercent;
        float yMax = y + yPercent;
        boolean scroll = false;
        if (percentX >= x && percentX <= xMax) {
            percentX = x;
        } else {
            scroll = true;
        }
        if (percentY >= y && percentY <= yMax) {
            percentY = y;
        } else {
            scroll = true;
        }
        
        if (scroll) {
            Dimension size = getSize();
            scroller.scrollToPosition((int)(size.width*percentX), (int)(size.height*percentY));
        }
    }
    
    public void scrollToPosition(float percentX, float percentY) {
        if (!BaseLayoutComponent.IsEnabled(scroller)) return;
        Dimension dims = getSize();
        scroller.scrollToPosition((int)(dims.width*percentX), (int)(dims.height*percentY));
    }
    
    protected Dimension getImageOffset() {
        if (BaseLayoutComponent.IsEnabled(coordsGrid)) {
            return coordsGrid.getOffset(getRenderScale());
        } else {
            return NO_OFFSET;
        }
    }
    
    private void updateMouseInputs(Dimension offset) {
        if (BaseLayoutComponent.IsEnabled(coordsHeader)) { coordsHeader.updateDisplayParameters(getRenderScale(), getPreferredSize(), offset); }
        if (BaseLayoutComponent.IsEnabled(mouseInput)) { mouseInput.updateDisplayParameters(getRenderScale(), getPreferredSize(), offset); }
    }

    public LayoutAnimator getAnimator() {
        return animator;
    }

    @Override
    public void animationFrameUpdated(AnimationFrameEvent e) {
        redraw();
    }
    
    public int getItemsPerRow() {
        return itemsPerRow;
    }

    public void setItemsPerRow(int itemsPerRow) {
        if (this.itemsPerRow != itemsPerRow) {
            this.itemsPerRow = itemsPerRow;
            if (coordsHeader != null) { coordsHeader.setItemsPerRow(itemsPerRow); }
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

    public float getRenderScale() {
        return BaseLayoutComponent.IsEnabled(scale) ? scale.getScale() : 1f;
    }

    public int getRenderScaleIndex() {
        return BaseLayoutComponent.IsEnabled(scale) ? scale.getScaleIndex(): RenderScaleHelpers.RENDER_SCALE_1X;
    }

    public void setRenderScaleIndex(int renderScaleIndex) {
        if (scale != null && scale.getScaleIndex() != renderScaleIndex) {
            scale.setScaleIndex(renderScaleIndex);
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

