/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;


public class SequenceCompleteNotificatoin extends Notification {
    static private final int eventUID = 3242;
    public SequenceCompleteNotificatoin(String dispatcherName, Integer  sequenceID) {
        super(dispatcherName, eventUID, sequenceID, SequenceCompleteNotificatoin.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return "sequenceID = "+getData().toString();
    }

    @Override
    public Integer getData() {
        return (Integer)data;
    }
    
}
