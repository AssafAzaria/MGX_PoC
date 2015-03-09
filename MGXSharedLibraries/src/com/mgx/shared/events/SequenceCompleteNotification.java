/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;


/**
 * This notification is sent by the MGX when a sequence is complete
 * @author Asaf
 */
public class SequenceCompleteNotification extends Notification {

    static final int UID =4581;    
    private final int cFPGAUID;
    public SequenceCompleteNotification(String dispatcherName, int sequenceID, int cFPGAUID) {
        super(dispatcherName, UID, sequenceID, SequenceCompleteNotification.class.getSimpleName());
        this.cFPGAUID = cFPGAUID;
    }
    
    /**
     * Return the UID of the cFPGA that executed the sequence
     * @return cFPGA UID
     */
    public int getcFPGAUID() {
        return this.cFPGAUID;
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
                
    }

    @Override
    public Integer getData() {
        return (Integer)data;
    }
    
    /**
     * Return the sequence UID that was completed
     * @return sequence UID
     */
    public int getSequenceID() {
        return this.getData();
    }
    
}
