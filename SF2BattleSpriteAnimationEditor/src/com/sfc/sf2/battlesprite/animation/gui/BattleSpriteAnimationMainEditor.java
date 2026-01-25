/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation.gui;

import com.sfc.sf2.battlescene.BattleSceneAction;
import com.sfc.sf2.battlescene.actions.ActionBattleScene;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimation;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationFrame;
import com.sfc.sf2.battlesprite.animation.BattleSpriteAnimationManager;
import com.sfc.sf2.core.actions.Action;
import com.sfc.sf2.core.actions.ActionManager;
import com.sfc.sf2.core.actions.ComboAction;
import com.sfc.sf2.core.actions.SpinnerAction;
import com.sfc.sf2.core.actions.ToggleAction;
import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.layout.LayoutAnimator;
import com.sfc.sf2.core.gui.layout.LayoutAnimator.AnimationListener.AnimationFrameEvent;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationMainEditor extends AbstractMainEditor {
    
    BattleSpriteAnimationManager battlespriteanimationManager = new BattleSpriteAnimationManager();
        
    private int actionWeaponPalette = -1;
    private boolean isTableSelectionChanging = false;
    
    public BattleSpriteAnimationMainEditor() {
        super();
        initComponents();
        initCore(console1);
    }
    
    @Override
    protected void initEditor() {
        super.initEditor();
        
        viewPanel1.setLayoutPanel(battleSpriteAnimationLayoutPanel);
        
        accordionPanelEnvironment.setExpanded(false);
        accordionPanelWeapon.setExpanded(false);
        
        battleSpriteAnimationLayoutPanel.getAnimator().addAnimationListener(this::onAnimationFrameUpdated);
        tableFrames.addTableModelListener(this::onTableFrameDataChanged);
        tableFrames.addListSelectionListener(this::onTableFrameSelectionChanged);
        TableColumnModel columns = tableFrames.jTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(50);
    }
    
    @Override
    protected void onDataLoaded() {
        super.onDataLoaded();
        
        ActionManager.setAndExecuteAction(new Action<BattleSpriteAnimation>(this, "Animation Imported", this::actionAnimationLoaded, battlespriteanimationManager.getBattleSpriteAnimation(), battleSpriteAnimationLayoutPanel.getAnimation()));
    }
    
    private void actionAnimationLoaded(BattleSpriteAnimation animation) {
        actionBattleSceneLoaded(new ActionBattleScene(battlespriteanimationManager.getBackground(), battlespriteanimationManager.getGround()));
        actionWeaponLoaded(battlespriteanimationManager.getWeaponsprite());
        battleSpriteAnimationLayoutPanel.setAnimation(animation);
        
        jComboBoxSpritePalette.removeAllItems();
        if (animation == null) {
            battleSpriteAnimationLayoutPanel.setBattlesprite(null);
        } else {
            battleSpriteAnimationLayoutPanel.setBattlesprite(animation.getBattleSprite());
            Palette[] battleSpritePalettes = animation.getBattleSprite().getPalettes();
            for (int i=0; i < battleSpritePalettes.length; i++) {
                jComboBoxSpritePalette.addItem(battleSpritePalettes[i].getName());
            }
            jComboBoxSpritePalette.setSelectedIndex(0);
            battleSpriteAnimationFramesModel.setTableData(animation.getFrames());
            int maxFrames = animation.getFrames().length-1;
            ((SpinnerNumberModel)jSpinnerAnimFrame.getModel()).setMaximum(maxFrames);
            jSpinnerSpellFrame.setValue(animation.getSpellInitFrame());
            jSpinnerSpellAnim.setValue(animation.getSpellAnim());
            jCheckBoxEndSpell.setSelected(animation.getEndSpellAnim());
            jSpinnerAnimFrame.setValue(0);
        }
    }
    
    protected void onBattleSceneDataLoaded() {
        super.onDataLoaded();
        
        ActionBattleScene newValue = new ActionBattleScene(battlespriteanimationManager.getBackground(), battlespriteanimationManager.getGround());
        ActionBattleScene oldValue = new ActionBattleScene(battleSpriteAnimationLayoutPanel.getBg(), battleSpriteAnimationLayoutPanel.getGround());
        ActionManager.setAndExecuteAction(new BattleSceneAction(this, "Battle Scene Imported", this::actionBattleSceneLoaded, newValue, oldValue));
    }
    
    private void actionBattleSceneLoaded(ActionBattleScene battleScene) {
        battleSpriteAnimationLayoutPanel.setBg(battleScene.background());
        battleSpriteAnimationLayoutPanel.setGround(battleScene.ground());
    }
    
    protected void onWeaponDataLoaded() {
        super.onDataLoaded();
        
        ActionManager.setAndExecuteAction(new Action<WeaponSprite>(this, "Weapon Imported", this::actionWeaponLoaded, battlespriteanimationManager.getWeaponsprite(), battleSpriteAnimationLayoutPanel.getWeaponsprite()));
    }
    
    private void actionWeaponLoaded(WeaponSprite weaponSprite) {
        battleSpriteAnimationLayoutPanel.setWeaponsprite(weaponSprite);
        battleSpriteAnimationLayoutPanel.setHideWeapon(jCheckBoxHideWeapon.isSelected());
        
        jComboBoxWeaponPalette.removeAllItems();
        if (weaponSprite != null) {
            Palette[] weaponPalettes = battlespriteanimationManager.getWeaponPalettes();
            for (int i=0; i < weaponPalettes.length; i++) {
                jComboBoxWeaponPalette.addItem(weaponPalettes[i].getName());
            }
            jComboBoxWeaponPalette.setSelectedIndex(0);
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

        battleSpriteAnimationFramesModel = new com.sfc.sf2.battlesprite.animation.models.BattleSpriteAnimationFramesTableModel();
        jPanel13 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        accordionPanelEnvironment = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonBackground = new com.sfc.sf2.core.gui.controls.FileButton();
        jLabel23 = new javax.swing.JLabel();
        fileButtonGroundBasePalette = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonGroundPalette = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonGround = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonImportBattleScene = new javax.swing.JButton();
        accordionPanelWeapon = new com.sfc.sf2.core.gui.controls.AccordionPanel();
        fileButtonWeaponPalettes = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonWeapon = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonImportWeapon = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        fileButtonBattleSprite = new com.sfc.sf2.core.gui.controls.FileButton();
        fileButtonAnimation = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonImportAnimation = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        infoButton4 = new com.sfc.sf2.core.gui.controls.InfoButton();
        fileButtonExportAnimation = new com.sfc.sf2.core.gui.controls.FileButton();
        jButtonExportAnimation = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        battleSpriteAnimationLayoutPanel = new com.sfc.sf2.battlesprite.animation.gui.BattleSpriteAnimationLayoutPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxHideWeapon = new javax.swing.JCheckBox();
        jComboBoxWeaponPalette = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxSpritePalette = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        infoButton5 = new com.sfc.sf2.core.gui.controls.InfoButton();
        jPanel14 = new javax.swing.JPanel();
        jButtonPlayAnim = new javax.swing.JButton();
        jCheckBoxIdle = new javax.swing.JCheckBox();
        jSpinnerAnimFrame = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        infoButton6 = new com.sfc.sf2.core.gui.controls.InfoButton();
        viewPanel1 = new com.sfc.sf2.battlesprite.animation.gui.BattleSpriteAnimationViewPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerSpellFrame = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jSpinnerSpellAnim = new javax.swing.JSpinner();
        jCheckBoxEndSpell = new javax.swing.JCheckBox();
        infoButton1 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton2 = new com.sfc.sf2.core.gui.controls.InfoButton();
        infoButton3 = new com.sfc.sf2.core.gui.controls.InfoButton();
        tableFrames = new com.sfc.sf2.core.gui.controls.Table();
        console1 = new com.sfc.sf2.core.gui.controls.Console();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2BattleSpriteAnimationEditor");

        jSplitPane1.setDividerLocation(700);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(400);
        jSplitPane2.setOneTouchExpandable(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from :"));
        jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

        accordionPanelEnvironment.setBorder(javax.swing.BorderFactory.createTitledBorder("Battle scene"));

        fileButtonBackground.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonBackground.setFilePath("..\\backgrounds\\background09.bin");
        fileButtonBackground.setInfoMessage("Loads a Background, for the animation preview.");
        fileButtonBackground.setLabelText("Background :");
        fileButtonBackground.setName("Import Background"); // NOI18N

        jLabel23.setText("Ground :");

        fileButtonGroundBasePalette.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonGroundBasePalette.setFilePath("..\\battlescenebasepalette.bin");
        fileButtonGroundBasePalette.setInfoMessage("The battle base palette to use for the Ground platform preview.");
        fileButtonGroundBasePalette.setLabelText("Ground base palette :");
        fileButtonGroundBasePalette.setName("Import Ground Base Palette"); // NOI18N

        fileButtonGroundPalette.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonGroundPalette.setFilePath("..\\grounds\\groundpalette09.bin");
        fileButtonGroundPalette.setInfoMessage("The palette to use for the Ground platform preview.");
        fileButtonGroundPalette.setLabelText("Gound palette :");
        fileButtonGroundPalette.setName("Import Ground Palette"); // NOI18N

        fileButtonGround.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonGround.setFilePath("..\\grounds\\groundtiles09.bin");
        fileButtonGround.setInfoMessage("Loads a Ground platform, for the animation preview.");
        fileButtonGround.setLabelText("Ground :");
        fileButtonGround.setName("Import Ground"); // NOI18N

        jButtonImportBattleScene.setText("Import");
        jButtonImportBattleScene.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportBattleSceneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout accordionPanelEnvironmentLayout = new javax.swing.GroupLayout(accordionPanelEnvironment);
        accordionPanelEnvironment.setLayout(accordionPanelEnvironmentLayout);
        accordionPanelEnvironmentLayout.setHorizontalGroup(
            accordionPanelEnvironmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelEnvironmentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanelEnvironmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonBackground, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(fileButtonGroundBasePalette, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(fileButtonGroundPalette, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(accordionPanelEnvironmentLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(fileButtonGround, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accordionPanelEnvironmentLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonImportBattleScene)))
                .addContainerGap())
        );
        accordionPanelEnvironmentLayout.setVerticalGroup(
            accordionPanelEnvironmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelEnvironmentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonBackground, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonGroundBasePalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonGroundPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonGround, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonImportBattleScene)
                .addContainerGap())
        );

        accordionPanelWeapon.setBorder(javax.swing.BorderFactory.createTitledBorder("Weapon"));

        fileButtonWeaponPalettes.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.ASM);
        fileButtonWeaponPalettes.setFilePath("..\\weapons\\palettes\\entries.asm");
        fileButtonWeaponPalettes.setInfoMessage("The entries file for weapon palettes.");
        fileButtonWeaponPalettes.setLabelText("Weapon palettes :");
        fileButtonWeaponPalettes.setName("Import Weapon Palettes"); // NOI18N

        fileButtonWeapon.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonWeapon.setFilePath("..\\weapons\\weaponsprite04.bin");
        fileButtonWeapon.setInfoMessage("<html>Loads a placeholder weapon for the animation preview.<br>The weapon can be hidden with the checbox on the right of the animation window.</html>");
        fileButtonWeapon.setLabelText("Weapon :");
        fileButtonWeapon.setName("Import Weapon"); // NOI18N

        jButtonImportWeapon.setText("Import");
        jButtonImportWeapon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportWeaponActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout accordionPanelWeaponLayout = new javax.swing.GroupLayout(accordionPanelWeapon);
        accordionPanelWeapon.setLayout(accordionPanelWeaponLayout);
        accordionPanelWeaponLayout.setHorizontalGroup(
            accordionPanelWeaponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelWeaponLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accordionPanelWeaponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonWeaponPalettes, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(fileButtonWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accordionPanelWeaponLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonImportWeapon)))
                .addContainerGap())
        );
        accordionPanelWeaponLayout.setVerticalGroup(
            accordionPanelWeaponLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accordionPanelWeaponLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileButtonWeaponPalettes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonImportWeapon)
                .addContainerGap())
        );

        jLabel2.setText("Import animation disassembly.");

        fileButtonBattleSprite.setFilePath(".\\allies\\allybattlesprite00.bin");
        fileButtonBattleSprite.setInfoMessage("<html>Loads a battlesprite to animate. In general, each battesprite is matched to specific animation files (see the info button for \"Battle Sprite Animation\").</html>");
        fileButtonBattleSprite.setLabelText("Battle sprite :");
        fileButtonBattleSprite.setName("Import Battle Sprite"); // NOI18N

        fileButtonAnimation.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonAnimation.setFilePath(".\\allies\\animations\\allyanimation000.bin");
        fileButtonAnimation.setInfoMessage("<html>Loads the animation data. By default, ally animations 0-39 are <i>attack</> animations, 40-79 are <i>dodge</> animations, 80+ for <i>special</> animations.<br>See <b>SF2Enums</b> \"; enum AllyBattleAnimations\".<br><br>By default, enemy animations 0-59 are <i>attack</> animations, 60-117 are <i>dodge</> animations, 118+ for <i>special</> animations.<br>See <b>SF2Enums</b> \"; enum EnemyBattleAnimations\".</html>");
        fileButtonAnimation.setLabelText("Battle sprite animation :");
        fileButtonAnimation.setName("Import Animation"); // NOI18N

        jButtonImportAnimation.setText("Import");
        jButtonImportAnimation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportAnimationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonImportAnimation))
                    .addComponent(fileButtonBattleSprite, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                    .addComponent(accordionPanelWeapon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accordionPanelEnvironment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fileButtonAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accordionPanelEnvironment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accordionPanelWeapon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonBattleSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonImportAnimation)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Export to :"));
        jPanel5.setPreferredSize(new java.awt.Dimension(32, 135));

        jLabel1.setText("Export animation disassembly.");

        infoButton4.setMessageText("<html>Exports the animation in format \"allyanimationXXX.bin\" or \"enemyanimationXXX.bin.<br>By default, ally animations 0-39 are <i>attack</> animations, 40-79 are <i>dodge</> animations, 80+ for <i>special</> animations.<br>See <b>SF2Enums</b> \"; enum AllyBattleAnimations\".<br><br>By default, enemy animations 0-59 are <i>attack</> animations, 60-117 are <i>dodge</> animations, 118+ for <i>special</> animations.<br>See <b>SF2Enums</b> \"; enum EnemyBattleAnimations\".</html>");
        infoButton4.setText("");

        fileButtonExportAnimation.setFileFormatFilter(com.sfc.sf2.core.io.FileFormat.BIN);
        fileButtonExportAnimation.setFilePath(".\\allies\\animations\\newallyanimation000.bin");
        fileButtonExportAnimation.setInfoMessage("");
        fileButtonExportAnimation.setLabelText("Animation :");
        fileButtonExportAnimation.setName("Export Animation"); // NOI18N

        jButtonExportAnimation.setText("Export");
        jButtonExportAnimation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportAnimationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileButtonExportAnimation, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExportAnimation))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileButtonExportAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportAnimation)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSplitPane2.setLeftComponent(jPanel8);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Animation"));
        jPanel1.setMinimumSize(new java.awt.Dimension(400, 400));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(592, 400));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout battleSpriteAnimationLayoutPanelLayout = new javax.swing.GroupLayout(battleSpriteAnimationLayoutPanel);
        battleSpriteAnimationLayoutPanel.setLayout(battleSpriteAnimationLayoutPanelLayout);
        battleSpriteAnimationLayoutPanelLayout.setHorizontalGroup(
            battleSpriteAnimationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
        );
        battleSpriteAnimationLayoutPanelLayout.setVerticalGroup(
            battleSpriteAnimationLayoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(battleSpriteAnimationLayoutPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Sprites"));

        jCheckBoxHideWeapon.setText("Hide weapon");
        jCheckBoxHideWeapon.setName("Hide Weapon Toggle"); // NOI18N
        jCheckBoxHideWeapon.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxHideWeaponItemStateChanged(evt);
            }
        });

        jComboBoxWeaponPalette.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0" }));
        jComboBoxWeaponPalette.setName("Weapon Palette Combo"); // NOI18N
        jComboBoxWeaponPalette.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxWeaponPaletteActionPerformed(evt);
            }
        });

        jLabel6.setText("<html>Weapon<br>Palette : </html>");

        jComboBoxSpritePalette.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0" }));
        jComboBoxSpritePalette.setName("Battle Sprite Palette Combo"); // NOI18N
        jComboBoxSpritePalette.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSpritePaletteActionPerformed(evt);
            }
        });

        jLabel5.setText("<html>Battle Sprite<br>Palette : </html>");

        infoButton5.setMessageText("Whether or not to show a weapon in the animation preview.");
        infoButton5.setText("");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBoxWeaponPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBoxSpritePalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jCheckBoxHideWeapon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxSpritePalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxWeaponPalette, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jCheckBoxHideWeapon)
                    .addComponent(infoButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Animation"));

        jButtonPlayAnim.setText("Play");
        jButtonPlayAnim.setName(""); // NOI18N
        jButtonPlayAnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayAnimActionPerformed(evt);
            }
        });

        jCheckBoxIdle.setText("Idle");
        jCheckBoxIdle.setName("Idle Animation Toggle"); // NOI18N
        jCheckBoxIdle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxIdleItemStateChanged(evt);
            }
        });

        jSpinnerAnimFrame.setModel(new javax.swing.SpinnerNumberModel(0, 0, 16, 1));
        jSpinnerAnimFrame.setName("Anim Frame Spinner"); // NOI18N
        jSpinnerAnimFrame.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerAnimFrameStateChanged(evt);
            }
        });

        jLabel8.setText("Anim frame :");

        infoButton6.setMessageText("<html>Alternates between animation frames 0 & 1.<br>NOTE: Some characters do not have an idle animation.<br>NOTE: Idle animation speed is defined by the battlesprite (edit in BattleSpriteManager).</html>");
        infoButton6.setText("");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jCheckBoxIdle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonPlayAnim))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSpinnerAnimFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerAnimFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButtonPlayAnim)
                    .addComponent(infoButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxIdle))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(viewPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(viewPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Animation spells"));

        jLabel3.setText("Spell initial frame :");

        jSpinnerSpellFrame.setModel(new javax.swing.SpinnerNumberModel(Byte.valueOf((byte)0), Byte.valueOf((byte)0), Byte.valueOf((byte)20), Byte.valueOf((byte)1)));
        jSpinnerSpellFrame.setName("Spell Initial Frame Spinner"); // NOI18N
        jSpinnerSpellFrame.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerSpellFrameStateChanged(evt);
            }
        });

        jLabel4.setText("Spell anim :");

        jSpinnerSpellAnim.setModel(new javax.swing.SpinnerNumberModel(Byte.valueOf((byte)-1), Byte.valueOf((byte)-1), Byte.valueOf((byte)127), Byte.valueOf((byte)1)));
        jSpinnerSpellAnim.setName("Spell Anim Spinner"); // NOI18N
        jSpinnerSpellAnim.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerSpellAnimStateChanged(evt);
            }
        });

        jCheckBoxEndSpell.setText("End spell");
        jCheckBoxEndSpell.setName("End Spell Toggle"); // NOI18N
        jCheckBoxEndSpell.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxEndSpellItemStateChanged(evt);
            }
        });

        infoButton1.setMessageText("<html>When a spell (or spell-like ability is cast) the <b>Spell initial frame</b> indicates the animation frame when the spell is triggered.</html>");
        infoButton1.setText("");

        infoButton2.setMessageText("<html>A normal attack can trigger a specific <b>Spell anim</b> to trigger on the <b>Spell initial frame</b>.<br>Values are linked to SF2Enums \"enum SpellAnimations\".</html>");
        infoButton2.setText("");

        infoButton3.setMessageText("<html>Whether or not to terminate spell animation when battle sprite anim finishes.</html>");
        infoButton3.setText("");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerSpellFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerSpellAnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxEndSpell)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(infoButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinnerSpellFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jSpinnerSpellAnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxEndSpell)
                    .addComponent(infoButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        tableFrames.setBorder(javax.swing.BorderFactory.createTitledBorder("Animation frames"));
        tableFrames.setInfoMessage("<html><b>Frame:</b> The frame number (cannot be edited).<br><b>Battlesprite: </b> The battlesprite to show during this frame.<br><b>Duration:</b> The speed of the animation. 60 = 1 second. Lower numbers animate faster.<br><b>X/Y:</b> The X and Y position offset when rendering the battle sprite.<br><b>Weapon Index:</b> The index of the weapon sprite frame to render.<br><b>H/V Flip:</b> Whether or not the weapon sprite is flipped Horizontally or Vertically.<br><b>Behnd:</b> Whether the weapon is rendered in front of or behind the batttle sprite.<br><b>Weapon X/Y:</b> The X and Y position offset when rendering the weapon.<br><br><b>Note:</b> For frame 0; Battlesprite, Duration, X, and Y cannot be edited.</html>");
        tableFrames.setModel(battleSpriteAnimationFramesModel);
        tableFrames.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableFrames.setSingleClickText(false);
        tableFrames.setSpinnerNumberEditor(true);
        tableFrames.setMinimumSize(new java.awt.Dimension(260, 150));
        tableFrames.setPreferredSize(new java.awt.Dimension(260, 200));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tableFrames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tableFrames, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
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
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE)
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

        setSize(new java.awt.Dimension(1198, 864));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExportAnimationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportAnimationActionPerformed
        Path spritePath = PathHelpers.getBasePath().resolve(fileButtonExportAnimation.getFilePath());
        if (!PathHelpers.createPathIfRequred(spritePath)) return;
        try {
            battlespriteanimationManager.exportDisassembly(spritePath, battleSpriteAnimationLayoutPanel.getAnimation());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Animation disasm could not be exported to : " + spritePath);
        }
    }//GEN-LAST:event_jButtonExportAnimationActionPerformed

    private void jButtonImportAnimationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportAnimationActionPerformed
        Path bgPath = PathHelpers.getBasePath().resolve(fileButtonBackground.getFilePath());
        Path groundBasePalettePath = PathHelpers.getBasePath().resolve(fileButtonGroundBasePalette.getFilePath());
        Path groundPalettePath = PathHelpers.getBasePath().resolve(fileButtonGroundPalette.getFilePath());
        Path groundPath = PathHelpers.getBasePath().resolve(fileButtonGround.getFilePath());
        Path weaponPalettesPath = PathHelpers.getBasePath().resolve(fileButtonWeaponPalettes.getFilePath());
        Path weaponPath = PathHelpers.getBasePath().resolve(fileButtonWeapon.getFilePath());
        Path battleSpritePath = PathHelpers.getBasePath().resolve(fileButtonBattleSprite.getFilePath());
        Path animationPath = PathHelpers.getBasePath().resolve(fileButtonAnimation.getFilePath());
        try {
            battlespriteanimationManager.importDisassembly(bgPath, groundBasePalettePath, groundPalettePath, groundPath, battleSpritePath, weaponPalettesPath, weaponPath, animationPath);
        } catch (Exception ex) {
            battlespriteanimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Animation disasm could not be imported from : " + animationPath);
        }
        onDataLoaded();
    }//GEN-LAST:event_jButtonImportAnimationActionPerformed

    private void jComboBoxSpritePaletteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSpritePaletteActionPerformed
        if (jComboBoxSpritePalette.getSelectedIndex() < 0 || battleSpriteAnimationLayoutPanel.getAnimation() == null) return;
        BattleSprite battlesprite = battleSpriteAnimationLayoutPanel.getAnimation().getBattleSprite();
        if (battlesprite == null) return;
        if (!ActionManager.isActionTriggering()) {
            int oldValue = battlesprite.getCurrentPaletteIndex();
            ActionManager.setActionWithoutExecute(new ComboAction(jComboBoxSpritePalette, jComboBoxSpritePalette.getSelectedIndex(), oldValue));
        }
        battlesprite.setCurrentPaletteIndex(jComboBoxSpritePalette.getSelectedIndex());
        battleSpriteAnimationLayoutPanel.setBattlesprite(battlesprite);
    }//GEN-LAST:event_jComboBoxSpritePaletteActionPerformed

    private void jComboBoxWeaponPaletteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxWeaponPaletteActionPerformed
        if (jComboBoxSpritePalette.getSelectedIndex() < 0 || battleSpriteAnimationLayoutPanel.getWeaponsprite() == null) return;
        WeaponSprite weaponsprite = battleSpriteAnimationLayoutPanel.getWeaponsprite();
        if (weaponsprite == null) return;
        if (!ActionManager.isActionTriggering()) {
            int oldValue = actionWeaponPalette;
            ActionManager.setActionWithoutExecute(new ComboAction(jComboBoxWeaponPalette, jComboBoxWeaponPalette.getSelectedIndex(), oldValue));
        }
        actionWeaponPalette = jComboBoxWeaponPalette.getSelectedIndex();
        Palette[] palettes = battlespriteanimationManager.getWeaponPalettes();
        weaponsprite.setPalette(palettes[actionWeaponPalette < 0 ? 0 : actionWeaponPalette]);
        battleSpriteAnimationLayoutPanel.redraw();
    }//GEN-LAST:event_jComboBoxWeaponPaletteActionPerformed

    private void jSpinnerAnimFrameStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerAnimFrameStateChanged
        LayoutAnimator animator = battleSpriteAnimationLayoutPanel.getAnimator();
        if (!battleSpriteAnimationLayoutPanel.hasData() || animator.isAnimating()) return;
        if (!isTableSelectionChanging && !ActionManager.isActionTriggering()) {
            int oldValue = animator.getFrame();
            ActionManager.setActionWithoutExecute(new SpinnerAction(jSpinnerAnimFrame, jSpinnerAnimFrame.getValue(), oldValue));
        }
        int frame = (int)jSpinnerAnimFrame.getModel().getValue();
        if (frame >= 0) {
            animator.setFrame(frame);
            tableFrames.jTable.setRowSelectionInterval(frame, frame);
        }
    }//GEN-LAST:event_jSpinnerAnimFrameStateChanged
    
    private void jButtonPlayAnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayAnimActionPerformed
        jCheckBoxIdle.setSelected(false);
        if (battleSpriteAnimationLayoutPanel.hasData()) {
            int speed = battleSpriteAnimationLayoutPanel.getAnimation().getFrames()[0].getDuration();
            int frames = battleSpriteAnimationLayoutPanel.getAnimation().getFrameCount()-1;
            battleSpriteAnimationLayoutPanel.getAnimator().startAnimation(speed, frames, false, true);
        }
    }//GEN-LAST:event_jButtonPlayAnimActionPerformed

    private void jSpinnerSpellFrameStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerSpellFrameStateChanged
        if (!battleSpriteAnimationLayoutPanel.hasData()) return;
        BattleSpriteAnimation animation = battleSpriteAnimationLayoutPanel.getAnimation();
        if (!ActionManager.isActionTriggering()) {
            byte oldValue = animation.getSpellInitFrame();
            ActionManager.setActionWithoutExecute(new SpinnerAction(jSpinnerSpellFrame, jSpinnerSpellFrame.getValue(), oldValue));
        }
        animation.setSpellInitFrame((byte)jSpinnerSpellFrame.getValue());
    }//GEN-LAST:event_jSpinnerSpellFrameStateChanged

    private void jSpinnerSpellAnimStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerSpellAnimStateChanged
        if (!battleSpriteAnimationLayoutPanel.hasData()) return;
        BattleSpriteAnimation animation = battleSpriteAnimationLayoutPanel.getAnimation();
        if (!ActionManager.isActionTriggering()) {
            byte oldValue = animation.getSpellAnim();
            ActionManager.setActionWithoutExecute(new SpinnerAction(jSpinnerSpellAnim, jSpinnerSpellAnim.getValue(), oldValue));
        }
        animation.setSpellAnim((byte)jSpinnerSpellAnim.getValue());
    }//GEN-LAST:event_jSpinnerSpellAnimStateChanged

    private void jCheckBoxEndSpellItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxEndSpellItemStateChanged
        if (!battleSpriteAnimationLayoutPanel.hasData()) return;
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxEndSpell, jCheckBoxEndSpell.isSelected()));
        }
        battleSpriteAnimationLayoutPanel.getAnimation().setEndSpellAnim(jCheckBoxEndSpell.isSelected());
    }//GEN-LAST:event_jCheckBoxEndSpellItemStateChanged

    private void jCheckBoxHideWeaponItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxHideWeaponItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxHideWeapon, jCheckBoxHideWeapon.isSelected()));
        }
        battleSpriteAnimationLayoutPanel.setHideWeapon(jCheckBoxHideWeapon.isSelected());
    }//GEN-LAST:event_jCheckBoxHideWeaponItemStateChanged

    private void jCheckBoxIdleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxIdleItemStateChanged
        if (!ActionManager.isActionTriggering()) {
            ActionManager.setActionWithoutExecute(new ToggleAction(jCheckBoxIdle, jCheckBoxIdle.isSelected()));
        }
        BattleSprite battleSprite = battleSpriteAnimationLayoutPanel.getAnimation().getBattleSprite();
        if (battleSprite != null && battleSpriteAnimationLayoutPanel.hasData()) {
            if (jCheckBoxIdle.isSelected()) {
                battleSpriteAnimationLayoutPanel.getAnimator().startAnimation(battleSprite.getAnimSpeed(), 1, true, false);
            } else {
                battleSpriteAnimationLayoutPanel.getAnimator().stopAnimation();
            }
        }
    }//GEN-LAST:event_jCheckBoxIdleItemStateChanged

    private void jButtonImportWeaponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportWeaponActionPerformed
        Path weaponPalettesPath = PathHelpers.getBasePath().resolve(fileButtonWeaponPalettes.getFilePath());
        Path weaponPath = PathHelpers.getBasePath().resolve(fileButtonWeapon.getFilePath());
        try {
            battlespriteanimationManager.importWeapon(weaponPalettesPath, weaponPath);
        } catch (Exception ex) {
            battlespriteanimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Weapon disasm could not be imported from : " + weaponPath);
        }
        onWeaponDataLoaded();
    }//GEN-LAST:event_jButtonImportWeaponActionPerformed

    private void jButtonImportBattleSceneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportBattleSceneActionPerformed
        Path bgPath = PathHelpers.getBasePath().resolve(fileButtonBackground.getFilePath());
        Path groundBasePalettePath = PathHelpers.getBasePath().resolve(fileButtonGroundBasePalette.getFilePath());
        Path groundPalettePath = PathHelpers.getBasePath().resolve(fileButtonGroundPalette.getFilePath());
        Path groundPath = PathHelpers.getBasePath().resolve(fileButtonGround.getFilePath());
        try {
            battlespriteanimationManager.importBattleScene(bgPath, groundBasePalettePath, groundPalettePath, groundPath);
        } catch (Exception ex) {
            battlespriteanimationManager.clearData();
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Battle Scene disasm could not be imported from : " + bgPath);
        }
        onBattleSceneDataLoaded();
    }//GEN-LAST:event_jButtonImportBattleSceneActionPerformed

    private void onAnimationFrameUpdated(AnimationFrameEvent e) {
        isTableSelectionChanging = true;
        jSpinnerAnimFrame.setValue(e.getCurrentFrame());
        tableFrames.jTable.setRowSelectionInterval(e.getCurrentFrame(), e.getCurrentFrame());
        isTableSelectionChanging = false;
    }
    
    private void onTableFrameSelectionChanged(ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting() || evt.getSource() == null) return;
        int selection = ((ListSelectionModel)evt.getSource()).getAnchorSelectionIndex();
        if (selection != (int)jSpinnerAnimFrame.getValue()) {
            isTableSelectionChanging = true;
            jSpinnerAnimFrame.setValue(selection);
            isTableSelectionChanging = false;
        }
    }
    
    private void onTableFrameDataChanged(TableModelEvent evt) {
        int row = tableFrames.jTable.getSelectedRow();
        if (row >= evt.getFirstRow() && row <= evt.getLastRow()) {
            battleSpriteAnimationLayoutPanel.redraw();
        }
        BattleSpriteAnimation animation = battleSpriteAnimationLayoutPanel.getAnimation();
        if (evt.getType() == TableModelEvent.INSERT || evt.getType() == TableModelEvent.DELETE) {
            animation.setFrames(battleSpriteAnimationFramesModel.getTableData(BattleSpriteAnimationFrame[].class));
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
                new BattleSpriteAnimationMainEditor().setVisible(true);  // <------ Change this class to new Main Editor class
            }
        });
    }
    /**
     * To create a new Main Editor, copy the above code
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanelEnvironment;
    private com.sfc.sf2.core.gui.controls.AccordionPanel accordionPanelWeapon;
    private com.sfc.sf2.battlesprite.animation.models.BattleSpriteAnimationFramesTableModel battleSpriteAnimationFramesModel;
    private com.sfc.sf2.battlesprite.animation.gui.BattleSpriteAnimationLayoutPanel battleSpriteAnimationLayoutPanel;
    private com.sfc.sf2.core.gui.controls.Console console1;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonAnimation;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonBackground;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonBattleSprite;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonExportAnimation;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonGround;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonGroundBasePalette;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonGroundPalette;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonWeapon;
    private com.sfc.sf2.core.gui.controls.FileButton fileButtonWeaponPalettes;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton1;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton2;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton3;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton4;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton5;
    private com.sfc.sf2.core.gui.controls.InfoButton infoButton6;
    private javax.swing.JButton jButtonExportAnimation;
    private javax.swing.JButton jButtonImportAnimation;
    private javax.swing.JButton jButtonImportBattleScene;
    private javax.swing.JButton jButtonImportWeapon;
    private javax.swing.JButton jButtonPlayAnim;
    private javax.swing.JCheckBox jCheckBoxEndSpell;
    private javax.swing.JCheckBox jCheckBoxHideWeapon;
    private javax.swing.JCheckBox jCheckBoxIdle;
    private javax.swing.JComboBox<String> jComboBoxSpritePalette;
    private javax.swing.JComboBox<String> jComboBoxWeaponPalette;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinnerAnimFrame;
    private javax.swing.JSpinner jSpinnerSpellAnim;
    private javax.swing.JSpinner jSpinnerSpellFrame;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private com.sfc.sf2.core.gui.controls.Table tableFrames;
    private com.sfc.sf2.battlesprite.animation.gui.BattleSpriteAnimationViewPanel viewPanel1;
    // End of variables declaration//GEN-END:variables
}
