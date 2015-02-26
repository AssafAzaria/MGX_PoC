/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;


public class SequenceIDResponse extends Response {

    static private int eventUID = 3342;
    public SequenceIDResponse(int listenerUID, String dispatcherName, Integer ID) {
        super(listenerUID, dispatcherName, eventUID, ID, SequenceIDResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }

    @Override
    public Integer getData() {
        return (Integer)this.data;
    }
    
}
