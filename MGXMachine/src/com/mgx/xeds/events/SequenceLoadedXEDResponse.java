/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.events;


public class SequenceLoadedXEDResponse extends XEDResponse {
    private static final int eventUID = 128;
    public SequenceLoadedXEDResponse(String XEDName, Integer sequenceID) {
        super(XEDName, eventUID, sequenceID, SequenceLoadedXEDResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return "XED "+dispatcherName+" loaded sequence "+getData();
    }

    @Override
    public Integer getData() {
        return (Integer)data;
    }
    
}
