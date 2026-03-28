/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import com.sfc.sf2.palette.CRAMColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author wiz
 */
public class ColorPane extends JPanel implements MouseListener, MouseMotionListener {

    private static final Border DEFAULT_BORDER = new MatteBorder(1, 1, 1, 1, Color.GRAY);
    private static final Border HOVER_BORDER = new MatteBorder(2, 2, 2, 2, Color.DARK_GRAY);
    private static final Border SELETED_BORDER = new MatteBorder(2, 2, 2, 2, Color.WHITE);
    
    private static ColorPane LAST_SELECTED;
    
    private PalettePane palettePane;
    private int thisIndex;
    private CRAMColor currentColor;

    public ColorPane(PalettePane palettePane, int thisIndex, CRAMColor color) {
        this.palettePane = palettePane;
        this.thisIndex = thisIndex;
        updateColor(color);
        setBackground(currentColor.CRAMColor());
        setBorder(DEFAULT_BORDER);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(30, 30);
    }

    public CRAMColor getCurrentColor() {
        return currentColor;
    }
    
    public void setSelected() {
        setBorder(true, false);
    }
    
    public static void clearSelection() {
        if (LAST_SELECTED == null) return;
        LAST_SELECTED.setBorder(DEFAULT_BORDER);
        LAST_SELECTED = null;
    }
    
    private void setBorder(boolean selected, boolean hover) {
        if (LAST_SELECTED == this || selected) {
            if (LAST_SELECTED != null) {
                LAST_SELECTED.setBorder(DEFAULT_BORDER);
            }
            setBorder(SELETED_BORDER);
            LAST_SELECTED = this;
        } else if (hover) {
            setBorder(HOVER_BORDER);
        } else {
            setBorder(DEFAULT_BORDER);
        }
    }
    
    public void updateColor(CRAMColor c) {
        currentColor = c;
        if (c.CRAMColor().getAlpha() == 0) {
            int rgb = c.CRAMColor().getRGB() & 0xFF;
            currentColor = CRAMColor.fromPremadeCramColor(new Color(rgb));
        }
        setBackground(this.currentColor.CRAMColor());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        boolean selected = false;
        if (SwingUtilities.isLeftMouseButton(e)) {
            palettePane.setColorPaneSelected(thisIndex);
            selected = true;
        }
        setBorder(selected, true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(LAST_SELECTED == this, false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != 1) return;
        palettePane.setColorPaneSelected(thisIndex);
        setBorder(true, false);
    }

    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseDragged(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mouseMoved(MouseEvent e) { }
}    
