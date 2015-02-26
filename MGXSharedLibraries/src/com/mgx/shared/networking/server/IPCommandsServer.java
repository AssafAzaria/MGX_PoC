/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking.server;

import com.mgx.shared.networking.server.ServerBase;
import com.mgx.shared.networking.server.IPServerConnection;
import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import java.io.IOException;
import java.net.Socket;

public abstract class IPCommandsServer<
        Inbound extends Command, 
        Outbound extends Event, 
        Handler extends CommandHandler> 
extends ServerBase {

    private static ActivityLogger l = new ActivityLogger(IPCommandsServer.class.getSimpleName());

    public IPCommandsServer(int port, int maxConnections, ActivityLogger logger) {
        super(port, maxConnections, logger);
    }
    public int addCommandHandler(CommandHandler handler) {
        return super.addInboundHandler(handler);
    }
    @Override
    protected ConnectionBase createNewConnection(Socket socket) {
        try {
            
            IPServerConnection client = new IPServerConnection(socket);
            client.setOwner(this.getName());
            client.addHandler(this);
            return client;
        } catch (IOException ex) {
            l.logE("failed to create connection");
            ex.printStackTrace();
        }

        return null;
    }

}
