/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.events;

import com.mgx.shared.XEDPropertyValueUpdate;


public class PropertyValueUpdateXEDNotification extends XEDNotification {

    static final int eventUID = 1122;
    /**
     * 
     * @param XEDName the name of the dispatcher XED
     * @param newData Property new value
     */
    public PropertyValueUpdateXEDNotification(String XEDName, XEDPropertyValueUpdate newData) {
        super(XEDName, eventUID, newData, PropertyValueUpdateXEDNotification.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }
    
    @Override
    public XEDPropertyValueUpdate getData() {
        return (XEDPropertyValueUpdate)this.data;
    }
}
