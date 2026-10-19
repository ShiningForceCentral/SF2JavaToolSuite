/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.mapcreator.gui;

import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.ComboAction;
import com.sfc.sf2.core.actions.NonCombinableAction;
import com.sfc.sf2.core.actions.ToggleAction;
import com.sfc.sf2.core.gui.AbstractMainEditor;
import static com.sfc.sf2.core.gui.AbstractMainEditor.programStart;
import static com.sfc.sf2.core.gui.AbstractMainEditor.setupMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.controls.FileButton;
import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.core.settings.ViewSettings;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import com.sfc.sf2.mapcreator.ProtoMap;
import com.sfc.sf2.mapcreator.MapCreatorManager;
import com.sfc.sf2.mapcreator.OptimisedMap;
import com.sfc.sf2.mapcreator.MapTilesetsData;
import com.sfc.sf2.mapcreator.OptimisedMap.TilesetFillMode;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 *
 * @author TiMMy
 */
public class MapCreatorMainEditor extends AbstractMainEditor {
    private final ViewSettings blockViewSettings = new ViewSettings(8, RenderScaleHelpers.RENDER_SCALE_1X);
    private final ViewSettings tilesetViewSettings = new ViewSettings(16, RenderScaleHelpers.RENDER_SCALE_1X);
    private final ViewSettings layoutViewSettings = new ViewSettings(64, RenderScaleHelpers.RENDER_SCALE_1X);
    
    private MapCreatorManager mapCreatorManager = new MapCreatorManager();
    private OptimisedMap actionOptimisedMap;
    private int actionFillMode;
        
    @Override
    protected Console getConsole() { return console1; }
    @Override
    protected void initEditorComponents() { initComponents(); }
    
    @Override
    protected void registerSettings() {
        SettingsManager.registerSettingsStore("mapBlockset", blockViewSettings);
        SettingsManager.registerSettingsStore("mapTileset", tilesetViewSettings);
        SettingsManager.registerSettingsStore("mapLayout", layoutViewSettings);
    }
    
    @Override
    protected void setupEditor() {
        accordionPanelMapsPaths.setExpanded(false);
        accordionPanelModifyTilesets.setExpanded(false);
        accordionPanelExportMaps.setExpanded(false);
        
        viewPanelTilesetOrphan.setLayoutPanel(tilesetPanelOrphan, tilesetViewSettings);
        viewPanelTilesetAdded.setLayoutPanel(tilesetPanelAdded, tilesetViewSettings);
        viewPanelTilesetsNew.setLayoutPanel(mapTilesetLayoutPanelNew, tilesetViewSettings);
        viewPanelTilesetsImported.setLayoutPanel(mapTilesetLayoutPanelImported, tilesetViewSettings);
        viewPanelTilesetImport.setLayoutPanel(tilesetPanelImport, tilesetViewSettings);
        blocksetViewPanelImported.setLayoutPanel(mapBlocksetPanelImported, blockViewSettings);
        mapCreatorViewPanel.setLayoutPanel(mapCreatorLayoutPanel, layoutViewSettings);
    }
    
    @Override
    protected void onDataLoaded() {
        ActionManager.setAndExecuteAction(new NonCombinableAction<ProtoMap>(this, "Map Image Imported", this::actionMapImageLoaded, mapCreatorManager.getProtoMap(), mapCreatorLayoutPanel.getProtoMap()));
    }
    
    private void actionMapImageLoaded(ProtoMap map) {
        mapCreatorLayoutPanel.setProtoMap(map);
        if (map == null) {
            mapBlocksetPanelImported.setBlockset(null);
            mapBlocksetPanelImported.setTilesets(null);
            tilesetPanelImport.setTileset(null);
        } else {
            if (jCheckBoxShowOptimised.isSelected()) {
                mapBlocksetPanelImported.setBlockset(map.getOptimisedBlockset());
                mapBlocksetPanelImported.setTilesets(new Tileset[] { map.getOptimisedTileset() });
                tilesetPanelImport.setTileset(map.getOptimisedTileset());
            } else {
                mapBlocksetPanelImported.setBlockset(map.getRawBlockset());
                mapBlocksetPanelImported.setTilesets(new Tileset[] { map.getRawTileset()});
                tilesetPanelImport.setTileset(map.getRawTileset());
            }
            jTabbedPaneMain.setSelectedIndex(0);
        }
        actionMapOptimised(null);
    }
    
    protected void onTilesetsLoaded() {
        ActionManager.setAndExecuteAction(new NonCombinableAction<Tileset[]>(this, "Map Tilesets Imported", this::actionTilesetsLoaded, mapCreatorManager.getImportedTilesets(), mapTilesetLayoutPanelImported.getTilesets()));
    }
    
    private void actionTilesetsLoaded(Tileset[] data) {
        mapTilesetLayoutPanelImported.setTilesets(data);
    }
    
    protected void onMapOptimised(OptimisedMap map) {
        ActionManager.setAndExecuteAction(new NonCombinableAction<OptimisedMap>(this, "Map Optimised", this::actionMapOptimised, map, actionOptimisedMap));
    }
    
    private void actionMapOptimised(OptimisedMap map) {
        actionOptimisedMap = map;
        if (map == null) {
            mapCreatorLayoutPanel.setProtoMap(mapCreatorLayoutPanel.getProtoMap());
            mapTilesetLayoutPanelNew.setTilesets(null);
            tilesetPanelAdded.setTileset(null);
            tilesetPanelOrphan.setTileset(null);
            mapCreatorLayoutPanel.setAddedTiles(null);
            mapCreatorLayoutPanel.setOrphanTiles(null);
        } else {
            mapCreatorLayoutPanel.setMapLayout(map.getNewLayout());
            mapTilesetLayoutPanelNew.setTilesets(map.getNewTilesets());
            tilesetPanelAdded.setTileset(map.getAddedTiles());
            tilesetPanelOrphan.setTileset(map.getOrphanTiles());
            jTabbedPaneMain.setSelectedIndex(2);
            mapCreatorLayoutPanel.setAddedTiles(map.getAddedTiles());
            mapCreatorLayoutPanel.setOrphanTiles(map.getOrphanTiles());
        }
    }
    
    //To create a new Main Editor, copy this method and change the new mainEditor class
    public static void main(String args[]) {
        programStart(args);
        setupMainEditor(new MapCreatorMainEditor());   // <------ Change this class to new Main Editor class
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser2 = new javax.swing.JFileChooser();
        buttonGroup1 = new com.sfc.sf2.core.gui.controls.NameableButtonGroup();
        buttonGroup2 = new com.sfc.sf2.core.gui.controls.NameableButtonGroup();
        jPanel13 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jButtonImportMap = new javax.swing.JButton();
        fileButtonImportImage = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportLayout = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportHp = new com.sfc.sf2.core.gui.controls.FileButton();
        jCheckBoxChestGraphic = new javax.swing.JCheckBox();
        infoButton3 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel56 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxShowOptimised = new javax.swing.JCheckBox();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mapBlocksetPanelImported = new com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel();
        blocksetViewPanelImported = new com.sfc.sf2.map.block.gui.BlocksetViewPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tilesetPanelImport = new com.sfc.sf2.graphics.gui.TilesetLayoutPanel();
        viewPanelTilesetImport = new com.sfc.sf2.core.gui.controls.ViewPanelStacked();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel51 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jButtonExistingMapImport = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        accordionPanelMapsPaths = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonExistingMapData = new com.sfc.sf2.core.gui.controls.FileButton();
        directoryButtonExistingMapPalette = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        directoryButtonExistingMapTilesets = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        directoryButtonExistingMapDir = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        jSpinnerExistingMapIndex = new javax.swing.JSpinner();
        jLabel51 = new javax.swing.JLabel();
        jButtonExistingMapCopy = new javax.swing.JButton();
        jPanel52 = new javax.swing.JPanel();
        jButtonCustomMapImport = new javax.swing.JButton();
        fileButtonCustomPalette = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonCustomTileset1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonCustomTileset2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonCustomTileset3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonCustomTileset4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonCustomTileset5 = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        mapTilesetLayoutPanelImported = new com.sfc.sf2.mapcreator.gui.MapTilesetLayoutPanel();
        viewPanelTilesetsImported = new com.sfc.sf2.core.gui.controls.ViewPanelStacked();
        jSplitPane6 = new javax.swing.JSplitPane();
        jPanel18 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jButtonOptimise = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jComboBoxFillMode = new javax.swing.JComboBox<>();
        infoButton4 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel3 = new javax.swing.JLabel();
        accordionPanelModifyTilesets = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        jCheckBoxModifyTileset1 = new javax.swing.JCheckBox();
        jCheckBoxModifyTileset2 = new javax.swing.JCheckBox();
        jCheckBoxModifyTileset3 = new javax.swing.JCheckBox();
        jCheckBoxModifyTileset4 = new javax.swing.JCheckBox();
        jCheckBoxModifyTileset5 = new javax.swing.JCheckBox();
        infoButton2 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        mapTilesetLayoutPanelNew = new com.sfc.sf2.mapcreator.gui.MapTilesetLayoutPanel();
        viewPanelTilesetsNew = new com.sfc.sf2.core.gui.controls.ViewPanelStacked();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tilesetPanelAdded = new com.sfc.sf2.graphics.gui.TilesetLayoutPanel();
        viewPanelTilesetAdded = new com.sfc.sf2.core.gui.controls.ViewPanelStacked();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tilesetPanelOrphan = new com.sfc.sf2.graphics.gui.TilesetLayoutPanel();
        viewPanelTilesetOrphan = new com.sfc.sf2.core.gui.controls.ViewPanelStacked();
        jPanel5 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        fileButtonExportPalette = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel2 = new javax.swing.JLabel();
        jButtonExportPalette = new javax.swing.JButton();
        jPanel48 = new javax.swing.JPanel();
        fileButtonExportTileset1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonExportTileset2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonExportTileset3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonExportTileset4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonExportTileset5 = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel8 = new javax.swing.JLabel();
        jButtonExportTilesets = new javax.swing.JButton();
        jPanel53 = new javax.swing.JPanel();
        jButtonExportMap = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        accordionPanelExportMaps = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonExportBlocks = new com.sfc.sf2.core.gui.controls.FileButton();
        directoryButtonExportMapDir = new com.sfc.sf2.core.gui.controls.DirectoryButton();
        fileButtonExportLayout = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonExportTilesets = new com.sfc.sf2.core.gui.controls.FileButton();
        jSpinnerExportMapIndex = new javax.swing.JSpinner();
        jLabel57 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        mapCreatorLayoutPanel = new com.sfc.sf2.mapcreator.gui.MapCreatorLayoutPanel();
        mapCreatorViewPanel = new com.sfc.sf2.mapcreator.gui.MapCreatorViewPanel();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        jFileChooser2.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2MapCreator");
        setPreferredSize(new java.awt.Dimension(1225, 1000));

        jSplitPane1.setDividerLocation(750);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(650);
        jSplitPane2.setOneTouchExpandable(true);

        jPanel9.setPreferredSize(new java.awt.Dimension(638, 600));

        jTabbedPaneMain.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jSplitPane4.setDividerLocation(300);
        jSplitPane4.setOneTouchExpandable(true);

        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        jLabel52.setText("<html><b>Step 1:</b> Import map image and metadata.</html>");
        jLabel52.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder("Map Image Import : "));

        jButtonImportMap.setText("Import");
        jButtonImportMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportMapActionPerformed(evt);
            }
        });

        fileButtonImportImage.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ANY_IMAGE);
        fileButtonImportImage.setFilePath("./export/layout.png");
        fileButtonImportImage.setInfoMessage("<html>An image of the map to import.<br><b>Image requirements :</b><br>- Dimensions: 64x64 map blocks = 1536x1536 pixels<br>- Image format: 16-color indexed (4bpp) - 256-color indexed tolerated if only first 16 colors are used.<br><br>Note: Color at index 0 should be transparent. All used colors from the palette should have distinct values, to avoid color index ambiguity.</html>");
        fileButtonImportImage.setLabelText("Map image :");

        fileButtonImportLayout.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
        fileButtonImportLayout.setFilePath("./export/layout_flags.txt");
        fileButtonImportLayout.setInfoMessage("<html>(Optional) Layout flags indicates the map flags on each map block - i.e. to identify search points, stairs, warp points, etc.<br><br>Generally, this data would be exported from an existing map in the MapEditor.<br>This data can be modified in the MapEditor once this map is created.</hml>");
        fileButtonImportLayout.setLabelText("Layout flags :");

        fileButtonImportHp.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.TXT);
        fileButtonImportHp.setFilePath("./export/layout_hptiles.txt");
        fileButtonImportHp.setInfoMessage("<html>(Optional) HP tiles indicates which tiles are drawn on the 'high priority' layer - i.e. drawn over the top of mapsprites.<br><br>Generally, this data would be exported from an existing map in the MapEditor.<br>This data can be modified in the MapEditor once this map is created.</hml>");
        fileButtonImportHp.setLabelText("HP tiles :");

        jCheckBoxChestGraphic.setSelected(true);
        jCheckBoxChestGraphic.setText("Add chest graphics");
        jCheckBoxChestGraphic.setName("Action Add Chest Toggle"); // NOI18N
        jCheckBoxChestGraphic.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxChestGraphicItemStateChanged(evt);
            }
        });

        infoButton3.setMessageText("<html>Ensure that chest graphics are setup correctly.<br><br>Chest graphics will be placed in blocks index 1 & 2. Tileset 5 will have chest open/closed graphics injected into it (if not already present).</html>");
        infoButton3.setText("");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileButtonImportHp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonImportLayout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(fileButtonImportImage, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonImportMap, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                                .addComponent(jCheckBoxChestGraphic)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonImportImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonImportLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonImportHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBoxChestGraphic)
                    .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonImportMap)
                .addContainerGap())
        );

        jLabel56.setText("<html>Import step generates basic 64x64 map block array and 192x192 tile array with no optimization at this point.<br><br>Use the <i>\"Show Optimised\"</i> toggle to view raw or optimised blocks/tiles.</html>");
        jLabel56.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel56.setMinimumSize(new java.awt.Dimension(70, 32));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(374, Short.MAX_VALUE))
        );

        jSplitPane4.setLeftComponent(jPanel3);

        jPanel4.setPreferredSize(new java.awt.Dimension(200, 500));

        jCheckBoxShowOptimised.setSelected(true);
        jCheckBoxShowOptimised.setText("Show Optimised");
        jCheckBoxShowOptimised.setName("Action Show Optimised Toggle"); // NOI18N
        jCheckBoxShowOptimised.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxShowOptimisedItemStateChanged(evt);
            }
        });

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Blockset"));

        javax.swing.GroupLayout mapBlocksetPanelImportedLayout = new javax.swing.GroupLayout(mapBlocksetPanelImported);
        mapBlocksetPanelImported.setLayout(mapBlocksetPanelImportedLayout);
        mapBlocksetPanelImportedLayout.setHorizontalGroup(
            mapBlocksetPanelImportedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
        );
        mapBlocksetPanelImportedLayout.setVerticalGroup(
            mapBlocksetPanelImportedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 708, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(mapBlocksetPanelImported);

        blocksetViewPanelImported.setMinimumSize(new java.awt.Dimension(200, 82));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(blocksetViewPanelImported, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(blocksetViewPanelImported, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Blocks", jPanel21);

        jScrollPane4.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Tileset"));

        javax.swing.GroupLayout tilesetPanelImportLayout = new javax.swing.GroupLayout(tilesetPanelImport);
        tilesetPanelImport.setLayout(tilesetPanelImportLayout);
        tilesetPanelImportLayout.setHorizontalGroup(
            tilesetPanelImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );
        tilesetPanelImportLayout.setVerticalGroup(
            tilesetPanelImportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 709, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(tilesetPanelImport);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(viewPanelTilesetImport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(viewPanelTilesetImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Tiles", jPanel19);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBoxShowOptimised)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxShowOptimised)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane3))
        );

        jSplitPane4.setRightComponent(jPanel4);

        jTabbedPaneMain.addTab("1. Import Map", jSplitPane4);

        jSplitPane5.setDividerLocation(300);

        jPanel50.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from map :"));

        jButtonExistingMapImport.setText("Import");
        jButtonExistingMapImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExistingMapImportActionPerformed(evt);
            }
        });

        jLabel41.setText("Import palette and tilesets from existing map");

        accordionPanelMapsPaths.setBorder(javax.swing.BorderFactory.createTitledBorder("Map Paths"));

        fileButtonExistingMapData.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonExistingMapData.setFilePath("00-tilesets.asm");
        fileButtonExistingMapData.setInfoMessage("");
        fileButtonExistingMapData.setLabelText("Map tileset data :");

        directoryButtonExistingMapPalette.setDirectoryPath("../graphics/maps/mappalettes/");
        directoryButtonExistingMapPalette.setInfoMessage("The path where all map palettes are stored.");
        directoryButtonExistingMapPalette.setLabelText("Map palette path :");

        directoryButtonExistingMapTilesets.setDirectoryPath("../graphics/maps/maptilesets/");
        directoryButtonExistingMapTilesets.setInfoMessage("The path where all map tilesets are stored.");
        directoryButtonExistingMapTilesets.setLabelText("Map tilesets path :");

        directoryButtonExistingMapDir.setDirectoryPath("./entries/");
        directoryButtonExistingMapDir.setInfoMessage("");
        directoryButtonExistingMapDir.setLabelText("Maps dir :");

        javax.swing.GroupLayout accordionPanelMapsPathsLayout = new javax.swing.GroupLayout(accordionPanelMapsPaths);
        accordionPanelMapsPaths.setLayout(accordionPanelMapsPathsLayout);
        accordionPanelMapsPathsLayout.setHorizontalGroup(
            accordionPanelMapsPathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelMapsPathsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanelMapsPathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonExistingMapData, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(directoryButtonExistingMapPalette, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(directoryButtonExistingMapTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(directoryButtonExistingMapDir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanelMapsPathsLayout.setVerticalGroup(
            accordionPanelMapsPathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelMapsPathsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(directoryButtonExistingMapPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(directoryButtonExistingMapTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExistingMapData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(directoryButtonExistingMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSpinnerExistingMapIndex.setModel(new javax.swing.SpinnerNumberModel(3, 0, null, 1));
        jSpinnerExistingMapIndex.setName("Import Map Index Spinner"); // NOI18N

        jLabel51.setText("Map index :");

        jButtonExistingMapCopy.setText("Copy below");
        jButtonExistingMapCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExistingMapCopyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(accordionPanelMapsPaths, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerExistingMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                        .addComponent(jButtonExistingMapCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonExistingMapImport)))
                .addContainerGap())
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accordionPanelMapsPaths, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerExistingMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButtonExistingMapImport)
                    .addComponent(jButtonExistingMapCopy))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel52.setBorder(javax.swing.BorderFactory.createTitledBorder("Import Custom :"));
        jPanel52.setPreferredSize(new java.awt.Dimension(32, 135));

        jButtonCustomMapImport.setText("Import");
        jButtonCustomMapImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustomMapImportActionPerformed(evt);
            }
        });

        fileButtonCustomPalette.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonCustomPalette.setFilePath("../../../graphics/maps/mappalettes/mappalette00.bin");
        fileButtonCustomPalette.setInfoMessage("");
        fileButtonCustomPalette.setLabelText("Palette :");

        fileButtonCustomTileset1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonCustomTileset1.setFilePath("../../../graphics/maps/maptilesets/maptileset000.bin");
        fileButtonCustomTileset1.setInfoMessage("<html>Tileset 0, index 0 should always be left empty</html>");
        fileButtonCustomTileset1.setLabelText("Tileset 1 :");

        fileButtonCustomTileset2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonCustomTileset2.setFilePath("../../../graphics/maps/maptilesets/maptileset037.bin");
        fileButtonCustomTileset2.setInfoMessage("");
        fileButtonCustomTileset2.setLabelText("Tileset 2 :");

        fileButtonCustomTileset3.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonCustomTileset3.setFilePath("../../../graphics/maps/maptilesets/maptileset043.bin");
        fileButtonCustomTileset3.setInfoMessage("");
        fileButtonCustomTileset3.setLabelText("Tileset 3 :");

        fileButtonCustomTileset4.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonCustomTileset4.setFilePath("../../../graphics/maps/maptilesets/maptileset053.bin");
        fileButtonCustomTileset4.setInfoMessage("");
        fileButtonCustomTileset4.setLabelText("Tileset 4 :");

        fileButtonCustomTileset5.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonCustomTileset5.setFilePath("../../../graphics/maps/maptilesets/maptileset066.bin");
        fileButtonCustomTileset5.setInfoMessage("<html>Open/closed chest tiles are expected at fixed positions in tileset 5 (if 'chest graphics' is ticked). See e.g. <i>maptileset066.bin</i>.</html>");
        fileButtonCustomTileset5.setLabelText("Tileset 5 :");

        jLabel4.setText("<html>Load individual palette and tilesets.<br><b>NOTE:</b> Set a tileset slot to empty to allow space for new tiles from imported map.</html>");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonCustomPalette, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel52Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCustomMapImport))
                    .addComponent(fileButtonCustomTileset1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonCustomTileset2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonCustomTileset3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonCustomTileset4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonCustomTileset5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonCustomPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonCustomTileset1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonCustomTileset2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonCustomTileset3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonCustomTileset4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonCustomTileset5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCustomMapImport)
                .addContainerGap())
        );

        jLabel53.setText("<html><b>Step 2:</b> Load tilesets from mapdata or individually.</html>");
        jLabel53.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel52, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel52, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jSplitPane5.setLeftComponent(jPanel51);

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane5.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Tilesets"));

        javax.swing.GroupLayout mapTilesetLayoutPanelImportedLayout = new javax.swing.GroupLayout(mapTilesetLayoutPanelImported);
        mapTilesetLayoutPanelImported.setLayout(mapTilesetLayoutPanelImportedLayout);
        mapTilesetLayoutPanelImportedLayout.setHorizontalGroup(
            mapTilesetLayoutPanelImportedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mapTilesetLayoutPanelImportedLayout.setVerticalGroup(
            mapTilesetLayoutPanelImportedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 653, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(mapTilesetLayoutPanelImported);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
            .addComponent(viewPanelTilesetsImported, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(viewPanelTilesetsImported, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSplitPane5.setRightComponent(jPanel25);

        jTabbedPaneMain.addTab("2. Import Tilesets", jSplitPane5);

        jSplitPane6.setDividerLocation(300);

        jLabel54.setText("<html><b>Step 3:</b> Fill existing tilesets or create new tilesets.</html>");
        jLabel54.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jButtonOptimise.setText("Optimise and Generate Data");
        jButtonOptimise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptimiseActionPerformed(evt);
            }
        });

        jLabel38.setText("<html>- <b>New Tileset:</b> The final tilesets, created from existing tilesets and new tiles (based on 'Fill mode' settings).<br><br>\n- <b>Added Tiles:</b> Tiles that weren't in the imported tilesets that have been added into new or existing tilesets.<br>Use the checkbox in the map view controls to see where the new tiles are on the map.<br><br>\n- <b>Orphan Tiles:</b> Any tiles that do not fit in the original tilesets.<br>Use the checkbox in the map view controls to see where the orphaned tiles are on the map.<br><br>\n</html>");
        jLabel38.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jComboBoxFillMode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "New Only", "New First", "Existing First" }));
        jComboBoxFillMode.setName("Action Fill Mode Combo"); // NOI18N
        jComboBoxFillMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFillModeActionPerformed(evt);
            }
        });

        infoButton4.setMessageText("<html>- <b>Fill new only:</b> Will only create new tilesets. Will not modifiy any existing tilesets. Left over tilesets will be 'orphaned'.<br><br>- <b>New first:</b> Will create and fill new tilesets first, then will fill empty spaces in existing tilesets.<br><br>- <b>Existing first:</b> Will fill spaces in existing tilesets first, then will create and fill new tilesets.</html>");
        infoButton4.setText("");

        jLabel3.setText("Fill mode :");

        accordionPanelModifyTilesets.setBorder(javax.swing.BorderFactory.createTitledBorder("Individual tileset controls"));

        jCheckBoxModifyTileset1.setSelected(true);
        jCheckBoxModifyTileset1.setText("Allow modify tileset 1");
        jCheckBoxModifyTileset1.setName("Action Modify Tileset1 Toggle"); // NOI18N
        jCheckBoxModifyTileset1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxModifyTileset1ItemStateChanged(evt);
            }
        });

        jCheckBoxModifyTileset2.setSelected(true);
        jCheckBoxModifyTileset2.setText("Allow modify tileset 2");
        jCheckBoxModifyTileset2.setName("Action Modify Tileset2 Toggle"); // NOI18N
        jCheckBoxModifyTileset2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxModifyTileset2ItemStateChanged(evt);
            }
        });

        jCheckBoxModifyTileset3.setSelected(true);
        jCheckBoxModifyTileset3.setText("Allow modify tileset 3");
        jCheckBoxModifyTileset3.setName("Action Modify Tileset3 Toggle"); // NOI18N
        jCheckBoxModifyTileset3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxModifyTileset3ItemStateChanged(evt);
            }
        });

        jCheckBoxModifyTileset4.setSelected(true);
        jCheckBoxModifyTileset4.setText("Allow modify tileset 4");
        jCheckBoxModifyTileset4.setName("Action Modify Tileset4 Toggle"); // NOI18N
        jCheckBoxModifyTileset4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxModifyTileset4ItemStateChanged(evt);
            }
        });

        jCheckBoxModifyTileset5.setSelected(true);
        jCheckBoxModifyTileset5.setText("Allow modify tileset 5");
        jCheckBoxModifyTileset5.setName("Action Modify Tileset5 Toggle"); // NOI18N
        jCheckBoxModifyTileset5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxModifyTileset5ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout accordionPanelModifyTilesetsLayout = new javax.swing.GroupLayout(accordionPanelModifyTilesets);
        accordionPanelModifyTilesets.setLayout(accordionPanelModifyTilesetsLayout);
        accordionPanelModifyTilesetsLayout.setHorizontalGroup(
            accordionPanelModifyTilesetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelModifyTilesetsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanelModifyTilesetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxModifyTileset3)
                    .addComponent(jCheckBoxModifyTileset4)
                    .addComponent(jCheckBoxModifyTileset5)
                    .addComponent(jCheckBoxModifyTileset2)
                    .addComponent(jCheckBoxModifyTileset1))
                .addContainerGap())
        );
        accordionPanelModifyTilesetsLayout.setVerticalGroup(
            accordionPanelModifyTilesetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelModifyTilesetsLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jCheckBoxModifyTileset1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxModifyTileset2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxModifyTileset3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxModifyTileset4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxModifyTileset5)
                .addContainerGap())
        );

        infoButton2.setMessageText("<html>Allows finer control over which tilesets can be modified. Does not override the 'Fill mode' setting.</html>");
        infoButton2.setText("");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonOptimise)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel18Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxFillMode, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(accordionPanelModifyTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 88, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxFillMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(accordionPanelModifyTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOptimise, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSplitPane6.setLeftComponent(jPanel18);

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane7.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Tilesets"));

        javax.swing.GroupLayout mapTilesetLayoutPanelNewLayout = new javax.swing.GroupLayout(mapTilesetLayoutPanelNew);
        mapTilesetLayoutPanelNew.setLayout(mapTilesetLayoutPanelNewLayout);
        mapTilesetLayoutPanelNewLayout.setHorizontalGroup(
            mapTilesetLayoutPanelNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mapTilesetLayoutPanelNewLayout.setVerticalGroup(
            mapTilesetLayoutPanelNewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(mapTilesetLayoutPanelNew);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(viewPanelTilesetsNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane7)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(viewPanelTilesetsNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("New Tilesets", jPanel2);

        jScrollPane8.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Tileset"));

        javax.swing.GroupLayout tilesetPanelAddedLayout = new javax.swing.GroupLayout(tilesetPanelAdded);
        tilesetPanelAdded.setLayout(tilesetPanelAddedLayout);
        tilesetPanelAddedLayout.setHorizontalGroup(
            tilesetPanelAddedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tilesetPanelAddedLayout.setVerticalGroup(
            tilesetPanelAddedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane8.setViewportView(tilesetPanelAdded);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(viewPanelTilesetAdded, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(viewPanelTilesetAdded, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Added Tiles", jPanel20);

        jScrollPane9.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Tileset"));

        javax.swing.GroupLayout tilesetPanelOrphanLayout = new javax.swing.GroupLayout(tilesetPanelOrphan);
        tilesetPanelOrphan.setLayout(tilesetPanelOrphanLayout);
        tilesetPanelOrphanLayout.setHorizontalGroup(
            tilesetPanelOrphanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tilesetPanelOrphanLayout.setVerticalGroup(
            tilesetPanelOrphanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane9.setViewportView(tilesetPanelOrphan);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(viewPanelTilesetOrphan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(viewPanelTilesetOrphan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Orphan Tiles", jPanel23);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane6.setRightComponent(jPanel11);

        jTabbedPaneMain.addTab("3. Optimise", jSplitPane6);

        jPanel5.setPreferredSize(new java.awt.Dimension(32, 135));

        jLabel55.setText("<html><b>Step 5:</b> Export map data.</html>");
        jLabel55.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jPanel47.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette :"));

        fileButtonExportPalette.setFilePath("../graphics/maps/mappalettes/mappalette16.bin");
        fileButtonExportPalette.setInfoMessage("");
        fileButtonExportPalette.setLabelText("Palette :");
        fileButtonExportPalette.setName("Palette Export Button"); // NOI18N

        jLabel2.setText("<html>Export current palette.</html>");

        jButtonExportPalette.setText("Export");
        jButtonExportPalette.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportPaletteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonExportPalette, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonExportPalette)))
                .addContainerGap())
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonExportPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonExportPalette))
                .addContainerGap())
        );

        jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder("Tilesets :"));

        fileButtonExportTileset1.setFilePath("../graphics/maps/maptilesets/maptileset115.bin");
        fileButtonExportTileset1.setInfoMessage("");
        fileButtonExportTileset1.setLabelText("Tileset1 :");
        fileButtonExportTileset1.setName("Tileset1 Export Button"); // NOI18N

        fileButtonExportTileset2.setFilePath("../graphics/maps/maptilesets/maptileset116.bin");
        fileButtonExportTileset2.setInfoMessage("");
        fileButtonExportTileset2.setLabelText("Tileset2 :");
        fileButtonExportTileset2.setName("Tileset2 Export Button"); // NOI18N

        fileButtonExportTileset3.setFilePath("../graphics/maps/maptilesets/maptileset117.bin");
        fileButtonExportTileset3.setInfoMessage("");
        fileButtonExportTileset3.setLabelText("Tileset3 :");
        fileButtonExportTileset3.setName("Tileset3 Export Button"); // NOI18N

        fileButtonExportTileset4.setFilePath("../graphics/maps/maptilesets/maptileset118.bin");
        fileButtonExportTileset4.setInfoMessage("");
        fileButtonExportTileset4.setLabelText("Tileset4 :");
        fileButtonExportTileset4.setName("Tileset4 Export Button"); // NOI18N

        fileButtonExportTileset5.setFilePath("../graphics/maps/maptilesets/maptileset119.bin");
        fileButtonExportTileset5.setInfoMessage("");
        fileButtonExportTileset5.setLabelText("Tileset5 :");
        fileButtonExportTileset5.setName("Tileset5 Export Button"); // NOI18N

        jLabel8.setText("<html>Export new tilesets.<br>\nLeave a slot blank to have it ignored.</html>");
        jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jButtonExportTilesets.setText("Export");
        jButtonExportTilesets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportTilesetsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonExportTileset1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonExportTilesets))
                    .addComponent(fileButtonExportTileset2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileButtonExportTileset3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileButtonExportTileset4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileButtonExportTileset5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonExportTileset1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportTileset2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportTileset3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportTileset4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportTileset5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonExportTilesets)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel53.setBorder(javax.swing.BorderFactory.createTitledBorder("Map data :"));

        jButtonExportMap.setText("Export");
        jButtonExportMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportMapActionPerformed(evt);
            }
        });

        jLabel42.setText("Import palette and tilesets from existing map");

        accordionPanelExportMaps.setBorder(javax.swing.BorderFactory.createTitledBorder("Map Paths"));

        fileButtonExportBlocks.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonExportBlocks.setFilePath("0-blocks.bin");
        fileButtonExportBlocks.setInfoMessage("");
        fileButtonExportBlocks.setLabelText("Blocks :");
        fileButtonExportBlocks.setName("Export Blocks Button"); // NOI18N

        directoryButtonExportMapDir.setDirectoryPath("./entries/");
        directoryButtonExportMapDir.setInfoMessage("");
        directoryButtonExportMapDir.setLabelText("Maps dir :");
        directoryButtonExportMapDir.setName("Export MapDir Button"); // NOI18N

        fileButtonExportLayout.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonExportLayout.setFilePath("1-layout.bin");
        fileButtonExportLayout.setInfoMessage("");
        fileButtonExportLayout.setLabelText("Layout :");
        fileButtonExportLayout.setName("Export Layout Button"); // NOI18N

        fileButtonExportTilesets.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonExportTilesets.setFilePath("00-tilesets.asm");
        fileButtonExportTilesets.setInfoMessage("");
        fileButtonExportTilesets.setLabelText("Tilesets :");
        fileButtonExportTilesets.setName("Export Tilesets Button"); // NOI18N

        javax.swing.GroupLayout accordionPanelExportMapsLayout = new javax.swing.GroupLayout(accordionPanelExportMaps);
        accordionPanelExportMaps.setLayout(accordionPanelExportMapsLayout);
        accordionPanelExportMapsLayout.setHorizontalGroup(
            accordionPanelExportMapsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelExportMapsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanelExportMapsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonExportBlocks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(directoryButtonExportMapDir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addComponent(fileButtonExportLayout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileButtonExportTilesets, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanelExportMapsLayout.setVerticalGroup(
            accordionPanelExportMapsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelExportMapsLayout.createSequentialGroup()
                .addComponent(directoryButtonExportMapDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportTilesets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSpinnerExportMapIndex.setModel(new javax.swing.SpinnerNumberModel(3, 0, null, 1));
        jSpinnerExportMapIndex.setName("Export Map Index Spinner"); // NOI18N

        jLabel57.setText("Map index :");

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(accordionPanelExportMaps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel53Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel57)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerExportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonExportMap)))
                .addContainerGap())
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accordionPanelExportMaps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonExportMap)
                    .addGroup(jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerExportMapIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel57)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addContainerGap())
            .addComponent(jPanel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPaneMain.addTab("4. Export", jPanel5);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(jPanel8);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map"));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout mapCreatorLayoutPanelLayout = new javax.swing.GroupLayout(mapCreatorLayoutPanel);
        mapCreatorLayoutPanel.setLayout(mapCreatorLayoutPanelLayout);
        mapCreatorLayoutPanelLayout.setHorizontalGroup(
            mapCreatorLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );
        mapCreatorLayoutPanelLayout.setVerticalGroup(
            mapCreatorLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(mapCreatorLayoutPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(mapCreatorViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(mapCreatorViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jSplitPane2)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSplitPane1.setTopComponent(jPanel15);
        jSplitPane1.setRightComponent(console1);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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

        setSize(new java.awt.Dimension(1344, 993));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonImportMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportMapActionPerformed
        Path imagePath = PathHelpers.getBasePath().resolve(fileButtonImportImage.getFilePath());
        Path flagsPath = PathHelpers.getBasePath().resolve(fileButtonImportLayout.getFilePath());
        Path hpTilesPath = PathHelpers.getBasePath().resolve(fileButtonImportHp.getFilePath());
        try {
            mapCreatorManager.importImage(imagePath, flagsPath, hpTilesPath, jCheckBoxChestGraphic.isSelected());
        } catch (Exception ex) {
            mapCreatorManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map image could not be imported from : " + imagePath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButtonImportMapActionPerformed

    private void jButtonOptimiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptimiseActionPerformed
        ProtoMap protoMap = mapCreatorLayoutPanel.getProtoMap();
        Tileset[] tilesets = mapTilesetLayoutPanelImported.getTilesets();
        if (protoMap == null || protoMap.getRawLayout() == null || tilesets == null) {
            return;
        }
        try {
            TilesetFillMode fillMode = TilesetFillMode.values()[jComboBoxFillMode.getSelectedIndex()];
            boolean[] modifyTilesets = new boolean[] { jCheckBoxModifyTileset1.isSelected(), jCheckBoxModifyTileset2.isSelected(), jCheckBoxModifyTileset3.isSelected(), jCheckBoxModifyTileset4.isSelected(), jCheckBoxModifyTileset5.isSelected() };
            OptimisedMap optimisedMap = new OptimisedMap(protoMap, tilesets, jCheckBoxChestGraphic.isSelected(), fillMode, modifyTilesets);
            onMapOptimised(optimisedMap);
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Tileset optimisation could not be completed");
        }
    }//GEN-LAST:event_jButtonOptimiseActionPerformed

    private void jButtonExportPaletteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportPaletteActionPerformed
        Path palettePath = PathHelpers.getBasePath().resolve(fileButtonExportPalette.getFilePath());
        try {
            mapCreatorManager.exportPalette(palettePath, mapCreatorLayoutPanel.getProtoMap().getRawLayout().getPalette());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map palette could not be exported to : " + palettePath);
        }
    }//GEN-LAST:event_jButtonExportPaletteActionPerformed

    private void jButtonExportTilesetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportTilesetsActionPerformed
        Tileset[] tilesets = mapTilesetLayoutPanelNew.getTilesets();
        FileButton[] fileButtons = new FileButton[] { fileButtonExportTileset1, fileButtonExportTileset2, fileButtonExportTileset3, fileButtonExportTileset4, fileButtonExportTileset5 };
        try {
            Path[] tilesetPaths = new Path[fileButtons.length];
            for (int i = 0; i < tilesetPaths.length; i++) {
                String path = fileButtons[i].getFilePath();
                tilesetPaths[i] = path.isBlank() ? null : PathHelpers.getBasePath().resolve(path);
            }
            mapCreatorManager.exportTilesets(tilesetPaths, tilesets);
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map image could not be copied");
        }
    }//GEN-LAST:event_jButtonExportTilesetsActionPerformed

    private void jButtonExistingMapImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExistingMapImportActionPerformed
        Path dataPath = PathHelpers.getBasePath().resolve(directoryButtonExistingMapDir.getDirectoryPath());
        dataPath = dataPath.resolve(String.format("map%02d", (int)jSpinnerExistingMapIndex.getValue()));
        dataPath = dataPath.resolve(fileButtonExistingMapData.getFilePath());
        Path palettesPath = PathHelpers.getBasePath().resolve(directoryButtonExistingMapPalette.getDirectoryPath());
        Path tilesetsPath = PathHelpers.getBasePath().resolve(directoryButtonExistingMapTilesets.getDirectoryPath());
        try {
            MapTilesetsData tilesetsData = mapCreatorManager.importMapData(dataPath, palettesPath, tilesetsPath);
            mapCreatorManager.importMapPaletteAndTilesets(tilesetsData.palettePath(), tilesetsData.tilesetPaths());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map image could not be imported from : " + dataPath);
        }
        onTilesetsLoaded();
    }//GEN-LAST:event_jButtonExistingMapImportActionPerformed

    private void jButtonCustomMapImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustomMapImportActionPerformed
        Path palettePath = PathHelpers.getBasePath().resolve(fileButtonCustomPalette.getFilePath());
        FileButton[] fileButtons = new FileButton[] { fileButtonCustomTileset1, fileButtonCustomTileset2, fileButtonCustomTileset3, fileButtonCustomTileset4, fileButtonCustomTileset5 };
        try {
            Path[] tilesetPaths = new Path[fileButtons.length];
            for (int i = 0; i < tilesetPaths.length; i++) {
                String path = fileButtons[i].getFilePath();
                tilesetPaths[i] = path.isBlank() ? null : PathHelpers.getBasePath().resolve(path);
            }
            mapCreatorManager.importMapPaletteAndTilesets(palettePath, tilesetPaths);
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map data could not be imported from custom paths");
        }
        onTilesetsLoaded();
    }//GEN-LAST:event_jButtonCustomMapImportActionPerformed

    private void jButtonExistingMapCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExistingMapCopyActionPerformed
        Path dataPath = PathHelpers.getBasePath().resolve(directoryButtonExistingMapDir.getDirectoryPath());
        dataPath = dataPath.resolve(String.format("map%02d", (int)jSpinnerExistingMapIndex.getValue()));
        dataPath = dataPath.resolve(fileButtonExistingMapData.getFilePath());
        Path palettesPath = PathHelpers.getBasePath().resolve(directoryButtonExistingMapPalette.getDirectoryPath());
        Path tilesetsPath = PathHelpers.getBasePath().resolve(directoryButtonExistingMapTilesets.getDirectoryPath());
        try {
            MapTilesetsData tilesetsData = mapCreatorManager.importMapData(dataPath, palettesPath, tilesetsPath);
            fileButtonCustomPalette.setFilePath(PathHelpers.getBasePath().relativize(tilesetsData.palettePath()).toString());
            Path[] paths = tilesetsData.tilesetPaths();
            if (paths[0] == null) {
                fileButtonCustomTileset1.setFilePath("");
            } else {
                fileButtonCustomTileset1.setFilePath(PathHelpers.getBasePath().relativize(paths[0]).toString());
            }
            if (paths[1] == null) {
                fileButtonCustomTileset2.setFilePath("");
            } else {
                fileButtonCustomTileset2.setFilePath(PathHelpers.getBasePath().relativize(paths[1]).toString());
            }
            if (paths[2] == null) {
                fileButtonCustomTileset3.setFilePath("");
            } else {
                fileButtonCustomTileset3.setFilePath(PathHelpers.getBasePath().relativize(paths[2]).toString());
            }
            if (paths[3] == null) {
                fileButtonCustomTileset4.setFilePath("");
            } else {
                fileButtonCustomTileset4.setFilePath(PathHelpers.getBasePath().relativize(paths[3]).toString());
            }
            if (paths[4] == null) {
                fileButtonCustomTileset5.setFilePath("");
            } else {
                fileButtonCustomTileset5.setFilePath(PathHelpers.getBasePath().relativize(paths[4]).toString());
            }
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map image could not be copied");
        }
    }//GEN-LAST:event_jButtonExistingMapCopyActionPerformed

    private void jCheckBoxShowOptimisedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxShowOptimisedItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxShowOptimised, jCheckBoxShowOptimised.isSelected()));
        }
        actionMapImageLoaded(mapCreatorLayoutPanel.getProtoMap());
        jTabbedPaneMain.setSelectedIndex(0);
    }//GEN-LAST:event_jCheckBoxShowOptimisedItemStateChanged

    private void jCheckBoxChestGraphicItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxChestGraphicItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxChestGraphic, jCheckBoxChestGraphic.isSelected()));
        }
        jTabbedPaneMain.setSelectedIndex(0);
    }//GEN-LAST:event_jCheckBoxChestGraphicItemStateChanged

    private void jCheckBoxModifyTileset1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxModifyTileset1ItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxModifyTileset1, jCheckBoxModifyTileset1.isSelected()));
        }
        jTabbedPaneMain.setSelectedIndex(2);
    }//GEN-LAST:event_jCheckBoxModifyTileset1ItemStateChanged

    private void jCheckBoxModifyTileset2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxModifyTileset2ItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxModifyTileset2, jCheckBoxModifyTileset2.isSelected()));
        }
        jTabbedPaneMain.setSelectedIndex(2);
    }//GEN-LAST:event_jCheckBoxModifyTileset2ItemStateChanged

    private void jCheckBoxModifyTileset3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxModifyTileset3ItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxModifyTileset3, jCheckBoxModifyTileset3.isSelected()));
        }
        jTabbedPaneMain.setSelectedIndex(2);
    }//GEN-LAST:event_jCheckBoxModifyTileset3ItemStateChanged

    private void jCheckBoxModifyTileset4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxModifyTileset4ItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxModifyTileset4, jCheckBoxModifyTileset4.isSelected()));
        }
        jTabbedPaneMain.setSelectedIndex(2);
    }//GEN-LAST:event_jCheckBoxModifyTileset4ItemStateChanged

    private void jCheckBoxModifyTileset5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxModifyTileset5ItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxModifyTileset5, jCheckBoxModifyTileset5.isSelected()));
        }
        jTabbedPaneMain.setSelectedIndex(2);
    }//GEN-LAST:event_jCheckBoxModifyTileset5ItemStateChanged

    private void jComboBoxFillModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFillModeActionPerformed
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ComboAction(jComboBoxFillMode, jComboBoxFillMode.getSelectedIndex(), actionFillMode));
        }
        actionFillMode = jComboBoxFillMode.getSelectedIndex();
        jTabbedPaneMain.setSelectedIndex(2);
    }//GEN-LAST:event_jComboBoxFillModeActionPerformed

    private void jButtonExportMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportMapActionPerformed
        Path mapDirPath = PathHelpers.getBasePath().resolve(directoryButtonExportMapDir.getDirectoryPath());
        mapDirPath = mapDirPath.resolve(String.format("map%02d", (int)jSpinnerExportMapIndex.getValue()));
        Path tilesetsPath = mapDirPath.resolve(fileButtonExportTilesets.getFilePath());
        Path blocksPath = mapDirPath.resolve(fileButtonExportBlocks.getFilePath());
        Path layoutPath = mapDirPath.resolve(fileButtonExportLayout.getFilePath());
        try {
            PathHelpers.createPathIfRequred(mapDirPath);
            mapCreatorManager.exportDisassembly(tilesetsPath, blocksPath, layoutPath, actionOptimisedMap.getNewBlockset(), actionOptimisedMap.getNewLayout());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map image could not be imported from : " + mapDirPath);
        }
    }//GEN-LAST:event_jButtonExportMapActionPerformed
    
    @Override
    protected boolean cliImportData(String[] data, FileFormat format, boolean isDirectory) throws Exception {
        throw new Exception("Not implemented");
    }

    @Override
    protected boolean cliExportData(String[] data, FileFormat format, boolean isDirectory) throws Exception {
        throw new Exception("Not implemented");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanelExportMaps;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanelMapsPaths;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanelModifyTilesets;
    private com.sfc.sf2.map.block.gui.BlocksetViewPanel blocksetViewPanelImported;
    private com.sfc.sf2.core.gui.controls.NameableButtonGroup buttonGroup1;
    private com.sfc.sf2.core.gui.controls.NameableButtonGroup buttonGroup2;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonExistingMapDir;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonExistingMapPalette;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonExistingMapTilesets;
    private com.sfc.sf2.core.gui.controls.DirectoryButton directoryButtonExportMapDir;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonCustomPalette;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonCustomTileset1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonCustomTileset2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonCustomTileset3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonCustomTileset4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonCustomTileset5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExistingMapData;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportBlocks;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportLayout;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportPalette;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportTileset1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportTileset2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportTileset3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportTileset4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportTileset5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportTilesets;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportHp;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportImage;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportLayout;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton2;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton3;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton4;
    private javax.swing.JButton jButtonCustomMapImport;
    private javax.swing.JButton jButtonExistingMapCopy;
    private javax.swing.JButton jButtonExistingMapImport;
    private javax.swing.JButton jButtonExportMap;
    private javax.swing.JButton jButtonExportPalette;
    private javax.swing.JButton jButtonExportTilesets;
    private javax.swing.JButton jButtonImportMap;
    private javax.swing.JButton jButtonOptimise;
    private javax.swing.JCheckBox jCheckBoxChestGraphic;
    private javax.swing.JCheckBox jCheckBoxModifyTileset1;
    private javax.swing.JCheckBox jCheckBoxModifyTileset2;
    private javax.swing.JCheckBox jCheckBoxModifyTileset3;
    private javax.swing.JCheckBox jCheckBoxModifyTileset4;
    private javax.swing.JCheckBox jCheckBoxModifyTileset5;
    private javax.swing.JCheckBox jCheckBoxShowOptimised;
    private javax.swing.JComboBox<String> jComboBoxFillMode;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinnerExistingMapIndex;
    private javax.swing.JSpinner jSpinnerExportMapIndex;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private com.sfc.sf2.map.block.gui.MapBlocksetLayoutPanel mapBlocksetPanelImported;
    private com.sfc.sf2.mapcreator.gui.MapCreatorLayoutPanel mapCreatorLayoutPanel;
    private com.sfc.sf2.mapcreator.gui.MapCreatorViewPanel mapCreatorViewPanel;
    private com.sfc.sf2.mapcreator.gui.MapTilesetLayoutPanel mapTilesetLayoutPanelImported;
    private com.sfc.sf2.mapcreator.gui.MapTilesetLayoutPanel mapTilesetLayoutPanelNew;
    private com.sfc.sf2.graphics.gui.TilesetLayoutPanel tilesetPanelAdded;
    private com.sfc.sf2.graphics.gui.TilesetLayoutPanel tilesetPanelImport;
    private com.sfc.sf2.graphics.gui.TilesetLayoutPanel tilesetPanelOrphan;
    private com.sfc.sf2.core.gui.controls.ViewPanelStacked viewPanelTilesetAdded;
    private com.sfc.sf2.core.gui.controls.ViewPanelStacked viewPanelTilesetImport;
    private com.sfc.sf2.core.gui.controls.ViewPanelStacked viewPanelTilesetOrphan;
    private com.sfc.sf2.core.gui.controls.ViewPanelStacked viewPanelTilesetsImported;
    private com.sfc.sf2.core.gui.controls.ViewPanelStacked viewPanelTilesetsNew;
    // End of variables declaration//GEN-END:variables
}
