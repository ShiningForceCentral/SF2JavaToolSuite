/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle.mapterrain.compression;

import com.sfc.sf2.helpers.BinaryHelpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author wiz
 */
public class BattleTerrainStackDecoder {

    private byte[] inputData;
    private short inputWord = 0;
    private int inputCursor = -2;
    private int inputBitCursor = 16;
    private List<Byte> output = new ArrayList();

    private List<Integer> historyStack = new ArrayList<Integer>(Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));

    public byte[] decode(byte[] input) {
        //Console.logger().finest("decodeStackData");
        //Console.logger().finest("Data length = " + input.length + " bytes.");
        this.inputData = input;
        boolean decodingDone = false;
        short commandBitmap = 0;
        int commandPattern = 0;
        int command = 0;
        short value = 0;
        int copyOffset = 0;
        int copyLength = 0;
        inputCursor = -2;
        inputBitCursor = 16;
        byte[] bytes = null;
        while (!decodingDone) {
            /* Step 1 - Parse command bitmap word */
            commandBitmap = 0;
            for (int i=0; i < 4; i++) {
                commandPattern = getNextCommandPattern();
                commandBitmap = (short) (commandBitmap << 4);
                commandBitmap += commandPattern;
            }
            //Console.logger().finest(String.format("command bitmap = %x", (commandBitmap & 0xFFFF)));

            /* Step 2 - Apply commands on following data */
            for (int i=0; i < 16; i++) {
                command = (commandBitmap >> 15-i) & 1;
                if (command == 0) {
                    /* command 0 : word value built from four 4-bit values taken from history stack */
                    value = getWordValue();
                    //Console.logger().finest(String.format("0 - word value = %x", (value & 0xFFFF)));
                    BinaryHelpers.setWordList(value, output);
                } else {
                    /* command 1 : section copy */
                    copyOffset = getCopyOffset();
                    if (copyOffset == 0) {
                        decodingDone = true;
                        break;
                    }
                    copyLength = getCopyLength();
                    //Console.logger().finest("1 - section copy offset=" + Integer.toHexString(copyOffset & 0xFFFF) + ", length=" + Integer.toHexString(copyLength & 0xFFFF));
                    for (int j = 0; j < copyLength; j++) {
                        output.add(output.get(output.size() - 2*copyOffset));
                        output.add(output.get(output.size() - 2*copyOffset));
                    }
                }

            }

        }

        bytes = new byte[output.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = output.get(i);
        }
        //Console.logger().finest("decodeStackData");
        return bytes;
    }

    private int getNextBit() {
        int bit = 0;
        if (inputBitCursor >= 16) {
            inputBitCursor = 0;
            inputCursor += 2;
            inputWord = BinaryHelpers.getWord(inputData, inputCursor);
        }
        bit = (inputWord >> (15-inputBitCursor)) & 1;
        inputBitCursor++;
        return bit;
    }

    private int getNextCommandPattern() {
        int commandPattern = 0;
        /* input bitstring	==> 4 command bits : 0 = word value, 1 = section copy */
        if (getNextBit() == 0) {
            /* 0 		==> 0000 (4 word values, most frequent pattern, naturally) */
            commandPattern = 0;
        } else {
            if (getNextBit() == 0) {
                if (getNextBit() == 0) {
                    /* 100 		==> 0001 (3 word values, then 1 section copy) */
                    commandPattern = 1;
                } else {
                    /* 101 		==> 0010 (2 word values etc ...) */
                    commandPattern = 2;
                }
            } else {
                if (getNextBit() == 0) {
                    /* 110 		==> 0100 */
                    commandPattern = 4;
                } else {
                    if (getNextBit() == 0) {
                        /* 1110 		==> 1000 */
                        commandPattern = 8;
                    } else {
                        /* 1111 xxxx 	==> xxxx (custom command pattern) */
                        commandPattern = getCustomCommandPattern();
                    }
                }
            }
        }

        return commandPattern;
    }

    private int getCustomCommandPattern() {
        int customPattern = 8 * getNextBit() + 4 * getNextBit() + 2 * getNextBit() + getNextBit();
        return customPattern;
    }

    private short getWordValue() {
        short wordValue = 0;
        for (int i = 0; i < 4; i++) {
            wordValue = (short)(wordValue << 4);
            wordValue += getNextValue();
        }
        return wordValue;
    }

    private int getNextValue() {
        int value = 0;
        int valueIndex = 0;
        /* Initial history stack order : 0 1 2 3 4 5 6 7 8 9 A B C D E F
            input bitstring	==> ouput 4-bit value */
        if (getNextBit() == 0) {
            if (getNextBit() == 0) {
                /* 00 		==> 0 : history index, most recent value */
                valueIndex = 0;
            } else {
                /* 01 		==> 1 : second most recent value */
                valueIndex = 1;
            }
        } else {
            if (getNextBit() == 0) {
                if (getNextBit() == 0) {
                    /* 100 		==> 2 : etc... */
                    valueIndex = 2;
                } else {
                    /* 101 		==> 3 */
                    valueIndex = 3;
                }
            } else {
                if (getNextBit() == 0) {
                    /* 110 		==> 4 */
                    valueIndex = 4;
                } else {
                    /* 111... */
                    if (getNextBit() == 0) {
                        if (getNextBit() == 0) {
                            /* 11100 		==> 5 */
                            valueIndex = 5;
                        } else {
                            /* 11101 		==> 6 */
                            valueIndex = 6;
                        }
                    } else {
                        if (getNextBit() == 0) {
                            /* 11110 		==> 7 */
                            valueIndex = 7;
                        } else {
                            /* 11111... */
                            if (getNextBit() == 0) {
                                if (getNextBit() == 0) {
                                    /* 1111100 	==> 8 */
                                    valueIndex = 8;
                                } else {
                                    /* 1111101 	==> 9 */
                                    valueIndex = 9;
                                }
                            } else {
                                if (getNextBit() == 0) {
                                    /* 1111110 	==> A */
                                    valueIndex = 10;
                                } else {
                                    if (getNextBit() == 0) {
                                        if (getNextBit() == 0) {
                                            /* 111111100 	==> B */
                                            valueIndex = 11;
                                        } else {
                                            /* 111111101 	==> C */
                                            valueIndex = 12;
                                        }
                                    } else {
                                        if (getNextBit() == 0) {
                                            /* 111111110 	==> D */
                                            valueIndex = 13;
                                        } else {
                                            if (getNextBit() == 0) {
                                                /* 1111111110 	==> E */
                                                valueIndex = 14;
                                            } else {
                                                /* 1111111111 	==> F */
                                                valueIndex = 15;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        value = historyStack.get(valueIndex);
        if (valueIndex != 0) {
            historyStack.remove(valueIndex);
            historyStack.add(0, value);
        }
        return value;
    }

    private int getCopyOffset() {
        int offset = 0;
        for (int i = 0; i < 11; i++) {
            offset = offset << 1;
            offset += getNextBit();
        }
        return offset;
    }

    private int getCopyLength() {
        int length = 2;
        while (getNextBit() == 0) {
            if (getNextBit() == 0) {
                length += 2;
            } else {
                length += 1;
                break;
            }
        }
        return length;
    }
    
    private static final List<String> historyBitStrings = new ArrayList<String>(Arrays.asList(new String[]{"00", "01", "100", "101", "110", "11100", "11101", "11110", "1111100",
        "1111101", "1111110", "111111100", "111111101", "111111110", "1111111110", "1111111111"}));
    private static final int MAX_COPY_OFFSET = 2047;

    public static byte[] encode(byte[] inputData) {
        //Console.logger().finest("produceData");
        List<Integer> historyStack = new ArrayList<Integer>(Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
        StringBuilder outputSb = new StringBuilder();
        StringBuilder commandSb = new StringBuilder(16);
        StringBuilder dataSb = new StringBuilder();
        //Console.logger().finest("input = " + BinaryHelpers.byteListToHex(inputData));
        int inputCursor = 0;
        byte[] output;
        int potentialCopyLength;
        int candidateSourceCursor;
        int copyCursor;
        StringBuilder offsetSb = new StringBuilder(11);
        StringBuilder lengthSb = new StringBuilder();
        while (inputCursor < inputData.length) {
            short inputWord = BinaryHelpers.getWord(inputData, inputCursor);
            //Console.logger().finest("inputWord = " + Integer.toHexString(inputWord & 0xFFFF));
            /* Get number of potential word sequence to copy */
            potentialCopyLength = 0;
            candidateSourceCursor = 0;
            copyCursor = inputCursor - 2;
            while (copyCursor >= 0 && (((inputCursor-copyCursor)/2) < MAX_COPY_OFFSET)) {
                int testLength = 0;
                short destWord = inputWord;
                short sourceWord = BinaryHelpers.getWord(inputData, copyCursor);
                while (sourceWord == destWord) {
                    testLength++;
                    if ((inputCursor+testLength*2) < inputData.length) {
                        sourceWord = BinaryHelpers.getWord(inputData, copyCursor+testLength*2);
                        destWord = BinaryHelpers.getWord(inputData, inputCursor+testLength*2);
                    } else {
                        break;
                    }
                }
                if (testLength > potentialCopyLength) {
                    candidateSourceCursor = copyCursor;
                    potentialCopyLength = testLength;
                }
                copyCursor -= 2;
            }
            //LOG.fine("Potential copy length from " + candidateSourceCursor + " = " + potentialCopyLength); 

            if (potentialCopyLength > 1) {
                // Apply word sequence copy
                int startOffset = (inputCursor-candidateSourceCursor)/2;
                int copyLength = potentialCopyLength;
                commandSb.append("1");
                offsetSb.setLength(0);
                offsetSb.append(Integer.toBinaryString(startOffset));
                while (offsetSb.length() < 11) {
                    offsetSb.insert(0, "0");
                }
                dataSb.append(offsetSb);
                lengthSb.setLength(0);
                copyLength -= 2;
                while (copyLength >= 0) {
                    switch (copyLength) {
                        case 0:
                            lengthSb.append("1");
                            copyLength = -1;
                            break;
                        case 1:
                            lengthSb.append("01");
                            copyLength = -1;
                            break;
                        default:
                            lengthSb.append("00");
                            copyLength -= 2;
                            break;
                    }
                }
                dataSb.append(lengthSb);
                inputCursor += potentialCopyLength * 2;
                //Console.logger().finest("input word " + Integer.toHexString(inputWord & 0xFFFF) + " copy : offset=" + startOffset + "/" + offsetSb.toString() + ", length=" + potentialCopyLength + "/" + lengthSb);
            } else {
                // No copy : word value
                commandSb.append("0");
                String valueBitString = getValueBitString(historyStack, inputWord);
                dataSb.append(valueBitString);
                inputCursor += 2;
                //Console.logger().finest("input word " + Integer.toHexString(inputWord & 0xFFFF) + " value : " + valueBitString + ", history=" + historyStack.toString());
            }

            if (commandSb.length() == 16) {
                String commandBitString = getCommandBitString(commandSb);
                //Console.logger().finest("commandSb=" + commandSb.toString() + ", commandBitString=" + commandBitString);
                outputSb.append(commandBitString);
                outputSb.append(dataSb);
                commandSb.setLength(0);
                dataSb.setLength(0);
                //Console.logger().finest("output = " + outputSb.toString());
            }

        }
        /* Add ending command with offset 0 */
        commandSb.append("1");
        dataSb.append("000000000001");
        while (commandSb.length() != 16) {
            commandSb.append("1");
        }
        String commandBitString = getCommandBitString(commandSb);
        outputSb.append(commandBitString);
        outputSb.append(dataSb);
        //Console.logger().finest("output = " + outputSb.toString());

        /* Word-wise padding */
        while (outputSb.length() % 16 != 0) {
            outputSb.append("1");
        }

        /* Byte array conversion */
        output = new byte[outputSb.length() / 8];
        for (int i = 0; i < output.length; i++) {
            Byte b = (byte) (Integer.valueOf(outputSb.substring(i * 8, i * 8 + 8), 2) & 0xFF);
            output[i] = b;
        }
        //Console.logger().finest("output bytes length = " + output.length);
        //Console.logger().finest("output = " + bytesToHex(output));
        //Console.logger().finest("produceData");
        return output;
    }

    private static String getValueBitString(List<Integer> historyStack, short value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int quartet = (value >> (16 - 4 * (i + 1))) & 0xF;
            for (int j = 0; j < 16; j++) {
                if (quartet == historyStack.get(j)) {
                    sb.append(historyBitStrings.get(j));
                    historyStack.remove(j);
                    historyStack.add(0, quartet);
                    break;
                }
            }
        }
        return sb.toString();
    }

    private static String getCommandBitString(StringBuilder commandSb) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String quartet = commandSb.substring(i * 4, i * 4 + 4);
            switch (quartet) {
                /* input bitstring	==> 4 command bits : 0 = word value, 1 = section copy */
                case "0000":
                    /* 0 		==> 0000 (4 word values, most frequent pattern, naturally) */
                    sb.append("0");
                    break;
                case "0001":
                    /* 100 		==> 0001 (3 word values, then 1 section copy) */
                    sb.append("100");
                    break;
                case "0010":
                    /* 101 		==> 0010 (2 word values etc ...) */
                    sb.append("101");
                    break;
                case "0100":
                    /* 110 		==> 0100 */
                    sb.append("110");
                    break;
                case "1000":
                    /* 1110 		==> 1000 */
                    sb.append("1110");
                    break;
                default:
                    /* 1111 xxxx 	==> xxxx (custom command pattern) */
                    sb.append("1111").append(quartet);
                    break;
            }

        }
        return sb.toString();
    }
}
