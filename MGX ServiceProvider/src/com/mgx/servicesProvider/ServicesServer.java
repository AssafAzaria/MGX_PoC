/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.servicesProvider;

import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.server.IPCommandsServer;

/**
 *
 * @author Asaf
 */
class ServicesServer extends IPCommandsServer<Command, Event, CommandHandler>{

    private static ActivityLogger l = new ActivityLogger(ServicesServer.class.getSimpleName());
    public ServicesServer(int port, int maxConnections, ActivityLogger logger) {
        super(port, maxConnections, logger);
    }
    
}
