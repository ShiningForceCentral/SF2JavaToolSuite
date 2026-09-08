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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author TiMMy
 */
public class PalettePane extends javax.swing.JPanel {

    private CRAMColorEditor colorEditor;
    private Palette palette;
    private byte[] pixelData;
    private ColorPane[] colorPanes;
    
    private ColorPane currentSelected;
    
    private ActionListener colorChangeListener;
    private ColorsSwappedListener colorsReorderedListener;
    
    public PalettePane() {
        initComponents();
        colorPanes = new ColorPane[] {
            colorPane1, colorPane2, colorPane3, colorPane4, colorPane5, colorPane6, colorPane7, colorPane8,
            colorPane9, colorPane10, colorPane11, colorPane12, colorPane13, colorPane14, colorPane15, colorPane16,
        };
        for (int i = 0; i < colorPanes.length; i++) {
            colorPanes[i].SetupColorPane(this, i, CRAMColor.BLACK);
        }
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
        infoButton1.setVisible(pixelData != null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorPane1 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane2 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane3 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane4 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane5 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane6 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane7 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane8 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane9 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane10 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane11 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane12 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane13 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane14 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane15 = new com.sfc.sf2.palette.gui.ColorPane();
        colorPane16 = new com.sfc.sf2.palette.gui.ColorPane();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 10), new java.awt.Dimension(10, 10), new java.awt.Dimension(10, 32767));
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();

        javax.swing.GroupLayout colorPane1Layout = new javax.swing.GroupLayout(colorPane1);
        colorPane1.setLayout(colorPane1Layout);
        colorPane1Layout.setHorizontalGroup(
            colorPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane1Layout.setVerticalGroup(
            colorPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane2Layout = new javax.swing.GroupLayout(colorPane2);
        colorPane2.setLayout(colorPane2Layout);
        colorPane2Layout.setHorizontalGroup(
            colorPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane2Layout.setVerticalGroup(
            colorPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane3Layout = new javax.swing.GroupLayout(colorPane3);
        colorPane3.setLayout(colorPane3Layout);
        colorPane3Layout.setHorizontalGroup(
            colorPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane3Layout.setVerticalGroup(
            colorPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane4Layout = new javax.swing.GroupLayout(colorPane4);
        colorPane4.setLayout(colorPane4Layout);
        colorPane4Layout.setHorizontalGroup(
            colorPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane4Layout.setVerticalGroup(
            colorPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane5Layout = new javax.swing.GroupLayout(colorPane5);
        colorPane5.setLayout(colorPane5Layout);
        colorPane5Layout.setHorizontalGroup(
            colorPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane5Layout.setVerticalGroup(
            colorPane5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane6Layout = new javax.swing.GroupLayout(colorPane6);
        colorPane6.setLayout(colorPane6Layout);
        colorPane6Layout.setHorizontalGroup(
            colorPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane6Layout.setVerticalGroup(
            colorPane6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane7Layout = new javax.swing.GroupLayout(colorPane7);
        colorPane7.setLayout(colorPane7Layout);
        colorPane7Layout.setHorizontalGroup(
            colorPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane7Layout.setVerticalGroup(
            colorPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane8Layout = new javax.swing.GroupLayout(colorPane8);
        colorPane8.setLayout(colorPane8Layout);
        colorPane8Layout.setHorizontalGroup(
            colorPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane8Layout.setVerticalGroup(
            colorPane8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane9Layout = new javax.swing.GroupLayout(colorPane9);
        colorPane9.setLayout(colorPane9Layout);
        colorPane9Layout.setHorizontalGroup(
            colorPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane9Layout.setVerticalGroup(
            colorPane9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane10Layout = new javax.swing.GroupLayout(colorPane10);
        colorPane10.setLayout(colorPane10Layout);
        colorPane10Layout.setHorizontalGroup(
            colorPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane10Layout.setVerticalGroup(
            colorPane10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane11Layout = new javax.swing.GroupLayout(colorPane11);
        colorPane11.setLayout(colorPane11Layout);
        colorPane11Layout.setHorizontalGroup(
            colorPane11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane11Layout.setVerticalGroup(
            colorPane11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane12Layout = new javax.swing.GroupLayout(colorPane12);
        colorPane12.setLayout(colorPane12Layout);
        colorPane12Layout.setHorizontalGroup(
            colorPane12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane12Layout.setVerticalGroup(
            colorPane12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane13Layout = new javax.swing.GroupLayout(colorPane13);
        colorPane13.setLayout(colorPane13Layout);
        colorPane13Layout.setHorizontalGroup(
            colorPane13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane13Layout.setVerticalGroup(
            colorPane13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane14Layout = new javax.swing.GroupLayout(colorPane14);
        colorPane14.setLayout(colorPane14Layout);
        colorPane14Layout.setHorizontalGroup(
            colorPane14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane14Layout.setVerticalGroup(
            colorPane14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane15Layout = new javax.swing.GroupLayout(colorPane15);
        colorPane15.setLayout(colorPane15Layout);
        colorPane15Layout.setHorizontalGroup(
            colorPane15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane15Layout.setVerticalGroup(
            colorPane15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout colorPane16Layout = new javax.swing.GroupLayout(colorPane16);
        colorPane16.setLayout(colorPane16Layout);
        colorPane16Layout.setHorizontalGroup(
            colorPane16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        colorPane16Layout.setVerticalGroup(
            colorPane16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        infoButton1.setMessageText("<html>Select the palette color to edit it.<br><br><b>Advanced Controls:</b><br>- <i>Left-Drag:</i> Rearrange ONLY the palette color indices. This will cause the image colors to visually change (since the image pixels are assigned a color index).<br>- <i>Right-Drag:</i> Rearrange the palette color indices AND image pixels. This ensures that the image does not visually change, but its underlying pixel data will be modified to match the color index changes.</html>");
        infoButton1.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(colorPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(colorPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(colorPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.palette.gui.ColorPane colorPane1;
    private com.sfc.sf2.palette.gui.ColorPane colorPane10;
    private com.sfc.sf2.palette.gui.ColorPane colorPane11;
    private com.sfc.sf2.palette.gui.ColorPane colorPane12;
    private com.sfc.sf2.palette.gui.ColorPane colorPane13;
    private com.sfc.sf2.palette.gui.ColorPane colorPane14;
    private com.sfc.sf2.palette.gui.ColorPane colorPane15;
    private com.sfc.sf2.palette.gui.ColorPane colorPane16;
    private com.sfc.sf2.palette.gui.ColorPane colorPane2;
    private com.sfc.sf2.palette.gui.ColorPane colorPane3;
    private com.sfc.sf2.palette.gui.ColorPane colorPane4;
    private com.sfc.sf2.palette.gui.ColorPane colorPane5;
    private com.sfc.sf2.palette.gui.ColorPane colorPane6;
    private com.sfc.sf2.palette.gui.ColorPane colorPane7;
    private com.sfc.sf2.palette.gui.ColorPane colorPane8;
    private com.sfc.sf2.palette.gui.ColorPane colorPane9;
    private javax.swing.Box.Filler filler1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    // End of variables declaration//GEN-END:variables
}
