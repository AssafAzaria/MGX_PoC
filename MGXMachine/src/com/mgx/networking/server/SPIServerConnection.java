/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.networking.server;

import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import java.io.IOException;
import java.net.Socket;


public class SPIServerConnection
<Inbound extends Event, 
        Outbound extends Command, 
        Handler extends CommandHandler> 
extends ConnectionBase{

    public SPIServerConnection(Socket socket) throws IOException {
        super(socket);
        isReady = true; //server side connection...
    }

    
    @Override
    protected String getInstanceClassName() {
        return SPIServerConnection.class.getSimpleName();
    }
    
}
