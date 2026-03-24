/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.gui;

import com.sfc.sf2.battle.AIPoint;
import com.sfc.sf2.battle.AIRegion;
import com.sfc.sf2.battle.Ally;
import com.sfc.sf2.battle.BattleManager;
import com.sfc.sf2.battle.BattleSpriteset;
import com.sfc.sf2.battle.Enemy;
import com.sfc.sf2.battle.actions.BattleActionData;
import com.sfc.sf2.battle.gui.BattleLayoutPanel.BattlePaintMode;
import com.sfc.sf2.battle.gui.BattleLayoutPanel.SpritesetPaintMode;
import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapterrain.LandEffectEnums;
import com.sfc.sf2.battle.mapterrain.LandEffectMovementType;
import com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel.TerrainDrawMode;
import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.BasicAction;
import com.sfc.sf2.core.actions.CustomAction;
import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.helpers.RenderScaleHelpers;
import com.sfc.sf2.map.block.settings.MapBlockSettings;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.settings.TerrainSettings;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author wiz
 */
public class BattleEditorMainEditor extends AbstractMainEditor {
    
    private final TerrainSettings terrainSettings = new TerrainSettings();
    private final BattleManager battleManager = new BattleManager();
        
    private String actionSharedTerrainInfo;
    private LandEffectEnums actionLandEffectEnums;
        
    private boolean getDrawTerrain() { return battleViewPanel1.getDrawTerrain(); }
    private boolean getDrawSprites() { return battleViewPanel1.getDrawSprites(); }
    private boolean getDrawAiRegions() { return battleViewPanel1.getDrawRegions(); }
    private boolean getDrawAiPoints() { return battleViewPanel1.getDrawPoints(); }
    
    public BattleEditorMainEditor() {
        super();
        SettingsManager.registerSettingsStore("terrain", terrainSettings);
        SettingsManager.registerSettingsStore("mapLayout", new MapBlockSettings(20, RenderScaleHelpers.DEFAULT_RENDER_SCALE_INDEX, new Color(200, 0, 200)));
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        battleViewPanel1.setLayoutPanel(battleLayoutPanel);
        accordionPanel1.setExpanded(false);
        accordionPanel2.setExpanded(false);
        
        terrainKeyPanel1.setActionListener(this::onTerrainSelectionChanged);
        terrainKeyPanel1.setModeChangedListener(this::onTerrainModeChanged);
        TerrainDrawMode terrainDrawMode = terrainSettings.getTerrainDrawMode();
        battleLayoutPanel.setTerrainDrawMode(terrainDrawMode);
        terrainKeyPanel1.setDrawMode(terrainDrawMode);
        
        tableAllies.addTableModelListener(this::onTableAlliesDataChanged);
        tableAllies.addListSelectionListener(this::onTableAlliesSelectionChanged);
        tableEnemies.addTableModelListener(this::onTableEnemiesDataChanged);
        tableEnemies.addListSelectionListener(this::onTableEnemiesSelectionChanged);
        tableAIRegions.addTableModelListener(this::onTableAIRegionsDataChanged);
        tableAIRegions.addListSelectionListener(this::onTableAIRegionsSelectionChanged);
        tableAIPoints.addTableModelListener(this::onTableAIPointsDataChanged);
        tableAIPoints.addListSelectionListener(this::onTableAIPointsSelectionChanged);
        TableColumnModel columns = tableAllies.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        columns = tableEnemies.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        columns.getColumn(1).setMinWidth(70);
        columns = tableAIRegions.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        columns = tableAIPoints.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(30);
        
        battleLayoutPanel.setSpritesetEditedListener(this::onLayoutSpritesetChanged);
        
        jTabbedPane2StateChanged(null);
        jTabbedPane3StateChanged(null);
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        BattleActionData newValue = new BattleActionData(battleManager.getBattle(), battleManager.getSharedTerrainInfo(), battleManager.getLandEffectEnums(), battleManager.getLandEffects(), battleManager.getEnemyData(), battleManager.getEnemyEnums());
        BattleActionData oldValue = new BattleActionData(battleLayoutPanel.getBattle(), actionSharedTerrainInfo, actionLandEffectEnums, landEffectTableModel.getTableData(LandEffectMovementType[].class), enemyPropertiesTableModel.getEnemyData(), enemyPropertiesTableModel.getEnemyEnums());
        ActionManager.setAndExecuteAction(new CustomAction<BattleActionData>(this, "Battle Imported", this::actionBattleLoaded, newValue, oldValue));
    }
    
    private void actionBattleLoaded(BattleActionData value) {
        if (value == null || value.battle() == null) {
            battleLayoutPanel.setBattle(null);
            battleLayoutPanel.setTerrain(null);
            battleLayoutPanel.setBattleCoords(null);
            battleLayoutPanel.setMapLayout(null);

            actionLandEffectEnums = value.landEffectEnums();
            landEffectTableModel.setTableData(null);
            mapCoordsPanel1.setup(null, null, null);

            allyPropertiesTableModel.setTableData(null);
            enemyPropertiesTableModel.setTableData(null);
            aIRegionPropertiesTableModel.setTableData(null);
            aIPointPropertiesTableModel.setTableData(null);

            actionSharedTerrainInfo = null;
            terrainKeyPanel1.setSharedTerrainInfo(actionSharedTerrainInfo);
        } else {
            battleLayoutPanel.setBattle(value.battle());
            battleLayoutPanel.setTerrain(value.battle().getTerrain());
            battleLayoutPanel.setBattleCoords(value.battle().getBattleCoords());
            battleLayoutPanel.setMapLayout(value.battle().getMapLayout());

            actionLandEffectEnums = value.landEffectEnums();
            landEffectTable.setLandEffectData(actionLandEffectEnums);
            landEffectTableModel.setTableData(value.landEffects());

            BattleMapCoords coords = value.battle().getBattleCoords();
            mapCoordsPanel1.setup(coords, battleLayoutPanel, this::onMapIndexChange);

            BattleSpriteset spriteset = value.battle().getSpriteset();
            if (spriteset == null) {
                allyPropertiesTableModel.setTableData(null);
                enemyPropertiesTableModel.setTableData(null);
                aIRegionPropertiesTableModel.setTableData(null);
                aIPointPropertiesTableModel.setTableData(null);
            } else {
                allyPropertiesTableModel.setTableData(spriteset.getAllies());
                enemyPropertiesTableModel.setTableData(spriteset.getEnemies());
                enemyPropertiesTableModel.setEnemyData(value.enemyData(), value.enemyEnums());
                aIRegionPropertiesTableModel.setTableData(spriteset.getAiRegions());
                aIPointPropertiesTableModel.setTableData(spriteset.getAiPoints());
            }

            actionSharedTerrainInfo = value.sharedTerrainInfo();
            terrainKeyPanel1.setSharedTerrainInfo(actionSharedTerrainInfo);
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

        aIPointPropertiesTableModel = new com.sfc.sf2.battle.models.AIPointPropertiesTableModel();
        aIRegionPropertiesTableModel = new com.sfc.sf2.battle.models.AIRegionPropertiesTableModel();
        allyPropertiesTableModel = new com.sfc.sf2.battle.models.AllyPropertiesTableModel();
        enemyPropertiesTableModel = new com.sfc.sf2.battle.models.EnemyPropertiesTableModel();
        landEffectTableModel = new com.sfc.sf2.battle.mapterrain.models.LandEffectTableModel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        accordionPanel1 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonPaletteEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTilesetEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonMapEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonTerrainEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportCoords = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonSpritesetEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonImportLandEffects = new com.sfc.sf2.core.gui.controls.FileButton();
        accordionPanel2 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonImportBasePalette = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonMapspriteEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonBattleEnums = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonEnemyMapsprites = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonSpecialSpriteEntries = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonSpecialSpritePointers = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSpinnerBattleIndex = new javax.swing.JSpinner();
        jButtonImportBattle = new javax.swing.JButton();
        infoButton7 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        infoButton9 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButtonExportTerrain = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonExportSpriteset = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonExportBattle = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        infoButton8 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButtonExportCoords = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonExportCoords = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        infoButton10 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButtonExportLandEffect = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonExportLandEffect = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel12 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        mapCoordsPanel1 = new com.sfc.sf2.battle.gui.MapCoordsPanel();
        jPanel4 = new javax.swing.JPanel();
        terrainKeyPanel1 = new com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        battleLayoutPanel = new com.sfc.sf2.battle.gui.BattleLayoutPanel();
        battleViewPanel1 = new com.sfc.sf2.battle.gui.BattleViewPanel();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        tableAllies = new com.sfc.sf2.core.gui.controls.Table();
        jPanel25 = new javax.swing.JPanel();
        tableEnemies = new com.sfc.sf2.battle.tables.EnemyPropertiesTable();
        jPanel26 = new javax.swing.JPanel();
        tableAIRegions = new com.sfc.sf2.core.gui.controls.Table();
        jPanel31 = new javax.swing.JPanel();
        tableAIPoints = new com.sfc.sf2.core.gui.controls.Table();
        jPanel7 = new javax.swing.JPanel();
        infoButton11 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel22 = new javax.swing.JLabel();
        landEffectTable = new com.sfc.sf2.battle.mapterrain.gui.LandEffectTable();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2BattleEditor");

        jSplitPane1.setDividerLocation(850);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(350);
        jSplitPane2.setOneTouchExpandable(true);

        jPanel9.setPreferredSize(new java.awt.Dimension(300, 733));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from :"));
        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map & terrain"));

        fileButtonPaletteEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonPaletteEntries.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButtonPaletteEntries.setInfoMessage("Entries file for map palettes.");
        fileButtonPaletteEntries.setLabelText("Palette entries :");
        fileButtonPaletteEntries.setName("Import Palette Entries"); // NOI18N

        fileButtonTilesetEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonTilesetEntries.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButtonTilesetEntries.setInfoMessage("Entries file for map tilesets.");
        fileButtonTilesetEntries.setLabelText("Tileset entries :");
        fileButtonTilesetEntries.setName("Import Tileset Entries"); // NOI18N

        fileButtonMapEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonMapEntries.setFilePath("..\\maps\\entries.asm");
        fileButtonMapEntries.setInfoMessage("<html>Map entries file. Lists all maps defined to be used in the game.<br>If a map is not loading then it might not have been added to the entries.</html>");
        fileButtonMapEntries.setLabelText("Map entries :");
        fileButtonMapEntries.setName("Import Map Entries"); // NOI18N

        fileButtonTerrainEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonTerrainEntries.setFilePath(".\\terrainentries.asm");
        fileButtonTerrainEntries.setInfoMessage("Entries for battle terrain data.");
        fileButtonTerrainEntries.setLabelText("Terrain entries :");
        fileButtonTerrainEntries.setName("Import Terrain Entries"); // NOI18N

        fileButtonImportCoords.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonImportCoords.setFilePath(".\\global\\battlemapcoords.asm");
        fileButtonImportCoords.setInfoMessage("Assembly file that defines the map and boundries for all battles.");
        fileButtonImportCoords.setLabelText("Battle map coords :");
        fileButtonImportCoords.setName("Import Battle Coords"); // NOI18N

        fileButtonSpritesetEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonSpritesetEntries.setFilePath(".\\spritesets\\entries.asm");
        fileButtonSpritesetEntries.setInfoMessage("<html>The entries file for battle data.<br>Battle \"Spritesets\" include ally positions, enemy info, AI Regions, and AI points.</html>");
        fileButtonSpritesetEntries.setLabelText("Spriteset entries :");
        fileButtonSpritesetEntries.setName("Import Spriteset Entries"); // NOI18N

        fileButtonImportLandEffects.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonImportLandEffects.setFilePath(".\\global\\landeffectsettingsandmovecosts.asm");
        fileButtonImportLandEffects.setInfoMessage("Assembly file that defines the land effects (block move cost and defense bonus) for each battle.");
        fileButtonImportLandEffects.setLabelText("Land effects :");
        fileButtonImportLandEffects.setName("Import Land Effects"); // NOI18N

        javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
        accordionPanel1.setLayout(accordionPanel1Layout);
        accordionPanel1Layout.setHorizontalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonPaletteEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonTilesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonMapEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonTerrainEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonImportCoords, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonSpritesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonImportLandEffects, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonTerrainEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonImportCoords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(fileButtonImportLandEffects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonSpritesetEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        accordionPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Map sprites :"));

        fileButtonImportBasePalette.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonImportBasePalette.setFilePath("..\\graphics\\tech\\basepalette.bin");
        fileButtonImportBasePalette.setInfoMessage("The base palette to use for map sprites.");
        fileButtonImportBasePalette.setLabelText("Base palette :");
        fileButtonImportBasePalette.setName("Import Base Palette"); // NOI18N

        fileButtonMapspriteEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonMapspriteEntries.setFilePath("..\\graphics\\mapsprites\\entries.asm");
        fileButtonMapspriteEntries.setInfoMessage("The entries file for map sprites.");
        fileButtonMapspriteEntries.setLabelText("Mapsprite entries :");
        fileButtonMapspriteEntries.setName("Import Mapsprite Entries"); // NOI18N

        fileButtonBattleEnums.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonBattleEnums.setFilePath("..\\..\\sf2enums.asm");
        fileButtonBattleEnums.setInfoMessage("Loads data from SF2Enums (such as enemy and item names).");
        fileButtonBattleEnums.setLabelText("Battle enums :");
        fileButtonBattleEnums.setName("Import Battle Enums"); // NOI18N

        fileButtonEnemyMapsprites.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonEnemyMapsprites.setFilePath("..\\stats\\enemies\\enemymapsprites.asm");
        fileButtonEnemyMapsprites.setInfoMessage("The file that determine which Mapsprite is used by which defined enemy.");
        fileButtonEnemyMapsprites.setLabelText("Enemy mapsprites :");
        fileButtonEnemyMapsprites.setName("Import Enemy Mapsprites"); // NOI18N

        fileButtonSpecialSpriteEntries.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonSpecialSpriteEntries.setFilePath("..\\graphics\\specialsprites\\entries.asm");
        fileButtonSpecialSpriteEntries.setInfoMessage("<html>Defines the special sprites (larger sprites). Certain enemies (e.g. bosses) use special sprites.</html>");
        fileButtonSpecialSpriteEntries.setLabelText("Special Sprites entries :");
        fileButtonSpecialSpriteEntries.setName("Import Special Sprite Entries"); // NOI18N

        fileButtonSpecialSpritePointers.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonSpecialSpritePointers.setFilePath("..\\graphics\\specialsprites\\pointers.asm");
        fileButtonSpecialSpritePointers.setInfoMessage("Pointers file used to load Special Sprites.");
        fileButtonSpecialSpritePointers.setLabelText("Special sprites pointers :");
        fileButtonSpecialSpritePointers.setName("Import Special Sprite Pointers"); // NOI18N

        javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
        accordionPanel2.setLayout(accordionPanel2Layout);
        accordionPanel2Layout.setHorizontalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonImportBasePalette, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonMapspriteEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonBattleEnums, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonEnemyMapsprites, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonSpecialSpriteEntries, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonSpecialSpritePointers, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel2Layout.setVerticalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonImportBasePalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonMapspriteEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonEnemyMapsprites, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonSpecialSpriteEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonSpecialSpritePointers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonBattleEnums, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Battle :"));

        jLabel2.setText("<html>Load battle from index.</html>");

        jLabel10.setText("Battle index :");

        jSpinnerBattleIndex.setModel(new javax.swing.SpinnerNumberModel(1, 0, 255, 1));
        jSpinnerBattleIndex.setName("Battle Index Spinner"); // NOI18N

        jButtonImportBattle.setText("Import");
        jButtonImportBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportBattleActionPerformed(evt);
            }
        });

        infoButton7.setText("");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerBattleIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonImportBattle))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonImportBattle)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jSpinnerBattleIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accordionPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accordionPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Export to :"));
        jPanel5.setPreferredSize(new java.awt.Dimension(32, 135));

        jLabel1.setText("Export the battle terrain and spritesets.");

        infoButton9.setMessageText("Exports the battle terrain data and spriteset data (ally, enemy, AI region & AI point).");
        infoButton9.setText("");

        fileButtonExportTerrain.setFilePath(".\\entries\\battle01\\terrain.bin");
        fileButtonExportTerrain.setInfoMessage("");
        fileButtonExportTerrain.setLabelText("Battle terrain :");
        fileButtonExportTerrain.setName("Export Battle Terrain"); // NOI18N

        fileButtonExportSpriteset.setFilePath(".\\spritesets\\spriteset01.asm");
        fileButtonExportSpriteset.setInfoMessage("");
        fileButtonExportSpriteset.setLabelText("Battle spriteset :");
        fileButtonExportSpriteset.setName("Export battle Spriteset"); // NOI18N

        jButtonExportBattle.setText("Export");
        jButtonExportBattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportBattleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExportBattle))
                    .addComponent(fileButtonExportTerrain, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addComponent(fileButtonExportSpriteset, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportTerrain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportSpriteset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportBattle)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Battle & Terrain", jPanel13);

        jLabel21.setText("Export battle map coordinates.");

        infoButton8.setMessageText("Exports the battle coordinates (map & area of the battle).");
        infoButton8.setText("");

        fileButtonExportCoords.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ANY_ASSEMBLY);
        fileButtonExportCoords.setFilePath(".\\global\\battlemapcoords.asm");
        fileButtonExportCoords.setInfoMessage("");
        fileButtonExportCoords.setLabelText("Battle map coords :");
        fileButtonExportCoords.setName("Export Map Coords"); // NOI18N

        jButtonExportCoords.setText("Export");
        jButtonExportCoords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportCoordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonExportCoords, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExportCoords))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportCoords, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportCoords)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Map Coords", jPanel18);

        jLabel23.setText("Export the land effect data.");

        infoButton10.setMessageText("Exports the land effect data (how each block affects movement and defense bonus).");
        infoButton10.setText("");

        fileButtonExportLandEffect.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonExportLandEffect.setFilePath(".\\global\\landeffectsettingsandmovecosts.asm");
        fileButtonExportLandEffect.setInfoMessage("");
        fileButtonExportLandEffect.setLabelText("Land effect :");
        fileButtonExportLandEffect.setName("Export Land Effects"); // NOI18N

        jButtonExportLandEffect.setText("Export");
        jButtonExportLandEffect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportLandEffectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonExportLandEffect, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExportLandEffect))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportLandEffect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportLandEffect)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Land Effect", jPanel19);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane2.setLeftComponent(jPanel8);

        jSplitPane3.setDividerLocation(550);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane4.setDividerLocation(225);
        jSplitPane4.setOneTouchExpandable(true);

        jPanel11.setPreferredSize(new java.awt.Dimension(400, 600));

        jTabbedPane2.setMinimumSize(new java.awt.Dimension(200, 500));
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(200, 500));
        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });
        jTabbedPane2.addTab("Map Coords", mapCoordsPanel1);

        terrainKeyPanel1.setPreferredSize(new java.awt.Dimension(200, 500));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(terrainKeyPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(terrainKeyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Terrain", jPanel4);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jSplitPane4.setLeftComponent(jPanel11);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map"));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 500));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        battleLayoutPanel.setPreferredSize(new java.awt.Dimension(1000, 1000));

        javax.swing.GroupLayout battleLayoutPanelLayout = new javax.swing.GroupLayout(battleLayoutPanel);
        battleLayoutPanel.setLayout(battleLayoutPanelLayout);
        battleLayoutPanelLayout.setHorizontalGroup(
            battleLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );
        battleLayoutPanelLayout.setVerticalGroup(
            battleLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1536, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(battleLayoutPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(battleViewPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(battleViewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jSplitPane4.setRightComponent(jPanel1);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1139, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1139, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))
        );

        jSplitPane3.setLeftComponent(jPanel12);

        jTabbedPane3.setMinimumSize(new java.awt.Dimension(80, 50));
        jTabbedPane3.setPreferredSize(new java.awt.Dimension(300, 200));
        jTabbedPane3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane3StateChanged(evt);
            }
        });

        tableAllies.setBorder(null);
        tableAllies.setInfoMessage("<html>Indicates where allies will spawn during a battle. Ally order is determined by battle order (ally join order).<br>Edit the positions in the table directly - or select a row then click on the map to place the ally battle start position.</html>");
        tableAllies.setModel(allyPropertiesTableModel);
        tableAllies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAllies.setSingleClickText(true);
        tableAllies.setSpinnerNumberEditor(true);
        tableAllies.setMinimumSize(new java.awt.Dimension(300, 100));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAllies, javax.swing.GroupLayout.DEFAULT_SIZE, 1127, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAllies, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Allies", jPanel24);

        tableEnemies.setBorder(null);
        tableEnemies.setHorizontalScrolling(true);
        tableEnemies.setInfoMessage("<html>When you select an enemy in the table, its data can then be edited in the controls below.<br>Like allies, selecting an enemy and then clicking on the map will move the enemy to the new position.<br>Enemy info:<br><b>Enemy</b>: The Id of the enemy. Determines which enemy.<br><b>X/Y</b>: The position of the enemy, relative to the top-left corner of the battle area.<br><b>AI</b>: Which AI logic the enemy will use.<br><b>Spawn</b>: AI Spawn rules:<br>    <b>Starting</b>: The enemy is visible from the start of the battle.<br>    <b>Respawn</b>: Enemy will respawn after being kiled, based on triggers.<br>    <b>Hidden</b>: Enemy does not spawn into the battle until a trigger is activated. If no trigger is activated then enemy will never spawn.<br><b>Item</b>: The item the enemy holds and item flags. Item flags:<br>-    <b>EQUIPPED</b>: Whether the enemy has this item equipped. May not do anything in the base game.<br>-    <b>USABLE_BY_AI</b>: Flags that the item can be used by the AI. The base game is hard-coded so that this only works with Healing Water.<br>-    <b>UNUSED_ITEM_DROP</b>: Flags the item to be dropped when enemy dies if it was not consumed (used) by the enemy.<br>-    <b>BROKEN</b>: Flags the item as broken (cracked) and needs to be repaired.<br><b>Move Order</b>: Directs target to stay in place or move towards specific targets.<br><b>Target (Move order)</b>: The target of the move order. <br><b>Trigger region 1/2</b>: Move orders are activated when region triggers.<br><b>Backup Move Order</b>: Backup move order (TODO What does this do specifically).<br><b>Target (backup)</b>: Target for the backup move order.<br><b>Unknown</b>: It is unknown what this does. Valid values seem to be 0 or 6.</html>");
        tableEnemies.setModel(enemyPropertiesTableModel);
        tableEnemies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableEnemies.setSingleClickText(false);
        tableEnemies.setSpinnerNumberEditor(true);
        tableEnemies.setMinimumSize(new java.awt.Dimension(300, 100));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableEnemies, javax.swing.GroupLayout.DEFAULT_SIZE, 1127, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableEnemies, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Enemies", jPanel25);

        tableAIRegions.setBorder(null);
        tableAIRegions.setHorizontalScrolling(true);
        tableAIRegions.setInfoMessage("<html>AI Regions indicate a an area that can influce specific enemy AI.<br>Edit the positions in the table directly - or select a row then click on the map to move the area around: Click near a point (corner) of the region to drag that point to the desired position.<br><br><b>Points:</b>Regions are made up of a number of points: P1, P2, P3, P4. Or, as you will see in the data [x1,y1], [x2,y2], etc.<br><br><b>Type 3:</b> Type 3 = a 3-point region (a triangle) made up of points [P1, P2, P3].<br><b>Type 4:</b> Type 4 = a 4-point region; or more accurately, 2 triangles made up of points [P1, P2, P4] & [P2, P3, P4].</html>");
        tableAIRegions.setModel(aIRegionPropertiesTableModel);
        tableAIRegions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAIRegions.setSingleClickText(true);
        tableAIRegions.setSpinnerNumberEditor(true);
        tableAIRegions.setMinimumSize(new java.awt.Dimension(300, 100));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIRegions, javax.swing.GroupLayout.DEFAULT_SIZE, 1127, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIRegions, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("AI Regions", jPanel26);

        tableAIPoints.setBorder(null);
        tableAIPoints.setInfoMessage("<html>AI Points indicate a point on the map for the enemy AI to target.<br>Edit the positions in the table directly - or select a row then click on the map to place the ally battle start position.</html>");
        tableAIPoints.setModel(aIPointPropertiesTableModel);
        tableAIPoints.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAIPoints.setSingleClickText(true);
        tableAIPoints.setSpinnerNumberEditor(true);
        tableAIPoints.setMinimumSize(new java.awt.Dimension(300, 100));

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIPoints, javax.swing.GroupLayout.DEFAULT_SIZE, 1127, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableAIPoints, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("AI Points", jPanel31);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1139, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );

        jSplitPane3.setRightComponent(jPanel22);

        jTabbedPane1.addTab("Map", jSplitPane3);

        jPanel7.setPreferredSize(new java.awt.Dimension(500, 298));

        infoButton11.setMessageText("<html>\"Land effect\" determines how each <i>movement type</i> is affected by each <i>terrain type</i>.<br>Each land effect entry determines defensive bonus and the movement cost that will affect the a unit with the specific movement type on the specific terrain.<br><br><b>Defenses:</b><br>Obstructed: Indicates that this <i>movement type</i> cannot move onto this tile.<br>LE0: By default, provides 0% defensive bonus (no bonus).<br>LE15: By default, provides 15% defensive bonus.<br>LE30: By default, provides 30% defensive bonus.<br><br><b>Movement costs:</b> Higher numbers = slower movement on that tile.</html>");
        infoButton11.setText("");

        jLabel22.setText("Land effect info");

        landEffectTable.setHorizontalScrolling(true);
        landEffectTable.setInfoMessage("");
        landEffectTable.setModel(landEffectTableModel);
        landEffectTable.setRowBorders(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(landEffectTable, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(infoButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(landEffectTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Land Effect", jPanel7);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1516, 1008));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportBattleActionPerformed
        Path terrainPath = PathHelpers.getBasePath().resolve(fileButtonExportTerrain.getFilePath());                           
        Path spritesetPath = PathHelpers.getBasePath().resolve(fileButtonExportSpriteset.getFilePath());
        if (!PathHelpers.createPathIfRequred(terrainPath)) return;
        if (!PathHelpers.createPathIfRequred(spritesetPath)) return;
        try {
            battleManager.exportDisassembly(terrainPath, spritesetPath, battleLayoutPanel.getBattle());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle disasm could not be exported to : " + terrainPath + " and/or " + spritesetPath);
        }
    }//GEN-LAST:event_jButtonExportBattleActionPerformed

    private void jButtonImportBattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportBattleActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButtonPaletteEntries.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonTilesetEntries.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButtonMapEntries.getFilePath());
        Path terrainEntriesPath = PathHelpers.getBasePath().resolve(fileButtonTerrainEntries.getFilePath());
        Path battleMapCoordsPath = PathHelpers.getBasePath().resolve(fileButtonImportCoords.getFilePath());
        Path spritesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonSpritesetEntries.getFilePath());
        Path landEffectPath = PathHelpers.getBasePath().resolve(fileButtonImportLandEffects.getFilePath());
        int battleIndex = (int)jSpinnerBattleIndex.getModel().getValue();
        
        Path basePalettePath = PathHelpers.getBasePath().resolve(fileButtonImportBasePalette.getFilePath());
        Path mapspriteEntriesPath = PathHelpers.getBasePath().resolve(fileButtonMapspriteEntries.getFilePath());
        Path enemyMapspritesPath = PathHelpers.getBasePath().resolve(fileButtonEnemyMapsprites.getFilePath());
        Path specialSpritesEntriesPath = PathHelpers.getBasePath().resolve(fileButtonSpecialSpriteEntries.getFilePath());
        Path specialSpritesPointersPath = PathHelpers.getBasePath().resolve(fileButtonSpecialSpritePointers.getFilePath());
        Path mapspriteEnumsPath = PathHelpers.getBasePath().resolve(fileButtonBattleEnums.getFilePath());
        
        try {
            battleManager.importLandEffects(mapspriteEnumsPath, landEffectPath);
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Land effect data could not be imported from : " + landEffectPath);
        }
        try {
            battleManager.importMapspriteData(basePalettePath, mapspriteEntriesPath, enemyMapspritesPath, specialSpritesEntriesPath, specialSpritesPointersPath, mapspriteEnumsPath);
        } catch (Exception ex) {
            battleManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Mapsprite data could not be imported from : " + mapspriteEntriesPath + " and " + mapspriteEnumsPath);
            return;
        }
        try {
            battleManager.importDisassembly(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, terrainEntriesPath, battleMapCoordsPath, spritesetEntriesPath, battleIndex);
        } catch (Exception ex) {
            battleManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle data could not be imported for battle : " + battleIndex);
            return;
        }
        onDataLoaded();
    }//GEN-LAST:event_jButtonImportBattleActionPerformed

    private void jTabbedPane3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane3StateChanged
        int index = jTabbedPane3.getSelectedIndex();
        battleLayoutPanel.setSelectedAlly(-1);
            ActionManager.setExternalActionTriggering(true);
        switch (index) {
            case 0:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.Ally);
                changeDrawMode(true, getDrawAiRegions(), getDrawAiPoints());
                tableAllies.jTable.clearSelection();
                break;
            case 1:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.Enemy);
                changeDrawMode(true, getDrawAiRegions(), getDrawAiPoints());
                tableEnemies.jTable.clearSelection();
                battleViewPanel1.getjCheckBoxSprites().setSelected(true);
                break;
            case 2:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.AiRegion);
                changeDrawMode(getDrawSprites(), true, getDrawAiPoints());
                tableAIRegions.jTable.clearSelection();
                battleViewPanel1.getjCheckBoxRegions().setSelected(true);
                break;
            case 3:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.AiPoint);
                changeDrawMode(getDrawSprites(), getDrawAiRegions(), true);
                tableAIPoints.jTable.clearSelection();
                battleViewPanel1.getjCheckBoxPoints().setSelected(true);
                break;
        }
        if (jTabbedPane2.getSelectedIndex() == 1) {
            battleViewPanel1.getjCheckBoxSprites().setEnabled(battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.Ally && battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.Enemy);
            battleViewPanel1.getjCheckBoxRegions().setEnabled(battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.AiRegion);
            battleViewPanel1.getjCheckBoxPoints().setEnabled(battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.AiPoint);
        }
        ActionManager.setExternalActionTriggering(false);
    }//GEN-LAST:event_jTabbedPane3StateChanged

    private void jButtonExportCoordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportCoordsActionPerformed
        Path coordsPath = PathHelpers.getBasePath().resolve(fileButtonExportCoords.getFilePath());
        if (!PathHelpers.createPathIfRequred(coordsPath)) return;
        try {
            battleManager.exportBattleCoords(coordsPath, battleLayoutPanel.getBattleCoords());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle coords disasm could not be exported to : " + coordsPath);
        }
    }//GEN-LAST:event_jButtonExportCoordsActionPerformed

    private void multiComboBoxItemFlagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiComboBoxItemFlagsActionPerformed
        //OnEnemyDataChanged(multiComboBoxItemFlags.getObjectsString(), 6);
    }//GEN-LAST:event_multiComboBoxItemFlagsActionPerformed

    private void jButtonExportLandEffectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportLandEffectActionPerformed
        Path landEffectPath = PathHelpers.getBasePath().resolve(fileButtonExportLandEffect.getFilePath());
        if (!PathHelpers.createPathIfRequred(landEffectPath)) return;
        try {
            battleManager.exportLandEffects(landEffectPath, landEffectTableModel.getTableData(LandEffectMovementType[].class));
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle coords disasm could not be exported to : " + landEffectPath);
        }
    }//GEN-LAST:event_jButtonExportLandEffectActionPerformed

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        int index = jTabbedPane2.getSelectedIndex();
        battleLayoutPanel.setSelectedAlly(-1);
        switch (index) {
            case 0:
                ActionManager.setExternalActionTriggering(true);
                battleViewPanel1.getjCheckBoxTerrain().setSelected(getDrawTerrain());
                ActionManager.setExternalActionTriggering(false);
                break;
            case 1:
                ActionManager.setExternalActionTriggering(true);
                battleLayoutPanel.setPaintMode(BattlePaintMode.Terrain);
                battleViewPanel1.getjCheckBoxTerrain().setSelected(true);
                battleLayoutPanel.setDrawTerrain(true);
                ActionManager.setExternalActionTriggering(false);
                break;
        }
    }//GEN-LAST:event_jTabbedPane2StateChanged
    
    private void onMapIndexChange(ActionEvent evt) {
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButtonPaletteEntries.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButtonTilesetEntries.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButtonMapEntries.getFilePath());
        int newMapIndex = evt.getID();
        try {
            MapLayout layout = battleManager.loadNewMap(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, newMapIndex);
            ActionManager.setActionWithoutExecute(new BasicAction<MapLayout>(this, "Map Changed", this::actionMapChanged, layout, battleLayoutPanel.getMapLayout()));
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map " + newMapIndex + " could not be imported");
        }
    }
    
    private void actionMapChanged(MapLayout layout) {
        battleLayoutPanel.getBattle().setMapLayout(layout);
        battleLayoutPanel.setMapLayout(layout);
    }
    
    private void changeDrawMode(boolean sprites, boolean aiRegions, boolean aiModes) {
        ActionManager.setExternalActionTriggering(true);
        battleViewPanel1.getjCheckBoxSprites().setSelected(sprites);
        battleViewPanel1.getjCheckBoxRegions().setSelected(aiRegions);
        battleViewPanel1.getjCheckBoxPoints().setSelected(aiModes);
        battleLayoutPanel.setDrawSprites(sprites);
        battleLayoutPanel.setDrawAiRegions(aiRegions);
        battleLayoutPanel.setDrawAiPoints(aiModes);
        ActionManager.setExternalActionTriggering(false);
    }
    
    private void onTableAlliesDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedAlly()) {
            battleLayoutPanel.redraw();
        }
        if (e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT) {
            battleLayoutPanel.getBattle().getSpriteset().setAllies(allyPropertiesTableModel.getTableData(Ally[].class));
        }
    }

    private void onTableAlliesSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tableAllies.jTable.getSelectedRow();
        if (row != battleLayoutPanel.getSelectedAlly()) {
            battleLayoutPanel.setSelectedAlly(row);
            battleLayoutPanel.setPaintMode(BattlePaintMode.Spriteset);
        }
    }

    private void onTableEnemiesDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedEnemy()) {
            battleLayoutPanel.redraw();
        }
        if (e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT) {
            battleLayoutPanel.getBattle().getSpriteset().setEnemies(enemyPropertiesTableModel.getTableData(Enemy[].class));
        }
    }

    private void onTableEnemiesSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tableEnemies.jTable.getSelectedRow();
        if (row != battleLayoutPanel.getSelectedEnemy()) {
            battleLayoutPanel.setSelectedEnemy(row);
            battleLayoutPanel.setPaintMode(BattlePaintMode.Spriteset);
        }
    }

    private void onTableAIRegionsDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedAIRegion()) {
            battleLayoutPanel.redraw();
        }
        if (e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT) {
            battleLayoutPanel.getBattle().getSpriteset().setAiRegions(aIRegionPropertiesTableModel.getTableData(AIRegion[].class));
        }
    }

    private void onTableAIRegionsSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tableAIRegions.jTable.getSelectedRow();
        if (row != battleLayoutPanel.getSelectedAIRegion()) {
            battleLayoutPanel.setDrawAiRegions(true);
            battleLayoutPanel.setSelectedAIRegion(row);
            battleLayoutPanel.setPaintMode(BattlePaintMode.Spriteset);
        }
    }

    private void onTableAIPointsDataChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        if (row == battleLayoutPanel.getSelectedAIPoint()) {
            battleLayoutPanel.redraw();
        }
        if (e.getType() == TableModelEvent.DELETE || e.getType() == TableModelEvent.INSERT) {
            battleLayoutPanel.getBattle().getSpriteset().setAiPoints(aIPointPropertiesTableModel.getTableData(AIPoint[].class));
        }
    }

    private void onTableAIPointsSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tableAIPoints.jTable.getSelectedRow();
        if (row != battleLayoutPanel.getSelectedAIPoint()) {
            battleLayoutPanel.setDrawAiPoints(true);
            battleLayoutPanel.setSelectedAIPoint(row);
            battleLayoutPanel.setPaintMode(BattlePaintMode.Spriteset);
        }
    }
    
    private void onLayoutSpritesetChanged(ActionEvent e) {
        int row = e.getID();
        if (row != -1) {
            switch (e.getActionCommand()) {
                case "Ally":
                    allyPropertiesTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "Enemy":
                    enemyPropertiesTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "AiRegion":
                    aIRegionPropertiesTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "AiPoint":
                    aIPointPropertiesTableModel.fireTableRowsUpdated(row, row);
                    break;
            }
        }
        battleLayoutPanel.setPaintMode(BattlePaintMode.Spriteset);
    }
    
    private void onTerrainSelectionChanged(ActionEvent e) {
        int terrain = Integer.parseInt(e.getActionCommand());
        battleLayoutPanel.setSelectedTerrainType(terrain);
        battleLayoutPanel.setPaintMode(BattlePaintMode.Terrain);
    }
    
    private void onTerrainModeChanged(ActionEvent e) {
        TerrainDrawMode drawMode = (TerrainDrawMode)e.getSource();
        battleLayoutPanel.setTerrainDrawMode(drawMode);
    }
    
    /**
     * To create a new Main Editor, copy the below code
     * Don't forget to change the new main class (below)
     */
    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BattleEditorMainEditor().setVisible(true);  // <------ Change this class to new Main Editor class
            }
        });
    }
    /**
     * To create a new Main Editor, copy the above code
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.battle.models.AIPointPropertiesTableModel aIPointPropertiesTableModel;
    private com.sfc.sf2.battle.models.AIRegionPropertiesTableModel aIRegionPropertiesTableModel;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel1;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanel2;
    private com.sfc.sf2.battle.models.AllyPropertiesTableModel allyPropertiesTableModel;
    private com.sfc.sf2.battle.gui.BattleLayoutPanel battleLayoutPanel;
    private com.sfc.sf2.battle.gui.BattleViewPanel battleViewPanel1;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.battle.models.EnemyPropertiesTableModel enemyPropertiesTableModel;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonBattleEnums;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonEnemyMapsprites;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportCoords;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportLandEffect;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportSpriteset;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportTerrain;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportBasePalette;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportCoords;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonImportLandEffects;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonMapEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonMapspriteEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonPaletteEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonSpecialSpriteEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonSpecialSpritePointers;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonSpritesetEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTerrainEntries;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonTilesetEntries;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton10;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton11;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton7;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton8;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton9;
    private javax.swing.JButton jButtonExportBattle;
    private javax.swing.JButton jButtonExportCoords;
    private javax.swing.JButton jButtonExportLandEffect;
    private javax.swing.JButton jButtonImportBattle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinnerBattleIndex;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private com.sfc.sf2.battle.mapterrain.gui.LandEffectTable landEffectTable;
    private com.sfc.sf2.battle.mapterrain.models.LandEffectTableModel landEffectTableModel;
    private com.sfc.sf2.battle.gui.MapCoordsPanel mapCoordsPanel1;
    private com.sfc.sf2.core.gui.controls.Table tableAIPoints;
    private com.sfc.sf2.core.gui.controls.Table tableAIRegions;
    private com.sfc.sf2.core.gui.controls.Table tableAllies;
    private com.sfc.sf2.battle.tables.EnemyPropertiesTable tableEnemies;
    private com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel terrainKeyPanel1;
    // End of variables declaration//GEN-END:variables
}
