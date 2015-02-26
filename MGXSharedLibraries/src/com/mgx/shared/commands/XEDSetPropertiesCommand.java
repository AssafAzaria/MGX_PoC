/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.XEDPropertyValueUpdate;


public class XEDSetPropertiesCommand extends Command {
    final static int UID = 6778;
    
    public XEDSetPropertiesCommand(int listenerUID, String senderName, XEDPropertyValueUpdate properties[]) {
        super(UID, XEDSetPropertiesCommand.class.getSimpleName(), listenerUID, senderName, properties, CommandType.XEDCommand);

    }
    @Override
    public XEDPropertyValueUpdate[] getData() {
        return (XEDPropertyValueUpdate[])data;
    }
    
    @Override
    public String dataToString() {
        XEDPropertyValueUpdate[] properties = this.getData();
        StringBuilder result = new StringBuilder("got "+properties.length+" propertis:\n");
        for (XEDPropertyValueUpdate prop : properties) {
            result.append(prop.toString());
            result.append("\n");
        }
        return result.toString();
    }

   
    
}
