/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.spellGraphic;

import com.sfc.sf2.background.Background;
import com.sfc.sf2.battlescene.BattleSceneManager;
import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.FileFormat;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.graphics.Tileset;
import com.sfc.sf2.graphics.TilesetManager;
import com.sfc.sf2.ground.Ground;
import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.spellGraphic.io.InvocationDisassemblyProcessor;
import com.sfc.sf2.spellGraphic.io.InvocationMetadataProcessor;
import com.sfc.sf2.spellGraphic.io.InvocationPackage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author TiMMy
 */
public class InvocationGraphicManager extends AbstractManager {
    
    private InvocationGraphic invocationGraphic;
    private Background background;
    private Ground ground;

    @Override
    public void clearData() {
        if (invocationGraphic != null) {
            invocationGraphic.clearIndexedColorImage();
        }
        if (background != null) {
            background.getTileset().clearIndexedColorImage(true);
        }
        if (ground != null) {
            ground.getTileset().clearIndexedColorImage(true);
        }
        invocationGraphic = null;
        background = null;
        ground = null;
    }
       
    public InvocationGraphic importDisassembly(Path filePath, Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath) throws IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importDisassembly");
        BattleSceneManager battleSceneManager = new BattleSceneManager();
        battleSceneManager.importDisassembly(backgroundPath, groundBasePalettePath, groundPalettePath, groundPath);
        background = battleSceneManager.getBackground();
        ground = battleSceneManager.getGround();
        InvocationPackage pckg = new InvocationPackage(PathHelpers.filenameFromPath(filePath));
        invocationGraphic = new InvocationDisassemblyProcessor().importDisassembly(filePath, pckg);
        Console.logger().info("Invocation with " + invocationGraphic.getFrames().length + " frames successfully imported from : " + filePath);
        Console.logger().finest("EXITING importDisassembly");
        return invocationGraphic;
    }
    
    public void exportDisassembly(Path filePath, InvocationGraphic invocationGraphic) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.invocationGraphic = invocationGraphic;
        new InvocationDisassemblyProcessor().exportDisassembly(filePath, invocationGraphic, null);
        Console.logger().info("Invocation successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public InvocationGraphic importImage(Path filePath, Path backgroundPath, Path groundBasePalettePath, Path groundPalettePath, Path groundPath) throws RawImageException, IOException, DisassemblyException, AsmException {
        Console.logger().finest("ENTERING importImage");
        BattleSceneManager battleSceneManager = new BattleSceneManager();
        battleSceneManager.importDisassembly(backgroundPath, groundBasePalettePath, groundPalettePath, groundPath);
        background = battleSceneManager.getBackground();
        ground = battleSceneManager.getGround();
        Path dir = filePath.getParent();
        String name = getImageName(filePath.getFileName().toString());
        FileFormat format = FileFormat.getFormat(filePath);
        if (format != FileFormat.PNG || format != FileFormat.GIF) {
            format = FileFormat.ANY_IMAGE;
        }
        List<Tileset> frames = new ArrayList<>();
        File[] files = FileHelpers.findAllFilesInDirectory(dir, name+"-frame-", format);
        TilesetManager tilesetManager = new TilesetManager();
        for (File f : files) { 
            Tileset frame = tilesetManager.importImage(f.toPath(), true);
            frames.add(frame);
        }
        if (frames.isEmpty()) {
            throw new DisassemblyException("ERROR : No frames found for pattern : " + dir.resolve(name+"-frame-"+"XX"+format.getExt()));
        }
        invocationGraphic = new InvocationGraphic(name, frames.toArray(new Tileset[frames.size()]));
        Console.logger().info("Invocation with " + invocationGraphic.getFrames().length + " frames successfully imported from : " + filePath);
        Path metaPath = dir.resolve(name+FileFormat.META.getExt());
        try {
            new InvocationMetadataProcessor().importMetadata(metaPath, invocationGraphic);
            Console.logger().info("Invocation meta successfully imported from : " + metaPath);
        } catch (Exception e) {
            Console.logger().log(Level.SEVERE, "Invocation metadata could not be loaded from : " + metaPath);
        }
        Console.logger().finest("EXITING importImage");
        return invocationGraphic;
    }
    
    public void exportImage(Path filePath, InvocationGraphic invocationGraphic) throws RawImageException, IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportImage");
        Path dir = filePath.getParent();
        String name = getImageName(filePath.getFileName().toString());
        FileFormat format = FileFormat.getFormat(filePath);
        if (format == FileFormat.UNKNOWN) {
            format = FileFormat.ANY_IMAGE;
        }
        
        this.invocationGraphic = invocationGraphic;
        Tileset[] frames = this.invocationGraphic.getFrames();
        TilesetManager tilesetManager = new TilesetManager();
        for (int i = 0; i < frames.length; i++) {
            Path framePath = dir.resolve(name + "-frame-" + String.format("%02d", i) + format.getExt());
            tilesetManager.exportImage(framePath, frames[i]);
        }
        Console.logger().info("Invocation successfully exported to : " + filePath.resolve(name));
        Path metaPath = dir.resolve(name + FileFormat.META.getExt());
        try {
            new InvocationMetadataProcessor().exportMetadata(metaPath, invocationGraphic);
        } catch (Exception e) {
            Console.logger().log(Level.SEVERE, "Invocation metadata could not be loaded from : " + metaPath);
        }
        Console.logger().info("Invocation meta successfully exported to : " + metaPath);
        Console.logger().finest("EXITING exportImage");
    }
    
    private String getImageName(String filename) {
        int dotIndex = filename.indexOf('.');
        int frameIndex = filename.indexOf("-frame");
        int dashIndex = filename.indexOf("-");
        if (frameIndex >= 0) {
            return filename.substring(0, frameIndex);
        } else if (dashIndex >= 0) {
            return filename.substring(0, dashIndex);
        } else if (dotIndex >= 0) {
            if (dotIndex > filename.lastIndexOf("\\")) {
                return filename.substring(0, dotIndex);
            }
        }
        return filename;
    }

    public InvocationGraphic getInvocationGraphic() {
        return invocationGraphic;
    }

    public void setInvocationGraphic(InvocationGraphic invocationGraphic) {
        this.invocationGraphic = invocationGraphic;
    }

    public Background getBackground() {
        return background;
    }

    public Ground getGround() {
        return ground;
    }
}
