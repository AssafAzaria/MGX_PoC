/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.commands;

import com.mgx.shared.commands.Command;
import com.mgx.shared.serviceprovider.responses.StoredSequencesListSPResponse;

/**
 *
 * @author Asaf
 */
public class getStoredSequencesSPCommand extends Command{

    static private final int UID = 23126;
    public getStoredSequencesSPCommand(int senderUID, String senderName) {
        super(UID, getStoredSequencesSPCommand.class.getSimpleName(), senderUID, senderName, null, CommandType.DataRepositoryCommand);
    }

    @Override
    public String dataToString() {
        return this.getData();
    }

    @Override
    public String getData() {
        return (String)this.data;
    }

    @Override
    public Class<StoredSequencesListSPResponse> getResponseClass() {
        return StoredSequencesListSPResponse.class;
    }
    
}
