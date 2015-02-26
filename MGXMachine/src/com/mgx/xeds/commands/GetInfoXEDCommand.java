/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.commands;

import com.mgx.manager.XedArrayProxy;


public class GetInfoXEDCommand extends XEDCommand {

    private static final int UID = 5;  
    
    
    public GetInfoXEDCommand() {
        super(UID, GetInfoXEDCommand.class.getSimpleName() , XedArrayProxy.UID, XedArrayProxy.class.getName(), null);
    }

    @Override
    public String dataToString() {
        return "";
    }

    @Override
    public Object getData() {
        return null;
    }
    
}
