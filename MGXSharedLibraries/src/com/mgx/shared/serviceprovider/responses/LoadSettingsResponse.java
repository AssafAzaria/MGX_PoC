/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.responses;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.events.Response;

/**
 *
 * @author Asaf
 */
public class LoadSettingsResponse extends Response{

    private final static int UID = 23435;
    public LoadSettingsResponse(int listenerUID, String dispatcherName, CFPGADescriptor cFPGA) {
        super(listenerUID, dispatcherName, UID, cFPGA, LoadSettingsResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }

    @Override
    public CFPGADescriptor getData() {
        return (CFPGADescriptor)this.data;
    }
    
}
