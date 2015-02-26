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
public class StageChangeNotification extends Notification{
    
    private static final int UID = 1;
    public StageChangeNotification(
            String dispatcherName,
            Object data){
        super(dispatcherName, UID, data, StageChangeNotification.class.getSimpleName());
        
    }

    @Override
    public String dataToString() {
        return "";
    }

    @Override
    public Object getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
