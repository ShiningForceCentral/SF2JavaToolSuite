/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import com.sfc.sf2.helpers.Direction;
import java.awt.Point;

/**
 *
 * @author wiz
 */
public class MapWarpEvent {
        
    private int triggerX;
    private int triggerY;
    private String warpType;
    private Direction scrollDirection;
    private String destMap;
    private int destX;
    private int destY;
    private Direction facing;
    private String comment;

    public MapWarpEvent(int triggerX, int triggerY, String warpType, Direction scrollDirection, String destMap, int destX, int destY, Direction facing, String comment) {
        this.triggerX = triggerX;
        this.triggerY = triggerY;
        this.warpType = warpType;
        this.scrollDirection = scrollDirection;
        this.destMap = destMap;
        this.destX = destX;
        this.destY = destY;
        this.facing = facing;
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

    public String getWarpType() {
        return warpType;
    }

    public void setWarpType(String warpType) {
        this.warpType = warpType;
    }

    public Direction getScrollDirection() {
        return scrollDirection;
    }

    public void setScrollDirection(Direction scrollDirection) {
        this.scrollDirection = scrollDirection;
    }

    public String getDestMap() {
        return destMap;
    }

    public void setDestMap(String destMap) {
        this.destMap = destMap;
    }

    public Point getDest() {
        return new Point(destX, destY);
    }

    public void setDest(Point point) {
        this.destX = point.x;
        this.destY = point.y;
    }

    public int getDestX() {
        return destX;
    }

    public void setDestX(int destX) {
        this.destX = destX;
    }

    public int getDestY() {
        return destY;
    }

    public void setDestY(int destY) {
        this.destY = destY;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public static MapWarpEvent createEmpty() {
        return new MapWarpEvent(0, 0, "warpNoScroll", Direction.RIGHT, "NONE", 0, 0, Direction.DOWN, null);
    }

    @Override
    public MapWarpEvent clone() {
        return new MapWarpEvent(triggerX, triggerY, warpType, scrollDirection, destMap, destX, destY, facing, comment);
    }
}
