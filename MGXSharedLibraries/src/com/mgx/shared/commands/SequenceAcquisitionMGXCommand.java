/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.sequences.SequenceInfo;


public class SequenceAcquisitionMGXCommand extends Command {
    private final static int UID = 543;
    private final int cFPGAUID;
    private boolean isSWAcquisition = false;
    public SequenceAcquisitionMGXCommand( int senderUID, String senderName, 
            int cFPGAUID, boolean isSWAcquisition, SequenceInfo sequence) {
        super(UID, SequenceAcquisitionMGXCommand.class.getSimpleName(), 
                senderUID, senderName, sequence, CommandType.XEDCommand);
        this.cFPGAUID = cFPGAUID;
        this.isSWAcquisition = isSWAcquisition;
        
    }

    public boolean isSWAcquisition() {
        return this.isSWAcquisition;
    }
    
    @Override
    public String dataToString() {
        return "Is SW acuisition = "+isSWAcquisition+" seq = " +this.getData().toString();
    }
    
    @Override
    public SequenceInfo getData() {
        return (SequenceInfo)super.data;
    }

    @Override
    public Class<CommandOKResponse> getResponseClass() {
        return CommandOKResponse.class;
    }

    /**
     * @return the UID of the cFPGA that needs to execute this sequence
     */
    public int getcFPGAUID() {
        return cFPGAUID;
    }
    
    public SequenceInfo getSequence() {
        return this.getData();
    }
    
}
