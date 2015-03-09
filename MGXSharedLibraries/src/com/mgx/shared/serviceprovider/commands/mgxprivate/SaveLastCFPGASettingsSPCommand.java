/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.commands.mgxprivate;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.serviceprovider.responses.mgxprivate.LastCFPGASettingsSPResponse;

/**
 *
 * @author Asaf
 */
public class SaveLastCFPGASettingsSPCommand extends Command{
    private final static int commandUID = 99239;
    public SaveLastCFPGASettingsSPCommand(int senderUID, String senderName, CFPGADescriptor cFPGA) {
        super(commandUID, SaveLastCFPGASettingsSPCommand.class.getSimpleName(), 
                senderUID, senderName, cFPGA, CommandType.DataRepositoryCommand);
    }

    /**
     * No response is registered to this command;
     * CommandOKResponse will be send
     * @return 
     */
    @Override
    public Class<CommandOKResponse> getResponseClass() {
        return CommandOKResponse.class;
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }

    @Override
    public CFPGADescriptor getData() {
        return (CFPGADescriptor)data;
    }
    
    
    
}
