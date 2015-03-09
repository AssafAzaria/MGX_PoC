/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.commands;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.sequences.SequenceInfo;

/**
 *
 * @author Asaf
 */
public class StoreSettingsSPCommand extends Command{

    private static final int UID = 77656;
    private final String settingsName;
    public StoreSettingsSPCommand(int senderUID, String senderName, String settingsName, CFPGADescriptor cFPGASettings) {
        super(UID, StoreSettingsSPCommand.class.getSimpleName(), senderUID, senderName, cFPGASettings, CommandType.DataRepositoryCommand);
        this.settingsName = settingsName;
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }
    
    public String getSettingName() {
        return this.settingsName;
    }

    @Override
    public CFPGADescriptor getData() {
        return (CFPGADescriptor)this.data;
    }

    @Override
    public Class<?> getResponseClass() {
        return CommandOKResponse.class;
    }

    
   
    
}
