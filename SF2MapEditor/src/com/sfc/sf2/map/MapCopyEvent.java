/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author wiz
 */
public class MapCopyEvent {
    
    private int triggerX;
    private int triggerY;
    private int sourceStartX;
    private int sourceStartY;
    private int sourceEndX;
    private int sourceEndY;
    private int destStartX;
    private int destStartY;
    private String comment;

    public MapCopyEvent(int triggerX, int triggerY, int sourceStartX, int sourceStartY, int sourceEndX, int sourceEndY, int destStartX, int destStartY, String comment) {
        this.triggerX = triggerX;
        this.triggerY = triggerY;
        this.sourceStartX = sourceStartX;
        this.sourceStartY = sourceStartY;
        this.sourceEndX = sourceEndX;
        this.sourceEndY = sourceEndY;
        this.destStartX = destStartX;
        this.destStartY = destStartY;
        this.comment = comment;
    }

    public Point getTrigger() {
        return new Point(triggerX, triggerY);
    }

    public void setTrigger(Point point) {
        this.triggerX = point.x;
        this.triggerY = point.y;
    }

    public int getTriggerX() {
        return triggerX;
    }

    public void setTriggerX(int triggerX) {
        this.triggerX = triggerX;
    }

    public int getTriggerY() {
        return triggerY;
    }

    public void setTriggerY(int triggerY) {
        this.triggerY = triggerY;
    }

    public Rectangle getSource() {
        return new Rectangle(sourceStartX, sourceStartY, sourceEndX-sourceStartX, sourceEndY-sourceStartY);
    }

    public void setSource(Rectangle rect) {
        this.sourceStartX = rect.x;
        this.sourceStartY = rect.y;
        this.sourceEndX = rect.x+rect.width;
        this.sourceEndY = rect.y+rect.height;
    }
    
    public int getSourceStartX() {
        return sourceStartX;
    }

    public void setSourceStartX(int sourceStartX) {
        this.sourceStartX = sourceStartX;
    }

    public int getSourceStartY() {
        return sourceStartY;
    }

    public void setSourceStartY(int sourceStartY) {
        this.sourceStartY = sourceStartY;
    }

    public int getSourceEndX() {
        return sourceEndX;
    }

    public void setSourceEndX(int sourceEndX) {
        this.sourceEndX = sourceEndX;
    }

    public int getSourceEndY() {
        return sourceEndY;
    }

    public void setSourceEndY(int sourceEndY) {
        this.sourceEndY = sourceEndY;
    }
    
    public int getWidth() {
        return isAutoSource() ? sourceEndX : sourceEndX-sourceStartX+1;
    }
    
    public int getHeight() {
        return isAutoSource() ? sourceEndY : sourceEndY-sourceStartY+1;
    }

    public boolean isAutoSource() {
        return sourceStartX == 0xFF && sourceStartY == 0xFF;
    }

    /**
     * Layer-1 origin of the copy. For auto (255,255) roofs this is dest minus
     * the main area's layer-2 offset, plus the area's layer-1 origin.
     */
    public Point getDisplaySourceStart(MapArea area) {
        if (isAutoSource() && area != null) {
            return new Point(
                    destStartX - area.getForegroundLayer2StartX() + area.getLayer1StartX(),
                    destStartY - area.getForegroundLayer2StartY() + area.getLayer1StartY());
        }
        return new Point(sourceStartX, sourceStartY);
    }

    public void setAutoSourceSize(int width, int height) {
        sourceStartX = 0xFF;
        sourceStartY = 0xFF;
        sourceEndX = Math.max(1, width);
        sourceEndY = Math.max(1, height);
    }

    public boolean isRectOutOfBounds(int x, int y, int width, int height, int mapWidth, int mapHeight) {
        return x < 0 || y < 0 || width <= 0 || height <= 0
                || x + width > mapWidth || y + height > mapHeight;
    }

    public Point getDest() {
        return new Point(destStartX, destStartY);
    }

    public void setDest(Point point) {
        this.destStartX = point.x;
        this.destStartY = point.y;
    }

    public int getDestStartX() {
        return destStartX;
    }

    public void SetDestStartX(int destStartX) {
        this.destStartX = destStartX;
    }

    public int getDestStartY() {
        return destStartY;
    }

    public void setDestStartY(int destStartY) {
        this.destStartY = destStartY;
    }

    public int getDestEndX() {
        return destStartX+getWidth()-1;
    }

    public int getDestEndY() {
        return destStartY+getHeight()-1;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public static MapCopyEvent createEmpty() {
        return new MapCopyEvent(0, 0, 0, 0, 1, 1, 1, 1, null);
    }

    @Override
    public MapCopyEvent clone() {
        return new MapCopyEvent(triggerX, triggerY, sourceStartX, sourceStartY, sourceEndX, sourceEndY, destStartX, destStartY, comment);
    }
}
