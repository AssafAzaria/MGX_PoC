/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.responses;


public class SequenceLoadedSPIResponse extends SPIResponse {
    private static final int eventUID = 128;
    private final int cFPGAUID;
    public SequenceLoadedSPIResponse(String XEDName, int cFPGAUID, int sequenceID) {
        super(XEDName, eventUID, sequenceID, SequenceLoadedSPIResponse.class.getSimpleName());
        this.cFPGAUID = cFPGAUID;
    }

    @Override
    public String dataToString() {
        return "XED "+dispatcherName+" loaded sequence "+getData();
    }

    @Override
    public Integer getData() {
        return (Integer)data;
    }
    
    /**
     * 
     * @return loaded sequence UID
     */
    public int getSequenceUID() {
        return this.getData();
    }
    /**
     * 
     * @return cFPGA UID that invoked this response 
     */
    public int getcFPGAUID() {
        return this.cFPGAUID;
    }
    
}
