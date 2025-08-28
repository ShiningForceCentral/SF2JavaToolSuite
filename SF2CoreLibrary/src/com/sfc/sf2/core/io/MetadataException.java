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
public class MetadataException extends Exception {
    public MetadataException(String message) {
        super(message);
    }
    public MetadataException(Throwable cause) {
        super(cause);
    }
    public MetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}
