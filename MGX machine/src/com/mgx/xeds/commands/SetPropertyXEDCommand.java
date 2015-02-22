/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.commands;

import com.mgx.manager.XedArrayProxy;
import com.thales.shared.XEDPropertyValueUpdate;


public class SetPropertyXEDCommand extends XEDCommand {

    final static int UID = 321;
    public SetPropertyXEDCommand(XEDPropertyValueUpdate property) {
        super(UID, SetPropertyXEDCommand.class.getSimpleName(), XedArrayProxy.UID, XedArrayProxy.class.getName(), property);
    }
    
    @Override
    public XEDPropertyValueUpdate getData() {
        return (XEDPropertyValueUpdate)this.data;
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }
}
