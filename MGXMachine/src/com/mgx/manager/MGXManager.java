/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.manager;

import com.mgx.shared.events.Response;
import com.mgx.states.States;
import com.mgx.shared.networking.ClientsTransmiter;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.ConnectionActiveResponse;
import com.mgx.shared.events.Notification;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.networking.server.MainServer;
import com.mgx.shared.Configuration;
import com.mgx.shared.networking.server.NewConnectionHandler;
import com.mgx.shared.commands.SetSequnceCommand;
import com.mgx.shared.commands.StartSequenceCommand;
import com.mgx.shared.events.CommandErrorResponse;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.networking.ResponseHandler;
import com.mgx.shared.networking.ServiceProviderConnector;
import com.mgx.shared.networking.Transmitable;
import com.mgx.shared.networking.client.IPConnection;
import com.mgx.shared.sequences.SequenceInfo;
import com.sun.corba.se.impl.activation.ServerMain;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class MGXManager implements CommandHandler, ClientsTransmiter, NewConnectionHandler {

    private ActivityLogger l = new ActivityLogger(MGXManager.class.getName());
    private MainServer server;
    private IPConnection serviceProviderLink;
    private XedArrayProxy xedProxy;

    private ManagerStates currentState = ManagerStates.Initializing;

    public SPConnector SPHandler = new SPConnector();

    public class SPConnector implements ResponseHandler, ServiceProviderConnector {

        private final HashMap<String, Command> commands = new HashMap();

        @Override
        public void handleResponse(Response response, ConnectionBase connectoin) {
            l.logD("Got response " + response.getName());

            if (response instanceof CommandErrorResponse) {
                CommandErrorResponse error = (CommandErrorResponse)response;
                commands.remove(error.getInvokerCommandClassName());
                l.logE("Command Eror ::"+error.toString());
            } else {
                //find the caller command
                Command cmd = commands.get(response.getClass().getName());
                if (cmd != null) {
                    cmd.execute();
                } else {
                    l.logE("Bad state - no assosiate command found");
                }
            }
        }

        @Override
        public String getName() {
            return SPConnector.class.getSimpleName();
        }

        @Override
        public void request(Command request) throws InterruptedException {
            serviceProviderLink.transmit(request);

        }

        @Override
        public void transmitAndExecute(Command request) throws InterruptedException {
            commands.put(request.getResponseClass().getName(), request);
            
            
            request.setAction(null);
            serviceProviderLink.transmit(request);
        }

        @Override
        public void connect() {
            try {
                serviceProviderLink = new IPConnection(Configuration.ServicesProviderServerPort, Configuration.getLocalhost());
                serviceProviderLink.setOwner(this.getName());
                serviceProviderLink.addHandler(SPHandler);
                serviceProviderLink.start();
            } catch (IOException ex) {
                l.logE("Failed to connect to service provider server");
                ex.printStackTrace();
                serviceProviderLink = null;
            }
        }

    }

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
    public void handleCommand(Command command, ConnectionBase connection) {
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

    private boolean storeSequence(SequenceInfo seq) {
        return false;
    }

    private SequenceInfo loadSequence(int SequenceID) {
        return null;
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

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
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

    private MGXManager() {

    }
    static MGXManager manager = null;

    public static MGXManager getInstance() {
        if (manager == null) {
            manager = new MGXManager();
        }

        return manager;
    }

    public void startSystem() throws IOException {

        serverStart();

        SPHandler.connect();
        XGProxyStart();

        currentState = ManagerStates.Ready;

    }

    private void serverStart() {

        server = new MainServer();
        server.setNewConnectionHandler(this);
        server.addCommandHandler(this);
        server.start();
    }

    private void XGProxyStart() {
        xedProxy = new XedArrayProxy(this);
        xedProxy.setServiceProviderConnector(SPHandler);
        xedProxy.init();

    }

    /**
     * @return XedArrayProxy
     */
    public XedArrayProxy getXGProxy() {
        return xedProxy;
    }

}
