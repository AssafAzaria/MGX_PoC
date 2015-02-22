/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.networking.server;

import com.thales.shared.networking.server.ServerBase;
import com.thales.shared.networking.server.IPServerConnection;
import com.thales.shared.Configuration;
import com.thales.shared.commands.Command;
import com.thales.shared.events.Event;
import com.thales.shared.loggers.ActivityLogger;
import com.thales.shared.networking.CommandHandler;
import com.thales.shared.networking.client.ConnectionBase;
import com.thales.shared.networking.server.IPCommandsServer;
import java.io.IOException;
import java.net.Socket;

public class MainServer extends IPCommandsServer {

    private static ActivityLogger l = new ActivityLogger(MainServer.class.getSimpleName());
    public MainServer() {
        super(Configuration.MGXServerPort, Configuration.MGXServerMaxConnections, l);
    }
    

}
