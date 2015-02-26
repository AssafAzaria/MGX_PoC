/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.responses;

import com.mgx.shared.events.Response;

/**
 *
 * @author Asaf
 */
public class DeleteSequenceResponse extends Response{

    private static final int id = 24212;
    public DeleteSequenceResponse(int listenerUID, String dispatcherName, int sequenceID) {
        super(listenerUID, dispatcherName, id, sequenceID, DeleteSequenceResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return "Sequence with ID "+getData()+ " deleted";
    }

    @Override
    public Integer getData() {
        return (Integer)this.data;
    }
    
    public int getSequenceID() {
        return this.getData();
    }
    
}
