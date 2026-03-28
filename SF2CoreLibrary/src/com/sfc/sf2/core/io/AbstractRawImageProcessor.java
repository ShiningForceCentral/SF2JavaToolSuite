/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.io;

import com.sfc.sf2.core.gui.controls.Console;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 *
 * @author TiMMy
 * @param <TType> The type of data being loaded
 * @param <TPackage> The input data required to load the TType
 */
public abstract class AbstractRawImageProcessor<TType extends Object, TPackage extends Object> {
    
    public TType importRawImage(Path filePath, TPackage pckg) throws IOException, RawImageException {
        FileFormat fileFormat = FileFormat.getFormat(filePath);
        Console.logger().finest("ENTERING importRawImage : " + filePath + ". Format : " + fileFormat);
        File imageFile = filePath.toFile();
        if (!imageFile.exists()) {
            throw new FileNotFoundException("Image file not found : " + filePath);
        }
        BufferedImage image = ImageIO.read(filePath.toFile());
        ColorModel cm = image.getColorModel();
        if (!(cm instanceof IndexColorModel)) {
            throw new RawImageException("ERROR Image must be in an indexed color format. Format : " + cm.getColorSpace().toString());
        }
        IndexColorModel icm = (IndexColorModel)cm;
        WritableRaster raster = image.getRaster();
        TType item = parseImageData(raster, icm, pckg);
        Console.logger().finest("EXITING importRawImage");
        return item;
    }
    
    protected abstract TType parseImageData(WritableRaster raster, IndexColorModel icm, TPackage pckg) throws RawImageException;
    
    public void exportRawImage(Path filePath, TType item, TPackage pckg) throws IOException, RawImageException {
        FileFormat fileFormat = FileFormat.getFormat(filePath);
        Console.logger().finest("ENTERING exportRawImage : " + filePath + ". Format : " + fileFormat);
        BufferedImage image = packageImageData(item, pckg);
        ColorModel cm = image.getColorModel();
        if (!(cm instanceof IndexColorModel)) {
            throw new RawImageException("ERROR Image must be in an indexed color format. Format : " + cm.getColorSpace().toString());
        }
        File outputfile = filePath.toFile();
        ImageIO.write(image, fileFormat.getName(), outputfile);
        Console.logger().finest("EXITING exportRawImage");
    }

    protected abstract BufferedImage packageImageData(TType item, TPackage pckg) throws RawImageException;
}
