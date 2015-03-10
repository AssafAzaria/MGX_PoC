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
public class LoadSequenceSPCommand extends Command{

    static private final int UID = 23123;
    public LoadSequenceSPCommand(int senderUID, String senderName, int seqID) {
        super(UID, LoadSequenceSPCommand.class.getSimpleName(), senderUID, senderName, seqID, CommandType.DataRepositoryCommand);
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }

    @Override
    public Integer getData() {
        return (Integer)this.data;
    }
    public int getSequenceID() {
        return this.getData();
    }

    @Override
    public Class<LoadSequenceSPCommand> getResponseClass() {
        return LoadSequenceSPCommand.class;
    }
    
}
