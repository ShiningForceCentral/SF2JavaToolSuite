/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.core.models.combobox.ComboBoxTableEditor;
import com.sfc.sf2.core.models.combobox.ComboBoxTableRenderer;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapCopyEvent;
import com.sfc.sf2.map.MapEnums;
import com.sfc.sf2.map.MapFlagCopyEvent;
import com.sfc.sf2.map.MapItem;
import com.sfc.sf2.map.MapManager;
import com.sfc.sf2.map.MapWarpEvent;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.gui.EditableBlockSlotPanel;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import com.sfc.sf2.map.settings.MapBlockSettings;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;

/**
 *
 * @author wiz
 */
public class MapEditorMainEditor extends AbstractMainEditor {
    
    private final MapBlockSettings mapLayoutSettings = new MapBlockSettings();
    MapManager mapManager = new MapManager();
    
    JCheckBox tabRelativeCheckbox;
    boolean tabRelativeCheckboxState;
    JCheckBox actionRelativeCheckbox;
    boolean actionRelativeCheckboxState;
    
    public MapEditorMainEditor() {
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
        
        infoButtonSharedBlocks.setVisible(false);
        infoButtonSharedAnimation.setVisible(false);
        
        colorPicker1.setColor(SettingsManager.getGlobalSettings().getTransparentBGColor());
        colorPickerTileset.setColor(mapLayoutSettings.getTilesetBGColor());
        colorPickerTilesetAnim.setColor(mapLayoutSettings.getTilesetBGColor());
        jComboBox5.setSelectedIndex(mapLayoutSettings.getTilesetScale()-1);
        
        //Map editing
        mapLayoutPanel.setShowGrid(jCheckBox10.isSelected());
        mapLayoutPanel.setDisplayScale(jComboBox9.getSelectedIndex()+1);
        mapLayoutPanel.setBGColor(colorPicker1.getColor());
        mapLayoutPanel.setShowPriority(jCheckBox13.isSelected());
        mapLayoutPanel.setShowExplorationFlags(jCheckBox11.isSelected());
        mapLayoutPanel.setShowInteractionFlags(false);
        mapLayoutPanel.setDrawMode_Tabs(MapLayoutPanel.DRAW_MODE_NONE);
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ALL, false);
        
        mapLayoutPanel.setShowAreasOverlay(jCheckBox1.isSelected());
        mapLayoutPanel.setShowAreasUnderlay(jCheckBox4.isSelected());
        mapLayoutPanel.setShowFlagCopyResult(jCheckBox6.isSelected());
        mapLayoutPanel.setShowStepCopyResult(jCheckBox12.isSelected());
        mapLayoutPanel.setShowRoofCopyResult(jCheckBox14.isSelected());
        mapLayoutPanel.setEventEditedListener(this::onMapEventChanged);
        
        //Blockset panel
        mapBlocksetLayoutPanel.setShowGrid(jCheckBox3.isSelected());
        mapBlocksetLayoutPanel.setDisplayScale(jComboBox3.getSelectedIndex()+1);
        mapBlocksetLayoutPanel.setBGColor(colorPickerBlockset.getColor());
        mapBlocksetLayoutPanel.setShowPriority(jCheckBox5.isSelected());
        mapBlocksetLayoutPanel.setItemsPerRow((int)jSpinner7.getValue());
        
        mapLayoutPanel.setLeftSlot(blockSlotPanelLeft);
        mapLayoutPanel.setMapBlockLayoutPanel(mapBlocksetLayoutPanel);
        mapBlocksetLayoutPanel.setLeftSlotBlockPanel(blockSlotPanelLeft);
        mapBlocksetLayoutPanel.setRightSlotBlockPanel(blockSlotPanelRight);
        mapBlocksetLayoutPanel.setCanSelectInitialBlocks(true);
        mapBlocksetLayoutPanel.setLeftSlotColor(Color.YELLOW);
        mapBlocksetLayoutPanel.setRightSlotColor(Color.MAGENTA);
        blockSlotPanelLeft.setBlockChangedListener(this::onLeftBlockSlotChanged);
        blockSlotPanelRight.setBlockChangedListener(this::onRightBlockSlotChanged);
        
        //Tilesets editing
        tilesetsLayoutPanel.setBGColor(colorPickerTilesetAnim.getColor());
        tilesetsLayoutPanel.setShowGrid(jCheckBox23.isSelected());
        tilesetsLayoutPanel.setDisplayScale(jComboBox5.getSelectedIndex()+1);
        
        editableBlockSlotPanel.setShowGrid(jCheckBox24.isSelected());
        editableBlockSlotPanel.setBGColor(colorPickerBlocks.getColor());
        editableBlockSlotPanel.setShowPriority(jCheckBox25.isSelected());
        
        editableBlockSlotPanel.setBlockEditedListener(this::onBlockEdited);
        editableBlockSlotPanel.setLeftTileSlotPanel(tileSlotPanelLeft);
        editableBlockSlotPanel.setRightTileSlotPanel(tileSlotPanelRight);
        mapBlocksetLayoutPanel.setEditableBlockPanel(editableBlockSlotPanel);
        tilesetsLayoutPanel.setLeftSlotTilePanel(tileSlotPanelLeft);
        tilesetsLayoutPanel.setRightSlotBlockPanel(tileSlotPanelRight);
        tilesetsLayoutPanel.setBlockSlotPanel(editableBlockSlotPanel);
        
        //Animation
        tilesetLayoutPanelAnim.setBGColor(colorPickerTilesetAnim.getColor());
        tilesetLayoutPanelAnim.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelAnim.setDisplayScale(jComboBox4.getSelectedIndex()+1);
        tilesetLayoutPanelAnim.setItemsPerRow((int)jSpinner6.getValue());
        tilesetLayoutPanelAnim.setShowAnimationFrames(jCheckBox7.isSelected());
        tilesetLayoutPanelModified.setBGColor(colorPickerTilesetAnim.getColor());
        tilesetLayoutPanelModified.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelModified.setDisplayScale(jComboBox4.getSelectedIndex()+1);
        tilesetLayoutPanelModified.setItemsPerRow((int)jSpinner6.getValue());
        tilesetLayoutPanelModified.setShowAnimationFrames(jCheckBox7.isSelected());
        
        tilesetLayoutPanelModified.getAnimator().addAnimationListener(this::onAnimationUpdated);
        
        tableAnimFrames.addListSelectionListener(this::onAnimationFramesSelectionChanged);
        tableAnimFrames.addTableModelListener(this::onAnimationFramesDataChanged);
        tableAnimFrames.jTable.getColumnModel().getColumn(0).setMaxWidth(30);
        
        //Tables
        tableAreas.setMinColumnWidth(0, 40);
        tableAreas.setMaxColumnWidth(0, 40);
        tableAreas.setMinColumnWidth(18, 100);
        tableAreas.jTable.setDefaultRenderer(String.class, new ComboBoxTableRenderer());
        tableAreas.jTable.setDefaultEditor(String.class, new ComboBoxTableEditor());
        tableFlagCopies.setMaxColumnWidth(0, 40);
        tableStepCopies.setMaxColumnWidth(0, 40);
        tableRoofCopies.setMaxColumnWidth(0, 40);
        tableWarps.setMaxColumnWidth(0, 40);
        tableWarps.setMinColumnWidth(4, 100);
        tableWarps.jTable.getColumnModel().getColumn(3).setCellRenderer(new ComboBoxTableRenderer());
        tableWarps.jTable.getColumnModel().getColumn(3).setCellEditor(new ComboBoxTableEditor());
        tableWarps.jTable.getColumnModel().getColumn(4).setCellRenderer(new ComboBoxTableRenderer());
        tableWarps.jTable.getColumnModel().getColumn(4).setCellEditor(new ComboBoxTableEditor());
        tableWarps.jTable.getColumnModel().getColumn(7).setCellRenderer(new ComboBoxTableRenderer());
        tableWarps.jTable.getColumnModel().getColumn(7).setCellEditor(new ComboBoxTableEditor());
        tableChestItems.setMaxColumnWidth(0, 40);
        tableChestItems.setMaxColumnWidth(0, 40);
        tableChestItems.jTable.getColumnModel().getColumn(5).setCellRenderer(new ComboBoxTableRenderer());
        tableChestItems.jTable.getColumnModel().getColumn(5).setCellEditor(new ComboBoxTableEditor());
        tableOtherItems.setMaxColumnWidth(0, 40);
        tableOtherItems.jTable.getColumnModel().getColumn(5).setCellRenderer(new ComboBoxTableRenderer());
        tableOtherItems.jTable.getColumnModel().getColumn(5).setCellEditor(new ComboBoxTableEditor());
        
        //Data
        tableAreas.addTableModelListener(this::OnAreasTableDataChanged);
        tableFlagCopies.addTableModelListener(this::OnFlagCopiesTableDataChanged);
        tableStepCopies.addTableModelListener(this::OnStepCopiesTableDataChanged);
        tableRoofCopies.addTableModelListener(this::OnRoofCopiesTableDataChanged);
        tableWarps.addTableModelListener(this::OnWarpsTableDataChanged);
        tableChestItems.addTableModelListener(this::OnChestItemsTableDataChanged);
        tableOtherItems.addTableModelListener(this::OnOtherItemsTableDataChanged);
        tableAreas.addListSelectionListener(this::OnTableSelectionChanged);
        tableFlagCopies.addListSelectionListener(this::OnTableSelectionChanged);
        tableStepCopies.addListSelectionListener(this::OnTableSelectionChanged);
        tableRoofCopies.addListSelectionListener(this::OnTableSelectionChanged);
        tableWarps.addListSelectionListener(this::OnTableSelectionChanged);
        tableChestItems.addListSelectionListener(this::OnTableSelectionChanged);
        tableOtherItems.addListSelectionListener(this::OnTableSelectionChanged);
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        
        Map map = mapManager.getMap();        
        mapLayoutPanel.setMap(map);
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_EXPLORATION_FLAGS, jCheckBox11.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_GRID, jCheckBox10.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_AREAS, jCheckBox15.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_FLAG_COPIES, jCheckBox16.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_STEP_COPIES, jCheckBox17.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ROOF_COPIES, jCheckBox18.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_WARPS, jCheckBox19.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_CHEST_ITEMS, jCheckBox20.isSelected());
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_TRIGGERS, jCheckBox21.isSelected());
                
        if (map != null) {
            MapEnums mapEnums = mapManager.getMapEnums();
            mapAreaTableModel.setEnums(mapEnums);
            mapWarpTableModel.setEnums(mapEnums);
            mapChestItemTableModel.setEnums(mapEnums);
            mapOtherItemTableModel.setEnums(mapEnums);
            
            mapAreaTableModel.setTableData(map.getAreas());
            mapFlagCopyTableModel.setTableData(map.getFlagCopies());
            mapStepCopyTableModel.setTableData(map.getStepCopies());
            mapRoofCopyTableModel.setTableData(map.getRoofCopies());
            mapWarpTableModel.setTableData(map.getWarps());
            mapChestItemTableModel.setTableData(map.getChestItems());
            mapOtherItemTableModel.setTableData(map.getOtherItems());
            
            MapBlockset mapBlockset = map.getBlockset();
            Tileset[] tilesets = map.getLayout().getTilesets();
            mapBlocksetLayoutPanel.setBlockset(mapBlockset);
            mapBlocksetLayoutPanel.setTilesets(tilesets);
            mapBlocksetLayoutPanel.setLeftSelectedIndex(-1);
            if (mapBlockset != null && tilesets != null) {
                tilesetsLayoutPanel.setTilesets(tilesets);
                String[] tilesetNames = new String[tilesets.length];
                for (int i = 0; i < tilesets.length; i++) {
                    if (tilesets[i] == null) {
                        tilesetNames[i] = "NONE";
                    } else {
                        tilesetNames[i] = tilesets[i].getName();
                    }
                }
                jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(tilesetNames));
                jComboBox6.setSelectedIndex(0);

                tilesetsLayoutPanel.setItemsPerRow((int)jSpinner8.getValue());
                tileSlotPanelLeft.setTile(null);
                tileSlotPanelLeft.setTilesets(tilesets);
                tileSlotPanelRight.setTile(null);
                tileSlotPanelRight.setTilesets(tilesets);
                editableBlockSlotPanel.setTilesets(tilesets);
                blockSlotPanelLeft.setTilesets(tilesets);
                blockSlotPanelRight.setTilesets(tilesets);
                blockSlotPanelLeft.setBlock(mapBlockset.getBlocks()[0]);
                blockSlotPanelRight.setBlock(mapBlockset.getBlocks()[0]);
            }

            MapAnimation animation = map.getAnimation();
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

            String sharedBlockInfo = mapManager.getSharedBlockInfo();
            infoButtonSharedBlocks.setVisible(sharedBlockInfo != null);
            if (sharedBlockInfo != null) {
                infoButtonSharedBlocks.setMessageText("This block and layout data are also used by the following maps:\n" + sharedBlockInfo + "\nAny changes will affect these other maps.\n\nTo unlink the maps, you can export this layout for a specific map and then update \\maps\\entries.asm");
            }
            String sharedAnimationInfo = mapManager.getSharedAnimationInfo();
            infoButtonSharedAnimation.setVisible(sharedAnimationInfo != null);
            if (sharedAnimationInfo != null) {
                infoButtonSharedAnimation.setMessageText("This animation data is also used by the following maps:\n" + sharedAnimationInfo + "\nAny changes will affect these other maps.\n\nTo unlink the maps, you can export this animation for a specific map and then update \\maps\\entries.asm");
            }
        } else {
            mapBlocksetLayoutPanel.setBlockset(null);
                    
            mapAreaTableModel.setTableData(null);
            mapFlagCopyTableModel.setTableData(null);
            mapStepCopyTableModel.setTableData(null);
            mapRoofCopyTableModel.setTableData(null);
            mapWarpTableModel.setTableData(null);
            mapChestItemTableModel.setTableData(null);
            mapOtherItemTableModel.setTableData(null);
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

        buttonGroupMapActions = new javax.swing.ButtonGroup();
        buttonGroupTileEditing = new javax.swing.ButtonGroup();
        mapAreaTableModel = new com.sfc.sf2.map.models.MapAreaTableModel();
        mapFlagCopyTableModel = new com.sfc.sf2.map.models.MapFlagCopyEventTableModel();
        mapChestItemTableModel = new com.sfc.sf2.map.models.MapItemTableModel();
        mapOtherItemTableModel = new com.sfc.sf2.map.models.MapItemTableModel();
        mapRoofCopyTableModel = new com.sfc.sf2.map.models.MapRoofCopyEventTableModel();
        mapStepCopyTableModel = new com.sfc.sf2.map.models.MapStepCopyEventTableModel();
        mapAnimationFrameTableModel = new com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel();
        mapWarpTableModel = new com.sfc.sf2.map.models.MapWarpTableModel();
        flatOptionPaneWarningIcon1 = new com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel9 = new javax.swing.JPanel();
        fileButton20 = new com.sfc.sf2.core.gui.controls.FileButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton14 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel6 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        accordionPanel2 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton5 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton6 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton7 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton8 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton9 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton10 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton11 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton12 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton13 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton31 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton32 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel37 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        infoButton23 = new com.sfc.sf2.core.gui.controls.InfoButton();
        directoryButton2 = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jButton33 = new javax.swing.JButton();
        jPanel39 = new javax.swing.JPanel();
        directoryButton4 = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jLabel24 = new javax.swing.JLabel();
        jButton34 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        fileButton21 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton22 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton23 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton24 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton25 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton26 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton27 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton28 = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel5 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        fileButton29 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton30 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton33 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton34 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton35 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton36 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton37 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton38 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton39 = new com.sfc.sf2.core.gui.controls.FileButton();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel40 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButton15 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton16 = new com.sfc.sf2.core.gui.controls.FileButton();
        jButton4 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        infoButton2 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButton17 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton18 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton19 = new com.sfc.sf2.core.gui.controls.FileButton();
        jButton5 = new javax.swing.JButton();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mapBlocksetLayoutPanel = new com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jSpinner7 = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        colorPickerBlockset = new com.sfc.sf2.core.gui.controls.ColorPicker();
        infoButtonSharedBlocks = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel24 = new javax.swing.JPanel();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        infoButtonSharedAnimation = new com.sfc.sf2.core.gui.controls.InfoButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tilesetLayoutPanelAnim = new com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel();
        tableAnimFrames = new com.sfc.sf2.core.gui.controls.Table();
        jScrollPane11 = new javax.swing.JScrollPane();
        tilesetLayoutPanelModified = new com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jCheckBox2 = new javax.swing.JCheckBox();
        colorPickerTilesetAnim = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel17 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel48 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel41 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mapLayoutPanel = new com.sfc.sf2.map.gui.MapLayoutPanel();
        jPanel33 = new javax.swing.JPanel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jCheckBox10 = new javax.swing.JCheckBox();
        colorPicker1 = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel59 = new javax.swing.JLabel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel12 = new javax.swing.JPanel();
        jPanelAreasDisplay = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jPanelFlagCopiesDisplay = new javax.swing.JPanel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanelStepCopiesDisplay = new javax.swing.JPanel();
        jCheckBox12 = new javax.swing.JCheckBox();
        jPanelRoofCopiesDisplay = new javax.swing.JPanel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jPanel42 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tilesetsLayoutPanel = new com.sfc.sf2.map.block.gui.TilesetsLayoutPanel();
        jPanel44 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jSpinner8 = new javax.swing.JSpinner();
        jLabel26 = new javax.swing.JLabel();
        jCheckBox23 = new javax.swing.JCheckBox();
        colorPickerTileset = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel13 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        colorPickerBlocks = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jLabel14 = new javax.swing.JLabel();
        editableBlockSlotPanel = new com.sfc.sf2.map.block.gui.EditableBlockSlotPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        tileSlotPanelLeft = new com.sfc.sf2.map.block.gui.TileSlotPanel();
        tileSlotPanelRight = new com.sfc.sf2.map.block.gui.TileSlotPanel();
        jRadioButton16 = new javax.swing.JRadioButton();
        jRadioButton17 = new javax.swing.JRadioButton();
        jRadioButton18 = new javax.swing.JRadioButton();
        infoButton18 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton19 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton20 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel20 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        infoButton3 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton4 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel17 = new javax.swing.JPanel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        infoButton10 = new com.sfc.sf2.core.gui.controls.InfoButton();
        blockSlotPanelLeft = new com.sfc.sf2.map.block.gui.BlockSlotPanel();
        blockSlotPanelRight = new com.sfc.sf2.map.block.gui.BlockSlotPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        infoButton5 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        infoButton6 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton14 = new javax.swing.JRadioButton();
        infoButton16 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton19 = new javax.swing.JRadioButton();
        infoButton21 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton20 = new javax.swing.JRadioButton();
        infoButton22 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        infoButton13 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        infoButton14 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton21 = new javax.swing.JRadioButton();
        infoButton24 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton15 = new javax.swing.JRadioButton();
        infoButton17 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel50 = new javax.swing.JPanel();
        jRadioButton7 = new javax.swing.JRadioButton();
        infoButton11 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        infoButton9 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton8 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton7 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        infoButton12 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton15 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButton13 = new javax.swing.JRadioButton();
        jRadioButton10 = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        tableAreas = new com.sfc.sf2.core.gui.controls.Table();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        tableFlagCopies = new com.sfc.sf2.core.gui.controls.Table();
        tableStepCopies = new com.sfc.sf2.core.gui.controls.Table();
        tableRoofCopies = new com.sfc.sf2.core.gui.controls.Table();
        tableWarps = new com.sfc.sf2.core.gui.controls.Table();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        tableChestItems = new com.sfc.sf2.core.gui.controls.Table();
        tableOtherItems = new com.sfc.sf2.core.gui.controls.Table();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2MapEditor");

        jSplitPane1.setDividerLocation(850);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(325);
        jSplitPane2.setOneTouchExpandable(true);
        jSplitPane2.setPreferredSize(new java.awt.Dimension(830, 500));

        fileButton20.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton20.setFilePath("..\\..\\sf2enums.asm");
        fileButton20.setInfoMessage("");
        fileButton20.setLabelText("Sf2enums :");

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette, tilesets, & map data"));

        fileButton1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButton1.setInfoMessage("The entries file for all map palettes.");
        fileButton1.setLabelText("Palette entries :");

        fileButton2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButton2.setInfoMessage("The entries file for all map tilesets.");
        fileButton2.setLabelText("Tileset entries :");

        fileButton14.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton14.setFilePath(".\\entries.asm");
        fileButton14.setInfoMessage("The entries files for all maps.");
        fileButton14.setLabelText("Map entries :");

        javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
        accordionPanel1.setLayout(accordionPanel1Layout);
        accordionPanel1Layout.setHorizontalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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
                .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Import"));

        jButton18.setText("Import");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(3, 0, 100, 1));

        jLabel4.setText("Map :");

        jLabel18.setText("Import map from entries.");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton18))
                    .addComponent(jLabel18))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButton18))
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Export"));

        jLabel2.setText("Export map disassembly files");

        jButton19.setText("Export");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(3, 0, 255, 1));

        jLabel16.setText("Map :");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton19)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jButton19))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accordionPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 438, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Entries", jPanel3);

        accordionPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Map files"));

        fileButton3.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton3.setFilePath("00-tilesets.asm");
        fileButton3.setInfoMessage("The map's tilesets file.");
        fileButton3.setLabelText("Tilesets :");

        fileButton4.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButton4.setFilePath("0-blocks.bin");
        fileButton4.setInfoMessage("The map's blocks file.");
        fileButton4.setLabelText("Blocks :");

        fileButton5.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButton5.setFilePath("1-layout.bin");
        fileButton5.setInfoMessage("The map's layout file.");
        fileButton5.setLabelText("Layout :");

        fileButton6.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton6.setFilePath("2-areas.asm");
        fileButton6.setInfoMessage("The map's areas file.");
        fileButton6.setLabelText("Areas :");

        fileButton7.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton7.setFilePath("3-flag-events.asm");
        fileButton7.setInfoMessage("The map's flag events file.");
        fileButton7.setLabelText("Flag events :");

        fileButton8.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton8.setFilePath("4-step-events.asm");
        fileButton8.setInfoMessage("The map's step events file.");
        fileButton8.setLabelText("Step events :");

        fileButton9.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton9.setFilePath("5-roof-events.asm");
        fileButton9.setInfoMessage("The map's roof events file.");
        fileButton9.setLabelText("Roof events :");

        fileButton10.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton10.setFilePath("6-warp-events.asm");
        fileButton10.setInfoMessage("The map's warp events file.");
        fileButton10.setLabelText("Warps :");

        fileButton11.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton11.setFilePath("7-chest-items.asm");
        fileButton11.setInfoMessage("The map's chest items file.");
        fileButton11.setLabelText("Chest items :");

        fileButton12.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton12.setFilePath("8-other-items.asm");
        fileButton12.setInfoMessage("The map's other items file.");
        fileButton12.setLabelText("Other items :");

        fileButton13.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton13.setFilePath("9-animations.asm");
        fileButton13.setInfoMessage("The map's map animation file.");
        fileButton13.setLabelText("Animations :");

        fileButton31.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton31.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButton31.setInfoMessage("The entries file for all map palettes.");
        fileButton31.setLabelText("Palette entries :");

        fileButton32.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton32.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButton32.setInfoMessage("The entries file for all map tilesets.");
        fileButton32.setLabelText("Tileset entries :");

        javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
        accordionPanel2.setLayout(accordionPanel2Layout);
        accordionPanel2Layout.setHorizontalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton31, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(fileButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel2Layout.setVerticalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

        jLabel23.setText("Import map from directory.");

        infoButton23.setMessageText("<html>Loads the map data by importing the above files.<br>NOTE: May not load shared files, if they are defined in other map folders (load from map entries instead).</html>");
        infoButton23.setText("");

        directoryButton2.setDirectoryPath(".\\entries\\map03\\");
            directoryButton2.setInfoMessage("");
            directoryButton2.setLabelText("Map dir :");

            jButton33.setText("Import");
            jButton33.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton33ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
            jPanel37.setLayout(jPanel37Layout);
            jPanel37Layout.setHorizontalGroup(
                jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel37Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(directoryButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                        .addGroup(jPanel37Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel37Layout.createSequentialGroup()
                            .addComponent(jLabel23)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(infoButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanel37Layout.setVerticalGroup(
                jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(infoButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                    .addComponent(directoryButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton33)
                    .addContainerGap())
            );

            jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder("Export :"));

            directoryButton4.setDirectoryPath(".\\entries\\map03\\");
                directoryButton4.setLabelText("Map dir :");

                jLabel24.setText("<html>Select map directory to save map data to</html>");
                jLabel24.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                jButton34.setText("Export");
                jButton34.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton34ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
                jPanel39.setLayout(jPanel39Layout);
                jPanel39Layout.setHorizontalGroup(
                    jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(directoryButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel39Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel39Layout.setVerticalGroup(
                    jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(directoryButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton34))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
                jPanel18.setLayout(jPanel18Layout);
                jPanel18Layout.setHorizontalGroup(
                    jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(accordionPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel18Layout.setVerticalGroup(
                    jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(accordionPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jTabbedPane1.addTab("Map folder", jPanel18);

                jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

                fileButton21.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton21.setFilePath("..\\graphics\\maps\\mappalettes\\mappalette00.bin");
                fileButton21.setInfoMessage("");
                fileButton21.setLabelText("Palette :");

                fileButton22.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton22.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset000.bin");
                fileButton22.setInfoMessage("");
                fileButton22.setLabelText("Tileset 1 :");

                fileButton23.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton23.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset037.bin");
                fileButton23.setInfoMessage("");
                fileButton23.setLabelText("Tileset 2 :");

                fileButton24.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton24.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset043.bin");
                fileButton24.setInfoMessage("");
                fileButton24.setLabelText("Tileset 3 :");

                fileButton25.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton25.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset053.bin");
                fileButton25.setInfoMessage("");
                fileButton25.setLabelText("Tileset 4 :");

                fileButton26.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton26.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset066.bin");
                fileButton26.setInfoMessage("");
                fileButton26.setLabelText("Tileset 5 :");

                fileButton27.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton27.setFilePath(".\\entries\\map03\\0-blocks.bin");
                fileButton27.setInfoMessage("");
                fileButton27.setLabelText("Blocks file :");

                fileButton28.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButton28.setFilePath(".\\entries\\map03\\1-layout.bin");
                fileButton28.setInfoMessage("");
                fileButton28.setLabelText("Layout file :");
                fileButton28.setToolTipText("");

                jLabel5.setText("<html>Select individual disassembly files.</html>");
                jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                jButton20.setText("Import");
                jButton20.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton20ActionPerformed(evt);
                    }
                });

                fileButton29.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton29.setFilePath(".\\entries\\map03\\9-animations.asm");
                fileButton29.setInfoMessage("");
                fileButton29.setLabelText("Animation file :");
                fileButton29.setToolTipText("");

                fileButton30.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton30.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
                fileButton30.setInfoMessage("");
                fileButton30.setLabelText("Tilesets entries :");

                fileButton33.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton33.setFilePath(".\\entries\\map03\\2-areas.asm");
                fileButton33.setInfoMessage("");
                fileButton33.setLabelText("Areas :");

                fileButton34.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton34.setFilePath(".\\entries\\map03\\3-flag-events.asm");
                fileButton34.setInfoMessage("");
                fileButton34.setLabelText("Flag events :");

                fileButton35.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton35.setFilePath(".\\entries\\map03\\4-step-events.asm");
                fileButton35.setInfoMessage("");
                fileButton35.setLabelText("Step events :");

                fileButton36.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton36.setFilePath(".\\entries\\map03\\5-roof-events.asm");
                fileButton36.setInfoMessage("");
                fileButton36.setLabelText("Roof events :");

                fileButton37.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton37.setFilePath(".\\entries\\map03\\6-warp-events.asm");
                fileButton37.setInfoMessage("");
                fileButton37.setLabelText("Warps :");

                fileButton38.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton38.setFilePath(".\\entries\\map03\\7-chest-items.asm");
                fileButton38.setInfoMessage("");
                fileButton38.setLabelText("Chest items :");

                fileButton39.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButton39.setFilePath(".\\entries\\map03\\8-other-items.asm");
                fileButton39.setInfoMessage("");
                fileButton39.setLabelText("Other items :");

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton20))
                            .addComponent(fileButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSeparator6))
                        .addContainerGap())
                );
                jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fileButton30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(fileButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(fileButton28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton20))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
                jPanel19.setLayout(jPanel19Layout);
                jPanel19Layout.setHorizontalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel19Layout.setVerticalGroup(
                    jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(141, Short.MAX_VALUE))
                );

                jTabbedPane1.addTab("Raw files", jPanel19);

                jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder("Blockset image export"));

                jLabel11.setText("Export blockset data");

                infoButton1.setMessageText("<html><b>Image Format:</b> Select an image File (e.g. PNG or GIF).<br>Color format should be 4BPP / 16 indexed colors.<br>(Images of 8BPP / 256 indexed colors will be converted to 4 BPP / 16). <br><br><b>Priority tiles:</b> Outputs a representation of the map's blockset with 'H' = a high-priority tile, and 'L' = a regular tile (low priority).<br>High priority tells the engine to draw tiles over the sprites (i.e. player character).</html>");
                infoButton1.setText("");

                fileButton15.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ANY_IMAGE);
                fileButton15.setFilePath(".\\export\\blockset.png");
                fileButton15.setInfoMessage("");
                fileButton15.setLabelText("Blockset image :");

                fileButton16.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
                fileButton16.setFilePath(".\\export\\blockset_hptiles.txt");
                fileButton16.setInfoMessage("");
                fileButton16.setLabelText("Blockset priority tiles :");

                jButton4.setText("Export");
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton4ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
                jPanel34.setLayout(jPanel34Layout);
                jPanel34Layout.setHorizontalGroup(
                    jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButton16, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(fileButton15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton4)
                                    .addGroup(jPanel34Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                );
                jPanel34Layout.setVerticalGroup(
                    jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addContainerGap())
                );

                jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder("Map layout image export"));

                jLabel12.setText("Export map layout data");

                infoButton2.setMessageText("<html><b>Image Format:</b> Select an image File (e.g. PNG or GIF).<br>Color format should be 4BPP / 16 indexed colors.<br>(Images of 8BPP / 256 indexed colors will be converted to 4 BPP / 16).<br><br><b>Flags:</b> Outputs a representation of the map containing flag values (i.e. if the tile has an item, warp, etc flag on it).<br><br><b>Priority tiles:</b> Outputs a representation of the map with 'H' = a high-priority tile, and 'L' = a regular tile (low priority).<br>High priority tells the engine to draw tiles over the sprites (i.e. player character).</html>");
                infoButton2.setText("");

                fileButton17.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ANY_IMAGE);
                fileButton17.setFilePath(".\\export\\layout.png");
                fileButton17.setInfoMessage("");
                fileButton17.setLabelText("Layout image :");

                fileButton18.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
                fileButton18.setFilePath(".\\export\\layout_flags.txt");
                fileButton18.setInfoMessage("");
                fileButton18.setLabelText("Layout flags :");

                fileButton19.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
                fileButton19.setFilePath(".\\export\\layout_hptiles.txt");
                fileButton19.setInfoMessage("");
                fileButton19.setLabelText("Layout priority tiles :");

                jButton5.setText("Export");
                jButton5.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton5ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
                jPanel35.setLayout(jPanel35Layout);
                jPanel35Layout.setHorizontalGroup(
                    jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButton19, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(fileButton18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(fileButton17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton5)
                                    .addGroup(jPanel35Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                );
                jPanel35Layout.setVerticalGroup(
                    jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
                jPanel40.setLayout(jPanel40Layout);
                jPanel40Layout.setHorizontalGroup(
                    jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel40Layout.setVerticalGroup(
                    jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(403, Short.MAX_VALUE))
                );

                jTabbedPane1.addTab("Misc.", jPanel40);

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .addComponent(fileButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel9Layout.setVerticalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fileButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1)
                        .addContainerGap())
                );

                jSplitPane2.setLeftComponent(jPanel9);

                jSplitPane3.setDividerLocation(625);
                jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
                jSplitPane3.setResizeWeight(1.0);

                jSplitPane4.setDividerLocation(350);
                jSplitPane4.setOneTouchExpandable(true);
                jSplitPane4.setPreferredSize(new java.awt.Dimension(725, 500));

                jTabbedPane6.setMinimumSize(new java.awt.Dimension(200, 200));

                jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                javax.swing.GroupLayout mapBlocksetLayoutPanelLayout = new javax.swing.GroupLayout(mapBlocksetLayoutPanel);
                mapBlocksetLayoutPanel.setLayout(mapBlocksetLayoutPanelLayout);
                mapBlocksetLayoutPanelLayout.setHorizontalGroup(
                    mapBlocksetLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                );
                mapBlocksetLayoutPanelLayout.setVerticalGroup(
                    mapBlocksetLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 736, Short.MAX_VALUE)
                );

                jScrollPane3.setViewportView(mapBlocksetLayoutPanel);

                jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Blocks display"));

                jLabel19.setText("Scale :");

                jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
                jComboBox3.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox3ItemStateChanged(evt);
                    }
                });

                jSpinner7.setModel(new javax.swing.SpinnerNumberModel(8, 4, 64, 2));
                jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jSpinner7StateChanged(evt);
                    }
                });

                jLabel20.setText("Blocks per row :");

                jCheckBox3.setSelected(true);
                jCheckBox3.setText("Show grid");
                jCheckBox3.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox3ItemStateChanged(evt);
                    }
                });

                jCheckBox5.setText("Show priority");
                jCheckBox5.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox5ItemStateChanged(evt);
                    }
                });

                jLabel21.setText("BG :");

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

                infoButtonSharedBlocks.setIcon(flatOptionPaneWarningIcon1);
                infoButtonSharedBlocks.setText("");

                javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
                jPanel13.setLayout(jPanel13Layout);
                jPanel13Layout.setHorizontalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(infoButtonSharedBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(jCheckBox3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorPickerBlockset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBox5)))
                        .addContainerGap())
                );
                jPanel13Layout.setVerticalGroup(
                    jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel20)
                            .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox3)
                            .addComponent(infoButtonSharedBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel19)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox5)
                            .addComponent(jLabel21)
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

                javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
                jPanel24.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                    jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton36)
                        .addContainerGap())
                );
                jPanel24Layout.setVerticalGroup(
                    jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton35)
                            .addComponent(jButton36)
                            .addComponent(jButton37)))
                );

                javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                jPanel11.setLayout(jPanel11Layout);
                jPanel11Layout.setHorizontalGroup(
                    jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)
                            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel11Layout.setVerticalGroup(
                    jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jTabbedPane6.addTab("Blockset", jPanel11);

                jPanel23.setPreferredSize(new java.awt.Dimension(356, 500));

                jPanel21.setPreferredSize(new java.awt.Dimension(360, 765));

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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(infoButtonSharedAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(infoButtonSharedAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );

                jScrollPane10.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Animation Tileset"));
                jScrollPane10.setMinimumSize(new java.awt.Dimension(100, 100));
                jScrollPane10.setPreferredSize(new java.awt.Dimension(335, 160));

                tilesetLayoutPanelAnim.setMinimumSize(new java.awt.Dimension(200, 50));

                javax.swing.GroupLayout tilesetLayoutPanelAnimLayout = new javax.swing.GroupLayout(tilesetLayoutPanelAnim);
                tilesetLayoutPanelAnim.setLayout(tilesetLayoutPanelAnimLayout);
                tilesetLayoutPanelAnimLayout.setHorizontalGroup(
                    tilesetLayoutPanelAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 995, Short.MAX_VALUE)
                );
                tilesetLayoutPanelAnimLayout.setVerticalGroup(
                    tilesetLayoutPanelAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 119, Short.MAX_VALUE)
                );

                jScrollPane10.setViewportView(tilesetLayoutPanelAnim);

                tableAnimFrames.setBorder(null);
                tableAnimFrames.setModel(mapAnimationFrameTableModel);
                tableAnimFrames.setSpinnerNumberEditor(true);
                tableAnimFrames.setMinimumSize(new java.awt.Dimension(260, 150));

                jScrollPane11.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Modified Tileset"));
                jScrollPane11.setMinimumSize(new java.awt.Dimension(100, 100));
                jScrollPane11.setPreferredSize(new java.awt.Dimension(335, 160));

                tilesetLayoutPanelModified.setMinimumSize(new java.awt.Dimension(200, 50));

                javax.swing.GroupLayout tilesetLayoutPanelModifiedLayout = new javax.swing.GroupLayout(tilesetLayoutPanelModified);
                tilesetLayoutPanelModified.setLayout(tilesetLayoutPanelModifiedLayout);
                tilesetLayoutPanelModifiedLayout.setHorizontalGroup(
                    tilesetLayoutPanelModifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 995, Short.MAX_VALUE)
                );
                tilesetLayoutPanelModifiedLayout.setVerticalGroup(
                    tilesetLayoutPanelModifiedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 119, Short.MAX_VALUE)
                );

                jScrollPane11.setViewportView(tilesetLayoutPanelModified);

                jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder("Tileset display"));
                jPanel38.setMinimumSize(new java.awt.Dimension(340, 100));

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

                colorPickerTilesetAnim.addColorChangedListener(new com.sfc.sf2.core.gui.controls.ColorPicker.ColorChangedListener() {
                    public void colorChanged(java.awt.event.ActionEvent evt) {
                        colorPickerTilesetAnimColorChanged(evt);
                    }
                });

                javax.swing.GroupLayout colorPickerTilesetAnimLayout = new javax.swing.GroupLayout(colorPickerTilesetAnim);
                colorPickerTilesetAnim.setLayout(colorPickerTilesetAnimLayout);
                colorPickerTilesetAnimLayout.setHorizontalGroup(
                    colorPickerTilesetAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );
                colorPickerTilesetAnimLayout.setVerticalGroup(
                    colorPickerTilesetAnimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 22, Short.MAX_VALUE)
                );

                jLabel17.setText("BG :");

                jLabel43.setText("Tiles per row :");

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
                        jCheckBox8animationActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
                jPanel38.setLayout(jPanel38Layout);
                jPanel38Layout.setHorizontalGroup(
                    jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jCheckBox8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBox2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel43)
                                .addGap(0, 0, 0)
                                .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel38Layout.createSequentialGroup()
                                .addComponent(jCheckBox7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorPickerTilesetAnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel38Layout.setVerticalGroup(
                    jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(jCheckBox7)
                            .addComponent(jLabel17)
                            .addComponent(colorPickerTilesetAnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jCheckBox2)
                            .addComponent(jCheckBox8)
                            .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
                jPanel21.setLayout(jPanel21Layout);
                jPanel21Layout.setHorizontalGroup(
                    jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tableAnimFrames, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                );
                jPanel21Layout.setVerticalGroup(
                    jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableAnimFrames, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
                jPanel23.setLayout(jPanel23Layout);
                jPanel23Layout.setHorizontalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                );
                jPanel23Layout.setVerticalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, Short.MAX_VALUE))
                );

                jTabbedPane6.addTab("Animation", jPanel23);

                jSplitPane4.setLeftComponent(jTabbedPane6);

                jTabbedPane5.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPane5StateChanged(evt);
                    }
                });

                jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map"));

                jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                javax.swing.GroupLayout mapLayoutPanelLayout = new javax.swing.GroupLayout(mapLayoutPanel);
                mapLayoutPanel.setLayout(mapLayoutPanelLayout);
                mapLayoutPanelLayout.setHorizontalGroup(
                    mapLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 1536, Short.MAX_VALUE)
                );
                mapLayoutPanelLayout.setVerticalGroup(
                    mapLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 1536, Short.MAX_VALUE)
                );

                jScrollPane2.setViewportView(mapLayoutPanel);

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                );
                jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                );

                jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder("Map view"));
                jPanel33.setPreferredSize(new java.awt.Dimension(160, 437));

                jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
                jComboBox9.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBox9ActionPerformed(evt);
                    }
                });

                jLabel10.setText("Scale :");

                jCheckBox10.setText("Show grid");
                jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox10ActionPerformed(evt);
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

                jCheckBox11.setText("Exploration flags");
                jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox11ActionPerformed(evt);
                    }
                });

                jCheckBox13.setText("Show priority");
                jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox13ActionPerformed(evt);
                    }
                });

                jCheckBox15.setText("Areas");
                jCheckBox15.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox15ActionPerformed(evt);
                    }
                });

                jCheckBox16.setText("Flag Copies");
                jCheckBox16.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox16ActionPerformed(evt);
                    }
                });

                jCheckBox17.setText("Step Copies");
                jCheckBox17.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox17ActionPerformed(evt);
                    }
                });

                jCheckBox18.setText("Roof Copies");
                jCheckBox18.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox18ActionPerformed(evt);
                    }
                });

                jCheckBox21.setText("Triggers");
                jCheckBox21.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox21ActionPerformed(evt);
                    }
                });

                jCheckBox20.setText("Items");
                jCheckBox20.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox20ActionPerformed(evt);
                    }
                });

                jCheckBox19.setText("Warps");
                jCheckBox19.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox19ActionPerformed(evt);
                    }
                });

                jCheckBox9.setText("Preview anim");
                jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox9animationActionPerformed(evt);
                    }
                });

                jCheckBox22.setText("Vehicles");
                jCheckBox22.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox22ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
                jPanel33.setLayout(jPanel33Layout);
                jPanel33Layout.setHorizontalGroup(
                    jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox9, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                                        .addComponent(jLabel59)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox10))
                                    .addComponent(jCheckBox11, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCheckBox15, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCheckBox19, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCheckBox21, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCheckBox20, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCheckBox22, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCheckBox13, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckBox18)
                                        .addComponent(jCheckBox17)
                                        .addComponent(jCheckBox16))))
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                );
                jPanel33Layout.setVerticalGroup(
                    jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel59)
                            .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox9)
                        .addContainerGap())
                );

                jPanelAreasDisplay.setBorder(javax.swing.BorderFactory.createTitledBorder("Areas display"));

                jCheckBox1.setText("Upper layer overlay");
                jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox1ActionPerformed(evt);
                    }
                });

                jCheckBox4.setText("BG underlay");
                jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox4ActionPerformed(evt);
                    }
                });

                jCheckBox26.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox26ActionPerformed(evt);
                    }
                });

                jLabel1.setText("<html>Simulate parallax and autoscroll</html>");
                jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                javax.swing.GroupLayout jPanelAreasDisplayLayout = new javax.swing.GroupLayout(jPanelAreasDisplay);
                jPanelAreasDisplay.setLayout(jPanelAreasDisplayLayout);
                jPanelAreasDisplayLayout.setHorizontalGroup(
                    jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAreasDisplayLayout.createSequentialGroup()
                        .addContainerGap(73, Short.MAX_VALUE)
                        .addGroup(jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAreasDisplayLayout.createSequentialGroup()
                                .addComponent(jCheckBox26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6))
                );
                jPanelAreasDisplayLayout.setVerticalGroup(
                    jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAreasDisplayLayout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox4)
                        .addGap(9, 9, 9)
                        .addGroup(jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                            .addGroup(jPanelAreasDisplayLayout.createSequentialGroup()
                                .addComponent(jCheckBox26)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                );

                jPanelFlagCopiesDisplay.setBorder(javax.swing.BorderFactory.createTitledBorder("Flag copies display"));

                jCheckBox6.setText("Show copy result");
                jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox6ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanelFlagCopiesDisplayLayout = new javax.swing.GroupLayout(jPanelFlagCopiesDisplay);
                jPanelFlagCopiesDisplay.setLayout(jPanelFlagCopiesDisplayLayout);
                jPanelFlagCopiesDisplayLayout.setHorizontalGroup(
                    jPanelFlagCopiesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFlagCopiesDisplayLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox6)
                        .addContainerGap())
                );
                jPanelFlagCopiesDisplayLayout.setVerticalGroup(
                    jPanelFlagCopiesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelFlagCopiesDisplayLayout.createSequentialGroup()
                        .addComponent(jCheckBox6)
                        .addContainerGap())
                );

                jPanelStepCopiesDisplay.setBorder(javax.swing.BorderFactory.createTitledBorder("Step copies display"));

                jCheckBox12.setText("Show copy result");
                jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox12ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanelStepCopiesDisplayLayout = new javax.swing.GroupLayout(jPanelStepCopiesDisplay);
                jPanelStepCopiesDisplay.setLayout(jPanelStepCopiesDisplayLayout);
                jPanelStepCopiesDisplayLayout.setHorizontalGroup(
                    jPanelStepCopiesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelStepCopiesDisplayLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox12)
                        .addContainerGap())
                );
                jPanelStepCopiesDisplayLayout.setVerticalGroup(
                    jPanelStepCopiesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelStepCopiesDisplayLayout.createSequentialGroup()
                        .addComponent(jCheckBox12)
                        .addContainerGap())
                );

                jPanelRoofCopiesDisplay.setBorder(javax.swing.BorderFactory.createTitledBorder("Roof copies display"));

                jCheckBox14.setText("Show copy result");
                jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox14ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanelRoofCopiesDisplayLayout = new javax.swing.GroupLayout(jPanelRoofCopiesDisplay);
                jPanelRoofCopiesDisplay.setLayout(jPanelRoofCopiesDisplayLayout);
                jPanelRoofCopiesDisplayLayout.setHorizontalGroup(
                    jPanelRoofCopiesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelRoofCopiesDisplayLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox14)
                        .addContainerGap())
                );
                jPanelRoofCopiesDisplayLayout.setVerticalGroup(
                    jPanelRoofCopiesDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRoofCopiesDisplayLayout.createSequentialGroup()
                        .addComponent(jCheckBox14)
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
                jPanel12.setLayout(jPanel12Layout);
                jPanel12Layout.setHorizontalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelAreasDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelFlagCopiesDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelStepCopiesDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelRoofCopiesDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0))
                );
                jPanel12Layout.setVerticalGroup(
                    jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelAreasDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelFlagCopiesDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelStepCopiesDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelRoofCopiesDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
                jPanel41.setLayout(jPanel41Layout);
                jPanel41Layout.setHorizontalGroup(
                    jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel41Layout.setVerticalGroup(
                    jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel41Layout.createSequentialGroup()
                                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );

                jPanel1.getAccessibleContext().setAccessibleName("");

                jTabbedPane5.addTab("Map Editor", jPanel41);

                jPanel43.setMinimumSize(new java.awt.Dimension(340, 200));
                jPanel43.setName(""); // NOI18N

                jScrollPane4.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Tileset"));
                jScrollPane4.setMaximumSize(new java.awt.Dimension(32767, 300));
                jScrollPane4.setPreferredSize(new java.awt.Dimension(558, 300));

                tilesetsLayoutPanel.setMaximumSize(null);

                javax.swing.GroupLayout tilesetsLayoutPanelLayout = new javax.swing.GroupLayout(tilesetsLayoutPanel);
                tilesetsLayoutPanel.setLayout(tilesetsLayoutPanelLayout);
                tilesetsLayoutPanelLayout.setHorizontalGroup(
                    tilesetsLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                );
                tilesetsLayoutPanelLayout.setVerticalGroup(
                    tilesetsLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 0, Short.MAX_VALUE)
                );

                jScrollPane4.setViewportView(tilesetsLayoutPanel);

                jPanel44.setBorder(javax.swing.BorderFactory.createTitledBorder("Tileset display"));
                jPanel44.setMinimumSize(new java.awt.Dimension(340, 100));

                jLabel25.setText("Scale :");

                jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
                jComboBox5.setSelectedIndex(1);
                jComboBox5.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox5ItemStateChanged(evt);
                    }
                });

                jSpinner8.setModel(new javax.swing.SpinnerNumberModel(16, 4, 32, 4));
                jSpinner8.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jSpinner8StateChanged(evt);
                    }
                });

                jLabel26.setText("Tiles per row :");

                jCheckBox23.setSelected(true);
                jCheckBox23.setText("Show grid");
                jCheckBox23.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox23ItemStateChanged(evt);
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

                jLabel13.setText("BG :");

                jLabel29.setText("Tileset : ");

                jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox6.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jComboBox6ItemStateChanged(evt);
                    }
                });

                javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
                jPanel44.setLayout(jPanel44Layout);
                jPanel44Layout.setHorizontalGroup(
                    jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel44Layout.setVerticalGroup(
                    jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel29)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)
                            .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)
                            .addComponent(jCheckBox23)
                            .addComponent(jLabel13)
                            .addComponent(colorPickerTileset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
                jPanel43.setLayout(jPanel43Layout);
                jPanel43Layout.setHorizontalGroup(
                    jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel43Layout.setVerticalGroup(
                    jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel6.setText("Selected block");

                jCheckBox24.setSelected(true);
                jCheckBox24.setText("Show grid");
                jCheckBox24.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox24ItemStateChanged(evt);
                    }
                });

                jCheckBox25.setText("Show priority");
                jCheckBox25.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jCheckBox25ItemStateChanged(evt);
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

                jLabel14.setText("BG :");

                editableBlockSlotPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
                editableBlockSlotPanel.setMaximumSize(new java.awt.Dimension(96, 96));
                editableBlockSlotPanel.setMinimumSize(new java.awt.Dimension(96, 96));

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

                javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
                jPanel46.setLayout(jPanel46Layout);
                jPanel46Layout.setHorizontalGroup(
                    jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox25)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(editableBlockSlotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                                .addComponent(jCheckBox24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorPickerBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel46Layout.setVerticalGroup(
                    jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editableBlockSlotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox24)
                            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel14)
                                .addComponent(colorPickerBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox25)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                jPanel47.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                jLabel27.setText("Left click");

                jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel30.setText("Right click");

                tileSlotPanelLeft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
                tileSlotPanelLeft.setMaximumSize(new java.awt.Dimension(32, 32));
                tileSlotPanelLeft.setMinimumSize(new java.awt.Dimension(32, 32));

                javax.swing.GroupLayout tileSlotPanelLeftLayout = new javax.swing.GroupLayout(tileSlotPanelLeft);
                tileSlotPanelLeft.setLayout(tileSlotPanelLeftLayout);
                tileSlotPanelLeftLayout.setHorizontalGroup(
                    tileSlotPanelLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 30, Short.MAX_VALUE)
                );
                tileSlotPanelLeftLayout.setVerticalGroup(
                    tileSlotPanelLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 30, Short.MAX_VALUE)
                );

                tileSlotPanelRight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));
                tileSlotPanelRight.setMaximumSize(new java.awt.Dimension(32, 32));
                tileSlotPanelRight.setMinimumSize(new java.awt.Dimension(32, 32));

                javax.swing.GroupLayout tileSlotPanelRightLayout = new javax.swing.GroupLayout(tileSlotPanelRight);
                tileSlotPanelRight.setLayout(tileSlotPanelRightLayout);
                tileSlotPanelRightLayout.setHorizontalGroup(
                    tileSlotPanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 30, Short.MAX_VALUE)
                );
                tileSlotPanelRightLayout.setVerticalGroup(
                    tileSlotPanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 30, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
                jPanel47.setLayout(jPanel47Layout);
                jPanel47Layout.setHorizontalGroup(
                    jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addGroup(jPanel47Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(tileSlotPanelLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                                .addComponent(tileSlotPanelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))))
                );
                jPanel47Layout.setVerticalGroup(
                    jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tileSlotPanelLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tileSlotPanelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(18, Short.MAX_VALUE))
                );

                buttonGroupTileEditing.add(jRadioButton16);
                jRadioButton16.setSelected(true);
                jRadioButton16.setText("Apply tile");
                jRadioButton16.setActionCommand("Apply tiles");
                jRadioButton16.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton16ActionPerformed(evt);
                    }
                });

                buttonGroupTileEditing.add(jRadioButton17);
                jRadioButton17.setText("Toggle priority flag");
                jRadioButton17.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton17ActionPerformed(evt);
                    }
                });

                buttonGroupTileEditing.add(jRadioButton18);
                jRadioButton18.setText("Flip tiles");
                jRadioButton18.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton18ActionPerformed(evt);
                    }
                });

                infoButton18.setMessageText("<html>Flip each tile in the selected block. Left click to toggle horizontal flip. Right click to toggle vertical flip. Middle click to clear any flipping.</html>");
                infoButton18.setText("");

                infoButton19.setMessageText("<html>Set the priority flag for each tile. Left-click to set. Right-click to unset.<br>'Priority' means that the tile is drawn above the mapSprites (i.e. above characters).<br>Examples include roof tiles or the top tiles for a wall or table.</html>");
                infoButton19.setText("");

                infoButton20.setMessageText("<html> 'Paint' the selected tiles (left or right click) to the selected block.<br>Use left or right click to select a tile above then left or right click on the <i>Selected block</i> panel to apply</html>");
                infoButton20.setText("");

                javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
                jPanel10.setLayout(jPanel10Layout);
                jPanel10Layout.setHorizontalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jRadioButton16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jRadioButton18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jRadioButton17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel10Layout.setVerticalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(infoButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton18)
                            .addComponent(infoButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(infoButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton17))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
                jPanel45.setLayout(jPanel45Layout);
                jPanel45Layout.setHorizontalGroup(
                    jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel45Layout.setVerticalGroup(
                    jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel45Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
                jPanel42.setLayout(jPanel42Layout);
                jPanel42Layout.setHorizontalGroup(
                    jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                                .addComponent(jPanel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                );
                jPanel42Layout.setVerticalGroup(
                    jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jTabbedPane5.addTab("Block Editor", jPanel42);

                javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
                jPanel48.setLayout(jPanel48Layout);
                jPanel48Layout.setHorizontalGroup(
                    jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane5)
                        .addContainerGap())
                );
                jPanel48Layout.setVerticalGroup(
                    jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane5)
                        .addContainerGap())
                );

                jSplitPane4.setRightComponent(jPanel48);

                javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
                jPanel8.setLayout(jPanel8Layout);
                jPanel8Layout.setHorizontalGroup(
                    jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
                );
                jPanel8Layout.setVerticalGroup(
                    jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );

                jSplitPane3.setTopComponent(jPanel8);

                jTabbedPane2.setMinimumSize(new java.awt.Dimension(390, 185));
                jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPane2StateChanged(evt);
                    }
                });

                jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));
                jPanel14.setMinimumSize(new java.awt.Dimension(904, 160));

                jButton1.setText("Undo");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                    }
                });

                jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Exploration Flags"));

                buttonGroupMapActions.add(jRadioButton1);
                jRadioButton1.setText("Obstructed");
                jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton1ActionPerformed(evt);
                    }
                });

                buttonGroupMapActions.add(jRadioButton3);
                jRadioButton3.setText("Stairs");
                jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton3ActionPerformed(evt);
                    }
                });

                infoButton3.setMessageText("<html><b>Stairs flag:</b> Marks a block so that character will walk diagonally up/down when moving left/right on the tile.<br>- Left click to flag a block as stairs. Left click again to toggle stairs direction<br>- Middle click to clear all flags from the block<br>- Right click to remove the stairs flag</html>");
                infoButton3.setText("");

                infoButton4.setMessageText("<html><b>Obstructed flag:</b> Marks a block so that it cannot be walked on.<br>- Left click to flag a block as obstructed<br>- Middle click to clear all flags from the block<br>- Right click to remove the obstructed flag</html>");
                infoButton4.setText("");

                javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
                jPanel16.setLayout(jPanel16Layout);
                jPanel16Layout.setHorizontalGroup(
                    jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(16, Short.MAX_VALUE))
                );
                jPanel16Layout.setVerticalGroup(
                    jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton1)
                            .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(infoButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                );

                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Blocks"));

                buttonGroupMapActions.add(jRadioButton2);
                jRadioButton2.setSelected(true);
                jRadioButton2.setText("Apply Blocks");
                jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton2ActionPerformed(evt);
                    }
                });

                jLabel3.setText("Left click :");

                jLabel7.setText("Right click :");

                infoButton10.setMessageText("<html><b>Block editing:</b> Place blocks on the map.<br>- Left click to place the 'left click' block<br>- Middle click to copy the hovered map block into the 'left click' slot. Hold and drag to copy a range of blocks<br>- Right click to place the 'right click' block</html>");
                infoButton10.setText("");

                blockSlotPanelLeft.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
                blockSlotPanelLeft.setPreferredSize(new java.awt.Dimension(48, 48));

                javax.swing.GroupLayout blockSlotPanelLeftLayout = new javax.swing.GroupLayout(blockSlotPanelLeft);
                blockSlotPanelLeft.setLayout(blockSlotPanelLeftLayout);
                blockSlotPanelLeftLayout.setHorizontalGroup(
                    blockSlotPanelLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );
                blockSlotPanelLeftLayout.setVerticalGroup(
                    blockSlotPanelLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );

                blockSlotPanelRight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255)));
                blockSlotPanelRight.setPreferredSize(new java.awt.Dimension(48, 48));

                javax.swing.GroupLayout blockSlotPanelRightLayout = new javax.swing.GroupLayout(blockSlotPanelRight);
                blockSlotPanelRight.setLayout(blockSlotPanelRightLayout);
                blockSlotPanelRightLayout.setHorizontalGroup(
                    blockSlotPanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );
                blockSlotPanelRightLayout.setVerticalGroup(
                    blockSlotPanelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 46, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
                jPanel17.setLayout(jPanel17Layout);
                jPanel17Layout.setHorizontalGroup(
                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(blockSlotPanelLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(blockSlotPanelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel17Layout.setVerticalGroup(
                    jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton2)
                            .addComponent(infoButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(blockSlotPanelLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(blockSlotPanelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder("Action Flags"));

                buttonGroupMapActions.add(jRadioButton4);
                jRadioButton4.setText("Warp");
                jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton4ActionPerformed(evt);
                    }
                });

                infoButton5.setMessageText("<html><b>Warp flag:</b> Marks a block with a warp trigger. Must be used in conjunction with a Warp Event..<br>- Left click to flag a block with a warp<br>- Middle click to clear all flags from the block<br>- Right click to remove the warp flag</html>");
                infoButton5.setText("");

                buttonGroupMapActions.add(jRadioButton8);
                jRadioButton8.setText("Trigger");
                jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton8ActionPerformed(evt);
                    }
                });

                infoButton6.setMessageText("<html><b>Trigger flag:</b> Marks a block so that it will trigger a search for Copy Events.<br>- Left click to flag a block with a trigger<br>- Middle click to clear all flags from the block<br>- Right click to remove the trigger  flag</html>");
                infoButton6.setText("");

                buttonGroupMapActions.add(jRadioButton14);
                jRadioButton14.setText("Layer Up");
                jRadioButton14.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton14ActionPerformed(evt);
                    }
                });

                infoButton16.setMessageText("<html><b>Layer Up flag:</b> Marks the block to shift the player (and followers) to the upper layer (the priority layer). Affected characters will be drawn above all map tiles.<br>- Left click to flag a block as a layer up block<br>- Middle click to clear all flags from the block<br>- Right click to remove the layer up flag<br><br>NOTE: When the player character is in the upper layer, flag checks will occur as if the character was in the upper region of the area (see map_06 as reference).</html>");
                infoButton16.setText("");

                buttonGroupMapActions.add(jRadioButton19);
                jRadioButton19.setText("Hide (Roof)");
                jRadioButton19.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton19ActionPerformed(evt);
                    }
                });

                infoButton21.setMessageText("<html><b>Hide (Roof) flag:</b> Marks the block to hide the upper layer (the priority layer). Used alongside Roof Copy events.<br>- Left click to flag a block as a hide block<br>- Middle click to clear all flags from the block<br>- Right click to remove the hide flag.<br><br>NOTE: Many maps do not put the Hide flag on the door of a building but on the open doorway block that replaces the door (via Step Copy event).</html>");
                infoButton21.setText("");

                buttonGroupMapActions.add(jRadioButton20);
                jRadioButton20.setText("Show (Roof)");
                jRadioButton20.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton20ActionPerformed(evt);
                    }
                });

                infoButton22.setMessageText("<html><b>Show (Roof) flag:</b> Marks the block to restore the last hidden section of upper layer (the priority layer). If not upper layer is hidden then does nothing.<br>- Left click to flag a block as a show block<br>- Middle click to clear all flags from the block<br>- Right click to remove the show flag.</html>");
                infoButton22.setText("");

                buttonGroupMapActions.add(jRadioButton12);
                jRadioButton12.setText("Raft");
                jRadioButton12.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton12ActionPerformed(evt);
                    }
                });

                infoButton13.setMessageText("<html><b>Caravan flag:</b> Marks the block so the player cannot walk on it but the caravan can.<br>- Left click to flag a block as a caravan block<br>- Middle click to clear all flags from the block<br>- Right click to remove the caravan flag</html>");
                infoButton13.setText("");

                buttonGroupMapActions.add(jRadioButton11);
                jRadioButton11.setText("Caravan");
                jRadioButton11.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton11ActionPerformed(evt);
                    }
                });

                infoButton14.setMessageText("<html><b>Raft flag:</b> Marks the block so the player cannot walk on it but the raft can.<br>- Left click to flag a block as a raft block<br>- Middle click to clear all flags from the block<br>- Right click to remove the raft flag</html>");
                infoButton14.setText("");

                buttonGroupMapActions.add(jRadioButton21);
                jRadioButton21.setText("Step");
                jRadioButton21.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton21ActionPerformed(evt);
                    }
                });

                infoButton24.setMessageText("<html><b>Step flag:</b> Marks a block so that it will trigger a search for Step Events.<br>- Left click to flag a block with a trigger<br>- Middle click to clear all flags from the block<br>- Right click to remove the step flag</html>");
                infoButton24.setText("");

                buttonGroupMapActions.add(jRadioButton15);
                jRadioButton15.setText("Layer Down");
                jRadioButton15.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton15ActionPerformed(evt);
                    }
                });

                infoButton17.setMessageText("<html><b>Layer Down flag:</b> Marks the block to shift the player (and followers) out of the upper layer (the priority layer). Restores characters to be drawn above map tiles but below 'priority' map tiles.<br>- Left click to flag a block as a layer down block<br>- Middle click to clear all flags from the block<br>- Right click to remove the layer down flag</html>");
                infoButton17.setText("");

                javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
                jPanel49.setLayout(jPanel49Layout);
                jPanel49Layout.setHorizontalGroup(
                    jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButton15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButton21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel49Layout.createSequentialGroup()
                                        .addGap(0, 0, 0)
                                        .addComponent(jRadioButton19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel49Layout.createSequentialGroup()
                                        .addComponent(jRadioButton20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel49Layout.setVerticalGroup(
                    jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton4)
                            .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton8)
                            .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton21)
                            .addComponent(infoButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton14)
                            .addComponent(infoButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton20)
                            .addComponent(infoButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton15)
                            .addComponent(infoButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jRadioButton12)
                                .addComponent(infoButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton11)
                            .addComponent(infoButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jRadioButton19)
                                .addComponent(infoButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(38, 38, 38))
                );

                buttonGroupMapActions.add(jRadioButton7);
                jRadioButton7.setText("Table");
                jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton7ActionPerformed(evt);
                    }
                });

                infoButton11.setMessageText("<html><b>Shelf flag:</b> Marks a block to be searchable with the bookshelf message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a bookshelf<br>- Middle click to clear all flags from the block<br>- Right click to remove the shelf flag<br><br>NOTE: This is separate from the map's s4_descriptions.asm</html>");
                infoButton11.setText("");

                buttonGroupMapActions.add(jRadioButton9);
                jRadioButton9.setText("Shelf");
                jRadioButton9.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton9ActionPerformed(evt);
                    }
                });

                infoButton9.setMessageText("<html><b>Table flag:</b> Marks the block so that interacting with it will interact with the block on the other side of it.<br>- Left click to flag a block as a table<br>- Middle click to clear all flags from the block<br>- Right click to remove the table flag</html>");
                infoButton9.setText("");

                infoButton8.setMessageText("<html><b>Vase flag:</b> Marks a block to be searchable with the vase message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a vase<br>- Middle click to clear all flags from the block<br>- Right click to remove the vase flag</html>");
                infoButton8.setText("");

                infoButton7.setMessageText("<html><b>Barrel flag:</b> Marks a block to be searchable with the barrel message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a barrel<br>- Middle click to clear all flags from the block<br>- Right click to remove the barrel flag</html>");
                infoButton7.setText("");

                buttonGroupMapActions.add(jRadioButton6);
                jRadioButton6.setText("Vase");
                jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton6ActionPerformed(evt);
                    }
                });

                buttonGroupMapActions.add(jRadioButton5);
                jRadioButton5.setText("Barrel");
                jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton5ActionPerformed(evt);
                    }
                });

                infoButton12.setMessageText("<html><b>Search flag:</b> Marks a block to be searchable with the \"searched the area\" message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a searchable<br>- Middle click to clear all flags from the block<br>- Right click to remove the search flag</html>");
                infoButton12.setText("");

                infoButton15.setMessageText("<html><b>Chest flag:</b> Marks a block to be searchable with the chest message & animation. Combine with \"Chest Items\" to allow player to obtain items.<br>- Left click to flag a block as a chest<br>- Middle click to clear all flags from the block<br>- Right click to remove the chest flag</html>");
                infoButton15.setText("");

                buttonGroupMapActions.add(jRadioButton13);
                jRadioButton13.setText("Chest");
                jRadioButton13.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton13ActionPerformed(evt);
                    }
                });

                buttonGroupMapActions.add(jRadioButton10);
                jRadioButton10.setText("Search");
                jRadioButton10.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButton10ActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
                jPanel50.setLayout(jPanel50Layout);
                jPanel50Layout.setHorizontalGroup(
                    jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButton6)
                                .addGap(10, 10, 10)
                                .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel50Layout.setVerticalGroup(
                    jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton10)
                            .addComponent(infoButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton13)
                            .addComponent(infoButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton5)
                            .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton6)
                            .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButton9)
                            .addComponent(infoButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton7)
                            .addComponent(infoButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

                javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
                jPanel31.setLayout(jPanel31Layout);
                jPanel31Layout.setHorizontalGroup(
                    jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );
                jPanel31Layout.setVerticalGroup(
                    jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
                jPanel14.setLayout(jPanel14Layout);
                jPanel14Layout.setHorizontalGroup(
                    jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel14Layout.setVerticalGroup(
                    jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                );

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel4Layout.setVerticalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jTabbedPane2.addTab("Map Edit", jPanel4);

                tableAreas.setBorder(null);
                tableAreas.setInfoMessage("<html><b>Areas:</b> Indicates areas for bounding the camera (forcing it to stay within the region), defining upper (roof) layers, and for foreground/background effects.<br>- L1 X/Y/X'/Y': Defines a rectangle representing the area. The game camera is bound to this space.<br>- L2 F X/Y: Defines the foreground (upper) layer for the area. Used to define roofs, treetops, etc.<br>- L2 B X/Y: Defines the backgroun layer. TODO: What is it used for.<br>L1/L2 P X/Y: Defines the parallax effect of the layer 1 or 2. Parallax causes layers to scroll at different speeds as the player character moves.<br>- L1/2 S X/Y: Defines the autoscroll speed for layers 1 & 2. Autoscroll will cause the layer to constantly scroll.<br>- Music: The music to start playing when the player character enters the area.<br><br><b>When Area row is selected:</b>Left-click to drag the closest corner or point of the area (look for the circular blue handle).</html>");
                tableAreas.setModel(mapAreaTableModel);
                tableAreas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableAreas.setSingleClickText(true);
                tableAreas.setSpinnerNumberEditor(true);
                tableAreas.setMinimumSize(new java.awt.Dimension(150, 150));
                tableAreas.setPreferredSize(new java.awt.Dimension(260, 150));
                jTabbedPane2.addTab("Areas", tableAreas);

                jTabbedPane3.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPane3StateChanged(evt);
                    }
                });

                tableFlagCopies.setBorder(null);
                tableFlagCopies.setInfoMessage("<html><b>Flag copies event:</b> If a game flag is triggered when the map loads, then copies map blocks in one section of the map to another section.<br>- Flag: The game flag to trigger the event.<br>- Flag Info: A helpful description of what the flag value refers to.<br>- Source X/Y/X'/Y': Defines a rectangle to copy blocks FROM.<br>- Dest. X/Y: The top-left of the section to copy blocks TO. Uses the same width and height as the source.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When Flag Copy row is selected:</b>TODO.</html>");
                tableFlagCopies.setModel(mapFlagCopyTableModel);
                tableFlagCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableFlagCopies.setSingleClickText(true);
                tableFlagCopies.setSpinnerNumberEditor(true);
                tableFlagCopies.setMinimumSize(null);
                jTabbedPane3.addTab("Flag Copies", tableFlagCopies);

                tableStepCopies.setBorder(null);
                tableStepCopies.setInfoMessage("<html><b>Step copies event:</b> If the player character steps on this map block, then copies map blocks in one section of the map to another section.<br>- Trigger X/Y: The trigger position for the step copy.<br>- Source X/Y/X'/Y': Defines a rectangle to copy blocks FROM.<br>- Dest. X/Y: The top-left of the section to copy blocks TO. Uses the width and height from Source.<br>- Comment: Optional comment that is saved to the .asm file.<br><br>NOTE: Step copy triggers before the character enters the Trigger X/Y block. If the Destination X/Y is the same block as the Trigger X/Y, this can then trigger another flag/event from the copied source (e.g. see door Hide flags in most town maps).<br><br><b>When Flag Copy row is selected:</b>TODO.</html>");
                tableStepCopies.setModel(mapStepCopyTableModel);
                tableStepCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableStepCopies.setSingleClickText(true);
                tableStepCopies.setSpinnerNumberEditor(true);
                tableStepCopies.setMinimumSize(null);
                jTabbedPane3.addTab("Step Copies", tableStepCopies);

                tableRoofCopies.setBorder(null);
                tableRoofCopies.setInfoMessage("<html><b>Roof copy event:</b> If the player character enters the trigger block, then copies map blocks in one section of the upper layer to another section of the upper layer.<br>- Trigger X/Y: The trigger position for the step copy.<br>- Source X/Y/X'/Y': Defines a rectangle to copy blocks FROM. Set Source X/Y both to 255 to automatically detect the source position from the destination, in relation to the 1st area defined.<br>- Dest. X/Y: The top-left of the section to copy blocks TO. Uses the width and height from the Source.<br>- Comment: Optional comment that is saved to the .asm file.<br><br>NOTE: Use Show flags to reverse the previous Roof Copy (i.e. to make the roof appear again).<br><br><b>When Flag Copy row is selected:</b>TODO.</html>");
                tableRoofCopies.setModel(mapRoofCopyTableModel);
                tableRoofCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableRoofCopies.setSingleClickText(true);
                tableRoofCopies.setSpinnerNumberEditor(true);
                tableRoofCopies.setMinimumSize(null);
                jTabbedPane3.addTab("Roof Copies", tableRoofCopies);

                javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
                jPanel22.setLayout(jPanel22Layout);
                jPanel22Layout.setHorizontalGroup(
                    jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1108, Short.MAX_VALUE)
                );
                jPanel22Layout.setVerticalGroup(
                    jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                );

                jTabbedPane2.addTab("Block Copies", jPanel22);

                tableWarps.setBorder(null);
                tableWarps.setInfoMessage("<html><b>Warp event:</b> Teleports the player to a new position or a new map.<br>- Trigger X/Y: The trigger point for the warp.<br>- Scroll Dir: The direction that the camera scrolls when warping (used for overworld maps).<br> - Dest. Map: The map to warp to. Set to \"CURRENT\" to warp to a different position on the current map.<br>- Dest X/Y: The destination position to warp to (on this map or another.<br>- Facing: The direction to face at the warp destination.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When a warp row is selected:</b> Left click and drag will set the set the Trigger or Destination X/Y position of the selected item event, whichever is closer to the cursor.</html>");
                tableWarps.setModel(mapWarpTableModel);
                tableWarps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableWarps.setSingleClickText(true);
                tableWarps.setSpinnerNumberEditor(true);
                tableWarps.setMinimumSize(new java.awt.Dimension(260, 150));
                jTabbedPane2.addTab("Warps", tableWarps);

                jTabbedPane4.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPane4StateChanged(evt);
                    }
                });

                tableChestItems.setBorder(null);
                tableChestItems.setInfoMessage("<html><b>Item event:</b> Allows player to aquire items.<br>- X/Y: The position of the item event.<br>- Flag: The flag that is written when the item event is triggered (when the item is aquired).<br> - Flag Info: A helpful description of what the flag value refers to.<br>- Item: The item that is acquired by the event. Set to \"NOTHING\" for no item.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When an item row is selected:</b> Left click will set the new X/Y position of the selected item event.</html>");
                tableChestItems.setModel(mapChestItemTableModel);
                tableChestItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableChestItems.setSingleClickText(true);
                tableChestItems.setSpinnerNumberEditor(true);
                tableChestItems.setMinimumSize(null);
                jTabbedPane4.addTab("Chest Items", tableChestItems);

                tableOtherItems.setBorder(null);
                tableOtherItems.setInfoMessage("<html><b>Item event:</b> Allows player to aquire items.<br>- X/Y: The position of the item event.<br>- Flag: The flag that is written when the item event is triggered (when the item is aquired).<br> - Flag Info: A helpful description of what the flag value refers to.<br>- Item: The item that is acquired by the event. Set to \"NOTHING\" for no item.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When an item row is selected:</b> Left click will set the new X/Y position of the selected item event.</html>");
                tableOtherItems.setModel(mapOtherItemTableModel);
                tableOtherItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableOtherItems.setSingleClickText(true);
                tableOtherItems.setSpinnerNumberEditor(true);
                tableOtherItems.setMinimumSize(null);
                jTabbedPane4.addTab("Other Items", tableOtherItems);

                jTabbedPane2.addTab("Items", jTabbedPane4);

                javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
                jPanel20.setLayout(jPanel20Layout);
                jPanel20Layout.setHorizontalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel20Layout.setVerticalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jSplitPane3.setBottomComponent(jPanel20);

                jSplitPane2.setRightComponent(jSplitPane3);

                javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
                jPanel15.setLayout(jPanel15Layout);
                jPanel15Layout.setHorizontalGroup(
                    jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );
                jPanel15Layout.setVerticalGroup(
                    jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
                        .addContainerGap())
                );

                jSplitPane1.setTopComponent(jPanel15);
                jSplitPane1.setBottomComponent(console1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1450, Short.MAX_VALUE)
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                );

                setSize(new java.awt.Dimension(1466, 1008));
                setLocationRelativeTo(null);
            }// </editor-fold>//GEN-END:initComponents

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton14.getFilePath());
        Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButton20.getFilePath());
        int mapId = (int)jSpinner4.getValue();
        try {
            mapManager.ImportMapEnums(sf2enumsPath);
            mapManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
            jSpinner5.setValue(mapId);
        } catch (Exception ex) {
            mapManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be imported for map : " + mapId);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Path blocketImagePath = PathHelpers.getBasePath().resolve(fileButton15.getFilePath());
        Path priorityPath = PathHelpers.getBasePath().resolve(fileButton16.getFilePath());
        int blocksPerRow = (int)jSpinner7.getValue();
        if (!PathHelpers.createPathIfRequred(blocketImagePath)) return;
        if (!PathHelpers.createPathIfRequred(priorityPath)) return;
        try {
            mapManager.exportMapBlocksetImage(blocketImagePath, priorityPath, blocksPerRow, mapLayoutPanel.getMap().getBlockset(), mapLayoutPanel.getMapLayout().getTilesets());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map blockset data could not be exported to : " + blocketImagePath);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Path layoutImagePath = PathHelpers.getBasePath().resolve(fileButton17.getFilePath());
        Path layoutFlagsPath = PathHelpers.getBasePath().resolve(fileButton18.getFilePath());
        Path priorityPath = PathHelpers.getBasePath().resolve(fileButton19.getFilePath());
        if (!PathHelpers.createPathIfRequred(layoutImagePath)) return;
        if (!PathHelpers.createPathIfRequred(layoutFlagsPath)) return;
        if (!PathHelpers.createPathIfRequred(priorityPath)) return;
        try {
            mapManager.exportMapLayoutImage(layoutImagePath, layoutFlagsPath, priorityPath, mapLayoutPanel.getMap().getLayout());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map layout data could not be exported to : " + layoutImagePath);
        }
    }//GEN-LAST:event_jButton5ActionPerformed
	
    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        mapLayoutPanel.setDisplayScale(jComboBox9.getSelectedIndex()+1);
        mapLayoutSettings.setTilesetScale(jComboBox9.getSelectedIndex()+1);
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed
        mapLayoutPanel.setShowGrid(jCheckBox10.isSelected());
    }//GEN-LAST:event_jCheckBox10ActionPerformed

    private void colorPicker1ColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPicker1ColorChanged
        mapLayoutPanel.setBGColor(colorPicker1.getColor());
        mapLayoutSettings.setTilesetBGColor(colorPicker1.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPicker1ColorChanged

    private void jCheckBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox11ActionPerformed
        mapLayoutPanel.setShowExplorationFlags(jCheckBox11.isSelected());
    }//GEN-LAST:event_jCheckBox11ActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        mapLayoutPanel.setShowPriority(jCheckBox13.isSelected());
    }//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jCheckBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox15ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_AREAS, jCheckBox15.isSelected());
    }//GEN-LAST:event_jCheckBox15ActionPerformed

    private void jCheckBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox16ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_FLAG_COPIES, jCheckBox16.isSelected());
    }//GEN-LAST:event_jCheckBox16ActionPerformed

    private void jCheckBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox17ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_STEP_COPIES, jCheckBox17.isSelected());
    }//GEN-LAST:event_jCheckBox17ActionPerformed

    private void jCheckBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox18ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ROOF_COPIES, jCheckBox18.isSelected());
    }//GEN-LAST:event_jCheckBox18ActionPerformed

    private void jCheckBox19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox19ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_WARPS, jCheckBox19.isSelected());
    }//GEN-LAST:event_jCheckBox19ActionPerformed

    private void jCheckBox20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox20ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_CHEST_ITEMS, jCheckBox20.isSelected());
    }//GEN-LAST:event_jCheckBox20ActionPerformed

    private void jCheckBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox21ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_TRIGGERS, jCheckBox21.isSelected());
    }//GEN-LAST:event_jCheckBox21ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton14.getFilePath());
        int mapId = (int)jSpinner5.getValue();
        try {
            mapManager.exportDisassemblyFromMapEntries(mapEntriesPath, mapId, mapLayoutPanel.getMap());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be exported to : " + mapId);
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jCheckBox9animationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox9animationActionPerformed
        boolean isSelected = ((JCheckBox)evt.getSource()).isSelected();
        jCheckBox8.setSelected(isSelected);
        jCheckBox9.setSelected(isSelected);
        tilesetLayoutPanelModified.setPreviewAnim(isSelected);
    }//GEN-LAST:event_jCheckBox9animationActionPerformed

    private void jCheckBox22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox22ActionPerformed
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_VEHICLES, jCheckBox22.isSelected());
    }//GEN-LAST:event_jCheckBox22ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButton2.getDirectoryPath());
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton31.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton32.getFilePath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButton3.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButton4.getFilePath());
        Path mapLayoutDataPath = mapDirectory.resolve(fileButton5.getFilePath());
        Path mapAreasDataPath = mapDirectory.resolve(fileButton6.getFilePath());
        Path mapFlagsDataPath = mapDirectory.resolve(fileButton7.getFilePath());
        Path mapStepsDataPath = mapDirectory.resolve(fileButton8.getFilePath());
        Path mapRoofsDataPath = mapDirectory.resolve(fileButton9.getFilePath());
        Path mapWarpsDataPath = mapDirectory.resolve(fileButton10.getFilePath());
        Path mapChestItemsDataPath = mapDirectory.resolve(fileButton11.getFilePath());
        Path mapOtherItemsDataPath = mapDirectory.resolve(fileButton12.getFilePath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButton13.getFilePath());
        Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButton20.getFilePath());
        try {
            mapManager.ImportMapEnums(sf2enumsPath);
            mapManager.importDisassemblyFromData(paletteEntriesPath, tilesetEntriesPath, mapTilesetDataPath, mapBlocksetDataPath, mapLayoutDataPath, mapAreasDataPath,
                    mapFlagsDataPath, mapStepsDataPath, mapRoofsDataPath, mapWarpsDataPath, mapChestItemsDataPath, mapOtherItemsDataPath, mapAnimationDataPath);
            directoryButton4.setDirectoryPath(directoryButton2.getDirectoryPath());
        } catch (Exception ex) {
            mapManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be imported from : " + mapAnimationDataPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButton4.getDirectoryPath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButton3.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButton4.getFilePath());
        Path mapLayoutDataPath = mapDirectory.resolve(fileButton5.getFilePath());
        Path mapAreasDataPath = mapDirectory.resolve(fileButton6.getFilePath());
        Path mapFlagsDataPath = mapDirectory.resolve(fileButton7.getFilePath());
        Path mapStepsDataPath = mapDirectory.resolve(fileButton8.getFilePath());
        Path mapRoofsDataPath = mapDirectory.resolve(fileButton9.getFilePath());
        Path mapWarpsDataPath = mapDirectory.resolve(fileButton10.getFilePath());
        Path mapChestItemsDataPath = mapDirectory.resolve(fileButton11.getFilePath());
        Path mapOtherItemsDataPath = mapDirectory.resolve(fileButton12.getFilePath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButton13.getFilePath());
        if (!PathHelpers.createPathIfRequred(mapDirectory)) return;
        try {
            mapManager.exportDisassemblyFromData(mapTilesetDataPath, mapBlocksetDataPath, mapLayoutDataPath, mapAreasDataPath, mapFlagsDataPath, mapStepsDataPath, mapRoofsDataPath,
                    mapWarpsDataPath, mapChestItemsDataPath, mapOtherItemsDataPath, mapAnimationDataPath, mapLayoutPanel.getMap());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be exported to : " + mapDirectory);
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        Path palettePath = PathHelpers.getBasePath().resolve(fileButton21.getFilePath());
        Path tilesetPath1 = PathHelpers.getBasePath().resolve(fileButton22.getFilePath());
        Path tilesetPath2 = PathHelpers.getBasePath().resolve(fileButton23.getFilePath());
        Path tilesetPath3 = PathHelpers.getBasePath().resolve(fileButton24.getFilePath());
        Path tilesetPath4 = PathHelpers.getBasePath().resolve(fileButton25.getFilePath());
        Path tilesetPath5 = PathHelpers.getBasePath().resolve(fileButton26.getFilePath());
        Path blocksPath = PathHelpers.getBasePath().resolve(fileButton27.getFilePath());
        Path layoutPath = PathHelpers.getBasePath().resolve(fileButton28.getFilePath());
        Path areasPath = PathHelpers.getBasePath().resolve(fileButton33.getFilePath());
        Path flagsPath = PathHelpers.getBasePath().resolve(fileButton34.getFilePath());
        Path stepsPath = PathHelpers.getBasePath().resolve(fileButton35.getFilePath());
        Path roofsPath = PathHelpers.getBasePath().resolve(fileButton36.getFilePath());
        Path warpsPath = PathHelpers.getBasePath().resolve(fileButton37.getFilePath());
        Path chestItemsPath = PathHelpers.getBasePath().resolve(fileButton38.getFilePath());
        Path otherItemsPath = PathHelpers.getBasePath().resolve(fileButton39.getFilePath());
        Path animationPath = PathHelpers.getBasePath().resolve(fileButton29.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton30.getFilePath());
        Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButton20.getFilePath());
        try {
            mapManager.ImportMapEnums(sf2enumsPath);
            mapManager.importDisassemblyFromRawFiles(palettePath, new Path[] { tilesetPath1, tilesetPath2, tilesetPath3, tilesetPath4, tilesetPath5 }, blocksPath, layoutPath,
                    areasPath, flagsPath, stepsPath, roofsPath, warpsPath, chestItemsPath, otherItemsPath, animationPath, tilesetEntriesPath);
        } catch (Exception ex) {
            mapManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be imported from : " + blocksPath.getParent());
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        if (jComboBox5.getSelectedIndex() < 0) return;
        int scale = (int)jComboBox5.getSelectedIndex()+1;
        if (scale != mapLayoutSettings.getTilesetScale()) {
            tilesetsLayoutPanel.setDisplayScale(scale);
            mapLayoutSettings.setTilesetScale(scale);
            SettingsManager.saveSettingsFile();
        }
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jSpinner8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner8StateChanged
        int tilesPerRow = (int)jSpinner8.getValue();
        if (tilesPerRow != mapLayoutSettings.getTilesetTilesPerRow()) {
            tilesetsLayoutPanel.setItemsPerRow(tilesPerRow);
            mapLayoutSettings.setTilesetTilesPerRow(tilesPerRow);
            SettingsManager.saveSettingsFile();
        }
    }//GEN-LAST:event_jSpinner8StateChanged

    private void jCheckBox23ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox23ItemStateChanged
        tilesetsLayoutPanel.setShowGrid(jCheckBox23.isSelected());
    }//GEN-LAST:event_jCheckBox23ItemStateChanged

    private void colorPickerTilesetColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerTilesetColorChanged
        tilesetsLayoutPanel.setBGColor(colorPickerTileset.getColor());
        mapLayoutSettings.setTilesetBGColor(colorPickerTileset.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPickerTilesetColorChanged

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
        if (jComboBox6.getSelectedIndex() < 0) return;
        tilesetsLayoutPanel.setSelectedTileset(jComboBox6.getSelectedIndex());
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    private void jCheckBox24ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox24ItemStateChanged
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setShowGrid(jCheckBox24.isSelected());
        }
    }//GEN-LAST:event_jCheckBox24ItemStateChanged

    private void jCheckBox25ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox25ItemStateChanged
        editableBlockSlotPanel.setShowPriority(jCheckBox25.isSelected());
    }//GEN-LAST:event_jCheckBox25ItemStateChanged

    private void colorPickerBlocksColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerBlocksColorChanged
        editableBlockSlotPanel.setBGColor(colorPickerBlocks.getColor());
        mapLayoutSettings.setBlockBGColor(colorPickerBlocks.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPickerBlocksColorChanged

    private void jRadioButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton16ActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_PAINT_TILE);
            blockSlot.setShowPriority(jCheckBox25.isSelected());
        }
    }//GEN-LAST:event_jRadioButton16ActionPerformed

    private void jRadioButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton17ActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_PRIORITY);
            blockSlot.setShowPriority(true);
        }
    }//GEN-LAST:event_jRadioButton17ActionPerformed

    private void jRadioButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton18ActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_FLIP);
            blockSlot.setShowPriority(jCheckBox25.isSelected());
        }
    }//GEN-LAST:event_jRadioButton18ActionPerformed

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        SetTabRelativeCheckbox(null, null, MapLayoutPanel.DRAW_MODE_NONE);
        jPanelAreasDisplay.setVisible(false);
        jPanelFlagCopiesDisplay.setVisible(false);
        jPanelStepCopiesDisplay.setVisible(false);
        jPanelRoofCopiesDisplay.setVisible(false);
        int index = jTabbedPane2.getSelectedIndex();
        mapLayoutPanel.setIsOnActionsTab(index == 0);
        switch (index) {
            case 0:     //Actions & Anims
            JCheckBox actionCheckbox = actionRelativeCheckbox;
            int mode = mapLayoutPanel.getCurrentPaintMode();
            onMapActionCheckboxSet(null, -1);
            onMapActionCheckboxSet(actionCheckbox, mode);
            mapLayoutPanel.setDrawMode_Tabs(MapLayoutPanel.DRAW_MODE_NONE);
            break;
            case 1:     //Areas panel
            SetTabRelativeCheckbox(jCheckBox15, tableAreas.jTable, MapLayoutPanel.DRAW_MODE_AREAS);
            jPanelAreasDisplay.setVisible(true);
            break;
            case 2:     //Block Copies panels
            jTabbedPane3StateChanged(new ChangeEvent(jTabbedPane3));
            break;
            case 3:     //Warps panel
            SetTabRelativeCheckbox(jCheckBox19, tableWarps.jTable, MapLayoutPanel.DRAW_MODE_WARPS);
            break;
            case 4:     //Items panel
            jTabbedPane4StateChanged(new ChangeEvent(jTabbedPane4));
            break;
        }
        mapLayoutPanel.redraw();
    }//GEN-LAST:event_jTabbedPane2StateChanged

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
                Tileset tileset = mapManager.getMapAnimationManager().importAnimationTileset(mapLayoutPanel.getMap().getLayout().getPalette(), tilesetEntriesPath, value);
                tilesetLayoutPanelAnim.setTileset(tileset);
            } catch (Exception ex) {
                Console.logger().log(Level.SEVERE, null, ex);
                Console.logger().severe("ERROR Tileset could not be imported for tileset : " + value);
            }
        }
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jCheckBox8animationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8animationActionPerformed
        boolean isSelected = ((JCheckBox)evt.getSource()).isSelected();
        jCheckBox8.setSelected(isSelected);
        jCheckBox9.setSelected(isSelected);
        tilesetLayoutPanelModified.setPreviewAnim(isSelected);
    }//GEN-LAST:event_jCheckBox8animationActionPerformed

    private void jCheckBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox7ItemStateChanged
        tilesetLayoutPanelAnim.setShowAnimationFrames(jCheckBox7.isSelected());
        tilesetLayoutPanelModified.setShowAnimationFrames(jCheckBox7.isSelected());
    }//GEN-LAST:event_jCheckBox7ItemStateChanged

    private void jSpinner6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner6StateChanged
        int tilesPerRow = (int)jSpinner6.getValue();
        tilesetLayoutPanelAnim.setItemsPerRow(tilesPerRow);
        tilesetLayoutPanelModified.setItemsPerRow(tilesPerRow);
    }//GEN-LAST:event_jSpinner6StateChanged

    private void colorPickerTilesetAnimColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerTilesetAnimColorChanged
        tilesetLayoutPanelAnim.setBGColor(colorPickerTilesetAnim.getColor());
        tilesetLayoutPanelModified.setBGColor(colorPickerTilesetAnim.getColor());
        mapLayoutSettings.setTilesetBGColor(colorPickerTilesetAnim.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPickerTilesetAnimColorChanged

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        tilesetLayoutPanelAnim.setShowGrid(jCheckBox2.isSelected());
        tilesetLayoutPanelModified.setShowGrid(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

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

    private void jTabbedPane4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane4StateChanged
        int index = jTabbedPane2.getSelectedIndex();
        if (index != 4) return; //Is not on Block copies panel
        SetTabRelativeCheckbox(null, null, MapLayoutPanel.DRAW_MODE_NONE);
        index = jTabbedPane4.getSelectedIndex();
        switch (index) {
            default:
            return;
            case 0:     //Chest items
            SetTabRelativeCheckbox(jCheckBox20, tableChestItems.jTable, MapLayoutPanel.DRAW_MODE_CHEST_ITEMS);
            break;
            case 1:     //Other items
            SetTabRelativeCheckbox(jCheckBox20, tableOtherItems.jTable, MapLayoutPanel.DRAW_MODE_OTHER_ITEMS);
            break;
        }
        mapLayoutPanel.redraw();
    }//GEN-LAST:event_jTabbedPane4StateChanged

    private void jTabbedPane3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane3StateChanged
        int index = jTabbedPane2.getSelectedIndex();
        if (index != 2) return; //Is not on Block copies panel
        SetTabRelativeCheckbox(null, null, MapLayoutPanel.DRAW_MODE_NONE);
        jPanelAreasDisplay.setVisible(false);
        jPanelFlagCopiesDisplay.setVisible(false);
        jPanelStepCopiesDisplay.setVisible(false);
        jPanelRoofCopiesDisplay.setVisible(false);
        index = jTabbedPane3.getSelectedIndex();
        switch (index) {
            default:
            return;
            case 0:     //Flag copies
            SetTabRelativeCheckbox(jCheckBox16, tableFlagCopies.jTable, MapLayoutPanel.DRAW_MODE_FLAG_COPIES);
            jPanelFlagCopiesDisplay.setVisible(true);
            break;
            case 1:     //Step copies
            SetTabRelativeCheckbox(jCheckBox17, tableStepCopies.jTable, MapLayoutPanel.DRAW_MODE_STEP_COPIES);
            jPanelStepCopiesDisplay.setVisible(true);
            break;
            case 2:     //Roof copies
            SetTabRelativeCheckbox(jCheckBox18, tableRoofCopies.jTable, MapLayoutPanel.DRAW_MODE_ROOF_COPIES);
            jPanelRoofCopiesDisplay.setVisible(true);
            break;
        }
        mapLayoutPanel.redraw();
    }//GEN-LAST:event_jTabbedPane3StateChanged

    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        mapLayoutPanel.setShowRoofCopyResult(jCheckBox14.isSelected());
    }//GEN-LAST:event_jCheckBox14ActionPerformed

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        mapLayoutPanel.setShowStepCopyResult(jCheckBox12.isSelected());
    }//GEN-LAST:event_jCheckBox12ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        mapLayoutPanel.setShowFlagCopyResult(jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        mapLayoutPanel.setShowAreasUnderlay(jCheckBox4.isSelected());
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        mapLayoutPanel.setShowAreasOverlay(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void colorPickerBlocksetColorChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorPickerBlocksetColorChanged
        mapBlocksetLayoutPanel.setBGColor(colorPickerBlockset.getColor());
        mapLayoutSettings.setBlocksetBGColor(colorPickerBlockset.getColor());
        SettingsManager.saveSettingsFile();
    }//GEN-LAST:event_colorPickerBlocksetColorChanged

    private void jCheckBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox5ItemStateChanged
        mapBlocksetLayoutPanel.setShowPriority(jCheckBox5.isSelected());
    }//GEN-LAST:event_jCheckBox5ItemStateChanged

    private void jCheckBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox3ItemStateChanged
        if (mapBlocksetLayoutPanel != null) {
            mapBlocksetLayoutPanel.setShowGrid(jCheckBox3.isSelected());
        }
    }//GEN-LAST:event_jCheckBox3ItemStateChanged

    private void jSpinner7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner7StateChanged
        if (mapBlocksetLayoutPanel != null) {
            int blocksPerRow = (int)jSpinner7.getValue();
            if (blocksPerRow != mapLayoutSettings.getBlocksetBlocksPerRow()) {
                mapBlocksetLayoutPanel.setItemsPerRow(blocksPerRow);
                mapLayoutSettings.setBlocksetBlocksPerRow(blocksPerRow);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jSpinner7StateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        if (jComboBox3.getSelectedIndex() >= 0 && mapBlocksetLayoutPanel != null) {
            int scale = (int)jComboBox3.getSelectedIndex()+1;
            if (scale != mapLayoutSettings.getBlocksetScale()) {
                mapBlocksetLayoutPanel.setDisplayScale(scale);
                mapLayoutSettings.setBlocksetScale(scale);
                SettingsManager.saveSettingsFile();
            }
        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jRadioButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton15ActionPerformed
        onMapActionCheckboxSet(jCheckBox21, MapLayoutBlock.MAP_FLAG_LAYER_DOWN);
    }//GEN-LAST:event_jRadioButton15ActionPerformed

    private void jRadioButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton14ActionPerformed
        onMapActionCheckboxSet(jCheckBox21, MapLayoutBlock.MAP_FLAG_LAYER_UP);
    }//GEN-LAST:event_jRadioButton14ActionPerformed

    private void jRadioButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton13ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapLayoutBlock.MAP_FLAG_CHEST);
    }//GEN-LAST:event_jRadioButton13ActionPerformed

    private void jRadioButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton12ActionPerformed
        onMapActionCheckboxSet(jCheckBox22, MapLayoutBlock.MAP_FLAG_RAFT);
    }//GEN-LAST:event_jRadioButton12ActionPerformed

    private void jRadioButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton11ActionPerformed
        onMapActionCheckboxSet(jCheckBox22, MapLayoutBlock.MAP_FLAG_CARAVAN);
    }//GEN-LAST:event_jRadioButton11ActionPerformed

    private void jRadioButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton10ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapLayoutBlock.MAP_FLAG_SEARCH);
    }//GEN-LAST:event_jRadioButton10ActionPerformed

    private void jRadioButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton9ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapLayoutBlock.MAP_FLAG_SHELF);
    }//GEN-LAST:event_jRadioButton9ActionPerformed

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        onMapActionCheckboxSet(jCheckBox21, MapLayoutBlock.MAP_FLAG_TRIGGER);
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapLayoutBlock.MAP_FLAG_TABLE);
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapLayoutBlock.MAP_FLAG_VASE);
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        onMapActionCheckboxSet(jCheckBox20, MapLayoutBlock.MAP_FLAG_BARREL);
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        onMapActionCheckboxSet(jCheckBox19, MapLayoutBlock.MAP_FLAG_WARP);
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        onMapActionCheckboxSet(null, MapLayoutPanel.MAP_FLAG_EDIT_BLOCK);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        onMapActionCheckboxSet(jCheckBox11, MapLayoutBlock.MAP_FLAG_STAIRS_RIGHT);
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        onMapActionCheckboxSet(jCheckBox11, MapLayoutBlock.MAP_FLAG_OBSTRUCTED);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mapLayoutPanel.revertLastAction();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTabbedPane5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane5StateChanged
        int index = jTabbedPane5.getSelectedIndex();
        if (index == 1) {   //Is on Block Editor
            jTabbedPane2.setSelectedIndex(0);
        } else if (mapLayoutPanel.getMapLayout() != null) { //Map editor
            mapLayoutPanel.getMapLayout().clearIndexedColorImage(true);
            mapLayoutPanel.redraw();
        }
    }//GEN-LAST:event_jTabbedPane5StateChanged

    private void jRadioButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton19ActionPerformed
        onMapActionCheckboxSet(jCheckBox18, MapLayoutBlock.MAP_FLAG_HIDE);
    }//GEN-LAST:event_jRadioButton19ActionPerformed

    private void jRadioButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton20ActionPerformed
        onMapActionCheckboxSet(jCheckBox18, MapLayoutBlock.MAP_FLAG_SHOW);
    }//GEN-LAST:event_jRadioButton20ActionPerformed

    private void jCheckBox26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox26ActionPerformed
        mapLayoutPanel.setSimulateParallax(jCheckBox26.isSelected());
    }//GEN-LAST:event_jCheckBox26ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        MapBlock block = null;
        int index = mapBlocksetLayoutPanel.getLeftSelectedIndex();
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
            mapBlocksetLayoutPanel.centerOnMapPoint(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        int index = mapBlocksetLayoutPanel.getLeftSelectedIndex();
        if (index < 0) return;
        if (index <= 2) {
            Console.logger().warning("WARNING Cannot delete first 3 map blocks.");
            return;
        }
        blockset.removeBlock(index);
        mapBlocksetLayoutPanel.setLeftSelectedIndex(index == 3 ? 3 : index-1);
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        int index = mapBlocksetLayoutPanel.getLeftSelectedIndex();
        if (index < 0 || index >= blockset.getBlocks().length) return;
        if (index <= 2) {
            Console.logger().warning("WARNING Cannot clone from first 3 map slots.");
            return;
        }
        MapBlock block = blockset.getBlocks()[index].clone();
        blockset.insertBlock(index+1, block);
        mapBlocksetLayoutPanel.setLeftSelectedIndex(index+1);
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jRadioButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton21ActionPerformed
        onMapActionCheckboxSet(jCheckBox17, MapLayoutBlock.MAP_FLAG_STEP);
    }//GEN-LAST:event_jRadioButton21ActionPerformed

    private void SetTabRelativeCheckbox(JCheckBox checkbox, JTable selectionTable, int mode) {
        mapLayoutPanel.setSelectedItemIndex(-1);
        if (selectionTable != null) {
            selectionTable.clearSelection();
        }
        if (checkbox == null) {
            // Restore checkboxes
            if (tabRelativeCheckbox != null) {
                tabRelativeCheckbox.setSelected(tabRelativeCheckboxState);
                tabRelativeCheckbox.setEnabled(true);
            }
            tabRelativeCheckbox = null;
        }
        else {
            if (tabRelativeCheckbox != null) {
                SetTabRelativeCheckbox(null, null, 0);
            }
            //If tabs change then disable the action tab affecting the checkboxes
            if (!mapLayoutPanel.isDrawMode_Tabs(MapLayoutPanel.DRAW_MODE_ACTION_FLAGS)) {
                JCheckBox actionCheckbox = actionRelativeCheckbox;
                int actionMode = mapLayoutPanel.getCurrentPaintMode();
                onMapActionCheckboxSet(null, -1);
                actionRelativeCheckbox = actionCheckbox;
                mapLayoutPanel.setCurrentPaintMode(actionMode);
            }
            // Lock active checkbox
            mapLayoutPanel.setDrawMode_Tabs(mode);
            tabRelativeCheckbox = checkbox;
            tabRelativeCheckboxState = checkbox.isSelected();
            tabRelativeCheckbox.setSelected(true);
            tabRelativeCheckbox.setEnabled(false);
        }
    }
    
    private void onMapActionCheckboxSet(JCheckBox checkbox, int mode) {
        if (mapLayoutPanel.getCurrentPaintMode() == mode) return;
        mapLayoutPanel.setCurrentPaintMode(mode);
        if (actionRelativeCheckbox != null) {
            // Restore checkboxes
            if (actionRelativeCheckbox != null) {
                actionRelativeCheckbox.setSelected(actionRelativeCheckboxState);
                actionRelativeCheckbox.setEnabled(true);
            }
            actionRelativeCheckbox = null;
        }
        if (checkbox != null) {
            // Lock active checkbox
            actionRelativeCheckbox = checkbox;
            actionRelativeCheckboxState = checkbox.isSelected();
            actionRelativeCheckbox.setSelected(true);
            actionRelativeCheckbox.setEnabled(false);
        }
        mapLayoutPanel.redraw();
    }
    
    private void onLeftBlockSlotChanged(ActionEvent e) {
        MapLayoutBlock block = e.getID() == -1 ? null : mapLayoutPanel.getMapLayout().getBlocks()[e.getID()];
        mapLayoutPanel.setSelectedBlock(block == null ? null : block.getMapBlock());
        mapLayoutPanel.redraw();
    }

    private void onRightBlockSlotChanged(ActionEvent e) {
        //Do nothing
    }

    private void onBlockEdited(ActionEvent e) {
        mapBlocksetLayoutPanel.getBlockset().clearIndexedColorImage(false);
        mapBlocksetLayoutPanel.redraw();
        blockSlotPanelLeft.redraw();
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
            if (tilesetLayoutPanelModified.getAnimator().isAnimating()) {
                tilesetLayoutPanelModified.setPreviewAnim(tilesetLayoutPanelModified.getAnimator().isAnimating());
            }
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
        mapLayoutPanel.getMapLayout().clearIndexedColorImage(true);
        mapLayoutPanel.redraw();
        tableAnimFrames.jTable.setRowSelectionInterval(e.getCurrentFrame(), e.getCurrentFrame());
    }
    
    private boolean onTableDataChanged(TableModelEvent e) {int row = e.getFirstRow();
        if (row == mapLayoutPanel.getSelectedItemIndex()) {
            mapLayoutPanel.redraw();
        }
        return e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT;
    }
    
    private void OnAreasTableDataChanged(TableModelEvent e) {
        if (onTableDataChanged(e)) {
            mapLayoutPanel.getMap().setAreas(mapAreaTableModel.getTableData(MapArea[].class));
        }
    }

    private void OnFlagCopiesTableDataChanged(TableModelEvent e) {
        if (onTableDataChanged(e)) {
            mapLayoutPanel.getMap().setFlagCopies(mapFlagCopyTableModel.getTableData(MapFlagCopyEvent[].class));
        }
    }

    private void OnStepCopiesTableDataChanged(TableModelEvent e) {
        if (onTableDataChanged(e)) {
            mapLayoutPanel.getMap().setStepCopies(mapStepCopyTableModel.getTableData(MapCopyEvent[].class));
        }
    }

    private void OnRoofCopiesTableDataChanged(TableModelEvent e) {
        if (onTableDataChanged(e)) {
            mapLayoutPanel.getMap().setRoofCopies(mapRoofCopyTableModel.getTableData(MapCopyEvent[].class));
        }
    }

    private void OnWarpsTableDataChanged(TableModelEvent e) {
        if (onTableDataChanged(e)) {
            mapLayoutPanel.getMap().setWarps(mapWarpTableModel.getTableData(MapWarpEvent[].class));
        }
    }

    private void OnChestItemsTableDataChanged(TableModelEvent e) {
        if (onTableDataChanged(e)) {
            mapLayoutPanel.getMap().setChestItems(mapChestItemTableModel.getTableData(MapItem[].class));
        }
    }

    private void OnOtherItemsTableDataChanged(TableModelEvent e) {
        if (onTableDataChanged(e)) {
            mapLayoutPanel.getMap().setOtherItems(mapOtherItemTableModel.getTableData(MapItem[].class));
        }
    }
    
    private void OnTableSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int index = ((ListSelectionModel)e.getSource()).getMaxSelectionIndex();
        mapLayoutPanel.setSelectedItemIndex(index);
    }
    
    private void onMapEventChanged(ActionEvent e) {
        int row = e.getID();
        if (row != -1) {
            switch (e.getActionCommand()) {
                case "Area":
                    mapAreaTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "FlagCopy":
                    mapFlagCopyTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "StepCopy":
                    mapStepCopyTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "RoofCopy":
                    mapRoofCopyTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "Warp":
                    mapWarpTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "ChestItem":
                    mapChestItemTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "OtherItem":
                    mapOtherItemTableModel.fireTableRowsUpdated(row, row);
                    break;
            }
        }
    }
    
    /**
     * To create a new Main Editor, copy the below code
     * Don't forget to change the new main class (below)
     */
    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MapEditorMainEditor().setVisible(true);  // <------ Change this class to new Main Editor class
            }
        });
    }
    /**
     * To create a new Main Editor, copy the above code
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel1;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel2;
    private com.sfc.sf2.map.block.gui.BlockSlotPanel blockSlotPanelLeft;
    private com.sfc.sf2.map.block.gui.BlockSlotPanel blockSlotPanelRight;
    private javax.swing.ButtonGroup buttonGroupMapActions;
    private javax.swing.ButtonGroup buttonGroupTileEditing;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPicker1;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerBlocks;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerBlockset;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerTileset;
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPickerTilesetAnim;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButton2;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButton4;
    private com.sfc.sf2.map.block.gui.EditableBlockSlotPanel editableBlockSlotPanel;
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
    private com.sfc.sf2.core.gui.controls.FileButton fileButton24;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton25;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton26;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton27;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton28;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton29;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton30;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton31;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton32;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton33;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton34;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton35;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton36;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton37;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton38;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton39;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton6;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton7;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton8;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton9;
    private com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon flatOptionPaneWarningIcon1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton10;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton11;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton12;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton13;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton14;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton15;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton16;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton17;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton18;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton19;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton2;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton20;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton21;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton22;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton23;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton24;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton3;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton4;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton5;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton6;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton7;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton8;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton9;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButtonSharedAnimation;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButtonSharedBlocks;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel59;
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
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelAreasDisplay;
    private javax.swing.JPanel jPanelFlagCopiesDisplay;
    private javax.swing.JPanel jPanelRoofCopiesDisplay;
    private javax.swing.JPanel jPanelStepCopiesDisplay;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton14;
    private javax.swing.JRadioButton jRadioButton15;
    private javax.swing.JRadioButton jRadioButton16;
    private javax.swing.JRadioButton jRadioButton17;
    private javax.swing.JRadioButton jRadioButton18;
    private javax.swing.JRadioButton jRadioButton19;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton20;
    private javax.swing.JRadioButton jRadioButton21;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JSpinner jSpinner8;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel mapAnimationFrameTableModel;
    private com.sfc.sf2.map.models.MapAreaTableModel mapAreaTableModel;
    private com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel mapBlocksetLayoutPanel;
    private com.sfc.sf2.map.models.MapItemTableModel mapChestItemTableModel;
    private com.sfc.sf2.map.models.MapFlagCopyEventTableModel mapFlagCopyTableModel;
    private com.sfc.sf2.map.gui.MapLayoutPanel mapLayoutPanel;
    private com.sfc.sf2.map.models.MapItemTableModel mapOtherItemTableModel;
    private com.sfc.sf2.map.models.MapRoofCopyEventTableModel mapRoofCopyTableModel;
    private com.sfc.sf2.map.models.MapStepCopyEventTableModel mapStepCopyTableModel;
    private com.sfc.sf2.map.models.MapWarpTableModel mapWarpTableModel;
    private com.sfc.sf2.core.gui.controls.Table tableAnimFrames;
    private com.sfc.sf2.core.gui.controls.Table tableAreas;
    private com.sfc.sf2.core.gui.controls.Table tableChestItems;
    private com.sfc.sf2.core.gui.controls.Table tableFlagCopies;
    private com.sfc.sf2.core.gui.controls.Table tableOtherItems;
    private com.sfc.sf2.core.gui.controls.Table tableRoofCopies;
    private com.sfc.sf2.core.gui.controls.Table tableStepCopies;
    private com.sfc.sf2.core.gui.controls.Table tableWarps;
    private com.sfc.sf2.map.block.gui.TileSlotPanel tileSlotPanelLeft;
    private com.sfc.sf2.map.block.gui.TileSlotPanel tileSlotPanelRight;
    private com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel tilesetLayoutPanelAnim;
    private com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel tilesetLayoutPanelModified;
    private com.sfc.sf2.map.block.gui.TilesetsLayoutPanel tilesetsLayoutPanel;
    // End of variables declaration//GEN-END:variables
}
