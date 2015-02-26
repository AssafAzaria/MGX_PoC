/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.XEDPropertyValueUpdate;


public class XEDSetPropertyCommand extends Command {
    private final static int UID = 6779;
    public XEDSetPropertyCommand(int listenerUID, String senderName, XEDPropertyValueUpdate property) {
        super(UID, XEDSetPropertyCommand.class.getSimpleName(), listenerUID, senderName, property, CommandType.XEDCommand);

    }
    
    /**
     * Get the property required to be set
     * @return property object
     */
    @Override
    public XEDPropertyValueUpdate getData() {
        return (XEDPropertyValueUpdate)data;
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }
}
