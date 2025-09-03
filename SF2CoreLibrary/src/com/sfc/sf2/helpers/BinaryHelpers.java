/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.helpers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public class BinaryHelpers {
    final protected static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        
    public static byte getByte(byte[] data, int cursor) {
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor]);
        byte b = bb.get(0);
        return b;
    }
    
    public static short getWord(byte[] data, int cursor) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(data[cursor+1]);
        bb.put(data[cursor]);
        short s = bb.getShort(0);
        return s;
    }
    
    public static void setWord(byte[] data, int cursor, short value){
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(value);
        data[cursor+1] = bb.get(0);
        data[cursor] = bb.get(1);
    }
    
    public static void setWordList(short word, List<Byte> data) {
        byte firstByte = (byte)((word>>8)&0xFF);
        byte secondByte = (byte)(word&0xFF);
        data.add(firstByte);
        data.add(secondByte);
    }
    
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static String byteListToHex(List<Byte> bytes) {
        char[] hexChars = new char[bytes.size() * 2];
        for ( int j = 0; j < bytes.size(); j++ ) {
            int v = bytes.get(j) & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static String shortListToHex(List<Short> shorts) {
        char[] hexChars = new char[shorts.size() * 4];
        for ( int j = 0; j < shorts.size(); j++ ) {
            short v = (short)(shorts.get(j) & 0xFFFF);
            hexChars[j * 4] = HEX_ARRAY[(v & 0xF000) >>> 12];
            hexChars[(j * 4) + 1] = HEX_ARRAY[(v & 0x0F00) >>> 8];
            hexChars[(j * 4) + 2] = HEX_ARRAY[(v & 0x00F0) >>> 4];
            hexChars[(j * 4) + 3] = HEX_ARRAY[(v & 0x000F)];            
        }
        return new String(hexChars);
    }
}
