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
public abstract class Response extends Event{
    
    public Response(
            int listenerUID,
            String dispatcherName,
            int eventUID,
            Object Data,
            String eventName){
        super(
                listenerUID,
                dispatcherName,
                eventUID,
                Data,
                eventName,
                EventType.RESPONSE);
    }
}
