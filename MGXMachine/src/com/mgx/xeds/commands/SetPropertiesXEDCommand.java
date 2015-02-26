/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.commands;

import com.mgx.manager.XedArrayProxy;
import com.mgx.shared.XEDPropertyValueUpdate;


public class SetPropertiesXEDCommand extends XEDCommand {
    final static int UID = 321;
    public SetPropertiesXEDCommand(XEDPropertyValueUpdate properties[]) {
        super(UID, SetPropertiesXEDCommand.class.getSimpleName(), XedArrayProxy.UID, XedArrayProxy.class.getName(), properties);
    }
    
    @Override
    public XEDPropertyValueUpdate[] getData() {
        return (XEDPropertyValueUpdate[])this.data;
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }
}
