/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.models;

import com.sfc.sf2.core.models.AbstractTableModel;
import com.sfc.sf2.stats.AllyClassStats;

/**
 *
 * @author TiMMy
 */
public class AllyStatsTableModel extends AbstractTableModel<AllyClassStats> {

    public AllyStatsTableModel() {
        super(new String[] {
            "Ally", "Label", "Class",
            "HP start", "HP proj", "HP curve",
            "MP start", "MP proj", "MP curve",
            "ATT start", "ATT proj", "ATT curve",
            "DEF start", "DEF proj", "DEF curve",
            "AGI start", "AGI proj", "AGI curve",
            "Use first list", "Spell list"
        }, -1);
    }

    @Override
    public Class<?> getColumnType(int col) {
        return switch (col) {
            case 0, 3, 4, 6, 7, 9, 10, 12, 13, 15, 16 -> Integer.class;
            case 18 -> Boolean.class;
            default -> String.class;
        };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    protected AllyClassStats createBlankItem(int row) {
        AllyClassStats item = new AllyClassStats();
        item.setAllyIndex(row);
        item.setAllyLabel(String.format("AllyStats%02d", row));
        return item;
    }

    @Override
    protected AllyClassStats cloneItem(AllyClassStats item) {
        return item.clone();
    }

    @Override
    protected Object getValue(AllyClassStats item, int row, int col) {
        return switch (col) {
            case 0 -> item.getAllyIndex();
            case 1 -> item.getAllyLabel();
            case 2 -> item.getClassName();
            case 3 -> item.getHpStart();
            case 4 -> item.getHpProjected();
            case 5 -> item.getHpCurve();
            case 6 -> item.getMpStart();
            case 7 -> item.getMpProjected();
            case 8 -> item.getMpCurve();
            case 9 -> item.getAttStart();
            case 10 -> item.getAttProjected();
            case 11 -> item.getAttCurve();
            case 12 -> item.getDefStart();
            case 13 -> item.getDefProjected();
            case 14 -> item.getDefCurve();
            case 15 -> item.getAgiStart();
            case 16 -> item.getAgiProjected();
            case 17 -> item.getAgiCurve();
            case 18 -> item.isUseFirstSpellList();
            case 19 -> item.getSpellList();
            default -> null;
        };
    }

    @Override
    protected AllyClassStats setValue(AllyClassStats item, int row, int col, Object value) {
        switch (col) {
            case 0 -> item.setAllyIndex(asInt(value));
            case 1 -> item.setAllyLabel((String) value);
            case 2 -> item.setClassName((String) value);
            case 3 -> item.setHpStart(asInt(value));
            case 4 -> item.setHpProjected(asInt(value));
            case 5 -> item.setHpCurve((String) value);
            case 6 -> item.setMpStart(asInt(value));
            case 7 -> item.setMpProjected(asInt(value));
            case 8 -> item.setMpCurve((String) value);
            case 9 -> item.setAttStart(asInt(value));
            case 10 -> item.setAttProjected(asInt(value));
            case 11 -> item.setAttCurve((String) value);
            case 12 -> item.setDefStart(asInt(value));
            case 13 -> item.setDefProjected(asInt(value));
            case 14 -> item.setDefCurve((String) value);
            case 15 -> item.setAgiStart(asInt(value));
            case 16 -> item.setAgiProjected(asInt(value));
            case 17 -> item.setAgiCurve((String) value);
            case 18 -> item.setUseFirstSpellList((Boolean) value);
            case 19 -> item.setSpellList((String) value);
        }
        return item;
    }

    @Override
    protected Comparable<?> getMinLimit(AllyClassStats item, int col) {
        return 0;
    }

    @Override
    protected Comparable<?> getMaxLimit(AllyClassStats item, int col) {
        return 255;
    }

    private static int asInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(value.toString());
    }
}
