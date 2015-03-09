/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;


/**
 * This notification is sent by the MGX when a sequence is loaded
 * @author Asaf
 */
public class SequenceLoadedNotification extends Notification {

    static final int UID =45349;    
    private final int cFPGAUID;
    public SequenceLoadedNotification(String dispatcherName, int sequenceID, int cFPGAUID) {
        super(dispatcherName, UID, sequenceID, SequenceLoadedNotification.class.getSimpleName());
        this.cFPGAUID = cFPGAUID;
    }
    
    /**
     * cFPGA UID that invoke this notification
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
     * Return the sequence UID that was loaded
     * @return sequence UID
     */
    public int getSequenceID() {
        return this.getData();
    }
    
}
