/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.sequences.SequenceInfo;


public class SetSequnceCommand extends Command {
    private static final int UID = 9987;
    public SetSequnceCommand(int senderUID, String senderName, SequenceInfo sequence) {
        super(UID, SetSequnceCommand.class.getSimpleName(), senderUID, senderName, sequence, CommandType.ControlCommand);
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
