/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.core.gui.layout.BaseLayoutComponent;
import com.sfc.sf2.core.gui.layout.BaseMouseCoordsComponent;
import com.sfc.sf2.core.gui.layout.LayoutMouseInput;
import static com.sfc.sf2.graphics.Block.PIXEL_HEIGHT;
import static com.sfc.sf2.graphics.Block.PIXEL_WIDTH;
import com.sfc.sf2.helpers.GraphicsHelpers;
import com.sfc.sf2.helpers.MapBlockHelpers;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapCopyEvent;
import com.sfc.sf2.map.MapFlagCopyEvent;
import com.sfc.sf2.map.MapItem;
import com.sfc.sf2.map.MapWarpEvent;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.gui.BlockSlotPanel;
import com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel;
import com.sfc.sf2.map.layout.MapLayout;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_HEIGHT;
import static com.sfc.sf2.map.layout.MapLayout.BLOCK_WIDTH;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import com.sfc.sf2.map.layout.gui.resources.MapLayoutFlagIcons;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author wiz
 */
public class MapLayoutPanel extends com.sfc.sf2.map.layout.gui.MapLayoutPanel {
    private static final Color COLOR_SELECTED = Color.YELLOW;
    private static final Color COLOR_SELECTED_SECONDARY = new Color(0xFFFFFF88);
    
    public static final int MAP_FLAG_EDIT_BLOCK = 0;
        
    public static final int DRAW_MODE_NONE = 0;
    public static final int DRAW_MODE_EXPLORATION_FLAGS = 1;
    public static final int DRAW_MODE_INTERACTION_FLAGS = 1<<1;
    public static final int DRAW_MODE_GRID = 1<<2;
    public static final int DRAW_MODE_AREAS = 1<<3;
    public static final int DRAW_MODE_FLAG_COPIES = 1<<4;
    public static final int DRAW_MODE_STEP_COPIES = 1<<5;
    public static final int DRAW_MODE_ROOF_COPIES = 1<<6;
    public static final int DRAW_MODE_WARPS = 1<<7;
    public static final int DRAW_MODE_CHEST_ITEMS = 1<<8;
    public static final int DRAW_MODE_OTHER_ITEMS = 1<<9;
    public static final int DRAW_MODE_TRIGGERS = 1<<10;
    public static final int DRAW_MODE_VEHICLES = 1<<11;
    public static final int DRAW_MODE_ACTION_FLAGS = 1<<12;
    public static final int DRAW_MODE_ALL = (1<<13)-1;
    
    private static final int ACTION_CHANGE_BLOCK_VALUE = 0;
    private static final int ACTION_CHANGE_BLOCK_FLAGS = 1;
    private static final int ACTION_MASS_COPY = 2;
        
    MapBlocksetLayoutPanel mapBlockLayoutPanel = null;
    BlockSlotPanel leftSlot = null;
    private boolean isOnActionsTab = true;
    
    private int currentPaintMode = 0;
    private int togglesDrawMode = 0;
    private int selectedTabsDrawMode = 0;
    private int selectedItemIndex = -1;
    private int closestSelectedPointIndex;
    private ActionListener eventEditedListener;
    
    private boolean showAreasOverlay;
    private boolean showAreasUnderlay;
    private boolean showFlagCopyResult;
    private boolean showStepCopyResult;
    private boolean showRoofCopyResult;
    private boolean simulateParallax;
    
    private MapBlock selectedBlock;
    MapLayoutBlock[][] copiedBlocks;
    private int copiedBlocksStartX = -1;
    private int copiedBlocksStartY = -1;
    private int copiedBlocksDrawX = -1;
    private int copiedBlocksDrawY = -1;
    private BufferedImage previewImage;
    private int lastMapX;
    private int lastMapY;
    private int previewIndex = -1;
    
    private List<int[]> actions = new ArrayList<int[]>();
    
    private Map map;

    public MapLayoutPanel() {
        super();
        mouseInput = new LayoutMouseInput(this, this::onMouseButtonInput, this::onMouseMove, PIXEL_WIDTH, PIXEL_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        drawHandleNode(g);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        Dimension offset = getImageOffset();
        g2.drawImage(previewImage, offset.width+(copiedBlocksDrawX*PIXEL_WIDTH)*getDisplayScale(), offset.height+(copiedBlocksDrawY*PIXEL_HEIGHT)*getDisplayScale(), null);
    }

    @Override
    protected void drawImage(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        
        if (shouldDraw(DRAW_MODE_AREAS)) {
            if (showAreasUnderlay) {
                for (int i = 0; i < map.getAreas().length; i++) {
                    underlayMapBackground(g2, map.getAreas()[i]);
                }
            }
            if (showAreasOverlay) {
                for (int i = 0; i < map.getAreas().length; i++) {
                    underlayMapUpperLayer(g2, map.getAreas()[i]);
                }
            }
        }
        
        super.drawImage(graphics);  //Draw map blocks
                
        if (shouldDraw(DRAW_MODE_AREAS)) {
            for (int i = 0; i < map.getAreas().length; i++) {
                drawMapArea(g2, map.getAreas()[i], false);
                if (showAreasOverlay) {
                    overlayMapUpperLayer(g2, map.getAreas()[i]);
                }
            }
        }
        if (shouldDraw(DRAW_MODE_FLAG_COPIES)) {
            for (int i = 0; i < map.getFlagCopies().length; i++) {
                if (showFlagCopyResult) {
                    drawFlagCopyResult(g2, map.getFlagCopies()[i]);
                }
                drawMapFlagCopy(g2, map.getFlagCopies()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_STEP_COPIES)) {
            drawMapStepCopies(g2);
            for (int i = 0; i < map.getStepCopies().length; i++) {
                if (showStepCopyResult) {
                    drawStepCopyResult(g2, map.getStepCopies()[i]);
                }
                drawMapStepCopy(g2, map.getStepCopies()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_ROOF_COPIES)) {
            drawMapRoofCopies(g2);
            for (int i = 0; i < map.getRoofCopies().length; i++) {
                if (showRoofCopyResult) {
                    drawRoofCopyResult(g2, map.getRoofCopies()[i]);
                }
                drawMapRoofCopy(g2, map.getRoofCopies()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_WARPS)) {
            drawMapWarps(g2);
            for (int i = 0; i < map.getWarps().length; i++) {
                drawMapWarp(g2, map.getWarps()[i], false);
            }
        }
        if (shouldDraw(DRAW_MODE_CHEST_ITEMS) || shouldDraw(DRAW_MODE_OTHER_ITEMS)) {
            drawMapItems(g2);
        }
        if (shouldDraw(DRAW_MODE_TRIGGERS)) {
            drawMapTriggers(g2);
        }
        if (shouldDraw(DRAW_MODE_VEHICLES)) {
            drawMapVehicleFlags(g2);
        }
        drawSelected(g2);
        buildPreviewImage();
    }
    
    private void drawHandleNode(Graphics graphics) {
        if (selectedTabsDrawMode == DRAW_MODE_NONE || selectedItemIndex == -1 || closestSelectedPointIndex == -1) return;
        int x = -1, y = -1, offsetX = 0, offsetY = 0;
        boolean atEdge = false;
        switch (selectedTabsDrawMode) {
            case DRAW_MODE_AREAS:
                MapArea area = map.getAreas()[selectedItemIndex];
                atEdge = true;
                switch (closestSelectedPointIndex) {
                    case 0:
                        x = area.getLayer1StartX();
                        y = area.getLayer1StartY();
                        offsetX = 2;
                        offsetY = 2;
                        break;
                    case 1:
                        x = area.getLayer1EndX()+1;
                        y = area.getLayer1StartY();
                        offsetX = -4;
                        offsetY = 2;
                        break;
                    case 2:
                        x = area.getLayer1StartX();
                        y = area.getLayer1EndY()+1;
                        offsetX = 2;
                        offsetY = -4;
                        break;
                    case 3:
                        x = area.getLayer1EndX()+1;
                        y = area.getLayer1EndY()+1;
                        offsetX = -4;
                        offsetY = -4;
                        break;
                    case 4:
                        atEdge = false;
                        if (area.hasBackgroundLayer2()) {
                            x = area.getBackgroundLayer2StartX();
                            y = area.getBackgroundLayer2StartY();
                        } else {
                            x = area.getLayer1StartX()+area.getForegroundLayer2StartX();
                            y = area.getLayer1StartY()+area.getForegroundLayer2StartY();
                        }
                        break;
                }
                break;
            case DRAW_MODE_FLAG_COPIES:
            case DRAW_MODE_STEP_COPIES:
            case DRAW_MODE_ROOF_COPIES:
                MapCopyEvent copy = selectedTabsDrawMode == DRAW_MODE_FLAG_COPIES ? map.getFlagCopies()[selectedItemIndex] : selectedTabsDrawMode == DRAW_MODE_STEP_COPIES ? map.getStepCopies()[selectedItemIndex] : map.getRoofCopies()[selectedItemIndex];
                atEdge = true;
                int sx = copy.getSourceStartX(), sy = copy.getSourceStartY();
                if (sx == 0xFF && sy == 0xFF) {
                    MapArea mainArea = map.getAreas()[0];
                    sx = copy.getDestStartX()-mainArea.getForegroundLayer2StartX();
                    sy = copy.getDestStartY()-mainArea.getForegroundLayer2StartY();
                }
                switch (closestSelectedPointIndex) {        
                    case 0:
                        x = copy.getTriggerX();
                        y = copy.getTriggerY();
                        atEdge = false;
                        break;
                    case 1:
                        x = sx;
                        y = sy;
                        offsetX = 2;
                        offsetY = 2;
                        break;
                    case 2:
                        x = sx+copy.getWidth();
                        y = sy;
                        offsetX = -4;
                        offsetY = 2;
                        break;
                    case 3:
                        x = sx;
                        y = sy+copy.getHeight();
                        offsetX = 2;
                        offsetY = -4;
                        break;
                    case 4:
                        x = sx+copy.getWidth();
                        y = sy+copy.getHeight();
                        offsetX = -4;
                        offsetY = -4;
                        break;
                    case 5:
                        x = copy.getDestStartX()+copy.getWidth()/2;
                        y = copy.getDestStartY()+copy.getHeight()/2;
                        atEdge = false;
                        break;
                }
                break;
            case DRAW_MODE_WARPS:
                MapWarpEvent warp = map.getWarps()[selectedItemIndex];
                MapArea mainArea = map.getAreas()[0];
                if (closestSelectedPointIndex == 0) {
                    x = warp.getTriggerX();
                    if (x == 0xFF) x = mainArea.getLayer1StartX()+mainArea.getWidth()/2;
                    y = warp.getTriggerY();
                    if (y == 0xFF) y = mainArea.getLayer1StartY()+mainArea.getHeight()/2;
                } else {
                    x = warp.getDestX();
                    y = warp.getDestY();
                }
                break;
            case DRAW_MODE_CHEST_ITEMS:
            case DRAW_MODE_OTHER_ITEMS:
                MapItem item = selectedTabsDrawMode == DRAW_MODE_CHEST_ITEMS ? map.getChestItems()[selectedItemIndex] : map.getOtherItems()[selectedItemIndex];
                x = item.getX();
                y = item.getY();
                break;
            default:
                return;
        }
        Dimension imageOffset = getImageOffset();
        int scale = getDisplayScale();
        x = x*PIXEL_WIDTH+offsetX;
        y = y*PIXEL_HEIGHT+offsetY;
        if (atEdge) {
            x -= 4;
            y -= 4;
        } else {
            x += 6;
            y += 6;
        }
        graphics.setColor(Color.CYAN);
        graphics.fillArc(x*scale + imageOffset.width, y*scale + imageOffset.height, 12*scale, 12*scale, 0, 360);
        x += 2;
        y += 2;
        graphics.setColor(Color.BLUE);
        graphics.fillArc(x*scale + imageOffset.width, y*scale + imageOffset.height, 8*scale, 8*scale, 0, 360);
    }
    
    private void buildPreviewImage() {
        if (!isOnActionsTab) return;
        Image preview = null;
        boolean overlayRect = false;
        
        switch (currentPaintMode) {
            case MAP_FLAG_EDIT_BLOCK:
                overlayRect = true;
                if (copiedBlocksStartX >= 0) {
                    //Dragging for mass copy
                    int width;
                    int height;
                    if (lastMapX < copiedBlocksStartX) {
                        width = copiedBlocksStartX - lastMapX + 1;
                        copiedBlocksDrawX = lastMapX;
                    } else {
                        width = lastMapX - copiedBlocksStartX + 1;
                        copiedBlocksDrawX = copiedBlocksStartX;
                    }
                    if (lastMapY < copiedBlocksStartY) {
                        height = copiedBlocksStartY - lastMapY + 1;
                        copiedBlocksDrawY = lastMapY;
                    } else {
                        height = lastMapY - copiedBlocksStartY + 1;
                        copiedBlocksDrawY = copiedBlocksStartY;
                    }
                    preview = new BufferedImage(width*PIXEL_WIDTH, height*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D)preview.getGraphics();
                    graphics.setColor(Color.YELLOW);
                    graphics.setStroke(new BasicStroke(2));
                    graphics.drawRect(0, 0, width*PIXEL_WIDTH, height*PIXEL_HEIGHT);
                    overlayRect = false;
                    graphics.dispose();
                } else if (copiedBlocks != null) {
                    overlayRect = true;
                    preview = new BufferedImage(copiedBlocks.length*PIXEL_WIDTH, copiedBlocks[0].length*PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D)preview.getGraphics();
                    graphics.setColor(Color.WHITE);
                    for (int i=0; i < copiedBlocks.length; i++) {
                        for (int j=0; j < copiedBlocks[i].length; j++) {
                            graphics.drawImage(copiedBlocks[i][j].getMapBlock().getIndexedColorImage(layout.getTilesets()), i*PIXEL_WIDTH, j*PIXEL_HEIGHT, null);
                        }
                    }
                    graphics.dispose();
                } else if (mapBlockLayoutPanel.selectedBlockIndexLeft == -1) {
                    //No block
                    preview = null;
                    previewIndex = -1;
                } else {
                    //Block selected
                    previewIndex = mapBlockLayoutPanel.selectedBlockIndexLeft;
                    selectedBlock = map.getBlockset().getBlocks()[previewIndex];
                    //"layout" is not MapBlockLayout. How to get that?
                    if (selectedBlock != null) {
                        preview = selectedBlock.getIndexedColorImage(layout.getTilesets());
                    }
                }
                break;
            default:
                preview = MapLayoutFlagIcons.getFlagIcon(currentPaintMode).getImage();
                if (preview != null) {
                    overlayRect = true;
                }
                break;
        }
        
        if (preview != null || overlayRect) {
            if (preview == null) {
                previewImage = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            } else {
                previewImage = new BufferedImage(preview.getWidth(null), preview.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            }
            Graphics2D graphics = (Graphics2D)previewImage.getGraphics();
            if (preview != null) {
                graphics.setColor(Color.WHITE);
                graphics.drawImage(preview, 0, 0, null);
            }
            if (overlayRect) {
                graphics.setColor(Color.YELLOW);
                graphics.setStroke(new BasicStroke(3));
                graphics.drawRect(0,0, previewImage.getWidth(null), previewImage.getHeight(null));
            }
            graphics.dispose();
            previewImage = scale.resizeImage(previewImage);
        }
    }
    
    private void drawMapArea(Graphics2D g2, MapArea area, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? COLOR_SELECTED : Color.WHITE);
        int width = area.getWidth();
        int heigth = area.getHeight();
        g2.drawRect(area.getLayer1StartX()*PIXEL_WIDTH+3, area.getLayer1StartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.LIGHT_GRAY);
        if (area.getForegroundLayer2StartX() != 0 || area.getForegroundLayer2StartY() != 0) {
            g2.drawRect((area.getLayer1StartX()+area.getForegroundLayer2StartX())*PIXEL_WIDTH+3, (area.getLayer1StartY()+area.getForegroundLayer2StartY())*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        }
        if (area.getBackgroundLayer2StartX() != 0 || area.getBackgroundLayer2StartY() != 0) {
            g2.drawRect(area.getBackgroundLayer2StartX()*PIXEL_WIDTH+3, area.getBackgroundLayer2StartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        }
    }
    
    private void overlayMapUpperLayer(Graphics2D g2, MapArea area) {
        if (area.hasBackgroundLayer2()) {
            //Hack for underground tunnel (because BG layer is drawn with priority)
            MapLayoutBlock[] blocks = layout.getBlocks();
            int index = area.getLayer1StartX()+area.getBackgroundLayer2StartX() + (area.getLayer1StartY()+area.getBackgroundLayer2StartY())*BLOCK_WIDTH;
            if (!blocks[index].getMapBlock().isAllPriority()) return;
            index = area.getLayer1StartX()+5+area.getBackgroundLayer2StartX() + (area.getLayer1StartY()+5+area.getBackgroundLayer2StartY())*BLOCK_WIDTH;
            if (!blocks[index].getMapBlock().isAllPriority()) return;
        } else if (!area.hasForegroundLayer2()) return;
        int width = area.getWidth();
        int height = area.getHeight();
        Point offset = calulateAreaOffset(area);
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int destX = (area.getLayer1StartX()+x)*PIXEL_WIDTH+offset.x;
                int destY = (area.getLayer1StartY()+y)*PIXEL_HEIGHT+offset.y;
                int index = 0;
                if (area.hasForegroundLayer2()) {
                    index = area.getLayer1StartX()+area.getForegroundLayer2StartX()+x + (area.getLayer1StartY()+area.getForegroundLayer2StartY()+y)*BLOCK_WIDTH;
                } else if (area.hasBackgroundLayer2()) {
                    index = area.getLayer1StartX()+area.getBackgroundLayer2StartX()+x + (area.getLayer1StartY()+area.getBackgroundLayer2StartY()+y)*BLOCK_WIDTH;
                }
                if (index < MapLayout.BLOCK_COUNT) {
                    g2.drawImage(blocks[index].getMapBlock().getIndexedColorImage(layout.getTilesets()), destX, destY, null);
                }
            }
        }
    }
    
    private void underlayMapUpperLayer(Graphics2D g2, MapArea area) {
        if (!area.hasForegroundLayer2()) return;
        int posX = area.getLayer1StartX()+area.getForegroundLayer2StartX();
        int posY = area.getLayer1StartY()+area.getForegroundLayer2StartY();
        int width = area.getWidth();
        int height = area.getHeight();
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int destX = (posX+x)*PIXEL_WIDTH;
                int destY = (posY+y)*PIXEL_HEIGHT;
                int index = area.getLayer1StartX()+x + (area.getLayer1StartY()+y)*BLOCK_WIDTH;
                if (index < MapLayout.BLOCK_COUNT) {
                    g2.drawImage(blocks[index].getMapBlock().getIndexedColorImage(layout.getTilesets()), destX, destY, null);
                }
            }
        }
        g2.setColor(MapBlockHelpers.PRIORITY_DARKEN_COLOR);
        g2.fillRect(posX*PIXEL_WIDTH, posY*PIXEL_HEIGHT, width*PIXEL_WIDTH, height*PIXEL_HEIGHT);
    }
    
    private void underlayMapBackground(Graphics2D g2, MapArea area) {
        if (!area.hasBackgroundLayer2()) return;
        int width = area.getWidth();
        int height = area.getHeight();
        Point offset = calulateAreaOffset(area);
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int destX = (area.getLayer1StartX()+x)*PIXEL_WIDTH+offset.x;
                int destY = (area.getLayer1StartY()+y)*PIXEL_HEIGHT+offset.y;
                int index = area.getBackgroundLayer2StartX()+x + (area.getBackgroundLayer2StartY()+y)*BLOCK_WIDTH;
                if (index < MapLayout.BLOCK_COUNT) {
                    g2.drawImage(blocks[index].getMapBlock().getIndexedColorImage(layout.getTilesets()), destX, destY, null);
                }
            }
        }
    }
    
    private Point calulateAreaOffset(MapArea area) {
        Point offset = new Point();
        if (simulateParallax && BaseLayoutComponent.IsEnabled(scroller)) {
            if (area.getLayer2ParallaxX() < 0x100 || area.getLayer2ParallaxY() < 0x100) {
                if (area.getLayer2ParallaxX() > 0 && area.getLayer2ParallaxX() < 0x100) {
                    offset.x += (int)(area.getLayer2ParallaxX()*PIXEL_WIDTH*scroller.getScrollPercent(true)*0.5f);
                }
                if (area.getLayer2ParallaxY() > 0 && area.getLayer2ParallaxY() < 0x100) {
                    offset.y += (int)(area.getLayer2ParallaxY()*PIXEL_WIDTH*scroller.getScrollPercent(false)*0.5f);
                }
            }
            if (area.getLayer2AutoscrollX()< 0x100 || area.getLayer2AutoscrollY()< 0x100) {
                if (area.getLayer2AutoscrollX() > 0 && area.getLayer2AutoscrollX() < 0x100) {
                    offset.x += (int)(area.getLayer2AutoscrollX()*PIXEL_WIDTH*scroller.getScrollPercent(true)*0.5f);
                }
                if (area.getLayer2AutoscrollY() > 0 && area.getLayer2AutoscrollY() < 0x100) {
                    offset.y += (int)(area.getLayer2AutoscrollY()*PIXEL_WIDTH*scroller.getScrollPercent(false)*0.5f);
                }
            }
        }
        return offset;
    }
    
    private void drawMapFlagCopy(Graphics2D g2, MapFlagCopyEvent flagCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        int width = flagCopy.getWidth();
        int heigth = flagCopy.getHeight();
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.LIGHT_GRAY);
        g2.drawRect(flagCopy.getDestStartX()*PIXEL_WIDTH+3, flagCopy.getDestStartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED : Color.CYAN);
        g2.drawRect(flagCopy.getSourceStartX()*PIXEL_WIDTH+3,flagCopy.getSourceStartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        if (selected) {
            GraphicsHelpers.drawArrowLine(g2, flagCopy.getSourceStartX()*PIXEL_WIDTH+12, flagCopy.getSourceStartY()*PIXEL_HEIGHT+12, flagCopy.getDestStartX()*PIXEL_WIDTH+12, flagCopy.getDestStartY()*PIXEL_HEIGHT+12);
        }
    }
    
    private void drawFlagCopyResult(Graphics2D g2, MapFlagCopyEvent flagCopy) {
        int dx = (flagCopy.getDestStartX()-flagCopy.getSourceStartX())*PIXEL_WIDTH;
        int dy = (flagCopy.getDestStartY()-flagCopy.getSourceStartY())*PIXEL_HEIGHT;
        g2.copyArea(flagCopy.getSourceStartX()*PIXEL_WIDTH, flagCopy.getSourceStartY()*PIXEL_HEIGHT, flagCopy.getWidth()*PIXEL_WIDTH, flagCopy.getHeight()*PIXEL_HEIGHT, dx, dy);
    }
    
    private void drawMapStepCopy(Graphics2D g2, MapCopyEvent stepCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        int width = stepCopy.getWidth();
        int heigth = stepCopy.getHeight();
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(stepCopy.getDestStartX()*PIXEL_WIDTH+3, stepCopy.getDestStartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.CYAN);
        g2.drawRect(stepCopy.getSourceStartX()*PIXEL_WIDTH+3,stepCopy.getSourceStartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED : Color.WHITE);
        g2.drawRect(stepCopy.getTriggerX()*PIXEL_WIDTH, stepCopy.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        if (selected) {
            GraphicsHelpers.drawArrowLine(g2, stepCopy.getSourceStartX()*PIXEL_WIDTH+12, stepCopy.getSourceStartY()*PIXEL_HEIGHT+12, stepCopy.getDestStartX()*PIXEL_WIDTH+12, stepCopy.getDestStartY()*PIXEL_HEIGHT+12);
        }
    }
    
    private void drawStepCopyResult(Graphics2D g2, MapCopyEvent stepCopy) {
        int dx = (stepCopy.getDestStartX()-stepCopy.getSourceStartX())*PIXEL_WIDTH;
        int dy = (stepCopy.getDestStartY()-stepCopy.getSourceStartY())*PIXEL_HEIGHT;
        g2.copyArea(stepCopy.getSourceStartX()*PIXEL_WIDTH, stepCopy.getSourceStartY()*PIXEL_HEIGHT, stepCopy.getWidth()*PIXEL_WIDTH, stepCopy.getHeight()*PIXEL_HEIGHT, dx, dy);
    }
    
    private void drawMapStepCopies(Graphics2D g2) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        g2.setColor(new Color(800080));
        g2.setStroke(new BasicStroke(1));
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getEventFlags();
                if (itemFlag == MapLayoutBlock.MAP_FLAG_STEP) {
                    g2.drawImage(MapLayoutFlagIcons.getStepIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawMapRoofCopies(Graphics2D g2) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(1));
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getEventFlags();
                if (itemFlag == MapLayoutBlock.MAP_FLAG_SHOW) {
                    g2.drawImage(MapLayoutFlagIcons.getShowIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
                else if (itemFlag == MapLayoutBlock.MAP_FLAG_HIDE) {
                    g2.drawImage(MapLayoutFlagIcons.getHideIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawMapRoofCopy(Graphics2D g2, MapCopyEvent roofCopy, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        int width = roofCopy.getWidth();
        int heigth = roofCopy.getHeight();
        int sourceX = roofCopy.getSourceStartX();
        int sourceY = roofCopy.getSourceStartY();
        if (sourceX == 0xFF && sourceY == 0xFF) {
            MapArea area = map.getAreas()[0];
            sourceX = roofCopy.getDestStartX() - (area.getForegroundLayer2StartX()-area.getLayer1StartX());
            sourceY = roofCopy.getDestStartY() - (area.getForegroundLayer2StartY()-area.getLayer1StartY());
        }
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.CYAN);
        g2.drawRect(sourceX*PIXEL_WIDTH+3, sourceY*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        g2.setColor(selected ? COLOR_SELECTED : Color.WHITE);
        g2.drawRect(roofCopy.getTriggerX()*PIXEL_WIDTH, roofCopy.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        g2.setColor(selected ? COLOR_SELECTED_SECONDARY : Color.LIGHT_GRAY);
        g2.drawRect(roofCopy.getDestStartX()*PIXEL_WIDTH + 3, roofCopy.getDestStartY()*PIXEL_HEIGHT+3, width*PIXEL_WIDTH-6, heigth*PIXEL_HEIGHT-6);
        if (selected) {
            g2.setColor(Color.WHITE);
            GraphicsHelpers.drawArrowLine(g2, roofCopy.getTriggerX()*PIXEL_WIDTH+12, roofCopy.getTriggerY()*PIXEL_HEIGHT+12, roofCopy.getDestStartX()*PIXEL_WIDTH+12, roofCopy.getDestStartY()*PIXEL_HEIGHT+12);
            g2.setColor(COLOR_SELECTED);
            GraphicsHelpers.drawArrowLine(g2, roofCopy.getDestStartX()*PIXEL_WIDTH+12, roofCopy.getDestStartY()*PIXEL_HEIGHT+12, sourceX*PIXEL_WIDTH+12, sourceY*PIXEL_HEIGHT+12, true, true);
        }
    }
    
    private void drawRoofCopyResult(Graphics2D g2, MapCopyEvent roofCopy) {
        int sourceX = roofCopy.getSourceStartX();
        int sourceY = roofCopy.getSourceStartY();
        int destX = roofCopy.getDestStartX();
        int destY = roofCopy.getDestStartY();
        if (sourceX == 0xFF && sourceY == 0xFF) {
            MapArea area = map.getAreas()[0];
            sourceX = destX;
            sourceY = destY;
            destX = roofCopy.getDestStartX() - (area.getForegroundLayer2StartX()-area.getLayer1StartX());
            destY = roofCopy.getDestStartY() - (area.getForegroundLayer2StartY()-area.getLayer1StartY());
        } else {
            MapArea mainArea = map.getAreas()[0];
            int areaL2StartX = mainArea.getLayer1StartX()+mainArea.getForegroundLayer2StartX();
            int areaL2StartY = mainArea.getLayer1StartY()+mainArea.getForegroundLayer2StartY();
            int areaL2EndX = areaL2StartX+(mainArea.getLayer1EndX()-mainArea.getLayer1StartX());
            int areaL2EndY = areaL2StartY+(mainArea.getLayer1EndY()-mainArea.getLayer1StartY());
            if (destX >= areaL2StartX && destX <= areaL2EndX && destY >= areaL2StartY && destY <= areaL2EndY) {
                destX -= areaL2StartX;
                destY -= areaL2StartY;
            }
        }
        int width = roofCopy.getWidth();
        int height = roofCopy.getHeight();
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int blockPosX = (destX+x)*PIXEL_WIDTH;
                int blockPosY = (destY+y)*PIXEL_HEIGHT;
                int index = sourceX+x + (sourceY+y)*BLOCK_WIDTH;
                g2.drawImage(blocks[index].getMapBlock().getIndexedColorImage(layout.getTilesets()), blockPosX, blockPosY, null);
            }
        }
    }
    
    private void drawMapWarps(Graphics2D g2) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(1));
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getEventFlags();
                if (itemFlag == MapLayoutBlock.MAP_FLAG_WARP) {
                    g2.drawImage(MapLayoutFlagIcons.getWarpIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawMapWarp(Graphics2D g2, MapWarpEvent warp, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(selected ? COLOR_SELECTED : Color.CYAN);
        if (warp.getTriggerX() == 0xFF || warp.getTriggerY() == 0xFF) {
            MapArea mainArea = map.getAreas()[0];
            int x, w, y, h;
            if (warp.getTriggerX() == 0xFF) {
                x = mainArea.getLayer1StartX();
                w = mainArea.getLayer1EndX()-x+1;
            } else {
                x = warp.getTriggerX();
                w = 1;
            }
            if (warp.getTriggerY() == 0xFF) {
                y = mainArea.getLayer1StartY();
                h = mainArea.getLayer1EndY()-y+1;
            } else {
                y = warp.getTriggerY();
                h = 1;
            }
            g2.drawRect(x*PIXEL_WIDTH, y*PIXEL_HEIGHT, w*PIXEL_WIDTH, h*PIXEL_HEIGHT);
        } else {
            g2.drawImage(MapLayoutFlagIcons.getWarpIcon().getImage(), warp.getTriggerX()*PIXEL_WIDTH, warp.getTriggerY()*PIXEL_HEIGHT, null);
            if (selected) {
                g2.drawRect(warp.getTriggerX()*PIXEL_WIDTH, warp.getTriggerY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
            }
        }
        if (warp.getDestMap().equals("CURRENT")) {
            g2.setColor(selected ? COLOR_SELECTED : Color.BLUE);
            GraphicsHelpers.drawArrowLine(g2, warp.getTriggerX()*PIXEL_WIDTH+12, warp.getTriggerY()*PIXEL_HEIGHT+12, warp.getDestX()*PIXEL_WIDTH+12, warp.getDestY()*PIXEL_HEIGHT+12);
            g2.drawRect(warp.getDestX()*PIXEL_WIDTH, warp.getDestY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
        }
    }    

    private void drawMapItems(Graphics2D g2) {
        g2.setStroke(new BasicStroke(3));
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                ImageIcon icon = MapLayoutFlagIcons.getBlockItemFlagIcon(blocks[x+y*BLOCK_WIDTH].getEventFlags());
                if (icon != null) {
                    g2.drawImage(icon.getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawItem(Graphics2D g2, MapItem item, boolean selected) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.YELLOW);
        g2.drawRect(item.getX()*PIXEL_WIDTH, item.getY()*PIXEL_HEIGHT, PIXEL_WIDTH, PIXEL_HEIGHT);
    }
        
    private void drawMapTriggers(Graphics2D g2) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                ImageIcon icon = MapLayoutFlagIcons.getBlockTriggersFlagIcon(blocks[x+y*BLOCK_WIDTH].getEventFlags());
                if (icon != null) {
                    g2.drawImage(icon.getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
        
    private void drawMapVehicleFlags(Graphics2D g2) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        for (int y=0; y < BLOCK_HEIGHT; y++) {
            for (int x=0; x < BLOCK_WIDTH; x++) {
                if (blocks[x+y*BLOCK_WIDTH].getEventFlags() == MapLayoutBlock.MAP_FLAG_CARAVAN) {
                    g2.drawImage(MapLayoutFlagIcons.getCaravanIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                } else if (blocks[x+y*BLOCK_WIDTH].getEventFlags() == MapLayoutBlock.MAP_FLAG_RAFT) {
                    g2.drawImage(MapLayoutFlagIcons.getRaftIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
            }
        }
    }
    
    private void drawSelected(Graphics2D g2) {
        if (selectedItemIndex == -1 || isOnActionsTab) return;
        switch (selectedTabsDrawMode) {
            case DRAW_MODE_AREAS:
                drawMapArea(g2, map.getAreas()[selectedItemIndex], true);
                break;
            case DRAW_MODE_FLAG_COPIES:
                drawMapFlagCopy(g2, map.getFlagCopies()[selectedItemIndex], true);
                break;
            case DRAW_MODE_STEP_COPIES:
                drawMapStepCopy(g2, map.getStepCopies()[selectedItemIndex], true);
                break;
            case DRAW_MODE_ROOF_COPIES:
                drawMapRoofCopy(g2, map.getRoofCopies()[selectedItemIndex], true);
                break;
            case DRAW_MODE_WARPS:
                drawMapWarp(g2, map.getWarps()[selectedItemIndex], true);
                break;
            case DRAW_MODE_CHEST_ITEMS:
                drawItem(g2, map.getChestItems()[selectedItemIndex], true);
                break;
            case DRAW_MODE_OTHER_ITEMS:
                drawItem(g2, map.getOtherItems()[selectedItemIndex], true);
                break;
        }
    }
    
    public void scrollToSelected() {
        if (selectedItemIndex == -1 || isOnActionsTab) return;
        int x = -1;
        int y = -1;
        switch (selectedTabsDrawMode) {
            case DRAW_MODE_AREAS:
                MapArea area = map.getAreas()[selectedItemIndex];
                x = area.getLayer1StartX()+10/getDisplayScale();
                y = area.getLayer1StartY()+10/getDisplayScale();
                break;
            case DRAW_MODE_FLAG_COPIES:
                MapCopyEvent flagCopy = map.getFlagCopies()[selectedItemIndex];
                x = flagCopy.getSourceStartX();
                y = flagCopy.getSourceStartY();
                break;
            case DRAW_MODE_STEP_COPIES:
                MapCopyEvent stepCopy = map.getStepCopies()[selectedItemIndex];
                x = stepCopy.getTriggerX();
                y = stepCopy.getTriggerY();
                break;
            case DRAW_MODE_ROOF_COPIES:
                MapCopyEvent roofCopy = map.getRoofCopies()[selectedItemIndex];
                x = roofCopy.getTriggerX();
                y = roofCopy.getTriggerY();
                break;
            case DRAW_MODE_WARPS:
                MapWarpEvent warp = map.getWarps()[selectedItemIndex];
                MapArea mainArea = map.getAreas()[0];
                if (warp.getTriggerX() == 0xFF) {
                    x = mainArea.getLayer1StartX() + (mainArea.getLayer1EndX()-mainArea.getLayer1StartX())/2;
                } else {
                    x = warp.getTriggerX();
                }
                if (warp.getTriggerY() == 0xFF) {
                    y = mainArea.getLayer1StartY() + (mainArea.getLayer1EndY()-mainArea.getLayer1StartY())/2;
                } else {
                    y = warp.getTriggerY();
                }
                break;
            case DRAW_MODE_CHEST_ITEMS:
                MapItem chestItem = map.getChestItems()[selectedItemIndex];
                x = chestItem.getX();
                y = chestItem.getY();
                break;
            case DRAW_MODE_OTHER_ITEMS:
                MapItem otherItem = map.getOtherItems()[selectedItemIndex];
                x = otherItem.getX();
                y = otherItem.getY();
                break;
        }
        if (x >= 0 && y >= 0) {
            centerOnMapPoint(x, y);
        }
    }

    public void setSelectedBlock(MapBlock selectedBlock) {
        this.selectedBlock = selectedBlock;
    }

    public List<int[]> getActions() {
        return actions;
    }

    public void setActions(List<int[]> actions) {
        this.actions = actions;
    }

    public int getCurrentPaintMode() {
        return currentPaintMode;
    }

    public void setCurrentPaintMode(int currentMode) {
        this.currentPaintMode = currentMode;
        redraw();
    }

    public int getCurrentEventEditMode() {
        return selectedTabsDrawMode;
    }

    public void setEventEditedListener(ActionListener eventEditedListener) {
        this.eventEditedListener = eventEditedListener;
    }

    public void setMapBlockLayoutPanel(MapBlocksetLayoutPanel mapBlockLayoutPanel) {
        this.mapBlockLayoutPanel = mapBlockLayoutPanel;
    }

    public void setLeftSlot(BlockSlotPanel leftSlot) {
        this.leftSlot = leftSlot;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
        if (map != null) {
            setMapLayout(map.getLayout());
        }
        redraw();
    }

    @Override
    public boolean getShowExplorationFlags() {
        return super.getShowExplorationFlags() || shouldDraw(DRAW_MODE_EXPLORATION_FLAGS);
    }

    @Override
    public void setShowExplorationFlags(boolean showExplorationFlags) {
        setDrawMode_Toggles(DRAW_MODE_EXPLORATION_FLAGS, showExplorationFlags);
        super.setShowExplorationFlags(showExplorationFlags);
    }
    
    private boolean shouldDraw(int drawFlag) {
        return isDrawMode_Toggles(drawFlag) || isDrawMode_Tabs(drawFlag) || isDrawMode_Actions(drawFlag);
    }

    public boolean isDrawMode_Toggles(int drawFlag) {
        return (togglesDrawMode & drawFlag) > 0;
    }

    public void setDrawMode_Toggles(int drawFlag, boolean on) {
        if (drawFlag == DRAW_MODE_ALL)
            togglesDrawMode = on ? drawFlag : 0;    
        else if (on)
            togglesDrawMode = (togglesDrawMode | drawFlag);
        else
            togglesDrawMode = (togglesDrawMode & ~drawFlag);
        redraw();
    }

    public boolean isDrawMode_Tabs(int drawFlag) {
        return (selectedTabsDrawMode & drawFlag) > 0;
    }

    public void setDrawMode_Tabs(int drawFlag) {
        selectedTabsDrawMode = drawFlag;
        redraw();
    }

    public boolean isDrawMode_Actions(int drawFlag) {
        if (!isOnActionsTab) return false;        
        switch (currentPaintMode)
        {
            case MapLayoutBlock.MAP_FLAG_OBSTRUCTED:
            case MapLayoutBlock.MAP_FLAG_STAIRS_RIGHT:
            case MapLayoutBlock.MAP_FLAG_STAIRS_LEFT:
                return (DRAW_MODE_EXPLORATION_FLAGS & drawFlag) != 0;
            case MapLayoutBlock.MAP_FLAG_CHEST:
            case MapLayoutBlock.MAP_FLAG_SEARCH:
            case MapLayoutBlock.MAP_FLAG_TABLE:
            case MapLayoutBlock.MAP_FLAG_VASE:
            case MapLayoutBlock.MAP_FLAG_BARREL:
            case MapLayoutBlock.MAP_FLAG_SHELF:
                return (DRAW_MODE_CHEST_ITEMS & drawFlag) != 0 || (DRAW_MODE_OTHER_ITEMS & drawFlag) != 0;
            case MapLayoutBlock.MAP_FLAG_WARP:
                return (DRAW_MODE_WARPS & drawFlag) != 0;
            case MapLayoutBlock.MAP_FLAG_TRIGGER:
            case MapLayoutBlock.MAP_FLAG_LAYER_UP:
            case MapLayoutBlock.MAP_FLAG_LAYER_DOWN:
                return (DRAW_MODE_TRIGGERS & drawFlag) != 0;
            case MapLayoutBlock.MAP_FLAG_HIDE:
            case MapLayoutBlock.MAP_FLAG_SHOW:
                return (DRAW_MODE_ROOF_COPIES & drawFlag) != 0;
            case MapLayoutBlock.MAP_FLAG_STEP:
                return (DRAW_MODE_STEP_COPIES & drawFlag) != 0;
            case MapLayoutBlock.MAP_FLAG_CARAVAN:
            case MapLayoutBlock.MAP_FLAG_RAFT:
                return (DRAW_MODE_VEHICLES & drawFlag) != 0;
            default:
                return false;
        }
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setSelectedItemIndex(int selectedItemIndex) {
        this.selectedItemIndex = selectedItemIndex;
        scrollToSelected();
        redraw();
    }
    
    public boolean getIsOnActionsTab() {        
        return isOnActionsTab;
    }
    
    public void setIsOnActionsTab(boolean isOnActionsTab) {
        this.isOnActionsTab = isOnActionsTab;
    }

    public void setShowAreasOverlay(boolean showAreasOverlay) {
        this.showAreasOverlay = showAreasOverlay;
        redraw();
    }

    public void setShowAreasUnderlay(boolean showAreasUnderlay) {
        this.showAreasUnderlay = showAreasUnderlay;
        redraw();
    }

    public void setShowFlagCopyResult(boolean showFlagCopyResult) {
        this.showFlagCopyResult = showFlagCopyResult;
        redraw();
    }

    public void setShowStepCopyResult(boolean showStepCopyResult) {
        this.showStepCopyResult = showStepCopyResult;
        redraw();
    }

    public void setShowRoofCopyResult(boolean showRoofCopyResult) {
        this.showRoofCopyResult = showRoofCopyResult;
        redraw();
    }

    public void setSimulateParallax(boolean simulateParallax) {
        this.simulateParallax = simulateParallax;
        redraw();
        if (simulateParallax) {
            scroller.addScrollChangedListener(this::onScrollerUpdated);
        } else {
            scroller.removeScrollChangedListener(this::onScrollerUpdated);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Input">      
    private void onMouseButtonInput(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (isOnActionsTab) {
            if (currentPaintMode == MAP_FLAG_EDIT_BLOCK) {
                editMapBlocks(evt);
            } else {
                editMapFlags(evt);
            }
        } else if (selectedTabsDrawMode != DRAW_MODE_NONE) {
            editMapEvents(evt);
        }
        repaint();
    }
    
    private void editMapEvents(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (selectedItemIndex == -1) return;
        if (evt.mouseButton() != MouseEvent.BUTTON1) return;
        if (evt.released()) return;
        int x = evt.x();
        int y = evt.y();
        switch (selectedTabsDrawMode) {
            case DRAW_MODE_AREAS:
                MapArea area = map.getAreas()[selectedItemIndex];
                if (closestSelectedPointIndex < 4) {
                    //Main rect
                    int sx = closestSelectedPointIndex == 0 || closestSelectedPointIndex == 2 ? x : area.getLayer1StartX();
                    int sy = closestSelectedPointIndex == 0 || closestSelectedPointIndex == 1 ? y : area.getLayer1StartY();
                    int ex = closestSelectedPointIndex == 1 || closestSelectedPointIndex == 3 ? x : area.getLayer1EndX();
                    int ey = closestSelectedPointIndex == 2 || closestSelectedPointIndex == 3 ? y : area.getLayer1EndY();
                    boolean pointsSwapped = false;
                    if (ex < sx) {
                        int temp = sx;
                        sx = ex;
                        ex = temp;
                        pointsSwapped = true;
                    }
                    if (ey < sy) {
                        int temp = sy;
                        sy = ey;
                        ey = temp;
                        pointsSwapped = true;
                    }
                    area.setLayer1StartX(sx);
                    area.setLayer1StartY(sy);
                    area.setLayer1EndX(ex);
                    area.setLayer1EndY(ey);
                    if (pointsSwapped) {
                        closestSelectedPointIndex = findClosestAreaPoint(area, x, y);
                    }
                } else {
                    //Second rect
                    if (area.hasBackgroundLayer2()) {
                        area.setBackgroundLayer2StartX(x);
                        area.setBackgroundLayer2StartY(y);
                    } else {
                        area.setForegroundLayer2StartX(x-area.getLayer1StartX());
                        area.setForegroundLayer2StartY(y-area.getLayer1StartY());
                    }
                }
                redraw();
                if (eventEditedListener != null) {
                    eventEditedListener.actionPerformed(new ActionEvent(this, selectedItemIndex, "Area"));
                }
                break;
            case DRAW_MODE_FLAG_COPIES:
            case DRAW_MODE_STEP_COPIES:
            case DRAW_MODE_ROOF_COPIES:
                MapCopyEvent copy = selectedTabsDrawMode == DRAW_MODE_FLAG_COPIES ? map.getFlagCopies()[selectedItemIndex] : selectedTabsDrawMode == DRAW_MODE_STEP_COPIES ? map.getStepCopies()[selectedItemIndex] : map.getRoofCopies()[selectedItemIndex];
                if (closestSelectedPointIndex == 0) {
                    copy.setTriggerX(x);
                    copy.setTriggerY(y);
                } else if (closestSelectedPointIndex == 5) {
                    copy.SetDestStartX(x);
                    copy.setDestStartY(y);
                } else if (copy.getSourceStartX() == 0xFF && copy.getSourceStartY() == 0xFF) {
                    //Main rect when infering source from roof (dest) position
                    MapArea mainArea = map.getAreas()[0];
                    int startX = copy.getDestStartX()-mainArea.getForegroundLayer2StartX();
                    int startY = copy.getDestStartY()-mainArea.getForegroundLayer2StartY();
                    int sx = closestSelectedPointIndex == 1 || closestSelectedPointIndex == 3 ? x : startX;
                    int sy = closestSelectedPointIndex == 1 || closestSelectedPointIndex == 2 ? y : startY;
                    int ex = closestSelectedPointIndex == 2 || closestSelectedPointIndex == 4 ? x+1 : startX+copy.getWidth();
                    int ey = closestSelectedPointIndex == 3 || closestSelectedPointIndex == 4 ? y+1 : startY+copy.getHeight();
                    boolean pointsSwapped = false;
                    if (ex < sx) {
                        int temp = sx;
                        sx = ex;
                        ex = temp;
                        pointsSwapped = true;
                    }
                    if (ey < sy) {
                        int temp = sy;
                        sy = ey;
                        ey = temp;
                        pointsSwapped = true;
                    }
                    copy.SetDestStartX(copy.getDestStartX()+(sx-startX));
                    copy.setDestStartY(copy.getDestStartY()+(sy-startY));
                    copy.setSourceEndX(ex-sx);
                    if (copy.getSourceEndX() <= 0) copy.setSourceEndX(1);
                    copy.setSourceEndY(ey-sy);
                    if (copy.getSourceEndY() <= 0) copy.setSourceEndY(1);
                    if (pointsSwapped) {
                        closestSelectedPointIndex = findClosestCopyPoint(copy, x, y, true);
                    }
                } else {
                    //Main rect
                    int sx = closestSelectedPointIndex == 1 || closestSelectedPointIndex == 3 ? x : copy.getSourceStartX();
                    int sy = closestSelectedPointIndex == 1 || closestSelectedPointIndex == 2 ? y : copy.getSourceStartY();
                    int ex = closestSelectedPointIndex == 2 || closestSelectedPointIndex == 4 ? x : copy.getSourceEndX();
                    int ey = closestSelectedPointIndex == 3 || closestSelectedPointIndex == 4 ? y : copy.getSourceEndY();
                    boolean pointsSwapped = false;
                    if (ex < sx) {
                        int temp = sx;
                        sx = ex;
                        ex = temp;
                        pointsSwapped = true;
                    }
                    if (ey < sy) {
                        int temp = sy;
                        sy = ey;
                        ey = temp;
                        pointsSwapped = true;
                    }
                    copy.SetDestStartX(copy.getDestStartX()+(sx-copy.getSourceStartX()));
                    copy.setDestStartY(copy.getDestStartY()+(sy-copy.getSourceStartY()));
                    copy.setSourceStartX(sx);
                    copy.setSourceStartY(sy);
                    copy.setSourceEndX(ex);
                    copy.setSourceEndY(ey);
                    if (pointsSwapped) {
                        closestSelectedPointIndex = findClosestCopyPoint(copy, x, y, true);
                    }
                }
                redraw();
                if (eventEditedListener != null) {
                    String copyType = selectedTabsDrawMode == DRAW_MODE_FLAG_COPIES ? "FlagCopy" : selectedTabsDrawMode == DRAW_MODE_STEP_COPIES ? "StepCopy" : "RoofCopy";
                    eventEditedListener.actionPerformed(new ActionEvent(this, selectedItemIndex, copyType));
                }
                break;
            case DRAW_MODE_WARPS:
                MapWarpEvent warp = map.getWarps()[selectedItemIndex];
                if (closestSelectedPointIndex == 0) {
                    if (warp.getTriggerX() != 0xFF) warp.setTriggerX(x);
                    if (warp.getTriggerY() != 0xFF) warp.setTriggerY(y);
                } else {
                    warp.setDestX(x);
                    warp.setDestY(y);
                }
                redraw();
                if (eventEditedListener != null) {
                    eventEditedListener.actionPerformed(new ActionEvent(this, selectedItemIndex, "Warp"));
                }
                break;
            case DRAW_MODE_CHEST_ITEMS:
                MapItem chestItem = map.getChestItems()[selectedItemIndex];
                chestItem.setX(x);
                chestItem.setY(y);
                redraw();
                if (eventEditedListener != null) {
                    eventEditedListener.actionPerformed(new ActionEvent(this, selectedItemIndex, "ChestItem"));
                }
                break;
            case DRAW_MODE_OTHER_ITEMS:
                MapItem otherItem = map.getOtherItems()[selectedItemIndex];
                otherItem.setX(x);
                otherItem.setY(y);
                redraw();
                if (eventEditedListener != null) {
                    eventEditedListener.actionPerformed(new ActionEvent(this, selectedItemIndex, "OtherItem"));
                }
                break;
        }
    }
    
    private void editMapBlocks(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        int x = evt.x();
        int y = evt.y();
        switch (evt.mouseButton()) {
            case MouseEvent.BUTTON1:
                if (!evt.released()) {
                    if (mapBlockLayoutPanel.selectedBlockIndexLeft !=- 1) {
                        setBlockValue(x, y, mapBlockLayoutPanel.selectedBlockIndexLeft);
                    } else if (copiedBlocks != null && !evt.dragging()) {
                        int width = copiedBlocks.length;
                        int height = copiedBlocks[0].length;
                        int[] action = new int[4+2*width*height];
                        action[0] = ACTION_MASS_COPY;
                        int blockIndex = x+y*BLOCK_WIDTH;
                        action[1] = blockIndex;
                        action[2] = width;
                        action[3] = height;
                        for (int j=0; j < height; j++) {
                            for (int i=0; i < width; i++) {
                                if ((i+(blockIndex%BLOCK_WIDTH)) < BLOCK_WIDTH && (blockIndex+i+j*BLOCK_WIDTH) < MapLayout.BLOCK_COUNT) {
                                    MapLayoutBlock layoutBlock = layout.getBlocks()[blockIndex+i+j*BLOCK_WIDTH];
                                    action[4+2*(i+j*width)] = layoutBlock.getMapBlock().getIndex();
                                    int origFlags = layoutBlock.getFlags();
                                    action[4+2*(i+j*width)+1] = origFlags;
                                    layoutBlock.setMapBlock(copiedBlocks[i][j].getMapBlock());
                                    layoutBlock.setFlags(copiedBlocks[i][j].getFlags());
                                } else {
                                    action[4+2*(i+j*width)] = -1;
                                    action[4+2*(i+j*width)+1] = -1;
                                }
                            }
                        }
                        actions.add(action);
                        redraw();
                    }
                }
                break;
            case MouseEvent.BUTTON2:
                if (evt.released()) {
                    if (x == copiedBlocksStartX && y == copiedBlocksStartY) {
                        selectedBlock = layout.getBlocks()[x+y*64].getMapBlock();
                        if (selectedBlock == null) selectedBlock = map.getBlockset().getBlocks()[0];
                        mapBlockLayoutPanel.selectedBlockIndexLeft = selectedBlock.getIndex();
                        updateLeftSlot(selectedBlock);
                    } else {
                        // Mass copy
                        int xStart, xEnd, yStart, yEnd;
                        if (x > copiedBlocksStartX) {
                            xStart = copiedBlocksStartX;
                            xEnd = x;
                        } else {
                            xStart = x;
                            xEnd = copiedBlocksStartX;
                        }
                        if (y > copiedBlocksStartY) {
                            yStart = copiedBlocksStartY;
                            yEnd = y;
                        } else {
                            yStart = y;
                            yEnd = copiedBlocksStartY;
                        }
                        int width = xEnd - xStart + 1;
                        int height = yEnd - yStart + 1;
                        copiedBlocks = new MapLayoutBlock[width][height];
                        MapLayoutBlock[] blocks = layout.getBlocks();
                        for(int j=0; j < height; j++) {
                            for (int i=0; i < width;i++){
                                copiedBlocks[i][j] = blocks[xStart+i+(yStart+j)*BLOCK_WIDTH];
                            }
                        }
                        mapBlockLayoutPanel.selectedBlockIndexLeft = -1;
                        BufferedImage img = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = (Graphics2D)img.getGraphics();
                        g2.setColor(Color.YELLOW);
                        g2.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
                        g2.setColor(Color.BLACK);
                        g2.drawString("copy", -1, 15);
                        g2.dispose();
                        leftSlot.setOverrideImage(img);
                    }
                    copiedBlocksStartX = copiedBlocksStartY = -1;
                    copiedBlocksDrawX = x;
                    copiedBlocksDrawY = y;
                    redraw();
                } else if (evt.dragging()) {
                    if (copiedBlocksStartX != -1 && copiedBlocksStartY != -1) {
                        previewImage = null;
                        lastMapX = x;
                        lastMapY = y;
                        redraw();
                    }
                } else {
                    copiedBlocksStartX = lastMapX = x;
                    copiedBlocksStartY = lastMapY = y;
                    copiedBlocks = null;
                    redraw();
                }
                break;
            case MouseEvent.BUTTON3:
                if (!evt.released()) {
                    setBlockValue(x, y, mapBlockLayoutPanel.selectedBlockIndexRight);
                }
                break;
        }
    }
    
    private void editMapFlags(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (evt.released()) return;
        int x = evt.x();
        int y = evt.y();
        switch (evt.mouseButton()) {
            case MouseEvent.BUTTON1:
                int flagVal = currentPaintMode;
                if (flagVal == MapLayoutBlock.MAP_FLAG_STAIRS_RIGHT) {
                    if (layout.getBlocks()[x+y*BLOCK_WIDTH].getExplorationFlags() == MapLayoutBlock.MAP_FLAG_STAIRS_RIGHT) {
                        flagVal = MapLayoutBlock.MAP_FLAG_STAIRS_LEFT;
                    }
                }
                setFlagValue(x, y, flagVal);
                break;
            case MouseEvent.BUTTON2:
                clearFlagValue(x, y, 0xFF00);
                break;
            case MouseEvent.BUTTON3:
                flagVal = currentPaintMode;
                if (flagVal == MapLayoutBlock.MAP_FLAG_STAIRS_RIGHT) {
                    if (layout.getBlocks()[x+y*BLOCK_WIDTH].getExplorationFlags() == MapLayoutBlock.MAP_FLAG_STAIRS_LEFT) {
                        flagVal = MapLayoutBlock.MAP_FLAG_STAIRS_LEFT;
                    }
                }
                clearFlagValue(x, y, flagVal);
                break;
        }
    }

    private void onMouseMove(BaseMouseCoordsComponent.GridMouseMoveEvent evt) {
        if (isOnActionsTab) {
            copiedBlocksDrawX = evt.x();
            copiedBlocksDrawY = evt.y();
            revalidate();
        } else if (!evt.dragging() && selectedTabsDrawMode != DRAW_MODE_NONE && selectedItemIndex != -1) {
            int x = evt.x();
            int y = evt.y();
            switch (selectedTabsDrawMode) {
                case DRAW_MODE_AREAS:
                    closestSelectedPointIndex = findClosestAreaPoint(map.getAreas()[selectedItemIndex], x, y);
                    break;
                case DRAW_MODE_FLAG_COPIES:
                    closestSelectedPointIndex = findClosestCopyPoint(map.getFlagCopies()[selectedItemIndex], x, y, true);
                    break;
                case DRAW_MODE_STEP_COPIES:
                    closestSelectedPointIndex = findClosestCopyPoint(map.getStepCopies()[selectedItemIndex], x, y, false);
                    break;
                case DRAW_MODE_ROOF_COPIES:
                    closestSelectedPointIndex = findClosestCopyPoint(map.getRoofCopies()[selectedItemIndex], x, y, false);
                    break;
                case DRAW_MODE_WARPS:
                    closestSelectedPointIndex = findClosestWarpPoint(map.getWarps()[selectedItemIndex], x, y);
                    break;
            }
            /*int region = findClosestRegionPoint(battle.getSpriteset().getAiRegions()[selectedSpritesetEntity], x, y);
            if (closestSelectedPoint != region) {
                closestSelectedPoint = region;
                this.repaint();
            }*/
        }
        repaint();
    }
    
    private void updateLeftSlot(MapBlock block){
        leftSlot.setBlock(block);
    }
    
    public void setBlockValue(int x, int y, int value) {
        if (value == -1) return;
        MapBlock[] blockset = map.getBlockset().getBlocks();
        MapLayoutBlock[] layoutBlocks = layout.getBlocks();
        MapLayoutBlock block = layoutBlocks[x+y*BLOCK_WIDTH];
        if (block.getMapBlock().getIndex() != value) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_VALUE;
            action[1] = x+y*BLOCK_WIDTH;
            action[2] = block.getMapBlock().getIndex();
            layoutBlocks[x+y*BLOCK_WIDTH].setMapBlock(blockset[value]);
            actions.add(action);
            redraw();
        }
    }
    
    public void setFlagValue(int x, int y, int value) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        MapLayoutBlock block = blocks[x+y*BLOCK_WIDTH];
        if (block.getFlags() != value) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = x+y*BLOCK_WIDTH;
            int origFlags = block.getFlags();
            action[2] = origFlags;
            int newFlags = 0;
            if ((value & MapLayoutBlock.MAP_FLAG_MASK_EXPLORE) != 0) {
                newFlags = value & MapLayoutBlock.MAP_FLAG_MASK_EXPLORE;
                newFlags += origFlags & MapLayoutBlock.MAP_FLAG_MASK_EVENTS;
            } else {
                newFlags = origFlags & MapLayoutBlock.MAP_FLAG_MASK_EXPLORE;
                newFlags += value & MapLayoutBlock.MAP_FLAG_MASK_EVENTS;
            }
            block.setFlags(newFlags);
            actions.add(action);
            redraw();
        }
    }
    
    public void clearFlagValue(int x, int y, int value) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        MapLayoutBlock block = blocks[x+y*BLOCK_WIDTH];
        if (block.getFlags() != 0) {
            int[] action = new int[3];
            action[0] = ACTION_CHANGE_BLOCK_FLAGS;
            action[1] = x+y*BLOCK_WIDTH;
            int origFlags = block.getFlags();
            action[2] = origFlags;
            int newFlags = -1;
            if (value == 0xFF00) {
                newFlags = origFlags & ~value;
            } else if ((value & MapLayoutBlock.MAP_FLAG_MASK_EXPLORE) != 0) {
                if (block.getExplorationFlags() == value) {
                    newFlags = block.getExplorationFlags() & ~value;
                }
            } else {
                if (block.getEventFlags() == value) {
                    newFlags = block.getEventFlags() & ~value;
                }
            }
            if (newFlags >= 0) {
                block.setFlags(newFlags);
                actions.add(action);
                redraw();
            }
        }
    }
    
    public void revertLastAction() {
        if (actions.isEmpty()) return;
        int[] action = actions.get(actions.size()-1);
        switch (action[0]) {
            case ACTION_CHANGE_BLOCK_VALUE:
                {
                    MapBlock[] blockset = map.getBlockset().getBlocks();
                    MapLayoutBlock[] layoutBlocks = layout.getBlocks();
                    layoutBlocks[action[1]].setMapBlock(blockset[action[2]]);
                    actions.remove(actions.size()-1);
                    redraw();
                    break;
                }
            case ACTION_CHANGE_BLOCK_FLAGS:
                {
                    MapLayoutBlock block = layout.getBlocks()[action[1]];
                    block.setFlags(action[2]);
                    actions.remove(actions.size()-1);
                    redraw();
                    break;
                }
            case ACTION_MASS_COPY:
                int blockIndex = action[1];
                int width = action[2];
                int height = action[3];
                for (int j=0; j < height; j++) {
                    for (int i=0; i < width; i++) {
                        int value = action[4+2*(j*width+i)];
                        int flags = action[4+2*(j*width+i)+1];
                        if (value != -1 && flags != -1) {
                            layout.getBlocks()[i+blockIndex+j*BLOCK_WIDTH].setMapBlock(map.getBlockset().getBlocks()[value]);
                        }
                    }
                }   actions.remove(actions.size()-1);
                redraw();
                break;
            default:
                break;
        }
    }
    
    private int findClosestAreaPoint(MapArea area, int x, int y) {
        Point mouse = new Point(x+1, y+1);
        Point[] points = new Point[8];
        points[0] = new Point(area.getLayer1StartX(), area.getLayer1StartY());
        points[1] = new Point(area.getLayer1StartX()+area.getWidth(), area.getLayer1StartY());
        points[2] = new Point(area.getLayer1StartX(), area.getLayer1StartY()+area.getHeight());
        points[3] = new Point(area.getLayer1StartX()+area.getWidth(), area.getLayer1StartY()+area.getHeight());
        if (area.hasBackgroundLayer2()) {
            points[4] = new Point(area.getBackgroundLayer2StartX(), area.getBackgroundLayer2StartY());
            points[5] = new Point(area.getBackgroundLayer2StartX()+area.getWidth(), area.getBackgroundLayer2StartY());
            points[6] = new Point(area.getBackgroundLayer2StartX(), area.getBackgroundLayer2StartY()+area.getHeight());
            points[7] = new Point(area.getBackgroundLayer2StartX()+area.getWidth(), area.getBackgroundLayer2StartY()+area.getHeight());
        } else {    //Foreground layer 2
            points[4] = new Point(area.getLayer1StartX()+area.getForegroundLayer2StartX(), area.getLayer1StartY()+area.getForegroundLayer2StartY());
            points[5] = new Point(area.getLayer1StartX()+area.getForegroundLayer2StartX()+area.getWidth(), area.getLayer1StartY()+area.getForegroundLayer2StartY());
            points[6] = new Point(area.getLayer1StartX()+area.getForegroundLayer2StartX(), area.getLayer1StartY()+area.getForegroundLayer2StartY()+area.getHeight());
            points[7] = new Point(area.getLayer1StartX()+area.getForegroundLayer2StartX()+area.getWidth(), area.getLayer1StartY()+area.getForegroundLayer2StartY()+area.getHeight());
        }
        int distIndex = 0;
        double tempDist;
        double dist = mouse.distanceSq(points[0]);
        for (int i = 1; i < points.length; i++) {
            tempDist = mouse.distanceSq(points[i]);
            if (tempDist < dist) {
                distIndex = i;
                dist = tempDist;
            }
        }
        if (distIndex > 4) {
            distIndex = 4;
        }
        return distIndex;
    }
    
    private int findClosestCopyPoint(MapCopyEvent copy, int x, int y, boolean skipTrigger) {
        Point mouse = new Point(x+1, y+1);
        Point[] points = new Point[6];
        points[0] = new Point(copy.getTriggerX(), copy.getTriggerY());
        if (copy.getSourceStartX() == 0xFF && copy.getSourceStartY() == 0xFF) {
            MapArea mainArea = map.getAreas()[0];
            int offsetX = mainArea.getForegroundLayer2StartX();
            int offsetY = mainArea.getForegroundLayer2StartY();
            points[1] = new Point(copy.getDestStartX()-offsetX, copy.getDestStartY()-offsetY);
            points[2] = new Point(copy.getDestEndX()-offsetX+1, copy.getDestStartY()-offsetY);
            points[3] = new Point(copy.getDestStartX()-offsetX, copy.getDestEndY()-offsetY+1);
            points[4] = new Point(copy.getDestEndX()-offsetX+1, copy.getDestEndY()-offsetY+1);
            points[5] = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        } else {
            points[1] = new Point(copy.getSourceStartX(), copy.getSourceStartY());
            points[2] = new Point(copy.getSourceEndX()+1, copy.getSourceStartY());
            points[3] = new Point(copy.getSourceStartX(), copy.getSourceEndY()+1);
            points[4] = new Point(copy.getSourceEndX()+1, copy.getSourceEndY()+1);
            points[5] = new Point(copy.getDestStartX()+copy.getWidth()/2, copy.getDestStartY()+copy.getHeight()/2);
        }
        int distIndex = skipTrigger ? 1 : 0;
        double tempDist;
        double dist = mouse.distanceSq(points[distIndex]);
        for (int i = distIndex+1; i < points.length; i++) {
            tempDist = mouse.distanceSq(points[i]);
            if (tempDist < dist) {
                distIndex = i;
                dist = tempDist;
            }
        }
        return distIndex;
    }
    
    private int findClosestWarpPoint(MapWarpEvent warp, int x, int y) {
        if (!warp.getDestMap().equals("CURRENT")) return 0;
        Point mouse = new Point(x, y);
        if (mouse.distanceSq(warp.getTriggerX(), warp.getTriggerY()) < mouse.distanceSq(warp.getDestX(), warp.getDestY())) {
            return 0;
        } else {
            return 1;
        }
    }
    
    private void onScrollerUpdated(AdjustmentEvent scrollerAdjustment) {
        redraw();
    }
    // </editor-fold>
}
