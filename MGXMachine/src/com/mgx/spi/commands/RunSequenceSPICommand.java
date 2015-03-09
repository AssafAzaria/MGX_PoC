/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.spi.commands;

import com.mgx.manager.XedArrayProxy;


public class RunSequenceSPICommand extends SPICommand {
    static final private int UID=761;
    public RunSequenceSPICommand() {
        super(UID, RunSequenceSPICommand.class.getSimpleName(), null);
    }

    @Override
    public String dataToString() {
        return "no data";
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public Class<?> getResponseClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
