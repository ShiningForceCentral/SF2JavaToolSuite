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
import com.sfc.sf2.helpers.PathHelpers;
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
    private final BattleSceneManager battleSceneManager = new BattleSceneManager();
    private final WeaponSpriteManager weaponSpriteManger = new WeaponSpriteManager();
    
    private BattleSpriteAnimation battlespriteAnimation;

    @Override
    public void clearData() {
        battleSceneManager.clearData();
        weaponSpriteManger.clearData();
        if (battlespriteAnimation != null) {
            battlespriteAnimation.getBattleSprite().clearIndexedColorImage(true);
            battlespriteAnimation = null;
        }
    }
       
    public void importDisassembly(Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath, Path battlespritePath, Path weaponPalettesPath, Path weaponPath, Path animationPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        try {
            battleSceneManager.importDisassembly(backgroundPath, groundBasePalettePath, groundPalettePath, groundPath);
        } catch (Exception e) {
            Console.logger().severe("ERROR Battle Scene could not be imported : " + e);
        }
        BattleSprite battleSprite = new BattleSpriteManager().importDisassembly(battlespritePath);
        BattleSpriteType type = battleSprite.getType();
        try {
            if(type == BattleSprite.BattleSpriteType.ALLY && weaponPalettesPath.getNameCount() > 0 && weaponPath.getNameCount() > 0) {
        weaponSpriteManger.importDisassemblyAndPalettes(weaponPalettesPath, weaponPath);
            }
        } catch (Exception e) {
            Console.logger().severe("ERROR Weapon could not be loaded : " + e);
        }
        BattleSpriteAnimationPackage pckg = new BattleSpriteAnimationPackage(PathHelpers.filenameFromPath(animationPath), battleSprite);
        battlespriteAnimation = new BattleSpriteAnimationDisassemblyProcessor().importDisassembly(animationPath, pckg);
        Console.logger().info("Animation successfully imported from : " + animationPath);
        Console.logger().finest("EXITING importDisassembly");
    }
    
    public void exportDisassembly(Path filePath, BattleSpriteAnimation battleSpriteAnimation) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.battlespriteAnimation = battleSpriteAnimation;
        BattleSpriteAnimationPackage pckg = new BattleSpriteAnimationPackage(battleSpriteAnimation.getName(), battleSpriteAnimation.getBattleSprite());
        new BattleSpriteAnimationDisassemblyProcessor().exportDisassembly(filePath, battleSpriteAnimation, pckg);
        Console.logger().info("Animation successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public void importBattleScene(Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importBattleScene");
        battleSceneManager.importDisassembly(backgroundPath, groundBasePalettePath, groundPalettePath, groundPath);
        Console.logger().info("Battle Scene successfully imported from : " + backgroundPath);
        Console.logger().finest("EXITING importBattleScene");
    }
    
    public void importWeapon(Path weaponPalettesPath, Path weaponPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importWeapon");
        weaponSpriteManger.importDisassemblyAndPalettes(weaponPalettesPath, weaponPath);
        Console.logger().info("Weapon successfully imported from : " + weaponPath);
        Console.logger().finest("EXITING importWeapon");
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

    public Palette[] getWeaponPalettes() {
        return weaponSpriteManger.getPalettes();
    }

    public WeaponSprite getWeaponsprite() {
        return weaponSpriteManger.getWeaponsprite();
    }
}
