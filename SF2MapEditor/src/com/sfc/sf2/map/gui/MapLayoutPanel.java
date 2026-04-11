/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.BasicAction;
import com.sfc.sf2.core.actions.CumulativeAction;
import com.sfc.sf2.core.actions.CustomAction;
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
import com.sfc.sf2.map.actions.ActionMapCopySourceEvent;
import com.sfc.sf2.map.actions.MapBlockActionData;
import com.sfc.sf2.map.actions.MapCopyBlocksActionData;
import com.sfc.sf2.map.actions.MapFlagActionData;
import com.sfc.sf2.map.actions.MapPointActionData;
import com.sfc.sf2.map.actions.MapRectActionData;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.gui.BlockSlotPanel;
import com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel;
import com.sfc.sf2.map.layout.BlockFlags;
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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
        
    public static final int DRAW_MODE_NONE = 0x0;
    public static final int DRAW_MODE_EXPLORATION_FLAGS = 0x1;
    public static final int DRAW_MODE_INTERACTION_FLAGS = 0x2;
    public static final int DRAW_MODE_GRID = 0x4;
    public static final int DRAW_MODE_AREAS = 0x8;
    public static final int DRAW_MODE_FLAG_COPIES = 0x10;
    public static final int DRAW_MODE_STEP_COPIES = 0x20;
    public static final int DRAW_MODE_ROOF_COPIES = 0x40;
    public static final int DRAW_MODE_WARPS = 0x80;
    public static final int DRAW_MODE_CHEST_ITEMS = 0x100;
    public static final int DRAW_MODE_OTHER_ITEMS = 0x200;
    public static final int DRAW_MODE_TRIGGERS = 0x400;
    public static final int DRAW_MODE_VEHICLES = 0x800;
    public static final int DRAW_MODE_ACTION_FLAGS = 0x1000;
    public static final int DRAW_MODE_ALL = (0x2000)-1;
        
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
    
    private Map map;
    
    private int actionDraggedFlag;

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
        g2.drawImage(previewImage, (int)(offset.width+(copiedBlocksDrawX*PIXEL_WIDTH)*getRenderScale()), (int)(offset.height+(copiedBlocksDrawY*PIXEL_HEIGHT)*getRenderScale()), null);
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
        float scale = getRenderScale();
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
        graphics.fillArc((int)(x*scale + imageOffset.width), (int)(y*scale + imageOffset.height), (int)(12*scale), (int)(12*scale), 0, 360);
        x += 2;
        y += 2;
        graphics.setColor(Color.BLUE);
        graphics.fillArc((int)(x*scale + imageOffset.width), (int)(y*scale + imageOffset.height), (int)(8*scale), (int)(8*scale), 0, 360);
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
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getFlags().getEventFlags();
                if (itemFlag == BlockFlags.MAP_FLAG_STEP) {
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
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getFlags().getEventFlags();
                if (itemFlag == BlockFlags.MAP_FLAG_SHOW) {
                    g2.drawImage(MapLayoutFlagIcons.getShowIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                }
                else if (itemFlag == BlockFlags.MAP_FLAG_HIDE) {
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
                int itemFlag = blocks[x+y*BLOCK_WIDTH].getFlags().getEventFlags();
                if (itemFlag == BlockFlags.MAP_FLAG_WARP) {
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
                ImageIcon icon = MapLayoutFlagIcons.getBlockItemFlagIcon(blocks[x+y*BLOCK_WIDTH].getFlags().getEventFlags());
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
                ImageIcon icon = MapLayoutFlagIcons.getBlockTriggersFlagIcon(blocks[x+y*BLOCK_WIDTH].getFlags().getEventFlags());
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
                if (blocks[x+y*BLOCK_WIDTH].getFlags().getEventFlags() == BlockFlags.MAP_FLAG_CARAVAN) {
                    g2.drawImage(MapLayoutFlagIcons.getCaravanIcon().getImage(), x*PIXEL_WIDTH, y*PIXEL_HEIGHT, null);
                } else if (blocks[x+y*BLOCK_WIDTH].getFlags().getEventFlags() == BlockFlags.MAP_FLAG_RAFT) {
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
                x = (int)(area.getLayer1StartX()+10/getRenderScale());
                y = (int)(area.getLayer1StartY()+10/getRenderScale());
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
        if (this.selectedBlock != null) {
            clearCopiedBlocks();
        }
    }
    
    private void clearCopiedBlocks() {
        copiedBlocksStartX = copiedBlocksStartY = -1;
        copiedBlocks = null;
        previewImage = null;
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
        MapLayout layout = map == null ? null : map.getLayout();
        setMapLayout(layout);
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
            case BlockFlags.MAP_FLAG_OBSTRUCTED:
            case BlockFlags.MAP_FLAG_STAIRS_RIGHT:
            case BlockFlags.MAP_FLAG_STAIRS_LEFT:
                return (DRAW_MODE_EXPLORATION_FLAGS & drawFlag) != 0;
            case BlockFlags.MAP_FLAG_CHEST:
            case BlockFlags.MAP_FLAG_SEARCH:
            case BlockFlags.MAP_FLAG_TABLE:
            case BlockFlags.MAP_FLAG_VASE:
            case BlockFlags.MAP_FLAG_BARREL:
            case BlockFlags.MAP_FLAG_SHELF:
                return (DRAW_MODE_CHEST_ITEMS & drawFlag) != 0 || (DRAW_MODE_OTHER_ITEMS & drawFlag) != 0;
            case BlockFlags.MAP_FLAG_WARP:
                return (DRAW_MODE_WARPS & drawFlag) != 0;
            case BlockFlags.MAP_FLAG_TRIGGER:
            case BlockFlags.MAP_FLAG_LAYER_UP:
            case BlockFlags.MAP_FLAG_LAYER_DOWN:
                return (DRAW_MODE_TRIGGERS & drawFlag) != 0;
            case BlockFlags.MAP_FLAG_HIDE:
            case BlockFlags.MAP_FLAG_SHOW:
                return (DRAW_MODE_ROOF_COPIES & drawFlag) != 0;
            case BlockFlags.MAP_FLAG_STEP:
                return (DRAW_MODE_STEP_COPIES & drawFlag) != 0;
            case BlockFlags.MAP_FLAG_CARAVAN:
            case BlockFlags.MAP_FLAG_RAFT:
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
                    Rectangle rect = new Rectangle(sx, sy, ex-sx, ey-sy);
                    MapRectActionData newValue = new MapRectActionData(area, selectedItemIndex, "Area-Layer1", rect);
                    MapRectActionData oldValue = new MapRectActionData(area, selectedItemIndex, "Area-Layer1", area.getLayer1());
                    ActionManager.setAndExecuteAction(new CustomAction<MapRectActionData>(area, "Set Area Layer 1", this::actionSetAreaLayer1, newValue, oldValue));
                    
                    if (pointsSwapped) {
                        closestSelectedPointIndex = findClosestAreaPoint(area, x, y);
                    }
                } else {
                    //Second rect
                    if (area.hasBackgroundLayer2()) {
                        Point point = new Point(x, y);
                        MapPointActionData newValue = new MapPointActionData(area, selectedItemIndex, "Area-Background2", point);
                        MapPointActionData oldValue = new MapPointActionData(area, selectedItemIndex, "Area-Background2", area.getBackgroundLayer2());
                        ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(area, "Set Area Background 2", this::actionSetAreaBackground2, newValue, oldValue));
                    } else {
                        Point point = new Point(x-area.getLayer1StartX(), y-area.getLayer1StartY());
                        MapPointActionData newValue = new MapPointActionData(area, selectedItemIndex, "Area-Foreground2", point);
                        MapPointActionData oldValue = new MapPointActionData(area, selectedItemIndex, "Area-Foreground2", area.getForegroundLayer2());
                        ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(area, "Set Area Foreground 2", this::actionSetAreaForeground2, newValue, oldValue));
                    }
                }
                break;
            case DRAW_MODE_FLAG_COPIES:
            case DRAW_MODE_STEP_COPIES:
            case DRAW_MODE_ROOF_COPIES:
                MapCopyEvent copy = selectedTabsDrawMode == DRAW_MODE_FLAG_COPIES ? map.getFlagCopies()[selectedItemIndex] : selectedTabsDrawMode == DRAW_MODE_STEP_COPIES ? map.getStepCopies()[selectedItemIndex] : map.getRoofCopies()[selectedItemIndex];
                String copyType = selectedTabsDrawMode == DRAW_MODE_FLAG_COPIES ? "FlagCopy" : selectedTabsDrawMode == DRAW_MODE_STEP_COPIES ? "StepCopy" : "RoofCopy";
                if (closestSelectedPointIndex == 0) {
                    //Trigger
                    Point point = new Point(x, y);
                    MapPointActionData newValue = new MapPointActionData(copy, selectedItemIndex, copyType+"-Trigger", point);
                    MapPointActionData oldValue = new MapPointActionData(copy, selectedItemIndex, copyType+"-Trigger", copy.getTrigger());
                    ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(copy, "Set "+copyType+" Trigger", this::actionSetCopyFlagTriggerPos, newValue, oldValue));
                } else if (closestSelectedPointIndex == 5) {
                    //Destination
                    Point point = new Point(x, y);
                    MapPointActionData newValue = new MapPointActionData(copy, selectedItemIndex, copyType+"-Dest", point);
                    MapPointActionData oldValue = new MapPointActionData(copy, selectedItemIndex, copyType+"-Dest", copy.getDest());
                    ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(copy, "Set "+copyType+" Destination", this::actionSetCopyFlagDestPos, newValue, oldValue));
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
                    Point point = new Point(copy.getDestStartX()+(sx-startX), copy.getDestStartY()+(sy-startY));
                    Rectangle rect = new Rectangle(sx, sy, ex-sx <= 0 ? 1 : ex-sx, ey-sy <= 0 ? 1 : ey-sy);
                    ActionMapCopySourceEvent newValue = new ActionMapCopySourceEvent(copy, selectedItemIndex, copyType+"-Source", rect, point);
                    ActionMapCopySourceEvent oldValue = new ActionMapCopySourceEvent(copy, selectedItemIndex, copyType+"-Source", copy.getSource(), copy.getDest());
                    ActionManager.setAndExecuteAction(new CustomAction<ActionMapCopySourceEvent>(copy, "Set "+copyType+" Source and Dest", this::actionSetFlagSourceAndDest, newValue, oldValue));
                    
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
                    Point point = new Point(copy.getDestStartX()+(sx-copy.getSourceStartX()), copy.getDestStartY()+(sy-copy.getSourceStartY()));
                    Rectangle rect = new Rectangle(sx, sy, ex-sx, ey-sy);
                    ActionMapCopySourceEvent newValue = new ActionMapCopySourceEvent(copy, selectedItemIndex, copyType+"-Source", rect, point);
                    ActionMapCopySourceEvent oldValue = new ActionMapCopySourceEvent(copy, selectedItemIndex, copyType+"-Source", copy.getSource(), copy.getDest());
                    ActionManager.setAndExecuteAction(new CustomAction<ActionMapCopySourceEvent>(copy, "Set "+copyType+" Source and Dest", this::actionSetFlagSourceAndDest, newValue, oldValue));
                    
                    if (pointsSwapped) {
                        closestSelectedPointIndex = findClosestCopyPoint(copy, x, y, true);
                    }
                }
                break;
            case DRAW_MODE_WARPS:
                MapWarpEvent warp = map.getWarps()[selectedItemIndex];
                if (closestSelectedPointIndex == 0) {
                    if (warp.getTriggerX() == 0xFF) x = 0xFF;
                    if (warp.getTriggerY() == 0xFF) y = 0xFF;
                    Point point = new Point(x, y);
                    MapPointActionData newValue = new MapPointActionData(warp, selectedItemIndex, "Warp-Trigger", point);
                    MapPointActionData oldValue = new MapPointActionData(warp, selectedItemIndex, "Warp-Trigger", warp.getTrigger());
                    ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(warp, "Set Warp Trigger", this::actionSetWarpTrigger, newValue, oldValue));
                } else {
                    Point point = new Point(x, y);
                    MapPointActionData newValue = new MapPointActionData(warp, selectedItemIndex, "Warp-Dest", point);
                    MapPointActionData oldValue = new MapPointActionData(warp, selectedItemIndex, "Warp-Dest", warp.getDest());
                    ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(warp, "Set Warp Destination", this::actionSetWarpDest, newValue, oldValue));
                }
                break;
            case DRAW_MODE_CHEST_ITEMS:
                MapItem chestItem = map.getChestItems()[selectedItemIndex];
                if (chestItem != null) {
                    Point point = new Point(x, y);
                    MapPointActionData newValue = new MapPointActionData(chestItem, selectedItemIndex, "ChestItem-Pos", point);
                    MapPointActionData oldValue = new MapPointActionData(chestItem, selectedItemIndex, "ChestItem-Pos", chestItem.getPos());
                    ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(chestItem, "Set Chest Item Position", this::actionSetChestItemPos, newValue, oldValue));
                }
                break;
            case DRAW_MODE_OTHER_ITEMS:
                MapItem otherItem = map.getOtherItems()[selectedItemIndex];
                if (otherItem != null) {
                    Point point = new Point(x, y);
                    MapPointActionData newValue = new MapPointActionData(otherItem, selectedItemIndex, "OtherItem-Pos", point);
                    MapPointActionData oldValue = new MapPointActionData(otherItem, selectedItemIndex, "OtherItem-Pos", otherItem.getPos());
                    ActionManager.setAndExecuteAction(new CustomAction<MapPointActionData>(otherItem, "Set Chest Item Position", this::actionSetOtherItemPos, newValue, oldValue));
                }
                break;
        }
    }
    
    private void actionSetAreaLayer1(MapRectActionData value) {
        MapArea area = (MapArea)value.mapDataItem();
        area.setLayer1(value.rect());
        triggerActionEventListener(value.itemIndex(), "Area");
    }
    
    private void actionSetAreaForeground2(MapPointActionData value) {
        MapArea area = (MapArea)value.mapDataItem();
        area.setForegroundLayer2(value.point());
        triggerActionEventListener(value.itemIndex(), "Area");
    }
    
    private void actionSetAreaBackground2(MapPointActionData value) {
        MapArea area = (MapArea)value.mapDataItem();
        area.setBackgroundLayer2(value.point());
        triggerActionEventListener(value.itemIndex(), "Area");
    }
    
    private void actionSetCopyFlagTriggerPos(MapPointActionData value) {
        MapCopyEvent flag = (MapCopyEvent)value.mapDataItem();
        flag.setTrigger(value.point());
        String copyType = value.event().substring(0, value.event().indexOf('-'));
        triggerActionEventListener(value.itemIndex(), copyType);
    }
    
    private void actionSetCopyFlagDestPos(MapPointActionData value) {
        MapCopyEvent flag = (MapCopyEvent)value.mapDataItem();
        flag.setDest(value.point());
        String copyType = value.event().substring(0, value.event().indexOf('-'));
        triggerActionEventListener(value.itemIndex(), copyType);
    }
    
    private void actionSetFlagSourceAndDest(ActionMapCopySourceEvent value) {
        MapCopyEvent flag = value.copyEvent();
        flag.setSource(value.source());
        flag.setDest(value.dest());
        String copyType = value.event().substring(0, value.event().indexOf('-'));
        triggerActionEventListener(value.itemIndex(), copyType);
    }
    
    private void actionSetWarpTrigger(MapPointActionData value) {
        MapWarpEvent warp = (MapWarpEvent)value.mapDataItem();
        warp.setTrigger(value.point());
        triggerActionEventListener(value.itemIndex(), "Warp");
    }
    
    private void actionSetWarpDest(MapPointActionData value) {
        MapWarpEvent warp = (MapWarpEvent)value.mapDataItem();
        warp.setDest(value.point());
        triggerActionEventListener(value.itemIndex(), "Warp");
    }
    
    private void actionSetChestItemPos(MapPointActionData value) {
        MapItem chestItem = (MapItem)value.mapDataItem();
        chestItem.setPos(value.point());
        triggerActionEventListener(value.itemIndex(), "ChestItem");
    }
    
    private void actionSetOtherItemPos(MapPointActionData value) {
        MapItem otherItem = (MapItem)value.mapDataItem();
        otherItem.setPos(value.point());
        triggerActionEventListener(value.itemIndex(), "OtherItem");
    }
    
    private void triggerActionEventListener(int index, String command) {
        selectedItemIndex = index;
        redraw();
        if (eventEditedListener != null) {
            eventEditedListener.actionPerformed(new ActionEvent(this, index, command));
        }
    }
    
    private void editMapBlocks(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        int x = evt.x();
        int y = evt.y();
        switch (evt.mouseButton()) {
            case MouseEvent.BUTTON3:
                if (!evt.released() && mapBlockLayoutPanel.selectedBlockIndexRight !=- 1) {
                    int index = x+y*BLOCK_WIDTH;
                    MapBlockActionData newValue = new MapBlockActionData(layout, map.getBlockset().getBlocks()[mapBlockLayoutPanel.selectedBlockIndexRight], index);
                    MapBlockActionData olValue = new MapBlockActionData(layout, layout.getBlocks()[index].getMapBlock(), index);
                    ActionManager.setAndExecuteAction(new CumulativeAction<MapBlockActionData>(this, "Set Block", this::actionSetBlock, newValue, olValue));
                }
                break;
            case MouseEvent.BUTTON1:
                if (!evt.released()) {
                    if (mapBlockLayoutPanel.selectedBlockIndexLeft !=- 1) {
                        int index = x+y*BLOCK_WIDTH;
                        MapBlockActionData newValue = new MapBlockActionData(layout, map.getBlockset().getBlocks()[mapBlockLayoutPanel.selectedBlockIndexLeft], index);
                        MapBlockActionData olValue = new MapBlockActionData(layout, layout.getBlocks()[index].getMapBlock(), index);
                        ActionManager.setAndExecuteAction(new CumulativeAction<MapBlockActionData>(this, "Set Block", this::actionSetBlock, newValue, olValue));
                        
                    } else if (evt.pressed() && copiedBlocks != null) {
                        int width = copiedBlocks.length;
                        int height = copiedBlocks[0].length;
                        MapLayoutBlock[] mapBlocks = layout.getBlocks();
                        MapLayoutBlock[][] currentBlocks = new MapLayoutBlock[width][height];
                        for (int j=0; j < height; j++) {
                            for (int i=0; i < width; i++) {
                                currentBlocks[i][j] = mapBlocks[x+i+(y+j)*BLOCK_WIDTH];
                            }
                        }
                        MapCopyBlocksActionData newValue = new MapCopyBlocksActionData(x, y, copiedBlocks);
                        MapCopyBlocksActionData oldValue = new MapCopyBlocksActionData(x, y, currentBlocks);
                        ActionManager.setAndExecuteAction(new CustomAction<MapCopyBlocksActionData>(this, "Paste Blocks", this::actionBlockPaste, newValue, oldValue));
                    }
                }
                break;
            case MouseEvent.BUTTON2:
                if (evt.released()) {
                    if (x == copiedBlocksStartX && y == copiedBlocksStartY) {
                        selectedBlock = layout.getBlocks()[x+y*BLOCK_WIDTH].getMapBlock();
                        if (selectedBlock == null) selectedBlock = map.getBlockset().getBlocks()[0];
                        ActionManager.setAndExecuteAction(new BasicAction<MapBlock>(this, "Block Selection - Left (Copy)", this::actionUpdateLeftSlot, selectedBlock, leftSlot.getBlock()));
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
                        MapLayoutBlock[][] copyBlocks = new MapLayoutBlock[width][height];
                        MapLayoutBlock[] blocks = layout.getBlocks();
                        for(int j=0; j < height; j++) {
                            for (int i=0; i < width;i++){
                                copyBlocks[i][j] = blocks[xStart+i+(yStart+j)*BLOCK_WIDTH].clone();
                            }
                        }
                        MapCopyBlocksActionData newValue = new MapCopyBlocksActionData(xStart, yStart, copyBlocks);
                        MapCopyBlocksActionData oldValue = null;
                        if (mapBlockLayoutPanel.selectedBlockIndexLeft == -1) {
                            oldValue = null;
                        } else {
                            //Sentinel (x = -1 means no copy data. y = index of currently selected)
                            oldValue = new MapCopyBlocksActionData(-1, mapBlockLayoutPanel.selectedBlockIndexLeft, null);
                        }
                        ActionManager.setAndExecuteAction(new CustomAction<MapCopyBlocksActionData>(this, "Copy Blocks", this::actionBlockCopy, newValue, oldValue));
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
                } else if (evt.pressed()) {
                    copiedBlocksStartX = lastMapX = x;
                    copiedBlocksStartY = lastMapY = y;
                    copiedBlocks = null;
                    redraw();
                }
                break;
        }
    }
    
    private void actionSetBlock(List<MapBlockActionData> values) {
        for (MapBlockActionData value : values) {
            value.layout().getBlocks()[value.layoutIndex()].setMapBlock(value.block());
        }
        redraw();
    }
    
    private void actionBlockCopy(MapCopyBlocksActionData value) {
        if (value == null) {
            clearCopiedBlocks();
            mapBlockLayoutPanel.selectedBlockIndexLeft = -1;
        } else if (value.x() == -1) {
            //Sentienl
            clearCopiedBlocks();
            mapBlockLayoutPanel.selectedBlockIndexLeft = value.y();
        } else {
            copiedBlocksStartX = value.x();
            copiedBlocksStartY = value.y();
            copiedBlocks = value.copyData();
            
            mapBlockLayoutPanel.selectedBlockIndexLeft = -1;
            previewImage = null;
            
            //Create preview image
            int dim;
            Point offset = new Point();
            if (copiedBlocks.length*PIXEL_WIDTH > copiedBlocks[0].length*PIXEL_HEIGHT) {
                dim = copiedBlocks.length*PIXEL_WIDTH;
                offset.y = (copiedBlocks.length*PIXEL_WIDTH-copiedBlocks[0].length*PIXEL_HEIGHT)/2;
            } else {
                dim = copiedBlocks[0].length*PIXEL_HEIGHT;
                offset.x = (copiedBlocks[0].length*PIXEL_HEIGHT-copiedBlocks.length*PIXEL_WIDTH)/2;
            }
            BufferedImage preview = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = (Graphics2D)preview.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, dim, dim);
            for (int i=0; i < copiedBlocks.length; i++) {
                for (int j=0; j < copiedBlocks[i].length; j++) {
                    graphics.drawImage(copiedBlocks[i][j].getMapBlock().getIndexedColorImage(layout.getTilesets()), offset.x+i*PIXEL_WIDTH, offset.y+j*PIXEL_HEIGHT, null);
                }
            }
            graphics.dispose();
            leftSlot.setOverrideImage(preview);
        }
        redraw();
    }
    
    private void actionBlockPaste(MapCopyBlocksActionData value) {
        MapLayoutBlock[] blocks = layout.getBlocks();
        int x = value.x();
        int y = value.y();
        MapLayoutBlock[][] copyData = value.copyData();
        for (int i = 0; i < copyData.length; i++) {
            for (int j = 0; j < copyData[0].length; j++) {
                blocks[x+i + (y+j)*BLOCK_WIDTH] = copyData[i][j];
            }
        }
        redraw();
    }
    
    private void actionUpdateLeftSlot(MapBlock block) {
        copiedBlocksStartX = -1;
        copiedBlocksStartY = -1;
        copiedBlocks = null;
        previewImage = null;
        selectedBlock = block;
        mapBlockLayoutPanel.selectedBlockIndexLeft = block == null ? -1 : block.getIndex();
        leftSlot.setBlock(block);
    }
    
    private void editMapFlags(BaseMouseCoordsComponent.GridMousePressedEvent evt) {
        if (evt.released()) return;
        int index = evt.x()+evt.y()*BLOCK_WIDTH;
        MapLayoutBlock block = layout.getBlocks()[index];
        
        if (evt.pressed() && !evt.dragging()) {
            actionDraggedFlag = currentPaintMode;
            if (evt.mouseButton() == MouseEvent.BUTTON1 && actionDraggedFlag == BlockFlags.MAP_FLAG_STAIRS_RIGHT && layout.getBlocks()[index].getFlags().getExplorationFlags() == BlockFlags.MAP_FLAG_STAIRS_RIGHT) {
                actionDraggedFlag = BlockFlags.MAP_FLAG_STAIRS_LEFT;
            }
        }
        
        switch (evt.mouseButton()) {
            case MouseEvent.BUTTON1:
            {
                int flagVal = actionDraggedFlag;
                if (!block.getFlags().isFlagOn(flagVal)) {
                    BlockFlags newFlags = block.getFlags().clone();
                    if ((flagVal & BlockFlags.MAP_FLAG_MASK_EXPLORE) != 0) {
                        newFlags.removeFlag(BlockFlags.MAP_FLAG_MASK_EXPLORE);
                        newFlags.setFlag(flagVal, true);
                    } else if ((flagVal & BlockFlags.MAP_FLAG_MASK_EVENTS) != 0) {
                        newFlags.removeFlag(BlockFlags.MAP_FLAG_MASK_EVENTS);
                        newFlags.setFlag(flagVal, true);
                    }
                    MapFlagActionData newValue = new MapFlagActionData(layout, new BlockFlags(flagVal), index, newFlags);
                    MapFlagActionData oldValue = new MapFlagActionData(layout, new BlockFlags(flagVal), index, block.getFlags());
                    ActionManager.setAndExecuteAction(new CumulativeAction<MapFlagActionData>(this, "Set Map Flag", this::actionMapFlagSet, newValue, oldValue));
                }
                break;
            }
            case MouseEvent.BUTTON2:
            {
                if (block.getFlags().value() != 0) {
                    MapFlagActionData newValue = new MapFlagActionData(layout, new BlockFlags(0), index, new BlockFlags(0));
                    MapFlagActionData oldValue = new MapFlagActionData(layout, new BlockFlags(0), index, block.getFlags());
                    ActionManager.setAndExecuteAction(new CumulativeAction<MapFlagActionData>(this, "Clear Map Flag", this::actionMapFlagSet, newValue, oldValue));
                }
                break;
            }
            case MouseEvent.BUTTON3:
            {
                if (currentPaintMode == BlockFlags.MAP_FLAG_STAIRS_RIGHT) {
                    if (layout.getBlocks()[index].getFlags().getExplorationFlags() == BlockFlags.MAP_FLAG_STAIRS_RIGHT) {
                        actionDraggedFlag = BlockFlags.MAP_FLAG_STAIRS_RIGHT;
                    } else if (layout.getBlocks()[index].getFlags().getExplorationFlags() == BlockFlags.MAP_FLAG_STAIRS_LEFT) {
                        actionDraggedFlag = BlockFlags.MAP_FLAG_STAIRS_LEFT;
                    }
                }
                int flagVal = actionDraggedFlag;
                if (block.getFlags().isFlagOn(flagVal)) {
                    BlockFlags newFlags = block.getFlags().clone();
                    if ((flagVal & BlockFlags.MAP_FLAG_MASK_EXPLORE) != 0) {
                        newFlags.removeFlag(BlockFlags.MAP_FLAG_MASK_EXPLORE);
                        newFlags.setFlag(flagVal, false);
                    } else if ((flagVal & BlockFlags.MAP_FLAG_MASK_EVENTS) != 0) {
                        newFlags.removeFlag(BlockFlags.MAP_FLAG_MASK_EVENTS);
                        newFlags.setFlag(flagVal, false);
                    }
                    MapFlagActionData newValue = new MapFlagActionData(layout, new BlockFlags(flagVal), index, newFlags);
                    MapFlagActionData oldValue = new MapFlagActionData(layout, new BlockFlags(flagVal), index, block.getFlags());
                    ActionManager.setAndExecuteAction(new CumulativeAction<MapFlagActionData>(this, "Clear Map Flag", this::actionMapFlagSet, newValue, oldValue));
                }
                break;
            }
        }
    }
    
    private void actionMapFlagSet(List<MapFlagActionData> values) {
        for (MapFlagActionData value : values) {
            MapLayoutBlock[] blocks = value.layout().getBlocks();
            MapLayoutBlock block = blocks[value.layoutIndex()];
            block.setFlags(value.blockNewFlag());
        }
        redraw();
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
