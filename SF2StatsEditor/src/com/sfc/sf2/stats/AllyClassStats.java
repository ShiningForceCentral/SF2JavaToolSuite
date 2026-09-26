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
public class AllyClassStats {

    private int allyIndex;
    private String allyLabel;
    private String className;
    private int hpStart;
    private int hpProjected;
    private String hpCurve;
    private int mpStart;
    private int mpProjected;
    private String mpCurve;
    private int attStart;
    private int attProjected;
    private String attCurve;
    private int defStart;
    private int defProjected;
    private String defCurve;
    private int agiStart;
    private int agiProjected;
    private String agiCurve;
    private String spellList;
    private boolean useFirstSpellList;

    public AllyClassStats() {
        this.allyLabel = "AllyStats00";
        this.className = "SDMN";
        this.hpCurve = "LINEAR";
        this.mpCurve = "LINEAR";
        this.attCurve = "LINEAR";
        this.defCurve = "LINEAR";
        this.agiCurve = "LINEAR";
        this.spellList = "";
    }

    public int getAllyIndex() { return allyIndex; }
    public void setAllyIndex(int allyIndex) { this.allyIndex = allyIndex; }
    public String getAllyLabel() { return allyLabel; }
    public void setAllyLabel(String allyLabel) { this.allyLabel = allyLabel; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public int getHpStart() { return hpStart; }
    public void setHpStart(int hpStart) { this.hpStart = hpStart; }
    public int getHpProjected() { return hpProjected; }
    public void setHpProjected(int hpProjected) { this.hpProjected = hpProjected; }
    public String getHpCurve() { return hpCurve; }
    public void setHpCurve(String hpCurve) { this.hpCurve = hpCurve; }
    public int getMpStart() { return mpStart; }
    public void setMpStart(int mpStart) { this.mpStart = mpStart; }
    public int getMpProjected() { return mpProjected; }
    public void setMpProjected(int mpProjected) { this.mpProjected = mpProjected; }
    public String getMpCurve() { return mpCurve; }
    public void setMpCurve(String mpCurve) { this.mpCurve = mpCurve; }
    public int getAttStart() { return attStart; }
    public void setAttStart(int attStart) { this.attStart = attStart; }
    public int getAttProjected() { return attProjected; }
    public void setAttProjected(int attProjected) { this.attProjected = attProjected; }
    public String getAttCurve() { return attCurve; }
    public void setAttCurve(String attCurve) { this.attCurve = attCurve; }
    public int getDefStart() { return defStart; }
    public void setDefStart(int defStart) { this.defStart = defStart; }
    public int getDefProjected() { return defProjected; }
    public void setDefProjected(int defProjected) { this.defProjected = defProjected; }
    public String getDefCurve() { return defCurve; }
    public void setDefCurve(String defCurve) { this.defCurve = defCurve; }
    public int getAgiStart() { return agiStart; }
    public void setAgiStart(int agiStart) { this.agiStart = agiStart; }
    public int getAgiProjected() { return agiProjected; }
    public void setAgiProjected(int agiProjected) { this.agiProjected = agiProjected; }
    public String getAgiCurve() { return agiCurve; }
    public void setAgiCurve(String agiCurve) { this.agiCurve = agiCurve; }
    public String getSpellList() { return spellList; }
    public void setSpellList(String spellList) { this.spellList = spellList; }
    public boolean isUseFirstSpellList() { return useFirstSpellList; }
    public void setUseFirstSpellList(boolean useFirstSpellList) { this.useFirstSpellList = useFirstSpellList; }

    public AllyClassStats clone() {
        AllyClassStats copy = new AllyClassStats();
        copy.allyIndex = allyIndex;
        copy.allyLabel = allyLabel;
        copy.className = className;
        copy.hpStart = hpStart;
        copy.hpProjected = hpProjected;
        copy.hpCurve = hpCurve;
        copy.mpStart = mpStart;
        copy.mpProjected = mpProjected;
        copy.mpCurve = mpCurve;
        copy.attStart = attStart;
        copy.attProjected = attProjected;
        copy.attCurve = attCurve;
        copy.defStart = defStart;
        copy.defProjected = defProjected;
        copy.defCurve = defCurve;
        copy.agiStart = agiStart;
        copy.agiProjected = agiProjected;
        copy.agiCurve = agiCurve;
        copy.spellList = spellList;
        copy.useFirstSpellList = useFirstSpellList;
        return copy;
    }
}
