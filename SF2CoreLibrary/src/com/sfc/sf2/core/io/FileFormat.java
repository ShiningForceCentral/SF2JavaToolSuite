/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import com.sfc.sf2.helpers.FileHelpers;
import com.sfc.sf2.helpers.PathHelpers;
import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author TiMMy
 */

public enum FileFormat {
    //Helpers
    ALL_BATTLE_SPRITES_FORMAT(-3),
    ANY_ASSEMBLY(-2),
    ANY_IMAGE(-1),
    //Default
    UNKNOWN(0),
    //Binay
    BIN(1),
    ASM(2),
    //Image
    PNG(3),
    GIF(4),
    //Metadata
    META(5),
    TXT(6);
    
    private final int format;

    private FileFormat(int format) {
        this.format = format;
    }

    public int getFormat() {
        return format;
    }
    
    /**
     *
     * @return a file extension with the dot. i.e. ".png" or ".gif"
     */
    public String getExt() {
        return "."+getName();
    }
    
    /**
     *
     * @return a file extension without the dot. i.e. "png" or "gif"
     * Required for exporting raw images
     */
    public String getName() {
        switch (this) {
            case BIN:
                return "bin";
            case ASM:
            case ANY_ASSEMBLY:
                return "asm";
            case PNG:
            case ANY_IMAGE:
            case ALL_BATTLE_SPRITES_FORMAT:
                return "png";
            case GIF:
                return "gif";
            case META:
                return "meta";
            case TXT:
                return "txt";
            default:
                return "unkn";
        }
    }
    
    public static FileFormat getFormat(Path filePath) {
        return getFormat(PathHelpers.extensionFromPath(filePath));
    }
    
    public static FileFormat getFormat(File file) {
        return getFormat(FileHelpers.getExtension(file));
    }
    
    public static FileFormat getFormat(String extension) {
        if (extension == null || extension.length() == 0) return UNKNOWN;
        if (extension.charAt(0) == '.') {
            extension = extension.substring(1);
        }
        switch (extension) {
            case "bin":
                return BIN;
            case "asm":
                return ASM;
            case "png":
                return PNG;
            case "gif":
                return GIF;
            case "meta":
                return META;
            case "txt":
                return TXT;
            default:
                return UNKNOWN;
        }
    }
    
    public FileFormatFilter getFileFilter(String prefix) {
        return new FileFormatFilter(this, prefix);
    }
    
    public FileChooserFilter getFilehooserFilter() {
        return new FileChooserFilter(this);
    }
    
    private class FileFormatFilter implements java.io.FileFilter {

        private final FileFormat format;
        private final String prefix;

        public FileFormatFilter(FileFormat format, String prefix) {
            this.format = format;
            this.prefix = prefix;
        }
        
        @Override
        public boolean accept(File f) {
            if (!f.getName().startsWith(prefix)) {
                return false;
            }
            FileFormat format = FileFormat.getFormat(f);
            switch (this.format) {
                case ANY_IMAGE:
                    return format == PNG || format == GIF;
                default:
                    return this.format == format;
            }
        }
    }
    
    private class FileChooserFilter extends javax.swing.filechooser.FileFilter {

        private final FileFormat format;

        public FileChooserFilter(FileFormat format) {
            this.format = format;
        }
        
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            FileFormat format = FileFormat.getFormat(f);
            switch (this.format) {
                case ANY_IMAGE:
                    return format == PNG || format == GIF;
                case ANY_ASSEMBLY:
                    return format == BIN || format == ASM;
                case ALL_BATTLE_SPRITES_FORMAT:
                    return format == PNG || format == GIF || format == BIN || format == META;
                default:
                    return this.format == format;
            }
        }

        @Override
        public String getDescription() {
            switch (format) {
                case BIN:
                    return "Disassembly (.bin)";
                case ASM:
                    return "Assembly code (.asm)";
                case ANY_ASSEMBLY:
                    return "Assembly formats (.bin or .asm)";
                case PNG:
                    return "PNG (.png)";
                case GIF:
                    return "GIF (.gif)";
                case ANY_IMAGE:
                    return "Image files (.png or .gif)";
                case META:
                    return "Metadata files (.meta)";
                case TXT:
                    return "Text files (.txt)";
                case ALL_BATTLE_SPRITES_FORMAT:
                    return "Battle sprites formats (.png, .gif, .bin, .meta)";
                default:
                    return "All files";
            }
        }
    }
}
