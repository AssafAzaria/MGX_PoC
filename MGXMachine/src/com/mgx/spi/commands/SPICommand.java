/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.commands;

import com.mgx.manager.XedArrayProxy;
import com.mgx.shared.commands.Command;

/**
 *
 * @author Asaf
 */
public abstract class SPICommand extends Command{

    public SPICommand(int UID, String commandName, Object data) {
        super(UID, commandName, XedArrayProxy.UID, XedArrayProxy.class.getSimpleName(), data, CommandType.XEDCommand);
    }
    
    
}
