/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battlesprite.animation;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.battlescene.BattleSceneManager;
import com.sfc.sf2.battlesprite.BattleSprite;
import com.sfc.sf2.battlesprite.BattleSprite.BattleSpriteType;
import com.sfc.sf2.battlesprite.BattleSpriteManager;
import com.sfc.sf2.battlesprite.animation.io.BattleSpriteAnimationDisassemblyProcessor;
import com.sfc.sf2.battlesprite.animation.io.BattleSpriteAnimationPackage;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.palette.Palette;
import com.sfc.sf2.weaponsprite.WeaponSprite;
import com.sfc.sf2.weaponsprite.WeaponSpriteManager;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author wiz
 */
public class BattleSpriteAnimationManager extends AbstractManager {
       
    private final BattleSpriteManager battlespriteManager = new BattleSpriteManager();
    private final WeaponSpriteManager weaponspriteManager = new WeaponSpriteManager();
    private final BattleSceneManager battleSceneManager = new BattleSceneManager();
    private final BattleSpriteAnimationDisassemblyProcessor battleSpriteAnimationDisassemblyProcessor = new BattleSpriteAnimationDisassemblyProcessor();
    
    private BattleSpriteAnimation battlespriteAnimation;

    @Override
    public void clearData() {
        battleSceneManager.clearData();
        battlespriteManager.clearData();
        weaponspriteManager.clearData();
        battlespriteAnimation = null;
    }
       
    public void importDisassembly(Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath, Path battlespritePath, Path weaponPalettesPath, Path weaponPath, Path animationPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        try {
            battleSceneManager.importDisassembly(backgroundPath, groundBasePalettePath, groundPalettePath, groundPath);
            Console.logger().info("Ground and background loaded");
        } catch (Exception e) {
            Console.logger().severe("ERROR Environment could not be imported : " + e);
        }
        battlespriteManager.importDisassembly(battlespritePath);
        BattleSpriteType type = battlespriteManager.getBattleSprite().getType();
        try {
            if(type == BattleSprite.BattleSpriteType.ALLY && weaponPalettesPath.getNameCount() > 0 && weaponPath.getNameCount() > 0) {
                weaponspriteManager.importDisassemblyAndPalettes(weaponPalettesPath, weaponPath);
                Console.logger().info("Weapon imported");
            }
        } catch (Exception e) {
            Console.logger().severe("ERROR Weapon could not be loaded : " + e);
        }
        BattleSpriteAnimationPackage pckg = new BattleSpriteAnimationPackage(battlespriteManager.getBattleSprite());
        battlespriteAnimation = battleSpriteAnimationDisassemblyProcessor.importDisassembly(animationPath, pckg);
        Console.logger().info("Animation successfully imported from : " + animationPath);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path filePath, BattleSpriteAnimation battlespriteanimation) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.battlespriteAnimation = battlespriteanimation;
        BattleSpriteAnimationPackage pckg = new BattleSpriteAnimationPackage(battlespriteanimation.getBattleSprite());
        battleSpriteAnimationDisassemblyProcessor.exportDisassembly(filePath, battlespriteanimation, pckg);
        Console.logger().info("Animation successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }

    public BattleSpriteAnimation getBattleSpriteAnimation() {
        return battlespriteAnimation;
    }

    public void setBattleSpriteAnimation(BattleSpriteAnimation battlespriteanimation) {
        this.battlespriteAnimation = battlespriteanimation;
    }

    public Background getBackground() {
        return battleSceneManager.getBackground();
    }

    public Ground getGround() {
        return battleSceneManager.getGround();
    }

    public BattleSprite getBattleSprite() {
        return battlespriteManager.getBattleSprite();
    }

    public Palette[] getWeaponPalettes() {
        return weaponspriteManager.getPalettes();
    }

    public WeaponSprite getWeaponsprite() {
        return weaponspriteManager.getWeaponsprite();
    }
}
