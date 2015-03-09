/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.responses.mgxprivate;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.events.Response;

/**
 *
 * @author Asaf
 */
public class LastCFPGASettingsSPResponse extends Response{

    private static final int UID = 2231;
    public LastCFPGASettingsSPResponse(int listenerUID, String dispatcherName, CFPGADescriptor settings) {
        super(listenerUID, dispatcherName, UID, settings, LastCFPGASettingsSPResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }

    @Override
    public CFPGADescriptor getData() {
        return (CFPGADescriptor)this.data;
    }
    
}
