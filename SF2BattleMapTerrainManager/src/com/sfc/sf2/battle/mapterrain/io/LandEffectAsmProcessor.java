/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.io;

import com.sfc.sf2.battle.mapterrain.BattleMapTerrain;
import com.sfc.sf2.battle.mapterrain.LandEffect;
import com.sfc.sf2.battle.mapterrain.LandEffectEnums;
import com.sfc.sf2.battle.mapterrain.LandEffectMovementType;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author TiMMy
 */
public class LandEffectAsmProcessor extends AbstractAsmProcessor<LandEffectMovementType[], LandEffectEnums> {
        
    @Override
    protected LandEffectMovementType[] parseAsmData(BufferedReader reader, LandEffectEnums pckg) throws IOException, AsmException {
        ArrayList<LandEffectMovementType> landEffectsList = new ArrayList<>();
        int setIndex= 0;
        int effectIndex = 0;
        LandEffect[] currentEffects = new LandEffect[16];
        String line;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("landEffectAndMoveCost")) {
                line = line.substring(22);
                String defense;
                int moveCost;
                int splitIndex = line.indexOf('|');
                if (splitIndex == -1) {
                    defense = line;
                    moveCost = -1;
                } else {
                    defense = line.substring(0, splitIndex);
                    moveCost = StringHelpers.getValueInt(line.substring(splitIndex+1));
                }
                currentEffects[effectIndex] = new LandEffect(defense, moveCost);
                effectIndex++;
                if (effectIndex >= 16) {
                    String movementType = LandEffectEnums.toEnumString(setIndex, pckg.getMoveTypes());
                    landEffectsList.add(new LandEffectMovementType(movementType, currentEffects));
                    currentEffects = new LandEffect[16];
                    effectIndex = 0;
                    setIndex++;
                }
            }
        }
        LandEffectMovementType[] landEffects = new LandEffectMovementType[landEffectsList.size()];
        landEffects = landEffectsList.toArray(landEffects);
        return landEffects;
    }

    @Override
    protected String getHeaderName(LandEffectMovementType[] item, LandEffectEnums pckg) {
        return "Land effect settings and move costs table";
    }

    @Override
    protected void packageAsmData(FileWriter writer, LandEffectMovementType[] item, LandEffectEnums pckg) throws IOException, AsmException {
        writer.write("table_LandEffectSettingsAndMovecosts:\n\n");
        writer.write("; Syntax        landEffectAndMoveCost [LANDEFFECTSETTING_]enum|moveCost\n;\n");
        writer.write("; Note: Constant names (\"enums\"), shorthands (defined by macro), and numerical indexes are interchangeable.)\n");
        for (int i = 0; i < item.length; i++) {
            writer.write(String.format("\n;%d: %s\n", i, LandEffectEnums.toEnumString(i, pckg.getMoveTypes())));
            LandEffect[] effects = item[i].getLandEffects();
            for (int e = 0; e < effects.length; e++) {
                String effectString;
                int effectEnumVal = LandEffectEnums.toEnumInt(effects[e].getDefense(), pckg.getDefenses());
                if (effectEnumVal == 0xFF) {
                    effectString = effects[e].getDefense(); //Obstructed flag
                } else {
                    effectString = String.format("%s|%d \t", effects[e].getDefense(), effects[e].getMoveCost());
                }
                writer.write(String.format("\t\t\tlandEffectAndMoveCost %s\t; %d: %s\n", effectString, e, BattleMapTerrain.getTerrainName(e)));
            }
        }
    }
}
