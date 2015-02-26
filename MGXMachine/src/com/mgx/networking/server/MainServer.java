/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.networking.server;

import com.mgx.shared.networking.server.ServerBase;
import com.mgx.shared.networking.server.IPServerConnection;
import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.shared.networking.server.IPCommandsServer;
import java.io.IOException;
import java.net.Socket;

public class MainServer extends IPCommandsServer {

    private static ActivityLogger l = new ActivityLogger(MainServer.class.getSimpleName());
    public MainServer() {
        super(Configuration.MGXServerPort, Configuration.MGXServerMaxConnections, l);
    }
    

}
