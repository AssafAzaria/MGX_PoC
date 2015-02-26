/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;


public class StartSequenceCommand extends Command {

    static final private int UID = 812;
    public StartSequenceCommand(int senderUID, String senderName, Integer sequenceID) {
        super(UID, StartSequenceCommand.class.getSimpleName(), senderUID, senderName, sequenceID, CommandType.ControlCommand);
    }

    @Override
    public String dataToString() {
        return "sequence id = "+getData().toString();
    }
    
    @Override
    public Integer getData() {
        return (Integer)data;
    }
    
}
