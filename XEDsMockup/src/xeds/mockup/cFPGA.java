/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xeds.mockup;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.spi.commands.AcuireSequenceSPICommand;
import com.mgx.shared.XEDDescriptor;
import com.mgx.shared.Configuration;
import com.mgx.shared.networking.CommandHandler;
import java.io.IOException;
import java.net.Socket;
import com.mgx.spi.commands.SPICommand;
import com.mgx.spi.responses.CFPGADescriptorSPIResponse;
import com.mgx.spi.responses.SequenceExecutionCompleteSPIREsponse;
import com.mgx.spi.responses.SequenceLoadedSPIResponse;
import com.mgx.shared.XEDProperty;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.shared.networking.client.SPIClientConnection;
import com.mgx.shared.sequences.SequenceInfo;
import com.mgx.shared.sequences.XPulseInfo;
import com.mgx.spi.commands.RunSequenceSPICommand;
import com.mgx.spi.commands.UpdateXEDsPropertiesSPICommand;
import com.mgx.spi.responses.XEDPropertiesUpdateResponse;
import com.mgx.spi.responses.XPulseActualValuesSPIResponse;
import java.util.Random;

/**
 *
 * @author Asaf
 */
public class cFPGA implements CommandHandler {

    protected final ActivityLogger l;
    private String name;
    private String description;
    private int UID;

    private XEDDescriptor XEDs[];
    private CFPGADescriptor self;
    protected ConnectionBase connection;
    private boolean isStarted = false;
    private SequenceInfo sequence = null;

    cFPGA() throws IOException {

        this(55432/*UID*/, "Test " + cFPGA.class.getSimpleName(), "XEDs Array controller");
    }

    cFPGA(int UID, String name, String description) throws IOException {
        Socket socket = new Socket(Configuration.getLocalhost(), Configuration.ProxyServerPort);
        this.connection = new SPIClientConnection(socket);
        this.connection.setOwner(name);
        this.connection.addHandler(this);

        this.UID = UID;
        this.name = name;
        this.description = description;
        this.l = new ActivityLogger(name);

        XEDs = new XEDDescriptor[3];

        //first XED
        XEDProperty props[] = {
            new XEDProperty("Output Voltage", "kV", 0.5f, 300, 0, 75.3f),
            new XEDProperty("Output Curent", "Amp", 0.5f, 72, 0, 15),
            new XEDProperty(("Temprature"), "C", 0.2f, 175f, -30.4f, 72.1f)
        };

        XEDs[0] = new XEDDescriptor("1st XED", 1, props);

        //2nd XED
        XEDProperty props2[] = {
            new XEDProperty("Output Voltage", "kV", 0.5f, 300, 0, 65.3f),
            new XEDProperty("Output Curent", "Amp", 0.5f, 72, 0, 66),
            new XEDProperty(("Temprature"), "C", 0.2f, 175f, -30.4f, -10.1f)
        };
        XEDs[1] = new XEDDescriptor("2nd XED", 2, props2);

        //3rd
        XEDProperty props3[] = {
            new XEDProperty("Output Voltage", "kV", 0.5f, 300, 0, 55.3f),
            new XEDProperty("Output Curent", "Amp", 0.5f, 72, 0, 35),
            new XEDProperty(("Temprature"), "C", 0.2f, 175f, -30.4f, 40.1f)
        };
        XEDs[2] = new XEDDescriptor("3rd XED", 3, props3);

        self = new CFPGADescriptor(name, UID, XEDs);
    }

    @Override
    public String getName() {
        return this.getClass().getName();
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
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    //abstract protected XEDPropertyInfo[] getProperties();
    protected SPICommand[] supportedCommands() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public synchronized void handleCommand(Command command, ConnectionBase connection) {
        try {

            switch (command.getName()) {
                case "CFPGAIndetifySPICommand":

                    CFPGADescriptorSPIResponse cFPGA = new CFPGADescriptorSPIResponse(this.getName(), this.UID, self);
                    connection.transmit(cFPGA);
                    break;

                case "UpdateXEDsPropertiesSPICommand": {
                    UpdateXEDsPropertiesSPICommand update = (UpdateXEDsPropertiesSPICommand) command;
                    //TODO: find more eficient way to compare properties (maybe use streams?)
                    for (XEDDescriptor updateData : update.getData()) {
                        for (XEDDescriptor xed : XEDs) {
                            boolean xedWasUpdated = false;
                            for (XEDProperty newProperty : updateData.getProperties()) {
                                for (XEDProperty oldProperty : xed.getProperties()) {
                                    if (newProperty.name.equals(oldProperty.name)) {
                                        xedWasUpdated = true;
                                        l.logD("Updating " + xed.getName() + ":" + oldProperty.getName());
                                        oldProperty = newProperty;
                                    }
                                }
                            }
                            if (xedWasUpdated) {
                                connection.transmit(new XEDPropertiesUpdateResponse(this.name, xed));
                            }
                        }
                    }
                    break;

                }
                case "AcuireSequenceSPICommand": {
                    AcuireSequenceSPICommand seq = (AcuireSequenceSPICommand) command;
                    sequence = seq.getSequence();
                    l.logD("saving new sequence + " + sequence.toString());
                    // connection.transmit(new CommandOKResponse(command, name));
                    //simulate actual acusition
                    wait(500);

                    connection.transmit(new SequenceLoadedSPIResponse(name, sequence.sequenceId, UID));
                    break;
                }
                case "RunSequenceSPICommand": {
                    RunSequenceSPICommand run = (RunSequenceSPICommand) command;
                    l.logD("Running sequence # " + sequence.sequenceId);
//                    SequenceInfo actualValues = new SequenceInfo();
//                    actualValues.postSeqDelay = sequence.postSeqDelay + 0.01f;
//                    actualValues.preSeqDelay = sequence.preSeqDelay + 0.5f;
//                    actualValues.seqRepeat = sequence.seqRepeat;
//                    actualValues.sequenceId = sequence.sequenceId;
//                    actualValues.sequenceName = sequence.sequenceName;

                    for (XPulseInfo xPulse : sequence.pulses) {
                        xPulse.postPulseDelay += 0.3f;
                        xPulse.postPulseVoltage += 0.7f;
                        xPulse.prePulseDelay -= 0.2f;
                        xPulse.pulseVoltage -= 10;
                        xPulse.pulseWidth += 1.2f;
                        xPulse.tubeCurrent += 5.3f;
                        connection.transmit(new XPulseActualValuesSPIResponse(name, UID, xPulse));
                    }
//                    actualValues.pulses = sequence.pulses;
                    SequenceExecutionCompleteSPIREsponse resp = new SequenceExecutionCompleteSPIREsponse(name, UID, sequence.sequenceId);
                    connection.transmit(resp);
                    break;
                }
                default:
                    handleCustomCommand(command);
            }

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    protected void handleCustomCommand(Command command) {
    }
}
