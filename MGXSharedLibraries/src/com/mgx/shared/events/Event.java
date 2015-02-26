/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.networking.Transmitable;
import java.io.Serializable;
/**
 *
 * @author Asaf
 */
public abstract class Event implements Serializable, Transmitable{
    private static final long serialVersionUID = 42L;

    public final int eventUID;
    public final Object data;
    public final String dispatcherName;
    public final int listenerUID;
    public final String eventName;
    public final EventType eventType;
    
    public Event(
            int listenerUID,
            String dispatcherName,
            int eventUID,
            Object Data,
            String eventName,
            EventType eventType) {
        
      this.listenerUID = listenerUID;
      this.dispatcherName = dispatcherName;
      this.eventUID = eventUID;
      this.data = Data;
      this.eventName = eventName;
      this.eventType = eventType;
      
    }
    
    public String getName() {
        return this.eventName;
    }
    
    @Override
    public int getUID() {
        return eventUID;
    }
    
    @Override
    public String toString() {
        return ""+this.getName()+":\n" 
                +" UID:"+this.eventUID
                + " dispatcher name:"+this.dispatcherName
                + " listenerUID:"+this.listenerUID
                + " type:"+this.eventType
                + "\ndata:"+ dataToString();
    
    }
    
        
    
    
}
