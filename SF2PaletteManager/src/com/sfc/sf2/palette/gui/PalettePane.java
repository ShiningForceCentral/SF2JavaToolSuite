/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.palette.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.BasicAction;
import com.sfc.sf2.core.actions.CustomAction;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.palette.CRAMColor;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.palette.actions.PaletteColorAction;
import com.sfc.sf2.palette.actions.PaletteColorSwapActionData;
import com.sfc.sf2.palette.gui.controls.CRAMColorEditor;
import com.sfc.sf2.palette.gui.controls.PaletteButton;
import com.sfc.sf2.palette.gui.controls.PaletteButton.ColorsSwappedListener;
import com.sfc.sf2.palette.helpers.PaletteGraphicsHelpers;
import com.sfc.sf2.palette.helpers.PaletteHelpers;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class PalettePane extends javax.swing.JPanel {

    private CRAMColorEditor colorEditor;
    private Palette palette;
    private byte[] pixelData;
    private ArrayList<ColorPane> colorPanes;
    
    private ColorPane currentSelected;
    
    private ActionListener colorChangeListener;
    private ColorsSwappedListener colorsReorderedListener;
    
    public PalettePane() {
        initComponents();
        colorPane.SetupColorPane(this, 0, CRAMColor.BLACK);
        if (!SettingsManager.isRunningInEditor())
            infoButton1.setVisible(false);
    }

    public void setColorChangeListener(ActionListener colorChangeListener, PaletteButton.ColorsSwappedListener colorsReorderedListener) {
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
            colorPanes.get(index).select();
            currentSelected = colorPanes.get(index);
            colorEditor.setColor(palette.getColors()[index], index);
        }
    }
  
    public void updateColor(int index, CRAMColor color) {
        if (palette == null || index < 0) return;
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new PaletteColorAction(this, index, color, colorPanes.get(index).getCurrentColor()));
        }
        actionUpdateColor(index, color);
    }
    
    private void actionUpdateColor(int index, CRAMColor color) {
        palette.getColors()[index] = color;
        colorEditor.setColor(color, index);
        colorPanes.get(index).updateColor(color);
        colorPanes.get(index).select();
        refreshColorPanes();
        if (colorChangeListener != null) {
            colorChangeListener.actionPerformed(new ActionEvent(this, index, "ColorChange"));
        }
    }
    
    public void refreshColorPanes() {
        for (int i = 0; i < colorPanes.size(); i++) {
            colorPanes.get(i).updateColor(palette.getColors()[i]);
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
        setUpColorPanes(palette);
        if (palette == null) {
            this.palette = null;
            for (int i = 0; i < colorPanes.size(); i++) {
                colorPanes.get(i).updateColor(CRAMColor.BLACK);
                colorPanes.get(i).setVisible(i < 16);
            }
        } else {
            CRAMColor[] colors = new CRAMColor[palette.getColors().length];
            for (int i = 0; i < colorPanes.size(); i++) {
                if (i < colors.length) {
                    colors[i] = palette.getColors()[i];
                    colorPanes.get(i).updateColor(colors[i]);
                    colorPanes.get(i).setVisible(true);
                } else {
                    colorPanes.get(i).setVisible(false);
                }
            }
            this.palette = new Palette(palette.getName(), colors, palette.isFirstColorTransparent(), false);
            this.palette.setName(palette.getName());
        }
        setColorPaneSelected(-1);
    }
    
    public void setPalette(Palette palette, int[] limitColorIndices) {
        setUpColorPanes(palette);
        setPalette(palette);
        if (limitColorIndices == null) return;
        
        for (int i = 0; i < colorPanes.size(); i++) {
            colorPanes.get(i).setVisible(false);
        }
        for (int i = 0; i < limitColorIndices.length; i++) {
            colorPanes.get(limitColorIndices[i]).setVisible(true);
        }
        setColorPaneSelected(-1);
    }
    
    private void setUpColorPanes(Palette palette) {
        int count = palette == null ? 16 : palette.getColorsCount();
        if (colorPanes == null)
            colorPanes = new ArrayList(count);
        if (colorPanes.size() < count) {
            for (int i = colorPanes.size(); i < count; i++) {
                if (i == 0) {
                    colorPanes.add(colorPane);
                } else {
                    ColorPane newPane = new ColorPane();
                    colorPane.getParent().add(newPane);
                    newPane.SetupColorPane(this, i, CRAMColor.BLACK);
                    colorPanes.add(newPane);
                }
            }
        }
        this.revalidate();
    }

    @Override
    public Dimension getMinimumSize() {
        int x = 500;
        int columns = this.getWidth()/30;
        int rows;
        if (colorPanes == null || columns == 0) {
            rows = 1;
        } else {
            rows = colorPanes.size()/columns;
            if (colorPanes.size()%columns != 0)
                rows++;
        }
        int y = 10+40*rows;
        return new Dimension(x, y);
    }

    public void setPixelData(byte[] pixelData) {
        this.pixelData = pixelData;
        infoButton1.setVisible(pixelData != null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelColors = new javax.swing.JPanel();
        colorPane = new com.sfc.sf2.palette.gui.ColorPane();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 10), new java.awt.Dimension(10, 10), new java.awt.Dimension(10, 32767));
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 10), new java.awt.Dimension(10, 10), new java.awt.Dimension(10, 32767));

        setMinimumSize(new java.awt.Dimension(420, 40));

        jPanelColors.setMinimumSize(new java.awt.Dimension(200, 40));
        jPanelColors.setPreferredSize(new java.awt.Dimension(420, 0));
        jPanelColors.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 5));

        javax.swing.GroupLayout colorPaneLayout = new javax.swing.GroupLayout(colorPane);
        colorPane.setLayout(colorPaneLayout);
        colorPaneLayout.setHorizontalGroup(
            colorPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPaneLayout.setVerticalGroup(
            colorPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanelColors.add(colorPane);

        infoButton1.setMessageText("<html>Select the palette color to edit it.<br><br><b>Advanced Controls:</b><br>- <i>Left-Drag:</i> Rearrange ONLY the palette color indices. This will cause the image colors to visually change (since the image pixels are assigned a color index).<br>- <i>Right-Drag:</i> Rearrange the palette color indices AND image pixels. This ensures that the image does not visually change, but its underlying pixel data will be modified to match the color index changes.</html>");
        infoButton1.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelColors, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelColors, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(filler2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.palette.gui.ColorPane colorPane;
    private com.sfc.sf2.palette.gui.ColorPane colorPane22;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private javax.swing.JPanel jPanelColors;
    // End of variables declaration//GEN-END:variables
}
