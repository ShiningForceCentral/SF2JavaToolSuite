/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.text.compression;

import com.sfc.sf2.core.gui.controls.Console;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author wiz
 */
public class TextDecoder {

    private byte previousSymbol;
    private int stringByteCounter;
    private int stringBitCounter;

    private short[] huffmanTreeOffsets;
    private HuffmanTree[] trees;

    public void parseOffsets(byte[] data) {
        huffmanTreeOffsets = new short[data.length / 2];
        ByteBuffer wrappedData = ByteBuffer.wrap(data);
        wrappedData.asShortBuffer().get(huffmanTreeOffsets);
        /*Console.logger().finest("com.sfc.sf2.text.compression.HuffmanTree.parseOffsets() - huffmanTreeOffsets : "
                + "\n" + Arrays.toString(huffmanTreeOffsets));*/
        trees = new HuffmanTree[huffmanTreeOffsets.length];
    }

    public void parseTrees(byte[] data) {
        for (int i = 0; i < huffmanTreeOffsets.length; i++) {
            if (huffmanTreeOffsets[i] == -1) {
                trees[i] = null;
            } else {
                trees[i] = new HuffmanTree(i, data, huffmanTreeOffsets[i]);
            }
        }
        logTrees();
    }

    private void logTrees() {
        /*for(int i = 0;i<trees.length;i++){
            if(trees[i] != null){
                Console.logger().finest(trees[i].toString());
            }else{
                Console.logger().finest("No tree for index " + i);
            }
        }*/
    }

    public String[] parseTextbank(byte[] data, int textbankIndex) {
        return parseTextbank(data, textbankIndex, 256);
    }

    public String[] parseTextbank(byte[] data, int textbankIndex, int linesToParse) {
        short bankPointer = 0;
        String[] textbankStrings = new String[linesToParse];
        int i;
        for (i = 0; i < linesToParse; i++) {
            String s = parseString(data, (short)(bankPointer + 1));
            String stringIndex = Integer.toString(textbankIndex * 256 + i, 16).toUpperCase();
            while (stringIndex.length() < 4) {
                stringIndex = "0" + stringIndex;
            }
            //Console.logger().finest("$"+stringIndex+"("+(data[bankPointer]&0xFF)+")="+s);
            //Console.logger().finest(Arrays.toString(Arrays.copyOfRange(data,bankPointer+1,(bankPointer+data[bankPointer]+1))));
            textbankStrings[i] = s;
            bankPointer += (data[bankPointer]&0xFF) + 1;
            if (bankPointer + 1 >= data.length) {
                textbankStrings = Arrays.copyOfRange(textbankStrings, 0, i + 1);
                break;
            }
        }
        return textbankStrings;
    }

    private String parseString(byte[] data, short offset) {
        StringBuilder sb = new StringBuilder();
        StringBuilder bitsString = new StringBuilder();
        previousSymbol = (byte)0xFE;
        stringByteCounter = 0;
        stringBitCounter = 0;
        while (true) {
            byte symbol = parseNextSymbol(trees[(int)previousSymbol&0xFF], data, offset, bitsString);
            if ((symbol&0xFF) == 0xFE) {  //End of line
                //Console.logger().finest(sb.toString());
                break;
            } else {
                if ((previousSymbol&0xFF) == 0xFC || (previousSymbol&0xFF) == 0xFD) {  //{NAME;x} and {COLOR;x} tags
                    sb.append(";").append(Integer.toString((int)symbol)).append("}");
                } else {
                    sb.append(Symbols.symbolToString(symbol&0xFF));
                }
            }
            previousSymbol = symbol;
        }
        //Console.logger().finest(bitsString.toString());
        return sb.toString();
    }

    private byte parseNextSymbol(HuffmanTree huffmanTree, byte[] data, short offset, StringBuilder bitsString) {
        byte symbol;
        int symbolsSkipCounter = 0;
        int nonLeafCounter;
        int leafCounter;
        byte treeByte = huffmanTree.tree[0];
        int treeByteCounter = 0;
        int treeBitCounter = 0;
        byte stringByte;
        bitsString.append("[");
        while (true) {
            if (treeBitCounter > 7) {
                treeByteCounter++;
                treeByte = huffmanTree.tree[treeByteCounter];
                treeBitCounter = 0;
            }
            int treeBit = (treeByte >> (7 - treeBitCounter))&1;
            treeBitCounter++;
            if (treeBit == 0) {
                if (stringBitCounter > 7) {
                    stringByteCounter++;
                    stringBitCounter = 0;
                }
                stringByte = data[offset + stringByteCounter];
                int stringBit = (stringByte >> (7 - stringBitCounter))&1;
                stringBitCounter++;
                if (stringBit == 0) {
                    bitsString.append("0");
                } else {
                    nonLeafCounter = 0;
                    leafCounter = 0;
                    while (leafCounter <= nonLeafCounter) {
                        if (treeBitCounter > 7) {
                            treeByteCounter++;
                            treeByte = huffmanTree.tree[treeByteCounter];
                            treeBitCounter = 0;
                        }
                        int bit = (treeByte >> (7 - treeBitCounter))&1;
                        treeBitCounter++;
                        if (bit == 0) {
                            nonLeafCounter++;
                        } else {
                            leafCounter++;
                        }
                    }
                    symbolsSkipCounter += leafCounter;
                    bitsString.append("1");
                }
            } else {
                break;
            }
        }
        bitsString.append("] ");
        symbol = huffmanTree.symbols[huffmanTree.symbols.length - 1 - symbolsSkipCounter];
        return symbol;
    }
}
