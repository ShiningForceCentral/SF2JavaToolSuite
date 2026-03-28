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
public class MapArea {
    
    private int layer1StartX;
    private int layer1StartY;
    private int layer1EndX;
    private int layer1EndY;
    private int foregroundLayer2StartX;
    private int foregroundLayer2StartY;
    private int backgroundLayer2StartX;
    private int backgroundLayer2StartY;
    private int layer1ParallaxX;
    private int layer1ParallaxY;
    private int layer2ParallaxX;
    private int layer2ParallaxY;
    private int layer1AutoscrollX;
    private int layer1AutoscrollY;
    private int layer2AutoscrollX;
    private int layer2AutoscrollY;
    private int layerType;
    private String defaultMusic;

    public MapArea(int layer1StartX, int layer1StartY, int layer1EndX, int layer1EndY, int foregroundLayer2StartX, int foregroundLayer2StartY, int backgroundLayer2StartX, int backgroundLayer2StartY, int layer1ParallaxX, int layer1ParallaxY, int layer2ParallaxX, int layer2ParallaxY, int layer1AutoscrollX, int layer1AutoscrollY, int layer2AutoscrollX, int layer2AutoscrollY, int layerType, String defaultMusic) {
        this.layer1StartX = layer1StartX;
        this.layer1StartY = layer1StartY;
        this.layer1EndX = layer1EndX;
        this.layer1EndY = layer1EndY;
        this.foregroundLayer2StartX = foregroundLayer2StartX;
        this.foregroundLayer2StartY = foregroundLayer2StartY;
        this.backgroundLayer2StartX = backgroundLayer2StartX;
        this.backgroundLayer2StartY = backgroundLayer2StartY;
        this.layer1ParallaxX = layer1ParallaxX;
        this.layer1ParallaxY = layer1ParallaxY;
        this.layer2ParallaxX = layer2ParallaxX;
        this.layer2ParallaxY = layer2ParallaxY;
        this.layer1AutoscrollX = layer1AutoscrollX;
        this.layer1AutoscrollY = layer1AutoscrollY;
        this.layer2AutoscrollX = layer2AutoscrollX;
        this.layer2AutoscrollY = layer2AutoscrollY;
        this.layerType = layerType;
        this.defaultMusic = defaultMusic;
    }
    
    public Rectangle getLayer1() {
        return new Rectangle(layer1StartX, layer1StartY, layer1EndX-layer1StartX, layer1EndY-layer1StartY);
    }
    
    public void setLayer1(Rectangle rect) {
        layer1StartX = rect.x;
        layer1StartY = rect.y;
        layer1EndX = rect.x+rect.width;
        layer1EndY = rect.y+rect.height;
    }

    public int getLayer1StartX() {
        return layer1StartX;
    }

    public void setLayer1StartX(int layer1StartX) {
        this.layer1StartX = layer1StartX;
    }

    public int getLayer1StartY() {
        return layer1StartY;
    }

    public void setLayer1StartY(int layer1StartY) {
        this.layer1StartY = layer1StartY;
    }

    public int getLayer1EndX() {
        return layer1EndX;
    }

    public void setLayer1EndX(int layer1EndX) {
        this.layer1EndX = layer1EndX;
    }

    public int getLayer1EndY() {
        return layer1EndY;
    }

    public void setLayer1EndY(int layer1EndY) {
        this.layer1EndY = layer1EndY;
    }
    
    public int getWidth() {
        return layer1EndX-layer1StartX+1;
    }
    
    public int getHeight() {
        return layer1EndY-layer1StartY+1;
    }
    
    public boolean hasForegroundLayer2() {
        return foregroundLayer2StartX != 0 || foregroundLayer2StartY != 0;
    }
    
    public Point getForegroundLayer2() {
        return new Point(foregroundLayer2StartX, foregroundLayer2StartY);
    }
    
    public void setForegroundLayer2(Point point) {
        foregroundLayer2StartX = point.x;
        foregroundLayer2StartY = point.y;
    }

    public int getForegroundLayer2StartX() {
        return foregroundLayer2StartX;
    }

    public void setForegroundLayer2StartX(int foregroundLayer2StartX) {
        this.foregroundLayer2StartX = foregroundLayer2StartX;
    }

    public int getForegroundLayer2StartY() {
        return foregroundLayer2StartY;
    }

    public void setForegroundLayer2StartY(int foregroundLayer2StartY) {
        this.foregroundLayer2StartY = foregroundLayer2StartY;
    }
    
    public boolean hasBackgroundLayer2() {
        return backgroundLayer2StartX != 0 || backgroundLayer2StartY != 0;
    }
    
    public Point getBackgroundLayer2() {
        return new Point(backgroundLayer2StartX, backgroundLayer2StartY);
    }
    
    public void setBackgroundLayer2(Point point) {
        backgroundLayer2StartX = point.x;
        backgroundLayer2StartY = point.y;
    }

    public int getBackgroundLayer2StartX() {
        return backgroundLayer2StartX;
    }

    public void setBackgroundLayer2StartX(int backgroundLayer2StartX) {
        this.backgroundLayer2StartX = backgroundLayer2StartX;
    }

    public int getBackgroundLayer2StartY() {
        return backgroundLayer2StartY;
    }

    public void setBackgroundLayer2StartY(int backgroundLayer2StartY) {
        this.backgroundLayer2StartY = backgroundLayer2StartY;
    }
    
    public boolean doesLayer1Parallax() {
        return layer1ParallaxX != 0 || layer1ParallaxY != 0;
    }
    
    public Point getLayer1Parallax() {
        return new Point(layer1ParallaxX, layer1ParallaxY);
    }
    
    public void setLayer1Parallax(Point point) {
        layer1ParallaxX = point.x;
        layer1ParallaxY = point.y;
    }

    public int getLayer1ParallaxX() {
        return layer1ParallaxX;
    }

    public void setLayer1ParallaxX(int layer1ParallaxX) {
        this.layer1ParallaxX = layer1ParallaxX;
    }

    public int getLayer1ParallaxY() {
        return layer1ParallaxY;
    }

    public void setLayer1ParallaxY(int layer1ParallaxY) {
        this.layer1ParallaxY = layer1ParallaxY;
    }
    
    public boolean doesLayer2Parallax() {
        return layer2ParallaxX != 0 || layer2ParallaxY != 0;
    }
    
    public Point getLayer2Parallax() {
        return new Point(layer2ParallaxX, layer2ParallaxY);
    }
    
    public void setLayer2Parallax(Point point) {
        layer2ParallaxX = point.x;
        layer2ParallaxY = point.y;
    }

    public int getLayer2ParallaxX() {
        return layer2ParallaxX;
    }

    public void setLayer2ParallaxX(int layer2ParallaxX) {
        this.layer2ParallaxX = layer2ParallaxX;
    }

    public int getLayer2ParallaxY() {
        return layer2ParallaxY;
    }

    public void setLayer2ParallaxY(int layer2ParallaxY) {
        this.layer2ParallaxY = layer2ParallaxY;
    }
    
    public boolean doesLayer1AutoScroll() {
        return layer1AutoscrollX != 0 || layer1AutoscrollY != 0;
    }
    
    public Point getLayer1AutoScroll() {
        return new Point(layer1AutoscrollX, layer1AutoscrollY);
    }
    
    public void setLayer1AutoScroll(Point point) {
        layer1AutoscrollX = point.x;
        layer1AutoscrollY = point.y;
    }

    public int getLayer1AutoscrollX() {
        return layer1AutoscrollX;
    }

    public void setLayer1AutoscrollX(int layer1AutoscrollX) {
        this.layer1AutoscrollX = layer1AutoscrollX;
    }

    public int getLayer1AutoscrollY() {
        return layer1AutoscrollY;
    }

    public void setLayer1AutoscrollY(int layer1AutoscrollY) {
        this.layer1AutoscrollY = layer1AutoscrollY;
    }
    
    public boolean doesLayer2AutoScroll() {
        return layer2AutoscrollX != 0 || layer2AutoscrollY != 0;
    }
    
    public Point getLayer2AutoScroll() {
        return new Point(layer2AutoscrollX, layer2AutoscrollY);
    }
    
    public void setLayer2AutoScroll(Point point) {
        layer2AutoscrollX = point.x;
        layer2AutoscrollY = point.y;
    }

    public int getLayer2AutoscrollX() {
        return layer2AutoscrollX;
    }

    public void setLayer2AutoscrollX(int layer2AutoscrollX) {
        this.layer2AutoscrollX = layer2AutoscrollX;
    }

    public int getLayer2AutoscrollY() {
        return layer2AutoscrollY;
    }

    public void setLayer2AutoscrollY(int layer2AutoscrollY) {
        this.layer2AutoscrollY = layer2AutoscrollY;
    }

    public int getLayerType() {
        return layerType;
    }

    public void setLayerType(int layerType) {
        this.layerType = layerType;
    }

    public String getDefaultMusic() {
        return defaultMusic;
    }

    public void setDefaultMusic(String defaultMusic) {
        this.defaultMusic = defaultMusic;
    }
    
    public static MapArea createEmpty() {
        return new MapArea(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, "NONE");
    }

    @Override
    public MapArea clone() {
        return new MapArea(layer1StartX, layer1StartY, layer1EndX, layer1EndY, foregroundLayer2StartX, foregroundLayer2StartY, backgroundLayer2StartX, backgroundLayer2StartY, layer1ParallaxX, layer1ParallaxY, layer2ParallaxX, layer2ParallaxY, layer1AutoscrollX, layer1AutoscrollY, layer2AutoscrollX, layer2AutoscrollY, layerType, defaultMusic);
    }
}
