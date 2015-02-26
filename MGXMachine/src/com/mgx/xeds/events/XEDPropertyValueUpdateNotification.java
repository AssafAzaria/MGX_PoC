/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.events;

import com.mgx.shared.XEDInfo;
import com.mgx.shared.events.Notification;


public class XEDPropertyValueUpdateNotification extends XEDNotification {

    static int eventUID = 134;
    public XEDPropertyValueUpdateNotification(String dispatcherName, XEDInfo Data) {
        super(dispatcherName, eventUID, Data, XEDPropertyValueUpdateNotification.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        if (data != null){
            return data.toString();
        }
        return "No data";
    }

    @Override
    public XEDInfo getData() {
        return (XEDInfo)data;
    }

     
}
