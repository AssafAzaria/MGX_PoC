/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

/**
 *
 * @author Asaf
 */
public class GetXEDsCommand extends Command{

    final static int UID = 1;
    public GetXEDsCommand(int senderUID, String senderName) {
        super(UID, "GetXEDsCommand", senderUID, senderName, null, CommandType.XEDCommand);
    }
    
    @Override
    public String toString() {
        return "GetXEDsCommand("+getUID()+") sent by " + getSenderName();
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
