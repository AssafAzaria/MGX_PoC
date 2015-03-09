/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.responses;

import com.mgx.shared.XEDDescriptor;
import com.mgx.shared.events.Notification;


public class XEDPropertiesUpdateResponse extends SPIResponse {

    static int eventUID = 134;
    public XEDPropertiesUpdateResponse(String dispatcherName, XEDDescriptor Data) {
        super(dispatcherName, eventUID, Data, XEDPropertiesUpdateResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        if (data != null){
            return data.toString();
        }
        return "No data";
    }

    @Override
    public XEDDescriptor getData() {
        return (XEDDescriptor)data;
    }

     
}
