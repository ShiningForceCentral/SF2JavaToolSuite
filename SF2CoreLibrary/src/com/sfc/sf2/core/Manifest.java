/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core;

import com.sfc.sf2.core.gui.controls.Console;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.logging.Level;

/**
 *
 * @author TiMMy
 */
public class Manifest {
    
    private static String projectName;
    private static String version;
    
    public static String getProjectName() {
        if (projectName == null) loadData();
        return projectName;
    }
    
    public static String getProjectVersion() {
        if (version == null) loadData();
        return version;
    }
    
    private static void loadData() {
        try {
            InputStream manifestStream = Manifest.class.getResourceAsStream("/META-INF/MANIFEST.MF");
            if (manifestStream != null) {
                java.util.jar.Manifest manifest = new java.util.jar.Manifest(manifestStream);
                Attributes attrs = manifest.getMainAttributes();
                projectName = attrs.getValue("Project-Name");
                version = attrs.getValue("Implementation-Version");
                if (projectName == null) {
                    Console.logger().severe("Project name not found in manifest. (Ignore this if running from IDE).");
                    projectName = loadNameFromProjectFolder();
                }
                if (version == null) {
                    Console.logger().severe("Project version not found in manifest. (Ignore this if running from IDE).");
                    version = loadVersionFromProjectFolder();
                }
            }
        } catch (Exception ex) {
            Console.logger().log(Level.SEVERE, "ERROR Could not load manifest. build.xml requires an update.", ex);
        }
    }
    
    private static String loadNameFromProjectFolder() {
        //Should only be used for running in IDE
        //Probably in editor
        String path = System.getProperty("user.dir");
        String projectName = path.substring(path.lastIndexOf(File.separatorChar)+1);
        if (projectName == null) {
            Console.logger().severe("ERROR Fallback project name not found. Settings will not load correctly.");
            return "SF2UnknownProject";
        } else {
            Console.logger().warning("Project name found : " + projectName);
            return projectName;
        }
    }
    
    private static String loadVersionFromProjectFolder() {
        //Should only be used for running in IDE
        File manifest = new File(System.getProperty("user.dir") + File.separator + "version.properties");
        if (manifest.exists()) {
            try {
                //Probably running in editor
                BufferedReader reader = new BufferedReader(new FileReader(manifest));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("version")) {
                        String version = line.substring(line.indexOf(':')+1).trim();
                        reader.close();
                        Console.logger().warning("Project version found : " + version);
                        return version;
                    }
                }
                reader.close();
            } catch (Exception ex) { }
        }
        Console.logger().severe("ERROR Fallback project version not found.");
        return "0.0-UNKNOWN";
    }
}
