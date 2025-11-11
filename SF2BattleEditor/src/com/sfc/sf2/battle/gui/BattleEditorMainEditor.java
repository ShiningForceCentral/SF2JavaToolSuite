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
import com.sfc.sf2.battle.EnemyEnums;
import com.sfc.sf2.battle.gui.BattleLayoutPanel.BattlePaintMode;
import com.sfc.sf2.battle.gui.BattleLayoutPanel.SpritesetPaintMode;
import com.sfc.sf2.battle.mapcoords.BattleMapCoords;
import com.sfc.sf2.battle.mapterrain.LandEffectEnums;
import com.sfc.sf2.battle.mapterrain.LandEffectMovementType;
import com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel.TerrainDrawMode;
import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.map.layout.MapLayout;
import com.sfc.sf2.settings.TerrainSettings;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
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
    
    private boolean drawSprites;
    private boolean drawAiRegions;
    private boolean drawAiPoints;
    
    public BattleEditorMainEditor() {
        super();
        SettingsManager.registerSettingsStore("terrain", terrainSettings);
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
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
        
        colorPicker1.setColor(SettingsManager.getGlobalSettings().getTransparentBGColor());
        
        battleLayoutPanel.setDisplayScale(jComboBox1.getSelectedIndex()+1);
        battleLayoutPanel.setShowGrid(jCheckBox2.isSelected());
        battleLayoutPanel.setBGColor(colorPicker1.getColor());
        battleLayoutPanel.setDrawTerrain(jCheckBox3.isSelected());
        battleLayoutPanel.setShowExplorationFlags(jCheckBox1.isSelected());
        battleLayoutPanel.setDrawTerrain(jCheckBox3.isSelected());
        battleLayoutPanel.setShowBattleCoords(true);
        
        drawSprites = jCheckBox4.isSelected();
        drawAiRegions = jCheckBox6.isSelected();
        drawAiPoints = jCheckBox7.isSelected();
        battleLayoutPanel.setDrawSprites(jCheckBox4.isSelected());
        battleLayoutPanel.setDrawAiRegions(drawAiRegions);
        battleLayoutPanel.setDrawAiPoints(drawAiPoints);
        battleLayoutPanel.setSpritesetEditedListener(this::onLayoutSpritesetChanged);
        
        jTabbedPane2StateChanged(null);
        jTabbedPane3StateChanged(null);
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        
        battleLayoutPanel.setBattle(battleManager.getBattle());
        battleLayoutPanel.setTerrain(battleManager.getBattle().getTerrain());
        battleLayoutPanel.setBattleCoords(battleManager.getBattleCoords());
        battleLayoutPanel.setMapLayout(battleManager.getMapLayout());
        
        LandEffectEnums landEffectEnums = battleManager.getLandEffectEnums();
        landEffectTable.setLandEffectData(landEffectEnums);
        landEffectTableModel.setTableData(battleManager.getLandEffects());
        
        BattleMapCoords coords = battleManager.getBattleCoords();
        jSpinner1.setValue(coords.getMap());
        jSpinner2.setValue(coords.getX());
        jSpinner3.setValue(coords.getY());
        jSpinner5.setValue(coords.getWidth());
        jSpinner6.setValue(coords.getHeight());
        jSpinner7.setValue(coords.getTrigX());
        jSpinner8.setValue(coords.getTrigY());
        UpdateEnemyControls(-1);
        
        BattleSpriteset spriteset = battleManager.getBattle().getSpriteset();
        if (spriteset == null) {
            allyPropertiesTableModel.setTableData(null);
            enemyPropertiesTableModel.setTableData(null);
            aIRegionPropertiesTableModel.setTableData(null);
            aIPointPropertiesTableModel.setTableData(null);
        } else {
            allyPropertiesTableModel.setTableData(spriteset.getAllies());
            enemyPropertiesTableModel.setTableData(spriteset.getEnemies());
            enemyPropertiesTableModel.setEnemyData(battleManager.getEnemyData(), battleManager.getEnemyEnums());
            aIRegionPropertiesTableModel.setTableData(spriteset.getAiRegions());
            aIPointPropertiesTableModel.setTableData(spriteset.getAiPoints());
        }
        
        EnemyEnums enemyEnums = battleManager.getEnemyEnums();
        jComboBox_Name.setModel(new DefaultComboBoxModel<>(enemyEnums.getEnemies().keySet().toArray(new String[enemyEnums.getEnemies().size()])));
        jComboBox_AI.setModel(new DefaultComboBoxModel<>(enemyEnums.getCommandSets().keySet().toArray(new String[enemyEnums.getCommandSets().size()])));
        jComboBox_Spawn.setModel(new DefaultComboBoxModel<>(enemyEnums.getSpawnParams().keySet().toArray(new String[enemyEnums.getSpawnParams().size()])));
        jComboBox_Order1.setModel(new DefaultComboBoxModel<>(enemyEnums.getOrders().keySet().toArray(new String[enemyEnums.getOrders().size()])));
        jComboBox_Order2.setModel(new DefaultComboBoxModel<>(enemyEnums.getOrders().keySet().toArray(new String[enemyEnums.getOrders().size()])));
        jComboBox_Items.setModel(new DefaultComboBoxModel<>(enemyEnums.getItems().keySet().toArray(new String[enemyEnums.getItems().size()])));
        multiComboBoxItemFlags.setModel(new DefaultComboBoxModel<>(enemyEnums.getItemFlags().keySet().toArray(new String[enemyEnums.getItemFlags().size()])));
        
        
        String sharedTerrainInfo = battleManager.getSharedTerrainInfo();
        terrainKeyPanel1.setSharedTerrainInfo(sharedTerrainInfo);
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
        fileButton1 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton2 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton3 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton4 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton5 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton9 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton16 = new com.sfc.sf2.core.gui.controls.FileButton();
        accordionPanel2 = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButton6 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton7 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton8 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton13 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton14 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton15 = new com.sfc.sf2.core.gui.controls.FileButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jButton18 = new javax.swing.JButton();
        infoButton7 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        infoButton9 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButton11 = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButton12 = new com.sfc.sf2.core.gui.controls.FileButton();
        jButton2 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        infoButton8 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButton10 = new com.sfc.sf2.core.gui.controls.FileButton();
        jButton3 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        infoButton10 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButton17 = new com.sfc.sf2.core.gui.controls.FileButton();
        jButton4 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jSpinner6 = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner7 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSpinner8 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jCheckBox5 = new javax.swing.JCheckBox();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        terrainKeyPanel1 = new com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel24 = new javax.swing.JPanel();
        tableAllies = new com.sfc.sf2.core.gui.controls.Table();
        infoButton2 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        tableEnemies = new com.sfc.sf2.core.gui.controls.Table();
        jPanel17 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSpinner_X = new javax.swing.JSpinner();
        jSpinner_Y = new javax.swing.JSpinner();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jComboBox_Name = new javax.swing.JComboBox<>();
        jComboBox_AI = new javax.swing.JComboBox<>();
        jComboBox_Spawn = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jComboBox_Items = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jComboBox_Order1 = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        jSpinner_OrderTarget1 = new javax.swing.JSpinner();
        jLabel37 = new javax.swing.JLabel();
        jSpinner_Trigger1 = new javax.swing.JSpinner();
        jSpinner_Trigger2 = new javax.swing.JSpinner();
        jLabel38 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jComboBox_Order2 = new javax.swing.JComboBox<>();
        jLabel42 = new javax.swing.JLabel();
        jSpinner_OrderTarget2 = new javax.swing.JSpinner();
        jLabel39 = new javax.swing.JLabel();
        jSpinner_Unknown = new javax.swing.JSpinner();
        multiComboBoxItemFlags = new com.sfc.sf2.core.gui.MultiComboBox();
        infoButton6 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton3 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel13 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        tableAIRegions = new com.sfc.sf2.core.gui.controls.Table();
        infoButton5 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel20 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        tableAIPoints = new com.sfc.sf2.core.gui.controls.Table();
        infoButton4 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        battleLayoutPanel = new com.sfc.sf2.battle.gui.BattleLayoutPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        colorPicker1 = new com.sfc.sf2.core.gui.controls.ColorPicker();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        infoButton11 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jLabel22 = new javax.swing.JLabel();
        landEffectTable = new com.sfc.sf2.battle.mapterrain.gui.LandEffectTable();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2BattleEditor");

        jSplitPane1.setDividerLocation(750);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(350);
        jSplitPane2.setOneTouchExpandable(true);

        jPanel9.setPreferredSize(new java.awt.Dimension(300, 733));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from :"));
        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        accordionPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map & terrain"));

        fileButton1.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton1.setFilePath("..\\graphics\\maps\\mappalettes\\entries.asm");
        fileButton1.setInfoMessage("Entries file for map palettes.");
        fileButton1.setLabelText("Palette entries :");

        fileButton2.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton2.setFilePath("..\\graphics\\maps\\maptilesets\\entries.asm");
        fileButton2.setInfoMessage("Entries file for map tilesets.");
        fileButton2.setLabelText("Tileset entries :");

        fileButton3.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton3.setFilePath("..\\maps\\entries.asm");
        fileButton3.setInfoMessage("<html>Map entries file. Lists all maps defined to be used in the game.<br>If a map is not loading then it might not have been added to the entries.</html>");
        fileButton3.setLabelText("Map entries :");

        fileButton4.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton4.setFilePath(".\\terrainentries.asm");
        fileButton4.setInfoMessage("Entries for battle terrain data.");
        fileButton4.setLabelText("Terrain entries :");

        fileButton5.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton5.setFilePath(".\\global\\battlemapcoords.asm");
        fileButton5.setInfoMessage("Assembly file that defines the map and boundries for all battles.");
        fileButton5.setLabelText("Battle map coords :");

        fileButton9.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton9.setFilePath(".\\spritesets\\entries.asm");
        fileButton9.setInfoMessage("<html>The entries file for battle data.<br>Battle \"Spritesets\" include ally positions, enemy info, AI Regions, and AI points.</html>");
        fileButton9.setLabelText("Spriteset entries :");

        fileButton16.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton16.setFilePath(".\\global\\landeffectsettingsandmovecosts.asm");
        fileButton16.setInfoMessage("Assembly file that defines the land effects (block move cost and defense bonus) for each battle.");
        fileButton16.setLabelText("Land effects :");

        javax.swing.GroupLayout accordionPanel1Layout = new javax.swing.GroupLayout(accordionPanel1);
        accordionPanel1.setLayout(accordionPanel1Layout);
        accordionPanel1Layout.setHorizontalGroup(
            accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addComponent(fileButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(fileButton16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        accordionPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Map sprites :"));

        fileButton6.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButton6.setFilePath("..\\graphics\\tech\\basepalette.bin");
        fileButton6.setInfoMessage("The base palette to use for map sprites.");
        fileButton6.setLabelText("Base palette :");

        fileButton7.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton7.setFilePath("..\\graphics\\mapsprites\\entries.asm");
        fileButton7.setInfoMessage("The entries file for map sprites.");
        fileButton7.setLabelText("Mapsprite entries :");

        fileButton8.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton8.setFilePath("..\\..\\sf2enums.asm");
        fileButton8.setInfoMessage("Loads data from SF2Enums (such as enemy and item names).");
        fileButton8.setLabelText("Battle enums :");

        fileButton13.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton13.setFilePath("..\\stats\\enemies\\enemymapsprites.asm");
        fileButton13.setInfoMessage("The file that determine which Mapsprite is used by which defined enemy.");
        fileButton13.setLabelText("Enemy mapsprites :");

        fileButton14.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton14.setFilePath("..\\graphics\\specialsprites\\entries.asm");
        fileButton14.setInfoMessage("<html>Defines the special sprites (larger sprites). Certain enemies (e.g. bosses) use special sprites.</html>");
        fileButton14.setLabelText("Special Sprites entries :");

        fileButton15.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton15.setFilePath("..\\graphics\\specialsprites\\pointers.asm");
        fileButton15.setInfoMessage("Pointers file used to load Special Sprites.");
        fileButton15.setLabelText("Special sprites pointers :");

        javax.swing.GroupLayout accordionPanel2Layout = new javax.swing.GroupLayout(accordionPanel2);
        accordionPanel2.setLayout(accordionPanel2Layout);
        accordionPanel2Layout.setHorizontalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        accordionPanel2Layout.setVerticalGroup(
            accordionPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Battle :"));

        jLabel2.setText("<html>Load battle from index.</html>");

        jLabel10.setText("Battle index :");

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1, 0, 255, 1));

        jButton18.setText("Import");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
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
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18))
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
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton18)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        fileButton11.setFilePath(".\\entries\\battle01\\terrain.bin");
        fileButton11.setInfoMessage("");
        fileButton11.setLabelText("Battle terrain :");

        fileButton12.setFilePath(".\\spritesets\\spriteset01.asm");
        fileButton12.setInfoMessage("");
        fileButton12.setLabelText("Battle spriteset :");

        jButton2.setText("Export");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                        .addComponent(jButton2))
                    .addComponent(fileButton11, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                .addComponent(fileButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButton12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Battle & Terrain", jPanel13);

        jLabel21.setText("Export battle map coordinates.");

        infoButton8.setMessageText("Exports the battle coordinates (map & area of the battle).");
        infoButton8.setText("");

        fileButton10.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ANY_ASSEMBLY);
        fileButton10.setFilePath(".\\global\\battlemapcoords.asm");
        fileButton10.setInfoMessage("");
        fileButton10.setLabelText("Battle map coords :");

        jButton3.setText("Export");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton10, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3))
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
                .addComponent(fileButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Map Coords", jPanel18);

        jLabel23.setText("Export the land effect data.");

        infoButton10.setMessageText("Exports the land effect data (how each block affects movement and defense bonus).");
        infoButton10.setText("");

        fileButton17.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButton17.setFilePath(".\\global\\landeffectsettingsandmovecosts.asm");
        fileButton17.setInfoMessage("");
        fileButton17.setLabelText("Land effect :");

        jButton4.setText("Export");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButton17, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4))
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
                .addComponent(fileButton17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );

        jTabbedPane4.addTab("Land Effect", jPanel19);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 606, Short.MAX_VALUE)
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
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane2.setLeftComponent(jPanel8);

        jSplitPane4.setDividerLocation(400);
        jSplitPane4.setOneTouchExpandable(true);

        jPanel11.setPreferredSize(new java.awt.Dimension(400, 600));

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });

        jPanel4.setMinimumSize(new java.awt.Dimension(400, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(400, 677));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Map coords"));

        jLabel6.setText("Start Y :");

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        jLabel7.setText("Width :");

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(0, 0, 48, 1));
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner5StateChanged(evt);
            }
        });

        jLabel8.setText("Height :");

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(0, 0, 48, 1));
        jSpinner6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner6StateChanged(evt);
            }
        });

        jLabel9.setText("Trigger X :");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(0, 0, 256, 1));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        jSpinner7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner7StateChanged(evt);
            }
        });

        jLabel3.setText("Map :");

        jLabel11.setText("Trigger Y :");

        jLabel5.setText("Start X :");

        jSpinner8.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        jSpinner8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner8StateChanged(evt);
            }
        });

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jCheckBox5.setText("Lock unit positions");

        infoButton1.setMessageText("<html>Changing the Start X and Start Y will move the allies, enemies, AI regions, and AI points. Toggling this on will fix those elements to the map while moving the battle boundaries.<br><br><b>WARNING</b> Points outside of the battle area will not be saved properly. A red alert icon will show any points outside of the battle area.</html>");
        infoButton1.setText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinner5, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(jSpinner2)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner1)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSpinner3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                                    .addComponent(jSpinner6)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jCheckBox5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBox5)
                    .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinner7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jSpinner8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(terrainKeyPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(terrainKeyPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(227, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Battle Map Edit", jPanel4);

        jTabbedPane3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane3StateChanged(evt);
            }
        });

        tableAllies.setBorder(null);
        tableAllies.setModel(allyPropertiesTableModel);
        tableAllies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAllies.setSingleClickText(true);
        tableAllies.setSpinnerNumberEditor(true);

        infoButton2.setMessageText("<html>Indicates where allies will spawn during a battle. Ally order is determined by battle order (ally join order).<br>Edit the positions in the table directly - or select a row then click on the map to place the ally battle start position.</html>");
        infoButton2.setText("");

        jLabel12.setText("Editing ally battle start positions :");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableAllies, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableAllies, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Allies", jPanel24);

        tableEnemies.setBorder(null);
        tableEnemies.setHorizontalScrolling(true);
        tableEnemies.setModel(enemyPropertiesTableModel);
        tableEnemies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableEnemies.setSingleClickText(true);
        tableEnemies.setSpinnerNumberEditor(true);
        tableEnemies.setMinimumSize(new java.awt.Dimension(260, 200));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Enemy :"));

        jLabel16.setText("Enemy :");

        jLabel17.setText("X :");

        jSpinner_X.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_X.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_XStateChanged(evt);
            }
        });

        jSpinner_Y.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Y.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_YStateChanged(evt);
            }
        });

        jLabel18.setText("Y :");

        jLabel19.setText("AI :");

        jComboBox_Name.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Name.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_NameItemStateChanged(evt);
            }
        });

        jComboBox_AI.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_AI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_AIItemStateChanged(evt);
            }
        });

        jComboBox_Spawn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Spawn.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_SpawnItemStateChanged(evt);
            }
        });

        jLabel40.setText("Spawn :");

        jLabel34.setText("Item :");

        jComboBox_Items.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Items.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_ItemsItemStateChanged(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel36.setText("Move Order :");

        jComboBox_Order1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Order1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_Order1ItemStateChanged(evt);
            }
        });

        jLabel41.setText("Target :");

        jSpinner_OrderTarget1.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_OrderTarget1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_OrderTarget1StateChanged(evt);
            }
        });

        jLabel37.setText("Trigger Region 1 :");

        jSpinner_Trigger1.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Trigger1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_Trigger1StateChanged(evt);
            }
        });

        jSpinner_Trigger2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Trigger2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_Trigger2StateChanged(evt);
            }
        });

        jLabel38.setText("Trigger Region 2 :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_Trigger1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel38))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_Order1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSpinner_OrderTarget1)
                    .addComponent(jSpinner_Trigger2))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jSpinner_OrderTarget1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41)
                    .addComponent(jComboBox_Order1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jSpinner_Trigger1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38)
                    .addComponent(jSpinner_Trigger2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel35.setText("Backup Move Order :");

        jComboBox_Order2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Order2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_Order2ItemStateChanged(evt);
            }
        });

        jLabel42.setText("Target :");

        jSpinner_OrderTarget2.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_OrderTarget2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_OrderTarget2StateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox_Order2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner_OrderTarget2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel42)
                        .addComponent(jSpinner_OrderTarget2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel35)
                        .addComponent(jComboBox_Order2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jLabel39.setText("Unknown :");

        jSpinner_Unknown.setModel(new javax.swing.SpinnerNumberModel(0, 0, 63, 1));
        jSpinner_Unknown.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner_UnknownStateChanged(evt);
            }
        });

        multiComboBoxItemFlags.setMinimumSize(new java.awt.Dimension(150, 26));
        multiComboBoxItemFlags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                multiComboBoxItemFlagsActionPerformed(evt);
            }
        });

        infoButton6.setMessageText("<html>Enemy info:<br><b>Enemy</b>: The Id of the enemy. Determines which enemy.<br><b>X/Y</b>: The position of the enemy, relative to the top-left corner of the battle area.<br><b>AI</b>: Which AI logic the enemy will use.<br><b>Spawn</b>: AI Spawn rules:<br>    <b>Starting</b>: The enemy is visible from the start of the battle.<br>    <b>Respawn</b>: Enemy will respawn after being kiled, based on triggers.<br>    <b>Hidden</b>: Enemy does not spawn into the battle until a trigger is activated. If no trigger is activated then enemy will never spawn.<br><b>Item</b>: The item the enemy holds and item flags. Item flags:<br>-    <b>EQUIPPED</b>: Whether the enemy has this item equipped. May not do anything in the base game.<br>-    <b>USABLE_BY_AI</b>: Flags that the item can be used by the AI. The base game is hard-coded so that this only works with Healing Water.<br>-    <b>UNUSED_ITEM_DROP</b>: Flags the item to be dropped when enemy dies if it was not consumed (used) by the enemy.<br>-    <b>BROKEN</b>: Flags the item as broken (cracked) and needs to be repaired.<br><b>Move Order</b>: Directs target to stay in place or move towards specific targets.<br><b>Target (Move order)</b>: The target of the move order. <br><b>Trigger region 1/2</b>: Move orders are activated when region triggers.<br><b>Backup Move Order</b>: Backup move order (TODO What does this do specifically).<br><b>Target (backup)</b>: Target for the backup move order.<br><b>Unknown</b>: It is unknown what this does. Valid values seem to be 0 or 6.</html>");
        infoButton6.setText("");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_Unknown, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox_Name, 0, 113, Short.MAX_VALUE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel34))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox_Items, 0, 115, Short.MAX_VALUE)
                                    .addComponent(jComboBox_AI, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSpinner_Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox_Spawn, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(multiComboBoxItemFlags, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner_X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jSpinner_Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jComboBox_AI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(jComboBox_Spawn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel34)
                        .addComponent(jComboBox_Items, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(multiComboBoxItemFlags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(infoButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel39)
                        .addComponent(jSpinner_Unknown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        infoButton3.setMessageText("<html>When you select an enemy in the table, its data can then be edited in the controls below.<br>Like allies, selecting an enemy and then clicking on the map will move the enemy to the new position.</html>");
        infoButton3.setText("");

        jLabel13.setText("Editing enmy data :");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableEnemies, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableEnemies, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane3.addTab("Enemies", jPanel25);

        tableAIRegions.setBorder(null);
        tableAIRegions.setHorizontalScrolling(true);
        tableAIRegions.setModel(aIRegionPropertiesTableModel);
        tableAIRegions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAIRegions.setSingleClickText(true);
        tableAIRegions.setSpinnerNumberEditor(true);

        infoButton5.setMessageText("<html>AI Regions indicate a an area that can influce specific enemy AI.<br>Edit the positions in the table directly - or select a row then click on the map to move the area around: Click near a point (corner) of the region to drag that point to the desired position.<br><br><b>Points:</b>Regions are made up of a number of points: P1, P2, P3, P4. Or, as you will see in the data [x1,y1], [x2,y2], etc.<br><br><b>Type 3:</b> Type 3 = a 3-point region (a triangle) made up of points [P1, P2, P3].<br><b>Type 4:</b> Type 4 = a 4-point region; or more accurately, 2 triangles made up of points [P1, P2, P4] & [P2, P3, P4].</html>");
        infoButton5.setText("");

        jLabel20.setText("Editing AI Regions :");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableAIRegions, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableAIRegions, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("AI Regions", jPanel26);

        tableAIPoints.setBorder(null);
        tableAIPoints.setModel(aIPointPropertiesTableModel);
        tableAIPoints.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAIPoints.setSingleClickText(true);
        tableAIPoints.setSpinnerNumberEditor(true);

        infoButton4.setMessageText("<html>AI Points indicate a point on the map for the enemy AI to target.<br>Edit the positions in the table directly - or select a row then click on the map to place the ally battle start position.</html>");
        infoButton4.setText("");

        jLabel15.setText("Editing AI Points :");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableAIPoints, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableAIPoints, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("AI Points", jPanel31);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTabbedPane2.addTab("Spriteset", jPanel22);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane2.getAccessibleContext().setAccessibleName("Map Edit");

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

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Display"));

        jLabel4.setText("Scale :");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Exploration");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Show grid");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Terrain");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Sprites");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jLabel14.setText("BG :");

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

        jCheckBox6.setText("AI regions");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jCheckBox7.setText("AI points");
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBox2)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(colorPicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox1)
                        .addComponent(jCheckBox3)
                        .addComponent(jCheckBox4)
                        .addComponent(jCheckBox6)
                        .addComponent(jCheckBox7)))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jSplitPane4.setRightComponent(jPanel1);

        jTabbedPane1.addTab("Map & Spritesets", jSplitPane4);

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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1139, Short.MAX_VALUE))
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
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 900, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1516, 908));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Path terrainPath = PathHelpers.getBasePath().resolve(fileButton11.getFilePath());                           
        Path spritesetPath = PathHelpers.getBasePath().resolve(fileButton12.getFilePath());
        if (!PathHelpers.createPathIfRequred(terrainPath)) return;
        if (!PathHelpers.createPathIfRequred(spritesetPath)) return;
        try {
            battleManager.exportDisassembly(terrainPath, spritesetPath, battleLayoutPanel.getBattle());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle disasm could not be exported to : " + terrainPath + " and/or " + spritesetPath);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedIndex() >= 0) {
            battleLayoutPanel.setDisplayScale(jComboBox1.getSelectedIndex()+1);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        battleLayoutPanel.setShowExplorationFlags(jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        battleLayoutPanel.setShowGrid(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        battleLayoutPanel.setDrawTerrain(jCheckBox3.isSelected());
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton3.getFilePath());
        Path terrainEntriesPath = PathHelpers.getBasePath().resolve(fileButton4.getFilePath());
        Path battleMapCoordsPath = PathHelpers.getBasePath().resolve(fileButton5.getFilePath());
        Path spritesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton9.getFilePath());
        Path landEffectPath = PathHelpers.getBasePath().resolve(fileButton16.getFilePath());
        int battleIndex = (int)jSpinner4.getModel().getValue();
        
        Path basePalettePath = PathHelpers.getBasePath().resolve(fileButton6.getFilePath());
        Path mapspriteEntriesPath = PathHelpers.getBasePath().resolve(fileButton7.getFilePath());
        Path enemyMapspritesPath = PathHelpers.getBasePath().resolve(fileButton13.getFilePath());
        Path specialSpritesEntriesPath = PathHelpers.getBasePath().resolve(fileButton14.getFilePath());
        Path specialSpritesPointersPath = PathHelpers.getBasePath().resolve(fileButton15.getFilePath());
        Path mapspriteEnumsPath = PathHelpers.getBasePath().resolve(fileButton8.getFilePath());
        
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
    }//GEN-LAST:event_jButton18ActionPerformed

    private void UpdateEnemyControls(int selectedRow) {
        boolean enabled = selectedRow >= 0;
        jComboBox_Name.setEnabled(enabled);
        jSpinner_X.setEnabled(enabled);
        jSpinner_Y.setEnabled(enabled);
        jComboBox_AI.setEnabled(enabled);
        jComboBox_Spawn.setEnabled(enabled);
        jSpinner_Trigger1.setEnabled(enabled);
        jSpinner_Trigger2.setEnabled(enabled);
        jComboBox_Order1.setEnabled(enabled);
        jComboBox_Order2.setEnabled(enabled);
        jSpinner_OrderTarget1.setEnabled(enabled);
        jSpinner_OrderTarget2.setEnabled(enabled);
        jComboBox_Items.setEnabled(enabled);
        multiComboBoxItemFlags.setEnabled(enabled);
        jSpinner_Unknown.setEnabled(enabled);

        if (selectedRow == -1) return;
        Enemy enemy = (selectedRow == -1) ? null : battleManager.getBattle().getSpriteset().getEnemies()[selectedRow];
        if (enemy == null) return;
        
        jComboBox_Name.setSelectedItem(enemy.getEnemyData().getName());
        jSpinner_X.setValue(enemy.getX());
        jSpinner_Y.setValue(enemy.getY());
        jComboBox_AI.setSelectedItem(enemy.getAi());
        jComboBox_Spawn.setSelectedItem(enemy.getSpawnParams());
        jSpinner_Trigger1.setValue(enemy.getTriggerRegion1());
        jSpinner_Trigger2.setValue(enemy.getTriggerRegion2());

        jComboBox_Order1.setSelectedItem(enemy.getMoveOrder());
        jSpinner_OrderTarget1.setValue(enemy.getMoveOrderTarget());
        jComboBox_Order2.setSelectedItem(enemy.getBackupMoveOrder());
        jSpinner_OrderTarget2.setValue(enemy.getBackupMoveOrderTarget());

        jComboBox_Items.setSelectedItem(enemy.getItem());
        multiComboBoxItemFlags.clearSelection();
        String flags = enemy.getItemFlags();
        if (flags != null && flags.length() > 0 && flags.contains("|")) {
            String[] split = flags.split("\\|");
            for (int i = 0; i < split.length; i++) {
                multiComboBoxItemFlags.setSelected(split[i], true);
            }
        }
        multiComboBoxItemFlags.setEnabled(!enemy.getItem().equals("NOTHING"));
        jSpinner_Unknown.setValue(enemy.getUnknown());
    }
    
    private void updateEnemyPosition(int selectedRow) {
        if (selectedRow == -1) return;
        Enemy enemy = (selectedRow == -1) ? null : battleManager.getBattle().getSpriteset().getEnemies()[selectedRow];
        if (enemy == null) return;
        jSpinner_X.setValue(enemy.getX());
        jSpinner_Y.setValue(enemy.getY());
    }
    
    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        drawSprites = jCheckBox4.isSelected();
        battleLayoutPanel.setDrawSprites(drawSprites);
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        Path paletteEntriesPath = PathHelpers.getBasePath().resolve(fileButton1.getFilePath());
        Path tilesetEntriesPath = PathHelpers.getBasePath().resolve(fileButton2.getFilePath());
        Path mapEntriesPath = PathHelpers.getBasePath().resolve(fileButton3.getFilePath());
        int newMapIndex = (int)jSpinner1.getModel().getValue();
        try {
            MapLayout layout = battleManager.loadNewMap(paletteEntriesPath, tilesetEntriesPath, mapEntriesPath, newMapIndex);
            battleLayoutPanel.setMapLayout(layout);
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Map " + newMapIndex + " could not be imported");
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        int newVal = (int)jSpinner2.getModel().getValue();
        BattleMapCoords coords = battleManager.getBattleCoords();
        if (jCheckBox5.isSelected()) {
            int xShift = coords.getX() - newVal;
            battleManager.getBattle().getSpriteset().shiftPositions(xShift, 0);
        }
        coords.setX(newVal);
        battleLayoutPanel.redraw();
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        int newVal = (int)jSpinner3.getModel().getValue();
        BattleMapCoords coords = battleManager.getBattleCoords();
        if (jCheckBox5.isSelected()) {
            int yShift = coords.getY() - newVal;
            battleManager.getBattle().getSpriteset().shiftPositions(0, yShift);
        }
        coords.setY(newVal);
        battleLayoutPanel.redraw();
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jSpinner5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner5StateChanged
        battleLayoutPanel.getBattleCoords().setWidth((int)jSpinner5.getModel().getValue());
        battleLayoutPanel.redraw();
    }//GEN-LAST:event_jSpinner5StateChanged

    private void jSpinner6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner6StateChanged
        battleLayoutPanel.getBattleCoords().setHeight((int)jSpinner6.getModel().getValue());
        battleLayoutPanel.redraw();
    }//GEN-LAST:event_jSpinner6StateChanged

    private void jSpinner7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner7StateChanged
        int oldVal = battleLayoutPanel.getBattleCoords().getTrigX();
        int newVal = (int)jSpinner7.getModel().getValue();
        if (oldVal < newVal && newVal >= 64) {
            newVal = 255;
            jSpinner7.getModel().setValue(newVal);
        } else if (oldVal > newVal && newVal >= 64) {
            newVal = 63;
            jSpinner7.getModel().setValue(newVal);
        }
        battleLayoutPanel.getBattleCoords().setTrigX(newVal);
        battleLayoutPanel.redraw();
    }//GEN-LAST:event_jSpinner7StateChanged

    private void jSpinner8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner8StateChanged
        int oldVal = battleLayoutPanel.getBattleCoords().getTrigY();
        int newVal = (int)jSpinner8.getModel().getValue();
        if (oldVal < newVal && newVal >= 64) {
            newVal = 255;
            jSpinner8.getModel().setValue(newVal);
        } else if (oldVal > newVal && newVal >= 64) {
            newVal = 63;
            jSpinner8.getModel().setValue(newVal);
        }
        battleLayoutPanel.getBattleCoords().setTrigY(newVal);
        battleLayoutPanel.redraw();
    }//GEN-LAST:event_jSpinner8StateChanged

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        int index = jTabbedPane2.getSelectedIndex();
        battleLayoutPanel.setSelectedAlly(-1);
        switch (index) {
            case 0:
                battleLayoutPanel.setPaintMode(BattlePaintMode.Terrain);
                jCheckBox4.setEnabled(true);
                jCheckBox6.setEnabled(true);
                jCheckBox7.setEnabled(true);
                changeDrawMode(drawSprites, drawAiRegions, drawAiPoints);
                break;
            case 1:
                battleLayoutPanel.setPaintMode(BattlePaintMode.Spriteset);
                jTabbedPane3StateChanged(null);
                break;
        }
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jTabbedPane3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane3StateChanged
        int index = jTabbedPane3.getSelectedIndex();
        battleLayoutPanel.setSelectedAlly(-1);
        switch (index) {
            case 0:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.Ally);
                changeDrawMode(true, drawAiRegions, drawAiPoints);
                tableAllies.jTable.clearSelection();
                break;
            case 1:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.Enemy);
                changeDrawMode(true, drawAiRegions, drawAiPoints);
                tableEnemies.jTable.clearSelection();
                jCheckBox4.setSelected(true);
                break;
            case 2:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.AiRegion);
                changeDrawMode(drawSprites, true, drawAiPoints);
                tableAIRegions.jTable.clearSelection();
                jCheckBox6.setSelected(true);
                break;
            case 3:
                battleLayoutPanel.setSpritesetMode(SpritesetPaintMode.AiPoint);
                changeDrawMode(drawSprites, drawAiRegions, true);
                tableAIPoints.jTable.clearSelection();
                jCheckBox7.setSelected(true);
                break;
        }
        if (jTabbedPane2.getSelectedIndex() == 1) {
            jCheckBox4.setEnabled(battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.Ally && battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.Enemy);
            jCheckBox6.setEnabled(battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.AiRegion);
            jCheckBox7.setEnabled(battleLayoutPanel.getSpritesetMode() != SpritesetPaintMode.AiPoint);
        }
    }//GEN-LAST:event_jTabbedPane3StateChanged

    private void jSpinner_Trigger1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_Trigger1StateChanged
        OnEnemyDataChanged((int)jSpinner_Trigger1.getModel().getValue(), 9);
    }//GEN-LAST:event_jSpinner_Trigger1StateChanged

    private void jSpinner_YStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_YStateChanged
        OnEnemyDataChanged((int)jSpinner_Y.getModel().getValue(), 3);
    }//GEN-LAST:event_jSpinner_YStateChanged

    private void jSpinner_Trigger2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_Trigger2StateChanged
        OnEnemyDataChanged((int)jSpinner_Trigger2.getModel().getValue(), 10);
    }//GEN-LAST:event_jSpinner_Trigger2StateChanged

    private void jSpinner_OrderTarget1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_OrderTarget1StateChanged
        OnEnemyDataChanged((int)jSpinner_OrderTarget1.getModel().getValue(), 8);
    }//GEN-LAST:event_jSpinner_OrderTarget1StateChanged

    private void jSpinner_OrderTarget2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_OrderTarget2StateChanged
        OnEnemyDataChanged((int)jSpinner_OrderTarget2.getModel().getValue(), 12);
    }//GEN-LAST:event_jSpinner_OrderTarget2StateChanged

    private void jComboBox_NameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_NameItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 1);
    }//GEN-LAST:event_jComboBox_NameItemStateChanged

    private void jSpinner_XStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_XStateChanged
        OnEnemyDataChanged((int)jSpinner_X.getModel().getValue(), 2);
    }//GEN-LAST:event_jSpinner_XStateChanged

    private void jComboBox_AIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_AIItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 4);
    }//GEN-LAST:event_jComboBox_AIItemStateChanged

    private void jComboBox_SpawnItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_SpawnItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 14);
    }//GEN-LAST:event_jComboBox_SpawnItemStateChanged

    private void jComboBox_ItemsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_ItemsItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 5);
        multiComboBoxItemFlags.setEnabled(!jComboBox_Items.getSelectedItem().equals("NOTHING"));
    }//GEN-LAST:event_jComboBox_ItemsItemStateChanged

    private void jComboBox_Order1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_Order1ItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 7);
    }//GEN-LAST:event_jComboBox_Order1ItemStateChanged

    private void jComboBox_Order2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_Order2ItemStateChanged
        OnEnemyDataChanged((String)evt.getItem(), 11);
    }//GEN-LAST:event_jComboBox_Order2ItemStateChanged

    private void jSpinner_UnknownStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner_UnknownStateChanged
        OnEnemyDataChanged((int)jSpinner_Unknown.getModel().getValue(), 13);
    }//GEN-LAST:event_jSpinner_UnknownStateChanged

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        drawAiRegions = jCheckBox6.isSelected();
        battleLayoutPanel.setDrawAiRegions(drawAiRegions);
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed
        drawAiPoints = jCheckBox7.isSelected();
        battleLayoutPanel.setDrawAiPoints(drawAiPoints);
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Path coordsPath = PathHelpers.getBasePath().resolve(fileButton10.getFilePath());
        if (!PathHelpers.createPathIfRequred(coordsPath)) return;
        try {
            battleManager.exportBattleCoords(coordsPath, battleLayoutPanel.getBattleCoords());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle coords disasm could not be exported to : " + coordsPath);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void multiComboBoxItemFlagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_multiComboBoxItemFlagsActionPerformed
        OnEnemyDataChanged(multiComboBoxItemFlags.getObjectsString(), 6);
    }//GEN-LAST:event_multiComboBoxItemFlagsActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Path landEffectPath = PathHelpers.getBasePath().resolve(fileButton17.getFilePath());
        if (!PathHelpers.createPathIfRequred(landEffectPath)) return;
        try {
            battleManager.exportLandEffects(landEffectPath, landEffectTableModel.getTableData(LandEffectMovementType[].class));
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle coords disasm could not be exported to : " + landEffectPath);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void changeDrawMode(boolean sprites, boolean aiRegions, boolean aiModes) {
        jCheckBox4.setSelected(sprites);
        jCheckBox6.setSelected(aiRegions);
        jCheckBox7.setSelected(aiModes);
        battleLayoutPanel.setDrawSprites(sprites);
        battleLayoutPanel.setDrawAiRegions(aiRegions);
        battleLayoutPanel.setDrawAiPoints(aiModes);
    }
    
    private void OnEnemyDataChanged(Object data, int column) {
        int selectRow = tableEnemies.jTable.getSelectedRow();
        if (selectRow == -1)
            return;
        
        tableEnemies.jTable.setValueAt(data, selectRow, column);
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
            UpdateEnemyControls(row);
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
                    updateEnemyPosition(row);
                    break;
                case "AiRegion":
                    aIRegionPropertiesTableModel.fireTableRowsUpdated(row, row);
                    break;
                case "AiPoint":
                    aIPointPropertiesTableModel.fireTableRowsUpdated(row, row);
                    break;
            }
        }
    }
    
    private void onTerrainSelectionChanged(ActionEvent e) {
        int terrain = Integer.parseInt(e.getActionCommand());
        battleLayoutPanel.setSelectedTerrainType(terrain);
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
    private com.sfc.sf2.core.gui.controls.ColorPicker colorPicker1;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.battle.models.EnemyPropertiesTableModel enemyPropertiesTableModel;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton10;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton11;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton12;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton13;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton14;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton15;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton16;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton17;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton2;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton3;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton4;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton5;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton6;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton7;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton8;
    private com.sfc.sf2.core.gui.controls.FileButton fileButton9;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton10;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton11;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton2;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton3;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton4;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton5;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton6;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton7;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton8;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton9;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox_AI;
    private javax.swing.JComboBox<String> jComboBox_Items;
    private javax.swing.JComboBox<String> jComboBox_Name;
    private javax.swing.JComboBox<String> jComboBox_Order1;
    private javax.swing.JComboBox<String> jComboBox_Order2;
    private javax.swing.JComboBox<String> jComboBox_Spawn;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
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
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JSpinner jSpinner8;
    private javax.swing.JSpinner jSpinner_OrderTarget1;
    private javax.swing.JSpinner jSpinner_OrderTarget2;
    private javax.swing.JSpinner jSpinner_Trigger1;
    private javax.swing.JSpinner jSpinner_Trigger2;
    private javax.swing.JSpinner jSpinner_Unknown;
    private javax.swing.JSpinner jSpinner_X;
    private javax.swing.JSpinner jSpinner_Y;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private com.sfc.sf2.battle.mapterrain.gui.LandEffectTable landEffectTable;
    private com.sfc.sf2.battle.mapterrain.models.LandEffectTableModel landEffectTableModel;
    private com.sfc.sf2.core.gui.MultiComboBox multiComboBoxItemFlags;
    private com.sfc.sf2.core.gui.controls.Table tableAIPoints;
    private com.sfc.sf2.core.gui.controls.Table tableAIRegions;
    private com.sfc.sf2.core.gui.controls.Table tableAllies;
    private com.sfc.sf2.core.gui.controls.Table tableEnemies;
    private com.sfc.sf2.battle.mapterrain.gui.TerrainKeyPanel terrainKeyPanel1;
    // End of variables declaration//GEN-END:variables
}
