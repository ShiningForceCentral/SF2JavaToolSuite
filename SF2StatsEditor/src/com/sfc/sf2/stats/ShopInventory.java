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
public class ShopInventory {

    private int index;
    private String name;
    private String items;

    public ShopInventory() {
        this.name = "Shop";
        this.items = "MEDICAL_HERB";
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public ShopInventory clone() {
        ShopInventory copy = new ShopInventory();
        copy.index = index;
        copy.name = name;
        copy.items = items;
        return copy;
    }
}
