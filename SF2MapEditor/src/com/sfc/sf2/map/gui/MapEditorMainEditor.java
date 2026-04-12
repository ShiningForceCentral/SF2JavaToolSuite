/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.BasicAction;
import com.sfc.sf2.core.actions.CustomAction;
import com.sfc.sf2.core.actions.RadioButtonAction;
import com.sfc.sf2.core.actions.SpinnerAction;
import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.core.models.combobox.ComboBoxTableEditor;
import com.sfc.sf2.core.models.combobox.ComboBoxTableRenderer;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.core.settings.ViewSettings;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.MapArea;
import com.sfc.sf2.map.MapCopyEvent;
import com.sfc.sf2.map.MapEnums;
import com.sfc.sf2.map.MapFlagCopyEvent;
import com.sfc.sf2.map.MapItem;
import com.sfc.sf2.map.MapManager;
import com.sfc.sf2.map.MapWarpEvent;
import com.sfc.sf2.map.actions.MapActionData;
import com.sfc.sf2.map.animation.MapAnimation;
import com.sfc.sf2.map.animation.MapAnimationFrame;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.block.actions.BlockChangeActionData;
import com.sfc.sf2.map.block.gui.EditableBlockSlotPanel;
import com.sfc.sf2.map.layout.MapLayoutBlock;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;

/**
 *
 * @author TiMMy
 */
public class MapEditorMainEditor extends AbstractMainEditor {
    
    private final ViewSettings blockViewSettings = new ViewSettings(10, RenderScaleHelpers.RENDER_SCALE_1X);
    private final ViewSettings blockEditViewSettings = new ViewSettings();
    private final ViewSettings TilesetViewSettings = new ViewSettings(16, RenderScaleHelpers.RENDER_SCALE_2X);
    private final ViewSettings TilesetAnimViewSettings = new ViewSettings(16, RenderScaleHelpers.RENDER_SCALE_2X);
    private final ViewSettings layoutViewSettings = new ViewSettings();
    private final MapManager mapManager = new MapManager();
    
    private JCheckBox tabRelativeCheckbox;
    private boolean tabRelativeCheckboxState;
    private JCheckBox actionRelativeCheckbox;
    private boolean actionRelativeCheckboxState;
    
    private JRadioButton actionSelectedMapFlag;
    private JRadioButton actionTileButton;
    
    private MapEnums actionMapEnums;
    private int actionMapId;
    
    public MapEditorMainEditor() {
        super();
        SettingsManager.registerSettingsStore("mapBlockset", blockViewSettings);
        SettingsManager.registerSettingsStore("blockEdit", blockEditViewSettings);
        SettingsManager.registerSettingsStore("mapTileset", TilesetViewSettings);
        SettingsManager.registerSettingsStore("mapTilesetAnim", TilesetAnimViewSettings);
        SettingsManager.registerSettingsStore("mapLayout", layoutViewSettings);
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        blocksetViewPanel1.setLayoutPanel(mapBlocksetLayoutPanel, blockViewSettings);
        blockEditViewPanel1.setLayoutPanel(editableBlockSlotPanel, blockEditViewSettings);
        tilesetViewPanel1.setLayoutPanel(tilesetsLayoutPanel, TilesetViewSettings);
        tilesetAnimViewPanel1.setLayoutPanel(tilesetLayoutPanelAnim, tilesetLayoutPanelModified, this::animationActionPerformed, TilesetAnimViewSettings);
        mapViewPanel.setLayoutPanel(mapLayoutPanel, this::animationActionPerformed, layoutViewSettings);
        
        actionSelectedMapFlag = jRadioButtonPaintBlocks;
        actionTileButton = jRadioButtonApplyTile;
                
        accordionPanel1.setExpanded(false);
        accordionPanel2.setExpanded(false);
        
        infoButtonSharedAnimation.setVisible(false);
        
        //Map editing
        mapLayoutPanel.setShowInteractionFlags(false);
        mapLayoutPanel.setDrawMode_Tabs(MapLayoutPanel.DRAW_MODE_NONE);
        mapLayoutPanel.setDrawMode_Toggles(MapLayoutPanel.DRAW_MODE_ALL, false);
        
        mapLayoutPanel.setShowAreasOverlay(jCheckBoxShowUpperLayer.isSelected());
        mapLayoutPanel.setShowAreasUnderlay(jCheckBoxShowBGLayer.isSelected());
        mapLayoutPanel.setShowFlagCopyResult(jCheckBox6.isSelected());
        mapLayoutPanel.setShowStepCopyResult(jCheckBox12.isSelected());
        mapLayoutPanel.setShowRoofCopyResult(jCheckBox14.isSelected());
        mapLayoutPanel.setEventEditedListener(this::onMapEventChanged);
        
        //Blockset panel
        mapLayoutPanel.setLeftSlot(blockSlotPanelLeft);
        mapLayoutPanel.setMapBlockLayoutPanel(mapBlocksetLayoutPanel);
        mapBlocksetLayoutPanel.setLeftSlotBlockPanel(blockSlotPanelLeft);
        mapBlocksetLayoutPanel.setRightSlotBlockPanel(blockSlotPanelRight);
        mapBlocksetLayoutPanel.setCanSelectInitialBlocks(true);
        mapBlocksetLayoutPanel.setLeftSlotColor(Color.YELLOW);
        mapBlocksetLayoutPanel.setRightSlotColor(Color.MAGENTA);
        blockSlotPanelLeft.setBlockChangedListener(this::onLeftBlockSlotChanged);
        blockSlotPanelRight.setBlockChangedListener(this::onRightBlockSlotChanged);
        
        editableBlockSlotPanel.setBlockEditedListener(this::onBlockEdited);
        editableBlockSlotPanel.setLeftTileSlotPanel(tileSlotPanelLeft);
        editableBlockSlotPanel.setRightTileSlotPanel(tileSlotPanelRight);
        mapBlocksetLayoutPanel.setEditableBlockPanel(editableBlockSlotPanel);
        tilesetsLayoutPanel.setLeftSlotTilePanel(tileSlotPanelLeft);
        tilesetsLayoutPanel.setRightSlotBlockPanel(tileSlotPanelRight);
        tilesetsLayoutPanel.setBlockSlotPanel(editableBlockSlotPanel);
        
        //Animation
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
        
        //Load initial map enums to get the map names
        try {
            Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButtonEnums.getFilePath());
            mapManager.ImportMapEnums(sf2enumsPath);
            updateMapNames();
        } catch (Exception ex) {
            Console.logger().log(Level.WARNING, null, ex);
            Console.logger().warning("Could not perform initial import of SF2Enums.");
        }
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        MapActionData newValue = new MapActionData(mapManager.getMap(), mapManager.getMapEnums());
        MapActionData oldValue = new MapActionData(mapLayoutPanel.getMap(), actionMapEnums);
        ActionManager.setAndExecuteAction(new CustomAction<MapActionData>(this, "Map Imported", this::actionMapLoaded, newValue, oldValue));
    }
    
    private void actionMapLoaded(MapActionData mapData) {    
        Map map = mapData.map();
        if (map != null) {
            MapEnums mapEnums = mapData.mapEnums();
            actionMapEnums = mapEnums;
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
            
            mapLayoutPanel.setMap(map);
            MapBlockset mapBlockset = map.getBlockset();
            Tileset[] tilesets = map.getLayout().getTilesets();
            mapBlocksetLayoutPanel.setBlockset(mapBlockset);
            mapBlocksetLayoutPanel.setTilesets(tilesets);
            mapBlocksetLayoutPanel.setLeftSelectedIndex(-1);
            tilesetsLayoutPanel.setTilesets(tilesets);
            tilesetViewPanel1.setTilesets(tilesets);
            tileSlotPanelLeft.setTile(null);
            tileSlotPanelLeft.setTilesets(tilesets);
            tileSlotPanelRight.setTile(null);
            tileSlotPanelRight.setTilesets(tilesets);
            editableBlockSlotPanel.setTilesets(tilesets);
            blockSlotPanelLeft.setTilesets(tilesets);
            blockSlotPanelRight.setTilesets(tilesets);
            if (mapBlockset == null) {
                blockSlotPanelLeft.setBlock(null);
                blockSlotPanelRight.setBlock(null);
            } else {
                blockSlotPanelLeft.setBlock(mapBlockset.getBlocks()[0]);
                blockSlotPanelRight.setBlock(mapBlockset.getBlocks()[0]);
            }

            MapAnimation animation = map.getAnimation();
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

            blocksetViewPanel1.setSharedBlockInfo(mapManager.getSharedBlockInfo());
            String sharedAnimationInfo = mapManager.getSharedAnimationInfo();
            infoButtonSharedAnimation.setVisible(sharedAnimationInfo != null);
            if (sharedAnimationInfo != null) {
                infoButtonSharedAnimation.setMessageText("This animation data is also used by the following maps:\n" + sharedAnimationInfo + "\nAny changes will affect these other maps.\n\nTo unlink the maps, you can export this animation for a specific map and then update \\maps\\entries.asm");
            }
        } else {
            mapLayoutPanel.setMap(null);
            mapBlocksetLayoutPanel.setBlockset(null);
                    
            mapAreaTableModel.setTableData(null);
            mapFlagCopyTableModel.setTableData(null);
            mapStepCopyTableModel.setTableData(null);
            mapRoofCopyTableModel.setTableData(null);
            mapWarpTableModel.setTableData(null);
            mapChestItemTableModel.setTableData(null);
            mapOtherItemTableModel.setTableData(null);
            
            infoButtonSharedAnimation.setVisible(false);
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

        buttonGroupMapActions = new com.sfc.sf2.core.gui.controls.NameableButtonGroup();
        buttonGroupTileEditing = new com.sfc.sf2.core.gui.controls.NameableButtonGroup();
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
        fileButtonEnums = new com.sfc.sf2.core.gui.controls.FileButton();
        jTabbedPaneImportExport = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonPaletteEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTilesetEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonMapEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel6 = new javax.swing.JPanel();
        jButtonImportMap = new javax.swing.JButton();
        jSpinnerImportMapIndex = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabelMapNameImport = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButtonExportMap = new javax.swing.JButton();
        jSpinnerExportMapIndex = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jLabelMapNameExport = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        accordionPanel2 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonTilesets = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonBlocks = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonLayout = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonAreas = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonFlagEvents = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonStepEvents = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonRoofEvents = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonWarpEvents = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonChestItems = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonOtherItems = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonAnimations = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonPaletteEntries1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTilesetEntries1 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel37 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        infoButton23 = new com.sfc.sf2.core.gui.controls.InfoButton();
        directoryButtonImportMapDir = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jButton33 = new javax.swing.JButton();
        jPanel39 = new javax.swing.JPanel();
        directoryButtonExportMapDir = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jLabel24 = new javax.swing.JLabel();
        jButtonExportMapDir = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        fileButtonPalette = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTileset1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTileset2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTileset3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTileset4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTileset5 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonBlocks2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonLayout2 = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel5 = new javax.swing.JLabel();
        jButtonImportRawMap = new javax.swing.JButton();
        fileButtonAnimations2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTilesetEntries2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonAreas2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonFlagEvents2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonStepEvents2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonRoofEvents2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonWarps2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonChestItems2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonOtherItems2 = new com.sfc.sf2.core.gui.controls.FileButton();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel40 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButtonBlocksetImage = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonBlocksetPriority = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonExportBlockset = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        infoButton2 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButtonLayoutImage = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonLayoutFlags = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonLayoutPriority = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonExportLayout = new javax.swing.JButton();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jTabbedPaneBlockset = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mapBlocksetLayoutPanel = new com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel();
        jPanel24 = new javax.swing.JPanel();
        jButtonAddBlock = new javax.swing.JButton();
        jButtonRemoveBlock = new javax.swing.JButton();
        jButtonCloneBlock = new javax.swing.JButton();
        blocksetViewPanel1 = new com.sfc.sf2.map.block.gui.BlocksetViewPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerTilesetId = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinnerTilesetLength = new javax.swing.JSpinner();
        infoButtonSharedAnimation = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton25 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton26 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        tilesetLayoutPanelAnim = new com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel();
        tableAnimFrames = new com.sfc.sf2.core.gui.controls.Table();
        jScrollPane11 = new javax.swing.JScrollPane();
        tilesetLayoutPanelModified = new com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel();
        tilesetAnimViewPanel1 = new com.sfc.sf2.map.animation.gui.TilesetAnimViewPanel();
        jPanel48 = new javax.swing.JPanel();
        jTabbedPaneEditor = new javax.swing.JTabbedPane();
        jPanel41 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mapLayoutPanel = new com.sfc.sf2.map.gui.MapLayoutPanel();
        mapViewPanel = new com.sfc.sf2.map.gui.MapViewPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanelAreasDisplay = new javax.swing.JPanel();
        jCheckBoxShowUpperLayer = new javax.swing.JCheckBox();
        jCheckBoxShowBGLayer = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jLabelShowParallax = new javax.swing.JLabel();
        jPanelFlagCopiesDisplay = new javax.swing.JPanel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanelStepCopiesDisplay = new javax.swing.JPanel();
        jCheckBox12 = new javax.swing.JCheckBox();
        jPanelRoofCopiesDisplay = new javax.swing.JPanel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jPanel42 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tilesetsLayoutPanel = new com.sfc.sf2.map.block.gui.MapTilesetsLayoutPanel();
        tilesetViewPanel1 = new com.sfc.sf2.map.block.gui.MapTilesetViewPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        editableBlockSlotPanel = new com.sfc.sf2.map.block.gui.EditableBlockSlotPanel();
        blockEditViewPanel1 = new com.sfc.sf2.map.block.gui.BlockEditViewPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        tileSlotPanelLeft = new com.sfc.sf2.map.block.gui.TileSlotPanel();
        tileSlotPanelRight = new com.sfc.sf2.map.block.gui.TileSlotPanel();
        jRadioButtonApplyTile = new javax.swing.JRadioButton();
        jRadioButtonSetPriority = new javax.swing.JRadioButton();
        jRadioButtonFlipTile = new javax.swing.JRadioButton();
        infoButton18 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton19 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton20 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel20 = new javax.swing.JPanel();
        jTabbedPaneMapEditModes = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jRadioButtonObstructed = new javax.swing.JRadioButton();
        jRadioButtonStars = new javax.swing.JRadioButton();
        infoButton3 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton4 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel17 = new javax.swing.JPanel();
        jRadioButtonPaintBlocks = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        infoButton10 = new com.sfc.sf2.core.gui.controls.InfoButton();
        blockSlotPanelLeft = new com.sfc.sf2.map.block.gui.BlockSlotPanel();
        blockSlotPanelRight = new com.sfc.sf2.map.block.gui.BlockSlotPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jRadioButtonWarp = new javax.swing.JRadioButton();
        infoButton5 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonTrigger = new javax.swing.JRadioButton();
        infoButton6 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonLayerUp = new javax.swing.JRadioButton();
        infoButton16 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonHideRoof = new javax.swing.JRadioButton();
        infoButton21 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonShowRoof = new javax.swing.JRadioButton();
        infoButton22 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonRaft = new javax.swing.JRadioButton();
        infoButton13 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonCaravan = new javax.swing.JRadioButton();
        infoButton14 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonStep = new javax.swing.JRadioButton();
        infoButton24 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonLayoutDown = new javax.swing.JRadioButton();
        infoButton17 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel50 = new javax.swing.JPanel();
        jRadioButtonTable = new javax.swing.JRadioButton();
        infoButton11 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonShelf = new javax.swing.JRadioButton();
        infoButton9 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton8 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton7 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonVase = new javax.swing.JRadioButton();
        jRadioButtonBarrel = new javax.swing.JRadioButton();
        infoButton12 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton15 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jRadioButtonChest = new javax.swing.JRadioButton();
        jRadioButtonSearch = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        tableAreas = new com.sfc.sf2.core.gui.controls.Table();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPaneCopyFlags = new javax.swing.JTabbedPane();
        tableFlagCopies = new com.sfc.sf2.core.gui.controls.Table();
        tableStepCopies = new com.sfc.sf2.core.gui.controls.Table();
        tableRoofCopies = new com.sfc.sf2.core.gui.controls.Table();
        tableWarps = new com.sfc.sf2.core.gui.controls.Table();
        jTabbedPaneItems = new javax.swing.JTabbedPane();
        tableChestItems = new com.sfc.sf2.core.gui.controls.Table();
        tableOtherItems = new com.sfc.sf2.core.gui.controls.Table();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        buttonGroupMapActions.setName("Map Edit Group");

        buttonGroupTileEditing.setName("Tile Editing Group");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2MapEditor");

        jSplitPane1.setDividerLocation(850);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(325);
        jSplitPane2.setOneTouchExpandable(true);
        jSplitPane2.setPreferredSize(new java.awt.Dimension(830, 500));

        fileButtonEnums.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonEnums.setFilePath("..\\..\\sf2enums.asm");
        fileButtonEnums.setInfoMessage("");
        fileButtonEnums.setLabelText("Sf2enums :");
        fileButtonEnums.setName("Import Enums"); // NOI18N

        jTabbedPaneImportExport.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette, tilesets, & map data"));

        fileButtonPaletteEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonPaletteEntries.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButtonPaletteEntries.setInfoMessage("The entries file for all map palettes.");
        fileButtonPaletteEntries.setLabelText("Palette entries :");
        fileButtonPaletteEntries.setName("Import Palette Entries"); // NOI18N

        fileButtonTilesetEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonTilesetEntries.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButtonTilesetEntries.setInfoMessage("The entries file for all map tilesets.");
        fileButtonTilesetEntries.setLabelText("Tileset entries :");
        fileButtonTilesetEntries.setName("Import Tileset Entries"); // NOI18N

        fileButtonMapEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonMapEntries.setFilePath(".\\entries.asm");
        fileButtonMapEntries.setInfoMessage("The entries files for all maps.");
        fileButtonMapEntries.setLabelText("Map entries :");
        fileButtonMapEntries.setName("Import Map Entries"); // NOI18N

        javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
        accordionPanel1.setLayout(accordionPanel1Layout);
        accordionPanel1Layout.setHorizontalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonPaletteEntries, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(fileButtonTilesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonMapEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel1Layout.setVerticalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonPaletteEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonTilesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonMapEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Import"));

        jButtonImportMap.setText("Import");
        jButtonImportMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportMapActionPerformed(evt);
            }
        });

        jSpinnerImportMapIndex.setModel(new javax.swing.SpinnerNumberModel(3, 0, 100, 1));
        jSpinnerImportMapIndex.setName("Import Map Number Spinner"); // NOI18N
        jSpinnerImportMapIndex.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mapIdStateChanged(evt);
            }
        });

        jLabel4.setText("Map :");

        jLabel18.setText("Import map from entries.");

        jLabelMapNameImport.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabelMapNameImport.setForeground(new java.awt.Color(153, 153, 153));
        jLabelMapNameImport.setText("No map");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonImportMap))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerImportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelMapNameImport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerImportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabelMapNameImport, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonImportMap)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Export"));

        jLabel2.setText("Export map disassembly files");

        jButtonExportMap.setText("Export");
        jButtonExportMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportMapActionPerformed(evt);
            }
        });

        jSpinnerExportMapIndex.setModel(new javax.swing.SpinnerNumberModel(3, 0, 255, 1));
        jSpinnerExportMapIndex.setName("Export Map Number Spinner"); // NOI18N
        jSpinnerExportMapIndex.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerExportMapIndexStateChanged(evt);
            }
        });

        jLabel16.setText("Map :");

        jLabelMapNameExport.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabelMapNameExport.setForeground(new java.awt.Color(153, 153, 153));
        jLabelMapNameExport.setText("No map");

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
                        .addComponent(jSpinnerExportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelMapNameExport, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExportMap)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerExportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabelMapNameExport, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportMap)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 374, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPaneImportExport.addTab("Entries", jPanel3);

        accordionPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Map files"));

        fileButtonTilesets.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonTilesets.setFilePath("00-tilesets.asm");
        fileButtonTilesets.setInfoMessage("The map's tilesets file.");
        fileButtonTilesets.setLabelText("Tilesets :");
        fileButtonTilesets.setName("Import Tilesets"); // NOI18N

        fileButtonBlocks.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonBlocks.setFilePath("0-blocks.bin");
        fileButtonBlocks.setInfoMessage("The map's blocks file.");
        fileButtonBlocks.setLabelText("Blocks :");
        fileButtonBlocks.setName("Import Blocks"); // NOI18N

        fileButtonLayout.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonLayout.setFilePath("1-layout.bin");
        fileButtonLayout.setInfoMessage("The map's layout file.");
        fileButtonLayout.setLabelText("Layout :");
        fileButtonLayout.setName("Import Layout"); // NOI18N

        fileButtonAreas.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonAreas.setFilePath("2-areas.asm");
        fileButtonAreas.setInfoMessage("The map's areas file.");
        fileButtonAreas.setLabelText("Areas :");
        fileButtonAreas.setName("Import Areas"); // NOI18N

        fileButtonFlagEvents.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonFlagEvents.setFilePath("3-flag-events.asm");
        fileButtonFlagEvents.setInfoMessage("The map's flag events file.");
        fileButtonFlagEvents.setLabelText("Flag events :");
        fileButtonFlagEvents.setName("Import Flag Events"); // NOI18N

        fileButtonStepEvents.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonStepEvents.setFilePath("4-step-events.asm");
        fileButtonStepEvents.setInfoMessage("The map's step events file.");
        fileButtonStepEvents.setLabelText("Step events :");
        fileButtonStepEvents.setName("Import Step Events"); // NOI18N

        fileButtonRoofEvents.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonRoofEvents.setFilePath("5-roof-events.asm");
        fileButtonRoofEvents.setInfoMessage("The map's roof events file.");
        fileButtonRoofEvents.setLabelText("Roof events :");
        fileButtonRoofEvents.setName("Import Roof Events"); // NOI18N

        fileButtonWarpEvents.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonWarpEvents.setFilePath("6-warp-events.asm");
        fileButtonWarpEvents.setInfoMessage("The map's warp events file.");
        fileButtonWarpEvents.setLabelText("Warps :");
        fileButtonWarpEvents.setName("Import Warp Events"); // NOI18N

        fileButtonChestItems.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonChestItems.setFilePath("7-chest-items.asm");
        fileButtonChestItems.setInfoMessage("The map's chest items file.");
        fileButtonChestItems.setLabelText("Chest items :");
        fileButtonChestItems.setName("Import Chest Items"); // NOI18N

        fileButtonOtherItems.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonOtherItems.setFilePath("8-other-items.asm");
        fileButtonOtherItems.setInfoMessage("The map's other items file.");
        fileButtonOtherItems.setLabelText("Other items :");
        fileButtonOtherItems.setName("Import Other Items"); // NOI18N

        fileButtonAnimations.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonAnimations.setFilePath("9-animations.asm");
        fileButtonAnimations.setInfoMessage("The map's map animation file.");
        fileButtonAnimations.setLabelText("Animations :");
        fileButtonAnimations.setName("Import Animations"); // NOI18N

        fileButtonPaletteEntries1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonPaletteEntries1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButtonPaletteEntries1.setInfoMessage("The entries file for all map palettes.");
        fileButtonPaletteEntries1.setLabelText("Palette entries :");
        fileButtonPaletteEntries1.setName("Import Palette Entries"); // NOI18N

        fileButtonTilesetEntries1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonTilesetEntries1.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButtonTilesetEntries1.setInfoMessage("The entries file for all map tilesets.");
        fileButtonTilesetEntries1.setLabelText("Tileset entries :");
        fileButtonTilesetEntries1.setName("Import Tileset Entries"); // NOI18N

        javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
        accordionPanel2.setLayout(accordionPanel2Layout);
        accordionPanel2Layout.setHorizontalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonTilesets, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(fileButtonBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonLayout, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonAreas, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonFlagEvents, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonStepEvents, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonRoofEvents, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonWarpEvents, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonChestItems, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonOtherItems, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonAnimations, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonPaletteEntries1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(fileButtonTilesetEntries1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel2Layout.setVerticalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonPaletteEntries1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonTilesetEntries1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonAreas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonFlagEvents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonStepEvents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonRoofEvents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonWarpEvents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonChestItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonOtherItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonAnimations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

        jLabel23.setText("Import map from directory.");

        infoButton23.setMessageText("<html>Loads the map data by importing the above files.<br>NOTE: May not load shared files, if they are defined in other map folders (load from map entries instead).</html>");
        infoButton23.setText("");

        directoryButtonImportMapDir.setDirectoryPath(".\\entries\\map03\\");
            directoryButtonImportMapDir.setInfoMessage("");
            directoryButtonImportMapDir.setLabelText("Map dir :");
            directoryButtonImportMapDir.setName("Import Map Directory"); // NOI18N

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
                        .addComponent(directoryButtonImportMapDir, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
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
                    .addComponent(directoryButtonImportMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton33)
                    .addContainerGap())
            );

            jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder("Export :"));

            directoryButtonExportMapDir.setDirectoryPath(".\\entries\\map03\\");
                directoryButtonExportMapDir.setLabelText("Map dir :");
                directoryButtonExportMapDir.setName("Export Map Directory"); // NOI18N

                jLabel24.setText("<html>Select map directory to save map data to</html>");
                jLabel24.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                jButtonExportMapDir.setText("Export");
                jButtonExportMapDir.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonExportMapDirActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
                jPanel39.setLayout(jPanel39Layout);
                jPanel39Layout.setHorizontalGroup(
                    jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(directoryButtonExportMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel39Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonExportMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel39Layout.setVerticalGroup(
                    jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(directoryButtonExportMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonExportMapDir))
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

                jTabbedPaneImportExport.addTab("Map folder", jPanel18);

                jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Import :"));

                fileButtonPalette.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonPalette.setFilePath("..\\graphics\\maps\\mappalettes\\mappalette00.bin");
                fileButtonPalette.setInfoMessage("");
                fileButtonPalette.setLabelText("Tileset Palette :");
                fileButtonPalette.setName("Import Tileset Palette"); // NOI18N

                fileButtonTileset1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonTileset1.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset000.bin");
                fileButtonTileset1.setInfoMessage("");
                fileButtonTileset1.setLabelText("Tileset 1 :");
                fileButtonTileset1.setName("Import Tileset 1"); // NOI18N

                fileButtonTileset2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonTileset2.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset037.bin");
                fileButtonTileset2.setInfoMessage("");
                fileButtonTileset2.setLabelText("Tileset 2 :");
                fileButtonTileset2.setName("Import Tileset 2"); // NOI18N

                fileButtonTileset3.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonTileset3.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset043.bin");
                fileButtonTileset3.setInfoMessage("");
                fileButtonTileset3.setLabelText("Tileset 3 :");
                fileButtonTileset3.setName("Import Tileset 3"); // NOI18N

                fileButtonTileset4.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonTileset4.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset053.bin");
                fileButtonTileset4.setInfoMessage("");
                fileButtonTileset4.setLabelText("Tileset 4 :");
                fileButtonTileset4.setName("Import Tileset 4"); // NOI18N

                fileButtonTileset5.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonTileset5.setFilePath("..\\graphics\\maps\\maptilesets\\maptileset066.bin");
                fileButtonTileset5.setInfoMessage("");
                fileButtonTileset5.setLabelText("Tileset 5 :");
                fileButtonTileset5.setName("Import Tileset 5"); // NOI18N

                fileButtonBlocks2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonBlocks2.setFilePath(".\\entries\\map03\\0-blocks.bin");
                fileButtonBlocks2.setInfoMessage("");
                fileButtonBlocks2.setLabelText("Blocks file :");
                fileButtonBlocks2.setName("Import Blocks"); // NOI18N

                fileButtonLayout2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
                fileButtonLayout2.setFilePath(".\\entries\\map03\\1-layout.bin");
                fileButtonLayout2.setInfoMessage("");
                fileButtonLayout2.setLabelText("Layout file :");
                fileButtonLayout2.setToolTipText("");
                fileButtonLayout2.setName("Import Layout"); // NOI18N

                jLabel5.setText("<html>Select individual disassembly files.</html>");
                jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                jButtonImportRawMap.setText("Import");
                jButtonImportRawMap.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonImportRawMapActionPerformed(evt);
                    }
                });

                fileButtonAnimations2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonAnimations2.setFilePath(".\\entries\\map03\\9-animations.asm");
                fileButtonAnimations2.setInfoMessage("");
                fileButtonAnimations2.setLabelText("Animation file :");
                fileButtonAnimations2.setToolTipText("");
                fileButtonAnimations2.setName("Import Animations"); // NOI18N

                fileButtonTilesetEntries2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonTilesetEntries2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
                fileButtonTilesetEntries2.setInfoMessage("");
                fileButtonTilesetEntries2.setLabelText("Tilesets entries :");
                fileButtonTilesetEntries2.setName("Import Tilesets Entries"); // NOI18N

                fileButtonAreas2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonAreas2.setFilePath(".\\entries\\map03\\2-areas.asm");
                fileButtonAreas2.setInfoMessage("");
                fileButtonAreas2.setLabelText("Areas :");
                fileButtonAreas2.setName("Import Areas"); // NOI18N

                fileButtonFlagEvents2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonFlagEvents2.setFilePath(".\\entries\\map03\\3-flag-events.asm");
                fileButtonFlagEvents2.setInfoMessage("");
                fileButtonFlagEvents2.setLabelText("Flag events :");
                fileButtonFlagEvents2.setName("Import Flag Events"); // NOI18N

                fileButtonStepEvents2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonStepEvents2.setFilePath(".\\entries\\map03\\4-step-events.asm");
                fileButtonStepEvents2.setInfoMessage("");
                fileButtonStepEvents2.setLabelText("Step events :");
                fileButtonStepEvents2.setName("Import Skip Events"); // NOI18N

                fileButtonRoofEvents2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonRoofEvents2.setFilePath(".\\entries\\map03\\5-roof-events.asm");
                fileButtonRoofEvents2.setInfoMessage("");
                fileButtonRoofEvents2.setLabelText("Roof events :");
                fileButtonRoofEvents2.setName("Import Roof Events"); // NOI18N

                fileButtonWarps2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonWarps2.setFilePath(".\\entries\\map03\\6-warp-events.asm");
                fileButtonWarps2.setInfoMessage("");
                fileButtonWarps2.setLabelText("Warps :");
                fileButtonWarps2.setName("Import Warps"); // NOI18N

                fileButtonChestItems2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonChestItems2.setFilePath(".\\entries\\map03\\7-chest-items.asm");
                fileButtonChestItems2.setInfoMessage("");
                fileButtonChestItems2.setLabelText("Chest items :");
                fileButtonChestItems2.setName("Import Chest Items"); // NOI18N

                fileButtonOtherItems2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
                fileButtonOtherItems2.setFilePath(".\\entries\\map03\\8-other-items.asm");
                fileButtonOtherItems2.setInfoMessage("");
                fileButtonOtherItems2.setLabelText("Other items :");
                fileButtonOtherItems2.setName("Import Other Items"); // NOI18N

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButtonPalette, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonTileset1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonTileset2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonTileset3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonTileset4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonTileset5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonBlocks2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonLayout2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonTilesetEntries2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonAreas2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonFlagEvents2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonStepEvents2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonRoofEvents2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonWarps2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonChestItems2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonOtherItems2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonImportRawMap))
                            .addComponent(fileButtonAnimations2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSeparator6))
                        .addContainerGap())
                );
                jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fileButtonTilesetEntries2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(fileButtonPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonTileset1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonTileset2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonTileset3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonTileset4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonTileset5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonBlocks2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(fileButtonLayout2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonAreas2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonFlagEvents2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonStepEvents2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonRoofEvents2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonWarps2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonChestItems2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonOtherItems2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonAnimations2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonImportRawMap))
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

                jTabbedPaneImportExport.addTab("Raw files", jPanel19);

                jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder("Blockset image export"));

                jLabel11.setText("Export blockset data");

                infoButton1.setMessageText("<html><b>Image Format:</b> Select an image File (e.g. PNG or GIF).<br>Color format should be 4BPP / 16 indexed colors.<br>(Images of 8BPP / 256 indexed colors will be converted to 4 BPP / 16). <br><br><b>Priority tiles:</b> Outputs a representation of the map's blockset with 'H' = a high-priority tile, and 'L' = a regular tile (low priority).<br>High priority tells the engine to draw tiles over the sprites (i.e. player character).</html>");
                infoButton1.setText("");

                fileButtonBlocksetImage.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ANY_IMAGE);
                fileButtonBlocksetImage.setFilePath(".\\export\\blockset.png");
                fileButtonBlocksetImage.setInfoMessage("");
                fileButtonBlocksetImage.setLabelText("Blockset image :");
                fileButtonBlocksetImage.setName("Export Blockset Image"); // NOI18N

                fileButtonBlocksetPriority.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
                fileButtonBlocksetPriority.setFilePath(".\\export\\blockset_hptiles.txt");
                fileButtonBlocksetPriority.setInfoMessage("");
                fileButtonBlocksetPriority.setLabelText("Blockset priority tiles :");
                fileButtonBlocksetPriority.setName("Export Blockset Priorities"); // NOI18N

                jButtonExportBlockset.setText("Export");
                jButtonExportBlockset.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonExportBlocksetActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
                jPanel34.setLayout(jPanel34Layout);
                jPanel34Layout.setHorizontalGroup(
                    jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButtonBlocksetPriority, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(fileButtonBlocksetImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonExportBlockset)
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
                        .addComponent(fileButtonBlocksetImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonBlocksetPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonExportBlockset)
                        .addContainerGap())
                );

                jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder("Map layout image export"));

                jLabel12.setText("Export map layout data");

                infoButton2.setMessageText("<html><b>Image Format:</b> Select an image File (e.g. PNG or GIF).<br>Color format should be 4BPP / 16 indexed colors.<br>(Images of 8BPP / 256 indexed colors will be converted to 4 BPP / 16).<br><br><b>Flags:</b> Outputs a representation of the map containing flag values (i.e. if the tile has an item, warp, etc flag on it).<br><br><b>Priority tiles:</b> Outputs a representation of the map with 'H' = a high-priority tile, and 'L' = a regular tile (low priority).<br>High priority tells the engine to draw tiles over the sprites (i.e. player character).</html>");
                infoButton2.setText("");

                fileButtonLayoutImage.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ANY_IMAGE);
                fileButtonLayoutImage.setFilePath(".\\export\\layout.png");
                fileButtonLayoutImage.setInfoMessage("");
                fileButtonLayoutImage.setLabelText("Layout image :");
                fileButtonLayoutImage.setName("Export Layout Image"); // NOI18N

                fileButtonLayoutFlags.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
                fileButtonLayoutFlags.setFilePath(".\\export\\layout_flags.txt");
                fileButtonLayoutFlags.setInfoMessage("");
                fileButtonLayoutFlags.setLabelText("Layout flags :");
                fileButtonLayoutFlags.setName("Export Layout Flags"); // NOI18N

                fileButtonLayoutPriority.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
                fileButtonLayoutPriority.setFilePath(".\\export\\layout_hptiles.txt");
                fileButtonLayoutPriority.setInfoMessage("");
                fileButtonLayoutPriority.setLabelText("Layout priority tiles :");
                fileButtonLayoutPriority.setName("Export Layout Priorities"); // NOI18N

                jButtonExportLayout.setText("Export");
                jButtonExportLayout.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonExportLayoutActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
                jPanel35.setLayout(jPanel35Layout);
                jPanel35Layout.setHorizontalGroup(
                    jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButtonLayoutPriority, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(fileButtonLayoutFlags, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(fileButtonLayoutImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonExportLayout)
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
                        .addComponent(fileButtonLayoutImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonLayoutFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileButtonLayoutPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonExportLayout)
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

                jTabbedPaneImportExport.addTab("Misc.", jPanel40);

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTabbedPaneImportExport, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .addComponent(fileButtonEnums, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel9Layout.setVerticalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(fileButtonEnums, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPaneImportExport)
                        .addContainerGap())
                );

                jSplitPane2.setLeftComponent(jPanel9);

                jSplitPane3.setDividerLocation(625);
                jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
                jSplitPane3.setResizeWeight(1.0);

                jSplitPane4.setDividerLocation(350);
                jSplitPane4.setOneTouchExpandable(true);
                jSplitPane4.setPreferredSize(new java.awt.Dimension(725, 500));

                jTabbedPaneBlockset.setMinimumSize(new java.awt.Dimension(200, 200));

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

                jButtonAddBlock.setText("Add block");
                jButtonAddBlock.setMargin(new java.awt.Insets(2, 5, 3, 5));
                jButtonAddBlock.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonAddBlockActionPerformed(evt);
                    }
                });

                jButtonRemoveBlock.setText("Remove block");
                jButtonRemoveBlock.setToolTipText("");
                jButtonRemoveBlock.setMargin(new java.awt.Insets(2, 5, 3, 5));
                jButtonRemoveBlock.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonRemoveBlockActionPerformed(evt);
                    }
                });

                jButtonCloneBlock.setText("Clone selected");
                jButtonCloneBlock.setMargin(new java.awt.Insets(2, 5, 3, 5));
                jButtonCloneBlock.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButtonCloneBlockActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
                jPanel24.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                    jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonAddBlock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(jButtonCloneBlock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addComponent(jButtonRemoveBlock)
                        .addContainerGap())
                );
                jPanel24Layout.setVerticalGroup(
                    jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonAddBlock)
                            .addComponent(jButtonRemoveBlock)
                            .addComponent(jButtonCloneBlock)))
                );

                javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
                jPanel11.setLayout(jPanel11Layout);
                jPanel11Layout.setHorizontalGroup(
                    jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(blocksetViewPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel11Layout.setVerticalGroup(
                    jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blocksetViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jTabbedPaneBlockset.addTab("Blockset", jPanel11);

                jPanel23.setPreferredSize(new java.awt.Dimension(356, 500));

                jPanel21.setPreferredSize(new java.awt.Dimension(360, 765));

                jLabel8.setText("Tileset :");

                jSpinnerTilesetId.setModel(new javax.swing.SpinnerNumberModel(0, 0, 256, 1));
                jSpinnerTilesetId.setName("Tileset ID Spinner"); // NOI18N
                jSpinnerTilesetId.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jSpinnerTilesetIdStateChanged(evt);
                    }
                });

                jLabel9.setText("Tileset length :");

                jSpinnerTilesetLength.setModel(new javax.swing.SpinnerNumberModel(0, 0, 104, 1));
                jSpinnerTilesetLength.setName("TilesetLengthSpinner"); // NOI18N
                jSpinnerTilesetLength.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jSpinnerTilesetLengthStateChanged(evt);
                    }
                });

                infoButtonSharedAnimation.setIcon(flatOptionPaneWarningIcon1);
                infoButtonSharedAnimation.setText("");

                infoButton25.setMessageText("<html>The id of the tileset to load for map animations.");
                infoButton25.setText("");

                infoButton26.setMessageText("<html>The number of tiles in the map animation tileset.<br><br><br>NOTE:</b> It seems that Tileset Length values larger than 104 tiles may cause maps not to load or to become unstable.</html>");
                infoButton26.setText("");

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
                                .addComponent(jSpinnerTilesetId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSpinnerTilesetLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(infoButton25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(infoButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                    .addComponent(jSpinnerTilesetId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(infoButton25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(jSpinnerTilesetLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(infoButton26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                tableAnimFrames.setInfoMessage("<html>- <b>Start:</b> The tile index of the <i>animation tileset</i> to start this frame.<br>- <b>Length:</b> The number of tiles for the animation frame.<br>- <b>Dest Tile:</b> The index of the map's tilesets to use as the <i>destination tileset</i>.<br>- <b>Dest Index:</b> The tile index of the <i>destination tileset</i> to start replacing tiles.<br>- <b>Delay:</b> The delay before moving to the next animation frame, essentially the animation speed (smaller numbers = faster).</html>");
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

                javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
                jPanel21.setLayout(jPanel21Layout);
                jPanel21Layout.setHorizontalGroup(
                    jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(tableAnimFrames, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tilesetAnimViewPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                );
                jPanel21Layout.setVerticalGroup(
                    jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableAnimFrames, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tilesetAnimViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
                jPanel23.setLayout(jPanel23Layout);
                jPanel23Layout.setHorizontalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel23Layout.setVerticalGroup(
                    jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                        .addGap(0, 0, Short.MAX_VALUE))
                );

                jTabbedPaneBlockset.addTab("Animation", jPanel23);

                jSplitPane4.setLeftComponent(jTabbedPaneBlockset);

                jTabbedPaneEditor.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPaneEditorStateChanged(evt);
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                );
                jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                );

                mapViewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Map View"));

                jPanelAreasDisplay.setBorder(javax.swing.BorderFactory.createTitledBorder("Areas display"));

                jCheckBoxShowUpperLayer.setText("Upper layer overlay");
                jCheckBoxShowUpperLayer.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBoxShowUpperLayerActionPerformed(evt);
                    }
                });

                jCheckBoxShowBGLayer.setText("BG underlay");
                jCheckBoxShowBGLayer.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBoxShowBGLayerActionPerformed(evt);
                    }
                });

                jCheckBox26.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jCheckBox26ActionPerformed(evt);
                    }
                });

                jLabelShowParallax.setText("<html>Simulate parallax and autoscroll</html>");
                jLabelShowParallax.setVerticalAlignment(javax.swing.SwingConstants.TOP);

                javax.swing.GroupLayout jPanelAreasDisplayLayout = new javax.swing.GroupLayout(jPanelAreasDisplay);
                jPanelAreasDisplay.setLayout(jPanelAreasDisplayLayout);
                jPanelAreasDisplayLayout.setHorizontalGroup(
                    jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAreasDisplayLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxShowBGLayer, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCheckBoxShowUpperLayer, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAreasDisplayLayout.createSequentialGroup()
                                .addComponent(jCheckBox26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelShowParallax, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6))
                );
                jPanelAreasDisplayLayout.setVerticalGroup(
                    jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAreasDisplayLayout.createSequentialGroup()
                        .addComponent(jCheckBoxShowUpperLayer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxShowBGLayer)
                        .addGap(9, 9, 9)
                        .addGroup(jPanelAreasDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelShowParallax, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
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
                            .addComponent(mapViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel41Layout.setVerticalGroup(
                    jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel41Layout.createSequentialGroup()
                                .addComponent(mapViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );

                jPanel1.getAccessibleContext().setAccessibleName("");

                jTabbedPaneEditor.addTab("Map Editor", jPanel41);

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

                javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
                jPanel43.setLayout(jPanel43Layout);
                jPanel43Layout.setHorizontalGroup(
                    jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(tilesetViewPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                );
                jPanel43Layout.setVerticalGroup(
                    jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(tilesetViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel6.setText("Selected block");

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
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addComponent(editableBlockSlotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(32, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel46Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addComponent(blockEditViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                );
                jPanel46Layout.setVerticalGroup(
                    jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editableBlockSlotPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(blockEditViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

                buttonGroupTileEditing.add(jRadioButtonApplyTile);
                jRadioButtonApplyTile.setSelected(true);
                jRadioButtonApplyTile.setText("Apply tile");
                jRadioButtonApplyTile.setActionCommand("Apply tiles");
                jRadioButtonApplyTile.setName("Tile Set Radio"); // NOI18N
                jRadioButtonApplyTile.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonApplyTileItemStateChanged(evt);
                    }
                });
                jRadioButtonApplyTile.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButtonApplyTileActionPerformed(evt);
                    }
                });

                buttonGroupTileEditing.add(jRadioButtonSetPriority);
                jRadioButtonSetPriority.setText("Toggle priority flag");
                jRadioButtonSetPriority.setName("Tile Set Priority Radio"); // NOI18N
                jRadioButtonSetPriority.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonSetPriorityItemStateChanged(evt);
                    }
                });
                jRadioButtonSetPriority.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButtonSetPriorityActionPerformed(evt);
                    }
                });

                buttonGroupTileEditing.add(jRadioButtonFlipTile);
                jRadioButtonFlipTile.setText("Flip tiles");
                jRadioButtonFlipTile.setName("Tile Flip Radio"); // NOI18N
                jRadioButtonFlipTile.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonFlipTileItemStateChanged(evt);
                    }
                });
                jRadioButtonFlipTile.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jRadioButtonFlipTileActionPerformed(evt);
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
                                .addComponent(jRadioButtonApplyTile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jRadioButtonFlipTile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jRadioButtonSetPriority)
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
                            .addComponent(jRadioButtonApplyTile))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonFlipTile)
                            .addComponent(infoButton18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(infoButton19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonSetPriority))
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

                jTabbedPaneEditor.addTab("Block Editor", jPanel42);

                javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
                jPanel48.setLayout(jPanel48Layout);
                jPanel48Layout.setHorizontalGroup(
                    jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPaneEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel48Layout.setVerticalGroup(
                    jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPaneEditor)
                        .addContainerGap())
                );

                jSplitPane4.setRightComponent(jPanel48);

                javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
                jPanel8.setLayout(jPanel8Layout);
                jPanel8Layout.setHorizontalGroup(
                    jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                );
                jPanel8Layout.setVerticalGroup(
                    jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                );

                jSplitPane3.setTopComponent(jPanel8);

                jTabbedPaneMapEditModes.setMinimumSize(new java.awt.Dimension(390, 185));
                jTabbedPaneMapEditModes.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPaneMapEditModesStateChanged(evt);
                    }
                });

                jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));
                jPanel14.setMinimumSize(new java.awt.Dimension(904, 160));

                jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Exploration Flags"));

                buttonGroupMapActions.add(jRadioButtonObstructed);
                jRadioButtonObstructed.setText("Obstructed");
                jRadioButtonObstructed.setActionCommand("0xC000");
                jRadioButtonObstructed.setName("Obstructed Flag Toggle"); // NOI18N
                jRadioButtonObstructed.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonExplorationItemStateChanged(evt);
                    }
                });

                buttonGroupMapActions.add(jRadioButtonStars);
                jRadioButtonStars.setText("Stairs");
                jRadioButtonStars.setActionCommand("0x4000");
                jRadioButtonStars.setName("Stairs Flag Toggle"); // NOI18N
                jRadioButtonStars.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonExplorationItemStateChanged(evt);
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
                                .addComponent(jRadioButtonObstructed)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jRadioButtonStars)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(16, Short.MAX_VALUE))
                );
                jPanel16Layout.setVerticalGroup(
                    jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonObstructed)
                            .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButtonStars, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(infoButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Blocks"));

                buttonGroupMapActions.add(jRadioButtonPaintBlocks);
                jRadioButtonPaintBlocks.setSelected(true);
                jRadioButtonPaintBlocks.setText("Paint Blocks");
                jRadioButtonPaintBlocks.setActionCommand("0x0");
                jRadioButtonPaintBlocks.setName("Paint Blocks Toggle"); // NOI18N
                jRadioButtonPaintBlocks.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonPaintBlocksItemStateChanged(evt);
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
                                .addComponent(jRadioButtonPaintBlocks)
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
                            .addComponent(jRadioButtonPaintBlocks)
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

                buttonGroupMapActions.add(jRadioButtonWarp);
                jRadioButtonWarp.setText("Warp");
                jRadioButtonWarp.setActionCommand("0x1000");
                jRadioButtonWarp.setName("Warp Flag Toggle"); // NOI18N
                jRadioButtonWarp.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonWarpsItemStateChanged(evt);
                    }
                });

                infoButton5.setMessageText("<html><b>Warp flag:</b> Marks a block with a warp trigger. Must be used in conjunction with a Warp Event..<br>- Left click to flag a block with a warp<br>- Middle click to clear all flags from the block<br>- Right click to remove the warp flag</html>");
                infoButton5.setText("");

                buttonGroupMapActions.add(jRadioButtonTrigger);
                jRadioButtonTrigger.setText("Trigger");
                jRadioButtonTrigger.setActionCommand("0x1400");
                jRadioButtonTrigger.setName("Trigger Flag Toggle"); // NOI18N
                jRadioButtonTrigger.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonTriggersItemStateChanged(evt);
                    }
                });

                infoButton6.setMessageText("<html><b>Trigger flag:</b> Marks a block so that it will trigger a search for Copy Events.<br>- Left click to flag a block with a trigger<br>- Middle click to clear all flags from the block<br>- Right click to remove the trigger  flag</html>");
                infoButton6.setText("");

                buttonGroupMapActions.add(jRadioButtonLayerUp);
                jRadioButtonLayerUp.setText("Layer Up");
                jRadioButtonLayerUp.setActionCommand("0x2000");
                jRadioButtonLayerUp.setName("Layer Up Flag Toggle"); // NOI18N
                jRadioButtonLayerUp.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonTriggersItemStateChanged(evt);
                    }
                });

                infoButton16.setMessageText("<html><b>Layer Up flag:</b> Marks the block to shift the player (and followers) to the upper layer (the priority layer). Affected characters will be drawn above all map tiles.<br>- Left click to flag a block as a layer up block<br>- Middle click to clear all flags from the block<br>- Right click to remove the layer up flag<br><br>NOTE: When the player character is in the upper layer, flag checks will occur as if the character was in the upper region of the area (see map_06 as reference).</html>");
                infoButton16.setText("");

                buttonGroupMapActions.add(jRadioButtonHideRoof);
                jRadioButtonHideRoof.setText("Hide (Roof)");
                jRadioButtonHideRoof.setActionCommand("0x0800");
                jRadioButtonHideRoof.setName("Hide (Roof) Flag Toggle"); // NOI18N
                jRadioButtonHideRoof.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonRoofCopiesItemStateChanged(evt);
                    }
                });

                infoButton21.setMessageText("<html><b>Hide (Roof) flag:</b> Marks the block to hide the upper layer (the priority layer). Used alongside Roof Copy events.<br>- Left click to flag a block as a hide block<br>- Middle click to clear all flags from the block<br>- Right click to remove the hide flag.<br><br>NOTE: Many maps do not put the Hide flag on the door of a building but on the open doorway block that replaces the door (via Step Copy event).</html>");
                infoButton21.setText("");

                buttonGroupMapActions.add(jRadioButtonShowRoof);
                jRadioButtonShowRoof.setText("Show (Roof)");
                jRadioButtonShowRoof.setActionCommand("0x0C00");
                jRadioButtonShowRoof.setName("Show (Roof) Flag Toggle"); // NOI18N
                jRadioButtonShowRoof.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonRoofCopiesItemStateChanged(evt);
                    }
                });

                infoButton22.setMessageText("<html><b>Show (Roof) flag:</b> Marks the block to restore the last hidden section of upper layer (the priority layer). If not upper layer is hidden then does nothing.<br>- Left click to flag a block as a show block<br>- Middle click to clear all flags from the block<br>- Right click to remove the show flag.</html>");
                infoButton22.setText("");

                buttonGroupMapActions.add(jRadioButtonRaft);
                jRadioButtonRaft.setText("Raft");
                jRadioButtonRaft.setActionCommand("0x3C00");
                jRadioButtonRaft.setName("Raft Flag Toggle"); // NOI18N
                jRadioButtonRaft.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonVehiclesItemStateChanged(evt);
                    }
                });

                infoButton13.setMessageText("<html><b>Caravan flag:</b> Marks the block so the player cannot walk on it but the caravan can.<br>- Left click to flag a block as a caravan block<br>- Middle click to clear all flags from the block<br>- Right click to remove the caravan flag</html>");
                infoButton13.setText("");

                buttonGroupMapActions.add(jRadioButtonCaravan);
                jRadioButtonCaravan.setText("Caravan");
                jRadioButtonCaravan.setActionCommand("0x3800");
                jRadioButtonCaravan.setName("Caravan Flag Toggle"); // NOI18N
                jRadioButtonCaravan.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonVehiclesItemStateChanged(evt);
                    }
                });

                infoButton14.setMessageText("<html><b>Raft flag:</b> Marks the block so the player cannot walk on it but the raft can.<br>- Left click to flag a block as a raft block<br>- Middle click to clear all flags from the block<br>- Right click to remove the raft flag</html>");
                infoButton14.setText("");

                buttonGroupMapActions.add(jRadioButtonStep);
                jRadioButtonStep.setText("Step");
                jRadioButtonStep.setActionCommand("0x0400");
                jRadioButtonStep.setName("Step Flag Toggle"); // NOI18N
                jRadioButtonStep.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonStepCopiesItemStateChanged(evt);
                    }
                });

                infoButton24.setMessageText("<html><b>Step flag:</b> Marks a block so that it will trigger a search for Step Events.<br>- Left click to flag a block with a trigger<br>- Middle click to clear all flags from the block<br>- Right click to remove the step flag</html>");
                infoButton24.setText("");

                buttonGroupMapActions.add(jRadioButtonLayoutDown);
                jRadioButtonLayoutDown.setText("Layer Down");
                jRadioButtonLayoutDown.setActionCommand("0x2400");
                jRadioButtonLayoutDown.setName("Layer Down Flag Toggle"); // NOI18N
                jRadioButtonLayoutDown.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonTriggersItemStateChanged(evt);
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
                                .addComponent(jRadioButtonLayoutDown)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButtonLayerUp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButtonStep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButtonWarp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel49Layout.createSequentialGroup()
                                        .addComponent(jRadioButtonHideRoof)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel49Layout.createSequentialGroup()
                                        .addComponent(jRadioButtonShowRoof)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(infoButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButtonTrigger)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButtonCaravan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jRadioButtonRaft)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanel49Layout.setVerticalGroup(
                    jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonWarp)
                            .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonTrigger)
                            .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonStep)
                            .addComponent(infoButton24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonLayerUp)
                            .addComponent(infoButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonShowRoof)
                            .addComponent(infoButton22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonLayoutDown)
                            .addComponent(infoButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonRaft)
                            .addComponent(infoButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonCaravan)
                            .addComponent(infoButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonHideRoof)
                            .addComponent(infoButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38))
                );

                buttonGroupMapActions.add(jRadioButtonTable);
                jRadioButtonTable.setText("Table");
                jRadioButtonTable.setActionCommand("0x2800");
                jRadioButtonTable.setName("Table Flag Toggle"); // NOI18N
                jRadioButtonTable.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonItemsItemStateChanged(evt);
                    }
                });

                infoButton11.setMessageText("<html><b>Shelf flag:</b> Marks a block to be searchable with the bookshelf message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a bookshelf<br>- Middle click to clear all flags from the block<br>- Right click to remove the shelf flag<br><br>NOTE: This is separate from the map's s4_descriptions.asm</html>");
                infoButton11.setText("");

                buttonGroupMapActions.add(jRadioButtonShelf);
                jRadioButtonShelf.setText("Shelf");
                jRadioButtonShelf.setActionCommand("0x3400");
                jRadioButtonShelf.setName("Shelf Flag Toggle"); // NOI18N
                jRadioButtonShelf.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonItemsItemStateChanged(evt);
                    }
                });

                infoButton9.setMessageText("<html><b>Table flag:</b> Marks the block so that interacting with it will interact with the block on the other side of it.<br>- Left click to flag a block as a table<br>- Middle click to clear all flags from the block<br>- Right click to remove the table flag</html>");
                infoButton9.setText("");

                infoButton8.setMessageText("<html><b>Vase flag:</b> Marks a block to be searchable with the vase message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a vase<br>- Middle click to clear all flags from the block<br>- Right click to remove the vase flag</html>");
                infoButton8.setText("");

                infoButton7.setMessageText("<html><b>Barrel flag:</b> Marks a block to be searchable with the barrel message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a barrel<br>- Middle click to clear all flags from the block<br>- Right click to remove the barrel flag</html>");
                infoButton7.setText("");

                buttonGroupMapActions.add(jRadioButtonVase);
                jRadioButtonVase.setText("Vase");
                jRadioButtonVase.setActionCommand("0x2C00");
                jRadioButtonVase.setName("Vase Flag Toggle"); // NOI18N
                jRadioButtonVase.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonItemsItemStateChanged(evt);
                    }
                });

                buttonGroupMapActions.add(jRadioButtonBarrel);
                jRadioButtonBarrel.setText("Barrel");
                jRadioButtonBarrel.setActionCommand("0x3000");
                jRadioButtonBarrel.setName("Barrel Flag Toggle"); // NOI18N
                jRadioButtonBarrel.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonItemsItemStateChanged(evt);
                    }
                });

                infoButton12.setMessageText("<html><b>Search flag:</b> Marks a block to be searchable with the \"searched the area\" message. Combine with \"Other Items\" to allow player to obtain items.<br>- Left click to flag a block as a searchable<br>- Middle click to clear all flags from the block<br>- Right click to remove the search flag</html>");
                infoButton12.setText("");

                infoButton15.setMessageText("<html><b>Chest flag:</b> Marks a block to be searchable with the chest message & animation. Combine with \"Chest Items\" to allow player to obtain items.<br>- Left click to flag a block as a chest<br>- Middle click to clear all flags from the block<br>- Right click to remove the chest flag</html>");
                infoButton15.setText("");

                buttonGroupMapActions.add(jRadioButtonChest);
                jRadioButtonChest.setText("Chest");
                jRadioButtonChest.setActionCommand("0x1800");
                jRadioButtonChest.setName("Chest Flag Toggle"); // NOI18N
                jRadioButtonChest.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonItemsItemStateChanged(evt);
                    }
                });

                buttonGroupMapActions.add(jRadioButtonSearch);
                jRadioButtonSearch.setText("Search");
                jRadioButtonSearch.setActionCommand("0x1C00");
                jRadioButtonSearch.setName("Search Flag Toggle"); // NOI18N
                jRadioButtonSearch.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                        jRadioButtonItemsItemStateChanged(evt);
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
                                .addComponent(jRadioButtonShelf)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButtonSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButtonBarrel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButtonVase)
                                .addGap(10, 10, 10)
                                .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButtonChest)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jRadioButtonTable)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel50Layout.setVerticalGroup(
                    jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonSearch)
                            .addComponent(infoButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonChest)
                            .addComponent(infoButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonBarrel)
                            .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonVase)
                            .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jRadioButtonShelf)
                            .addComponent(infoButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButtonTable)
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
                    .addComponent(jSeparator1)
                    .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                );

                javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
                jPanel14.setLayout(jPanel14Layout);
                jPanel14Layout.setHorizontalGroup(
                    jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel14Layout.setVerticalGroup(
                    jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

                jTabbedPaneMapEditModes.addTab("Map Edit", jPanel4);

                tableAreas.setBorder(null);
                tableAreas.setInfoMessage("<html><b>Areas:</b> Indicates areas for bounding the camera (forcing it to stay within the region), defining upper (roof) layers, and for foreground/background effects.<br>- L1 X/Y/X'/Y': Defines a rectangle representing the area. The game camera is bound to this space.<br>- L2 F X/Y: Defines the foreground (upper) layer for the area. Used to define roofs, treetops, etc.<br>- L2 B X/Y: Defines the backgroun layer. TODO: What is it used for.<br>- L1/L2 P X/Y: Defines the parallax effect of the layer 1 or 2. Parallax causes layers to scroll at different speeds as the player character moves.<br>- L1/2 S X/Y: Defines the autoscroll speed for layers 1 & 2. Autoscroll will cause the layer to constantly scroll.<br>- Music: The music to start playing when the player character enters the area.<br><br><b>When Area row is selected:</b>Left-click to drag the closest corner or point of the area (look for the circular blue handle).</html>");
                tableAreas.setModel(mapAreaTableModel);
                tableAreas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableAreas.setSingleClickText(true);
                tableAreas.setSpinnerNumberEditor(true);
                tableAreas.setMinimumSize(new java.awt.Dimension(150, 150));
                tableAreas.setPreferredSize(new java.awt.Dimension(260, 150));
                jTabbedPaneMapEditModes.addTab("Areas", tableAreas);

                jTabbedPaneCopyFlags.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPaneCopyFlagsStateChanged(evt);
                    }
                });

                tableFlagCopies.setBorder(null);
                tableFlagCopies.setInfoMessage("<html><b>Flag copies event:</b> If a game flag is triggered when the map loads, then copies map blocks in one section of the map to another section.<br>- Flag: The game flag to trigger the event.<br>- Flag Info: A helpful description of what the flag value refers to.<br>- Source X/Y/X'/Y': Defines a rectangle to copy blocks FROM.<br>- Dest. X/Y: The top-left of the section to copy blocks TO. Uses the same width and height as the source.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When Flag Copy row is selected:</b>TODO.</html>");
                tableFlagCopies.setModel(mapFlagCopyTableModel);
                tableFlagCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableFlagCopies.setSingleClickText(true);
                tableFlagCopies.setSpinnerNumberEditor(true);
                tableFlagCopies.setMinimumSize(null);
                jTabbedPaneCopyFlags.addTab("Flag Copies", tableFlagCopies);

                tableStepCopies.setBorder(null);
                tableStepCopies.setInfoMessage("<html><b>Step copies event:</b> If the player character steps on this map block, then copies map blocks in one section of the map to another section.<br>- Trigger X/Y: The trigger position for the step copy.<br>- Source X/Y/X'/Y': Defines a rectangle to copy blocks FROM.<br>- Dest. X/Y: The top-left of the section to copy blocks TO. Uses the width and height from Source.<br>- Comment: Optional comment that is saved to the .asm file.<br><br>NOTE: Step copy triggers before the character enters the Trigger X/Y block. If the Destination X/Y is the same block as the Trigger X/Y, this can then trigger another flag/event from the copied source (e.g. see door Hide flags in most town maps).<br><br><b>When Flag Copy row is selected:</b>TODO.</html>");
                tableStepCopies.setModel(mapStepCopyTableModel);
                tableStepCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableStepCopies.setSingleClickText(true);
                tableStepCopies.setSpinnerNumberEditor(true);
                tableStepCopies.setMinimumSize(null);
                jTabbedPaneCopyFlags.addTab("Step Copies", tableStepCopies);

                tableRoofCopies.setBorder(null);
                tableRoofCopies.setInfoMessage("<html><b>Roof copy event:</b> If the player character enters the trigger block, then copies map blocks in one section of the upper layer to another section of the upper layer.<br>- Trigger X/Y: The trigger position for the step copy.<br>- Source X/Y/X'/Y': Defines a rectangle to copy blocks FROM. Set Source X/Y both to 255 to automatically detect the source position from the destination, in relation to the 1st area defined.<br>- Dest. X/Y: The top-left of the section to copy blocks TO. Uses the width and height from the Source.<br>- Comment: Optional comment that is saved to the .asm file.<br><br>NOTE: Use Show flags to reverse the previous Roof Copy (i.e. to make the roof appear again).<br><br><b>When Flag Copy row is selected:</b>TODO.</html>");
                tableRoofCopies.setModel(mapRoofCopyTableModel);
                tableRoofCopies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableRoofCopies.setSingleClickText(true);
                tableRoofCopies.setSpinnerNumberEditor(true);
                tableRoofCopies.setMinimumSize(null);
                jTabbedPaneCopyFlags.addTab("Roof Copies", tableRoofCopies);

                javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
                jPanel22.setLayout(jPanel22Layout);
                jPanel22Layout.setHorizontalGroup(
                    jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPaneCopyFlags, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                );
                jPanel22Layout.setVerticalGroup(
                    jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPaneCopyFlags, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                );

                jTabbedPaneMapEditModes.addTab("Block Copies", jPanel22);

                tableWarps.setBorder(null);
                tableWarps.setInfoMessage("<html><b>Warp event:</b> Teleports the player to a new position or a new map.<br>- Trigger X/Y: The trigger point for the warp.<br>- Scroll Dir: The direction that the camera scrolls when warping (used for overworld maps).<br> - Dest. Map: The map to warp to. Set to \"CURRENT\" to warp to a different position on the current map.<br>- Dest X/Y: The destination position to warp to (on this map or another.<br>- Facing: The direction to face at the warp destination.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When a warp row is selected:</b> Left click and drag will set the set the Trigger or Destination X/Y position of the selected item event, whichever is closer to the cursor.</html>");
                tableWarps.setModel(mapWarpTableModel);
                tableWarps.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableWarps.setSingleClickText(true);
                tableWarps.setSpinnerNumberEditor(true);
                tableWarps.setMinimumSize(new java.awt.Dimension(260, 150));
                jTabbedPaneMapEditModes.addTab("Warps", tableWarps);

                jTabbedPaneItems.addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        jTabbedPaneItemsStateChanged(evt);
                    }
                });

                tableChestItems.setBorder(null);
                tableChestItems.setInfoMessage("<html><b>Item event:</b> Allows player to aquire items.<br>- X/Y: The position of the item event.<br>- Flag: The flag that is written when the item event is triggered (when the item is aquired).<br> - Flag Info: A helpful description of what the flag value refers to.<br>- Item: The item that is acquired by the event. Set to \"NOTHING\" for no item.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When an item row is selected:</b> Left click will set the new X/Y position of the selected item event.</html>");
                tableChestItems.setModel(mapChestItemTableModel);
                tableChestItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableChestItems.setSingleClickText(true);
                tableChestItems.setSpinnerNumberEditor(true);
                tableChestItems.setMinimumSize(null);
                jTabbedPaneItems.addTab("Chest Items", tableChestItems);

                tableOtherItems.setBorder(null);
                tableOtherItems.setInfoMessage("<html><b>Item event:</b> Allows player to aquire items.<br>- X/Y: The position of the item event.<br>- Flag: The flag that is written when the item event is triggered (when the item is aquired).<br> - Flag Info: A helpful description of what the flag value refers to.<br>- Item: The item that is acquired by the event. Set to \"NOTHING\" for no item.<br>- Comment: Optional comment that is saved to the .asm file.<br><br><b>When an item row is selected:</b> Left click will set the new X/Y position of the selected item event.</html>");
                tableOtherItems.setModel(mapOtherItemTableModel);
                tableOtherItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                tableOtherItems.setSingleClickText(true);
                tableOtherItems.setSpinnerNumberEditor(true);
                tableOtherItems.setMinimumSize(null);
                jTabbedPaneItems.addTab("Other Items", tableOtherItems);

                jTabbedPaneMapEditModes.addTab("Items", jTabbedPaneItems);

                javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
                jPanel20.setLayout(jPanel20Layout);
                jPanel20Layout.setHorizontalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPaneMapEditModes, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel20Layout.setVerticalGroup(
                    jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPaneMapEditModes, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
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
                        .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 844, Short.MAX_VALUE)
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
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, Short.MAX_VALUE)
                );

                setSize(new java.awt.Dimension(1466, 1008));
                setLocationRelativeTo(null);
            }// </editor-fold>//GEN-END:initComponents

    private void jButtonImportMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportMapActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButtonPaletteEntries.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonTilesetEntries.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButtonMapEntries.getFilePath());
        Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButtonEnums.getFilePath());
        int mapId = (int)jSpinnerImportMapIndex.getValue();
        try {
            mapManager.ImportMapEnums(sf2enumsPath);
            mapManager.importDisassemblyFromEntries(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, mapId);
            jSpinnerExportMapIndex.setValue(mapId);
        } catch (Exception ex) {
            mapManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be imported for map : " + mapId);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButtonImportMapActionPerformed

    private void jButtonExportBlocksetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportBlocksetActionPerformed
        Path blocketImagePath = PathHelpers.getBasePath().resolve(fileButtonBlocksetImage.getFilePath());
        Path priorityPath = PathHelpers.getBasePath().resolve(fileButtonBlocksetPriority.getFilePath());
        int blocksPerRow = (int)blocksetViewPanel1.getItemsPerRowSpinner().getValue();
        if (!PathHelpers.createPathIfRequred(blocketImagePath)) return;
        if (!PathHelpers.createPathIfRequred(priorityPath)) return;
        try {
            mapManager.exportMapBlocksetImage(blocketImagePath, priorityPath, blocksPerRow, mapLayoutPanel.getMap().getBlockset(), mapLayoutPanel.getMapLayout().getTilesets());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map blockset data could not be exported to : " + blocketImagePath);
        }
    }//GEN-LAST:event_jButtonExportBlocksetActionPerformed

    private void jButtonExportLayoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportLayoutActionPerformed
        Path layoutImagePath = PathHelpers.getBasePath().resolve(fileButtonLayoutImage.getFilePath());
        Path layoutFlagsPath = PathHelpers.getBasePath().resolve(fileButtonLayoutFlags.getFilePath());
        Path priorityPath = PathHelpers.getBasePath().resolve(fileButtonLayoutPriority.getFilePath());
        if (!PathHelpers.createPathIfRequred(layoutImagePath)) return;
        if (!PathHelpers.createPathIfRequred(layoutFlagsPath)) return;
        if (!PathHelpers.createPathIfRequred(priorityPath)) return;
        try {
            mapManager.exportMapLayoutImage(layoutImagePath, layoutFlagsPath, priorityPath, mapLayoutPanel.getMap().getLayout());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map layout data could not be exported to : " + layoutImagePath);
        }
    }//GEN-LAST:event_jButtonExportLayoutActionPerformed
	
    private void jButtonExportMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportMapActionPerformed
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButtonMapEntries.getFilePath());
        int mapId = (int)jSpinnerExportMapIndex.getValue();
        try {
            mapManager.exportDisassemblyFromMapEntries(mapEntriesPath, mapId, mapLayoutPanel.getMap());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be exported to : " + mapId);
        }
    }//GEN-LAST:event_jButtonExportMapActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButtonImportMapDir.getDirectoryPath());
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButtonPaletteEntries1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonTilesetEntries1.getFilePath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButtonTilesets.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButtonBlocks.getFilePath());
        Path mapLayoutDataPath = mapDirectory.resolve(fileButtonLayout.getFilePath());
        Path mapAreasDataPath = mapDirectory.resolve(fileButtonAreas.getFilePath());
        Path mapFlagsDataPath = mapDirectory.resolve(fileButtonFlagEvents.getFilePath());
        Path mapStepsDataPath = mapDirectory.resolve(fileButtonStepEvents.getFilePath());
        Path mapRoofsDataPath = mapDirectory.resolve(fileButtonRoofEvents.getFilePath());
        Path mapWarpsDataPath = mapDirectory.resolve(fileButtonWarpEvents.getFilePath());
        Path mapChestItemsDataPath = mapDirectory.resolve(fileButtonChestItems.getFilePath());
        Path mapOtherItemsDataPath = mapDirectory.resolve(fileButtonOtherItems.getFilePath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButtonAnimations.getFilePath());
        Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButtonEnums.getFilePath());
        try {
            mapManager.ImportMapEnums(sf2enumsPath);
            mapManager.importDisassemblyFromData(paletteEntriesPath, tilesetEntriesPath, mapTilesetDataPath, mapBlocksetDataPath, mapLayoutDataPath, mapAreasDataPath,
                    mapFlagsDataPath, mapStepsDataPath, mapRoofsDataPath, mapWarpsDataPath, mapChestItemsDataPath, mapOtherItemsDataPath, mapAnimationDataPath);
            directoryButtonExportMapDir.setDirectoryPath(directoryButtonImportMapDir.getDirectoryPath());
        } catch (Exception ex) {
            mapManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be imported from : " + mapAnimationDataPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButtonExportMapDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportMapDirActionPerformed
        Path mapDirectory = PathHelpers.getBasePath().resolve(directoryButtonExportMapDir.getDirectoryPath());
        Path mapTilesetDataPath = mapDirectory.resolve(fileButtonTilesets.getFilePath());
        Path mapBlocksetDataPath = mapDirectory.resolve(fileButtonBlocks.getFilePath());
        Path mapLayoutDataPath = mapDirectory.resolve(fileButtonLayout.getFilePath());
        Path mapAreasDataPath = mapDirectory.resolve(fileButtonAreas.getFilePath());
        Path mapFlagsDataPath = mapDirectory.resolve(fileButtonFlagEvents.getFilePath());
        Path mapStepsDataPath = mapDirectory.resolve(fileButtonStepEvents.getFilePath());
        Path mapRoofsDataPath = mapDirectory.resolve(fileButtonRoofEvents.getFilePath());
        Path mapWarpsDataPath = mapDirectory.resolve(fileButtonWarpEvents.getFilePath());
        Path mapChestItemsDataPath = mapDirectory.resolve(fileButtonChestItems.getFilePath());
        Path mapOtherItemsDataPath = mapDirectory.resolve(fileButtonOtherItems.getFilePath());
        Path mapAnimationDataPath = mapDirectory.resolve(fileButtonAnimations.getFilePath());
        if (!PathHelpers.createPathIfRequred(mapDirectory)) return;
        try {
            mapManager.exportDisassemblyFromData(mapTilesetDataPath, mapBlocksetDataPath, mapLayoutDataPath, mapAreasDataPath, mapFlagsDataPath, mapStepsDataPath, mapRoofsDataPath,
                    mapWarpsDataPath, mapChestItemsDataPath, mapOtherItemsDataPath, mapAnimationDataPath, mapLayoutPanel.getMap());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map disasm could not be exported to : " + mapDirectory);
        }
    }//GEN-LAST:event_jButtonExportMapDirActionPerformed

    private void jButtonImportRawMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportRawMapActionPerformed
        Path palettePath = PathHelpers.getBasePath().resolve(fileButtonPalette.getFilePath());
        Path tilesetPath1 = PathHelpers.getBasePath().resolve(fileButtonTileset1.getFilePath());
        Path tilesetPath2 = PathHelpers.getBasePath().resolve(fileButtonTileset2.getFilePath());
        Path tilesetPath3 = PathHelpers.getBasePath().resolve(fileButtonTileset3.getFilePath());
        Path tilesetPath4 = PathHelpers.getBasePath().resolve(fileButtonTileset4.getFilePath());
        Path tilesetPath5 = PathHelpers.getBasePath().resolve(fileButtonTileset5.getFilePath());
        Path blocksPath = PathHelpers.getBasePath().resolve(fileButtonBlocks2.getFilePath());
        Path layoutPath = PathHelpers.getBasePath().resolve(fileButtonLayout2.getFilePath());
        Path areasPath = PathHelpers.getBasePath().resolve(fileButtonAreas2.getFilePath());
        Path flagsPath = PathHelpers.getBasePath().resolve(fileButtonFlagEvents2.getFilePath());
        Path stepsPath = PathHelpers.getBasePath().resolve(fileButtonStepEvents2.getFilePath());
        Path roofsPath = PathHelpers.getBasePath().resolve(fileButtonRoofEvents2.getFilePath());
        Path warpsPath = PathHelpers.getBasePath().resolve(fileButtonWarps2.getFilePath());
        Path chestItemsPath = PathHelpers.getBasePath().resolve(fileButtonChestItems2.getFilePath());
        Path otherItemsPath = PathHelpers.getBasePath().resolve(fileButtonOtherItems2.getFilePath());
        Path animationPath = PathHelpers.getBasePath().resolve(fileButtonAnimations2.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonTilesetEntries2.getFilePath());
        Path sf2enumsPath = PathHelpers.getBasePath().resolve(fileButtonEnums.getFilePath());
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
    }//GEN-LAST:event_jButtonImportRawMapActionPerformed

    private void jRadioButtonApplyTileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonApplyTileActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_PAINT_TILE);
            blockSlot.setShowPriority(blockEditViewPanel1.getPriorityCheckBox().isSelected());
        }
    }//GEN-LAST:event_jRadioButtonApplyTileActionPerformed

    private void jRadioButtonSetPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSetPriorityActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_PRIORITY);
            blockSlot.setShowPriority(true);
        }
    }//GEN-LAST:event_jRadioButtonSetPriorityActionPerformed

    private void jRadioButtonFlipTileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonFlipTileActionPerformed
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_FLIP);
            blockSlot.setShowPriority(blockEditViewPanel1.getPriorityCheckBox().isSelected());
        }
    }//GEN-LAST:event_jRadioButtonFlipTileActionPerformed

    private void jTabbedPaneMapEditModesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneMapEditModesStateChanged
        ActionManager.setExternalActionTriggering(true);
        onTabRelativeCheckboxSet(null, null, MapLayoutPanel.DRAW_MODE_NONE);
        jPanelAreasDisplay.setVisible(false);
        jPanelFlagCopiesDisplay.setVisible(false);
        jPanelStepCopiesDisplay.setVisible(false);
        jPanelRoofCopiesDisplay.setVisible(false);
        int index = jTabbedPaneMapEditModes.getSelectedIndex();
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
            onTabRelativeCheckboxSet(mapViewPanel.getjCheckBoxShowAreas(), tableAreas.jTable, MapLayoutPanel.DRAW_MODE_AREAS);
            jPanelAreasDisplay.setVisible(true);
            break;
            case 2:     //Block Copies panels
            jTabbedPaneCopyFlagsStateChanged(new ChangeEvent(jTabbedPaneCopyFlags));
            break;
            case 3:     //Warps panel
            onTabRelativeCheckboxSet(mapViewPanel.getjCheckBoxShowWarps(), tableWarps.jTable, MapLayoutPanel.DRAW_MODE_WARPS);
            break;
            case 4:     //Items panel
            jTabbedPaneItemsStateChanged(new ChangeEvent(jTabbedPaneItems));
            break;
        }
        mapLayoutPanel.redraw();
        ActionManager.setExternalActionTriggering(false);
    }//GEN-LAST:event_jTabbedPaneMapEditModesStateChanged

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
                Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonTilesetEntries.getFilePath());
                Tileset tileset = mapManager.importAnimationTileset(tilesetLayoutPanelAnim.getMapAnimation(), mapLayoutPanel.getMap().getLayout().getPalette(), tilesetEntriesPath, value);
                tilesetLayoutPanelAnim.setTileset(tileset);
            } catch (Exception ex) {
                Console.logger().log(Level.SEVERE, null, ex);
                Console.logger().severe("ERROR Tileset could not be imported for tileset : " + value);
            }
        }
    }//GEN-LAST:event_jSpinnerTilesetIdStateChanged

    private void jTabbedPaneItemsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneItemsStateChanged
        int index = jTabbedPaneMapEditModes.getSelectedIndex();
        if (index != 4) return; //Is not on Block copies panel
        ActionManager.setExternalActionTriggering(true);
        onTabRelativeCheckboxSet(null, null, MapLayoutPanel.DRAW_MODE_NONE);
        index = jTabbedPaneItems.getSelectedIndex();
        switch (index) {
            default:
            return;
            case 0:     //Chest items
            onTabRelativeCheckboxSet(mapViewPanel.getjCheckBoxShowItems(), tableChestItems.jTable, MapLayoutPanel.DRAW_MODE_CHEST_ITEMS);
            break;
            case 1:     //Other items
            onTabRelativeCheckboxSet(mapViewPanel.getjCheckBoxShowItems(), tableOtherItems.jTable, MapLayoutPanel.DRAW_MODE_OTHER_ITEMS);
            break;
        }
        mapLayoutPanel.redraw();
        ActionManager.setExternalActionTriggering(false);
    }//GEN-LAST:event_jTabbedPaneItemsStateChanged

    private void jTabbedPaneCopyFlagsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneCopyFlagsStateChanged
        int index = jTabbedPaneMapEditModes.getSelectedIndex();
        if (index != 2) return; //Is not on Block copies panel
        ActionManager.setExternalActionTriggering(true);
        onTabRelativeCheckboxSet(null, null, MapLayoutPanel.DRAW_MODE_NONE);
        jPanelAreasDisplay.setVisible(false);
        jPanelFlagCopiesDisplay.setVisible(false);
        jPanelStepCopiesDisplay.setVisible(false);
        jPanelRoofCopiesDisplay.setVisible(false);
        index = jTabbedPaneCopyFlags.getSelectedIndex();
        switch (index) {
            default:
            return;
            case 0:     //Flag copies
            onTabRelativeCheckboxSet(mapViewPanel.getjCheckBoxShowFlagCopies(), tableFlagCopies.jTable, MapLayoutPanel.DRAW_MODE_FLAG_COPIES);
            jPanelFlagCopiesDisplay.setVisible(true);
            break;
            case 1:     //Step copies
            onTabRelativeCheckboxSet(mapViewPanel.getjCheckBoxShowStepCopies(), tableStepCopies.jTable, MapLayoutPanel.DRAW_MODE_STEP_COPIES);
            jPanelStepCopiesDisplay.setVisible(true);
            break;
            case 2:     //Roof copies
            onTabRelativeCheckboxSet(mapViewPanel.getjCheckBoxShowRoofCopies(), tableRoofCopies.jTable, MapLayoutPanel.DRAW_MODE_ROOF_COPIES);
            jPanelRoofCopiesDisplay.setVisible(true);
            break;
        }
        mapLayoutPanel.redraw();
        ActionManager.setExternalActionTriggering(false);
    }//GEN-LAST:event_jTabbedPaneCopyFlagsStateChanged

    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        mapLayoutPanel.setShowRoofCopyResult(jCheckBox14.isSelected());
    }//GEN-LAST:event_jCheckBox14ActionPerformed

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        mapLayoutPanel.setShowStepCopyResult(jCheckBox12.isSelected());
    }//GEN-LAST:event_jCheckBox12ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        mapLayoutPanel.setShowFlagCopyResult(jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBoxShowBGLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxShowBGLayerActionPerformed
        mapLayoutPanel.setShowAreasUnderlay(jCheckBoxShowBGLayer.isSelected());
    }//GEN-LAST:event_jCheckBoxShowBGLayerActionPerformed

    private void jCheckBoxShowUpperLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxShowUpperLayerActionPerformed
        mapLayoutPanel.setShowAreasOverlay(jCheckBoxShowUpperLayer.isSelected());
    }//GEN-LAST:event_jCheckBoxShowUpperLayerActionPerformed

    private void jTabbedPaneEditorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneEditorStateChanged
        ActionManager.setExternalActionTriggering(true);
        int index = jTabbedPaneEditor.getSelectedIndex();
        if (index == 1) {   //Is on Block Editor
            jTabbedPaneMapEditModes.setSelectedIndex(0);
        } else if (mapLayoutPanel.getMapLayout() != null) { //Map editor
            mapLayoutPanel.getMapLayout().clearIndexedColorImage(true);
            mapLayoutPanel.redraw();
        }
        ActionManager.setExternalActionTriggering(false);
    }//GEN-LAST:event_jTabbedPaneEditorStateChanged

    private void jCheckBox26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox26ActionPerformed
        mapLayoutPanel.setSimulateParallax(jCheckBox26.isSelected());
    }//GEN-LAST:event_jCheckBox26ActionPerformed

    private void jButtonAddBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddBlockActionPerformed
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
        BlockChangeActionData data = new BlockChangeActionData(block, index);
        ActionManager.setAndExecuteAction(new CustomAction<BlockChangeActionData>(this, "Add Block", this::actionAddBlock, data, this::actionRemoveBlock, data));
    }//GEN-LAST:event_jButtonAddBlockActionPerformed

    private void jButtonRemoveBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveBlockActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        int index = editableBlockSlotPanel.getBlockIndex();
        if (index < 0) return;
        if (index <= 2) {
            Console.logger().warning("WARNING Cannot delete first 3 map blocks.");
            return;
        }
        MapBlock block = blockset.getBlocks()[index];
        BlockChangeActionData data = new BlockChangeActionData(block, index);
        ActionManager.setAndExecuteAction(new CustomAction<BlockChangeActionData>(this, "Remove Block", this::actionRemoveBlock, data, this::actionInsertBlock, data));
    }//GEN-LAST:event_jButtonRemoveBlockActionPerformed

    private void jButtonCloneBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloneBlockActionPerformed
        if (mapBlocksetLayoutPanel.getBlockset() == null) return;
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        int index = editableBlockSlotPanel.getBlockIndex();
        if (index < 0 || index >= blockset.getBlocks().length) return;
        if (index <= 2) {
            Console.logger().warning("WARNING Cannot clone from first 3 map slots.");
            return;
        }
        MapBlock block = blockset.getBlocks()[index];
        BlockChangeActionData data = new BlockChangeActionData(block, index+1);
        ActionManager.setAndExecuteAction(new CustomAction<BlockChangeActionData>(this, "Clone Block", this::actionAddBlock, data, this::actionRemoveBlock, data));
    }//GEN-LAST:event_jButtonCloneBlockActionPerformed

    private void jRadioButtonExplorationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonExplorationItemStateChanged
        onFlagSelectItemStateChanged(mapViewPanel.getjCheckBoxShowExplorationFlags(), (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonExplorationItemStateChanged

    private void jRadioButtonStepCopiesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonStepCopiesItemStateChanged
        onFlagSelectItemStateChanged(mapViewPanel.getjCheckBoxShowStepCopies(), (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonStepCopiesItemStateChanged

    private void jRadioButtonTriggersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonTriggersItemStateChanged
        onFlagSelectItemStateChanged(mapViewPanel.getjCheckBoxShowTriggers(), (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonTriggersItemStateChanged

    private void jRadioButtonWarpsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonWarpsItemStateChanged
        onFlagSelectItemStateChanged(mapViewPanel.getjCheckBoxShowWarps(), (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonWarpsItemStateChanged

    private void jRadioButtonRoofCopiesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonRoofCopiesItemStateChanged
        onFlagSelectItemStateChanged(mapViewPanel.getjCheckBoxShowRoofCopies(), (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonRoofCopiesItemStateChanged

    private void jRadioButtonVehiclesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonVehiclesItemStateChanged
        onFlagSelectItemStateChanged(mapViewPanel.getjCheckBoxShowVehicles(), (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonVehiclesItemStateChanged

    private void jRadioButtonItemsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonItemsItemStateChanged
        onFlagSelectItemStateChanged(mapViewPanel.getjCheckBoxShowItems(), (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonItemsItemStateChanged

    private void jRadioButtonPaintBlocksItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonPaintBlocksItemStateChanged
        onFlagSelectItemStateChanged(null, (JRadioButton)evt.getSource());
    }//GEN-LAST:event_jRadioButtonPaintBlocksItemStateChanged
    
    private void onFlagSelectItemStateChanged(JCheckBox checkbox, JRadioButton radioButton) {                                                        
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new RadioButtonAction(buttonGroupMapActions, radioButton, actionSelectedMapFlag));
        }
        actionSelectedMapFlag = radioButton;
        int value = Integer.decode(radioButton.getActionCommand());
        ActionManager.setExternalActionTriggering(true);
        onMapActionCheckboxSet(checkbox, value);
        ActionManager.setExternalActionTriggering(false);
    }
    
    private void mapIdStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mapIdStateChanged
        if (!ActionManager.isActionTriggering()) {
            JSpinner source = (JSpinner)evt.getSource();
            int mapID = (int)source.getValue();
            ActionManager.setAndExecuteAction(new BasicAction<Integer>(this, "Map ID Changed", this::actionMapIdStateChanged, mapID, actionMapId));
        }
    }//GEN-LAST:event_mapIdStateChanged

    private void jRadioButtonApplyTileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonApplyTileItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new RadioButtonAction(buttonGroupTileEditing, jRadioButtonApplyTile, actionTileButton));
        }
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_PAINT_TILE);
            blockSlot.setShowPriority(blockEditViewPanel1.getPriorityCheckBox().isSelected());
        }
        if (jRadioButtonApplyTile.isSelected()) {
            actionTileButton = jRadioButtonApplyTile;
        }
    }//GEN-LAST:event_jRadioButtonApplyTileItemStateChanged

    private void jRadioButtonFlipTileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonFlipTileItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new RadioButtonAction(buttonGroupTileEditing, jRadioButtonFlipTile, actionTileButton));
        }
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_FLIP);
            blockSlot.setShowPriority(blockEditViewPanel1.getPriorityCheckBox().isSelected());
        }
        if (jRadioButtonFlipTile.isSelected()) {
            actionTileButton = jRadioButtonFlipTile;
        }
    }//GEN-LAST:event_jRadioButtonFlipTileItemStateChanged

    private void jRadioButtonSetPriorityItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonSetPriorityItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new RadioButtonAction(buttonGroupTileEditing, jRadioButtonSetPriority, actionTileButton));
        }
        EditableBlockSlotPanel blockSlot = tilesetsLayoutPanel.getBlockSlotPanel();
        if (blockSlot != null) {
            blockSlot.setCurrentMode(EditableBlockSlotPanel.BlockSlotEditMode.MODE_TOGGLE_PRIORITY);
            blockSlot.setShowPriority(true);
        }
        if (jRadioButtonSetPriority.isSelected()) {
            actionTileButton = jRadioButtonSetPriority;
        }
    }//GEN-LAST:event_jRadioButtonSetPriorityItemStateChanged

    private void jSpinnerExportMapIndexStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerExportMapIndexStateChanged
        updateMapNames();
    }//GEN-LAST:event_jSpinnerExportMapIndexStateChanged
    
    private void actionAddBlock(BlockChangeActionData data) {
        //Clone and add the block
        actionAddBlock(data.index(), data.block(), true);
    }
    
    private void actionInsertBlock(BlockChangeActionData data) {
        //Insert without cloning
        actionAddBlock(data.index(), data.block(), false);
    }
    
    private void actionAddBlock(int index, MapBlock block, boolean clone) {//Insert without cloning
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();                                        
        blockset.insertBlock(index, block, clone);
        mapBlocksetLayoutPanel.setLeftSelectedIndex(index);
        if (index == blockset.getBlocks().length-1) { //Scroll to bottom
            mapBlocksetLayoutPanel.centerOnMapPoint(Integer.MAX_VALUE, Integer.MAX_VALUE);
        } else {
            mapBlocksetLayoutPanel.scrollToIndex(index, blockset.getBlocks().length);
        }
    }
    
    private void actionRemoveBlock(BlockChangeActionData data) {
        MapBlockset blockset = mapBlocksetLayoutPanel.getBlockset();
        blockset.removeBlock(data.index());
        mapBlocksetLayoutPanel.setLeftSelectedIndex(data.index() <= 3 ? 3 : data.index()-1);
    }

    private void onBlockEdited(ActionEvent e) {
        if (jTabbedPaneBlockset.getSelectedIndex() == 0) {
            mapBlocksetLayoutPanel.getBlockset().clearIndexedColorImage(false);
            mapBlocksetLayoutPanel.redraw();
            blockSlotPanelLeft.redraw();
        }
        if (jTabbedPaneEditor.getSelectedIndex() == 0) {
            mapLayoutPanel.redraw();
        }
    }
    
    private void actionMapIdStateChanged(int mapID) {
        actionMapId = mapID;
        jSpinnerImportMapIndex.setValue(mapID);
        jSpinnerExportMapIndex.setValue(mapID);
        updateMapNames();
    }
    
    private void onTabRelativeCheckboxSet(JCheckBox checkbox, JTable selectionTable, int mode) {
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
                onTabRelativeCheckboxSet(null, null, 0);
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
    
    private void animationActionPerformed(java.awt.event.ActionEvent evt) {                                          
        boolean isSelected = evt.getID() == 1;
        mapViewPanel.getCheckBoxPreviewAnim().setSelected(isSelected);
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
    
    private void updateMapNames() {
        if (mapManager.getMapEnums() == null) {
            String text = "Load a map first";
            jLabelMapNameImport.setText(text);
            jLabelMapNameExport.setText(text);
        } else {
            Object[] mapNames = mapManager.getMapEnums().getMaps().keySet().toArray();
            int mapID = (int)jSpinnerImportMapIndex.getValue();
            if (mapID >= 0 && mapID < mapNames.length) {
                jLabelMapNameImport.setText(mapNames[mapID].toString());
            } else {
                jLabelMapNameImport.setText("Unknown Map");
            }
            mapID = (int)jSpinnerExportMapIndex.getValue();
            if (mapID >= 0 && mapID < mapNames.length) {
                jLabelMapNameExport.setText(mapNames[mapID].toString());
            } else {
                jLabelMapNameExport.setText("Unknown Map");
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
    private com.sfc.sf2.map.block.gui.BlockEditViewPanel blockEditViewPanel1;
    private com.sfc.sf2.map.block.gui.BlockSlotPanel blockSlotPanelLeft;
    private com.sfc.sf2.map.block.gui.BlockSlotPanel blockSlotPanelRight;
    private com.sfc.sf2.map.block.gui.BlocksetViewPanel blocksetViewPanel1;
    private com.sfc.sf2.core.gui.controls.NameableButtonGroup buttonGroupMapActions;
    private com.sfc.sf2.core.gui.controls.NameableButtonGroup buttonGroupTileEditing;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonExportMapDir;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonImportMapDir;
    private com.sfc.sf2.map.block.gui.EditableBlockSlotPanel editableBlockSlotPanel;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonAnimations;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonAnimations2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonAreas;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonAreas2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonBlocks;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonBlocks2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonBlocksetImage;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonBlocksetPriority;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonChestItems;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonChestItems2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonEnums;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonFlagEvents;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonFlagEvents2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonLayout;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonLayout2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonLayoutFlags;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonLayoutImage;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonLayoutPriority;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonMapEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonOtherItems;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonOtherItems2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonPalette;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonPaletteEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonPaletteEntries1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonRoofEvents;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonRoofEvents2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonStepEvents;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonStepEvents2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTileset1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTileset2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTileset3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTileset4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTileset5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTilesetEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTilesetEntries1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTilesetEntries2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTilesets;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonWarpEvents;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonWarps2;
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
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton25;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton26;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton3;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton4;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton5;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton6;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton7;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton8;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton9;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButtonSharedAnimation;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButtonAddBlock;
    private javax.swing.JButton jButtonCloneBlock;
    private javax.swing.JButton jButtonExportBlockset;
    private javax.swing.JButton jButtonExportLayout;
    private javax.swing.JButton jButtonExportMap;
    private javax.swing.JButton jButtonExportMapDir;
    private javax.swing.JButton jButtonImportMap;
    private javax.swing.JButton jButtonImportRawMap;
    private javax.swing.JButton jButtonRemoveBlock;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBoxShowBGLayer;
    private javax.swing.JCheckBox jCheckBoxShowUpperLayer;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelMapNameExport;
    private javax.swing.JLabel jLabelMapNameImport;
    private javax.swing.JLabel jLabelShowParallax;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
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
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
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
    private javax.swing.JRadioButton jRadioButtonApplyTile;
    private javax.swing.JRadioButton jRadioButtonBarrel;
    private javax.swing.JRadioButton jRadioButtonCaravan;
    private javax.swing.JRadioButton jRadioButtonChest;
    private javax.swing.JRadioButton jRadioButtonFlipTile;
    private javax.swing.JRadioButton jRadioButtonHideRoof;
    private javax.swing.JRadioButton jRadioButtonLayerUp;
    private javax.swing.JRadioButton jRadioButtonLayoutDown;
    private javax.swing.JRadioButton jRadioButtonObstructed;
    private javax.swing.JRadioButton jRadioButtonPaintBlocks;
    private javax.swing.JRadioButton jRadioButtonRaft;
    private javax.swing.JRadioButton jRadioButtonSearch;
    private javax.swing.JRadioButton jRadioButtonSetPriority;
    private javax.swing.JRadioButton jRadioButtonShelf;
    private javax.swing.JRadioButton jRadioButtonShowRoof;
    private javax.swing.JRadioButton jRadioButtonStars;
    private javax.swing.JRadioButton jRadioButtonStep;
    private javax.swing.JRadioButton jRadioButtonTable;
    private javax.swing.JRadioButton jRadioButtonTrigger;
    private javax.swing.JRadioButton jRadioButtonVase;
    private javax.swing.JRadioButton jRadioButtonWarp;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSpinner jSpinnerExportMapIndex;
    private javax.swing.JSpinner jSpinnerImportMapIndex;
    private javax.swing.JSpinner jSpinnerTilesetId;
    private javax.swing.JSpinner jSpinnerTilesetLength;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPaneBlockset;
    private javax.swing.JTabbedPane jTabbedPaneCopyFlags;
    private javax.swing.JTabbedPane jTabbedPaneEditor;
    private javax.swing.JTabbedPane jTabbedPaneImportExport;
    private javax.swing.JTabbedPane jTabbedPaneItems;
    private javax.swing.JTabbedPane jTabbedPaneMapEditModes;
    private com.sfc.sf2.map.animation.models.MapAnimationFrameTableModel mapAnimationFrameTableModel;
    private com.sfc.sf2.map.models.MapAreaTableModel mapAreaTableModel;
    private com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel mapBlocksetLayoutPanel;
    private com.sfc.sf2.map.models.MapItemTableModel mapChestItemTableModel;
    private com.sfc.sf2.map.models.MapFlagCopyEventTableModel mapFlagCopyTableModel;
    private com.sfc.sf2.map.gui.MapLayoutPanel mapLayoutPanel;
    private com.sfc.sf2.map.models.MapItemTableModel mapOtherItemTableModel;
    private com.sfc.sf2.map.models.MapRoofCopyEventTableModel mapRoofCopyTableModel;
    private com.sfc.sf2.map.models.MapStepCopyEventTableModel mapStepCopyTableModel;
    private com.sfc.sf2.map.gui.MapViewPanel mapViewPanel;
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
    private com.sfc.sf2.map.animation.gui.TilesetAnimViewPanel tilesetAnimViewPanel1;
    private com.sfc.sf2.map.animation.gui.MapAnimationTilesetLayoutPanel tilesetLayoutPanelAnim;
    private com.sfc.sf2.map.animation.gui.MapModifiedTilesetLayoutPanel tilesetLayoutPanelModified;
    private com.sfc.sf2.map.block.gui.MapTilesetViewPanel tilesetViewPanel1;
    private com.sfc.sf2.map.block.gui.MapTilesetsLayoutPanel tilesetsLayoutPanel;
    // End of variables declaration//GEN-END:variables
}
