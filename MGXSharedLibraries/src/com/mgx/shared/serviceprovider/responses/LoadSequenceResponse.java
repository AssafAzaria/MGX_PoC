/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.responses;

import com.mgx.shared.events.Response;
import com.mgx.shared.sequences.SequenceInfo;

/**
 *
 * @author Asaf
 */
public class LoadSequenceResponse extends Response{

    private final static int UID = 23432;
    public LoadSequenceResponse(int listenerUID, String dispatcherName, SequenceInfo sequence) {
        super(listenerUID, dispatcherName, UID, sequence, LoadSequenceResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }

    @Override
    public SequenceInfo getData() {
        return (SequenceInfo)this.data;
    }
    public SequenceInfo getSeqeuence() {
        return this.getData();
    }
    
}
