/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation.gui;

import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.SpinnerAction;
import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import com.sfc.sf2.map.animation.MapAnimationManager;
import com.sfc.sf2.map.animation.actions.ActionMapAnimationData;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import com.sfc.sf2.map.block.settings.MapBlockSettings;
import java.awt.Color;
import java.awt.event.ActionEvent;

/**
 *
 * @author TiMMy
 */
public class MapAnimationMainEditor extends AbstractMainEditor {
    
    private final MapAnimationManager mapAnimationManager = new MapAnimationManager();
    
    private ActionMapAnimationData currentAnimData;
    
    public MapAnimationMainEditor() {
        super();
        SettingsManager.registerSettingsStore("mapLayout", new MapBlockSettings());
        SettingsManager.registerSettingsStore("mapTileset", new MapBlockSettings(20, RenderScaleHelpers.stringToIndex("2x"), new Color(200, 0, 200)));
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        mapLayoutAnimViewPanel1.setLayoutPanel(mapAnimationLayoutPanel, this::animationActionPerformed);
        tilesetAnimViewPanel1.setLayoutPanel(tilesetLayoutPanelAnim, tilesetLayoutPanelModified, this::animationActionPerformed);
        accordionPanel1.setExpanded(false);
        accordionPanel2.setExpanded(false);
        
        tilesetLayoutPanelModified.getAnimator().addAnimationListener(this::onAnimationUpdated);
        
        tableAnimFrames.addListSelectionListener(this::onAnimationFramesSelectionChanged);
        tableAnimFrames.addTableModelListener(this::onAnimationFramesDataChanged);
        tableAnimFrames.jTable.getColumnModel().getColumn(0).setMaxWidth(30);
        
        infoButtonSharedAnimation.setVisible(false);
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        ActionMapAnimationData newValue = new ActionMapAnimationData(mapAnimationManager.getMapLayout(), mapAnimationManager.getMapBlockset(), mapAnimationManager.getSharedBlockInfo(), mapAnimationManager.getMapAnimation(), mapAnimationManager.getSharedAnimationInfo());
        ActionMapAnimationData oldValue = currentAnimData;
        if (oldValue == null)
            oldValue = new ActionMapAnimationData(null, null, null, null, null);
        ActionManager.setAndExecuteAction(new Action<ActionMapAnimationData>(this, "Map Animation Imported", this::actionAnimationLoaded, newValue, oldValue));
    }
    
    private void actionAnimationLoaded(ActionMapAnimationData data) {
        currentAnimData = data;
        mapAnimationLayoutPanel.setMapLayout(data.layout());
        
        MapAnimation animation = data.animation();
        tilesetLayoutPanelAnim.setMapAnimation(animation);
        tilesetLayoutPanelModified.setMapAnimation(animation);
        tilesetLayoutPanelModified.getAnimator().stopAnimation();
        tableAnimFrames.jTable.clearSelection();
        animationActionPerformed(new ActionEvent(this, 0, null));
        if (animation == null) {
            tilesetLayoutPanelModified.setSelectedTileset(-1);
            mapAnimationFrameTableModel.setTableData(null);
        } else {
            tilesetLayoutPanelAnim.setMapAnimation(animation);
            tilesetLayoutPanelModified.setMapAnimation(animation);
            if (animation.getFrames().length == 0) {
                tilesetLayoutPanelModified.setSelectedTileset(-1);
            } else {
                tilesetLayoutPanelModified.setSelectedTileset(animation.getFrames()[0].getDestTileset());
            }
            jSpinnerTilesetId.setValue(animation.getTilesetId());
            jSpinnerTilesetLength.setValue(animation.getLength());

            tableAnimFrames.jTable.clearSelection();
            mapAnimationFrameTableModel.setTableData(animation.getFrames());
        }
        
        String sharedAnimationInfo = data.sharedAnimationInfo();
        infoButtonSharedAnimation.setVisible(sharedAnimationInfo != null);
        if (sharedAnimationInfo != null) {
            infoButtonSharedAnimation.setMessageText("This animation data is used by the following maps:\n" + sharedAnimationInfo + "\nAny changes will affect all of these maps.\n\nTo unlink the maps, you can export this animation for a specific map folder and then update \\maps\\entries.asm");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mapAnimationFrameTableModel = new com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel();
        flatOptionPaneWarningIcon1 = new com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon();
        jPanel13 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel18 = new javax.swing.JPanel();
        accordionPanel2 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonImportPaletteEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImporttilesetEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportMapEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        directoryButtonImportMapDir = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jSpinnerImportMapIndex = new javax.swing.JSpinner();
        jButtonImportMapEntry = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jSpinnerExportMapIndex = new javax.swing.JSpinner();
        jButtonExportMapEntry = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton32 = new javax.swing.JButton();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel24 = new javax.swing.JPanel();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonImportPaletteEntries1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTilesetEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTilesets = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportBlockset = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportLayout = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportAnim = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel27 = new javax.swing.JPanel();
        directoryButtonImportMapFolder = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jLabel17 = new javax.swing.JLabel();
        jButtonImportMapFolder = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        directoryButtonExportMapFolder = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jLabel19 = new javax.swing.JLabel();
        jButtonExportMapFolder = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        fileButtonImportPalette = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTileset1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTileset2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTileset3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTileset4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTileset5 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportBlocksFile = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportLayoutFile = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel2 = new javax.swing.JLabel();
        jButtonImportRaw = new javax.swing.JButton();
        fileButtonImportAnimFile = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportTilesetEntries2 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerTilesetId = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinnerTilesetLength = new javax.swing.JSpinner();
        infoButtonSharedAnimation = new com.sfc.sf2.core.gui.controls.InfoButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tilesetLayoutPanelAnim = new com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel();
        tableAnimFrames = new com.sfc.sf2.core.gui.controls.Table();
        jScrollPane8 = new javax.swing.JScrollPane();
        tilesetLayoutPanelModified = new com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel();
        tilesetAnimViewPanel1 = new com.sfc.sf2.map.animation.gui.TilesetAnimViewPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mapAnimationLayoutPanel = new com.sfc.sf2.map.layout.gui.MapLayoutPanel();
        mapLayoutAnimViewPanel1 = new com.sfc.sf2.map.animation.gui.MapLayoutAnimViewPanel();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2MapAnimationEditor");

        jSplitPane1.setDividerLocation(700);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(650);
        jSplitPane2.setOneTouchExpandable(true);

        jSplitPane4.setDividerLocation(300);
        jSplitPane4.setOneTouchExpandable(true);

        jPanel18.setPreferredSize(new java.awt.Dimension(300, 623));

        accordionPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette, tilesets, & map data"));

        fileButtonImportPaletteEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonImportPaletteEntries.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButtonImportPaletteEntries.setInfoMessage("");
        fileButtonImportPaletteEntries.setLabelText("Palette entries :");
        fileButtonImportPaletteEntries.setName("Import Palette Entries"); // NOI18N

        fileButtonImporttilesetEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonImporttilesetEntries.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButtonImporttilesetEntries.setInfoMessage("");
        fileButtonImporttilesetEntries.setLabelText("Tilesets entries :");
        fileButtonImporttilesetEntries.setName("Import Tilesets Entries"); // NOI18N

        fileButtonImportMapEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonImportMapEntries.setFilePath(".\\entries.asm");
        fileButtonImportMapEntries.setInfoMessage("");
        fileButtonImportMapEntries.setLabelText("Map entries :");
        fileButtonImportMapEntries.setName("Import Map Entries"); // NOI18N

        directoryButtonImportMapDir.setDirectoryPath(".\\entries\\");
            directoryButtonImportMapDir.setInfoMessage("");
            directoryButtonImportMapDir.setLabelText("Maps dir :");
            directoryButtonImportMapDir.setName("Import Maps Directory"); // NOI18N

            javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
            accordionPanel2.setLayout(accordionPanel2Layout);
            accordionPanel2Layout.setHorizontalGroup(
                accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fileButtonImportPaletteEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButtonImporttilesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButtonImportMapEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(directoryButtonImportMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );
            accordionPanel2Layout.setVerticalGroup(
                accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(fileButtonImportPaletteEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButtonImporttilesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButtonImportMapEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(directoryButtonImportMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

            jLabel10.setText("<html>Select map number to import from map entries.</html>");
            jLabel10.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jSpinnerImportMapIndex.setModel(new javax.swing.SpinnerNumberModel(3, 0, 255, 1));
            jSpinnerImportMapIndex.setName("Import Map Num"); // NOI18N

            jButtonImportMapEntry.setText("Import");
            jButtonImportMapEntry.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonImportMapEntryActionPerformed(evt);
                }
            });

            jLabel4.setText("Map :");

            javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
            jPanel21.setLayout(jPanel21Layout);
            jPanel21Layout.setHorizontalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSpinnerImportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonImportMapEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerImportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jButtonImportMapEntry))
                    .addGap(9, 9, 9)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Export :"));

            jLabel16.setText("<html>Select map number to export to files defined in map entries.</html>");
            jLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jSpinnerExportMapIndex.setModel(new javax.swing.SpinnerNumberModel(3, 0, 255, 1));
            jSpinnerExportMapIndex.setName("Export Map Num"); // NOI18N

            jButtonExportMapEntry.setText("Export");
            jButtonExportMapEntry.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonExportMapEntryActionPerformed(evt);
                }
            });

            jLabel5.setText("Map :");

            jButton32.setText("Save as new");
            jButton32.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton32ActionPerformed(evt);
                }
            });

            infoButton1.setMessageText("<html>Create a new map entry and map folder and save the data to that entry.</html>");
            infoButton1.setText("");

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton32)
                    .addContainerGap())
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton32)
                        .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
            jPanel26.setLayout(jPanel26Layout);
            jPanel26Layout.setHorizontalGroup(
                jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                        .addGroup(jPanel26Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSpinnerExportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonExportMapEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanel26Layout.setVerticalGroup(
                jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerExportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jButtonExportMapEntry))
                    .addGap(9, 9, 9)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
            jPanel18.setLayout(jPanel18Layout);
            jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(accordionPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(accordionPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 316, Short.MAX_VALUE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Entries", jPanel18);

            accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette, tilesets, & map data"));

            fileButtonImportPaletteEntries1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButtonImportPaletteEntries1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
            fileButtonImportPaletteEntries1.setInfoMessage("");
            fileButtonImportPaletteEntries1.setLabelText("Palette entries :");
            fileButtonImportPaletteEntries1.setName("Import Palette Entries"); // NOI18N

            fileButtonImportTilesetEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButtonImportTilesetEntries.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
            fileButtonImportTilesetEntries.setInfoMessage("");
            fileButtonImportTilesetEntries.setLabelText("Tilesets entries :");
            fileButtonImportTilesetEntries.setName("Import Tilesets Entries"); // NOI18N

            fileButtonImportTilesets.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButtonImportTilesets.setFilePath("00-tilesets.asm");
            fileButtonImportTilesets.setInfoMessage("");
            fileButtonImportTilesets.setLabelText("Map tilesets :");
            fileButtonImportTilesets.setName("Import Map Tilesets"); // NOI18N

            fileButtonImportBlockset.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
            fileButtonImportBlockset.setFilePath("0-blocks.bin");
            fileButtonImportBlockset.setInfoMessage("");
            fileButtonImportBlockset.setLabelText("Map blockset :");
            fileButtonImportBlockset.setToolTipText("");
            fileButtonImportBlockset.setName("Import Map Blockset"); // NOI18N

            fileButtonImportLayout.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
            fileButtonImportLayout.setFilePath("1-layout.bin");
            fileButtonImportLayout.setInfoMessage("");
            fileButtonImportLayout.setLabelText("Map layout :");
            fileButtonImportLayout.setToolTipText("");
            fileButtonImportLayout.setName("Import Map Layout"); // NOI18N

            fileButtonImportAnim.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButtonImportAnim.setFilePath("9-animations.asm");
            fileButtonImportAnim.setInfoMessage("");
            fileButtonImportAnim.setLabelText("Map anim :");
            fileButtonImportAnim.setToolTipText("");
            fileButtonImportAnim.setName("Import Map Animation"); // NOI18N

            javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
            accordionPanel1.setLayout(accordionPanel1Layout);
            accordionPanel1Layout.setHorizontalGroup(
                accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fileButtonImportPaletteEntries1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                        .addComponent(fileButtonImportTilesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButtonImportTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButtonImportBlockset, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButtonImportLayout, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButtonImportAnim, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );
            accordionPanel1Layout.setVerticalGroup(
                accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(fileButtonImportPaletteEntries1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButtonImportTilesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButtonImportTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButtonImportBlockset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButtonImportLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButtonImportAnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

            directoryButtonImportMapFolder.setDirectoryPath(".\\entries\\map03\\");
                directoryButtonImportMapFolder.setLabelText("Map dir :");
                directoryButtonImportMapFolder.setName("Import Map Directory"); // NOI18N

                jLabel17.setText("<html>Select map directory to load map from.</html>");
                jLabel17.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                jButtonImportMapFolder.setText("Import");
                jButtonImportMapFolder.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonImportMapFolderActionPerformed(evt);
                    }
                });

                jLabel18.setText("<html>NOTE: May not load shared files defined in other maps (load from map entries instead).</html>");
                jLabel18.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
                jPanel27.setLayout(jPanel27Layout);
                jPanel27Layout.setHorizontalGroup(
                    jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(directoryButtonImportMapFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonImportMapFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel27Layout.setVerticalGroup(
                    jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(directoryButtonImportMapFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonImportMapFolder)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Export :"));

                directoryButtonExportMapFolder.setDirectoryPath(".\\entries\\map03\\");
                    directoryButtonExportMapFolder.setLabelText("Map dir :");
                    directoryButtonExportMapFolder.setName("Export Map Directory"); // NOI18N

                    jLabel19.setText("<html>Select map directory to save map data to</html>");
                    jLabel19.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                    jButtonExportMapFolder.setText("Export");
                    jButtonExportMapFolder.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButtonExportMapFolderActionPerformed(evt);
                        }
                    });

                    javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
                    jPanel28.setLayout(jPanel28Layout);
                    jPanel28Layout.setHorizontalGroup(
                        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(directoryButtonExportMapFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel28Layout.createSequentialGroup()
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButtonExportMapFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                    );
                    jPanel28Layout.setVerticalGroup(
                        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(directoryButtonExportMapFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonExportMapFolder))
                            .addContainerGap())
                    );

                    javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
                    jPanel24.setLayout(jPanel24Layout);
                    jPanel24Layout.setHorizontalGroup(
                        jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(accordionPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel24Layout.setVerticalGroup(
                        jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(accordionPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                            .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );

                    jTabbedPane1.addTab("Map folder", jPanel24);

                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

                    fileButtonImportPalette.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportPalette.setFilePath("..\\graphics\\maps\\mappalettes\\mappalette00.bin");
                    fileButtonImportPalette.setInfoMessage("");
                    fileButtonImportPalette.setLabelText("Palette :");
                    fileButtonImportPalette.setName("Import Palette"); // NOI18N

                    fileButtonImportTileset1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportTileset1.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset000.bin");
                    fileButtonImportTileset1.setInfoMessage("");
                    fileButtonImportTileset1.setLabelText("Tileset 1 :");
                    fileButtonImportTileset1.setName("Import Tileset 1"); // NOI18N

                    fileButtonImportTileset2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportTileset2.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset037.bin");
                    fileButtonImportTileset2.setInfoMessage("");
                    fileButtonImportTileset2.setLabelText("Tileset 2 :");
                    fileButtonImportTileset2.setName("Import Tileset 2"); // NOI18N

                    fileButtonImportTileset3.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportTileset3.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset043.bin");
                    fileButtonImportTileset3.setInfoMessage("");
                    fileButtonImportTileset3.setLabelText("Tileset 3 :");
                    fileButtonImportTileset3.setName("Import Tileset 3"); // NOI18N

                    fileButtonImportTileset4.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportTileset4.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset053.bin");
                    fileButtonImportTileset4.setInfoMessage("");
                    fileButtonImportTileset4.setLabelText("Tileset 4 :");
                    fileButtonImportTileset4.setName("Import Tileset 4"); // NOI18N

                    fileButtonImportTileset5.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportTileset5.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset066.bin");
                    fileButtonImportTileset5.setInfoMessage("");
                    fileButtonImportTileset5.setLabelText("Tileset 5 :");
                    fileButtonImportTileset5.setName("Import Tileset 5"); // NOI18N

                    fileButtonImportBlocksFile.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportBlocksFile.setFilePath(".\\entries\\map03\\0-blocks.bin");
                    fileButtonImportBlocksFile.setInfoMessage("");
                    fileButtonImportBlocksFile.setLabelText("Blocks file :");
                    fileButtonImportBlocksFile.setName("Import Blocks File"); // NOI18N

                    fileButtonImportLayoutFile.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButtonImportLayoutFile.setFilePath(".\\entries\\map03\\1-layout.bin");
                    fileButtonImportLayoutFile.setInfoMessage("");
                    fileButtonImportLayoutFile.setLabelText("Layout file :");
                    fileButtonImportLayoutFile.setToolTipText("");
                    fileButtonImportLayoutFile.setName("Import Layout File"); // NOI18N

                    jLabel2.setText("<html>Select individual disassembly files.</html>");
                    jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                    jButtonImportRaw.setText("Import");
                    jButtonImportRaw.setName("Import Raw Files"); // NOI18N
                    jButtonImportRaw.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButtonImportRawActionPerformed(evt);
                        }
                    });

                    fileButtonImportAnimFile.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                    fileButtonImportAnimFile.setFilePath(".\\entries\\map03\\9-animations.asm");
                    fileButtonImportAnimFile.setInfoMessage("");
                    fileButtonImportAnimFile.setLabelText("Animation file :");
                    fileButtonImportAnimFile.setToolTipText("");
                    fileButtonImportAnimFile.setName("Import Animation File"); // NOI18N

                    fileButtonImportTilesetEntries2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                    fileButtonImportTilesetEntries2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
                    fileButtonImportTilesetEntries2.setInfoMessage("");
                    fileButtonImportTilesetEntries2.setLabelText("Tilesets entries :");
                    fileButtonImportTilesetEntries2.setName("Import Tilesets Entries"); // NOI18N

                    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                    jPanel4.setLayout(jPanel4Layout);
                    jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fileButtonImportPalette, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButtonImportRaw))
                                .addComponent(fileButtonImportTileset1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportTileset2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportTileset3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportTileset4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportTileset5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportBlocksFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportLayoutFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportAnimFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButtonImportTilesetEntries2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(fileButtonImportTilesetEntries2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(fileButtonImportPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButtonImportTileset1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButtonImportTileset2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButtonImportTileset3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButtonImportTileset4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButtonImportTileset5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButtonImportBlocksFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(fileButtonImportLayoutFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButtonImportAnimFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonImportRaw))
                            .addContainerGap())
                    );

                    javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
                    jPanel19.setLayout(jPanel19Layout);
                    jPanel19Layout.setHorizontalGroup(
                        jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
                    );
                    jPanel19Layout.setVerticalGroup(
                        jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(267, Short.MAX_VALUE))
                    );

                    jTabbedPane1.addTab("Raw files", jPanel19);

                    javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
                    jPanel33.setLayout(jPanel33Layout);
                    jPanel33Layout.setHorizontalGroup(
                        jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
                    );
                    jPanel33Layout.setVerticalGroup(
                        jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
                    );

                    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                    jPanel9.setLayout(jPanel9Layout);
                    jPanel9Layout.setHorizontalGroup(
                        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    );
                    jPanel9Layout.setVerticalGroup(
                        jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(2, 2, 2))
                    );

                    jSplitPane4.setLeftComponent(jPanel9);

                    jLabel8.setText("Tileset :");

                    jSpinnerTilesetId.setModel(new javax.swing.SpinnerNumberModel(0, 0, 256, 1));
                    jSpinnerTilesetId.setName("Tileset ID Spinner"); // NOI18N
                    jSpinnerTilesetId.addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(javax.swing.event.ChangeEvent evt) {
                            jSpinnerTilesetIdStateChanged(evt);
                        }
                    });

                    jLabel9.setText("Tileset length :");

                    jSpinnerTilesetLength.setModel(new javax.swing.SpinnerNumberModel(0, 0, 128, 1));
                    jSpinnerTilesetLength.setName("Tileset Length Spinner"); // NOI18N
                    jSpinnerTilesetLength.addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(javax.swing.event.ChangeEvent evt) {
                            jSpinnerTilesetLengthStateChanged(evt);
                        }
                    });

                    infoButtonSharedAnimation.setIcon(flatOptionPaneWarningIcon1);
                    infoButtonSharedAnimation.setText("");

                    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                    jPanel2.setLayout(jPanel2Layout);
                    jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jSpinnerTilesetId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(infoButtonSharedAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jSpinnerTilesetLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );
                    jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(infoButtonSharedAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jSpinnerTilesetId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(jSpinnerTilesetLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                    );

                    jScrollPane9.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Animation Tileset"));
                    jScrollPane9.setPreferredSize(new java.awt.Dimension(335, 154));

                    javax.swing.GroupLayout tilesetLayoutPanelAnimLayout = new javax.swing.GroupLayout(tilesetLayoutPanelAnim);
                    tilesetLayoutPanelAnim.setLayout(tilesetLayoutPanelAnimLayout);
                    tilesetLayoutPanelAnimLayout.setHorizontalGroup(
                        tilesetLayoutPanelAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 399, Short.MAX_VALUE)
                    );
                    tilesetLayoutPanelAnimLayout.setVerticalGroup(
                        tilesetLayoutPanelAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 125, Short.MAX_VALUE)
                    );

                    jScrollPane9.setViewportView(tilesetLayoutPanelAnim);

                    tableAnimFrames.setBorder(null);
                    tableAnimFrames.setModel(mapAnimationFrameTableModel);
                    tableAnimFrames.setSpinnerNumberEditor(true);

                    jScrollPane8.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Modified Tileset"));
                    jScrollPane8.setPreferredSize(new java.awt.Dimension(335, 154));

                    javax.swing.GroupLayout tilesetLayoutPanelModifiedLayout = new javax.swing.GroupLayout(tilesetLayoutPanelModified);
                    tilesetLayoutPanelModified.setLayout(tilesetLayoutPanelModifiedLayout);
                    tilesetLayoutPanelModifiedLayout.setHorizontalGroup(
                        tilesetLayoutPanelModifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 399, Short.MAX_VALUE)
                    );
                    tilesetLayoutPanelModifiedLayout.setVerticalGroup(
                        tilesetLayoutPanelModifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 125, Short.MAX_VALUE)
                    );

                    jScrollPane8.setViewportView(tilesetLayoutPanelModified);

                    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                    jPanel11.setLayout(jPanel11Layout);
                    jPanel11Layout.setHorizontalGroup(
                        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tableAnimFrames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tilesetAnimViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel11Layout.setVerticalGroup(
                        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                            .addGap(0, 0, 0)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(tableAnimFrames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(0, 0, 0)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(tilesetAnimViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );

                    jSplitPane4.setRightComponent(jPanel11);

                    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
                    jPanel8.setLayout(jPanel8Layout);
                    jPanel8Layout.setHorizontalGroup(
                        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane4)
                    );
                    jPanel8Layout.setVerticalGroup(
                        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane4)
                    );

                    jSplitPane2.setLeftComponent(jPanel8);

                    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map"));

                    jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    javax.swing.GroupLayout mapAnimationLayoutPanelLayout = new javax.swing.GroupLayout(mapAnimationLayoutPanel);
                    mapAnimationLayoutPanel.setLayout(mapAnimationLayoutPanelLayout);
                    mapAnimationLayoutPanelLayout.setHorizontalGroup(
                        mapAnimationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1536, Short.MAX_VALUE)
                    );
                    mapAnimationLayoutPanelLayout.setVerticalGroup(
                        mapAnimationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1536, Short.MAX_VALUE)
                    );

                    jScrollPane2.setViewportView(mapAnimationLayoutPanel);

                    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                    jPanel1.setLayout(jPanel1Layout);
                    jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                    );
                    jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                    );

                    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                    jPanel10.setLayout(jPanel10Layout);
                    jPanel10Layout.setHorizontalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mapLayoutAnimViewPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel10Layout.setVerticalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(0, 0, 0)
                            .addComponent(mapLayoutAnimViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );

                    jSplitPane2.setRightComponent(jPanel10);

                    javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
                    jPanel15.setLayout(jPanel15Layout);
                    jPanel15Layout.setHorizontalGroup(
                        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane2)
                    );
                    jPanel15Layout.setVerticalGroup(
                        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane2)
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
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
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

                    setSize(new java.awt.Dimension(1216, 908));
                    setLocationRelativeTo(null);
                }// </editor-fold>//GEN-END:initComponents

    private void jSpinnerTilesetLengthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerTilesetLengthStateChanged
        if (!ActionManager.isActionTriggering()) {
            int oldVal = tilesetLayoutPanelModified.getMapAnimation() == null ? 0 : tilesetLayoutPanelModified.getMapAnimation().getLength();
            ActionManager.setActionWithoutExecute(new SpinnerAction(jSpinnerTilesetLength, jSpinnerTilesetLength.getValue(), oldVal));
        }
        int value = (int)jSpinnerTilesetLength.getValue();
        MapAnimation anim = tilesetLayoutPanelModified.getMapAnimation();
        if (anim != null && anim.getLength()!= value) {
            anim.setLength(value);
        }
    }//GEN-LAST:event_jSpinnerTilesetLengthStateChanged

    private void jSpinnerTilesetIdStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerTilesetIdStateChanged
        if (!ActionManager.isActionTriggering()) {
            int oldVal = tilesetLayoutPanelModified.getMapAnimation() == null ? 0 : tilesetLayoutPanelModified.getMapAnimation().getTilesetId();
            ActionManager.setActionWithoutExecute(new SpinnerAction(jSpinnerTilesetId, jSpinnerTilesetId.getValue(), oldVal));
        }
        int value = (int)jSpinnerTilesetId.getValue();
        MapAnimation anim = tilesetLayoutPanelModified.getMapAnimation();
        if (anim != null && anim.getTilesetId()!= value) {
            try {
                anim.setTilesetId(value);
                Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImporttilesetEntries.getFilePath());
                Tileset tileset = mapAnimationManager.importAnimationTileset(tilesetLayoutPanelAnim.getMapAnimation(), mapAnimationLayoutPanel.getMapLayout().getPalette(), tilesetEntriesPath, value);
                tilesetLayoutPanelAnim.setTileset(tileset);
            } catch (Exception ex) {
                Console.logger().log(Level.SEVERE, null, ex);
                Console.logger().severe("ERROR Tileset could not be imported for tileset : " + value);
            }
        }
    }//GEN-LAST:event_jSpinnerTilesetIdStateChanged

    private void jButtonImportMapEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportMapEntryActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImportPaletteEntries.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImporttilesetEntries.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImportMapEntries.getFilePath());
        int mapId = (int)jSpinnerImportMapIndex.getValue();
        try {
            mapAnimationManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
            jSpinnerExportMapIndex.setValue(jSpinnerImportMapIndex.getValue());
        } catch (Exception ex) {
            mapAnimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation be imported for map : " + mapId);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButtonImportMapEntryActionPerformed

    private void jButtonExportMapEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportMapEntryActionPerformed
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImportMapEntries.getFilePath());
        int mapId = (int)jSpinnerExportMapIndex.getValue();
        try {
            mapAnimationManager.exportDisassemblyFromMapEntries(mapEntriesPath, mapId, tilesetLayoutPanelModified.getMapAnimation());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be exported to : " + mapId);
        }
    }//GEN-LAST:event_jButtonExportMapEntryActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        //TODO Add support for creating new map entries
        /*Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton3.getFilePath());
        Path mapDirectories = PathHelpers.getBasePath().resolve(directoryButton3.getDirectoryPath());
        try {
            int mapId = maplayoutManager.createNewMapEntry(mapDirectories, mapEntriesPath);
            jSpinner4.setValue(mapId);
            jSpinner5.setValue(mapId);
            jButton30ActionPerformed(null); //Save the map in new location
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR New map entry could not be created.");
        }*/
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButtonImportMapFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportMapFolderActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButtonImportMapFolder.getDirectoryPath());
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImportPaletteEntries1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImportTilesetEntries.getFilePath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButtonImportTilesets.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButtonImportBlockset.getFilePath());
        Path mapLayoutDataPath = mapDirectory.resolve(fileButtonImportLayout.getFilePath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButtonImportAnim.getFilePath());
        int mapId = (int)jSpinnerImportMapIndex.getValue();
        try {
            mapAnimationManager.importDisassemblyFromMapData(paletteEntriesPath, tilesetEntriesPath, mapTilesetDataPath, mapBlocksetDataPath, mapLayoutDataPath, mapAnimationDataPath);
            directoryButtonExportMapFolder.setDirectoryPath(directoryButtonExportMapFolder.getDirectoryPath());
        } catch (Exception ex) {
            mapAnimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be imported from : " + mapAnimationDataPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButtonImportMapFolderActionPerformed

    private void jButtonExportMapFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportMapFolderActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButtonExportMapFolder.getDirectoryPath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButtonImportAnim.getFilePath());
        if (!PathHelpers.createPathIfRequred(mapDirectory)) return;
        try {
            mapAnimationManager.exportDisassembly(mapAnimationDataPath, tilesetLayoutPanelModified.getMapAnimation());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be exported to : " + mapAnimationDataPath);
        }
    }//GEN-LAST:event_jButtonExportMapFolderActionPerformed

    private void jButtonImportRawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportRawActionPerformed
        Path palettePath = PathHelpers.getBasePath().resolve(fileButtonImportPalette.getFilePath());
        Path tilesetPath1 = PathHelpers.getBasePath().resolve(fileButtonImportTileset1.getFilePath());
        Path tilesetPath2 = PathHelpers.getBasePath().resolve(fileButtonImportTileset2.getFilePath());
        Path tilesetPath3 = PathHelpers.getBasePath().resolve(fileButtonImportTileset3.getFilePath());
        Path tilesetPath4 = PathHelpers.getBasePath().resolve(fileButtonImportTileset4.getFilePath());
        Path tilesetPath5 = PathHelpers.getBasePath().resolve(fileButtonImportTileset5.getFilePath());
        Path blocksPath = PathHelpers.getBasePath().resolve(fileButtonImportBlocksFile.getFilePath());
        Path layoutPath = PathHelpers.getBasePath().resolve(fileButtonImportLayoutFile.getFilePath());
        Path animationPath = PathHelpers.getBasePath().resolve(fileButtonImportAnimFile.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonImportTilesetEntries2.getFilePath());
        try {
            mapAnimationManager.importDisassemblyFromRawFiles(palettePath, new Path[] { tilesetPath1, tilesetPath2, tilesetPath3, tilesetPath4, tilesetPath5 }, blocksPath, layoutPath, animationPath, tilesetEntriesPath);
        } catch (Exception ex) {
            mapAnimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be imported from : " + animationPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButtonImportRawActionPerformed
    
    private void animationActionPerformed(java.awt.event.ActionEvent evt) {                                          
        boolean isSelected = evt.getID() == 1;
        mapLayoutAnimViewPanel1.getCheckBoxPreviewAnim().setSelected(isSelected);
        tilesetAnimViewPanel1.getCheckBoxPreviewAnim().setSelected(isSelected);
        tilesetLayoutPanelModified.setPreviewAnim(isSelected);
    }
    
    private void onAnimationFramesSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || tilesetLayoutPanelModified.getAnimator().isAnimating()) return;
        int selected = tableAnimFrames.jTable.getSelectedRow();
        tilesetLayoutPanelAnim.setSelectedFrame(selected);
        tilesetLayoutPanelModified.setSelectedFrame(selected);
        if (selected == -1) selected = 0;
        MapAnimation animation = tilesetLayoutPanelModified.getMapAnimation();
        int tileset = -1;
        if (animation != null && animation.getFrames() != null && selected < animation.getFrames().length) {
            tileset = animation.getFrames()[selected].getDestTileset();
        }
        tilesetLayoutPanelModified.setSelectedTileset(tileset);
    }
    
    private void onAnimationFramesDataChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT || e.getType() == TableModelEvent.DELETE) {
            //Number of animation frames changed
            MapAnimationFrame[] frames = mapAnimationFrameTableModel.getTableData(MapAnimationFrame[].class);
            tilesetLayoutPanelModified.getMapAnimation().setFrames(frames);
        } else if (e.getColumn() == 3) {
            //Editing destination tileset
            MapAnimation animation = tilesetLayoutPanelModified.getMapAnimation();
            animation.generateModifiedTilesets();
            tilesetLayoutPanelModified.setMapAnimation(animation);
            tilesetLayoutPanelModified.setSelectedTileset(animation.getFrames()[e.getFirstRow()].getDestTileset());
        } else if (e.getColumn() == 4) {
            //Editing destination index
            MapAnimation animation = tilesetLayoutPanelModified.getMapAnimation();
            int frame = e.getFirstRow();
            animation.generateModifiedTileset(frame);
            tilesetLayoutPanelModified.setMapAnimation(animation);
            tilesetLayoutPanelModified.setSelectedTileset(animation.getFrames()[frame].getDestTileset());
        } else if (e.getColumn() < 3) {
            //Editing the start or length of the frame (affects both panels)
            tilesetLayoutPanelAnim.redraw();
        }
        tilesetLayoutPanelModified.redraw();
    }
    
    private void onAnimationUpdated(LayoutAnimator.AnimationListener.AnimationFrameEvent e) {
        mapAnimationLayoutPanel.getMapLayout().clearIndexedColorImage(true);
        mapAnimationLayoutPanel.redraw();
        tableAnimFrames.jTable.setRowSelectionInterval(e.getCurrentFrame(), e.getCurrentFrame());
    }
    
    /**
     * To create a new Main Editor, copy the below code
     * Don't forget to change the new main class (below)
     */
    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MapAnimationMainEditor().setVisible(true);  // <------ Change this class to new Main Editor class
            }
        });
    }
    /**
     * To create a new Main Editor, copy the above code
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel1;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel2;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonExportMapFolder;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonImportMapDir;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonImportMapFolder;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportAnim;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportAnimFile;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportBlocksFile;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportBlockset;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportLayout;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportLayoutFile;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportMapEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportPalette;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportPaletteEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportPaletteEntries1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTileset1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTileset2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTileset3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTileset4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTileset5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTilesetEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTilesetEntries2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportTilesets;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImporttilesetEntries;
    private com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon flatOptionPaneWarningIcon1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButtonSharedAnimation;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButtonExportMapEntry;
    private javax.swing.JButton jButtonExportMapFolder;
    private javax.swing.JButton jButtonImportMapEntry;
    private javax.swing.JButton jButtonImportMapFolder;
    private javax.swing.JButton jButtonImportRaw;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinnerExportMapIndex;
    private javax.swing.JSpinner jSpinnerImportMapIndex;
    private javax.swing.JSpinner jSpinnerTilesetId;
    private javax.swing.JSpinner jSpinnerTilesetLength;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel mapAnimationFrameTableModel;
    private com.sfc.sf2.map.layout.gui.MapLayoutPanel mapAnimationLayoutPanel;
    private com.sfc.sf2.map.animation.gui.MapLayoutAnimViewPanel mapLayoutAnimViewPanel1;
    private com.sfc.sf2.core.gui.controls.Table tableAnimFrames;
    private com.sfc.sf2.map.animation.gui.TilesetAnimViewPanel tilesetAnimViewPanel1;
    private com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel tilesetLayoutPanelAnim;
    private com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel tilesetLayoutPanelModified;
    // End of variables declaration//GEN-END:variables
}
