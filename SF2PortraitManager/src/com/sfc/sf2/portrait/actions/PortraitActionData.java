/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.portrait.actions;

import com.sfc.sf2.core.actions.IActionData;

/**
 *
 * @author TiMMy
 */
public class PortraitActionData implements IActionData<PortraitActionData> {
    
    private int row;
    private int[] data;

    public PortraitActionData(int row, int[] data) {
        this.row = row;
        this.data = data;
    }

    public int row() {
        return row;
    }

    public int[] data() {
        return data;
    }

    @Override
    public boolean isInvalidated(PortraitActionData other) {
        if (row != other.row) return false;
        for (int i = 0; i < 4; i++) {
            if (data[i] != other.data[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canBeCombined(PortraitActionData other) {
        return this.row() == other.row();
    }

    @Override
    public PortraitActionData combine(PortraitActionData other) {
        return other;
    }

    @Override
    public String toString() {
        if (data == null || row == -1) {
            return null;
        } else {
            return String.format("(Row %d): %d, %d - %d, %d", row, data[0], data[1], data[2], data[3]);
        }
    }
}
