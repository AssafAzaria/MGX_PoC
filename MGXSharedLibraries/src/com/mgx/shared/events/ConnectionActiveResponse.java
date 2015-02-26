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
public class ConnectionActiveResponse extends Response{
    
    static final int UID = 2;
    public ConnectionActiveResponse(String dispatcherName){
        
        super(0, dispatcherName, UID, null, 
                ConnectionActiveResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return "no data";
    }

    @Override
    public Object getData() {
        return null;
    }
    
    
}
