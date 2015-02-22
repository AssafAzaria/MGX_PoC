/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.commands;

import com.thales.shared.sequences.SequenceInfo;

/**
 *
 * @author Asaf
 */
public class LoadSequenceXEDCommand extends XEDCommand {

    static private final int UID = 3211;
    public LoadSequenceXEDCommand(int senderUID, String senderName, SequenceInfo sequenceInfo) {
        super(UID, LoadSequenceXEDCommand.class.getSimpleName(), senderUID, senderName, sequenceInfo);
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }
    
    @Override
    public SequenceInfo getData() {
        return (SequenceInfo)data;
    }
    
}
