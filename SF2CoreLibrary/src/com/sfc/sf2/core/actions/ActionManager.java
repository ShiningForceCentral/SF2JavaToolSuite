/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.actions;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.core.settings.SettingsManager;
import java.util.Stack;

/**
 *
 * @author TiMMy
 */
public class ActionManager {
    private static final int ACTION_HISTORY_LIMIT = 100;
    
    private static final IAction[] history = new IAction[ACTION_HISTORY_LIMIT];
    private static int stackStart = 0;
    private static int stackPointer = 0;
    
    private static boolean actionTriggering = false;
    private static boolean externalActionTriggering = false;
    private static long lastActionTime = 0;

    public static boolean isActionTriggering() {
        return actionTriggering || externalActionTriggering || !SettingsManager.isSavingAllowed();
    }

    public static void setExternalActionTriggering(boolean tempActionTriggering) {
        ActionManager.externalActionTriggering = tempActionTriggering;
    }

    private static void setActionTriggering(boolean actionTriggering) {
        ActionManager.actionTriggering = actionTriggering;
        ActionManager.externalActionTriggering = false;
    }
    
    public static void setAndExecuteAction(IAction action) {
        if (action == null) return;
        setActionWithoutExecute(action);
        setActionTriggering(true);
        action.execute();
        setActionTriggering(false);
    }
        
    public static void setActionWithoutExecute(IAction action) {
        if (action == null) return;
        long time = System.currentTimeMillis();
        if (time - lastActionTime < 20000) {
            //Check if action can be combined
            if (getCurrentHistoryIndex() > 0) {
                int pointer = stackPointer-1;
                if (pointer < 0) {
                    pointer += ACTION_HISTORY_LIMIT;
                }
                if (history[pointer] != null && history[pointer].canBeCombined(action)) {
                    //Actions can be combined so combine but don't adjust the action stack
                    history[pointer].combine(action);
                    if (history[pointer].isInvalidated()) {
                        history[pointer] = null;
                        shiftPointerBack();
                    }
                    lastActionTime = time;
                    return;
                }
            }
        }
        lastActionTime = time;
        history[stackPointer] = action;
        shiftPointerForward(true);
        clearInvalidRedos();
    }
    
    public static void undo() {
        lastActionTime = 0;
        if (stackPointer == stackStart) {
            Console.logger().finest("No more actions to undo.");
            return;
        }
        shiftPointerBack();
        setActionTriggering(true);
        history[stackPointer].undo();
        Console.logger().finest(String.format("Undo (%d/%d) performed on : %s", getCurrentHistoryIndex(), ACTION_HISTORY_LIMIT, actionToString(history[stackPointer])));
        setActionTriggering(false);
    }
    
    public static void redo() {
        if (history[stackPointer] == null || stackPointer == stackStart-1 || (stackPointer == ACTION_HISTORY_LIMIT-1 && stackStart == 0)) {
            Console.logger().finest("No more actions to redo.");
            return;
        }
        int pointer = stackPointer;
        shiftPointerForward(false);
        setActionTriggering(true);
        history[pointer].execute();
        Console.logger().finest(String.format("Redo (%d/%d) performed on : %s", getCurrentHistoryIndex(), ACTION_HISTORY_LIMIT, actionToString(history[pointer])));
        setActionTriggering(false);
    }
    
    private static void shiftPointerBack() {
        if (stackPointer == stackStart) return;
        stackPointer--;
        if (stackPointer < 0) {
            stackPointer += ACTION_HISTORY_LIMIT;
        }
    }
    
    private static void shiftPointerForward(boolean pushStart) {
        int pointer = stackPointer+1;
        if (pointer >= ACTION_HISTORY_LIMIT) {
            pointer -= ACTION_HISTORY_LIMIT;
        }
        if (pointer == stackStart) {
            if (pushStart) {
                stackStart++;
                if (stackStart >= ACTION_HISTORY_LIMIT) {
                    stackStart -= ACTION_HISTORY_LIMIT;
                }
            } else {
                return; //Don't adjust stack pointer
            }
        }
        stackPointer = pointer;
    }
    
    public static void preventActionsCombining() {
        lastActionTime = 0;
    }
    
    public static void clearInvalidRedos() {
        for (int i = stackPointer; i < ACTION_HISTORY_LIMIT; i++) {
            int index = i;
            if (index > history.length)
                index -= history.length;
            if (history[i] != null) {
                history[i].dispose();
                history[i] = null;
            } else {
                break;
            }
        }
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
        setActionTriggering(false);
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
        for (int i = 0; i < 1; i++) {
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
    private static String actionToString(IAction action) {
        Object[] data = formatTableData(action.toTableData());
        return String.format("%s (%s): New Data = %s, Old Data = %s", data[0], data[1], data[2], data[3]);
    }
}
