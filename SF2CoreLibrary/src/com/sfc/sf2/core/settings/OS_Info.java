/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import java.nio.file.Path;

/**
 *
 * @author timmy
 */
public enum OS_Info {
    UNKNOWN,
    WIN,
    MAC,
    LINUX;
    
    private static OS_Info CURRENT_OS = OS_Info.UNKNOWN;
    
    public static OS_Info getOS() {
        if (CURRENT_OS == OS_Info.UNKNOWN) {
            String os = System.getProperty("os.name");
            CURRENT_OS = UNKNOWN;
            if (os.contains("mac") || os.contains("darwin")) {
                CURRENT_OS = MAC;
            } else if (os.contains("win")) {
                CURRENT_OS = WIN;
            } else if (os.contains("nux")) {
                CURRENT_OS = LINUX;
            }
        }
        
        return CURRENT_OS;
    }
    
    public static Path getUserDataPath() {
        Path path;
        switch (getOS()) {
            case WIN:
                path = Path.of(System.getenv("APPDATA"));
                break;
            default:
                path = Path.of(System.getProperty("user.home"));
                break;
        }
        path = path.resolve("SF2");
        return path;
    }

    @Override
    public String toString() {
        switch (this) {
            case WIN:
                return "Windows";
            case MAC:
                return "MacOS";
            case LINUX:
                return "LINUX";
            default:
                return "Unknown";
        }
    }
}
