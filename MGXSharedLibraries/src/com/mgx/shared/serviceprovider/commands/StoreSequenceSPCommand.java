/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.commands;

import com.mgx.shared.commands.Command;
import com.mgx.shared.sequences.SequenceInfo;
import com.mgx.shared.serviceprovider.responses.StoreSequenceResponse;

/**
 *
 * @author Asaf
 */
public class StoreSequenceSPCommand extends Command{

    private static final int UID = 7765;
    public StoreSequenceSPCommand(int senderUID, String senderName, SequenceInfo data) {
        super(UID, StoreSequenceSPCommand.class.getSimpleName(), senderUID, senderName, data, CommandType.DataRepositoryCommand);
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }

    @Override
    public SequenceInfo getData() {
        return (SequenceInfo)this.data;
    }

    @Override
    public Class<StoreSequenceResponse> getResponseClass() {
        return StoreSequenceResponse.class;
    }

    
   
    
}
