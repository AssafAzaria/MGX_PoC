/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.manager;

import com.thales.mgx.DataRepository.DataRepository;

import com.mgx.shared.events.Response;
import com.mgx.shared.events.StageChangeNotification;
import com.mgx.states.States;
import com.mgx.shared.networking.ClientsTransmiter;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.ConnectionActiveResponse;
import com.mgx.shared.events.Notification;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.networking.server.MainServer;
import com.mgx.shared.networking.server.NewConnectionHandler;
import com.mgx.shared.commands.GetNextSequenceIDCommand;
import com.mgx.shared.commands.SetSequnceCommand;
import com.mgx.shared.commands.StartSequenceCommand;
import com.mgx.shared.events.CommandErrorResponse;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.events.SequenceIDResponse;
import com.mgx.shared.sequences.SequenceInfo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class GenArrayManager extends CommandHandler implements ClientsTransmiter, NewConnectionHandler {

    private ActivityLogger l = new ActivityLogger(GenArrayManager.class.getName());
    private MainServer server;
    private DataRepository dataRepos;
    private XedArrayProxy xedProxy;

    private ManagerStates currentState = ManagerStates.Initializing;

    @Override
    public void notifyAllClients(Notification notification) {
        server.sendMessageToAllClients(notification);
    }

    @Override
    public void notifyClient(String clientName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commandResponse(Response response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispatchErrorMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void handleCommand(Command command, ConnectionBase connection) {
        try {
            switch (command.getType()) {
                case XEDCommand:
                    l.logI("dispatching as XEDCommand ");

                     {
                        try {
                            xedProxy.proccessRequest(command, connection);

                        } catch (InterruptedException ex) {
                            l.logE("Failed to process command " + command.toString());
                            ex.printStackTrace();
                        }
                    }

                    break;
                
                case ControlCommand:
                    l.logI("dispatching as ManagerCommand ");
                    switch (command.getName()) {
                        case "GetNextSequenceIDCommand": {
                            Response response = null;
                            try {

                                int seqID = getNextSequenceID();
                                response = (Response) new SequenceIDResponse(
                                        command.getSenderUID(), this.getName(), seqID);
                            } catch (IOException ex) {
                                l.logE("getNextSequenceID() failed!!!");
                                ex.printStackTrace();
                                response = new CommandErrorResponse(command, this.getName(), null);
                            }
                            connection.transmit(response);

                        }
                        break;
                        case "SetSequnceCommand": {
                            SetSequnceCommand seq = (SetSequnceCommand) command;
                            l.logD("Trying to save sequance");
                            if (storeSequence(seq.getData())) {
                                l.logD("sequence saved");
                                connection.transmit(new CommandOKResponse(command, this.getName()));
                            } else {
                                l.logD("Failed to save sequence");
                                connection.transmit(new CommandErrorResponse(command, this.getName(), "Failed to store sequence"));
                            }

                            break;
                        }
                        case "StartSequenceCommand": {

                            if (!xedProxy.isSequenceRunning()) {
                                SequenceInfo seq = loadSequence(((StartSequenceCommand) command).getData());
                                if (seq != null) {
                                    xedProxy.loadAndRunSequence(seq);
                                } else {
                                    connection.transmit(new CommandErrorResponse(command, this.getName(), "Failed to load sequance"));
                                }
                            } else {
                                connection.transmit(new CommandErrorResponse(command, this.getName(), "Sequence is allready running"));
                            }
                            break;
                        }
                    }
                    break;
                default:
                    //TODO: handle command not supported
                    l.logE("Command " + command + " not supported");
            }
        } catch (InterruptedException ex) {
            l.logE("connection.transmit() failed");
            ex.printStackTrace();
        }
    }

    private synchronized int getNextSequenceID() throws IOException {

        String stringID = dataRepos.fetch("SequanceIDSeed");
        int id = Integer.parseInt(stringID);
        dataRepos.store("SequanceIDSeed", Integer.toString(id + 1));
        return id;
    }

    private static final String SEQUENCE_PREFIX = "sequnce";

    private boolean storeSequence(SequenceInfo seq) {
        return dataRepos.storeObject(SEQUENCE_PREFIX + seq.sequenceId, seq);
    }

    private SequenceInfo loadSequence(int SequenceID) {
        return (SequenceInfo) dataRepos.loadObject(SEQUENCE_PREFIX + SequenceID);
    }

    @Override
    public boolean newConnectionNotification(ConnectionBase connection) {
        boolean result = true;
        try {
            Response connectionOK = new ConnectionActiveResponse(this.getName());
            connection.transmit(connectionOK);
            result = true;
        } catch (InterruptedException ex) {
            l.logE("Failed to trasmit ConnectionActiveResponse to client");
            ex.printStackTrace();

        }
        return result;
    }

    public enum ManagerStates implements States {

        Initializing {
                    public String getName() {
                        return "Initializing";
                    }
                },
        Ready {
                    public String getName() {
                        return "Ready";
                    }
                },
        Busy {
                    public String getName() {
                        return "Ready";
                    }
                };

        public String toString() {
            return "ManagerStates::" + getName();
        }
    }

    private GenArrayManager() {

    }
    static GenArrayManager manager = null;

    public static GenArrayManager getInstance() {
        if (manager == null) {
            manager = new GenArrayManager();
        }

        return manager;
    }

    public void startSystem() {

        serverStart();

        dataRepositoryStart();
        XGProxyStart();

        currentState = ManagerStates.Ready;

    }

    private void serverStart() {

        server = new MainServer();
        server.setNewConnectionHandler(this);
        server.addCommandHandler(this);
        server.start();
    }

    private void dataRepositoryStart() {
        try {
            dataRepos = new DataRepository();
        } catch (IOException ex) {
            l.logE("Failed to start data repository...");
            ex.printStackTrace();
        }
    }

    private void XGProxyStart() {
        xedProxy = new XedArrayProxy(this);
        xedProxy.init();

    }

    /**
     * @return XedArrayProxy
     */
    public XedArrayProxy getXGProxy() {
        return xedProxy;
    }

}
