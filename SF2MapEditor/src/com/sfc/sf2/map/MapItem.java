/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map;

import java.awt.Point;

/**
 *
 * @author wiz
 */
public class MapItem {
    
    private int x;
    private int y;
    private int flag;
    private String item;
    private String comment;

    public MapItem(int x, int y, int flag, String item, String comment) {
        this.x = x;
        this.y = y;
        this.flag = flag;
        this.item = item;
        this.comment = comment;
    }

    public Point getPos() {
        return new Point(x, y);
    }

    public void setPos(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
    
    public String getFlagInfo() {
        return MapFlagCopyEvent.getFlagInfo(flag);
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public static MapItem createEmpty() {
        return new MapItem(0, 0, 0, "MEDICAL_HERB", "");
    }

    @Override
    public MapItem clone() {
        return new MapItem(x, y, flag, item, comment);
    }
}
