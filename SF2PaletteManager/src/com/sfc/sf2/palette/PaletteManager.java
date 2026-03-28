/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.palette;

import com.sfc.sf2.core.AbstractManager;
import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.io.BinaryDisassemblyProcessor;
import com.sfc.sf2.core.io.DisassemblyException;
import com.sfc.sf2.core.io.RawImageException;
import com.sfc.sf2.helpers.PathHelpers;
import com.sfc.sf2.palette.io.PaletteDisassemblyProcessor;
import com.sfc.sf2.palette.io.PalettePackage;
import com.sfc.sf2.palette.io.PaletteRawImageProcessor;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public  class PaletteManager extends AbstractManager {
    
    private Palette palette;

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    @Override
    public void clearData() {
        palette = null;
    }
       
    public Palette importDisassembly(Path filePath, boolean firstColorTransparent) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING importDisassembly");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        palette = new PaletteDisassemblyProcessor().importDisassembly(filePath, pckg);
        Console.logger().info("Palette successfully imported from : " + filePath);
        Console.logger().finest("EXITING importDisassembly");
        return palette;
    }
    
    public Palette[] importDisassemblyFromPartials(Path[] filePaths, int[] offsets, int[] lengths, boolean firstColorTransparent) throws IOException, DisassemblyException {
        byte[][] dataSets = new byte[filePaths.length][];
        Palette[] palettes = new Palette[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            if (i > 0 && filePaths[i].equals(filePaths[i-1])) {
                dataSets[i] = dataSets[i-1];
            } else {
                PalettePackage pckg = new PalettePackage(Integer.toString(i), firstColorTransparent);
                dataSets[i] = new BinaryDisassemblyProcessor().importDisassembly(filePaths[i], null);
            }
            
            byte[] newData = Arrays.copyOfRange(dataSets[i], offsets[i], offsets[i]+lengths[i]);
            palettes[i] = new Palette(Integer.toString(i), PaletteDecoder.decodePalette(newData), true);
        }
        return palettes;
    }
    
    public Palette importDisassemblyFromPartial(Path filePath, int offset, int length, boolean firstColorTransparent) throws IOException, DisassemblyException {
        byte[] data = new BinaryDisassemblyProcessor().importDisassembly(filePath, null);
        byte[] paletteData = Arrays.copyOfRange(data, offset, offset+length);
        return new Palette(PathHelpers.filenameFromPath(filePath), PaletteDecoder.decodePalette(paletteData), true);
    }
    
    public void exportDisassembly(Path filePath, Palette palette) throws IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportDisassembly");
        this.palette = palette;
        PalettePackage pckg = new PalettePackage(palette.getName(), false);
        new PaletteDisassemblyProcessor().exportDisassembly(filePath, palette, pckg);
        Console.logger().info("Palette successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportDisassembly");
    }
    
    public Palette importImage(Path filePath, boolean firstColorTransparent) throws RawImageException, DisassemblyException, IOException {
        Console.logger().finest("ENTERING importImage");
        PalettePackage pckg = new PalettePackage(PathHelpers.filenameFromPath(filePath), firstColorTransparent);
        palette = new PaletteRawImageProcessor().importRawImage(filePath, pckg);
        Console.logger().info("Palette successfully imported from : " + filePath);
        Console.logger().finest("EXITING importImage");
        return palette;
    }
    
    public void exportImage(Path filePath, Palette currentPalette, boolean firstColorTransparent) throws RawImageException, IOException, DisassemblyException {
        Console.logger().finest("ENTERING exportImage");
        palette = currentPalette;
        PalettePackage pckg = new PalettePackage(palette.getName(), firstColorTransparent);
        new PaletteRawImageProcessor().exportRawImage(filePath, palette, pckg);
        Console.logger().info("Palette successfully exported to : " + filePath);
        Console.logger().finest("EXITING exportImage");
    }
}
