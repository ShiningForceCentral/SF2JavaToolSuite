/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.gui;

import com.sfc.sf2.core.gui.AbstractMainEditor;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.gui.controls.DirectoryButton;
import com.sfc.sf2.core.gui.controls.FileButton;
import com.sfc.sf2.core.gui.controls.Table;
import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.stats.AllyClassStats;
import com.sfc.sf2.stats.ClassDefinition;
import com.sfc.sf2.stats.ItemDefinition;
import com.sfc.sf2.stats.ShopInventory;
import com.sfc.sf2.stats.SpellDefinition;
import com.sfc.sf2.stats.StatsManager;
import com.sfc.sf2.stats.models.AllyStatsTableModel;
import com.sfc.sf2.stats.models.ClassDefsTableModel;
import com.sfc.sf2.stats.models.ItemDefsTableModel;
import com.sfc.sf2.stats.models.ShopInventoriesTableModel;
import com.sfc.sf2.stats.models.SpellDefsTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Path;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author TiMMy
 */
public class StatsMainEditor extends AbstractMainEditor {

    private final StatsManager statsManager = new StatsManager();
    private final AllyStatsTableModel allyModel = new AllyStatsTableModel();
    private final SpellDefsTableModel spellModel = new SpellDefsTableModel();
    private final ItemDefsTableModel itemModel = new ItemDefsTableModel();
    private final ClassDefsTableModel classModel = new ClassDefsTableModel();
    private final ShopInventoriesTableModel shopModel = new ShopInventoriesTableModel();

    private Console console1;
    private DirectoryButton directoryButtonAllies;
    private FileButton fileButtonSpells;
    private FileButton fileButtonItems;
    private FileButton fileButtonClasses;
    private FileButton fileButtonShops;
    private Table allyTable;
    private Table spellTable;
    private Table itemTable;
    private Table classTable;
    private Table shopTable;

    public StatsMainEditor() {
        super();
        initComponents();
        initCore(console1);
    }

    @Override
    protected void initEditor() {
        super.initEditor();
        allyTable.setModel(allyModel);
        spellTable.setModel(spellModel);
        itemTable.setModel(itemModel);
        classTable.setModel(classModel);
        shopTable.setModel(shopModel);
        allyTable.setHorizontalScrolling(true);
        itemTable.setHorizontalScrolling(true);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SF2StatsEditor");

        console1 = new Console();
        directoryButtonAllies = new DirectoryButton();
        directoryButtonAllies.setLabelText("Ally stats folder :");
        directoryButtonAllies.setDirectoryPath("./allies/stats");
        directoryButtonAllies.setInfoMessage("Folder of allystatsXX.asm files.");

        fileButtonSpells = pathButton("Spell defs :", "./spells/spelldefs.asm");
        fileButtonItems = pathButton("Item defs :", "./items/itemdefs.asm");
        fileButtonClasses = pathButton("Class defs :", "./allies/classes/classdefs.asm");
        fileButtonShops = pathButton("Shop inventories :", "./items/shopinventories.asm");

        JButton importAll = new JButton("Import all");
        importAll.addActionListener(e -> importAll());
        JButton exportAll = new JButton("Export all");
        exportAll.addActionListener(e -> exportAll());

        JPanel ioPanel = new JPanel();
        ioPanel.setLayout(new BoxLayout(ioPanel, BoxLayout.Y_AXIS));
        ioPanel.setBorder(BorderFactory.createTitledBorder("Disassembly files"));
        JLabel help = new JLabel("<html>Loads SF2DISASM standard stats macros.<br>Default paths assume the jar is in disasm/data/stats/.</html>");
        help.setAlignmentX(LEFT_ALIGNMENT);
        ioPanel.add(help);
        ioPanel.add(Box.createVerticalStrut(8));
        for (javax.swing.JComponent component : new javax.swing.JComponent[] {
            directoryButtonAllies, fileButtonSpells, fileButtonItems, fileButtonClasses, fileButtonShops
        }) {
            component.setAlignmentX(LEFT_ALIGNMENT);
            component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            ioPanel.add(component);
            ioPanel.add(Box.createVerticalStrut(4));
        }
        JPanel buttons = new JPanel();
        buttons.setAlignmentX(LEFT_ALIGNMENT);
        buttons.add(importAll);
        buttons.add(exportAll);
        ioPanel.add(buttons);
        ioPanel.add(Box.createVerticalGlue());

        allyTable = table("Ally class growths and spell lists. Spell list is level, SPELL pairs.");
        spellTable = table("Spell definitions from spelldefs.asm.");
        itemTable = table("Item definitions from itemdefs.asm.");
        classTable = table("Class definitions from classdefs.asm.");
        shopTable = table("Shop inventories. Items is a comma-separated ITEM enum list.");

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Allies", allyTable);
        tabs.addTab("Spells", spellTable);
        tabs.addTab("Items", itemTable);
        tabs.addTab("Classes", classTable);
        tabs.addTab("Shops", shopTable);

        JSplitPane horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ioPanel, tabs);
        horizontal.setDividerLocation(340);
        horizontal.setOneTouchExpandable(true);

        JSplitPane vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontal, console1);
        vertical.setDividerLocation(560);
        vertical.setResizeWeight(0.85);
        vertical.setOneTouchExpandable(true);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(vertical, BorderLayout.CENTER);
        setSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);
    }

    private static FileButton pathButton(String label, String path) {
        FileButton button = new FileButton();
        button.setLabelText(label);
        button.setFilePath(path);
        button.setFileFormatFilter(FileFormat.ASM);
        button.setInfoMessage("");
        return button;
    }

    private static Table table(String info) {
        Table table = new Table();
        table.setInfoMessage(info);
        table.setButtonsVisible(true);
        table.setHorizontalScrolling(true);
        return table;
    }

    private void importAll() {
        try {
            statsManager.importAllies(PathHelpers.getBasePath().resolve(directoryButtonAllies.getDirectoryPath()));
            allyModel.setTableData(statsManager.getAllies());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Ally stats could not be imported.");
        }
        try {
            statsManager.importSpells(PathHelpers.getBasePath().resolve(fileButtonSpells.getFilePath()));
            spellModel.setTableData(statsManager.getSpells());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Spell defs could not be imported.");
        }
        try {
            statsManager.importItems(PathHelpers.getBasePath().resolve(fileButtonItems.getFilePath()));
            itemModel.setTableData(statsManager.getItems());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Item defs could not be imported.");
        }
        try {
            statsManager.importClasses(PathHelpers.getBasePath().resolve(fileButtonClasses.getFilePath()));
            classModel.setTableData(statsManager.getClasses());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Class defs could not be imported.");
        }
        try {
            statsManager.importShops(PathHelpers.getBasePath().resolve(fileButtonShops.getFilePath()));
            shopModel.setTableData(statsManager.getShops());
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Shop inventories could not be imported.");
        }
        onDataLoaded();
    }

    private void exportAll() {
        exportPath(PathHelpers.getBasePath().resolve(directoryButtonAllies.getDirectoryPath()), true, () ->
                statsManager.exportAllies(PathHelpers.getBasePath().resolve(directoryButtonAllies.getDirectoryPath()), allyModel.getTableData(AllyClassStats[].class)));
        exportPath(PathHelpers.getBasePath().resolve(fileButtonSpells.getFilePath()), false, () ->
                statsManager.exportSpells(PathHelpers.getBasePath().resolve(fileButtonSpells.getFilePath()), spellModel.getTableData(SpellDefinition[].class)));
        exportPath(PathHelpers.getBasePath().resolve(fileButtonItems.getFilePath()), false, () ->
                statsManager.exportItems(PathHelpers.getBasePath().resolve(fileButtonItems.getFilePath()), itemModel.getTableData(ItemDefinition[].class)));
        exportPath(PathHelpers.getBasePath().resolve(fileButtonClasses.getFilePath()), false, () ->
                statsManager.exportClasses(PathHelpers.getBasePath().resolve(fileButtonClasses.getFilePath()), classModel.getTableData(ClassDefinition[].class)));
        exportPath(PathHelpers.getBasePath().resolve(fileButtonShops.getFilePath()), false, () ->
                statsManager.exportShops(PathHelpers.getBasePath().resolve(fileButtonShops.getFilePath()), shopModel.getTableData(ShopInventory[].class)));
    }

    private static void exportPath(Path path, boolean directory, ExportAction action) {
        Path check = directory ? path.resolve("allystats00.asm") : path;
        if (!PathHelpers.createPathIfRequred(check)) {
            return;
        }
        try {
            action.run();
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, null, ex);
            Console.logger().severe("ERROR Could not export to : " + path);
        }
    }

    @FunctionalInterface
    private interface ExportAction {
        void run() throws Exception;
    }

    public static void main(String args[]) {
        AbstractMainEditor.programSetup();
        java.awt.EventQueue.invokeLater(() -> new StatsMainEditor().setVisible(true));
    }
}
