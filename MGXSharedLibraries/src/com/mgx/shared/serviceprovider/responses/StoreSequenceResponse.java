/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.responses;

import com.mgx.shared.events.Response;


public class StoreSequenceResponse extends Response {

    private static final int eventUID = 83232;
    public StoreSequenceResponse(int listenerUID, String dispatcherName, int seqID) {
        super(listenerUID, dispatcherName, eventUID, seqID, StoreSequenceResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return "Stored sequence ID = "+getData();
    }

    @Override
    public Integer getData() {
        return (Integer)this.data;
    }
    
    public int getSequenceID() {
        return this.getData();
    }
    
}
