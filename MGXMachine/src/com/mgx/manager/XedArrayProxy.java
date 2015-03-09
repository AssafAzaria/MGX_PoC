/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.manager;

import com.mgx.spi.commands.CFPGAIndetifySPICommand;
import com.mgx.shared.networking.ClientsTransmiter;
import com.mgx.shared.Configuration;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.commands.Command;
import com.mgx.shared.commands.UpdateSettingsMGXCommand;
import com.mgx.shared.events.CommandErrorResponse;
import com.mgx.shared.events.Event;
import com.mgx.shared.events.MGXNodesResponse;
import com.mgx.shared.events.Response;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.shared.networking.server.NewConnectionHandler;
import com.mgx.networking.server.XEDsProxyServer;
import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.XEDDescriptor;
import com.mgx.shared.XEDProperty;
import com.mgx.shared.commands.CommandAction;
import com.mgx.shared.commands.SequenceAcquisitionMGXCommand;
import com.mgx.shared.commands.UpdateSettingsMGXCommand;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.events.SequenceCompleteNotification;
import com.mgx.shared.events.SequenceLoadedNotification;
import com.mgx.shared.events.XEDUpdateNotification;
import com.mgx.shared.events.XPulseActualRunValuesNotification;
import com.mgx.shared.networking.ResponseHandler;
import com.mgx.shared.networking.ServiceProviderConnector;
import com.mgx.shared.sequences.SequenceInfo;
import com.mgx.shared.serviceprovider.commands.mgxprivate.LoadLastCFPGASettingsSPCommand;
import com.mgx.shared.serviceprovider.commands.mgxprivate.SaveLastCFPGASettingsSPCommand;
import com.mgx.spi.commands.AcuireSequenceSPICommand;
import com.mgx.spi.commands.RunSequenceSPICommand;
import com.mgx.spi.commands.UpdateXEDsPropertiesSPICommand;
import com.mgx.spi.responses.CFPGADescriptorSPIResponse;
import com.mgx.spi.responses.SequenceExecutionCompleteSPIREsponse;
import com.mgx.spi.responses.SequenceLoadedSPIResponse;
import com.mgx.spi.responses.XEDPropertiesUpdateResponse;
import com.mgx.spi.responses.XPulseActualValuesSPIResponse;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
//TODO: have different handler to Reponse and Notifications. i.e replace InboundParcelHandler;
public class XedArrayProxy implements ResponseHandler, NewConnectionHandler {

    public static final int UID = 66677;
    private final ActivityLogger l = new ActivityLogger(XedArrayProxy.class.getName());

    private boolean isSequenceRunning = false;
    private SequenceInfo currentSequence;
    private XEDsProxyServer server = new XEDsProxyServer();
    private ConcurrentHashMap<String, XEDDescriptor> XEDs = new ConcurrentHashMap<>(Configuration.XEDsMaxNum);
    private CFPGADescriptor cFPGA = null;
    /**
     * The trasmiter holds all the connections to the clients
     */
    private ClientsTransmiter clientsTransmiter;
    private ServiceProviderConnector SPConnector;
    private ConnectionBase SPIConnection;

    /*private class XEDDescriptor {

     public XEDDescriptor(XEDDescriptor info, SPIServerConnection connection) {
     this.info = info;
     this.connection = connection;

     }
     public XEDDescriptor info;
     public SPIServerConnection connection;
        
     //TODO: change to XEDState
     public boolean isMarkedForRunning = false;
     public boolean isSequenceLoaded = false;
     public boolean isRunning = false;

     }*/
    public XedArrayProxy(ClientsTransmiter transmiter) {
        this.clientsTransmiter = transmiter;

    }

    boolean isSequenceRunning() {
        return isSequenceRunning;
    }

    public void init() {

        server.addEventHandler(this);
        server.setNewConnectionHandler(this);
        server.start();
    }

    public void setServiceProviderConnector(ServiceProviderConnector connector) {
        this.SPConnector = connector;
    }

    private boolean validateSettings(CFPGADescriptor cFPGA) {
        l.logD("Settings are valid");
        //TODO:Implement settings validator
        return true;

    }

    public boolean proccessRequest(Command command, ConnectionBase connection) throws InterruptedException {
        boolean result = false;
        String errMsg = null;
        switch (command.getName()) {

            case "GetMGXNodesCommand":
                connection.transmit(new MGXNodesResponse(command.getSenderUID(), this.getName(), new CFPGADescriptor[]{cFPGA}));

                result = true;
                break;
            case "UpdateSettingsMGXCommand": {
                //TODO: error handling, if cFPGA doesn't match or empty
                UpdateSettingsMGXCommand updateCommand = (UpdateSettingsMGXCommand) command;
                CFPGADescriptor _cFPGA = updateCommand.getData()[0];
                //TODO:Error handling on bad settings
                validateSettings(cFPGA);

                //save settings
                SaveLastCFPGASettingsSPCommand cmd = new SaveLastCFPGASettingsSPCommand(UID, this.getName(), cFPGA);
                cmd.setAction(new CommandAction() {

                    @Override
                    public void executeOnResponse() {
                        l.logD("Settings saved on server successfuly");
                    }

                    @Override
                    public void executeOnError() {
                        l.logE("Error saving cFPGA settings ");
                    }

                });
                //TODO: error handling on transmit failure
                SPConnector.transmitAndExecute(cmd);
                SPIConnection.transmit(new UpdateXEDsPropertiesSPICommand(cFPGA.getXEDs()));
                connection.transmit(new CommandOKResponse(command, this.getName()));

                break;
            }

            case "SequenceAcquisitionMGXCommand": {
                SequenceAcquisitionMGXCommand saCommand = (SequenceAcquisitionMGXCommand) command;
                SPIConnection.transmit(new AcuireSequenceSPICommand(saCommand.getSenderUID(), saCommand.getSequence()));
                if (saCommand.isSWAcquisition()) {
                    SPIConnection.transmit(new RunSequenceSPICommand());
                }
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

    @Override
    public synchronized void handleResponse(Response response, ConnectionBase connection) {
        l.logI("got response " + response.getName());

        try {
            switch (response.getName()) {

                case "CFPGADescriptorSPIResponse": {

                    cFPGA = ((CFPGADescriptorSPIResponse) response).getData();
                    l.logD("Now connected with cFPGA \n" + cFPGA.toString());

                    //load last settings
                    //SPConnector.request();
                    LoadLastCFPGASettingsSPCommand cmd = new LoadLastCFPGASettingsSPCommand(UID, this.getName(), cFPGA.getUID());
                    cmd.setAction(new CommandAction() {

                        @Override
                        public void executeOnResponse() {
                            try {
                                SPIConnection.transmit(new UpdateXEDsPropertiesSPICommand(cmd.getResponseData().getXEDs()));
                            } catch (InterruptedException ex) {
                                throw new RuntimeException("Failed to transmit SetPropertiesSPICommand", ex);
                            }
                        }

                        @Override
                        public void executeOnError() {
                            l.logD("No settings loadded from service provider. Skipping last system state resume");
                        }
                    });
                    SPConnector.transmitAndExecute(cmd);

                    break;

                }
                case "XEDPropertiesUpdateResponse": {
                    XEDPropertiesUpdateResponse updateResponse = (XEDPropertiesUpdateResponse) response;
                    clientsTransmiter.notifyAllClients(new XEDUpdateNotification(this.getName(),
                            new CFPGADescriptor(cFPGA.getName(), cFPGA.getUID(), new XEDDescriptor[]{updateResponse.getData()})));
                    break;
                }

                case "XPulseActualValuesSPIResponse": {
                    XPulseActualValuesSPIResponse xpulseUpdate = (XPulseActualValuesSPIResponse) response;
                    clientsTransmiter.notifyAllClients(
                            new XPulseActualRunValuesNotification(
                                    this.getName(), xpulseUpdate.getSequenceID(), cFPGA.getUID(), xpulseUpdate.getXPulse()));
                    break;
                }

                case "SequenceExecutionCompleteSPIREsponse": {
                    SequenceExecutionCompleteSPIREsponse exeDone = (SequenceExecutionCompleteSPIREsponse) response;
                    clientsTransmiter.notifyAllClients(new SequenceCompleteNotification(this.getName(), exeDone.getSequenceID(), exeDone.getcFPGAUID()));
                    break;
                }
                case "SequenceLoadedSPIResponse": {
                    SequenceLoadedSPIResponse seqLoadded = (SequenceLoadedSPIResponse) response;
                    clientsTransmiter.notifyAllClients(new SequenceLoadedNotification(this.getName(), seqLoadded.getSequenceUID(), seqLoadded.getcFPGAUID()));
                    break;
                }
                default: {
                    l.logW("Command " + response.getName() + " not supported");
                    new Exception().printStackTrace();
                }
            }
        } catch (InterruptedException ex) {
            l.logE("Failed to transmit..");
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            if (ex.getCause() != null) {
                l.logE(ex.getCause().getMessage());
            } else {
                l.logE(ex.getMessage());
            }
            ex.printStackTrace();;
        }


        /*
         switch (response.getName()) {
         case "SendInfoXEDResponse": {
         SendInfoXEDResponse info = (SendInfoXEDResponse) response;
         if (addXed(info.getData(), (SPIServerConnection) connection)) {
         //server.addConnectoin(connection, info.getData().UID);
         }

         break;
         }
         case "XEDPropertyValueUpdateNotification": {
         XEDDescriptor info = (XEDDescriptor) response.data;
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
         }*/
    }

    //private ArrayList<XEDDescriptor> xedsNotificationList = new ArrayList<>(8);
    void loadAndRunSequence(SequenceInfo seq) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public Response getXEDs() {
        return null;
    }

    @Override
    public synchronized boolean newConnectionNotification(ConnectionBase connection) {
        boolean result = false;
        try {
            wait(200);
            Command getXeds = new CFPGAIndetifySPICommand();
            connection.transmit(getXeds);
            this.SPIConnection = connection;
            result = true;
        } catch (InterruptedException ex) {
            l.logE("Failed to send command to XED");
            ex.printStackTrace();
        }
        return result;

    }

    private boolean allXEDsLoadedSequence() {

        throw new UnsupportedOperationException("not implemented");

    }

    private boolean allXEDSFinishedSequenceExecution() {
        throw new UnsupportedOperationException("not implemented");
    }

    private void runSequence() {
        throw new UnsupportedOperationException("not implemented");

    }

}
