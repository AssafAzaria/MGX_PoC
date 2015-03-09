/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.commands;

import com.mgx.shared.sequences.SequenceInfo;

/**
 *
 * @author Asaf
 */
public class AcuireSequenceSPICommand extends SPICommand {

    static private final int UID = 3211;
    public AcuireSequenceSPICommand(int senderUID, SequenceInfo sequenceInfo) {
        super(UID, AcuireSequenceSPICommand.class.getSimpleName(), sequenceInfo);
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }
    
    @Override
    public SequenceInfo getData() {
        return (SequenceInfo)data;
    }
    
    public SequenceInfo getSequence() {
        return this.getData();
    }

    @Override
    public Class<?> getResponseClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
