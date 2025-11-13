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
    
    private static final Action[] history = new Action[ACTION_HISTORY_LIMIT];
    private static int stackStart = 0;
    private static int stackPointer = 0;
        
    public static void setAndExecuteAction(Action action) {
        if (action == null) return;
        setActionWithoutExecute(action);
        action.execute();
    }
        
    public static void setActionWithoutExecute(Action action) {
        if (action == null) return;
        history[stackPointer] = action;
        stackPointer++;
        if (stackPointer >= ACTION_HISTORY_LIMIT) {
            stackPointer -= ACTION_HISTORY_LIMIT;
        }
        if (stackPointer == stackStart) {
            stackStart++;
            if (stackStart >= ACTION_HISTORY_LIMIT) {
                stackStart -= ACTION_HISTORY_LIMIT;
            }
        }
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
        Console.logger().finest(String.format("Undo (%d/%d) performed on : %s", getCurrentHistoryIndex(), ACTION_HISTORY_LIMIT, actionToString(history[stackPointer])));
        
    }
    
    public static void redo() {
        int pointer = stackPointer;
        if (history[pointer] == null || stackPointer == stackStart-1 || (stackPointer == ACTION_HISTORY_LIMIT-1 && stackStart == 0)) {
            Console.logger().finest("No more actions to redo.");
            return;
        }
        stackPointer++;
        if (stackPointer >= ACTION_HISTORY_LIMIT) {
            stackPointer -= ACTION_HISTORY_LIMIT;
        }
        history[pointer].execute();
        Console.logger().finest(String.format("Redo (%d/%d) performed on : %s", getCurrentHistoryIndex(), ACTION_HISTORY_LIMIT, actionToString(history[pointer])));
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
    
    /**
     * Get an index representing how far along the action history the current pointer is
     */
    public static int getCurrentHistoryIndex() {
        if (stackPointer < stackStart) {
            return stackPointer+ACTION_HISTORY_LIMIT-stackStart;
        } else {
            return stackPointer-stackStart;
        }
    }
    
    /**
     * Organise the action history data in an ordered array
     */
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
        return data.isEmpty() ? null : (Object[][])data.toArray(new Object[0][]);
    }
    
    /**
     * Organise an action into a table-friendly format
     */
    private static Object[] formatTableData(Object[] data) {
        for (int i = 0; i < data.length; i++) {
            int dotIndex = data[i].toString().lastIndexOf('.');
            if (dotIndex != -1) {
                data[i] = data[i].toString().substring(dotIndex+1);
            }
        }
        return data;
    }
    
    /**
     * Converts an action to a string in similar format to how the tables are displayed
     */
    private static String actionToString(Action action) {
        Object[] data = formatTableData(action.toTableData());
        return String.format("%s, New Data = %s, Old Data = %s", data[0], data[1], data[2]);
    }
}
