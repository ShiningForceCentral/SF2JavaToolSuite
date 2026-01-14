/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.palette.gui.controls.CRAMColorEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.actions.PaletteAction;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 *
 * @author wiz
 */
public class PalettePane extends JPanel {
        
    private CRAMColorEditor colorEditor;
    private Palette palette;
    private ColorPane[] colorPanes;
    
    private ActionListener colorChangeListener;
    
    public PalettePane() {
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        colorPanes = new ColorPane[16];
        for (int i = 0; i < 16; i++) {
            gbc.gridx = i;
            ColorPane colorPane = new ColorPane(this, i, CRAMColor.BLACK);
            colorPanes[i] = colorPane;
            add(colorPane, gbc);
        }
        
        setColorPaneSelected(-1);
    }

    public void setColorChangeListener(ActionListener colorChangeListener) {
        this.colorChangeListener = colorChangeListener;
    }
    
    public void setColorEditor(CRAMColorEditor colorEditor) {
        this.colorEditor = colorEditor;
        colorEditor.setColorPane(this);
    }
  
    public void setColorPaneSelected(int index) {
        if (colorEditor == null) return;
        if (index == -1 || palette == null) {
            colorEditor.setColor(CRAMColor.BLACK, -1);
            ColorPane.clearSelection();
        } else {
            colorEditor.setColor(palette.getColors()[index], index);
        }
    }
  
    public void updateColor(int index, CRAMColor color) {
        if (palette == null || index < 0) return;
        if (ActionManager.isActionTriggering()) {
            actionUpdateColor(index, color);
        } else {
            ActionManager.setAndExecuteAction(new PaletteAction(this, index, color, colorPanes[index].getCurrentColor()));
        }
    }
    
    private void actionUpdateColor(int index, CRAMColor color) {
        palette.getColors()[index] = color;
        colorEditor.setColor(color, index);
        colorPanes[index].updateColor(color);
        colorPanes[index].setSelected();
        refreshColorPanes();
        if (colorChangeListener != null) {
            colorChangeListener.actionPerformed(new ActionEvent(palette, index, "ColorChange"));
        }
    }
    
    public void refreshColorPanes() {
        for (int i = 0; i < colorPanes.length; i++) {
            colorPanes[i].updateColor(palette.getColors()[i]);
        }
        int selected = colorEditor.getThisIndex();
        if (selected == -1) return;
        colorEditor.setColor(palette.getColors()[selected], selected);
    }

    public Palette getPalette() {
        return palette;
    }
    
   public void setPalette(Palette palette) {
        this.palette = palette;
        if (palette == null) {
            for (int i = 0; i < colorPanes.length; i++) {
                colorPanes[i].updateColor(CRAMColor.BLACK);
                colorPanes[i].setVisible(true);
            }
        } else {
            CRAMColor[] colors = palette.getColors();
            for (int i = 0; i < colorPanes.length; i++) {
                if (i < colors.length) {
                    colorPanes[i].updateColor(colors[i]);
                    colorPanes[i].setVisible(true);
                } else {
                    colorPanes[i].setVisible(false);
                }
            }
        }
        setColorPaneSelected(-1);
   }
   
   public Palette getUpdatedPalette() {
       if (palette == null) {
           Console.logger().warning("Palette not loaded.");
           return null;
       }
       CRAMColor[] colors = new CRAMColor[palette.getColors().length];
       for(int i=0; i < colorPanes.length; i++) {
           colors[i] = colorPanes[i].getCurrentColor();
       }
       return new Palette("New Palette", colors, true);
   }
}
