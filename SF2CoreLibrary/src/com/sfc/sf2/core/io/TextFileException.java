/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.core.io;

/**
 *
 * @author troyp
 */
public class TextFileException extends Exception {
    public TextFileException(String message) {
        super(message);
    }
    public TextFileException(Throwable cause) {
        super(cause);
    }
    public TextFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
