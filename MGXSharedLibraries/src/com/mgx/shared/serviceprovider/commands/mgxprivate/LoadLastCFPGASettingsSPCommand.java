/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.commands.mgxprivate;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.commands.Command;
import com.mgx.shared.serviceprovider.responses.mgxprivate.LastCFPGASettingsSPResponse;

/**
 *
 * @author Asaf
 */
public class LoadLastCFPGASettingsSPCommand extends Command{
    private final static int commandUID = 99238;
    public LoadLastCFPGASettingsSPCommand(int senderUID, String senderName, int cFPGAID) {
        super(commandUID, LoadLastCFPGASettingsSPCommand.class.getSimpleName(), 
                senderUID, senderName, cFPGAID, CommandType.DataRepositoryCommand);
    }

    @Override
    public Class<LastCFPGASettingsSPResponse> getResponseClass() {
        return LastCFPGASettingsSPResponse.class;
    }

    @Override
    public String dataToString() {
        return this.getData().toString();
    }

    @Override
    public Integer getData() {
        return (Integer)data;
    }
    
    @Override
    public CFPGADescriptor getResponseData() {
        return (CFPGADescriptor)super.getResponseData();
    }
    
}
