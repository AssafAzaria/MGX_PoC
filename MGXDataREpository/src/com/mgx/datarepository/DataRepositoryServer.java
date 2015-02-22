/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.datarepository;

import com.thales.shared.Configuration;
import com.thales.shared.commands.Command;
import com.thales.shared.events.Event;
import com.thales.shared.loggers.ActivityLogger;
import com.thales.shared.networking.CommandHandler;
import com.thales.shared.networking.server.IPCommandsServer;

/**
 *
 * @author Asaf
 */
class DataRepositoryServer extends IPCommandsServer<Command, Event, CommandHandler>{

    private static ActivityLogger l = new ActivityLogger(DataRepositoryServer.class.getSimpleName());
    public DataRepositoryServer(int port, int maxConnections, ActivityLogger logger) {
        super(port, maxConnections, logger);
    }
    
}
