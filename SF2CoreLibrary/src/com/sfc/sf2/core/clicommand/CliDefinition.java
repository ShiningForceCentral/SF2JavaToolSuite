/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.core.clicommand;

/**
 *
 * @author TiMMy
 */
public record CliDefinition(CliCommandID id, String longCommand, String shortCommand, String helpInfo) {
    public enum CliCommandID {
        UNKNOWN,
        HEADLESS,
        HELP,
        IMPORT,
        EXPORT,
        IMPORT_IMAGE,
        EXPORT_IMAGE,
    }
    
    private static CliDefinition[] CLI_DEFINITIONS = new CliDefinition[] {
        new CliDefinition(CliCommandID.HEADLESS, "--headless", "-c", "<> - Runs the app command-line only"),
        new CliDefinition(CliCommandID.HELP, "--help", "-h", "<> - Prints help info"),
        new CliDefinition(CliCommandID.IMPORT, "--import", "-i", "<filePath> - Imports the app's primary data type from disassembly (.asm, .bin, or .txt)"),
        new CliDefinition(CliCommandID.EXPORT, "--export", "-e", "<filePath> - Exports the app's primary data type from disassembly (.asm, .bin, or .txt). Must import data first"),
        new CliDefinition(CliCommandID.IMPORT_IMAGE, "--import_image", null, "<imagePath> - Imports the app's primary data type from image (.png or .gif)"),
        new CliDefinition(CliCommandID.EXPORT_IMAGE, "--export_image", null, "<imagePath> - Exports the app's primary data type from image (.png or .gif). Must import data first"),
    };
    
    public static CliCommandID commandFromString(String cmd) {
        if (cmd.charAt(1) == '-') {
            cmd = cmd.substring(2);
        } else if (cmd.charAt(1) == '-') {
            cmd = cmd.substring(1);
        }
        cmd = cmd.toLowerCase();
        for (int i = 0; i < CLI_DEFINITIONS.length; i++) {
            if (cmd.equals(CLI_DEFINITIONS[i].longCommand()) || cmd.equals(CLI_DEFINITIONS[i].shortCommand())) {
                return CLI_DEFINITIONS[i].id();
            }
        }
        return CliCommandID.UNKNOWN;
    }
    
    public static void PrintHelpString() {
        System.out.println(String.format("%s cli help:"));
        for (int i = 0; i < CLI_DEFINITIONS.length; i++) {
            CliDefinition def = CLI_DEFINITIONS[i];
            System.out.print(def.id());
            System.out.print(": ");
            System.out.print(def.longCommand());
            if (def.shortCommand() != null) {
                System.out.print(String.format(" (%s) - ", def.shortCommand()));
            } else {
                System.out.print(" - ");
            }
            if (def.helpInfo() != null) {
                System.out.print(def.helpInfo());
            }
            System.out.print("\n");
        }
    }
}

