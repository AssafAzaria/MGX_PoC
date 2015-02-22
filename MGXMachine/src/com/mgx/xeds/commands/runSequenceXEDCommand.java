/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.commands;

import com.mgx.manager.XedArrayProxy;


public class runSequenceXEDCommand extends XEDCommand {
    static final private int UID=761;
    public runSequenceXEDCommand() {
        super(UID, runSequenceXEDCommand.class.getSimpleName(), XedArrayProxy.UID, XedArrayProxy.class.getSimpleName(), null);
    }

    @Override
    public String dataToString() {
        return "no data";
    }
    
}
