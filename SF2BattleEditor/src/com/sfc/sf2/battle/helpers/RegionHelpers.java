/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.helpers;

import com.sfc.sf2.battle.AIRegion;
import java.awt.Point;

/**
 *
 * @author timmy
 */
public class RegionHelpers {
    
    private static Point[] triPoints;
    
    public static boolean isPointInRegion(Point p, AIRegion region) {            
        if (triPoints == null)
            triPoints = new Point[3];
        switch (region.getType()) {
            case 3: //3-points
                triPoints[0] = region.getPoint(0);
                triPoints[1] = region.getPoint(1);
                triPoints[2] = region.getPoint(2);
                return isPointInTriangle(p, triPoints);
            case 4: //4-points (2 triangles)
                triPoints[0] = region.getPoint(0);
                triPoints[1] = region.getPoint(1);
                triPoints[2] = region.getPoint(3);
                if (isPointInTriangle(p, triPoints)) {
                    return true;
                }
                triPoints[0] = region.getPoint(1);
                triPoints[1] = region.getPoint(2);
                triPoints[2] = region.getPoint(3);
                return isPointInTriangle(p, triPoints);
            default:
                return false;
        }
    }
    
    public static boolean isPointInTriangle(Point p, Point[] tri) {
        int flag;
        
        flag = checkEdge(tri[0], tri[1], p);
        if (flag == 0b01) return true; // Collinear in-bounds
        if ((flag&1) == 0 && flag != checkEdge(tri[0], tri[1], tri[2])) return false;

        flag = checkEdge(tri[2], tri[0], p);
        if (flag == 0b01) return true; // Collinear in-bounds
        if ((flag&1) == 0 && flag != checkEdge(tri[2], tri[0], tri[1])) return false;

        flag = checkEdge(tri[2], tri[1], p);
        if (flag == 0b01) return true; // Collinear in-bounds
        if ((flag&1) != 0) return false; // Exception flags (%11) means outside of triangle

        return flag == checkEdge(tri[2], tri[1], tri[0]);
    }

    /**
     * Replicates 'CheckTestPointPositionRelativeToSegment' from disasm/code/gameflow/battle/battleloop/triggerregions.asm
     */
    private static int checkEdge(Point a, Point b, Point c) {
        int dyBA = b.y - a.y;
        int dxBA = b.x - a.x;

        //Determine dominant axis (Horizontal vs Vertical path)
        if (Math.abs(dxBA) >= Math.abs(dyBA)) {
            if (dxBA == 0) {
                return checkVerticalPath(a, b, c, dyBA);    //Fallback to prevent division by zero
            } else {
                return checkHorizontalPath(a, b, c, dxBA, dyBA);
            }
        } else {
            return checkVerticalPath(a, b, c, dyBA);
        }
    }

    /**
     * Replicates @horizontalPath from disasm/code/gameflow/battle/battleloop/triggerregions.asm
     */
    private static int checkHorizontalPath(Point a, Point b, Point c, int dxBA, int dyBA) {
        int yProj = b.y + ((dyBA * (c.x - b.x)) / dxBA); //Integer division
        if (yProj < 0) return 0b10;

        if (yProj != c.y) {
            return (yProj < c.y) ? 0b10 : 0b00;
        }
        
        boolean inBounds = (a.x >= b.x) ? (c.x <= a.x && b.x <= c.x) : (b.x > c.x && c.x >= a.x);
        return inBounds ? 0b01 : 0b10;
    }

    /**
     * Replicates @verticalPath from disasm/code/gameflow/battle/battleloop/triggerregions.asm
     */
    private static int checkVerticalPath(Point a, Point b, Point c, int dyBA) {
        if (dyBA == 0) return 0b11;

        int xProj = b.x + (((b.x - a.x) * (c.y - b.y)) / dyBA); //Integer division
        if (xProj < 0) return 0b10;

        if (xProj != c.x) {
            return (xProj < c.x) ? 0b10 : 0b00;
        }
        
        boolean inBounds = (a.y >= b.y) ? (c.y <= a.y && b.y <= c.y) : (b.y > c.y && c.y >= a.y);
        return inBounds ? 0b01 : 0b10;
    }
}
