/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.XEDDescriptor;
import com.mgx.shared.events.CommandOKResponse;

public class UpdateSettingsMGXCommand extends Command {

    final static int UID = 6778;

    public UpdateSettingsMGXCommand(int listenerUID, String senderName, CFPGADescriptor cFPGAs[]) {
        super(UID, UpdateSettingsMGXCommand.class.getSimpleName(), listenerUID, senderName, cFPGAs, CommandType.XEDCommand);

    }

    @Override
    public CFPGADescriptor[] getData() {
        return (CFPGADescriptor[]) data;
    }
   

    @Override
    public String dataToString() {
        CFPGADescriptor[] _data = this.getData();
        StringBuilder asString = new StringBuilder("Updating "+_data.length+" cFPGA(s)\n");
        int i = 0;
        for (CFPGADescriptor cFPGA: _data) {
            asString.append("cFPGA #"+(i++));
            asString.append(cFPGA.toString());
        }
        return asString.toString();
        
    }

    @Override
    public Class<CommandOKResponse> getResponseClass() {
        return CommandOKResponse.class;
    }

}
