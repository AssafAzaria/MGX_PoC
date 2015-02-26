/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.events;

import com.mgx.shared.events.Notification;

/**
 *
 * @author Asaf
 */
public abstract class  XEDNotification extends Notification{

    public XEDNotification(String dispatcherName, int eventUID, Object Data, String eventName) {
        super(dispatcherName, eventUID, Data, eventName);
    }

    
    
}
