/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking.server;

import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.networking.ResponseHandler;
import com.mgx.shared.networking.Transmitable;
import com.mgx.shared.networking.client.ConnectionBase;
import java.io.IOException;
import java.net.Socket;


public class IPServerConnection<
        Inbound extends Command, 
        Outbound extends Event, 
        Handler extends ResponseHandler> 
extends ConnectionBase {

    public IPServerConnection(Socket socket) throws IOException {
        super(socket);
        isReady = true; //server side connection...
    }

   

    @Override
    protected String getInstanceClassName() {
        return IPServerConnection.class.getSimpleName();
    }
    
}
