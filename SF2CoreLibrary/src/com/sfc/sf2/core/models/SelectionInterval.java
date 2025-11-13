/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.models;

/**
 *
 * @author TiMMy
 */
public class SelectionInterval {
    
    private int start;
    private int end;
    private Object[] data;
    private int offset;
    
    public SelectionInterval(int start, int end) {
        this(start, end, 0);
    }
    
    public SelectionInterval(int start, int end, int offset) {
        this.start = start;
        this.end = end;
        this.offset = offset;
    }
    
    public SelectionInterval(int start, Object[] data) {
        this(start, data, 0);
    }
    
    public SelectionInterval(int start, Object[] data, int offset) {
        this.start = start;
        this.end = -1;
        this.data = data;
        this.offset = offset;
    }

    public int start() {
        return start;
    }

    public int end() {
        return data == null ? end : start+data.length-1;
    }
    
    public int count() {
        return data == null ? end-start+1 : data.length;
    }

    public Object[] data() {
        return data;
    }

    public int offset() {
        return offset;
    }
}
