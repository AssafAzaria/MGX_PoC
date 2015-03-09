/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.commands;

import com.mgx.manager.XedArrayProxy;
import com.mgx.shared.CFPGADescriptor;


public class CFPGAIndetifySPICommand extends SPICommand {

    private static final int UID = 5;  
    
    /**
     *a request from the XED Array Proxy to cFPGA
     * Should be answered with XEDsListSPIResponse
     */
    public CFPGAIndetifySPICommand() {
        super(UID, CFPGAIndetifySPICommand.class.getSimpleName(), null);
    }

    @Override
    public String dataToString() {
        return "";
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public Class<?> getResponseClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public CFPGADescriptor getResponseData() {
        return (CFPGADescriptor)super.getResponseData();
    }
}
