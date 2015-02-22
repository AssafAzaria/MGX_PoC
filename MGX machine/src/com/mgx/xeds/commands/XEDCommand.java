/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.commands;

import com.thales.shared.commands.Command;

/**
 *
 * @author Asaf
 */
public abstract class XEDCommand extends Command{

    public XEDCommand(int UID, String commandName, int senderUID, String senderName, Object data) {
        super(UID, commandName, senderUID, senderName, data, CommandType.XEDCommand);
    }
    
    
}
