/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.responses;

import com.mgx.shared.CFPGADescriptor;

/**
 * Response for CFPGAIdentifySPICommand
 * @author Asaf
 */
public class CFPGADescriptorSPIResponse extends SPIResponse {

    private static final int UID = 123;
    /**
     * A response for CFPGAIdentifySPICommand
     * @param cFPGAName the name of the responding cFPGA
     * @param cFPGAUID the UID of the responding cFPGA
     * @param cFPGA all related data of this cFPGA
     */
    public CFPGADescriptorSPIResponse(String cFPGAName, int cFPGAUID, CFPGADescriptor cFPGA) {
        super(cFPGAName, UID, cFPGA, CFPGADescriptorSPIResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }

    @Override
    public CFPGADescriptor getData() {
        return (CFPGADescriptor)this.data;
    }
    
    
                
    
    
}
