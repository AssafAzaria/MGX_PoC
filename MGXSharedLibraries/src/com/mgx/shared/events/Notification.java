/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

/**
 *
 * @author Asaf
 */
public abstract class Notification extends Event{
    
    public Notification(            
            String dispatcherName,
            int eventUID,
            Object Data,
            String eventName){
        super(
                0, //broadcast, no listener
                dispatcherName,
                eventUID,
                Data,
                eventName,
                EventType.NOTIFICATION);
    }
    
}
