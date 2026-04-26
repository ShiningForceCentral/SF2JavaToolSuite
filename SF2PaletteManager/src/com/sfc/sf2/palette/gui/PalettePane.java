/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.BasicAction;
import com.sfc.sf2.core.actions.CustomAction;
import com.sfc.sf2.palette.gui.controls.CRAMColorEditor;
import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.actions.PaletteColorSwapActionData;
import com.sfc.sf2.palette.actions.PaletteColorAction;
import com.sfc.sf2.palette.gui.controls.PaletteButton.ColorsSwappedListener;
import com.sfc.sf2.palette.helpers.PaletteGraphicsHelpers;
import com.sfc.sf2.palette.helpers.PaletteHelpers;
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
    private byte[] pixelData;
    private ColorPane[] colorPanes;
    
    private ColorPane currentSelected;
    
    private ActionListener colorChangeListener;
    private ColorsSwappedListener colorsReorderedListener;
    
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

    public void setColorChangeListener(ActionListener colorChangeListener, ColorsSwappedListener colorsReorderedListener) {
        this.colorChangeListener = colorChangeListener;
        this.colorsReorderedListener = colorsReorderedListener;
    }
    
    public void setColorEditor(CRAMColorEditor colorEditor) {
        this.colorEditor = colorEditor;
        colorEditor.setColorPane(this);
    }

    public int getCurrentSelected() {
        return currentSelected == null ? -1 : currentSelected.getIndex();
    }
    
    public void clearSelection() {
        if (currentSelected == null) return;
        currentSelected.deselect();
        currentSelected = null;
    }
  
    public void setColorPaneSelected(int index) {
        if (colorEditor == null) return;
        if (ActionManager.isActionTriggering()) {
            actionColorPaneSelected(index);
        } else {
            ActionManager.setAndExecuteAction(new BasicAction<Integer>(this, "Index Selected", this::actionColorPaneSelected, index, currentSelected == null ? -1 : currentSelected.getIndex()));
        }
    }
    
    private void actionColorPaneSelected(int index) {
        clearSelection();
        if (index == -1 || palette == null) {
            colorEditor.setColor(CRAMColor.BLACK, -1);
        } else {
            colorPanes[index].select();
            currentSelected = colorPanes[index];
            colorEditor.setColor(palette.getColors()[index], index);
        }
    }
  
    public void updateColor(int index, CRAMColor color) {
        if (palette == null || index < 0) return;
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new PaletteColorAction(this, index, color, colorPanes[index].getCurrentColor()));
        }
        actionUpdateColor(index, color);
    }
    
    private void actionUpdateColor(int index, CRAMColor color) {
        palette.getColors()[index] = color;
        colorEditor.setColor(color, index);
        colorPanes[index].updateColor(color);
        colorPanes[index].select();
        refreshColorPanes();
        if (colorChangeListener != null) {
            colorChangeListener.actionPerformed(new ActionEvent(this, index, "ColorChange"));
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
    
    public void swapColors(int from, int to, boolean swapPixels) {
        if (palette == null || from < 0 || to < 0) return;
        
        Palette swappedPalette = PaletteHelpers.swapColors(palette, from, to);
        byte[] newPixelData = null;
        if (swapPixels && pixelData != null) {
            newPixelData = PaletteGraphicsHelpers.swapColorIndices(this.pixelData, (byte)from, (byte)to);
        }
        
        if (ActionManager.isActionTriggering()) {
            actionSwapColors(new PaletteColorSwapActionData(swappedPalette, newPixelData, from, to));
        } else {
            PaletteColorSwapActionData oldValue = new PaletteColorSwapActionData(palette, pixelData, to, from);
            PaletteColorSwapActionData newValue = new PaletteColorSwapActionData(swappedPalette, newPixelData, from, to);
            ActionManager.setAndExecuteAction(new CustomAction<PaletteColorSwapActionData>(this, "Color Index Swap", this::actionSwapColors, newValue, oldValue));
        }
    }

    private void actionSwapColors(PaletteColorSwapActionData value) {
        palette = value.palette();
        pixelData = value.pixelData();
        refreshColorPanes();
        setColorPaneSelected(value.toIndex());
        if (colorsReorderedListener != null) {
            colorsReorderedListener.colorsSwapped(value);
        }
        if (colorChangeListener != null) {
            colorChangeListener.actionPerformed(new ActionEvent(this, value.toIndex(), "PaletteIndexSwap"));
        }
    }
   
    public Palette getPalette() {
        return palette;
    }
    
    public void setPalette(Palette palette) {
        if (palette == null) {
            this.palette = null;
            for (int i = 0; i < colorPanes.length; i++) {
                colorPanes[i].updateColor(CRAMColor.BLACK);
                colorPanes[i].setVisible(true);
            }
        } else {
            CRAMColor[] colors = new CRAMColor[palette.getColors().length];
            for (int i = 0; i < colorPanes.length; i++) {
                if (i < colors.length) {
                    colors[i] = palette.getColors()[i];
                    colorPanes[i].updateColor(colors[i]);
                    colorPanes[i].setVisible(true);
                } else {
                    colorPanes[i].setVisible(false);
                }
            }
            this.palette = new Palette(palette.getName(), colors, palette.isFirstColorTransparent(), false);
            this.palette.setName(palette.getName());
        }
        setColorPaneSelected(-1);
    }
    
    public void setPalette(Palette palette, int[] limitColorIndices) {
        setPalette(palette);
        if (limitColorIndices == null) return;
        
        for (int i = 0; i < colorPanes.length; i++) {
            colorPanes[i].setVisible(false);
        }
        for (int i = 0; i < limitColorIndices.length; i++) {
            colorPanes[limitColorIndices[i]].setVisible(true);
        }
        setColorPaneSelected(-1);
    }

    public void setPixelData(byte[] pixelData) {
        this.pixelData = pixelData;
    }
}