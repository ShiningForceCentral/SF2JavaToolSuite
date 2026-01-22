/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellAnimation;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.background.BackgroundManager;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.ground.GroundManager;
import com.sfc.sf2.spellAnimation.io.SpellAnimationAsmProcessor;
import com.sfc.sf2.spellGraphic.SpellGraphicManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */
public class SpellAnimationManager extends AbstractManager {    
    private SpellAnimation spellAnimation;
    private Background background;
    private Ground ground;

    @Override
    public void clearData() {        
        spellAnimation = null;
        background = null;
        ground = null;
    }

    public SpellAnimation importDisassembly(Path spellGraphicPath, Path spellAnimationPath, Path spellPalettePath) throws IOException, DisassemblyException {
        return importDisassembly(spellGraphicPath, spellAnimationPath, spellPalettePath, null, null, null, null);
    }
    
    public SpellAnimation importDisassembly(Path spellAnimationPath, Path spellGraphicPath, Path spellPalettePath, Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        Tileset spellGraphic = new SpellGraphicManager().importDisassembly(spellGraphicPath, spellPalettePath, 16);
        spellAnimation = new SpellAnimationAsmProcessor().importAsmData(groundPath, null);
        spellAnimation.setSpellGraphic(spellGraphic);
        background = null;
        if (backgroundPath != null) {
            background = new BackgroundManager().importDisassembly(backgroundPath);
        }
        ground = null;
        if (groundPath != null) {
            ground = new GroundManager().importDisassembly(groundBasePalettePath, groundPalettePath, groundPath);
        }
        Console.logger().info("Spell Animation successfully imported from : " + spellAnimationPath);
        Console.logger().finest("EXITING exportDisassembly");
        return spellAnimation;
    }
    
    public void exportDisassembly(Path filePath, SpellAnimation spellAnimation) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        this.spellAnimation = spellAnimation;
        new SpellAnimationAsmProcessor().exportAsmData(filePath, spellAnimation);
        Console.logger().info("Spell Animation successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");   
    }

    public SpellAnimation getSpellAnimation() {
        return spellAnimation;
    }

    public Background getBackground() {
        return background;
    }

    public Ground getGround() {
        return ground;
    }
}
