/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.manager;

import com.thales.shared.XEDInfo;
import com.thales.shared.XEDPropertyInfo;
import com.mgx.xeds.commands.GetInfoXEDCommand;
import com.mgx.xeds.commands.SetPropertyXEDCommand;
import com.mgx.xeds.events.PropertyValueUpdateXEDNotification;
import com.mgx.xeds.events.SendInfoXEDResponse;
import com.thales.shared.networking.ClientsTransmiter;
import com.thales.shared.Configuration;
import com.thales.shared.XEDPropertyValueUpdate;
import com.thales.shared.loggers.ActivityLogger;
import com.thales.shared.commands.Command;
import com.thales.shared.commands.XEDSetPropertyCommand;
import com.thales.shared.events.CommandErrorResponse;
import com.thales.shared.events.CommandOKResponse;
import com.thales.shared.events.Event;
import com.thales.shared.events.GetXEDsResponse;
import com.thales.shared.events.Response;
import com.thales.shared.events.XEDUpdateNotification;
import com.thales.shared.networking.ResponseHandler;
import com.thales.shared.networking.client.ConnectionBase;
import com.mgx.networking.server.SPIServerConnection;
import com.thales.shared.networking.server.NewConnectionHandler;
import com.mgx.networking.server.XEDsProxyServer;
import com.mgx.xeds.commands.LoadSequenceXEDCommand;
import com.mgx.xeds.commands.runSequenceXEDCommand;
import com.mgx.xeds.events.SequenceLoadedXEDResponse;
import com.mgx.xeds.events.SequenceExecutionDoneXEDNotification;
import com.thales.shared.XED;
import com.thales.shared.commands.XEDSetPropertiesCommand;
import com.thales.shared.events.SequenceCompleteNotificatoin;
import com.thales.shared.networking.EventsHandler;
import com.thales.shared.sequences.SequenceInfo;
import com.thales.shared.sequences.XPulseInfo;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class XedArrayProxy extends EventsHandler implements  NewConnectionHandler {

    public static final int UID = 66677;
    private final ActivityLogger l = new ActivityLogger(XedArrayProxy.class.getName());

    private boolean isSequenceRunning = false;
    private SequenceInfo currentSequence;
    private XEDsProxyServer server = new XEDsProxyServer();
    private ConcurrentHashMap<String, XEDDescriptor> XEDs = new ConcurrentHashMap<>(Configuration.XEDsMaxNum);
    private ClientsTransmiter transmiter;

    private class XEDDescriptor {

        public XEDDescriptor(XEDInfo info, SPIServerConnection connection) {
            this.info = info;
            this.connection = connection;

        }
        public XEDInfo info;
        public SPIServerConnection connection;
        
        //TODO: change to XEDState
        public boolean isMarkedForRunning = false;
        public boolean isSequenceLoaded = false;
        public boolean isRunning = false;

    }

    public XedArrayProxy(ClientsTransmiter transmiter) {
        this.transmiter = transmiter;

    }

    boolean isSequenceRunning() {
        return isSequenceRunning;
    }

    public void init() {

        server.addEventHandler(this);
        server.setNewConnectionHandler(this);
        server.start();
    }

    public void dispatchXEDCommand(Command c) {

    }

    public boolean proccessRequest(Command command, ConnectionBase connection) throws InterruptedException {
        boolean result = false;
        String errMsg = null;
        switch (command.getName()) {

            case "GetXEDsCommand":
                l.logE("*****Sending XEDsResponse");
                Event event = new GetXEDsResponse(command.getSenderUID(), this.getName(), getAsXEDInfo());
                connection.transmit(event);
                result = true;
                break;
            case "XEDSetPropertyCommand": {
                XEDSetPropertyCommand updateCommand = (XEDSetPropertyCommand) command;
                XEDPropertyValueUpdate property = updateCommand.getData();
                XEDDescriptor xed = XEDs.get(property.XEDName);

                if (xed != null) {
                    for (XEDPropertyInfo prop : xed.info.properties) {
                        if (prop.name.equals(property.name)) {
                            SetPropertyXEDCommand setCommand = new SetPropertyXEDCommand(property);
                            try {
                                xed.connection.transmit(setCommand);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            //server.sendMessageToClient(xed.UID, command);
                            Event response = new CommandOKResponse(command, this.getName());
                            connection.transmit(response);
                            result = true;
                        }
                    }
                    if (!result) {
                        errMsg = "XED " + property.XEDName + " (UID " + property.XEDUID + ")"
                                + "Dosn't support the property:" + property.name;
                    }
                } else {
                    errMsg = "Can't find XED " + property.XEDName + " (UID " + property.XEDUID + ")";
                    l.logE(errMsg);

                }

                break;
            }
            case "UpdateXed":
                XED xedo = new XED(1);
                //xedo.voltage.idleVoltage.
                break;
            case "XEDSetPropertiesCommand": {
                //TODO: add support for same property in different XEDs(UID) 
                XEDSetPropertiesCommand propertiesCommand = (XEDSetPropertiesCommand) command;
                XEDPropertyValueUpdate properties[] = propertiesCommand.getData();

                for (XEDPropertyValueUpdate property : properties) {

                    XEDDescriptor xed = getpropertyXED(property);

                    if (xed != null) {
                        for (XEDPropertyInfo prop : xed.info.properties) {
                            if (prop.name.equals(property.name)) {
                                SetPropertyXEDCommand setCommand = new SetPropertyXEDCommand(property);
                                try {
                                    xed.connection.transmit(setCommand);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                //server.sendMessageToClient(xed.UID, command);
                                Event response = new CommandOKResponse(command, this.getName());
                                connection.transmit(response);
                                result = true;
                            }
                        }
                        if (!result) {
                            errMsg = "XED " + property.XEDName + " (UID " + property.XEDUID + ")"
                                    + "Dosn't support the property:" + property.name;
                        }
                    } else {
                        errMsg = "Can't find XED " + property.XEDName + " (UID " + property.XEDUID + ")";
                        l.logE(errMsg);

                    }
                } //for
                break;
            }
            default:
                errMsg = "Command " + command + " is not supported";
                l.logE(errMsg);
        }

        //handle errors
        if (!result && errMsg != null) {
            Event event = new CommandErrorResponse(command, this.getName(), errMsg);
            connection.transmit(event);
        }
        return result;
    }

    private XEDDescriptor getpropertyXED(XEDPropertyValueUpdate property) {
        return XEDs.get(property.XEDName);
    }

    @Override
    public synchronized void handleEvent(Event response, ConnectionBase connection) {
        l.logI("got response " + response.getName());
        switch (response.getName()) {
            case "SendInfoXEDResponse": {
                SendInfoXEDResponse info = (SendInfoXEDResponse) response;
                if (addXed(info.getData(), (SPIServerConnection) connection)) {
                    //server.addConnectoin(connection, info.getData().UID);
                }

                break;
            }
            case "XEDPropertyValueUpdateNotification": {
                XEDInfo info = (XEDInfo) response.data;
                StringBuilder tmp = new StringBuilder("\n " + info.Name + ":\n");
                for (XEDPropertyInfo prop : info.properties) {
                    tmp.append("property " + prop.name + " value=" + prop.value + "\n");
                }
                l.logE("XEDsProxy::handleREsponse:Got XEDPropertyValueUpdateNotification:" + tmp.toString());
                updateXED(info);
                break;
            }
            case "PropertyValueUpdateXEDNotification": {
                PropertyValueUpdateXEDNotification IncommingNotification
                        = (PropertyValueUpdateXEDNotification) response;
                XEDUpdateNotification outgoingNotificatoin
                        = new XEDUpdateNotification(this.getName(),
                                IncommingNotification.getData());
                //server.sendMessageToAllClients(outgoingNotificatoin);
                transmiter.notifyAllClients(outgoingNotificatoin);
                break;
            }
            case "SequenceLoadedXEDResponse": {

                SequenceLoadedXEDResponse resp = (SequenceLoadedXEDResponse) response;
                l.logD("XED " + resp.dispatcherName + " loaded sequence " + resp.getData());
                //TODO: check that sequence ID coresponde between response and the waiting list
                XEDDescriptor xed = XEDs.get(resp.dispatcherName);
                xed.isSequenceLoaded = true;
                if (allXEDsLoadedSequence()) {
                    l.logD("All XEDs loaded sequence...starting sequence");
                    runSequence();
                }

                break;
            }
            case "SequenceExecutionDoneXEDNotification":
            {
                SequenceExecutionDoneXEDNotification notification = (SequenceExecutionDoneXEDNotification)response;
                XEDDescriptor xed = XEDs.get(notification.dispatcherName);
                l.logD("XED "+xed.info.Name+" ("+xed.info.UID+") finished executing of sequence # "+ notification.getData().sequenceID);
                xed.isRunning = false;
                xed.isMarkedForRunning = false;
                xed.isSequenceLoaded = false;
                
                if(allXEDSFinishedSequenceExecution()) {
                    
                    transmiter.notifyAllClients(
                            new SequenceCompleteNotificatoin(
                                    this.getName(), 
                                    notification.getData().sequenceID));
                    this.isSequenceRunning = false;
                }
                break;
                
            }
        }

    }

    private synchronized boolean addXed(XEDInfo XED, SPIServerConnection connection) {
        XEDDescriptor descriptor = new XEDDescriptor(XED, connection);
        l.logI("trying to add XED " + XED.Name);
        boolean isAdded = false;
        if (XEDs.putIfAbsent(XED.Name, descriptor) != null) {
            l.logW("XED " + XED.Name + " already exsits ignoring");
        } else {
            l.logI("XED " + XED.Name + " with " + XED.properties.length + " properties added");
            isAdded = true;

        }
        return isAdded;

    }

    //private ArrayList<XEDDescriptor> xedsNotificationList = new ArrayList<>(8);
    void loadAndRunSequence(SequenceInfo seq) {
        if (!isSequenceRunning) {
            isSequenceRunning = true;
            currentSequence = seq;

            XEDs.values().stream().forEach(xed -> {
                for (XPulseInfo pulse : seq.pulses) {
                    //check if xed need to run sequence
                    if (pulse.XEDNum == xed.info.UID) {
                        if (xed.isMarkedForRunning) {
                            l.logD("XED " + xed.info.Name + " was already marked... continue");
                            continue;
                        }

                        xed.isMarkedForRunning = true;
                        try {
                                //notify xed to load sequence

                            xed.connection.transmit(new LoadSequenceXEDCommand(UID, this.getName(), seq));
                        } catch (InterruptedException ex) {
                            l.logE("Failed to send sequence to xed #" + xed.info.UID);
                            //TODO: abort sequance (send aboart to other xeds and send error back to client)
                            ex.printStackTrace();
                        }

                    }
                }
            });
                //waitForAllXedsToLoadSequance();

        }
    }

    private synchronized void updateXED(XEDInfo info) {
        XEDDescriptor oldDescriptor = XEDs.get(info.Name);
        if (oldDescriptor != null) {
            XEDDescriptor newDescriptor = new XEDDescriptor(info, oldDescriptor.connection);
            if (XEDs.replace(info.Name, oldDescriptor, newDescriptor)) {
                l.logI("XED " + info.Name + " was updated");
                notifyClients_update(info);
            } else {
                l.logW("XED " + info.Name + " not updated (either not exsist or equal");
            }
        }
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    private void notifyClients_update(XEDInfo info) {
        //transmiter.notifyAllClients(new XEDUpdateNotification(this.getClass().getName(), info));
    }

    public Response getXEDs() {
        return null;
    }

    private XEDInfo[] getAsXEDInfo() {
        XEDInfo xeds[] = new XEDInfo[XEDs.size()];
        int i = 0;
        Enumeration elements = XEDs.elements();
        while (elements.hasMoreElements()) {
            XEDDescriptor xed = (XEDDescriptor) elements.nextElement();
            xeds[i++] = xed.info;

        }
        return xeds;
    }

    @Override
    public synchronized boolean newConnectionNotification(ConnectionBase connection) {
        boolean result = false;
        try {
            wait(200);
            Command getXed = new GetInfoXEDCommand();
            connection.transmit(getXed);
            result = true;
        } catch (InterruptedException ex) {
            l.logE("Failed to send command to XED");
            ex.printStackTrace();
        }
        return result;

    }

    private boolean allXEDsLoadedSequence() {

        if (XEDs.values().stream().allMatch((xed) -> (xed.isMarkedForRunning && xed.isSequenceLoaded))) {
            return true;
        }

        return false;
    }
    
    private boolean allXEDSFinishedSequenceExecution() {
        if(XEDs.values().stream().allMatch(xed->(!xed.isRunning))) {
            return true;
        }
        return false;
    }

    private void runSequence() {
        XEDs.values().stream().forEach(xed -> {
            if (xed.isMarkedForRunning) {
                l.logD("Sending runSequenceXEDCommand to " + xed.info.Name + "(" + xed.info.UID + ")");
                try {
                    xed.connection.transmit(new runSequenceXEDCommand());
                    xed.isRunning = true;
                } catch (InterruptedException ex) {
                    l.logE("Failed to send command....");
                    ex.printStackTrace();
                }
            }
        });

    }
}
