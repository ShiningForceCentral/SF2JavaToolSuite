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
       
    private final BackgroundManager backgroundManager = new BackgroundManager();
    private final GroundManager groundManager = new GroundManager();
    
    @Override
    public void clearData() {
        backgroundManager.clearData();
        groundManager.clearData();
    }
       
    public void importDisassembly(Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        backgroundManager.importDisassembly(backgroundPath);
        groundManager.importDisassembly(groundBasePalettePath, groundPalettePath, groundPath);
        Console.logger().info("Animation successfully imported.");
        Console.logger().finest("EXITING importDisassembly");
    }

    public Background getBackground() {
        return backgroundManager.getBackgrounds()[0];
    }

    public Ground getGround() {
        return groundManager.getGround();
    }
}
