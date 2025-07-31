/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite;

import com.sfc.sf2.battlesprite.io.DisassemblyManager;
import com.sfc.sf2.battlesprite.io.RawImageManager;
import com.sfc.sf2.battlesprite.io.SFCDBankManager;

/**
 *
 * @author wiz
 */
public class BattleSpriteManager {
       
    private BattleSprite battlesprite = new BattleSprite();
       
    public void importDisassembly(String filePath){
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importDisassembly() - Importing disassembly ...");
        battlesprite = DisassemblyManager.importDisassembly(filePath);
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importDisassembly() - Disassembly imported.");
    }
    
    public void exportDisassembly(String filepath){
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importDisassembly() - Exporting disassembly ...");
        DisassemblyManager.exportDisassembly(battlesprite, filepath);
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importDisassembly() - Disassembly exported.");        
    }
    
    public void importPng(String filepath, boolean usePngPalette){
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importPng() - Importing PNG ...");
        battlesprite = RawImageManager.importImage(filepath, battlesprite, usePngPalette);
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importPng() - PNG imported.");
    }
    
    public void exportPng(String filepath, int selectedPalette){
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.exportPng() - Exporting PNG ...");
        RawImageManager.exportImage(battlesprite, filepath, selectedPalette, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_PNG);
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.exportPng() - PNG exported.");       
    }
    
    public void importGif(String filepath, boolean useGifPalette){
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importGif() - Importing GIF ...");
        battlesprite = RawImageManager.importImage(filepath, battlesprite, useGifPalette);
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importGif() - GIF imported.");
    }
    
    public void exportGif(String filepath, int selectedPalette){
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.exportGif() - Exporting GIF ...");
        RawImageManager.exportImage(battlesprite, filepath, selectedPalette, com.sfc.sf2.graphics.io.RawImageManager.FILE_FORMAT_GIF);
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.exportGif() - GIF exported.");       
    }
    
    public void importSFCDBank(String filePath, String loadingOffset, String pointerTableOffset, int battleSpriteIndex){
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importDisassembly() - Importing disassembly ...");
        int ldOffset = Integer.parseInt(loadingOffset, 16);
        int ptOffset = 0;
        if(!pointerTableOffset.isBlank()){
            ptOffset = Integer.parseInt(pointerTableOffset, 16);
        }
        try{
            battlesprite = SFCDBankManager.importSFCDBank(filePath, ldOffset, ptOffset, battleSpriteIndex);
        }catch(Exception e){
            
        }
        System.out.println("com.sfc.sf2.battlesprite.BattleSpriteManager.importDisassembly() - Disassembly imported.");
    }

    public BattleSprite getBattleSprite() {
        return battlesprite;
    }

    public void setBattleSprite(BattleSprite battlesprite) {
        this.battlesprite = battlesprite;
    }
}
