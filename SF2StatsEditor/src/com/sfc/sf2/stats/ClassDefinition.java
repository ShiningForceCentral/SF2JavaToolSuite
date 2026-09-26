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
public class ClassDefinition {

    private int index;
    private String name;
    private int mov;
    private String resistance;
    private String moveType;
    private String prowess;

    public ClassDefinition() {
        this.name = "SDMN";
        this.mov = 6;
        this.resistance = "NONE";
        this.moveType = "REGULAR";
        this.prowess = "CRITICAL125_1IN16|DOUBLE_1IN32|COUNTER_1IN32";
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getMov() { return mov; }
    public void setMov(int mov) { this.mov = mov; }
    public String getResistance() { return resistance; }
    public void setResistance(String resistance) { this.resistance = resistance; }
    public String getMoveType() { return moveType; }
    public void setMoveType(String moveType) { this.moveType = moveType; }
    public String getProwess() { return prowess; }
    public void setProwess(String prowess) { this.prowess = prowess; }

    public ClassDefinition clone() {
        ClassDefinition copy = new ClassDefinition();
        copy.index = index;
        copy.name = name;
        copy.mov = mov;
        copy.resistance = resistance;
        copy.moveType = moveType;
        copy.prowess = prowess;
        return copy;
    }
}
