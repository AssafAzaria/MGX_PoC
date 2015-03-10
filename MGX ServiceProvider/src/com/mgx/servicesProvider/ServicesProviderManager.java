/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.servicesProvider;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.serviceprovider.commands.StoreSequenceSPCommand;
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
import com.mgx.shared.serviceprovider.commands.LoadSettingsSPCommand;
import com.mgx.shared.serviceprovider.commands.StoreSettingsSPCommand;
import com.mgx.shared.serviceprovider.commands.GetStoredSequencesSPCommand;
import com.mgx.shared.serviceprovider.commands.mgxprivate.LoadLastCFPGASettingsSPCommand;
import com.mgx.shared.serviceprovider.commands.mgxprivate.SaveLastCFPGASettingsSPCommand;
import com.mgx.shared.serviceprovider.responses.DeleteSequenceResponse;
import com.mgx.shared.serviceprovider.responses.LoadSequenceResponse;
import com.mgx.shared.serviceprovider.responses.LoadSettingsResponse;
import com.mgx.shared.serviceprovider.responses.StoredSequencesListSPResponse;
import com.mgx.shared.serviceprovider.responses.mgxprivate.LastCFPGASettingsSPResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class ServicesProviderManager implements CommandHandler, NewConnectionHandler {
    
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
    
    @Override
    public String getName() {
        return ServicesProviderManager.class.getSimpleName();
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
    public void handleCommand(Command command, ConnectionBase connection) {
        l.logD("Got command " + command.getName());
        
        try {
            switch (command.getName()) {
                case "StoreSequenceSPCommand": {
                    StoreSequenceSPCommand incoming = (StoreSequenceSPCommand) command;
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
                
                case "GetStoredSequencesSPCommand": {
                    GetStoredSequencesSPCommand getCmd = (GetStoredSequencesSPCommand) command;
                    try {
                        connection.transmit(new StoredSequencesListSPResponse(getCmd.getSenderUID(), this.getName(), db.getStoredSequences()));
                    } catch (DataRepositoryErrorException ex) {
                        connection.transmit(new CommandErrorResponse(command, this.getName(), "Failed to get stored sequences list"));
                    }
                    break;
                }
                
                case "LoadLastCFPGASettingsSPCommand": {
                    LoadLastCFPGASettingsSPCommand request = (LoadLastCFPGASettingsSPCommand) command;
                    try {
                        
                        CFPGADescriptor settings = db.loadCFPGASettings(request.getData());
                        connection.transmit(new LastCFPGASettingsSPResponse(request.getSenderUID(), this.getName(), settings));
                    } catch (DataRepositoryErrorException ex) {
                        l.logE("Failed to load settings");
                        ex.printStackTrace();
                        connection.transmit(new CommandErrorResponse(command, this.getName(), "Failed to load settings"));
                    }
                    
                    break;
                }
                
                case "SaveLastCFPGASettingsSPCommand": {
                    
                    try {
                        SaveLastCFPGASettingsSPCommand request = (SaveLastCFPGASettingsSPCommand) command;
                        db.storeCFPGASettings(request.getData());
                        connection.transmit(new CommandOKResponse(request, this.getName()));
                    } catch (DataRepositoryErrorException ex) {
                        l.logE("Failed to store settings");
                        ex.printStackTrace();
                        connection.transmit(new CommandErrorResponse(command, this.getName(), "Failed to store settings"));
                    }
                    
                    break;
                }
                
                case "StoreSettingsSPCommand": {
                    StoreSettingsSPCommand request = (StoreSettingsSPCommand) command;
                    try {
                        db.storeClientSettings(request.getSettingName(), request.getData());
                        connection.transmit(new CommandOKResponse(command, this.getName()));
                    } catch (DataRepositoryErrorException ex) {
                        connection.transmit(new CommandErrorResponse(command, this.getName(), "Failed to store settings"));
                    }
                    break;
                }
                
                case "LoadSettingsSPCommand": {
                    LoadSettingsSPCommand request = (LoadSettingsSPCommand) command;
                    try {
                        CFPGADescriptor cFPGA = db.loadClientSettings(request.getData());
                        connection.transmit(new LoadSettingsResponse(request.getSenderUID(), this.getName(), cFPGA));
                    } catch (DataRepositoryErrorException ex) {
                        connection.transmit(new CommandErrorResponse(command, this.getName(), "Failed to load settings " + request.getData()));
                    }
                    break;
                }
            }
        } catch (InterruptedException ex) {
            l.logE("Failed to transmit response - " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
