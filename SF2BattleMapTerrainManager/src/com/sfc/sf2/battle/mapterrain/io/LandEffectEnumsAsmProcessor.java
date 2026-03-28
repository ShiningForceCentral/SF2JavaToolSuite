/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.io;

import com.sfc.sf2.battle.mapterrain.LandEffectEnums;
import com.sfc.sf2.core.io.asm.SF2EnumsAsmProcessor;
import com.sfc.sf2.helpers.StringHelpers;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class LandEffectEnumsAsmProcessor extends SF2EnumsAsmProcessor<LandEffectEnums> {

    //Note: Land effect data is split into 2 lists to read them both then combine them
    
    public LandEffectEnumsAsmProcessor() {
        super(new String[] { "LandEffectSettings (bitfield)", "LandEffectSetting_Obstructed", "MoveType (bitfield)" });
    }

    @Override
    protected void parseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData) {
        switch (categoryIndex) {
            case 0:
            case 1:
                if (line.startsWith("LANDEFFECTSETTING_")) {
                    line = line.substring(line.indexOf("_") + 1);
                    String name = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(name, value);
                }
            case 2:
                //Move types are duplicated in engine so we only care about 1 set of them
                if (line.startsWith("MOVETYPE_LOWER_")) {
                    line = line.substring(15);
                    String command = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(command, value);
                }
                break;
        }
    }

    @Override
    protected LandEffectEnums createEnumsData(LinkedHashMap<String, Integer>[] dataSets) {
        dataSets[2].put("UNDEFINED", 0);    //Add the first value in the set
        dataSets[0].putAll(dataSets[1]);
        return new LandEffectEnums(dataSets[0], dataSets[2]);
    }
}
