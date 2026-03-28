/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.gui.layout;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.gui.AbstractLayoutPanel;
import com.sfc.sf2.core.gui.controls.Console;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

/**
 * A common component to handle mouse movement and input in common ways. Handles click and drag as similar operations
 * Be sure to call the {@code setupListeners} function (could not be in constructor).
 * @author TiMMy
 */
public abstract class BaseMouseCoordsComponent extends BaseLayoutComponent implements MouseListener, MouseMotionListener {
    
    private static final Dimension NO_OFFSET = new Dimension();
    
    private final Dimension mouseCoordsGrid;
    private AbstractLayoutPanel panel;
    private GridMousePressedListener buttonListener;
    private GridMouseMoveListener motionListener;
    
    private Dimension bounds = NO_OFFSET;
    private Dimension coordsOffset = NO_OFFSET;
    private float displayScale = 1;
    
    private int lastX = -1;
    private int lastY = -1;
    private int buttonHeld = -1;

    public BaseMouseCoordsComponent(AbstractLayoutPanel panel, int gridX, int gridY) {
        this.panel = panel;
        this.mouseCoordsGrid = new Dimension(gridX, gridY);
        setEnabled(false);  //Disabled until listeners are setup
    }
    
    protected void setupListeners(GridMousePressedListener buttonListener, GridMouseMoveListener motionListener) {
        this.buttonListener = buttonListener;
        this.motionListener = motionListener;
        setEnabled(true);
        if (buttonListener != null) {
            panel.addMouseListener(this);
            panel.addMouseMotionListener(this);
        } else if (motionListener != null) {
            panel.addMouseMotionListener(this);
        } else {
            Console.logger().warning("WARNING Mouse coords component instantiated in " + panel.getName() + " without any callbacks. Will be disabled.");
            setEnabled(false);
        }
    }
    
    public void setMouseButtonListener(GridMousePressedListener buttonListener) {
        this.buttonListener = buttonListener;
    }
    
    public void setMouseMotionListener(GridMouseMoveListener motionListener) {
        this.motionListener = motionListener;
    }
    
    public void updateDisplayParameters(float displayScale, Dimension bounds, Dimension coordsOffset) {
        this.bounds = bounds;
        this.displayScale = displayScale;
        this.coordsOffset = coordsOffset;
    }
    
    private int getXCoord(int mouseX) {
        int x = mouseX-coordsOffset.width;
        if (x < 0) x = 0;
        else if (x >= bounds.width-coordsOffset.width) x = bounds.width-coordsOffset.width-1;
        x /= (displayScale * mouseCoordsGrid.width);
        return x;
    }
    
    private int getYCoord(int mouseY) {
        int y = mouseY-coordsOffset.height;
        if (y < 0) y = 0;
        else if (y >= bounds.height-coordsOffset.height) y = bounds.height-coordsOffset.height-1;
        y /= (displayScale * mouseCoordsGrid.height);
        return y;
    }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if (motionListener == null) return;
        if (!panel.contains(e.getPoint())) return;
        int x = getXCoord(e.getX());
        int y = getYCoord(e.getY());
        if (x == -1 || y == -1) return;
        if (x == lastX && y == lastY) return;
        lastX = x;
        lastY = y;
        motionListener.mouseMoved(new GridMouseMoveEvent(x, y, false));
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (buttonListener == null) return;
        if (!panel.contains(e.getPoint())) return;
        int x = getXCoord(e.getX());
        int y = getYCoord(e.getY());
        if (x == -1 || y == -1) return;
        lastX = x;
        lastY = y;
        buttonHeld = e.getButton();
        buttonListener.mousePressed(new GridMousePressedEvent(x, y, buttonHeld, false, true, false));
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (buttonHeld == -1) return;
        if (buttonListener == null && motionListener == null) return;
        if (!panel.contains(e.getPoint())) return;
        int x = getXCoord(e.getX());
        int y = getYCoord(e.getY());
        if (x == -1 || y == -1) return;
        if (x == lastX && y == lastY) return;
        lastX = x;
        lastY = y;
        if (buttonListener != null) {
            buttonListener.mousePressed(new GridMousePressedEvent(x, y, buttonHeld, true, true, false));
        }
        if (motionListener != null) {
            motionListener.mouseMoved(new GridMouseMoveEvent(x, y, true));
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (buttonHeld == -1) return;
        int x = getXCoord(e.getX());
        int y = getYCoord(e.getY());
        if (x == -1 || y == -1) return;
        if (buttonListener != null) {
            buttonListener.mousePressed(new GridMousePressedEvent(x, y, buttonHeld, false, false, true));
        }
        buttonHeld = -1;
        //When mouse is released then stop any action from combining
        ActionManager.preventActionsCombining();
    }
    
    public record GridMousePressedEvent(int x, int y, int mouseButton, boolean dragging, boolean pressed, boolean released) { }
    public interface GridMousePressedListener extends EventListener {
        public void mousePressed(GridMousePressedEvent evt);
    }
    
    public record GridMouseMoveEvent(int x, int y, boolean dragging) { }
    public interface GridMouseMoveListener extends EventListener {
        public void mouseMoved(GridMouseMoveEvent evt);
    }
}