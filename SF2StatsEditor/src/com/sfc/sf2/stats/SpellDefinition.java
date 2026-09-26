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
public class SpellDefinition {

    private String entry;
    private int mpCost;
    private String animation;
    private String properties;
    private int rangeMin;
    private int rangeMax;
    private int radius;
    private int power;

    public SpellDefinition() {
        this.entry = "HEAL";
        this.animation = "NONE";
        this.properties = "TYPE_ATTACK";
    }

    public String getEntry() { return entry; }
    public void setEntry(String entry) { this.entry = entry; }
    public int getMpCost() { return mpCost; }
    public void setMpCost(int mpCost) { this.mpCost = mpCost; }
    public String getAnimation() { return animation; }
    public void setAnimation(String animation) { this.animation = animation; }
    public String getProperties() { return properties; }
    public void setProperties(String properties) { this.properties = properties; }
    public int getRangeMin() { return rangeMin; }
    public void setRangeMin(int rangeMin) { this.rangeMin = rangeMin; }
    public int getRangeMax() { return rangeMax; }
    public void setRangeMax(int rangeMax) { this.rangeMax = rangeMax; }
    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; }
    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public SpellDefinition clone() {
        SpellDefinition copy = new SpellDefinition();
        copy.entry = entry;
        copy.mpCost = mpCost;
        copy.animation = animation;
        copy.properties = properties;
        copy.rangeMin = rangeMin;
        copy.rangeMax = rangeMax;
        copy.radius = radius;
        copy.power = power;
        return copy;
    }
}
