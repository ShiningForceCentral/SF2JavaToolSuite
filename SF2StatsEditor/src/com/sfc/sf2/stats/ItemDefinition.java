/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats;

/**
 *
 * @author TiMMy
 */
public class ItemDefinition {

    private int index;
    private String name;
    private String equipFlags;
    private int rangeMin;
    private int rangeMax;
    private int price;
    private String itemType;
    private String useSpell;
    private String effect1;
    private int effectParam1;
    private String effect2;
    private int effectParam2;
    private String effect3;
    private int effectParam3;

    public ItemDefinition() {
        this.name = "New Item";
        this.equipFlags = "NONE";
        this.itemType = "NONE";
        this.useSpell = "NOTHING";
        this.effect1 = "NONE";
        this.effect2 = "NONE";
        this.effect3 = "NONE";
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEquipFlags() { return equipFlags; }
    public void setEquipFlags(String equipFlags) { this.equipFlags = equipFlags; }
    public int getRangeMin() { return rangeMin; }
    public void setRangeMin(int rangeMin) { this.rangeMin = rangeMin; }
    public int getRangeMax() { return rangeMax; }
    public void setRangeMax(int rangeMax) { this.rangeMax = rangeMax; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getUseSpell() { return useSpell; }
    public void setUseSpell(String useSpell) { this.useSpell = useSpell; }
    public String getEffect1() { return effect1; }
    public void setEffect1(String effect1) { this.effect1 = effect1; }
    public int getEffectParam1() { return effectParam1; }
    public void setEffectParam1(int effectParam1) { this.effectParam1 = effectParam1; }
    public String getEffect2() { return effect2; }
    public void setEffect2(String effect2) { this.effect2 = effect2; }
    public int getEffectParam2() { return effectParam2; }
    public void setEffectParam2(int effectParam2) { this.effectParam2 = effectParam2; }
    public String getEffect3() { return effect3; }
    public void setEffect3(String effect3) { this.effect3 = effect3; }
    public int getEffectParam3() { return effectParam3; }
    public void setEffectParam3(int effectParam3) { this.effectParam3 = effectParam3; }

    public ItemDefinition clone() {
        ItemDefinition copy = new ItemDefinition();
        copy.index = index;
        copy.name = name;
        copy.equipFlags = equipFlags;
        copy.rangeMin = rangeMin;
        copy.rangeMax = rangeMax;
        copy.price = price;
        copy.itemType = itemType;
        copy.useSpell = useSpell;
        copy.effect1 = effect1;
        copy.effectParam1 = effectParam1;
        copy.effect2 = effect2;
        copy.effectParam2 = effectParam2;
        copy.effect3 = effect3;
        copy.effectParam3 = effectParam3;
        return copy;
    }
}
