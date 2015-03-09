/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.events.MGXNodesResponse;

/**
 *
 * @author Asaf
 */
public class GetMGXNodesCommand extends Command{

    final static int UID = 1;
    /**
     * Request the MGX machine to provide all the controllers and 
     * XEDs that connected to it
     * 
     * @param senderUID the UID of the sender
     * @param senderName string representation of the sender
     */
    public GetMGXNodesCommand(int senderUID, String senderName) {
        super(UID, GetMGXNodesCommand.class.getSimpleName(), senderUID, senderName, null, CommandType.XEDCommand);
    }
    @Override
    public Class<MGXNodesResponse> getResponseClass() {
        return MGXNodesResponse.class;
    }
    
    @Override
    public String toString() {
        return this.getName()+" ("+getUID()+") sent by " + getSenderName();
    }

    @Override
    public String dataToString() {
        return "";
    }

    @Override
    public Object getData() {
        return null;
    }

    
    
}
