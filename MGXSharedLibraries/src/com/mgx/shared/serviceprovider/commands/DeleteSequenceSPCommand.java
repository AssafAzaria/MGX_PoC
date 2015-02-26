/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.commands;

import com.mgx.shared.commands.Command;

/**
 *
 * @author Asaf
 */
public class DeleteSequenceSPCommand extends Command{

    private static final int UID = 4432;
    public DeleteSequenceSPCommand( int senderUID, String senderName, int sequenceID) {
        super(UID, DeleteSequenceSPCommand.class.getSimpleName(), senderUID, senderName, sequenceID, CommandType.DataRepositoryCommand);
    }

    public int getSequenceID() {
        return this.getData();
    }
    
    @Override
    public String dataToString() {
        return "Sequence ID = "+getData();
    }

    @Override
    public Integer getData() {
        return (Integer)this.data;
    }
    
}
