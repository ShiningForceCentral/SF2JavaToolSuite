/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.stats.io.AllyStatsAsmProcessor;
import com.sfc.sf2.stats.io.ClassDefsAsmProcessor;
import com.sfc.sf2.stats.io.ItemDefsAsmProcessor;
import com.sfc.sf2.stats.io.ShopInventoriesAsmProcessor;
import com.sfc.sf2.stats.io.SpellDefsAsmProcessor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author TiMMy
 */
public class StatsManager extends AbstractManager {

    private AllyClassStats[] allies;
    private SpellDefinition[] spells;
    private ItemDefinition[] items;
    private ClassDefinition[] classes;
    private ShopInventory[] shops;

    @Override
    public void clearData() {
        allies = null;
        spells = null;
        items = null;
        classes = null;
        shops = null;
    }

    public AllyClassStats[] importAllies(Path directory) throws IOException, AsmException {
        Console.logger().finest("ENTERING importAllies");
        File[] files = FileHelpers.findAllFilesInDirectory(directory, "allystats", FileFormat.ASM);
        if (files == null || files.length == 0) {
            throw new IOException("No allystats*.asm files found in " + directory);
        }
        Arrays.sort(files, Comparator.comparingInt(FileHelpers::getNumberFromFileName));
        ArrayList<AllyClassStats> all = new ArrayList<>();
        AllyStatsAsmProcessor processor = new AllyStatsAsmProcessor();
        for (File file : files) {
            int index = FileHelpers.getNumberFromFileName(file);
            processor.setAllyIdentity(index, PathHelpers.filenameFromPath(file.toPath()));
            AllyClassStats[] parsed = processor.importAsmData(file.toPath(), null);
            for (AllyClassStats entry : parsed) {
                entry.setAllyIndex(index);
                if (entry.getAllyLabel() == null || entry.getAllyLabel().isEmpty()) {
                    entry.setAllyLabel(String.format("AllyStats%02d", index));
                }
                all.add(entry);
            }
        }
        allies = all.toArray(AllyClassStats[]::new);
        Console.logger().info(allies.length + " ally class-stat rows imported from : " + directory);
        Console.logger().finest("EXITING importAllies");
        return allies;
    }

    public void exportAllies(Path directory, AllyClassStats[] data) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportAllies");
        this.allies = data;
        Map<Integer, ArrayList<AllyClassStats>> grouped = new LinkedHashMap<>();
        for (AllyClassStats row : data) {
            grouped.computeIfAbsent(row.getAllyIndex(), key -> new ArrayList<>()).add(row);
        }
        AllyStatsAsmProcessor processor = new AllyStatsAsmProcessor();
        for (Map.Entry<Integer, ArrayList<AllyClassStats>> entry : grouped.entrySet()) {
            int index = entry.getKey();
            Path filePath = directory.resolve(String.format("allystats%02d.asm", index));
            if (!PathHelpers.createPathIfRequred(filePath)) {
                continue;
            }
            processor.setAllyIdentity(index, entry.getValue().get(0).getAllyLabel());
            processor.exportAsmData(filePath, entry.getValue().toArray(AllyClassStats[]::new), null);
        }
        Console.logger().info(grouped.size() + " ally stats files exported to : " + directory);
        Console.logger().finest("EXITING exportAllies");
    }

    public SpellDefinition[] importSpells(Path filePath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importSpells");
        spells = new SpellDefsAsmProcessor().importAsmData(filePath, null);
        Console.logger().info(spells.length + " spells imported from : " + filePath);
        Console.logger().finest("EXITING importSpells");
        return spells;
    }

    public void exportSpells(Path filePath, SpellDefinition[] data) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportSpells");
        this.spells = data;
        new SpellDefsAsmProcessor().exportAsmData(filePath, data, null);
        Console.logger().info(data.length + " spells exported to : " + filePath);
        Console.logger().finest("EXITING exportSpells");
    }

    public ItemDefinition[] importItems(Path filePath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importItems");
        items = new ItemDefsAsmProcessor().importAsmData(filePath, null);
        Console.logger().info(items.length + " items imported from : " + filePath);
        Console.logger().finest("EXITING importItems");
        return items;
    }

    public void exportItems(Path filePath, ItemDefinition[] data) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportItems");
        this.items = data;
        new ItemDefsAsmProcessor().exportAsmData(filePath, data, null);
        Console.logger().info(data.length + " items exported to : " + filePath);
        Console.logger().finest("EXITING exportItems");
    }

    public ClassDefinition[] importClasses(Path filePath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importClasses");
        classes = new ClassDefsAsmProcessor().importAsmData(filePath, null);
        Console.logger().info(classes.length + " classes imported from : " + filePath);
        Console.logger().finest("EXITING importClasses");
        return classes;
    }

    public void exportClasses(Path filePath, ClassDefinition[] data) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportClasses");
        this.classes = data;
        new ClassDefsAsmProcessor().exportAsmData(filePath, data, null);
        Console.logger().info(data.length + " classes exported to : " + filePath);
        Console.logger().finest("EXITING exportClasses");
    }

    public ShopInventory[] importShops(Path filePath) throws IOException, AsmException {
        Console.logger().finest("ENTERING importShops");
        shops = new ShopInventoriesAsmProcessor().importAsmData(filePath, null);
        Console.logger().info(shops.length + " shops imported from : " + filePath);
        Console.logger().finest("EXITING importShops");
        return shops;
    }

    public void exportShops(Path filePath, ShopInventory[] data) throws IOException, AsmException {
        Console.logger().finest("ENTERING exportShops");
        this.shops = data;
        new ShopInventoriesAsmProcessor().exportAsmData(filePath, data, null);
        Console.logger().info(data.length + " shops exported to : " + filePath);
        Console.logger().finest("EXITING exportShops");
    }

    public AllyClassStats[] getAllies() { return allies; }
    public SpellDefinition[] getSpells() { return spells; }
    public ItemDefinition[] getItems() { return items; }
    public ClassDefinition[] getClasses() { return classes; }
    public ShopInventory[] getShops() { return shops; }
}
