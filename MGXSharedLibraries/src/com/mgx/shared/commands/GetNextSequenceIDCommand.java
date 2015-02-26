/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;


public class GetNextSequenceIDCommand extends Command {
    static private final int UID = 11221;
    public GetNextSequenceIDCommand(int senderUID, String senderName) {
        super(UID, GetNextSequenceIDCommand.class.getSimpleName(), senderUID, senderName, null, CommandType.ControlCommand);
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
