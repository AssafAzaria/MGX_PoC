/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.responses;

import com.mgx.shared.events.Response;
import java.util.HashMap;


public class StoredSequencesListSPResponse extends Response {

    private static final int UID = 83235;
    public StoredSequencesListSPResponse(int listenerUID, String dispatcherName, HashMap<Integer, String>sequences) {
        super(listenerUID, dispatcherName, UID, sequences, StoredSequencesListSPResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }

    @Override
    public HashMap<Integer, String> getData() {
        return (HashMap<Integer, String>)this.data;
    }
    
    public HashMap<Integer, String> getStoredSequencesList(){
        return this.getData();
    }
    
}
