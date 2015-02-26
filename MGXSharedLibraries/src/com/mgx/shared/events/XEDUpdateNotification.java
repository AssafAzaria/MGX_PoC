/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.XEDInfo;
import com.mgx.shared.XEDPropertyValueUpdate;


public class XEDUpdateNotification extends Notification {

    static final int eventUID =456;
    public XEDUpdateNotification(String dispatcherName, XEDPropertyValueUpdate property) {
        super(dispatcherName, eventUID, property, XEDUpdateNotification.class.getName());
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
                
    }

    @Override
    public XEDPropertyValueUpdate getData() {
        return (XEDPropertyValueUpdate)data;
    }
    
}
