/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking.client;

import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.networking.ResponseHandler;
import java.io.IOException;
import java.net.Socket;


public class SPIClientConnection<Inbound extends Command, 
        Outbound extends Event, 
        Handler extends ResponseHandler> 
extends ConnectionBase{

    public SPIClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    @Override
    protected String getInstanceClassName() {
        return SPIClientConnection.class.getSimpleName();
    }
    
}
