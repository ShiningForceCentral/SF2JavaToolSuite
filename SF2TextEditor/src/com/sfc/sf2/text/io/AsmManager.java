/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.io;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author wiz
 */
public class AsmManager {
    
    public final String HUFFMANTREEOFFSETS_FILENAME = "huffmantreeoffsets.bin";
    public final String HUFFMANTREES_FILENAME = "huffmantrees.bin";
    public final String TEXTBANK_FILENAME = "textbankXX.bin";  
    public final int INVESTIGATION_LINE_BASE_INDEX = 0x1A7;
    
    public String[] importAsm(String path, String[] inputscript){
        System.out.println("com.sfc.sf2.text.io.AsmManager.importAsm() - Importing ASM ...");
        String[] outputscript = inputscript;
        int textCursor=0;
        int baseIndex=-1;
        int scanLineNumber = 0;
        try{
            File file = new File(path);
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                String line = scan.nextLine();
                scanLineNumber++;
                try{
                    line = removeName(line).trim(); 
                    if(line.trim().startsWith("txt")&&line.contains(";")&&line.contains("\"")){
                        String indexString = line.substring(3, line.indexOf(';')).trim();
                        String textLine = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
                        int index = 0;
                        if(indexString.startsWith("$")){
                            index = Integer.parseInt(indexString.substring(1), 16);
                        }else{
                            index = Integer.parseInt(indexString);
                        }
                        outputscript = updateScript(textLine, index, outputscript);
                    }else if(line.trim().startsWith("textCursor")){
                        String indexString = line.trim().substring(line.trim().indexOf(" ")).trim();
                        int index = 0;
                        if(indexString.startsWith("$")){
                            index = Integer.parseInt(indexString.substring(1), 16);
                        }else{
                            index = Integer.parseInt(indexString);
                        }
                        textCursor = index;
                        System.out.println("textCursor=$"+Integer.toHexString(textCursor));
                    }else if(
                            (
                            line.trim().startsWith("nextSingleText")
                            ||line.trim().startsWith("nextText")
                            ||line.trim().startsWith("nextSingleTextVar")
                            ||line.trim().startsWith("nextTextVar")
                            )
                            &&line.contains(";")&&line.contains("\"")){
                        String textLine = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
                        outputscript = updateScript(textLine, textCursor, outputscript);
                        textCursor++;
                    }else if(line.trim().startsWith("move.w")&&line.contains(",d3")){
                        String indexString = line.substring(line.indexOf('#')+1, line.indexOf(',')).trim();
                        int index = 0;
                        if(indexString.startsWith("$")){
                            index = Integer.parseInt(indexString.substring(1), 16);
                        }else{
                            index = Integer.parseInt(indexString);
                        }
                        baseIndex = index;
                        System.out.println("baseIndex=$"+Integer.toHexString(baseIndex));
                    }else if(line.trim().startsWith("msDesc")&&line.contains(";")&&line.contains("\"")&&baseIndex!=-1){
                        String[] params = line.substring(7,line.indexOf(";")).split(",");
                        String investigationLineIndexString = params[2].trim();
                        int investigationLineIndex = 0;
                        if(investigationLineIndexString.startsWith("$")){
                            investigationLineIndex = Integer.parseInt(investigationLineIndexString.substring(1), 16);
                        }else{
                            investigationLineIndex = Integer.parseInt(investigationLineIndexString);
                        }
                        String investigationLine = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));
                        outputscript = updateScript(investigationLine, INVESTIGATION_LINE_BASE_INDEX+investigationLineIndex, outputscript);
                        String secondLine = scan.nextLine();
                        scanLineNumber++;
                        if(secondLine.trim().startsWith(";")&&secondLine.contains("\"")){
                            String descriptionLineIndexString = params[3].trim();
                            String descriptionLine = secondLine.substring(secondLine.indexOf("\"")+1,secondLine.lastIndexOf("\""));
                            int descriptionLineIndex = 0;
                            if(descriptionLineIndexString.startsWith("$")){
                                descriptionLineIndex = Integer.parseInt(descriptionLineIndexString.substring(1), 16);
                            }else{
                                descriptionLineIndex = Integer.parseInt(descriptionLineIndexString);
                            }
                            outputscript = updateScript(descriptionLine, baseIndex+descriptionLineIndex, outputscript);
                        }
                    }
                }catch(Exception e){
                    System.err.println("com.sfc.sf2.text.io.AsmManager.importAsm() - Error while parsing line at index "+scanLineNumber+" : "+e);
                    e.printStackTrace();
                }
            }          
        }catch(Exception e){
             System.err.println("com.sfc.sf2.text.io.AsmManager.importAsm() - Error while parsing line at index "+scanLineNumber+" : "+e);
             e.printStackTrace();
        }    
        for(int i=0;i<outputscript.length;i++){
            if(outputscript[i]==null){
                outputscript[i]="";
            }
        }
        System.out.println("com.sfc.sf2.text.io.AsmManager.importAsm() - ASM imported.");
        return outputscript;
    }

    
    private String[] updateScript(String line, int index, String[] script){
        if(index<script.length){
            script[index] = line;
        }else{
            String[] newScript = new String[index+1];
            System.arraycopy(script, 0, newScript, 0, script.length);
            newScript[index] = line;
            script = newScript;
        }   
        System.out.println(getLineIndexString(index)+"="+line);
        return script;
    }
    
    
    private String getLineIndexString(int index){
        String indexString = Integer.toHexString(index);
        while(indexString.length()<4){
            indexString="0"+indexString;
        }
        return indexString;
    }
    
    private String removeName(String line){
        if(line.indexOf(":")>=0 && 
                (
                line.indexOf(":")<line.indexOf(" ")
                || line.indexOf(":")<line.indexOf("\t")
                || line.indexOf(":")<line.indexOf(";")
                )
                    ){
            line = line.substring(line.indexOf(":")+1);
        }
        return line;
    }
    
}
