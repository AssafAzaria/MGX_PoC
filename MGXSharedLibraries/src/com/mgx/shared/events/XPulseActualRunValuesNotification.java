/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.sequences.XPulseInfo;


/**
 * This notification is sent by the MGX when a sequence is complete
 * @author Asaf
 */
public class XPulseActualRunValuesNotification extends Notification {

    static final int UID =45854;    
    private final int sequenceID;
    private final int cFPGAUID;
    public XPulseActualRunValuesNotification(String dispatcherName, int sequenceID, int cFPGAUID, XPulseInfo xPulse) {
        super(dispatcherName, UID, xPulse, XPulseActualRunValuesNotification.class.getSimpleName());
        this.sequenceID = sequenceID;
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
        return "cFPGA UID = "+cFPGAUID
                +" sequence ID = "+sequenceID
                +" pulse = "+this.getData().toString();
                
    }

    @Override
    public XPulseInfo getData() {
        return (XPulseInfo)data;
    }
    
    /**
     * Return the sequence UID this XPulse belongs to 
     * @return sequence UID
     */
    public int getSequenceUID() {
        return this.sequenceID;
    }
    
    /**
     * 
     * @return the actual values of the pulse 
     */
    public XPulseInfo getXPulse() {
        return this.getData();
    }
    
}
