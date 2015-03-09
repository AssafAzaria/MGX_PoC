/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.serviceprovider.commands;

import com.mgx.shared.commands.Command;
import com.mgx.shared.serviceprovider.responses.LoadSettingsResponse;

/**
 *
 * @author Asaf
 */
public class LoadSettingsSPCommand extends Command{

    static private final int UID = 23124;
    public LoadSettingsSPCommand(int senderUID, String senderName, String settingsName) {
        super(UID, LoadSettingsSPCommand.class.getSimpleName(), senderUID, senderName, settingsName, CommandType.DataRepositoryCommand);
    }

    @Override
    public String dataToString() {
        return this.getData();
    }

    @Override
    public String getData() {
        return (String)this.data;
    }

    @Override
    public Class<LoadSettingsResponse> getResponseClass() {
        return LoadSettingsResponse.class;
    }
    
}
