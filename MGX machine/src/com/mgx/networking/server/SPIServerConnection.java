/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.networking.server;

import com.thales.shared.commands.Command;
import com.thales.shared.events.Event;
import com.thales.shared.networking.ResponseHandler;
import com.thales.shared.networking.Transmitable;
import com.thales.shared.networking.CommandHandler;
import com.thales.shared.networking.client.ConnectionBase;
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
