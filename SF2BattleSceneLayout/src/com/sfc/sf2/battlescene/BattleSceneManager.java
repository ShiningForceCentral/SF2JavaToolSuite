/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlescene;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.background.BackgroundManager;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.ground.GroundManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class BattleSceneManager extends AbstractManager {
    
    private Background background;
    private Ground ground;

    @Override
    public void clearData() {
        if (background != null) {
            background.getTileset().clearIndexedColorImage(true);
        }
        if (ground != null) {
            ground.getTileset().clearIndexedColorImage(true);
        }
        background = null;
        ground = null;
    }
       
    public void importDisassembly(Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        background = new BackgroundManager().importDisassembly(backgroundPath);
        ground = new GroundManager().importDisassembly(groundBasePalettePath, groundPalettePath, groundPath);
        Console.logger().info("Battle Scene successfully imported.");
        Console.logger().finest("EXITING importDisassembly");
    }

    public Background getBackground() {
        return background;
    }

    public Ground getGround() {
        return ground;
    }
}
