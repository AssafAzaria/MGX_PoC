/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.servicesProvider;

import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.serviceprovider.commands.StoreSequenceDRCommand;
import com.mgx.shared.events.CommandErrorResponse;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.events.ConnectionActiveResponse;
import com.mgx.shared.serviceprovider.responses.StoreSequenceResponse;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.shared.networking.server.NewConnectionHandler;
import com.mgx.shared.sequences.SequenceInfo;
import com.mgx.shared.serviceprovider.commands.DeleteSequenceSPCommand;
import com.mgx.shared.serviceprovider.commands.LoadSequenceSPCommand;
import com.mgx.shared.serviceprovider.responses.DeleteSequenceResponse;
import com.mgx.shared.serviceprovider.responses.LoadSequenceResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class ServicesProviderManager extends CommandHandler implements NewConnectionHandler {

    private static ServicesProviderManager instance;
    ActivityLogger l = new ActivityLogger(ServicesProviderManager.class.getName());
    private ServicesServer server;

    private ServicesProviderManager() {
    }
    private ValidatorImpl validator;
    private DataProvider db;

    public static void start() {
        if (instance == null) {
            instance = new ServicesProviderManager();
            instance.init();
        }
    }

    private void init() {

        server = new ServicesServer(Configuration.ServicesProviderServerPort,
                Configuration.ServicesProviderServerMaxConnections, l);
        server.addCommandHandler(this);
        server.setNewConnectionHandler(this);
        server.start();

        validator = new ValidatorImpl();

        db = new FileSystemDataProvider();
        try {
            db.init();
        } catch (DataRepositoryErrorException ex) {
            l.logE("Failed to initilize DR");
            db = null;
            ex.getCause().printStackTrace();
        }
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
        l.logD("Got command " + command.getName());

        try {
            switch (command.getName()) {
                case "StoreSequenceDRCommand": {
                    StoreSequenceDRCommand incoming = (StoreSequenceDRCommand) command;
                    try {
                        int seqID = db.storeSequence(incoming.getData());
                        connection.transmit(new StoreSequenceResponse(command.getSenderUID(), this.getName(), seqID));
                    } catch (DataRepositoryErrorException ex) {
                        ex.printStackTrace();
                        connection.transmit(new CommandErrorResponse(command, this.getName(), ex.toString()));

                    }
                    break;
                }
                case "LoadSequenceSPCommand": {

                    LoadSequenceSPCommand loadCommand = (LoadSequenceSPCommand) command;
                    SequenceInfo seq = db.getSequenceByUID(loadCommand.getSequenceID());
                    if (seq == null) {
                        connection.transmit(new CommandErrorResponse(command, this.getName(), "Sequence with ID " + loadCommand.getSequenceID() + " doesn't exist"));
                    } else {
                        connection.transmit(new LoadSequenceResponse(command.getSenderUID(), this.getName(), seq));
                    }

                    break;
                }
                case "DeleteSequenceSPCommand": {
                    DeleteSequenceSPCommand deleteCommand = (DeleteSequenceSPCommand) command;
                    try {
                        db.deleteSequence(deleteCommand.getSequenceID());
                        connection.transmit(new DeleteSequenceResponse(deleteCommand.getSenderUID(), this.getName(), deleteCommand.getSequenceID()));
                    } catch (DataRepositoryErrorException ex) {
                        connection.transmit(new CommandErrorResponse(command, this.getName(), ex.getMessage()));
                        ex.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException ex) {
            l.logE("Failed to transmit response - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
