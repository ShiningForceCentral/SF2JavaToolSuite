/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.map.gui;

import com.sfc.sf2.graphics.layout.DefaultLayout;
import com.sfc.sf2.map.Map;
import com.sfc.sf2.map.block.gui.BlockSlotPanel;
import com.sfc.sf2.map.block.layout.MapBlockLayout;
import com.sfc.sf2.map.MapManager;
import java.awt.GridLayout;
import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author wiz
 */
public class MainEditor extends javax.swing.JFrame {
    
    MapManager mapManager = new MapManager();
    MapPanel mapPanel = null;
    MapBlockLayout baseBlocksLayout = null;
    DefaultLayout baseTilesLayout = null;
    DefaultLayout tileset1Layout = null;
    DefaultLayout tileset2Layout = null;
    DefaultLayout tileset3Layout = null;
    DefaultLayout tileset4Layout = null;
    DefaultLayout tileset5Layout = null;
    DefaultLayout newtileset1Layout = null;
    DefaultLayout newtileset2Layout = null;
    DefaultLayout newtileset3Layout = null;
    DefaultLayout newtileset4Layout = null;
    DefaultLayout newtileset5Layout = null;
    DefaultLayout orphanTilesLayout = null;
    MapBlockLayout optimizedBlocksLayout = null;
    
    /**
     * Creates new form NewApplication
     */
    public MainEditor() {
        try {
            initComponents();
            initConsole(jTextArea1);
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%2$s - %5$s%6$s%n");
            initLogger("com.sfc.sf2.graphics", Level.WARNING);
            File workingDirectory = new File(MainEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            System.setProperty("user.dir", workingDirectory.toString());
            jFileChooser1.setCurrentDirectory(workingDirectory);
            jFileChooser2.setCurrentDirectory(workingDirectory);
            jSpinner2.setValue(16);
            jComboBox3.setSelectedIndex(1);
            jTextArea2.setCaretPosition(0);
            
            //Base tilesets
            jPanel30.removeAll();       
            jPanel30.setLayout(new GridLayout(1,1));
            tileset1Layout = new DefaultLayout();
            jPanel30.add(tileset1Layout);
            jPanel33.removeAll();       
            jPanel33.setLayout(new GridLayout(1,1));
            tileset2Layout = new DefaultLayout();
            jPanel33.add(tileset2Layout);
            jPanel36.removeAll();       
            jPanel36.setLayout(new GridLayout(1,1));
            tileset3Layout = new DefaultLayout();
            jPanel36.add(tileset3Layout);
            jPanel37.removeAll();       
            jPanel37.setLayout(new GridLayout(1,1));
            tileset4Layout = new DefaultLayout();
            jPanel37.add(tileset4Layout);
            jPanel38.removeAll();       
            jPanel38.setLayout(new GridLayout(1,1));
            tileset5Layout = new DefaultLayout();
            jPanel38.add(tileset5Layout);
            
            //New tilesets
            jPanel39.removeAll();       
            jPanel39.setLayout(new GridLayout(1,1));
            newtileset1Layout = new DefaultLayout();
            jPanel39.add(newtileset1Layout);
            jPanel40.removeAll();       
            jPanel40.setLayout(new GridLayout(1,1));
            newtileset2Layout = new DefaultLayout();
            jPanel40.add(newtileset2Layout);
            jPanel41.removeAll();       
            jPanel41.setLayout(new GridLayout(1,1));
            newtileset3Layout = new DefaultLayout();
            jPanel41.add(newtileset3Layout);
            jPanel42.removeAll();       
            jPanel42.setLayout(new GridLayout(1,1));
            newtileset4Layout = new DefaultLayout();
            jPanel42.add(newtileset4Layout);
            jPanel43.removeAll();       
            jPanel43.setLayout(new GridLayout(1,1));
            newtileset5Layout = new DefaultLayout();
            jPanel43.add(newtileset5Layout);

            //Blocksets & orphaned tiles
            jPanel45.removeAll();       
            jPanel45.setLayout(new GridLayout(1,1));
            optimizedBlocksLayout = new MapBlockLayout();
            jPanel45.add(optimizedBlocksLayout);
            jPanel27.removeAll();       
            jPanel27.setLayout(new GridLayout(1,1));
            orphanTilesLayout = new DefaultLayout();
            jPanel27.add(orphanTilesLayout);
            
            //Misc
            jPanel23.removeAll();
            jPanel23.setLayout(new GridLayout(1,1));
            baseTilesLayout = new DefaultLayout();
            jPanel23.add(baseTilesLayout);
            
            jPanel6.removeAll();
            jPanel6.setLayout(new GridLayout(1,1));
            baseBlocksLayout = new MapBlockLayout();
            jPanel6.add(baseBlocksLayout);
            
            jPanel2.removeAll();
            jPanel2.setLayout(new GridLayout(1,1));
            mapPanel = new MapPanel();
            jPanel2.add(mapPanel);
            
            BlockSlotPanel leftSlotBlockPanel = new BlockSlotPanel();
            BlockSlotPanel rightSlotBlockPanel = new BlockSlotPanel();
            baseBlocksLayout.setLeftSlotBlockPanel(leftSlotBlockPanel);
            baseBlocksLayout.setRightSlotBlockPanel(rightSlotBlockPanel);
        } catch (URISyntaxException ex) {
            Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initLogger(String name, Level level){
        Logger log = Logger.getLogger(name);
        log.setUseParentHandlers(false);
        log.setLevel(level);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(level);        
        log.addHandler(ch);                

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
        jLabel26 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jButton31 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jButton30 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jButton39 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jButton41 = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jButton18 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jButton26 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jButton28 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jButton40 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel51 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jButton44 = new javax.swing.JButton();
        jTextField37 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jTextField43 = new javax.swing.JTextField();
        jButton50 = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        jTextField44 = new javax.swing.JTextField();
        jButton51 = new javax.swing.JButton();
        jPanel52 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jButton42 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jButton46 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jButton47 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jButton48 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jTextField41 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel48 = new javax.swing.JLabel();
        jTextField42 = new javax.swing.JTextField();
        jButton49 = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jPanel37 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jPanel38 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jSplitPane6 = new javax.swing.JSplitPane();
        jPanel18 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel34 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel39 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jPanel40 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jPanel41 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jPanel43 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jTextField17 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton24 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jTextField25 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jButton33 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jTextField14 = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jButton27 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jButton43 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jFileChooser2.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2MapCreator");

        jSplitPane1.setDividerLocation(750);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        jSplitPane2.setDividerLocation(750);
        jSplitPane2.setOneTouchExpandable(true);

        jLabel26.setText("Base dir :");

        jTextField24.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jTextField24.setText(".\\entries\\map03\\");

            jButton31.setText("File...");
            jButton31.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton31ActionPerformed(evt);
                }
            });

            jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

            jSplitPane4.setDividerLocation(400);
            jSplitPane4.setOneTouchExpandable(true);

            jPanel3.setPreferredSize(new java.awt.Dimension(590, 135));

            jLabel52.setText("<html>Step 1: Import map image and metadata.</html>");
            jLabel52.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder("GIF Import : "));

            jButton19.setText("Import");
            jButton19.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton19ActionPerformed(evt);
                }
            });

            jLabel25.setText("GIF :");

            jTextField22.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField22.setText("maplayout.gif");

            jButton30.setText("File...");
            jButton30.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton30ActionPerformed(evt);
                }
            });

            jLabel34.setText("HP Tiles : ");

            jTextField23.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField23.setText("maphptiles.txt");

            jButton39.setText("File...");
            jButton39.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton39ActionPerformed(evt);
                }
            });

            jLabel37.setText("Layout flags :");

            jTextField34.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField34.setText("maplayoutflags.txt");

            jButton41.setText("File...");
            jButton41.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton41ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
            jPanel29.setLayout(jPanel29Layout);
            jPanel29Layout.setHorizontalGroup(
                jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField22))
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addComponent(jLabel34)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField23))
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addComponent(jLabel37)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField34)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel29Layout.setVerticalGroup(
                jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel29Layout.createSequentialGroup()
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25)
                        .addComponent(jButton30))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37)
                        .addComponent(jButton41))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)
                        .addComponent(jButton39))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton19)
                    .addGap(0, 6, Short.MAX_VALUE))
            );

            jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder("PNG Import : "));

            jButton18.setText("Import");
            jButton18.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton18ActionPerformed(evt);
                }
            });

            jLabel21.setText("PNG :");

            jTextField20.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField20.setText("maplayout.png");

            jButton26.setText("File...");
            jButton26.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton26ActionPerformed(evt);
                }
            });

            jLabel23.setText("HP Tiles : ");

            jTextField21.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField21.setText("maphptiles.txt");

            jButton28.setText("File...");
            jButton28.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton28ActionPerformed(evt);
                }
            });

            jLabel36.setText("Layout flags : ");

            jTextField33.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField33.setText("maplayoutflags.txt");

            jButton40.setText("File...");
            jButton40.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton40ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
            jPanel31.setLayout(jPanel31Layout);
            jPanel31Layout.setHorizontalGroup(
                jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel31Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField20))
                        .addGroup(jPanel31Layout.createSequentialGroup()
                            .addComponent(jLabel23)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField21))
                        .addGroup(jPanel31Layout.createSequentialGroup()
                            .addComponent(jLabel36)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField33, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel31Layout.setVerticalGroup(
                jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel31Layout.createSequentialGroup()
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21)
                        .addComponent(jButton26))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36)
                        .addComponent(jButton40))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)
                        .addComponent(jButton28))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton18)
                    .addContainerGap())
            );

            jLabel51.setText("<html>Import step generates basic 64*64 block array and 192*192 tile array with no optimization at this point.<br/><br/>Image requirements :\n<br/>- Dimensions : 1536px*1536px<br/>- 16-color indexed (4bpp)<br/>256-indexed tolerated if only first 16 colors are used.\n<br/>- Transparency color at index 0\n<br><br>\nBreware :<br/All used colors from the palette (transparency color included !) should have distinct values, to avoid color index ambiguity.<br/><br/>\n\nOptional :\nLayout flags : indicates flags on each tiles<br>\nHP Tiles : Indicates which tiles are rendered above the player character (i.e. roofs).<br>\n*These can be generated from an existing map with SF2MapEditor.</html>");
            jLabel51.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
            );

            jSplitPane4.setLeftComponent(jPanel3);

            jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
            jPanel6.setLayout(jPanel6Layout);
            jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane3.setViewportView(jPanel6);

            jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));

            jLabel5.setText("Blocks :");

            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
            jComboBox2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox2ActionPerformed(evt);
                }
            });

            jLabel6.setText("Blocks per row :");

            jSpinner1.setModel(new javax.swing.SpinnerNumberModel(10, 0, 1024, 1));
            jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    jSpinner1StateChanged(evt);
                }
            });

            javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
            jPanel14.setLayout(jPanel14Layout);
            jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
            );

            javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
            jPanel21.setLayout(jPanel21Layout);
            jPanel21Layout.setHorizontalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addComponent(jScrollPane3)
                    .addContainerGap())
            );
            jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            jTabbedPane3.addTab("Blocks", jPanel21);

            jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
            jPanel23.setLayout(jPanel23Layout);
            jPanel23Layout.setHorizontalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane5.setViewportView(jPanel23);

            jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));

            jLabel9.setText("Size :");

            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
            jComboBox3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3ActionPerformed(evt);
                }
            });

            jLabel10.setText("Tiles per row :");

            jSpinner2.setModel(new javax.swing.SpinnerNumberModel(10, 0, 1024, 1));
            jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    jSpinner2StateChanged(evt);
                }
            });

            javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
            jPanel24.setLayout(jPanel24Layout);
            jPanel24Layout.setHorizontalGroup(
                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel24Layout.createSequentialGroup()
                    .addComponent(jLabel9)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanel24Layout.setVerticalGroup(
                jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
            );

            javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
            jPanel19.setLayout(jPanel19Layout);
            jPanel19Layout.setHorizontalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane5)
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanel19Layout.setVerticalGroup(
                jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            jTabbedPane3.addTab("Tiles", jPanel19);

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane3)
            );
            jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane3)
            );

            jSplitPane4.setRightComponent(jPanel4);

            jTabbedPane1.addTab("1. Import map", jSplitPane4);

            jSplitPane5.setDividerLocation(400);

            jLabel47.setText("<html>Load existing tilesets, or keep free tileset slots to fill with new tiles.<br/>Managed constraints : <br/>- Empty tile at tileset 0 index 0<br/>- Open/closed chest tiles expected at fixed position in last tileset<br/><br/>To manage manually :<br/>Map animation tiles have to be grouped in a pre-created tileset for the map animation data to point to it. (eg : for map 03, first 32 tiles of tileset 066).</html>");
            jLabel47.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jPanel50.setBorder(javax.swing.BorderFactory.createTitledBorder("Import from map :"));

            jButton44.setText("File...");
            jButton44.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton44ActionPerformed(evt);
                }
            });

            jTextField37.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField37.setText("00-tilesets.asm");
            jTextField37.setMinimumSize(new java.awt.Dimension(30, 26));

            jLabel40.setText("Tileset data :");

            jButton8.setText("Import");
            jButton8.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton8ActionPerformed(evt);
                }
            });

            jLabel41.setText("Import palette and tilesets from map");

            jLabel49.setText("Palettes path :");

            jTextField43.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField43.setText("../../../graphics/maps/mappalettes/mappalette");
            jTextField43.setMinimumSize(new java.awt.Dimension(30, 26));
            jTextField43.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField43ActionPerformed(evt);
                }
            });

            jButton50.setText("File...");
            jButton50.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton50ActionPerformed(evt);
                }
            });

            jLabel50.setText("Tilesets path :");

            jTextField44.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField44.setText("../../../graphics/maps/maptilesets/maptileset");
            jTextField44.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton51.setText("File...");
            jButton51.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton51ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
            jPanel50.setLayout(jPanel50Layout);
            jPanel50Layout.setHorizontalGroup(
                jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel50Layout.createSequentialGroup()
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel50Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel40)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(6, 6, 6)
                                    .addComponent(jButton44))
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel49)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGap(6, 6, 6)
                                    .addComponent(jButton50))
                                .addGroup(jPanel50Layout.createSequentialGroup()
                                    .addComponent(jLabel50)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGap(6, 6, 6)
                                    .addComponent(jButton51))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                            .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton8)))
                    .addContainerGap())
            );
            jPanel50Layout.setVerticalGroup(
                jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel50Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel40)
                        .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton44))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel49)
                        .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton50))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel50)
                        .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton51))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jButton8)
                        .addComponent(jLabel41))
                    .addContainerGap())
            );

            jPanel52.setBorder(javax.swing.BorderFactory.createTitledBorder("Import custom :"));
            jPanel52.setPreferredSize(new java.awt.Dimension(32, 135));

            jButton9.setText("Import");
            jButton9.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton9ActionPerformed(evt);
                }
            });

            jLabel42.setText("Base Tileset 1 :");

            jTextField35.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField35.setText("../../../graphics/maps/maptilesets/maptileset001.bin");

            jButton42.setText("File...");
            jButton42.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton42ActionPerformed(evt);
                }
            });

            jButton45.setText("File...");
            jButton45.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton45ActionPerformed(evt);
                }
            });

            jLabel43.setText("Base Tileset 2 :");

            jTextField38.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField38.setText("../../../graphics/maps/maptilesets/maptileset002.bin");

            jButton46.setText("File...");
            jButton46.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton46ActionPerformed(evt);
                }
            });

            jLabel44.setText("Base Tileset 3 :");

            jTextField39.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField39.setText("../../../graphics/maps/maptilesets/maptileset003.bin");

            jButton47.setText("File...");
            jButton47.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton47ActionPerformed(evt);
                }
            });

            jLabel45.setText("Base Tileset 4 :");

            jTextField40.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField40.setText("../../../graphics/maps/maptilesets/maptileset004.bin");

            jButton48.setText("File...");
            jButton48.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton48ActionPerformed(evt);
                }
            });

            jLabel46.setText("Base Tileset 5 :");

            jTextField41.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField41.setText("../../../graphics/maps/maptilesets/maptileset005.bin");

            jCheckBox3.setSelected(true);
            jCheckBox3.setText("Chest graphics");

            jLabel48.setText("Palette :");

            jTextField42.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField42.setText("../../../graphics/maps/mappalettes/mappalette00.bin");

            jButton49.setText("File...");
            jButton49.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton49ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
            jPanel52.setLayout(jPanel52Layout);
            jPanel52Layout.setHorizontalGroup(
                jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel52Layout.createSequentialGroup()
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel52Layout.createSequentialGroup()
                            .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel52Layout.createSequentialGroup()
                                    .addComponent(jLabel46)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addGroup(jPanel52Layout.createSequentialGroup()
                                    .addComponent(jCheckBox3)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(jPanel52Layout.createSequentialGroup()
                                    .addComponent(jLabel43)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addGroup(jPanel52Layout.createSequentialGroup()
                                    .addComponent(jLabel42)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addGroup(jPanel52Layout.createSequentialGroup()
                                    .addComponent(jLabel45)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                .addGroup(jPanel52Layout.createSequentialGroup()
                                    .addComponent(jLabel44)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                            .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel52Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jButton9))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton48, javax.swing.GroupLayout.Alignment.TRAILING)))))
                        .addGroup(jPanel52Layout.createSequentialGroup()
                            .addComponent(jLabel48)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton49)))
                    .addContainerGap())
            );
            jPanel52Layout.setVerticalGroup(
                jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel52Layout.createSequentialGroup()
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel48)
                        .addComponent(jButton49))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel42)
                        .addComponent(jButton42))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel43)
                        .addComponent(jButton45))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44)
                        .addComponent(jButton46))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel45)
                        .addComponent(jButton47))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel46)
                        .addComponent(jButton48))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton9)
                        .addComponent(jCheckBox3))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jLabel53.setText("<html>Step 2 : Load tilesets from mapdata or individually.</html>");
            jLabel53.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
            jPanel51.setLayout(jPanel51Layout);
            jPanel51Layout.setHorizontalGroup(
                jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel51Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel53)
                        .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jPanel52, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                        .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel51Layout.setVerticalGroup(
                jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel51Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel53)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel52, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jSplitPane5.setLeftComponent(jPanel51);

            jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
            jPanel30.setLayout(jPanel30Layout);
            jPanel30Layout.setHorizontalGroup(
                jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel30Layout.setVerticalGroup(
                jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane7.setViewportView(jPanel30);

            jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            jPanel33.setPreferredSize(new java.awt.Dimension(276, 130));

            javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
            jPanel33.setLayout(jPanel33Layout);
            jPanel33Layout.setHorizontalGroup(
                jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 319, Short.MAX_VALUE)
            );
            jPanel33Layout.setVerticalGroup(
                jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 130, Short.MAX_VALUE)
            );

            jScrollPane8.setViewportView(jPanel33);

            jScrollPane11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane11.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
            jPanel36.setLayout(jPanel36Layout);
            jPanel36Layout.setHorizontalGroup(
                jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel36Layout.setVerticalGroup(
                jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane11.setViewportView(jPanel36);

            jScrollPane12.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane12.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
            jPanel37.setLayout(jPanel37Layout);
            jPanel37Layout.setHorizontalGroup(
                jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel37Layout.setVerticalGroup(
                jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane12.setViewportView(jPanel37);

            jScrollPane13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane13.setToolTipText("");
            jScrollPane13.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
            jPanel38.setLayout(jPanel38Layout);
            jPanel38Layout.setHorizontalGroup(
                jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel38Layout.setVerticalGroup(
                jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane13.setViewportView(jPanel38);

            javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
            jPanel25.setLayout(jPanel25Layout);
            jPanel25Layout.setHorizontalGroup(
                jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane7)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane11)
                .addComponent(jScrollPane12)
                .addComponent(jScrollPane13)
            );
            jPanel25Layout.setVerticalGroup(
                jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            jSplitPane5.setRightComponent(jPanel25);

            jTabbedPane1.addTab("2. Import tilesets", jSplitPane5);

            jLabel7.setText("<html>Step 3 (Optional) : Edit map.<br><br>\n\nImported map layout flags not editable here.<br/>Use SF2MapEditor on exported data.<br/><br/>Tile priority edit :<br/>Use the \"Tile Priority\" display checkbox<br/>to edit tile display priorities.<br><br>\nTODO: More edit options</html>");

            jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Export"));

            jLabel24.setText("HP Tiles :");

            jTextField19.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField19.setText("maphptiles.txt");

            jButton29.setText("File...");
            jButton29.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton29ActionPerformed(evt);
                }
            });

            jLabel3.setText("<html>Save current state of tile priority map.</html>");

            jButton3.setText("Export");
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
            jPanel17.setLayout(jPanel17Layout);
            jPanel17Layout.setHorizontalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                            .addComponent(jLabel24)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField19, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE))
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(12, 12, 12)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanel17Layout.setVerticalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel17Layout.createSequentialGroup()
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24)
                        .addComponent(jButton29))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
            jPanel16.setLayout(jPanel16Layout);
            jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7))
                    .addContainerGap())
            );
            jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel16Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(473, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("3. Edit", jPanel16);

            jSplitPane6.setDividerLocation(300);

            jLabel54.setText("<html>Step 4 : Generate new blocksets and tilesets.</html>");
            jLabel54.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jButton5.setText("Generate Data");
            jButton5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton5ActionPerformed(evt);
                }
            });

            jLabel38.setText("<html>*Loads base tilesets declared in step 2, if not already done.<br><br>\n- Orphaned Tiles : Any tiles that do not fit in the original tilesets.<br>Use the checkbox to see where the orphaned tiles are on the map.<br><br>\n- Optimized Blockset : Identifies all unique blocks on the map in order of appearance (i.e. duplicates are combined).<br><br>\n- New Tileset : Any orphaned tiles will be inserted into blank tiles spaces in the new tilesets.<br>Empty tilesets will be filled to create new tilesets.</html>");
            jLabel38.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
            jPanel18.setLayout(jPanel18Layout);
            jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel54)
                        .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                        .addGroup(jPanel18Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addContainerGap())
            );

            jSplitPane6.setLeftComponent(jPanel18);

            jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
            jPanel27.setLayout(jPanel27Layout);
            jPanel27Layout.setHorizontalGroup(
                jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 577, Short.MAX_VALUE)
            );
            jPanel27Layout.setVerticalGroup(
                jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 609, Short.MAX_VALUE)
            );

            jScrollPane6.setViewportView(jPanel27);

            jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));

            jLabel11.setText("Size :");

            jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
            jComboBox4.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox4ActionPerformed(evt);
                }
            });

            jLabel12.setText("Tiles per row :");

            jSpinner3.setModel(new javax.swing.SpinnerNumberModel(10, 0, 1024, 1));
            jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    jSpinner3StateChanged(evt);
                }
            });

            jCheckBox5.setText("Show orphaned tiles on map");
            jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox5ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
            jPanel28.setLayout(jPanel28Layout);
            jPanel28Layout.setHorizontalGroup(
                jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel28Layout.createSequentialGroup()
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addComponent(jCheckBox5)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanel28Layout.setVerticalGroup(
                jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel28Layout.createSequentialGroup()
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jCheckBox5)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
            jPanel26.setLayout(jPanel26Layout);
            jPanel26Layout.setHorizontalGroup(
                jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanel26Layout.setVerticalGroup(
                jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane2.addTab("Orphan Tiles", jPanel26);

            jScrollPane17.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane17.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
            jPanel45.setLayout(jPanel45Layout);
            jPanel45Layout.setHorizontalGroup(
                jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel45Layout.setVerticalGroup(
                jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane17.setViewportView(jPanel45);

            jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder("Actions"));

            jLabel14.setText("Blocks :");

            jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
            jComboBox5.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox5ActionPerformed(evt);
                }
            });

            jLabel15.setText("Blocks per row :");

            jSpinner4.setModel(new javax.swing.SpinnerNumberModel(10, 0, 1024, 1));
            jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    jSpinner4StateChanged(evt);
                }
            });

            javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
            jPanel46.setLayout(jPanel46Layout);
            jPanel46Layout.setHorizontalGroup(
                jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel46Layout.createSequentialGroup()
                    .addComponent(jLabel14)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanel46Layout.setVerticalGroup(
                jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
            );

            javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
            jPanel44.setLayout(jPanel44Layout);
            jPanel44Layout.setHorizontalGroup(
                jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane17)
            );
            jPanel44Layout.setVerticalGroup(
                jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel44Layout.createSequentialGroup()
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
            jPanel34.setLayout(jPanel34Layout);
            jPanel34Layout.setHorizontalGroup(
                jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 425, Short.MAX_VALUE)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel34Layout.setVerticalGroup(
                jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 717, Short.MAX_VALUE)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane2.addTab("Optimized Blockset", jPanel34);

            jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
            jPanel39.setLayout(jPanel39Layout);
            jPanel39Layout.setHorizontalGroup(
                jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel39Layout.setVerticalGroup(
                jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane9.setViewportView(jPanel39);

            jScrollPane10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane10.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            jPanel40.setPreferredSize(new java.awt.Dimension(276, 130));

            javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
            jPanel40.setLayout(jPanel40Layout);
            jPanel40Layout.setHorizontalGroup(
                jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 419, Short.MAX_VALUE)
            );
            jPanel40Layout.setVerticalGroup(
                jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 130, Short.MAX_VALUE)
            );

            jScrollPane10.setViewportView(jPanel40);

            jScrollPane14.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane14.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
            jPanel41.setLayout(jPanel41Layout);
            jPanel41Layout.setHorizontalGroup(
                jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel41Layout.setVerticalGroup(
                jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane14.setViewportView(jPanel41);

            jScrollPane15.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane15.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
            jPanel42.setLayout(jPanel42Layout);
            jPanel42Layout.setHorizontalGroup(
                jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel42Layout.setVerticalGroup(
                jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane15.setViewportView(jPanel42);

            jScrollPane16.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane16.setToolTipText("");
            jScrollPane16.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

            javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
            jPanel43.setLayout(jPanel43Layout);
            jPanel43Layout.setHorizontalGroup(
                jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );
            jPanel43Layout.setVerticalGroup(
                jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 0, Short.MAX_VALUE)
            );

            jScrollPane16.setViewportView(jPanel43);

            javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
            jPanel35.setLayout(jPanel35Layout);
            jPanel35Layout.setHorizontalGroup(
                jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane9)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane14)
                .addComponent(jScrollPane15)
                .addComponent(jScrollPane16)
            );
            jPanel35Layout.setVerticalGroup(
                jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            jTabbedPane2.addTab("New Tilesets", jPanel35);

            javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
            jPanel11.setLayout(jPanel11Layout);
            jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 425, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2))
            );
            jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 752, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING))
            );

            jSplitPane6.setRightComponent(jPanel11);

            jTabbedPane1.addTab("4. Generate", jSplitPane6);

            jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Export to :"));
            jPanel5.setPreferredSize(new java.awt.Dimension(32, 135));

            jPanel47.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette :"));

            jLabel20.setText("Palette :");

            jTextField32.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField32.setText("../../../graphics/maps/mappalettes/mappalette16.bin");
            jTextField32.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton25.setText("File...");
            jButton25.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton25ActionPerformed(evt);
                }
            });

            jButton6.setText("Export");
            jButton6.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton6ActionPerformed(evt);
                }
            });

            jLabel2.setText("<html>Export current palette.</html>");

            javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
            jPanel47.setLayout(jPanel47Layout);
            jPanel47Layout.setHorizontalGroup(
                jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel47Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel47Layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField32, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                            .addGap(6, 6, 6)
                            .addComponent(jButton25))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton6)))
                    .addContainerGap())
            );
            jPanel47Layout.setVerticalGroup(
                jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel47Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jButton25)
                        .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder("Tilesets :"));

            jLabel17.setText("Tileset1 :");

            jTextField16.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField16.setText("../../../graphics/maps/maptilesets/maptileset115.bin");
            jTextField16.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton22.setText("File...");
            jButton22.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton22ActionPerformed(evt);
                }
            });

            jButton23.setText("File...");
            jButton23.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton23ActionPerformed(evt);
                }
            });

            jTextField17.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField17.setText("../../../graphics/maps/maptilesets/maptileset116.bin");
            jTextField17.setMinimumSize(new java.awt.Dimension(30, 26));

            jLabel18.setText("Tileset2 :");

            jLabel19.setText("Tileset3 :");

            jTextField18.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField18.setText("../../../graphics/maps/maptilesets/maptileset117.bin");
            jTextField18.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton24.setText("File...");
            jButton24.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton24ActionPerformed(evt);
                }
            });

            jButton32.setText("File...");
            jButton32.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton32ActionPerformed(evt);
                }
            });

            jTextField25.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField25.setText("../../../graphics/maps/maptilesets/maptileset118.bin");
            jTextField25.setMinimumSize(new java.awt.Dimension(30, 26));

            jLabel27.setText("Tileset4 :");

            jLabel28.setText("Tileset5 :");

            jTextField26.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField26.setText("../../../graphics/maps/maptilesets/maptileset119.bin");
            jTextField26.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton33.setText("File...");
            jButton33.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton33ActionPerformed(evt);
                }
            });

            jButton7.setText("Export");
            jButton7.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });

            jLabel8.setText("<html>Export new tilesets.<br>\n*Leave a slot blank to have it ignored.</html>");
            jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
            jPanel48.setLayout(jPanel48Layout);
            jPanel48Layout.setHorizontalGroup(
                jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel48Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel48Layout.createSequentialGroup()
                            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel48Layout.createSequentialGroup()
                                    .addComponent(jLabel17)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGroup(jPanel48Layout.createSequentialGroup()
                                    .addComponent(jLabel18)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGroup(jPanel48Layout.createSequentialGroup()
                                    .addComponent(jLabel19)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGroup(jPanel48Layout.createSequentialGroup()
                                    .addComponent(jLabel27)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGroup(jPanel48Layout.createSequentialGroup()
                                    .addComponent(jLabel28)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                            .addGap(6, 6, 6)
                            .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton22, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton23, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton24, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton32, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton33, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGroup(jPanel48Layout.createSequentialGroup()
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton7)))
                    .addContainerGap())
            );
            jPanel48Layout.setVerticalGroup(
                jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel48Layout.createSequentialGroup()
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel17)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton22))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel18)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton23))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jButton24)
                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel27)
                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton32))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel28)
                        .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton33))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel48Layout.createSequentialGroup()
                            .addComponent(jButton7)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addContainerGap())
            );

            jPanel49.setBorder(javax.swing.BorderFactory.createTitledBorder("Map data :"));

            jTextField14.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField14.setText("0-blocks.bin");
            jTextField14.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton21.setText("File...");
            jButton21.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton21ActionPerformed(evt);
                }
            });

            jLabel1.setText("<html>Select new target files.<br>\n*Uses the tileset filenames above as fallback, if loaded tilesets are not named.</html>");
            jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            jButton2.setText("Export");
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jLabel22.setText("Layout :");

            jTextField15.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField15.setText("1-layout.bin");
            jTextField15.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton27.setText("File...");
            jButton27.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton27ActionPerformed(evt);
                }
            });

            jLabel16.setText("Blocks :");

            jLabel39.setText("Tileset data :");

            jTextField36.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
            jTextField36.setText("00-tilesets.asm");
            jTextField36.setMinimumSize(new java.awt.Dimension(30, 26));

            jButton43.setText("File...");
            jButton43.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton43ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
            jPanel49.setLayout(jPanel49Layout);
            jPanel49Layout.setHorizontalGroup(
                jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel49Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel49Layout.createSequentialGroup()
                            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel49Layout.createSequentialGroup()
                                    .addComponent(jLabel22)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGroup(jPanel49Layout.createSequentialGroup()
                                    .addComponent(jLabel16)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel49Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton27, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButton21, javax.swing.GroupLayout.Alignment.TRAILING)))))
                        .addGroup(jPanel49Layout.createSequentialGroup()
                            .addComponent(jLabel39)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField36, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                            .addGap(6, 6, 6)
                            .addComponent(jButton43)))
                    .addContainerGap())
            );
            jPanel49Layout.setVerticalGroup(
                jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel39)
                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton43))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel16)
                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton21))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel22)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton27))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton2)
                        .addComponent(jLabel1))
                    .addGap(15, 15, 15))
            );

            jLabel55.setText("<html>Step 5 : Export map data.</html>");
            jLabel55.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel55)
                    .addContainerGap())
            );
            jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(173, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("5. Export", jPanel5);

            javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
            jPanel9.setLayout(jPanel9Layout);
            jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(jLabel26)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField24)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0))))
            );
            jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26)
                        .addComponent(jButton31))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTabbedPane1)
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
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jSplitPane2.setLeftComponent(jPanel8);

            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Map"));

            jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 815, Short.MAX_VALUE)
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 975, Short.MAX_VALUE)
            );

            jScrollPane2.setViewportView(jPanel2);

            jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Display"));

            jLabel4.setText("Map :");

            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x1", "x2", "x3", "x4" }));
            jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox1ActionPerformed(evt);
                }
            });

            jCheckBox2.setText("Grid");
            jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox2ActionPerformed(evt);
                }
            });

            jCheckBox1.setText("Tile Priority");
            jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox1ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
            jPanel12.setLayout(jPanel12Layout);
            jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addComponent(jLabel4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jCheckBox2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jCheckBox1)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox1))
            );

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
            jPanel10.setLayout(jPanel10Layout);
            jPanel10Layout.setHorizontalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(4, 4, 4))
            );
            jPanel10Layout.setVerticalGroup(
                jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

            jSplitPane3.setDividerLocation(500);
            jSplitPane3.setOneTouchExpandable(true);

            jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Help"));

            jScrollPane4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

            jTextArea2.setEditable(false);
            jTextArea2.setColumns(20);
            jTextArea2.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 13)); // NOI18N
            jTextArea2.setLineWrap(true);
            jTextArea2.setRows(5);
            jTextArea2.setAutoscrolls(false);
            jScrollPane4.setViewportView(jTextArea2);

            javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
            jPanel20.setLayout(jPanel20Layout);
            jPanel20Layout.setHorizontalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
            );
            jPanel20Layout.setVerticalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
            );

            jSplitPane3.setLeftComponent(jPanel20);

            jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Console"));
            jPanel7.setToolTipText("");

            jTextArea1.setEditable(false);
            jTextArea1.setColumns(20);
            jTextArea1.setRows(5);
            jScrollPane1.setViewportView(jTextArea1);

            javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
            jPanel7.setLayout(jPanel7Layout);
            jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1)
                    .addContainerGap())
            );
            jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
            );

            jSplitPane3.setRightComponent(jPanel7);

            jSplitPane1.setBottomComponent(jSplitPane3);

            javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
            jPanel13.setLayout(jPanel13Layout);
            jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1)
            );
            jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
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

            setSize(new java.awt.Dimension(1241, 877));
            setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        System.out.println(basePath.toString());
        
        Path tPath = Paths.get(jTextField36.getText());
        Path tilesetsPath;
        if(!tPath.isAbsolute()){
           tilesetsPath = basePath.resolve(tPath).normalize();
        }else{
            tilesetsPath = tPath;
        }
        Path bPath = Paths.get(jTextField14.getText());
        Path blocksetPath;
        if(!bPath.isAbsolute()){
           blocksetPath = basePath.resolve(bPath).normalize();
        }else{
            blocksetPath = bPath;
        }
        System.out.println(blocksetPath.toString());
        Path lPath = Paths.get(jTextField15.getText());
        Path layoutPath;
        if(!lPath.isAbsolute()){
           layoutPath = basePath.resolve(lPath).normalize();
        }else{
            layoutPath = lPath;
        }        
        System.out.println(layoutPath.toString());
        mapManager.exportDisassembly(tilesetsPath.toString(), blocksetPath.toString(), layoutPath.toString());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();

        Path iPath = Paths.get(jTextField20.getText());
        Path imagePath;
        if(!iPath.isAbsolute()){
           imagePath = basePath.resolve(iPath).normalize();
        }else{
            imagePath = iPath;
        }        
        System.out.println(imagePath.toString());
        Path fPath = Paths.get(jTextField33.getText());
        Path flagsPath;
        if(!fPath.isAbsolute()){
           flagsPath = basePath.resolve(fPath).normalize();
        }else{
            flagsPath = fPath;
        }        
        System.out.println(flagsPath.toString());
        Path hPath = Paths.get(jTextField21.getText());
        Path hptilesPath;
        if(!hPath.isAbsolute()){
           hptilesPath = basePath.resolve(hPath).normalize();
        }else{
            hptilesPath = hPath;
        }        
        System.out.println(hptilesPath.toString());

        mapManager.importPng(imagePath.toString(),flagsPath.toString(),hptilesPath.toString());
        UpdateMiscInterfaces();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField14.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if(jComboBox1.getSelectedIndex()>=0 && mapPanel!=null){
            mapPanel.setCurrentDisplaySize(jComboBox1.getSelectedIndex()+1);
            jPanel2.revalidate();
            jPanel2.repaint();  
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            /*Path basePath = Paths.get(jTextField24.getText()).toAbsolutePath();
            Path filePath = Paths.get(file.getAbsolutePath());
            Path relativePath = basePath.relativize(filePath);
            jTextField20.setText(relativePath.toString());*/
            jTextField20.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField15.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        if(jComboBox2.getSelectedIndex()>=0 && baseBlocksLayout!=null){
            baseBlocksLayout.setCurrentDisplaySize(jComboBox2.getSelectedIndex()+1);
            jPanel6.revalidate();
            jPanel6.repaint();  
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        if(baseBlocksLayout != null){
            baseBlocksLayout.setBlocksPerRow((int)jSpinner1.getModel().getValue());
            jPanel6.revalidate();
            jPanel6.repaint();
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        int returnVal = jFileChooser2.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser2.getSelectedFile();
            jTextField24.setText(file.getAbsolutePath()+System.getProperty("file.separator"));
        }
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if(mapPanel!=null){
            mapPanel.setDrawGrid(jCheckBox2.isSelected());
            jPanel2.revalidate();
            jPanel2.repaint();
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField16.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField17.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField18.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField25.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField26.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            /*Path basePath = Paths.get(jTextField24.getText()).toAbsolutePath();
            Path filePath = Paths.get(file.getAbsolutePath());
            Path relativePath = basePath.relativize(filePath);
            jTextField20.setText(relativePath.toString());*/
            jTextField21.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(mapPanel!=null){
            mapPanel.setDrawLPTiles(jCheckBox1.isSelected());
            jPanel2.revalidate();
            jPanel2.repaint();
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField19.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
                                       
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        System.out.println(basePath.toString());
        Path tPath = Paths.get(jTextField19.getText());
        Path phtilesPath;
        if(!tPath.isAbsolute()){
           phtilesPath = basePath.resolve(tPath).normalize();
        }else{
            phtilesPath = tPath;
        }
        System.out.println(phtilesPath.toString());
        
        mapManager.exportHPTiles(phtilesPath.toString());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        if(jComboBox3.getSelectedIndex()>=0 && baseTilesLayout!=null){
            baseTilesLayout.setDisplaySize(jComboBox3.getSelectedIndex()+1);
            jPanel23.revalidate();
            jPanel23.repaint();  
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        if(baseTilesLayout != null){
            baseTilesLayout.setTilesPerRow((int)jSpinner2.getModel().getValue());
            jPanel23.revalidate();
            jPanel23.repaint();
        }
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Map map = mapManager.getMap();
        if(map.getTilesets()==null || map.getTilesets()[0]==null){
            jButton9ActionPerformed(evt);
        }     
        
        mapManager.generateOptimisedBlockset();
        mapManager.generateOrphanedTiles();
        mapManager.generateTilesets();
        updateBlocksetInterface();
        updateOrphansInterface();
        UpdateNewTilesetsInterface();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();

        Path iPath = Paths.get(jTextField22.getText());
        Path imagePath;
        if(!iPath.isAbsolute()){
           imagePath = basePath.resolve(iPath).normalize();
        }else{
            imagePath = iPath;
        }        
        Path fPath = Paths.get(jTextField33.getText());
        Path flagsPath;
        if(!fPath.isAbsolute()){
           flagsPath = basePath.resolve(fPath).normalize();
        }else{
            flagsPath = fPath;
        }        
        System.out.println(flagsPath.toString());
        System.out.println(imagePath.toString());
        Path hPath = Paths.get(jTextField23.getText());
        Path hptilesPath;
        if(!hPath.isAbsolute()){
           hptilesPath = basePath.resolve(hPath).normalize();
        }else{
            hptilesPath = hPath;
        }        
        System.out.println(hptilesPath.toString());

        mapManager.importGif(imagePath.toString(),flagsPath.toString(),hptilesPath.toString());
        UpdateMiscInterfaces();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField22.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField23.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jSpinner4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner4StateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner4StateChanged

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField32.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField33.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField34.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        mapPanel.setDrawOrphanedTiles(jCheckBox5.isSelected());
        RepaintMiscPanels();
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        System.out.println(basePath.toString());
        
        Path pPath = Paths.get(jTextField32.getText());
        Path palettePath;
        if(!pPath.isAbsolute()){
           palettePath = basePath.resolve(pPath).normalize();
        }else{
            palettePath = pPath;
        }
        
        mapManager.exportPalette(palettePath.toString());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        System.out.println(basePath.toString());
        
        Path t1Path = Paths.get(jTextField16.getText());
        Path tileset1Path;
        if(!t1Path.isAbsolute()){
           tileset1Path = basePath.resolve(t1Path).normalize();
        }else{
            tileset1Path = t1Path;
        }
        Path t2Path = Paths.get(jTextField17.getText());
        Path tileset2Path;
        if(!t2Path.isAbsolute()){
           tileset2Path = basePath.resolve(t2Path).normalize();
        }else{
            tileset2Path = t2Path;
        }
        Path t3Path = Paths.get(jTextField18.getText());
        Path tileset3Path;
        if(!t3Path.isAbsolute()){
           tileset3Path = basePath.resolve(t3Path).normalize();
        }else{
            tileset3Path = t3Path;
        }
        Path t4Path = Paths.get(jTextField25.getText());
        Path tileset4Path;
        if(!t4Path.isAbsolute()){
           tileset4Path = basePath.resolve(t4Path).normalize();
        }else{
            tileset4Path = t4Path;
        }
        Path t5Path = Paths.get(jTextField26.getText());
        Path tileset5Path;
        if(!t5Path.isAbsolute()){
           tileset5Path = basePath.resolve(t5Path).normalize();
        }else{
            tileset5Path = t5Path;
        }
        
        mapManager.exportTilesets(new String[] {tileset1Path.toString(), tileset2Path.toString(), tileset3Path.toString(), tileset4Path.toString(), tileset5Path.toString()});
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField37.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        
        Path dPath = Paths.get(jTextField37.getText());
        Path mapDataPath;
        if(!dPath.isAbsolute()){
           mapDataPath = basePath.resolve(dPath).normalize();
        }else{
            mapDataPath = dPath;
        }        
        System.out.println(mapDataPath.toString());
        
        Path pPath = Paths.get(jTextField43.getText());
        Path palettesPath;
        if(!pPath.isAbsolute()){
           palettesPath = basePath.resolve(pPath).normalize();
        }else{
            palettesPath = pPath;
        }        
        System.out.println(palettesPath.toString());
        
        Path tPath = Paths.get(jTextField44.getText());
        Path tilesetsPath;
        if(!tPath.isAbsolute()){
           tilesetsPath = basePath.resolve(tPath).normalize();
        }else{
            tilesetsPath = tPath;
        }        
        System.out.println(tilesetsPath.toString());

        mapManager.importMapPaletteAndTilesets(palettesPath.toString(), tilesetsPath.toString(), mapDataPath.toString());
        UpdateBaseTilesetsInterface();
        UpdateMiscInterfaces();
        updateExportPaths();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        
        String toolDir = System.getProperty("user.dir");
        Path toolPath = Paths.get(toolDir);
        
        String mapPath = jTextField24.getText();
        if(!mapPath.endsWith("\\")){
            mapPath = mapPath+"\\";
        }
        //Path basePath = Paths.get(mapPath).toAbsolutePath();
        System.out.println(toolPath.toString());
        Path basePath = toolPath.resolve(Paths.get(mapPath)).normalize();
        
        Path pPath = Paths.get(jTextField42.getText());
        Path palettePath;
        if(!pPath.isAbsolute()){
           palettePath = basePath.resolve(pPath).normalize();
        }else{
            palettePath = pPath;
        }        
        System.out.println(palettePath.toString());

        Path t1Path = Paths.get(jTextField35.getText());
        Path tileset1Path;
        if(!t1Path.isAbsolute()){
           tileset1Path = basePath.resolve(t1Path).normalize();
        }else{
            tileset1Path = t1Path;
        }        
        System.out.println(tileset1Path.toString());

        Path t2Path = Paths.get(jTextField38.getText());
        Path tileset2Path;
        if(!t2Path.isAbsolute()){
           tileset2Path = basePath.resolve(t2Path).normalize();
        }else{
            tileset2Path = t2Path;
        }        
        System.out.println(tileset2Path.toString());

        Path t3Path = Paths.get(jTextField39.getText());
        Path tileset3Path;
        if(!t3Path.isAbsolute()){
           tileset3Path = basePath.resolve(t3Path).normalize();
        }else{
            tileset3Path = t3Path;
        }        
        System.out.println(tileset3Path.toString());

        Path t4Path = Paths.get(jTextField40.getText());
        Path tileset4Path;
        if(!t4Path.isAbsolute()){
           tileset4Path = basePath.resolve(t4Path).normalize();
        }else{
            tileset4Path = t4Path;
        }        
        System.out.println(tileset4Path.toString());

        Path t5Path = Paths.get(jTextField41.getText());
        Path tileset5Path;
        if(!t5Path.isAbsolute()){
           tileset5Path = basePath.resolve(t5Path).normalize();
        }else{
            tileset5Path = t5Path;
        }        
        System.out.println(tileset5Path.toString());
        
        String[] tilesetPaths = new String[5];        
        tilesetPaths[0] = tileset1Path.toString();
        tilesetPaths[1] = tileset2Path.toString();
        tilesetPaths[2] = tileset3Path.toString();
        tilesetPaths[3] = tileset4Path.toString();
        tilesetPaths[4] = tileset5Path.toString();

        mapManager.importBaseTilesets(tilesetPaths, jCheckBox3.isSelected(), palettePath.toString());
        UpdateBaseTilesetsInterface();
        UpdateMiscInterfaces();
        updateExportPaths();
    }//GEN-LAST:event_jButton9ActionPerformed

    void updateExportPaths() {
        jTextField32.setText(mapManager.getLastImportedPalettePath());
        String[] lastTilesetPaths = mapManager.getLastImportedTilesetsPaths();
        JTextField text = null;
        if (lastTilesetPaths != null) {
            for (int i = 0; i < lastTilesetPaths.length; i++) {
                switch (i) {
                    case 0:
                        text = jTextField16;
                        break;
                    case 1:
                        text = jTextField17;
                        break;
                    case 2:
                        text = jTextField18;
                        break;
                    case 3:
                        text = jTextField25;
                        break;
                    case 4:
                        text = jTextField26;
                        break;
                }
                if (lastTilesetPaths[i] != null && lastTilesetPaths[i].length() > 0) {
                    text.setText(lastTilesetPaths[i]);
                } else {
                    text.setText(String.format("../../../graphics/maps/maptilesets/newmaptileset%03d.bin", i));
                }
            }
        }
    }
    
    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField35.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField38.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField39.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField40.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField41.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField42.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField43.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            jTextField44.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jTextField43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField43ActionPerformed
    
    private void UpdateBaseTilesetsInterface() {
        Map map = mapManager.getMap();
        if (map != null && map.getTilesets()!= null) {
            tileset1Layout.setTilesPerRow(16);
            tileset1Layout.setDisplaySize(2);
            tileset1Layout.setTiles(map.getTilesets()[0].getTiles());
            tileset2Layout.setTilesPerRow(16);
            tileset2Layout.setDisplaySize(2);
            tileset2Layout.setTiles(map.getTilesets()[1].getTiles());
            tileset3Layout.setTilesPerRow(16);
            tileset3Layout.setDisplaySize(2);
            tileset3Layout.setTiles(map.getTilesets()[2].getTiles());
            tileset4Layout.setTilesPerRow(16);
            tileset4Layout.setDisplaySize(2);
            tileset4Layout.setTiles(map.getTilesets()[3].getTiles());
            tileset5Layout.setTilesPerRow(16);
            tileset5Layout.setDisplaySize(2);
            tileset5Layout.setTiles(map.getTilesets()[4].getTiles());
        
            RepaintBaseTilesetsPanels();
        }
    }
    
    private void UpdateNewTilesetsInterface() {
        Map map = mapManager.getMap();
        if (map != null && map.getNewTilesets() != null) {
            newtileset1Layout.setTilesPerRow(16);
            newtileset1Layout.setDisplaySize(2);
            newtileset1Layout.setTiles(map.getNewTilesets()[0].getTiles());
            newtileset2Layout.setTilesPerRow(16);
            newtileset2Layout.setDisplaySize(2);
            newtileset2Layout.setTiles(map.getNewTilesets()[1].getTiles());
            newtileset3Layout.setTilesPerRow(16);
            newtileset3Layout.setDisplaySize(2);
            newtileset3Layout.setTiles(map.getNewTilesets()[2].getTiles());
            newtileset4Layout.setTilesPerRow(16);
            newtileset4Layout.setDisplaySize(2);
            newtileset4Layout.setTiles(map.getNewTilesets()[3].getTiles());
            newtileset5Layout.setTilesPerRow(16);
            newtileset5Layout.setDisplaySize(2);
            newtileset5Layout.setTiles(map.getNewTilesets()[4].getTiles());
        
            RepaintNewTilesetsPanels();
        }
    }
    
    private void updateBlocksetInterface() {
        Map map = mapManager.getMap();
        if (map != null && map.getOptimizedBlockset()!= null) {
            optimizedBlocksLayout.setBlocksPerRow(((int)jSpinner4.getModel().getValue()));
            optimizedBlocksLayout.setCurrentDisplaySize(jComboBox5.getSelectedIndex()+1);
            optimizedBlocksLayout.setBlocks(map.getOptimizedBlockset());

            repaintBlocksetInterface();
        }
    }
    
    private void updateOrphansInterface() {
        Map map = mapManager.getMap();
        if (map != null && map.getOrphanTiles()!= null) {
            orphanTilesLayout.setTilesPerRow(16);
            orphanTilesLayout.setDisplaySize(2);
            orphanTilesLayout.setTiles(map.getOrphanTiles());

            repaintOrphansInterface();
        }
    }
    
    private void UpdateMiscInterfaces() {
        Map map = mapManager.getMap();
        if (map != null) {
            baseBlocksLayout.setBlocksPerRow(((int)jSpinner1.getModel().getValue()));
            baseBlocksLayout.setCurrentDisplaySize(jComboBox2.getSelectedIndex()+1);
            baseBlocksLayout.setBlocks(map.getBlocks());

            baseTilesLayout.setTilesPerRow(((int)jSpinner2.getModel().getValue()));
            baseTilesLayout.setDisplaySize(jComboBox3.getSelectedIndex()+1);
            baseTilesLayout.setTiles(map.getTiles());
        
            mapPanel.setMap(map);
            mapPanel.setMapLayout(map.getLayout());
            mapPanel.setBlockset(map.getBlocks());
            mapPanel.clearAllCachedImages();
            mapPanel.setDrawOrphanedTiles(jCheckBox5.isSelected());
            mapPanel.setCurrentDisplaySize(jComboBox1.getSelectedIndex()+1);
            
            RepaintMiscPanels();
        }
    }
    
    private void RepaintBaseTilesetsPanels() {
        jPanel30.setSize(tileset1Layout.getWidth(), tileset1Layout.getHeight());
        jPanel30.revalidate();
        jPanel30.repaint();
        jPanel33.setSize(tileset2Layout.getWidth(), tileset2Layout.getHeight());
        jPanel33.revalidate();
        jPanel33.repaint();
        jPanel36.setSize(tileset3Layout.getWidth(), tileset3Layout.getHeight());
        jPanel36.revalidate();
        jPanel36.repaint(); 
        jPanel37.setSize(tileset4Layout.getWidth(), tileset4Layout.getHeight());
        jPanel37.revalidate();
        jPanel37.repaint(); 
        jPanel38.setSize(tileset5Layout.getWidth(), tileset5Layout.getHeight());
        jPanel38.revalidate();
        jPanel38.repaint();
    }
    
    private void RepaintNewTilesetsPanels() {
        jPanel39.setSize(newtileset1Layout.getWidth(), newtileset1Layout.getHeight());
        jPanel39.revalidate();
        jPanel39.repaint();
        jPanel40.setSize(newtileset2Layout.getWidth(), newtileset2Layout.getHeight());
        jPanel40.revalidate();
        jPanel40.repaint();
        jPanel41.setSize(newtileset3Layout.getWidth(), newtileset3Layout.getHeight());
        jPanel41.revalidate();
        jPanel41.repaint();
        jPanel42.setSize(newtileset4Layout.getWidth(), newtileset4Layout.getHeight());
        jPanel42.revalidate();
        jPanel42.repaint();
        jPanel43.setSize(newtileset5Layout.getWidth(), newtileset5Layout.getHeight());
        jPanel43.revalidate();
        jPanel43.repaint();
    }
    
    private void repaintBlocksetInterface() {
        jPanel45.setSize(optimizedBlocksLayout.getWidth(), optimizedBlocksLayout.getHeight());
        jPanel45.revalidate();
        jPanel45.repaint();
    }
    
    private void repaintOrphansInterface() {
        jPanel27.setSize(orphanTilesLayout.getWidth(), orphanTilesLayout.getHeight());
        jPanel27.revalidate();
        jPanel27.repaint();
    }
    
    private void RepaintMiscPanels() {
        jPanel6.setSize(baseBlocksLayout.getWidth(), baseBlocksLayout.getHeight());
        jPanel6.revalidate();
        jPanel6.repaint();
        jPanel23.setSize(baseTilesLayout.getWidth(), baseTilesLayout.getHeight());
        jPanel23.revalidate();
        jPanel23.repaint();
        jPanel2.setSize(mapPanel.getWidth(), mapPanel.getHeight());
        jPanel2.revalidate();
        jPanel2.repaint();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Windows is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainEditor().setVisible(true);
            }
        });
    }
    
    private static void initConsole(JTextArea textArea){
        PrintStream con=new PrintStream(new TextAreaOutputStream(textArea));
        System.setOut(con);
        System.setErr(con);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.sfc.sf2.core.gui.controls.NameableButtonGroup buttonGroup1;
    private com.sfc.sf2.core.gui.controls.NameableButtonGroup buttonGroup2;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
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
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
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
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    // End of variables declaration//GEN-END:variables


}
