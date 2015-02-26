/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xeds.mockup;

import com.mgx.xeds.commands.LoadSequenceXEDCommand;
import com.mgx.xeds.commands.SetPropertyXEDCommand;
import com.mgx.shared.XEDPropertyInfo;
import com.mgx.shared.XEDInfo;
import com.mgx.shared.Configuration;
import com.mgx.shared.networking.CommandHandler;
import java.io.IOException;
import java.net.Socket;
import com.mgx.xeds.commands.XEDCommand;
import com.mgx.xeds.events.PropertyValueUpdateXEDNotification;
import com.mgx.xeds.events.SendInfoXEDResponse;
import com.mgx.xeds.events.SequenceExecutionDoneXEDNotification;
import com.mgx.xeds.events.SequenceLoadedXEDResponse;
import com.mgx.xeds.events.XEDPropertyValueUpdateNotification;
import com.mgx.shared.XEDPropertyValueUpdate;
import com.mgx.shared.commands.Command;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.shared.networking.client.SPIClientConnection;
import com.mgx.shared.sequences.SequenceInfo;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Asaf
 */
public abstract class XEDBase extends CommandHandler {

    protected final ActivityLogger l;
    private String name;
    private String description;
    private int UID;
    private XEDPropertyInfo[] properties;

    protected ConnectionBase connection;
    private boolean isStarted = false;
    private SequenceInfo sequence = null;

    XEDBase(int UID, String name, String description) throws IOException {
        Socket socket = new Socket(Configuration.getLocalhost(), Configuration.ProxyServerPort);
        this.connection = new SPIClientConnection(socket);
        this.connection.setOwner(name);
        this.connection.addHandler(this);

        this.UID = UID;
        this.name = name;
        this.description = description;
        this.l = new ActivityLogger(name);
    }

    protected void start() {
        if (!isStarted) {
            this.connection.start();
            isStarted = true;
        }
    }

    public void runEngine() {
        Random random = new Random();
        int sleepDuration = 5000;
        int intr = 2;
        while (intr-- > 0) {
            try {
                Thread.sleep(sleepDuration);
                // ConcurrentHashMap<String, Float>value = new ConcurrentHashMap<>(properties.length);
                ArrayList<XEDPropertyInfo> props = new ArrayList<>(properties.length);
                if (properties == null) {
                    continue;

                }
                for (int i = 0; i < properties.length; i++) {
                    float tmp = random.nextInt(((int) (properties[i].max - properties[i].min)) + 1) + properties[i].min + properties[i].mesurementInterval;
                    properties[i].value = tmp;
                    break;
                    /*float tmp = random.nextInt(((int) (prop.max - prop.min)) + 1) + prop.min + prop.mesurementInterval;
                     if (tmp != prop.value) {
                     prop.value = tmp;
                       
                     //value.put(prop.name, tmp);
                        
                     }*/
                }

                XEDInfo info = new XEDInfo(name, UID, properties.clone());
                XEDPropertyValueUpdateNotification notify = new XEDPropertyValueUpdateNotification(this.getName(), info);
                this.connection.transmit(notify);
                info = null;
                sleepDuration = random.nextInt(15000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    protected void setProperties(XEDPropertyInfo[] properties) {
        this.properties = properties;
    }

    //abstract protected XEDPropertyInfo[] getProperties();
    abstract protected XEDCommand[] supportedCommands();

    @Override
    protected void handleCommand(Command command, ConnectionBase connection) {
        try {

            
            switch (command.getName()) {
                case "GetInfoXEDCommand":

                    XEDInfo info = new XEDInfo(this.getName(), UID, this.properties);
                    SendInfoXEDResponse response = new SendInfoXEDResponse(this.getName(), info);
                    connection.transmit(response);
                    break;
                case "SetPropertyXEDCommand":
                {
                    SetPropertyXEDCommand setCommand = (SetPropertyXEDCommand) command;
                    XEDPropertyValueUpdate property = setCommand.getData();
                    for (XEDPropertyInfo xedProperty : this.properties) {
                        if (xedProperty.name.equals(property.name)) {
                            xedProperty.value = property.newValue;
                            PropertyValueUpdateXEDNotification notify
                                    = new PropertyValueUpdateXEDNotification(
                                            name, new XEDPropertyValueUpdate(xedProperty.name, xedProperty.value, name, UID));
                            connection.transmit(notify);
                        }
                    }
                    break;
                }
                case "SetPropertiesXEDCommand":
                {
                    SetPropertyXEDCommand setCommand = (SetPropertyXEDCommand) command;
                    XEDPropertyValueUpdate property = setCommand.getData();
                    for (XEDPropertyInfo xedProperty : this.properties) {
                        if (xedProperty.name.equals(property.name)) {
                            xedProperty.value = property.newValue;
                            PropertyValueUpdateXEDNotification notify
                                    = new PropertyValueUpdateXEDNotification(
                                            name, new XEDPropertyValueUpdate(xedProperty.name, xedProperty.value, name, UID));
                            connection.transmit(notify);
                        }
                    }
                    break;
                }
                case "LoadSequenceXEDCommand" :
                {
                    LoadSequenceXEDCommand seq = (LoadSequenceXEDCommand)command;
                    sequence = seq.getData();
                    l.logD("saving new sequence + "+ sequence.toString());
                    connection.transmit(new SequenceLoadedXEDResponse(name, sequence.sequenceId));
                    break;
                }
                case "runSequenceXEDCommand":
                {
                    l.logD("Running sequence # "+sequence.sequenceId);
                    connection.transmit(new SequenceExecutionDoneXEDNotification(name, UID, sequence.sequenceId));
                    break;
                }
                default:
                    handleCustomCommand(command);
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private void updatePropertyAndNotify(ConnectionBase connection, XEDPropertyValueUpdate property) {
        
    }
    protected abstract void handleCustomCommand(Command command);
}
