/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.compression;

import com.sfc.sf2.core.gui.controls.Console;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class HuffmanTree {
    
    public int index;
    public byte[] symbols;
    public byte[] tree;

    public HuffmanTree(int index, byte[] data, short offset){
        this.index = index;
        splitTree(data,offset);
        /*Console.logger().finest("sfc.segahr.HuffmanTree.<init>() - Created new HuffmanTree with following data :"
                + "\nsymbols : " + Arrays.toString(symbols)
                + "\ntree : " + Arrays.toString(tree));*/
    }
    
    private void splitTree(byte[] data, short offset){
        int nonLeafCounter = 0;
        int leafCounter = 0;
        int treeByteCounter = 0;
        byte workByte = data[offset];
        int workByteBitCounter = 0;
        while(leafCounter<=nonLeafCounter){
            if(workByteBitCounter>7){
                treeByteCounter++;
                workByte = data[offset+treeByteCounter];
                workByteBitCounter = 0;
            }
            int bit = (workByte >> (7-workByteBitCounter)) & 1;
            if(bit == 0){
                nonLeafCounter++;
            }else{
                leafCounter++;
            }
            workByteBitCounter++;
        }
        symbols = new byte[leafCounter];
        System.arraycopy(data,offset-leafCounter,symbols,0,leafCounter);
        tree = new byte[treeByteCounter+1];
        System.arraycopy(data,offset,tree,0,treeByteCounter+1);
    }
    
    @Override
    public String toString(){
        return "HuffmanTree " + index + " for symbol \'" + Symbols.asciiToSymbol(index) + "\' : "
                + "\n\tSymbols (" + symbols.length + ") : " + printSymbols()
                + "\n\tTree : " + printTree();
    }
    
    private String printSymbols(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<symbols.length;i++){
            sb.append(symbols[i]).append(":\'").append(Symbols.asciiToSymbol((int)symbols[i]&0xFF)).append("\' ");
        }
        return sb.toString();
    }
    
    private String printTree(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<tree.length;i++){
            sb.append(String.format("%8s", Integer.toBinaryString(tree[i] & 0xFF)).replace(' ', '0')).append(" ");
        }
        return sb.toString();
    }        
    
}
