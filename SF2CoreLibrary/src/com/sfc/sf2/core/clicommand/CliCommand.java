/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.core.clicommand;

import com.sfc.sf2.core.clicommand.CliDefinition.CliCommandID;

/**
 *
 * @author TiMMy
 */
public record CliCommand(CliCommandID id, String[] data) { }
