/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.responses;

import java.io.Serializable;


public class SequenceExecutionCompleteSPIREsponse extends SPIResponse {
    static final private int UID = 9986;
    private final int cFPGAUID;
    private final int sequenceID;
    
    
    public SequenceExecutionCompleteSPIREsponse(String dispatcherName, int cFPGAUID, int sequenceID ) {
        super(dispatcherName, UID,null, SequenceExecutionCompleteSPIREsponse.class.getSimpleName());
        this.cFPGAUID = cFPGAUID;
        this.sequenceID = sequenceID;
    }

    @Override
    public String dataToString() {
        return "";
    }

    /**
     * Not used. use direct methods instead
     * @return 
     */
    @Override
    public Object getData() {
        return null;
    }

    /**
     * @return the cFPGA UID
     */
    public int getcFPGAUID() {
        return cFPGAUID;
    }

    /**
     * @return the sequenceID
     */
    public int getSequenceID() {
        return sequenceID;
    }

    
    
}
