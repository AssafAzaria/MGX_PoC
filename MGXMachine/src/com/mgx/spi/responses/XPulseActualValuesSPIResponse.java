/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.responses;

import com.mgx.shared.sequences.XPulseInfo;


public class XPulseActualValuesSPIResponse extends SPIResponse {

    final static private int UID = 231211;
    private final int sequenceID;
    public XPulseActualValuesSPIResponse(String cFPGAName, int sequenceID, XPulseInfo pulse) {
        super(cFPGAName, UID, pulse, XPulseActualValuesSPIResponse.class.getSimpleName());
        this.sequenceID = sequenceID;
    }

    @Override
    public String dataToString() {
        return "sequence ID = "+sequenceID + "xPulse actuall values:"+getData().toString();
    }

    @Override
    public XPulseInfo getData() {
        return (XPulseInfo)super.data;
    }
    
    public int getSequenceID() {
        return this.sequenceID;
    }
    
    public XPulseInfo getXPulse() {
        return this.getData();
    }
    
}
