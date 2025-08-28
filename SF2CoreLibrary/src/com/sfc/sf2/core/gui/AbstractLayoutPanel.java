/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui;

import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.helpers.GraphicsHelpers;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author TiMMy
 */
public abstract class AbstractLayoutPanel extends JPanel implements MouseMotionListener {
    private static final int COORDS_PADDING_X = 6;
    private static final int COORDS_PADDING_Y = 6;
    private static final int COORDS_PADDING_SCALE = 2;
    
    protected int tilesPerRow = 8;
    private int displayScale = 1;
    
    private BufferedImage currentImage;
    private boolean redraw = true;
    private int renderCounter = 0;
    
    protected boolean bgCheckerPattern = true;  
    protected boolean showGrid = false;    
    private int gridWidth = -1;
    private int gridHeight = -1;
    private int thickGridWidth = -1;
    private int thickGridHeight = -1;
    
    private BufferedImage coordsImageX;
    private BufferedImage coordsImageY;
    private boolean showCoords = false;
    private int coordsX = -1;
    private int coordsY = -1;
    private int verticalPadding = 0;
    
    protected boolean showCoordsInTitle = true;
    private int lastX;
    private int lastY;
    private JComponent coordsContainer;
    private TitledBorder coordsTitle;
    private String origTitle;
    
    protected Color bgColor = Color.WHITE;

    public AbstractLayoutPanel() {
        super();
        bgColor = SettingsManager.getGlobalSettings().getTransparentBGColor();
        addMouseMotionListener(this);
        java.awt.EventQueue.invokeLater(() -> { getCoordsTitle(); });
    }
    
    private void getCoordsTitle() {
        if (!showCoords) return;
        try {
            Border border = null;
            Container parent = this;
            for (int i = 0; i < 3; i++) {
                parent = parent.getParent();
                if (parent instanceof JScrollPane) {
                    coordsContainer = (JComponent)parent;
                    border = ((JScrollPane)coordsContainer).getViewportBorder();
                } else if (parent instanceof JComponent) {
                    coordsContainer = (JComponent)parent;
                    border = coordsContainer.getBorder();
                }
                if (border != null && border instanceof TitledBorder) {
                    coordsTitle = (TitledBorder)border;
                    break;
                }
            }
        } catch (Exception e) { }
        if (coordsTitle != null) {
            origTitle = coordsTitle.getTitle();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasData()) {
            Dimension dims = getImageDimensions();
            int offsetX = 0;
            int offsetY = 0;
            if (showCoords) {
                if (redraw) {
                    coordsImageX = paintCoordsImage(true, dims.width, coordsX);
                    coordsImageY = paintCoordsImage(false, dims.height, coordsY);
                }
                Dimension coordsPadding = getCoordsPadding();
                offsetX = verticalPadding+coordsPadding.width;
                offsetY = coordsPadding.height;
            }
            g.drawImage(coordsImageX, offsetX, 0, this);
            g.drawImage(coordsImageY, 0, offsetY, this);
            g.drawImage(paintImage(dims), offsetX, offsetY, this);
            Dimension size = new Dimension(currentImage.getWidth()+offsetX, currentImage.getHeight()+offsetY);
            setSize(size);
            setPreferredSize(size);
        }
    }
    
    public BufferedImage paintImage(Dimension dims) {
        if (!redraw || !hasData()) return currentImage;
        
        if (currentImage != null) {
            currentImage.flush();
        }
        //Setup image
        currentImage = new BufferedImage(dims.width, dims.height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = currentImage.getGraphics();
        //Render BG color
        if (bgCheckerPattern) {
            GraphicsHelpers.drawBackgroundTransparencyPattern(currentImage, bgColor, 4);
        } else {
            GraphicsHelpers.drawFlatBackgroundColor(currentImage, bgColor);
        }
        //Render main image
        renderCounter++;
        System.getLogger(AbstractLayoutPanel.class.getName()).log(System.Logger.Level.ALL, "render " + renderCounter);
        buildImage(graphics);
        graphics.dispose();
        //Resize
        currentImage = resize(currentImage);
        //DrawGrid
        if (showGrid) { drawGrid(currentImage); }
        getParent().revalidate();

        redraw = false;
        return currentImage;
    }
    
    private BufferedImage paintCoordsImage(boolean xAxis, int imageSize, int coordsSize) {
        imageSize *= displayScale;
        coordsSize *= displayScale;
        int padding = (xAxis ? COORDS_PADDING_Y : COORDS_PADDING_X+verticalPadding) + COORDS_PADDING_SCALE*displayScale;
        BufferedImage image = new BufferedImage(xAxis ? imageSize : padding, xAxis ? padding : imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        int fontSize = 4+2*displayScale;
        g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.WHITE : Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int count = imageSize/coordsSize;
        float halfPadding = padding*0.5f;
        float offset = coordsSize*0.5f + 1 + displayScale;
        for (int i = 0; i <= count; i++) {
            String item = Integer.toString(i);
            float textWidth = (float)fontMetrics.getStringBounds(item, g2).getWidth();
            float x = xAxis ? i*coordsSize + offset - textWidth : halfPadding-1 - textWidth*0.5f;
            float y = xAxis ? halfPadding+1 + displayScale*0.5f : i*coordsSize+offset;
            g2.drawString (Integer.toString(i), x, y);
        }
        g2.setColor(SettingsManager.getGlobalSettings().getIsDarkTheme() ? Color.BLACK : Color.WHITE);
        for (int i = 0; i <= count; i++) {
            if (xAxis) {
                g2.drawLine(i*coordsSize, 0, i*coordsSize, padding);
            } else {
                g2.drawLine(0, i*coordsSize, padding, i*coordsSize);
            }
        }
        if (xAxis) {
            g2.drawLine(imageSize-1, 0, imageSize-1, padding);
        } else {
            g2.drawLine(0, imageSize-1, padding, imageSize-1);
        }
        //g2.drawRect(0, 0, image.getWidth()-1, image.getHeight()-1);
        g2.dispose();
        return image;
    }
    
    protected abstract boolean hasData();
    
    protected abstract Dimension getImageDimensions();
    protected abstract void buildImage(Graphics graphics);
    
    protected void setGridDimensions(int gridW, int gridH) {
        setGridDimensions(gridW, gridH, -1, -1);
    }
    
    protected void setGridDimensions(int gridW, int gridH, int thickGridW, int thickGridH) {
        this.gridWidth = gridW;
        this.gridHeight = gridH;
        this.thickGridWidth = thickGridW;
        this.thickGridHeight = thickGridH;
    }
    
    protected void setCoordsDimensions(int rowSize, int columnSize, int verticalPadding) {
        this.showCoords = true;
        this.coordsX = rowSize;
        this.coordsY = columnSize;
        this.verticalPadding = verticalPadding;
    }
    
    private void drawGrid(BufferedImage image) {
        if (gridWidth >= 0 || gridHeight >= 0) {
            GraphicsHelpers.drawGrid(image, gridWidth*displayScale, gridHeight*displayScale, 1);
            if (thickGridWidth >= 0 || thickGridHeight >= 0) {
                GraphicsHelpers.drawGrid(image, thickGridWidth*displayScale, thickGridHeight*displayScale, 3);
            }
        }
    }
    
    public void resize(int size){
        this.displayScale = size;
        currentImage = resize(currentImage);
    }
    
    private BufferedImage resize(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth()*displayScale, image.getHeight()*displayScale, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();
        g.drawImage(image, 0, 0, image.getWidth()*displayScale, image.getHeight()*displayScale, null);
        g.dispose();
        image.flush();
        return newImage;
    }
    
    protected Dimension getCoordsPadding() {
        if (showCoords) {
            return new Dimension(COORDS_PADDING_X+displayScale*COORDS_PADDING_SCALE, COORDS_PADDING_Y+displayScale*COORDS_PADDING_SCALE);
        } else {
            return new Dimension();
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
    public int getTilesPerRow() {
        return tilesPerRow;
    }

    public void setTilesPerRow(int tilesPerRow) {
        this.tilesPerRow = tilesPerRow;
        redraw = true;
    }

    public int getDisplayScale() {
        return displayScale;
    }

    public void setDisplayScale(int displayScale) {
        this.displayScale = displayScale;
        redraw = true;
    }

    public Color getBGColor() {
        return bgColor;
    }

    public void setBGColor(Color bgColor) {
        this.bgColor = bgColor;
        redraw = true;
    }

    public void redraw() {
        this.redraw = true;
    }
    
    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        this.redraw = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!hasData() || !showCoordsInTitle || coordsTitle == null) return;
        Dimension coordsPadding = getCoordsPadding();
        int x = e.getX() - coordsPadding.width;
        int y = e.getY() - coordsPadding.height;
        x /= (getDisplayScale() * coordsX);
        y /= (getDisplayScale() * coordsY);
        if (lastX != x || lastY != y) {
            lastX = x;
            lastY = y;
            coordsTitle.setTitle(String.format("%s : (%d, %d)", origTitle, x, y));
            coordsContainer.repaint();
        }
    }
}

