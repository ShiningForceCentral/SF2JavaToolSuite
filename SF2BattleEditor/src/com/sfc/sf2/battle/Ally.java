/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import java.awt.Point;

/**
 *
 * @author wiz
 */
public class Ally {
    private int x;
    private int y;

    public Ally(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point getPos() {
        return new Point(x, y);
    }

    public void setPos(Point pos) {
        this.x = pos.x;
        this.y = pos.y;
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
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Ally)) return super.equals(obj);
        Ally other = (Ally)obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public Ally clone() {
        return new Ally(x, y);
    }
    
    public static Ally emptyAlly() {
        return new Ally(0, 0);
    }
}
