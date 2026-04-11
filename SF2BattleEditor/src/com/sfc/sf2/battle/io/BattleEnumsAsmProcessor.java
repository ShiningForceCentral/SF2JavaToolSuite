/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.battle.io;

import com.sfc.sf2.battle.BattleEnums;
import com.sfc.sf2.core.io.asm.SF2EnumsAsmProcessor;
import com.sfc.sf2.helpers.StringHelpers;
import java.util.LinkedHashMap;

/**
 *
 * @author TiMMy
 */
public class BattleEnumsAsmProcessor extends SF2EnumsAsmProcessor<BattleEnums> {

    public BattleEnumsAsmProcessor() {
        super(new String[] { "Battles" });
    }
    
    @Override
    protected void parseLine(int categoryIndex, String line, LinkedHashMap<String, Integer> asmData) {
        switch (categoryIndex) {
            case 0:
                if (line.startsWith("BATTLE_")) {
                    line = line.substring(line.indexOf('_') + 1);
                    String battle = line.substring(0, line.indexOf(":"));
                    int value = StringHelpers.getValueInt(line.substring(line.indexOf("equ") + 4));
                    asmData.put(battle, value);
                }
        }
    }

    @Override
    protected BattleEnums createEnumsData(LinkedHashMap<String, Integer>[] dataSets) {
        return new BattleEnums(dataSets[0]);
    }
}
