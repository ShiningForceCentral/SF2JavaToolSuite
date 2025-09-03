/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.block.gui;

import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockManager;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.settings.MapBlockSettings;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.JScrollPane;

/**
 *
 * @author wiz
 */
public class MapBlockMainEditor extends AbstractMainEditor {
    
    MapBlockSettings MapBlockSettings = new MapBlockSettings();
    MapBlockManager mapblockManager = new MapBlockManager();
    
    public MapBlockMainEditor() {
        super();
        SettingsManager.registerSettingsStore("mapBlock", MapBlockSettings);
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        accordionPanel1.setExpanded(false);
        
        colorPickerBlockset.setColor(MapBlockSettings.getBlocksetBGColor());
        jSpinner1.setValue(MapBlockSettings.getBlocksetBlocksPerRow());
        jComboBox1.setSelectedIndex(MapBlockSettings.getBlocksetScale()-1);
        colorPickerTileset.setColor(MapBlockSettings.getTilesetBGColor());
        jSpinner4.setValue(MapBlockSettings.getTilesetTilesPerRow());
        jComboBox4.setSelectedIndex(MapBlockSettings.getTilesetScale()-1);
        colorPickerBlocks.setColor(MapBlockSettings.getBlockBGColor());
        
        editableBlockSlotPanel.setMapBlocksetLayout(mapBlocksetLayoutPanel);
        editableBlockSlotPanel.setLeftTileSlotPanel(tileSlotPanelLeft);
        editableBlockSlotPanel.setRightTileSlotPanel(tileSlotPanelRight);
        mapBlocksetLayoutPanel.setLeftSlotBlockPanel(editableBlockSlotPanel);
        tilesetsLayoutPanel.setLeftSlotTilePanel(tileSlotPanelLeft);
        tilesetsLayoutPanel.setRightSlotBlockPanel(tileSlotPanelRight);
        tilesetsLayoutPanel.setBlockSlotPanel(editableBlockSlotPanel);
    }
    
    @Override
    protected void onDataLoaded() {
        Tileset[] tilesets = mapblockManager.getTilesets();
        MapBlockset mapBlockset = mapblockManager.getMapBlockset();
        if (mapBlockset != null) {
            mapBlocksetLayoutPanel.setBlockset(mapBlockset);            
            mapBlocksetLayoutPanel.setBlocksPerRow(((int)jSpinner1.getModel().getValue()));
            mapBlocksetLayoutPanel.setBGColor(colorPickerBlockset.getColor());
            mapBlocksetLayoutPanel.setShowGrid(jCheckBox1.isSelected());
            mapBlocksetLayoutPanel.setDisplayScale(jComboBox1.getSelectedIndex()+1);
            mapBlocksetLayoutPanel.setShowPriority(jCheckBox4.isSelected());
            
            editableBlockSlotPanel.setBGColor(colorPickerBlocks.getColor());
            editableBlockSlotPanel.setShowGrid(jCheckBox5.isSelected());
            editableBlockSlotPanel.setShowPriority(jCheckBox3.isSelected());
        }
        if (tilesets != null) {
            tilesetsLayoutPanel.setTilesets(tilesets);
            String[] tilesetNames = new String[tilesets.length];
            for (int i = 0; i < tilesets.length; i++) {
                if (tilesets[i] == null) {
                    tilesetNames[i] = "NONE";
                } else {
                    tilesetNames[i] = tilesets[i].getName();
                }
            }
            jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(tilesetNames));
            jComboBox5.setSelectedIndex(0);
            
            tilesetsLayoutPanel.setItemsPerRow(((int)jSpinner4.getModel().getValue()));
            tilesetsLayoutPanel.setBGColor(colorPickerTileset.getColor());
            tilesetsLayoutPanel.setShowGrid(jCheckBox2.isSelected());
            tilesetsLayoutPanel.setDisplayScale(jComboBox4.getSelectedIndex()+1);
        }
        
        super.onDataLoaded();
    }
    
    @Override
    protected void repaintEditorLayout() {
        super.repaintEditorLayout();
        
        repaintMapBlockLayout();
        repaintTilesetLayout();
    }
    
    protected void repaintMapBlockLayout() {
        mapBlocksetLayoutPanel.revalidate();
        mapBlocksetLayoutPanel.repaint();
    }
    
    protected void repaintTilesetLayout() {
        tilesetsLayoutPanel.revalidate();
        tilesetsLayoutPanel.repaint();
    }
    
    protected void repaintSelectedBlockPanel() {
        editableBlockSlotPanel.revalidate();
        editableBlockSlotPanel.repaint();
        jCheckBox3.setEnabled(editableBlockSlotPanel.getCurrentMode() != EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_PRIORITY);
    }
    
    protected void repaintTilesPanels() {
        tileSlotPanelLeft.revalidate();
        tileSlotPanelLeft.repaint();
        tileSlotPanelRight.revalidate();
        tileSlotPanelRight.repaint();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel13 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel30 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton28 = new javax.swing.JButton();
        directoryButton1 = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton4 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        fileButton5 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton6 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton7 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton8 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton9 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton10 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton11 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel17 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        directoryButton2 = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jPanel28 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        fileButton13 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton14 = new com.sfc.sf2.core.gui.controls.FileButton();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton2 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mapBlocksetLayoutPanel = new com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        colorPickerBlockset = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jPanel7 = new javax.swing.JPanel();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tilesetsLayoutPanel = new com.sfc.sf2.map.block.gui.TilesetsLayoutPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel21 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        colorPickerTileset = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel10 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        colorPickerBlocks = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel11 = new javax.swing.JLabel();
        editableBlockSlotPanel = new com.sfc.sf2.map.block.gui.EditableBlockSlotPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        tileSlotPanelLeft = new com.sfc.sf2.map.block.gui.TileSlotPanel();
        tileSlotPanelRight = new com.sfc.sf2.map.block.gui.TileSlotPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        infoButton3 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton4 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton5 = new com.sfc.sf2.core.gui.controls.InfoButton();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2MapBlockManager");

        jSplitPane1.setDividerLocation(600);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(700);
        jSplitPane2.setResizeWeight(1.0);
        jSplitPane2.setOneTouchExpandable(true);

        jSplitPane3.setDividerLocation(350);
        jSplitPane3.setResizeWeight(1.0);

        jPanel31.setMinimumSize(new java.awt.Dimension(300, 300));

        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from :"));

        jLabel6.setText("Select disassembly files.");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jButton28.setText("Import");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        directoryButton1.setDirectoryPath(".\\entries\\map03\\");
            directoryButton1.setLabelText("Map dir :");

            javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
            jPanel21.setLayout(jPanel21Layout);
            jPanel21Layout.setHorizontalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(directoryButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(directoryButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel6)
                        .addComponent(jButton28))
                    .addContainerGap())
            );

            accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette, tilesets, & map data"));

            fileButton1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
            fileButton1.setLabelText("Palette entries :");

            fileButton2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
            fileButton2.setLabelText("Tilesets entries :");

            fileButton3.setFilePath("00-tilesets.asm");
            fileButton3.setLabelText("Map tilesets :");

            fileButton4.setFilePath("0-blocks.bin");
            fileButton4.setLabelText("Map blockset :");
            fileButton4.setToolTipText("");

            javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
            accordionPanel1.setLayout(accordionPanel1Layout);
            accordionPanel1Layout.setHorizontalGroup(
                accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fileButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );
            accordionPanel1Layout.setVerticalGroup(
                accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(fileButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
            jPanel14.setLayout(jPanel14Layout);
            jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(accordionPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(accordionPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Map", jPanel14);

            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from :"));
            jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

            jLabel2.setText("Select individual disassembly files.");
            jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jButton18.setText("Import");
            jButton18.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton18ActionPerformed(evt);
                }
            });

            fileButton5.setFilePath("..\\graphics\\maps\\mappalettes\\mappalette00.bin");
            fileButton5.setLabelText("Palette :");

            fileButton6.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset000.bin");
            fileButton6.setLabelText("Tileset 1 :");

            fileButton7.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset037.bin");
            fileButton7.setLabelText("Tileset 2 :");

            fileButton8.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset043.bin");
            fileButton8.setLabelText("Tileset 3 :");

            fileButton9.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset053.bin");
            fileButton9.setLabelText("Tileset 4 :");

            fileButton10.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset066.bin");
            fileButton10.setLabelText("Tileset 5 :");

            fileButton11.setFilePath(".\\entries\\map03\\0-blocks.bin");
            fileButton11.setLabelText("Blocks file :");

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton18))
                        .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jButton18))
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
            jPanel11.setLayout(jPanel11Layout);
            jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("Data", jPanel11);

            javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
            jPanel32.setLayout(jPanel32Layout);
            jPanel32Layout.setHorizontalGroup(
                jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            );
            jPanel32Layout.setVerticalGroup(
                jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1)
            );

            jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Export :"));
            jPanel17.setPreferredSize(new java.awt.Dimension(32, 135));

            jLabel7.setText("<html>Select new target file.</html>");
            jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jButton4.setText("Export");
            jButton4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton4ActionPerformed(evt);
                }
            });

            directoryButton2.setDirectoryPath(".\\entries\\map03\\");
                directoryButton2.setLabelText("Map dir :");

                javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
                jPanel26.setLayout(jPanel26Layout);
                jPanel26Layout.setHorizontalGroup(
                    jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(directoryButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)))
                        .addContainerGap())
                );
                jPanel26Layout.setVerticalGroup(
                    jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(directoryButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4))
                        .addContainerGap())
                );

                jTabbedPane2.addTab("Map", jPanel26);

                jLabel8.setText("<html>blockset data to a new image file and 'priority' flag data to text file.<br>Recommended to save as PNG or GIF.<br>Exported color format : 4BPP / 16 indexed colors.<br>Transparent color at index 0.</html>");
                jLabel8.setToolTipText("");
                jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                jButton5.setText("Export");
                jButton5.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton5ActionPerformed(evt);
                    }
                });

                fileButton13.setFilePath(".\\blockset.png");
                fileButton13.setLabelText("Image file :");

                fileButton14.setFilePath(".\\blocksethptiles.txt");
                fileButton14.setLabelText("Block HP tiles :");

                infoButton1.setText("");

                infoButton2.setText("");

                javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
                jPanel28.setLayout(jPanel28Layout);
                jPanel28Layout.setHorizontalGroup(
                    jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(infoButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(infoButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                );
                jPanel28Layout.setVerticalGroup(
                    jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                jTabbedPane2.addTab("PNG", jPanel28);

                javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
                jPanel17.setLayout(jPanel17Layout);
                jPanel17Layout.setHorizontalGroup(
                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                );
                jPanel17Layout.setVerticalGroup(
                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 194, Short.MAX_VALUE)
                );

                jTabbedPane2.getAccessibleContext().setAccessibleName("tab");

                javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
                jPanel31.setLayout(jPanel31Layout);
                jPanel31Layout.setHorizontalGroup(
                    jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel31Layout.setVerticalGroup(
                    jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 28, Short.MAX_VALUE)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                jSplitPane3.setLeftComponent(jPanel31);

                jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Blocks"));
                jPanel1.setPreferredSize(new java.awt.Dimension(10, 300));

                jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 300));

                javax.swing.GroupLayout mapBlocksetLayoutPanelLayout = new javax.swing.GroupLayout(mapBlocksetLayoutPanel);
                mapBlocksetLayoutPanel.setLayout(mapBlocksetLayoutPanelLayout);
                mapBlocksetLayoutPanelLayout.setHorizontalGroup(
                    mapBlocksetLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 435, Short.MAX_VALUE)
                );
                mapBlocksetLayoutPanelLayout.setVerticalGroup(
                    mapBlocksetLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 427, Short.MAX_VALUE)
                );

                jScrollPane3.setViewportView(mapBlocksetLayoutPanel);

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                );
                jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                );

                jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Blocks display"));

                jLabel4.setText("Scale :");

                jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
                jComboBox1.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox1ItemStateChanged(evt);
                    }
                });

                jSpinner1.setModel(new javax.swing.SpinnerNumberModel(8, 4, 64, 2));
                jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jSpinner1StateChanged(evt);
                    }
                });

                jLabel5.setText("Blocks per row :");

                jCheckBox1.setSelected(true);
                jCheckBox1.setText("Show grid");
                jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox1ItemStateChanged(evt);
                    }
                });

                jCheckBox4.setText("Show priority");
                jCheckBox4.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox4ItemStateChanged(evt);
                    }
                });

                jLabel9.setText("BG :");

                colorPickerBlockset.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
                    public void colorChanged(java.awt.event.ActionEvent evt) {
                        colorPickerBlocksetColorChanged(evt);
                    }
                });

                javax.swing.GroupLayout colorPickerBlocksetLayout = new javax.swing.GroupLayout(colorPickerBlockset);
                colorPickerBlockset.setLayout(colorPickerBlocksetLayout);
                colorPickerBlocksetLayout.setHorizontalGroup(
                    colorPickerBlocksetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );
                colorPickerBlocksetLayout.setVerticalGroup(
                    colorPickerBlocksetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                jPanel12.setLayout(jPanel12Layout);
                jPanel12Layout.setHorizontalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBox1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorPickerBlockset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBox4)))
                        .addContainerGap())
                );
                jPanel12Layout.setVerticalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel5)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox4)
                            .addComponent(jLabel9)
                            .addComponent(colorPickerBlockset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                jButton35.setText("Add block");
                jButton35.setMargin(new java.awt.Insets(2, 5, 3, 5));
                jButton35.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton35ActionPerformed(evt);
                    }
                });

                jButton36.setText("Remove block");
                jButton36.setToolTipText("");
                jButton36.setMargin(new java.awt.Insets(2, 5, 3, 5));
                jButton36.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton36ActionPerformed(evt);
                    }
                });

                jButton37.setText("Clone selected");
                jButton37.setMargin(new java.awt.Insets(2, 5, 3, 5));
                jButton37.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton37ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton36)
                        .addContainerGap())
                );
                jPanel7Layout.setVerticalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton35)
                            .addComponent(jButton36)
                            .addComponent(jButton37)))
                );

                javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                jPanel10.setLayout(jPanel10Layout);
                jPanel10Layout.setHorizontalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0))
                );
                jPanel10Layout.setVerticalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                jSplitPane3.setRightComponent(jPanel10);

                javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
                jPanel30.setLayout(jPanel30Layout);
                jPanel30Layout.setHorizontalGroup(
                    jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 700, Short.MAX_VALUE)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane3))
                );
                jPanel30Layout.setVerticalGroup(
                    jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 594, Short.MAX_VALUE)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane3))
                );

                jSplitPane2.setLeftComponent(jPanel30);

                jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
                jPanel9.setMinimumSize(new java.awt.Dimension(340, 200));
                jPanel9.setName(""); // NOI18N
                jPanel9.setPreferredSize(new java.awt.Dimension(340, 300));

                jScrollPane4.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Tileset"));

                javax.swing.GroupLayout tilesetsLayoutPanelLayout = new javax.swing.GroupLayout(tilesetsLayoutPanel);
                tilesetsLayoutPanel.setLayout(tilesetsLayoutPanelLayout);
                tilesetsLayoutPanelLayout.setHorizontalGroup(
                    tilesetsLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 558, Short.MAX_VALUE)
                );
                tilesetsLayoutPanelLayout.setVerticalGroup(
                    tilesetsLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 223, Short.MAX_VALUE)
                );

                jScrollPane4.setViewportView(tilesetsLayoutPanel);

                jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Tileset display"));
                jPanel20.setMinimumSize(new java.awt.Dimension(340, 100));
                jPanel20.setPreferredSize(new java.awt.Dimension(340, 100));

                jLabel15.setText("Scale :");

                jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
                jComboBox4.setSelectedIndex(1);
                jComboBox4.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox4ItemStateChanged(evt);
                    }
                });

                jSpinner4.setModel(new javax.swing.SpinnerNumberModel(16, 4, 32, 4));
                jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jSpinner4StateChanged(evt);
                    }
                });

                jLabel21.setText("Tiles per row :");

                jCheckBox2.setSelected(true);
                jCheckBox2.setText("Show grid");
                jCheckBox2.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox2ItemStateChanged(evt);
                    }
                });

                colorPickerTileset.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
                    public void colorChanged(java.awt.event.ActionEvent evt) {
                        colorPickerTilesetColorChanged(evt);
                    }
                });

                javax.swing.GroupLayout colorPickerTilesetLayout = new javax.swing.GroupLayout(colorPickerTileset);
                colorPickerTileset.setLayout(colorPickerTilesetLayout);
                colorPickerTilesetLayout.setHorizontalGroup(
                    colorPickerTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );
                colorPickerTilesetLayout.setVerticalGroup(
                    colorPickerTilesetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );

                jLabel10.setText("BG :");

                jLabel29.setText("Tileset : ");

                jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox5.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox5ItemStateChanged(evt);
                    }
                });

                javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
                jPanel20.setLayout(jPanel20Layout);
                jPanel20Layout.setHorizontalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel20Layout.setVerticalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel21)
                            .addComponent(jLabel29)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jCheckBox2)
                            .addComponent(jLabel10)
                            .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                );
                jPanel9Layout.setVerticalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel3.setText("Selected block");

                jCheckBox5.setSelected(true);
                jCheckBox5.setText("Show grid");
                jCheckBox5.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox5ItemStateChanged(evt);
                    }
                });

                jCheckBox3.setText("Show priority");
                jCheckBox3.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox3ItemStateChanged(evt);
                    }
                });

                colorPickerBlocks.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
                    public void colorChanged(java.awt.event.ActionEvent evt) {
                        colorPickerBlocksColorChanged(evt);
                    }
                });

                javax.swing.GroupLayout colorPickerBlocksLayout = new javax.swing.GroupLayout(colorPickerBlocks);
                colorPickerBlocks.setLayout(colorPickerBlocksLayout);
                colorPickerBlocksLayout.setHorizontalGroup(
                    colorPickerBlocksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );
                colorPickerBlocksLayout.setVerticalGroup(
                    colorPickerBlocksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );

                jLabel11.setText("BG :");

                editableBlockSlotPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                editableBlockSlotPanel.setMaximumSize(new java.awt.Dimension(96, 96));
                editableBlockSlotPanel.setMinimumSize(new java.awt.Dimension(96, 96));
                editableBlockSlotPanel.setPreferredSize(new java.awt.Dimension(96, 96));

                javax.swing.GroupLayout editableBlockSlotPanelLayout = new javax.swing.GroupLayout(editableBlockSlotPanel);
                editableBlockSlotPanel.setLayout(editableBlockSlotPanelLayout);
                editableBlockSlotPanelLayout.setHorizontalGroup(
                    editableBlockSlotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 94, Short.MAX_VALUE)
                );
                editableBlockSlotPanelLayout.setVerticalGroup(
                    editableBlockSlotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 94, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
                jPanel22.setLayout(jPanel22Layout);
                jPanel22Layout.setHorizontalGroup(
                    jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox3)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(editableBlockSlotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                .addComponent(jCheckBox5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorPickerBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel22Layout.setVerticalGroup(
                    jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editableBlockSlotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox5)
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel11)
                                .addComponent(colorPickerBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                jLabel22.setText("Left click");

                jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel30.setText("Right click");

                tileSlotPanelLeft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                tileSlotPanelLeft.setMaximumSize(new java.awt.Dimension(48, 48));
                tileSlotPanelLeft.setMinimumSize(new java.awt.Dimension(48, 48));
                tileSlotPanelLeft.setPreferredSize(new java.awt.Dimension(48, 48));

                javax.swing.GroupLayout tileSlotPanelLeftLayout = new javax.swing.GroupLayout(tileSlotPanelLeft);
                tileSlotPanelLeft.setLayout(tileSlotPanelLeftLayout);
                tileSlotPanelLeftLayout.setHorizontalGroup(
                    tileSlotPanelLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );
                tileSlotPanelLeftLayout.setVerticalGroup(
                    tileSlotPanelLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );

                tileSlotPanelRight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                tileSlotPanelRight.setMaximumSize(new java.awt.Dimension(48, 48));
                tileSlotPanelRight.setMinimumSize(new java.awt.Dimension(48, 48));
                tileSlotPanelRight.setPreferredSize(new java.awt.Dimension(48, 48));

                javax.swing.GroupLayout tileSlotPanelRightLayout = new javax.swing.GroupLayout(tileSlotPanelRight);
                tileSlotPanelRight.setLayout(tileSlotPanelRightLayout);
                tileSlotPanelRightLayout.setHorizontalGroup(
                    tileSlotPanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );
                tileSlotPanelRightLayout.setVerticalGroup(
                    tileSlotPanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
                jPanel23.setLayout(jPanel23Layout);
                jPanel23Layout.setHorizontalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(tileSlotPanelLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel23Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(tileSlotPanelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel30))
                        .addGap(20, 20, 20))
                );
                jPanel23Layout.setVerticalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tileSlotPanelLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tileSlotPanelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                buttonGroup1.add(jRadioButton1);
                jRadioButton1.setSelected(true);
                jRadioButton1.setText("Apply tile");
                jRadioButton1.setActionCommand("Apply tiles");
                jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton1ActionPerformed(evt);
                    }
                });

                buttonGroup1.add(jRadioButton2);
                jRadioButton2.setText("Toggle priority flag");
                jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton2ActionPerformed(evt);
                    }
                });

                buttonGroup1.add(jRadioButton3);
                jRadioButton3.setText("Flip tiles");
                jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton3ActionPerformed(evt);
                    }
                });

                infoButton3.setMessageText("<html>Flip each tile in the selected block. Left click to toggle horizontal flip. Right click to toggle vertical flip. Middle click to clear any flipping.</html>");
                infoButton3.setText("");

                infoButton4.setMessageText("<html>Set the priority flag for each tile. 'Priority' means that the tile is drawn above the mapSprites (i.e. above characters).<br>Examples include roof tiles or the top tiles for a wall or table.</html>");
                infoButton4.setText("");

                infoButton5.setMessageText("<html> 'Paint' the selected tiles (left or right click) to the selected block.<br>Use left or right click to select a tile above then left or right click on the <i>Selected block</i> panel to apply</html>");
                infoButton5.setText("");

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton3)
                            .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton2))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                jPanel6.setLayout(jPanel6Layout);
                jPanel6Layout.setHorizontalGroup(
                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(98, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel6Layout.setVerticalGroup(
                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0))
                );

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel4Layout.setVerticalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );

                jSplitPane2.setRightComponent(jPanel4);

                javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
                jPanel15.setLayout(jPanel15Layout);
                jPanel15Layout.setHorizontalGroup(
                    jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2)
                );
                jPanel15Layout.setVerticalGroup(
                    jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jSplitPane2)
                        .addContainerGap())
                );

                jSplitPane1.setTopComponent(jPanel15);
                jSplitPane1.setBottomComponent(console1);

                javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
                jPanel13.setLayout(jPanel13Layout);
                jPanel13Layout.setHorizontalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1)
                );
                jPanel13Layout.setVerticalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );

                setSize(new java.awt.Dimension(1177, 751));
                setLocationRelativeTo(null);
            }// </editor-fold>//GEN-END:initComponents

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        Path palettePath = PathHelpers.getBasePath().resolve(fileButton5.getFilePath());
        Path blocksPath = PathHelpers.getBasePath().resolve(fileButton11.getFilePath());
        Path tilesetPath1 = PathHelpers.getBasePath().resolve(fileButton6.getFilePath());
        Path tilesetPath2 = PathHelpers.getBasePath().resolve(fileButton7.getFilePath());
        Path tilesetPath3 = PathHelpers.getBasePath().resolve(fileButton8.getFilePath());
        Path tilesetPath4 = PathHelpers.getBasePath().resolve(fileButton9.getFilePath());
        Path tilesetPath5 = PathHelpers.getBasePath().resolve(fileButton10.getFilePath());
        try {
            mapblockManager.importDisassembly(palettePath, new Path[] { tilesetPath1, tilesetPath2, tilesetPath3, tilesetPath4, tilesetPath5 }, blocksPath);
        } catch (Exception ex) {
            mapblockManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map blockset disasm could not be imported from : " + blocksPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButton2.getDirectoryPath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButton3.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButton4.getFilePath());
        if (!PathHelpers.createPathIfRequred(mapDirectory)) return;
        try {
            mapblockManager.exportDisassembly(mapTilesetDataPath, mapBlocksetDataPath, mapBlocksetLayoutPanel.getBlockset(), tilesetsLayoutPanel.getTilesets());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map block disasm could not be exported to : " + mapDirectory);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButton1.getDirectoryPath());
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButton3.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButton4.getFilePath());
        try {
            mapblockManager.importDisassembly(paletteEntriesPath, tilesetEntriesPath, mapTilesetDataPath, mapBlocksetDataPath);
        } catch (Exception ex) {
            mapblockManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map blockset disasm could not be imported from : " + mapDirectory);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton28ActionPerformed
    
    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        MapBlock block = null;
        int index = editableBlockSlotPanel.getBlockIndex();
        if (index >= 0 && index <= 2) {
            Console.logger().warning("WARNING Cannot insert map blocks before first 3 slots.");
            return;
        }
        if (index < 0 || index >= blockset.getBlocks().length-1) {
            index = blockset.getBlocks().length;
            block = blockset.getBlocks()[3];
        } else {
            index++;
            block = blockset.getBlocks()[0];
        }
        blockset.insertBlock(index, block);
        mapBlocksetLayoutPanel.setLeftSelectedIndex(index);
        if (index == blockset.getBlocks().length-1) { //Scroll to bottom
            ((JScrollPane)mapBlocksetLayoutPanel.getParent().getParent()).getVerticalScrollBar().setValue(Integer.MAX_VALUE);
        }
        repaintEditorLayout();
    }//GEN-LAST:event_jButton35ActionPerformed
    
    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        int index = editableBlockSlotPanel.getBlockIndex();
        if (index < 0) return;
        if (index <= 2) {
            Console.logger().warning("WARNING Cannot delete first 3 map blocks.");
            return;
        }
        blockset.removeBlock(index);
        mapBlocksetLayoutPanel.setLeftSelectedIndex(index == 3 ? 3 : index-1);
        repaintEditorLayout();
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        if(jComboBox5.getSelectedIndex() >= 0 && tilesetsLayoutPanel != null) {
            tilesetsLayoutPanel.setSelectedTileset(jComboBox5.getSelectedIndex());
            repaintEditorLayout();
        }
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        if (jComboBox4.getSelectedIndex() >= 0 && tilesetsLayoutPanel != null) {
            int scale = (int)jComboBox4.getSelectedIndex()+1;
            if (scale != MapBlockSettings.getTilesetScale()) {
                tilesetsLayoutPanel.setDisplayScale(scale);
                repaintEditorLayout();
                MapBlockSettings.setTilesetScale(scale);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (jComboBox1.getSelectedIndex() >= 0 && mapBlocksetLayoutPanel != null) {
            int scale = (int)jComboBox1.getSelectedIndex()+1;
            if (scale != MapBlockSettings.getBlocksetScale()) {
                mapBlocksetLayoutPanel.setDisplayScale(scale);
                repaintEditorLayout();
                MapBlockSettings.setBlocksetScale(scale);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        if (mapBlocksetLayoutPanel != null) {
            mapBlocksetLayoutPanel.setShowGrid(jCheckBox1.isSelected());
            repaintMapBlockLayout();
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        if (tilesetsLayoutPanel != null) {
            tilesetsLayoutPanel.setShowGrid(jCheckBox2.isSelected());
            repaintTilesetLayout();
        }
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        if (mapBlocksetLayoutPanel != null) {
            int blocksPerRow = (int)jSpinner1.getValue();
            if (blocksPerRow != MapBlockSettings.getBlocksetBlocksPerRow()) {
                mapBlocksetLayoutPanel.setBlocksPerRow(blocksPerRow);
                repaintMapBlockLayout();
                MapBlockSettings.setBlocksetBlocksPerRow(blocksPerRow);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jSpinner4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner4StateChanged
        if (tilesetsLayoutPanel != null) {
            int tilesPerRow = (int)jSpinner4.getValue();
            if (tilesPerRow != MapBlockSettings.getTilesetTilesPerRow()) {
                tilesetsLayoutPanel.setItemsPerRow(tilesPerRow);
                repaintTilesetLayout();
                MapBlockSettings.setTilesetTilesPerRow(tilesPerRow);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jSpinner4StateChanged

    private void jCheckBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox3ItemStateChanged
        editableBlockSlotPanel.setShowPriority(jCheckBox3.isSelected());
        repaintSelectedBlockPanel();
    }//GEN-LAST:event_jCheckBox3ItemStateChanged

    private void jCheckBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox4ItemStateChanged
        mapBlocksetLayoutPanel.setShowPriority(jCheckBox4.isSelected());
        repaintMapBlockLayout();
    }//GEN-LAST:event_jCheckBox4ItemStateChanged

    private void jCheckBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox5ItemStateChanged
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setShowGrid(jCheckBox5.isSelected());
            repaintSelectedBlockPanel();
        }
    }//GEN-LAST:event_jCheckBox5ItemStateChanged

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_PRIORITY);
            blockSlot.setShowPriority(true);
            repaintSelectedBlockPanel();
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_FLIP);
            blockSlot.setShowPriority(jCheckBox3.isSelected());
            repaintSelectedBlockPanel();
        }
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_PAINT_TILE);
            blockSlot.setShowPriority(jCheckBox3.isSelected());
            repaintSelectedBlockPanel();
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //mapblockManager.exportImage(jTextField27.getText(), jTextField28.getText(), (int)jSpinner1.getValue());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void colorPickerBlocksetColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerBlocksetColorChanged
        mapBlocksetLayoutPanel.setBGColor(colorPickerBlockset.getColor());
        MapBlockSettings.setBlocksetBGColor(colorPickerBlockset.getColor());
        SettingsManager.saveSettingsFile();
        repaintMapBlockLayout();
    }//GEN-LAST:event_colorPickerBlocksetColorChanged

    private void colorPickerTilesetColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerTilesetColorChanged
        tilesetsLayoutPanel.setBGColor(colorPickerTileset.getColor());
        MapBlockSettings.setTilesetBGColor(colorPickerTileset.getColor());
        SettingsManager.saveSettingsFile();
        repaintTilesetLayout();
    }//GEN-LAST:event_colorPickerTilesetColorChanged

    private void colorPickerBlocksColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerBlocksColorChanged
        editableBlockSlotPanel.setBGColor(colorPickerBlocks.getColor());
        MapBlockSettings.setBlockBGColor(colorPickerBlocks.getColor());
        SettingsManager.saveSettingsFile();
        repaintSelectedBlockPanel();
    }//GEN-LAST:event_colorPickerBlocksColorChanged

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        int index = editableBlockSlotPanel.getBlockIndex();
        if (index < 0 || index >= blockset.getBlocks().length) return;
        if (index <= 2) {
            Console.logger().warning("WARNING Cannot clone from first 3 map slots.");
            return;
        }
        MapBlock block = blockset.getBlocks()[index];
        blockset.insertBlock(index+1, block);
        mapBlocksetLayoutPanel.setLeftSelectedIndex(index+1);
        repaintMapBlockLayout();
    }//GEN-LAST:event_jButton37ActionPerformed

    /**
     * To create a new Main Editor, copy the below code
     * Don't forget to change the new main class (below)
     */
    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MapBlockMainEditor().setVisible(true);  // <------ Change this class to new Main Editor class
            }
        });
    }
    /**
     * To create a new Main Editor, copy the above code
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel1;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerBlocks;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerBlockset;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerTileset;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButton1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButton2;
    private com.sfc.sf2.map.block.gui.EditableBlockSlotPanel editableBlockSlotPanel;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton10;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton11;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton13;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton14;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton6;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton7;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton8;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton9;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton2;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton3;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton4;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton5;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel mapBlocksetLayoutPanel;
    private com.sfc.sf2.map.block.gui.TileSlotPanel tileSlotPanelLeft;
    private com.sfc.sf2.map.block.gui.TileSlotPanel tileSlotPanelRight;
    private com.sfc.sf2.map.block.gui.TilesetsLayoutPanel tilesetsLayoutPanel;
    // End of variables declaration//GEN-END:variables

}
