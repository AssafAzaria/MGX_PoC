/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.XEDProperty;




public class XEDUpdateNotification extends Notification {

    static final int eventUID =456;
    public XEDUpdateNotification(String dispatcherName, CFPGADescriptor cFPGA) {
        super(dispatcherName, eventUID, cFPGA, XEDUpdateNotification.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
                
    }

    @Override
    public CFPGADescriptor getData() {
        return (CFPGADescriptor)data;
    }
    
}
