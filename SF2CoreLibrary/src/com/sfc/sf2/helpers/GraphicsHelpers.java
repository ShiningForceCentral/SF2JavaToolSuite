/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author TiMMy
 */
public class GraphicsHelpers {
    
    public static void drawFlatBackgroundColor(BufferedImage image, Color bgColor) {
        int bgInt = bgColor.getRGB();
        int[] data = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();
        for (int i = 0; i < data.length; i++) {
            data[i] = bgInt;
        }
    }
    
    public static void drawBackgroundTransparencyPattern(BufferedImage image, Color bgColor, int pixelGrid) {
        if (bgColor == null) return;
        Color bgDarkerColor = bgColor.darker();
        int bgInt = bgColor.getRGB();
        int darkInt = bgDarkerColor.getRGB();
        int[] data = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();
        int width = image.getWidth();
        int height = image.getHeight();
        int offset;
        for (int j = 0; j < height; j++) {
            offset = j/pixelGrid;
            for (int i = 0; i < width; i++) {
                data[i+j*width] = (i/pixelGrid + offset)%2 == 0 ? bgInt : darkInt;
            }
        }
    }
    
    public static void drawGrid(BufferedImage image, int gridW, int gridH, int gridThickness) {
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(gridThickness));
        int x = 0;
        int y = 0;
        if (gridW > 0) {
            while (x < image.getWidth()) {
                graphics.drawLine(x, 0, x, image.getHeight());
                x += gridW;
            }
            graphics.drawLine(x-1, 0, x-1, image.getHeight());
        }
        if (gridH > 0) {
            while (y < image.getHeight()) {
                graphics.drawLine(0, y, image.getWidth(), y);
                y += gridH;
            }
            graphics.drawLine(0, y-1, image.getWidth(), y-1);
        }
        graphics.dispose();
    }
    
    /**
    * Draw an arrow line between two points
    */
    public static void drawArrowLine(Graphics2D g2, int x1, int y1, int x2, int y2) {
        drawArrowLine(g2, x1, y1, x2, y2, 1, 20, 5, false, true);
    }
    
    /**
    * Draw an arrow line between two points
    */
    public static void drawArrowLine(Graphics2D g2, int x1, int y1, int x2, int y2, boolean startArrow, boolean endArrow) {
        drawArrowLine(g2, x1, y1, x2, y2, 1, 20, 5, startArrow, endArrow);
    }
   
    /**
    * Draw an arrow line between two points
    * @param stroke Thickness of the line
    * @param arrowOffset Arrowhead distance back from end of line
    * @param arrowSize The size of the arrowhead
    */
    public static void drawArrowLine(Graphics2D g2, int x1, int y1, int x2, int y2, int stroke, int arrowOffset, int arrowSize, boolean startArrow, boolean endArrow) {
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(arrowSize, 0);
        arrowHead.addPoint(-arrowSize, arrowSize);
        arrowHead.addPoint(-arrowSize/2, -0);
        arrowHead.addPoint(-arrowSize, -arrowSize);
        
        Graphics2D g = (Graphics2D)g2.create();
        AffineTransform ot = g.getTransform();
        g.setStroke(new BasicStroke(1));
        g.drawLine(x1, y1, x2, y2);
        
        double l = Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2 - y1), 2));//  line length
        double dx = x2-x1, dy = y2-y1;
        double angle = (Math.atan2(dy, dx)); //get angle (Radians) between ours line and x vectors line. (counter clockwise)
        angle = (-1)*Math.toDegrees(angle);// convert to degree and reverse it to round clock for better understand what we need to do.
        if (angle < 0) {
            angle = 360 + angle; // convert negative degrees to positive degree
        }
        angle = (-1)*angle; // convert to counter clockwise mode
        angle = Math.toRadians(angle);//  convert Degree to Radians

        if (endArrow) {
            double newX = x2+(((x1-x2)/l)*arrowOffset); // new x of arrowhead position on the line with d distance from end of the line.
            double newY = y2+(((y1-y2)/l)*arrowOffset); // new y of arrowhead position on the line with d distance from end of the line.

            AffineTransform at = new AffineTransform();
            at.translate(newX, newY);// transport cursor to draw arrowhead position.
            at.rotate(angle);
            g.transform(at);
            g.fillPolygon(arrowHead);
            g.setTransform(ot);
        }
        if (startArrow) {
            double newX = x1-(((x1-x2)/l)*arrowOffset); // new x of arrowhead position on the line with d distance from end of the line.
            double newY = y1-(((y1-y2)/l)*arrowOffset); // new y of arrowhead position on the line with d distance from end of the line.
            angle += Math.PI;

            AffineTransform at = new AffineTransform();
            at.translate(newX, newY);// transport cursor to draw arrowhead position.
            at.rotate(angle);
            g.transform(at);
            g.fillPolygon(arrowHead);
            g.setTransform(ot);
        }
        g.dispose();
   }
}
