/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import com.sfc.sf2.core.settings.CoreSettings;
import com.sfc.sf2.core.settings.SettingsManager;
import com.sfc.sf2.core.gui.controls.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JOptionPane;

/**
 *
 * @author TiMMy
 */
public class PathHelpers {
    
    private static final CoreSettings coreSettings = SettingsManager.getSettingsStore("core");
    
    public static Path getApplicationpath() {
        return Path.of(System.getProperty("user.dir"));
    }
    
    public static Path getBasePath() {
        return Path.of(coreSettings.getBasePath());
    }
    
    public static Path getIncbinPath() {
        return Path.of(coreSettings.getIncbinPath());
    }
    
    public static String filenameFromPath(Path path) {
        String name = path.getFileName().toString();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) {
            return name;
        } else {
            return name.substring(0, dotIndex);
        }
    }
    
    public static String extensionFromPath(Path path) {
        String name = path.getFileName().toString();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        } else {
            return name.substring(dotIndex+1);
        }
    }
    
    public static File getNearestValidParent(Path path) {
        if (!path.isAbsolute()) {
            path = getBasePath().resolve(path);
        }
        return getNearestValidParent(path.toFile());
    }
    
    public static File getNearestValidParent(File file) {
        if (file.exists()) return file;
        File parent = file.getParentFile();
        if (parent == null) return file;
        return getNearestValidParent(parent);
    }
    
    public static boolean createPathIfRequred(Path path) {
        File file = path.toFile();
        if (file.exists()) {
            return true;    //File/Folder exists
        }
        boolean isFile = path.getFileName().toString().lastIndexOf('.') != -1;
        if (isFile) {
            file = file.getParentFile();
            if (file.exists()) {
                return true;    //Parent folder exists
            }
        }
        int ret = JOptionPane.showConfirmDialog(null, "Folder '" + file.toString() + "'does not exist.\nDo you want to create this folder?", "Directly not found", JOptionPane.OK_CANCEL_OPTION);
        if (ret == JOptionPane.YES_OPTION) {
            try {
                Files.createDirectories(file.toPath());
                Console.logger().info("Folder created at " + file);
                return true;
            } catch (IOException ex) {
                System.getLogger(PathHelpers.class.getName()).log(System.Logger.Level.ERROR, (String)null, ex);
                Console.logger().info("ERROR: Folder could not be created at " + file);
                return false;
            }
        }
        Console.logger().info("User decided not to create folder at " + file);
        return false;   //Do not save file
    }
    
    public static Path replaceExtension(Path filePath, String extension) {
        if (extension.startsWith(".")) {
            String newPath = filePath.toString();
            newPath = newPath.substring(0, newPath.lastIndexOf('.'));
            newPath += extension;
            return Path.of(newPath);
        } else {
            return Path.of(extension);
        }
    }
}
