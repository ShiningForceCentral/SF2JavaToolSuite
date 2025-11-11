/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.animation.gui;

import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import com.sfc.sf2.map.animation.MapAnimationManager;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import com.sfc.sf2.map.settings.MapBlockSettings;
import javax.swing.JCheckBox;

/**
 *
 * @author TiMMy
 */
public class MapAnimationMainEditor extends AbstractMainEditor {
    
    private final MapBlockSettings mapLayoutSettings = new MapBlockSettings();
    MapAnimationManager mapAnimationManager = new MapAnimationManager();
    
    public MapAnimationMainEditor() {
        super();
        SettingsManager.registerSettingsStore("mapLayout", mapLayoutSettings);
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        accordionPanel1.setExpanded(false);
        accordionPanel2.setExpanded(false);
        
        colorPicker1.setColor(SettingsManager.getGlobalSettings().getTransparentBGColor());
        colorPickerTileset.setColor(mapLayoutSettings.getTilesetBGColor());
        jComboBox4.setSelectedIndex(mapLayoutSettings.getTilesetScale()-1);
        
        mapAnimationLayoutPanel.setShowGrid(jCheckBox5.isSelected());
        mapAnimationLayoutPanel.setDisplayScale(jComboBox9.getSelectedIndex()+1);
        mapAnimationLayoutPanel.setBGColor(colorPicker1.getColor());
        mapAnimationLayoutPanel.setShowPriority(jCheckBox6.isSelected());
        mapAnimationLayoutPanel.setShowExplorationFlags(jCheckBox3.isSelected());
        mapAnimationLayoutPanel.setShowInteractionFlags(jCheckBox4.isSelected());
        
        tilesetLayoutPanelAnim.setBGColor(colorPickerTileset.getColor());
        tilesetLayoutPanelAnim.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelAnim.setDisplayScale(jComboBox4.getSelectedIndex()+1);
        tilesetLayoutPanelAnim.setItemsPerRow((int)jSpinner6.getValue());
        tilesetLayoutPanelAnim.setShowAnimationFrames(jCheckBox7.isSelected());
        tilesetLayoutPanelModified.setBGColor(colorPickerTileset.getColor());
        tilesetLayoutPanelModified.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelModified.setDisplayScale(jComboBox4.getSelectedIndex()+1);
        tilesetLayoutPanelModified.setItemsPerRow((int)jSpinner6.getValue());
        tilesetLayoutPanelModified.setShowAnimationFrames(jCheckBox7.isSelected());
        
        tilesetLayoutPanelModified.getAnimator().addAnimationListener(this::onAnimationUpdated);
        
        tableAnimFrames.addListSelectionListener(this::onAnimationFramesSelectionChanged);
        tableAnimFrames.addTableModelListener(this::onAnimationFramesDataChanged);
        tableAnimFrames.jTable.getColumnModel().getColumn(0).setMaxWidth(30);
        
        infoButtonSharedAnimation.setVisible(false);
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        
        mapAnimationLayoutPanel.setMapLayout(mapAnimationManager.getMapLayout());
        
        MapAnimation animation = mapAnimationManager.getMapAnimation();
        if (animation != null) {
            tilesetLayoutPanelAnim.setMapAnimation(animation);
            tilesetLayoutPanelModified.setMapAnimation(animation);
            if (animation.getFrames().length == 0) {
                tilesetLayoutPanelModified.setSelectedTileset(-1);
            } else {
                tilesetLayoutPanelModified.setSelectedTileset(animation.getFrames()[0].getDestTileset());
            }
            tilesetLayoutPanelModified.getAnimator().stopAnimation();
            jCheckBox8.setSelected(false);
            jCheckBox9.setSelected(false);
            jSpinner2.setValue(animation.getTilesetId());
            jSpinner3.setValue(animation.getLength());

            tableAnimFrames.jTable.clearSelection();
            mapAnimationFrameTableModel.setTableData(animation.getFrames());
        }
        
        String sharedAnimationInfo = mapAnimationManager.getSharedAnimationInfo();
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
        fileButton1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton3 = new com.sfc.sf2.core.gui.controls.FileButton();
        directoryButton1 = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jButton30 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jButton31 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton32 = new javax.swing.JButton();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel24 = new javax.swing.JPanel();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton13 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton14 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton19 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton20 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton21 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton22 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel27 = new javax.swing.JPanel();
        directoryButton2 = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jLabel17 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        directoryButton4 = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jLabel19 = new javax.swing.JLabel();
        jButton34 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        fileButton8 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton9 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton10 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton11 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton12 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton15 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton16 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton17 = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel2 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        fileButton18 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton23 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        infoButtonSharedAnimation = new com.sfc.sf2.core.gui.controls.InfoButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tilesetLayoutPanelAnim = new com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel();
        tableAnimFrames = new com.sfc.sf2.core.gui.controls.Table();
        jScrollPane8 = new javax.swing.JScrollPane();
        tilesetLayoutPanelModified = new com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jCheckBox2 = new javax.swing.JCheckBox();
        colorPickerTileset = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mapAnimationLayoutPanel = new com.sfc.sf2.map.layout.gui.MapLayoutPanel();
        jPanel25 = new javax.swing.JPanel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jCheckBox5 = new javax.swing.JCheckBox();
        colorPicker1 = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel59 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
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

        fileButton1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButton1.setInfoMessage("");
        fileButton1.setLabelText("Palette entries :");

        fileButton2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButton2.setInfoMessage("");
        fileButton2.setLabelText("Tilesets entries :");

        fileButton3.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton3.setFilePath(".\\entries.asm");
        fileButton3.setInfoMessage("");
        fileButton3.setLabelText("Map entries :");

        directoryButton1.setDirectoryPath(".\\entries\\");
            directoryButton1.setInfoMessage("");
            directoryButton1.setLabelText("Maps dir :");

            javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
            accordionPanel2.setLayout(accordionPanel2Layout);
            accordionPanel2Layout.setHorizontalGroup(
                accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fileButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(directoryButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );
            accordionPanel2Layout.setVerticalGroup(
                accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(fileButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(directoryButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

            jLabel10.setText("<html>Select map number to import from map entries.</html>");
            jLabel10.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jSpinner4.setModel(new javax.swing.SpinnerNumberModel(3, 0, 255, 1));

            jButton30.setText("Import");
            jButton30.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton30ActionPerformed(evt);
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
                            .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jButton30))
                    .addGap(9, 9, 9)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Export :"));

            jLabel16.setText("<html>Select map number to export to files defined in map entries.</html>");
            jLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jSpinner5.setModel(new javax.swing.SpinnerNumberModel(3, 0, 255, 1));

            jButton31.setText("Export");
            jButton31.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton31ActionPerformed(evt);
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
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                        .addGroup(jPanel26Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanel26Layout.setVerticalGroup(
                jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jButton31))
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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 320, Short.MAX_VALUE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Entries", jPanel18);

            accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette, tilesets, & map data"));

            fileButton13.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButton13.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
            fileButton13.setInfoMessage("");
            fileButton13.setLabelText("Palette entries :");

            fileButton14.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButton14.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
            fileButton14.setInfoMessage("");
            fileButton14.setLabelText("Tilesets entries :");

            fileButton19.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButton19.setFilePath("00-tilesets.asm");
            fileButton19.setInfoMessage("");
            fileButton19.setLabelText("Map tilesets :");

            fileButton20.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
            fileButton20.setFilePath("0-blocks.bin");
            fileButton20.setInfoMessage("");
            fileButton20.setLabelText("Map blockset :");
            fileButton20.setToolTipText("");

            fileButton21.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
            fileButton21.setFilePath("1-layout.bin");
            fileButton21.setInfoMessage("");
            fileButton21.setLabelText("Map layout :");
            fileButton21.setToolTipText("");

            fileButton22.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
            fileButton22.setFilePath("9-animations.asm");
            fileButton22.setInfoMessage("");
            fileButton22.setLabelText("Map anim :");
            fileButton22.setToolTipText("");

            javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
            accordionPanel1.setLayout(accordionPanel1Layout);
            accordionPanel1Layout.setHorizontalGroup(
                accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fileButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                        .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(fileButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );
            accordionPanel1Layout.setVerticalGroup(
                accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(accordionPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(fileButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

            directoryButton2.setDirectoryPath(".\\entries\\map03\\");
                directoryButton2.setLabelText("Map dir :");

                jLabel17.setText("<html>Select map directory to load map from.</html>");
                jLabel17.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                jButton33.setText("Import");
                jButton33.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton33ActionPerformed(evt);
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
                            .addComponent(directoryButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel27Layout.setVerticalGroup(
                    jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(directoryButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton33)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Export :"));

                directoryButton4.setDirectoryPath(".\\entries\\map03\\");
                    directoryButton4.setLabelText("Map dir :");

                    jLabel19.setText("<html>Select map directory to save map data to</html>");
                    jLabel19.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                    jButton34.setText("Export");
                    jButton34.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton34ActionPerformed(evt);
                        }
                    });

                    javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
                    jPanel28.setLayout(jPanel28Layout);
                    jPanel28Layout.setHorizontalGroup(
                        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(directoryButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel28Layout.createSequentialGroup()
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                    );
                    jPanel28Layout.setVerticalGroup(
                        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(directoryButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton34))
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
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                            .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                    );

                    jTabbedPane1.addTab("Map folder", jPanel24);

                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

                    fileButton8.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton8.setFilePath("..\\graphics\\maps\\mappalettes\\mappalette00.bin");
                    fileButton8.setInfoMessage("");
                    fileButton8.setLabelText("Palette :");

                    fileButton9.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton9.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset000.bin");
                    fileButton9.setInfoMessage("");
                    fileButton9.setLabelText("Tileset 1 :");

                    fileButton10.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton10.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset037.bin");
                    fileButton10.setInfoMessage("");
                    fileButton10.setLabelText("Tileset 2 :");

                    fileButton11.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton11.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset043.bin");
                    fileButton11.setInfoMessage("");
                    fileButton11.setLabelText("Tileset 3 :");

                    fileButton12.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton12.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset053.bin");
                    fileButton12.setInfoMessage("");
                    fileButton12.setLabelText("Tileset 4 :");

                    fileButton15.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton15.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset066.bin");
                    fileButton15.setInfoMessage("");
                    fileButton15.setLabelText("Tileset 5 :");

                    fileButton16.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton16.setFilePath(".\\entries\\map03\\0-blocks.bin");
                    fileButton16.setInfoMessage("");
                    fileButton16.setLabelText("Blocks file :");

                    fileButton17.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                    fileButton17.setFilePath(".\\entries\\map03\\1-layout.bin");
                    fileButton17.setInfoMessage("");
                    fileButton17.setLabelText("Layout file :");
                    fileButton17.setToolTipText("");

                    jLabel2.setText("<html>Select individual disassembly files.</html>");
                    jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                    jButton18.setText("Import");
                    jButton18.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jButton18ActionPerformed(evt);
                        }
                    });

                    fileButton18.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                    fileButton18.setFilePath(".\\entries\\map03\\9-animations.asm");
                    fileButton18.setInfoMessage("");
                    fileButton18.setLabelText("Animation file :");
                    fileButton18.setToolTipText("");

                    fileButton23.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                    fileButton23.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
                    fileButton23.setInfoMessage("");
                    fileButton23.setLabelText("Tilesets entries :");

                    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                    jPanel4.setLayout(jPanel4Layout);
                    jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButton18))
                                .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(fileButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(fileButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(4, 4, 4)
                            .addComponent(fileButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton18))
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
                            .addContainerGap(257, Short.MAX_VALUE))
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

                    jSpinner2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 256, 1));
                    jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(javax.swing.event.ChangeEvent evt) {
                            jSpinner2StateChanged(evt);
                        }
                    });

                    jLabel9.setText("Tileset length :");

                    jSpinner3.setModel(new javax.swing.SpinnerNumberModel(0, 0, 128, 1));
                    jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(javax.swing.event.ChangeEvent evt) {
                            jSpinner3StateChanged(evt);
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
                            .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(infoButtonSharedAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                    );

                    jScrollPane9.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Animation Tileset"));
                    jScrollPane9.setPreferredSize(new java.awt.Dimension(335, 154));

                    javax.swing.GroupLayout tilesetLayoutPanelAnimLayout = new javax.swing.GroupLayout(tilesetLayoutPanelAnim);
                    tilesetLayoutPanelAnim.setLayout(tilesetLayoutPanelAnimLayout);
                    tilesetLayoutPanelAnimLayout.setHorizontalGroup(
                        tilesetLayoutPanelAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 367, Short.MAX_VALUE)
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
                        .addGap(0, 367, Short.MAX_VALUE)
                    );
                    tilesetLayoutPanelModifiedLayout.setVerticalGroup(
                        tilesetLayoutPanelModifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 125, Short.MAX_VALUE)
                    );

                    jScrollPane8.setViewportView(tilesetLayoutPanelModified);

                    jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Tileset display"));
                    jPanel20.setMinimumSize(new java.awt.Dimension(340, 100));

                    jLabel15.setText("Scale :");

                    jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
                    jComboBox4.setSelectedIndex(1);
                    jComboBox4.addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(java.awt.event.ItemEvent evt) {
                            jComboBox4ItemStateChanged(evt);
                        }
                    });

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

                    jLabel12.setText("BG :");

                    jLabel21.setText("Tiles per row :");

                    jSpinner6.setModel(new javax.swing.SpinnerNumberModel(16, 4, 32, 4));
                    jSpinner6.addChangeListener(new javax.swing.event.ChangeListener() {
                        public void stateChanged(javax.swing.event.ChangeEvent evt) {
                            jSpinner6StateChanged(evt);
                        }
                    });

                    jCheckBox7.setSelected(true);
                    jCheckBox7.setText("Show anim frames");
                    jCheckBox7.addItemListener(new java.awt.event.ItemListener() {
                        public void itemStateChanged(java.awt.event.ItemEvent evt) {
                            jCheckBox7ItemStateChanged(evt);
                        }
                    });

                    jCheckBox8.setText("Preview anim");
                    jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            animationActionPerformed(evt);
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
                                    .addComponent(jCheckBox2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel21)
                                    .addGap(0, 0, 0)
                                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel20Layout.createSequentialGroup()
                                    .addComponent(jCheckBox7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCheckBox8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                    );
                    jPanel20Layout.setVerticalGroup(
                        jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21)
                                .addComponent(jCheckBox2)
                                .addComponent(jLabel15)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jCheckBox7)
                                .addComponent(jCheckBox8)
                                .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))
                            .addContainerGap())
                    );

                    javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                    jPanel11.setLayout(jPanel11Layout);
                    jPanel11Layout.setHorizontalGroup(
                        jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tableAnimFrames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    );

                    jSplitPane4.setRightComponent(jPanel11);

                    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
                    jPanel8.setLayout(jPanel8Layout);
                    jPanel8Layout.setHorizontalGroup(
                        jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
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
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    );
                    jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
                    );

                    jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Map view"));

                    jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
                    jComboBox9.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jComboBox9ActionPerformed(evt);
                        }
                    });

                    jLabel11.setText("Scale :");

                    jCheckBox5.setText("Show grid");
                    jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jCheckBox5ActionPerformed(evt);
                        }
                    });

                    colorPicker1.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
                        public void colorChanged(java.awt.event.ActionEvent evt) {
                            colorPicker1ColorChanged(evt);
                        }
                    });

                    javax.swing.GroupLayout colorPicker1Layout = new javax.swing.GroupLayout(colorPicker1);
                    colorPicker1.setLayout(colorPicker1Layout);
                    colorPicker1Layout.setHorizontalGroup(
                        colorPicker1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 22, Short.MAX_VALUE)
                    );
                    colorPicker1Layout.setVerticalGroup(
                        colorPicker1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 22, Short.MAX_VALUE)
                    );

                    jLabel59.setText("BG :");

                    jCheckBox3.setText("Exploration flags");
                    jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jCheckBox3ActionPerformed(evt);
                        }
                    });

                    jCheckBox4.setText("Interaction flags");
                    jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jCheckBox4ActionPerformed(evt);
                        }
                    });

                    jCheckBox6.setText("Priority");
                    jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            jCheckBox6ActionPerformed(evt);
                        }
                    });

                    jCheckBox9.setText("Preview anim");
                    jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            animationActionPerformed(evt);
                        }
                    });

                    javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
                    jPanel25.setLayout(jPanel25Layout);
                    jPanel25Layout.setHorizontalGroup(
                        jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel25Layout.createSequentialGroup()
                                    .addComponent(jCheckBox3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel59)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCheckBox5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel25Layout.createSequentialGroup()
                                    .addComponent(jCheckBox4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCheckBox6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCheckBox9)
                                    .addGap(0, 0, Short.MAX_VALUE)))
                            .addContainerGap())
                    );
                    jPanel25Layout.setVerticalGroup(
                        jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel25Layout.createSequentialGroup()
                            .addGap(0, 0, 0)
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel11)
                                .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCheckBox5)
                                .addComponent(jLabel59)
                                .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCheckBox3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jCheckBox4)
                                .addComponent(jCheckBox6)
                                .addComponent(jCheckBox9))
                            .addContainerGap())
                    );

                    javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                    jPanel10.setLayout(jPanel10Layout);
                    jPanel10Layout.setHorizontalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
                    );
                    jPanel10Layout.setVerticalGroup(
                        jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    );

                    jSplitPane2.setRightComponent(jPanel10);

                    javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
                    jPanel15.setLayout(jPanel15Layout);
                    jPanel15Layout.setHorizontalGroup(
                        jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
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

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        int value = (int)jSpinner3.getValue();
        MapAnimation anim = tilesetLayoutPanelModified.getMapAnimation();
        if (anim != null && anim.getLength()!= value) {
            anim.setLength(value);
        }
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        int value = (int)jSpinner2.getValue();
        MapAnimation anim = tilesetLayoutPanelModified.getMapAnimation();
        if (anim != null && anim.getTilesetId()!= value) {
            try {
                anim.setTilesetId(value);
                Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
                Tileset tileset = mapAnimationManager.importAnimationTileset(mapAnimationLayoutPanel.getMapLayout().getPalette(), tilesetEntriesPath, value);
                tilesetLayoutPanelAnim.setTileset(tileset);
            } catch (Exception ex) {
                Console.logger().log(Level.SEVERE, null, ex);
                Console.logger().severe("ERROR Tileset could not be imported for tileset : " + value);
            }
        }
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        mapAnimationLayoutPanel.setDisplayScale(jComboBox9.getSelectedIndex()+1);
        mapLayoutSettings.setTilesetScale(jComboBox9.getSelectedIndex()+1);
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        mapAnimationLayoutPanel.setShowGrid(jCheckBox5.isSelected());
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void colorPicker1ColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPicker1ColorChanged
        mapAnimationLayoutPanel.setBGColor(colorPicker1.getColor());
        mapLayoutSettings.setTilesetBGColor(colorPicker1.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPicker1ColorChanged

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        mapAnimationLayoutPanel.setShowExplorationFlags(jCheckBox3.isSelected());
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        mapAnimationLayoutPanel.setShowInteractionFlags(jCheckBox4.isSelected());
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        mapAnimationLayoutPanel.setShowPriority(jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        if (jComboBox4.getSelectedIndex() >= 0 && tilesetLayoutPanelModified != null) {
            int scale = (int)jComboBox4.getSelectedIndex()+1;
            if (scale != mapLayoutSettings.getTilesetScale()) {
                tilesetLayoutPanelAnim.setDisplayScale(scale);
                tilesetLayoutPanelModified.setDisplayScale(scale);
                mapLayoutSettings.setTilesetScale(scale);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        tilesetLayoutPanelAnim.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelModified.setShowGrid(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

    private void colorPickerTilesetColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerTilesetColorChanged
        tilesetLayoutPanelAnim.setBGColor(colorPickerTileset.getColor());
        tilesetLayoutPanelModified.setBGColor(colorPickerTileset.getColor());
        mapLayoutSettings.setTilesetBGColor(colorPickerTileset.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPickerTilesetColorChanged

    private void jSpinner6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner6StateChanged
        int tilesPerRow = (int)jSpinner6.getValue();
        tilesetLayoutPanelAnim.setItemsPerRow(tilesPerRow);
        tilesetLayoutPanelModified.setItemsPerRow(tilesPerRow);
    }//GEN-LAST:event_jSpinner6StateChanged

    private void jCheckBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox7ItemStateChanged
        tilesetLayoutPanelAnim.setShowAnimationFrames(jCheckBox7.isSelected());
        tilesetLayoutPanelModified.setShowAnimationFrames(jCheckBox7.isSelected());
    }//GEN-LAST:event_jCheckBox7ItemStateChanged

    private void animationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animationActionPerformed
        boolean isSelected = ((JCheckBox)evt.getSource()).isSelected();
        jCheckBox8.setSelected(isSelected);
        jCheckBox9.setSelected(isSelected);
        tilesetLayoutPanelModified.setPreviewAnim(isSelected);
    }//GEN-LAST:event_animationActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton3.getFilePath());
        int mapId = (int)jSpinner4.getValue();
        try {
            mapAnimationManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
            jSpinner5.setValue(jSpinner4.getValue());
        } catch (Exception ex) {
            mapAnimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation be imported for map : " + mapId);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton3.getFilePath());
        int mapId = (int)jSpinner5.getValue();
        try {
            mapAnimationManager.exportDisassemblyFromMapEntries(mapEntriesPath, mapId, tilesetLayoutPanelModified.getMapAnimation());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be exported to : " + mapId);
        }
    }//GEN-LAST:event_jButton31ActionPerformed

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

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButton2.getDirectoryPath());
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton13.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton14.getFilePath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButton19.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButton20.getFilePath());
        Path mapLayoutDataPath = mapDirectory.resolve(fileButton21.getFilePath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButton22.getFilePath());
        int mapId = (int)jSpinner4.getValue();
        try {
            mapAnimationManager.importDisassemblyFromMapData(paletteEntriesPath, tilesetEntriesPath, mapTilesetDataPath, mapBlocksetDataPath, mapLayoutDataPath, mapAnimationDataPath);
            directoryButton4.setDirectoryPath(directoryButton4.getDirectoryPath());
        } catch (Exception ex) {
            mapAnimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be imported from : " + mapAnimationDataPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButton4.getDirectoryPath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButton22.getFilePath());
        if (!PathHelpers.createPathIfRequred(mapDirectory)) return;
        try {
            mapAnimationManager.exportDisassembly(mapAnimationDataPath, tilesetLayoutPanelModified.getMapAnimation());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be exported to : " + mapAnimationDataPath);
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        Path palettePath = PathHelpers.getBasePath().resolve(fileButton8.getFilePath());
        Path tilesetPath1 = PathHelpers.getBasePath().resolve(fileButton9.getFilePath());
        Path tilesetPath2 = PathHelpers.getBasePath().resolve(fileButton10.getFilePath());
        Path tilesetPath3 = PathHelpers.getBasePath().resolve(fileButton11.getFilePath());
        Path tilesetPath4 = PathHelpers.getBasePath().resolve(fileButton12.getFilePath());
        Path tilesetPath5 = PathHelpers.getBasePath().resolve(fileButton15.getFilePath());
        Path blocksPath = PathHelpers.getBasePath().resolve(fileButton16.getFilePath());
        Path layoutPath = PathHelpers.getBasePath().resolve(fileButton17.getFilePath());
        Path animationPath = PathHelpers.getBasePath().resolve(fileButton18.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton23.getFilePath());
        try {
            mapAnimationManager.importDisassemblyFromRawFiles(palettePath, new Path[] { tilesetPath1, tilesetPath2, tilesetPath3, tilesetPath4, tilesetPath5 }, blocksPath, layoutPath, animationPath, tilesetEntriesPath);
        } catch (Exception ex) {
            mapAnimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map animation disasm could not be imported from : " + animationPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton18ActionPerformed
    
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
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPicker1;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerTileset;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButton1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButton2;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButton4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton10;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton11;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton12;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton13;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton14;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton15;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton16;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton17;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton18;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton19;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton20;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton21;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton22;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton23;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton8;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton9;
    private com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon flatOptionPaneWarningIcon1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButtonSharedAnimation;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel59;
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
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
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
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel mapAnimationFrameTableModel;
    private com.sfc.sf2.map.layout.gui.MapLayoutPanel mapAnimationLayoutPanel;
    private com.sfc.sf2.core.gui.controls.Table tableAnimFrames;
    private com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel tilesetLayoutPanelAnim;
    private com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel tilesetLayoutPanelModified;
    // End of variables declaration//GEN-END:variables
}
