/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking;
import com.mgx.shared.commands.Command;
import com.mgx.shared.networking.client.ConnectionBase;

public interface CommandHandler extends InboundParcelHandler {

    public void handleCommand(Command command, ConnectionBase connection);
    
    @Override
    default public void handleInboundParcel(Transmitable command, ConnectionBase connection) {
       
        handleCommand((Command)command, connection);
    }

    
}
