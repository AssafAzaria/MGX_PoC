/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.commands;

import com.mgx.manager.XedArrayProxy;
import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.XEDDescriptor;
import com.mgx.shared.XEDProperty;


public class UpdateXEDsPropertiesSPICommand extends SPICommand {
    final static int UID = 321;
    public UpdateXEDsPropertiesSPICommand(XEDDescriptor XEDs[]) {
        super(UID, UpdateXEDsPropertiesSPICommand.class.getSimpleName(), XEDs);
    }
    
    @Override
    public XEDDescriptor[] getData() {
        return (XEDDescriptor[])this.data;
    }

    @Override
    public String dataToString() {

        XEDDescriptor[] xeds = this.getData();
        StringBuilder result = new StringBuilder("Requested to set properties to "+xeds.length+" XEDs \n");
        
        for (XEDDescriptor xed : xeds) {
            result.append("XED "+xed.getName()+" UID = "+xed.getUID()+"\n");
            
            for (XEDProperty prop : xed.getProperties()) {
                result.append(prop.toString());
                result.append("\n");
            }
        }
        return result.toString();
    }

    @Override
    public Class<?> getResponseClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
