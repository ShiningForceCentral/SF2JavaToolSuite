/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import java.awt.Point;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class AIRegion {
    private static String[] typeToString = new String[] { "NONE", "??? (point?)", "??? (line?)", "Triangle", "Rectangle" };
    
    private int type;
    private Point[] points = new Point[4];

    public AIRegion(int type, Point p0, Point p1, Point p2, Point p3) {
        this.type = type;
        points[0] = p0;
        points[1] = p1;
        points[2] = p2;
        points[3] = p3;
    }
    public AIRegion(int type, Point[] points) {
        this.type = type;
        this.points = points;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public String getTypeString() {
        if (type < 0 || type >= typeToString.length) {
            return "???";
        } else {
            return typeToString[type];
        }
    }
    
    public Point[] getPoints() {
        return points;
    }
    
    public Point getPoint(int index) {
        return points[index];
    }
    
    public void setPoint(int index, Point point) {
        points[index] = point;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AIRegion)) return super.equals(obj);
        AIRegion other = (AIRegion)obj;
        return this.type == other.type && Arrays.equals(this.points, other.points);
    }
    
    @Override
    public AIRegion clone() {
        return new AIRegion(type, new Point(points[0]), new Point(points[1]), new Point(points[2]), new Point(points[3]));
    }
    
    public static AIRegion emptyAIRegion() {
        return new AIRegion(4, new Point(0, 0), new Point(0, 4), new Point(4, 4), new Point(4, 0));
    }
}
