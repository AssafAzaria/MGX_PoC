/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgxtests;

import com.mgx.shared.Configuration;
import com.mgx.shared.XEDPropertyValueUpdate;
import com.mgx.shared.commands.Command;
import com.mgx.shared.commands.GetNextSequenceIDCommand;
import com.mgx.shared.commands.GetXEDsCommand;
import com.mgx.shared.commands.SetSequnceCommand;
import com.mgx.shared.commands.StartSequenceCommand;
import com.mgx.shared.commands.XEDSetPropertyCommand;
import com.mgx.shared.serviceprovider.commands.StoreSequenceDRCommand;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.client.IPConnection;
import com.mgx.shared.sequences.FSType;
import com.mgx.shared.sequences.SequenceInfo;
import com.mgx.shared.sequences.XPulseInfo;
import com.mgx.shared.serviceprovider.commands.DeleteSequenceSPCommand;
import com.mgx.shared.serviceprovider.commands.LoadSequenceSPCommand;
import com.mgx.shared.util.XEDUtils;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class Tests extends Thread {

    private final ActivityLogger l = new ActivityLogger(Tests.class.getName());

    private final Lock lock = new ReentrantLock();
    private final Condition responseSignal = lock.newCondition();
    private int ID = 111;
    private int handlerUID;

    public Tests() {
    }

    ;
    public void run() {

        //clientReConnect();
        //connect2Clients();
        // send1Command();
        //clientSequance();
        //twoClientsNotification();
        //getNextSequanceID();
        //sendSequance();
        ServiceProvider_sequanceServices();
    }

    private IPConnection getConnectionToManager() throws IOException {
        IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
        client.setOwner(Tests.class.getSimpleName());
        int UID = client.addHandler(new LocalEventHandler(1));
        client.start();

        return client;
    }
    
    private IPConnection getConnectionToServiceProvider() throws IOException{
         IPConnection client = new IPConnection(Configuration.ServicesProviderServerPort, Configuration.getLocalhost());
        client.setOwner(Tests.class.getSimpleName());
        this.handlerUID = client.addHandler(new LocalEventHandler(1));
        client.start();

        return client;
    }

    private void connect2Clients() {
        try {
            l.logI("Starting client #1");
            IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(Tests.class.getSimpleName());
            client.addHandler(new LocalEventHandler(1));
            client.start();
            l.logI("client #1 running");
            l.logI("Starting client #1");
            IPConnection client2 = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client2.setOwner(Tests.class.getSimpleName());
            client2.addHandler(new LocalEventHandler(2));
            client2.start();
            l.logI("client #2 running");
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void send1Command() {
        try {
            IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(Tests.class.getSimpleName());
            int UID = client.addHandler(new LocalEventHandler(1));
            client.start();
            l.logI("client  running");
            Command command = new GetXEDsCommand(UID, "testSequence2");
            client.transmit(command);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void twoClientsNotification() {
        try {
            IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(Tests.class.getSimpleName());
            LocalEventHandler localEventHandler = new LocalEventHandler(1);
            int UID = client.addHandler(localEventHandler);
            client.start();

            IPConnection client2 = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(Tests.class.getSimpleName());
            LocalEventHandler2 localEventHandler2 = new LocalEventHandler2();
            client2.addHandler(localEventHandler2);
            client2.start();

            l.logI("clients are running");
            Command command = new GetXEDsCommand(UID, "clientSequance");
            localEventHandler.setSignal(lock, responseSignal);
            client.transmit(command);
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            l.logI("got response");

            XEDPropertyValueUpdate property = XEDUtils.getXEDPropertyUpdateFromXEDInfo(
                    localEventHandler.xeds[0], //the xed to use
                    0, //index of the property we want to update
                    (localEventHandler.xeds[0].properties[0].max - localEventHandler.xeds[0].properties[0].min) / 2); //new value
            command = new XEDSetPropertyCommand(UID, "clientSequance", property);
            localEventHandler.setSignal(lock, responseSignal);
            client.transmit(command);
            //wait for OK response
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            //wait for notification
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            l.logI("got response");

        } catch (UnknownHostException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clientSequance() {
        try {
            IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(Tests.class.getSimpleName());
            LocalEventHandler localEventHandler = new LocalEventHandler(1);
            int UID = client.addHandler(localEventHandler);
            client.start();
            l.logI("client  running");
            Command command = new GetXEDsCommand(UID, "clientSequance");
            localEventHandler.setSignal(lock, responseSignal);
            client.transmit(command);
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            l.logI("got response");

            XEDPropertyValueUpdate property = XEDUtils.getXEDPropertyUpdateFromXEDInfo(
                    localEventHandler.xeds[0], //the xed to use
                    0, //index of the property we want to update
                    (localEventHandler.xeds[0].properties[0].max - localEventHandler.xeds[0].properties[0].min) / 2); //new value
            command = new XEDSetPropertyCommand(UID, "clientSequance", property);
            localEventHandler.setSignal(lock, responseSignal);
            client.transmit(command);
            //wait for OK response
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            //wait for notification
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            l.logI("got response");

        } catch (UnknownHostException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void clientReConnect() {
        try {
            IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(Tests.class.getSimpleName());
            client.addHandler(new LocalEventHandler(0));
            client.start();
            for (int i = 5; i > 0; i--) {
                client.dropConnection(null);
                client.restart();

            }

        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getNextSequanceID() {
        try {
            IPConnection connectoin = getConnectionToManager();

            connectoin.transmit(new GetNextSequenceIDCommand(ID, this.getClass().getSimpleName()));
            connectoin.transmit(new GetNextSequenceIDCommand(ID, this.getClass().getSimpleName()));
            connectoin.transmit(new GetNextSequenceIDCommand(ID, this.getClass().getSimpleName()));

        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private SequenceInfo createSequence() {
        SequenceInfo seq = new SequenceInfo();
            seq.pulses = new XPulseInfo[2];
            
            seq.postSeqDelay = 300;
            seq.preSeqDelay = 200;
            seq.sequenceId = -1;
            seq.seqRepeat = 2;

            XPulseInfo xpInfo = new XPulseInfo();
            xpInfo.XEDNum = 1;
            xpInfo.fs = FSType.MEDIUM;
            xpInfo.postPulseDelay = 150;
            xpInfo.postPulseVoltage = 20;
            xpInfo.prePulseDelay = 50;
            xpInfo.pulseID = 7;
            xpInfo.pulseVoltage = 700;
            xpInfo.pulseWidth = 200;
            xpInfo.repeat = 3;
            xpInfo.tubeCurrent = 550;

            
            seq.pulses[0] = xpInfo;

            xpInfo = new XPulseInfo();
            xpInfo.XEDNum = 2;
            xpInfo.fs = FSType.LARGE;
            xpInfo.postPulseDelay = 1500;
            xpInfo.postPulseVoltage = 205;
            xpInfo.prePulseDelay = 540;
            xpInfo.pulseID = 7;
            xpInfo.pulseVoltage = 7500;
            xpInfo.pulseWidth = 24300;
            xpInfo.repeat = 3;
            xpInfo.tubeCurrent = 250;

            seq.pulses[1] = xpInfo;
            
            return seq;
    }
    private void sendSequance() {
        try {
            IPConnection connection = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            connection.setOwner(Tests.class.getSimpleName());
            LocalEventHandler localEventHandler = new LocalEventHandler(1);
            int UID = connection.addHandler(localEventHandler);
            connection.start();
            localEventHandler.setSignal(lock, responseSignal);

            connection.transmit(new GetNextSequenceIDCommand(ID, this.getClass().getSimpleName()));

            //wait for response
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }

            SequenceInfo seq = new SequenceInfo();
            seq.pulses = new XPulseInfo[2];
            
            seq.postSeqDelay = 300;
            seq.preSeqDelay = 200;
            seq.sequenceId = localEventHandler.sequanceID;
            seq.seqRepeat = 2;

            XPulseInfo xpInfo = new XPulseInfo();
            xpInfo.XEDNum = 1;
            xpInfo.fs = FSType.MEDIUM;
            xpInfo.postPulseDelay = 150;
            xpInfo.postPulseVoltage = 20;
            xpInfo.prePulseDelay = 50;
            xpInfo.pulseID = 7;
            xpInfo.pulseVoltage = 700;
            xpInfo.pulseWidth = 200;
            xpInfo.repeat = 3;
            xpInfo.tubeCurrent = 550;

            
            seq.pulses[0] = xpInfo;

            xpInfo = new XPulseInfo();
            xpInfo.XEDNum = 2;
            xpInfo.fs = FSType.LARGE;
            xpInfo.postPulseDelay = 1500;
            xpInfo.postPulseVoltage = 205;
            xpInfo.prePulseDelay = 540;
            xpInfo.pulseID = 7;
            xpInfo.pulseVoltage = 7500;
            xpInfo.pulseWidth = 24300;
            xpInfo.repeat = 3;
            xpInfo.tubeCurrent = 250;

            seq.pulses[1] = xpInfo;

            localEventHandler.setSignal(lock, responseSignal);
            connection.transmit(new SetSequnceCommand(UID, this.getName(),seq));
            
             //wait for response
            try {
                lock.lock();
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            
            
            connection.transmit(new StartSequenceCommand(UID, this.getName(), seq.sequenceId));
            
        } catch (IOException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    private void waitForResponse() {
         //wait for response
            try {
                lock.lock();
                try {
                    responseSignal.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } finally {
                lock.unlock();
            }
    }
    private void ServiceProvider_sequanceServices() {
        try {
            IPConnection connection = getConnectionToServiceProvider();
            SequenceInfo seq = createSequence();
            LocalEventHandler handler = (LocalEventHandler)connection.getHandler(handlerUID);
            Command storeSeq = new StoreSequenceDRCommand(handlerUID, this.getName(), seq);
            handler.setSignal(lock, responseSignal);
            connection.transmit(storeSeq);
            waitForResponse();
            
            
            handler.setSignal(lock, responseSignal);
            connection.transmit(new LoadSequenceSPCommand(handlerUID, this.getName(), handler.sequanceID));
            waitForResponse();
            
            l.logD("sequance loaded - "+handler.sequence.toString());
            
            handler.setSignal(lock, responseSignal);
            connection.transmit(new DeleteSequenceSPCommand(handlerUID, this.getName(), handler.sequanceID));
            waitForResponse();
            
            l.logD("sequence deleted");
            l.logD("trying to delete same  sequence...expecting error");
            handler.setSignal(lock, responseSignal);
            connection.transmit(new DeleteSequenceSPCommand(handlerUID, this.getName(), handler.sequanceID));
            waitForResponse();
            
            //expecting CommandErrorResponse
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        
    }
}
