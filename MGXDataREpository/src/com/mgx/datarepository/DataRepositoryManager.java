/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.datarepository;

import com.thales.shared.Configuration;
import com.thales.shared.commands.Command;
import com.thales.shared.events.ConnectionActiveResponse;
import com.thales.shared.loggers.ActivityLogger;
import com.thales.shared.networking.CommandHandler;
import com.thales.shared.networking.client.ConnectionBase;
import com.thales.shared.networking.server.NewConnectionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class DataRepositoryManager extends CommandHandler implements NewConnectionHandler{
    private static DataRepositoryManager instance;
    ActivityLogger l = new ActivityLogger(DataRepositoryManager.class.getName());
    private DataRepositoryServer server;
    private DataRepositoryManager(){}
    private ValidatorImpl validator;
    private DataProvider db;
    public static void start() {
        if (instance == null) {
            instance = new DataRepositoryManager();
            instance.init(); 
        }
    }

    private void init() {
        server = new DataRepositoryServer(Configuration.DataRepositoryServerPort, Configuration.DataRepositoryServerMaxConnections, l);
        server.addCommandHandler(this);
        server.start();
        
        validator = new ValidatorImpl();
        
        
        db = new FileSystemDataProvider();
    }

    @Override
    public boolean newConnectionNotification(ConnectionBase connection) {
        boolean result = false;
        try {
            connection.transmit(new ConnectionActiveResponse(this.getName()));
            result = true;
        } catch (InterruptedException ex) {
            l.logE("Failed to transmit to new client...");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    protected void handleCommand(Command command, ConnectionBase connection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
