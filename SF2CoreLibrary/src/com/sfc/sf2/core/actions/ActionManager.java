/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.gui.controls.Console;
import java.util.Stack;

/**
 *
 * @author TiMMy
 */
public class ActionManager {
    private static final int ACTION_HISTORY_LIMIT = 10;//100;
    
    private static final IAction[] history = new IAction[ACTION_HISTORY_LIMIT];
    private static int stackStart = 0;
    private static int stackPointer = 0;
        
    public static void setAndExecuteAction(IAction action) {
        if (action == null) return;
        history[stackPointer] = action;
        stackPointer++;
        if (stackPointer >= ACTION_HISTORY_LIMIT-1) {
            stackPointer -= ACTION_HISTORY_LIMIT;
            stackStart++;
            if (stackStart >= ACTION_HISTORY_LIMIT) {
                stackStart -= ACTION_HISTORY_LIMIT;
            }
        }
        action.execute();
    }
    
    public static void undo() {
        if (stackPointer == stackStart) {
            Console.logger().finest("No more actions to undo.");
            return;
        }
        stackPointer--;
        if (stackPointer < 0) {
            stackPointer += ACTION_HISTORY_LIMIT;
        }
        history[stackPointer].undo();
        Console.logger().finest("Undo performed on : " + tableDataToString(history[stackPointer]));
        
    }
    
    public static void redo() {
        int pointer = stackPointer;
        if (pointer < 0) {
            pointer += ACTION_HISTORY_LIMIT;
        }
        if (history[pointer] == null) {
            Console.logger().finest("No more actions to redo.");
            return;
        }
        stackPointer = pointer+1;
        history[pointer].execute();
        Console.logger().finest("Redo performed on : " + tableDataToString(history[pointer]));
    }
    
    public static void clearActionhistory() {
        stackStart = 0;
        stackPointer = 0;
        for (int i = 0; i < ACTION_HISTORY_LIMIT; i++) {
            if (history[i] != null) {
                history[i].dispose();
            }
            history[i] = null;
        }
    }
    
    public static Object[][] getHistoryTableData() {
        Stack<Object[]> data = new Stack<>();
        int index = stackStart;
        while (history[index] != null && (data.isEmpty() || index != stackStart)) {
            data.add(formatTableData(history[index].toTableData()));
            index++;
            if (index >= ACTION_HISTORY_LIMIT) {
                index = 0;
            }
        }
        return data.size() == 0 ? null : (Object[][])data.toArray(new Object[0][]);
    }
    
    private static Object[] formatTableData(Object[] data) {
        for (int i = 0; i < data.length; i++) {
            int dotIndex = data[i].toString().lastIndexOf('.');
            if (dotIndex != -1) {
                data[i] = data[i].toString().substring(dotIndex+1);
            }
        }
        return data;
    }
    
    private static String tableDataToString(IAction action) {
        Object[] data = formatTableData(action.toTableData());
        return String.format("%s, New Data = %s, Old Data = %s", data[0], data[1], data[2]);
    }
}
