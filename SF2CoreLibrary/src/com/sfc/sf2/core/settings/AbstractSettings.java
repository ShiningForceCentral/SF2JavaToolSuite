/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.core.settings;

import java.util.HashMap;

/**
 *
 * @author TiMMy
 */
public abstract interface AbstractSettings {
    
    public void initialiseNewUser();
    public void decodeSettings(HashMap<String, String> data);
    public void encodeSettings(HashMap<String, String> data);
}
