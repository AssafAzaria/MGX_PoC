/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thales.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.thales.shared.Configuration;
import com.thales.shared.XEDPropertyValueUpdate;
import com.thales.shared.commands.Command;
import com.thales.shared.commands.GetXEDsCommand;
import com.thales.shared.commands.XEDSetPropertyCommand;
import com.thales.shared.loggers.ActivityLogger;
import com.thales.shared.networking.client.IPConnection;
import com.thales.shared.util.XEDUtils;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roey
 */
public class MGXJUnitTest {
    
    //
    //
    //
    
    private final ActivityLogger l = new ActivityLogger(MGXJUnitTest.class.getName());

    private final Lock lock = new ReentrantLock();
    private final Condition responseSignal = lock.newCondition();    
    
    public MGXJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

     @Test
     public void test_hello() {}
     
    @Test
    public void test_connect2Clients() {
        try {
            l.logI("Starting client #1");
            IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(MGXJUnitTest.class.getSimpleName());
            client.addHandler(new LocalEventHandler(1));
            client.start();
            l.logI("client #1 running");
            l.logI("Starting client #1");
            IPConnection client2 = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client2.setOwner(MGXJUnitTest.class.getSimpleName());
            client2.addHandler(new LocalEventHandler(2));
            client2.start();
            l.logI("client #2 running");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        //TODO: cleanup the test resources including client.dropConnection()

    }

    @Test
    public void test_send1Command() {
        IPConnection client = null;
        try {
            client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(MGXJUnitTest.class.getSimpleName());
            int UID = client.addHandler(new LocalEventHandler(1));
            client.start();
            l.logI("client  running");
            Command command = new GetXEDsCommand(UID, "testSequence2");
            client.transmit(command);            
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        finally{
            //TODO: client.dropConnection(null) throws NPE
            try{client.dropConnection(null);} catch(Exception e){e.printStackTrace();}
        }
    }
    
    @Test
    public void test_clientReConnect() {
        IPConnection client = null;
        try {
            client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(MGXJUnitTest.class.getSimpleName());
            client.addHandler(new LocalEventHandler(0));
            client.start();
            for (int i = 5; i > 0; i--) {
                //TODO: client.dropConnection(null) throws NPE
                try{client.dropConnection(null);} catch(Exception e){e.printStackTrace();}
                //TODO: client.restart() hangs after 2-3 times..
                client.restart();

            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        finally{
            try{client.dropConnection(null);} catch(Exception e){e.printStackTrace();}
        }
    }        
        

    @Test
    public void test_twoClientsNotification() {
        try {
            IPConnection client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(MGXJUnitTest.class.getSimpleName());
            LocalEventHandler localEventHandler = new LocalEventHandler(1);
            int UID = client.addHandler(localEventHandler);
            client.start();
            
            
            IPConnection client2 = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(MGXJUnitTest.class.getSimpleName());
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
                //TODO: the test thread reaches here, and then blocks 
                responseSignal.await();
            } finally {
                lock.unlock();
            }
            l.logI("got response");

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void test_clientSequance() {
        IPConnection client = null;
        try {
            client = new IPConnection(Configuration.MGXServerPort, Configuration.getLocalhost());
            client.setOwner(MGXJUnitTest.class.getSimpleName());
            LocalEventHandler localEventHandler = new LocalEventHandler(1);
            int UID = client.addHandler(localEventHandler);
            client.start();
            l.logI("client  running");
            Command command = new GetXEDsCommand(UID, "clientSequance");
            localEventHandler.setSignal(lock, responseSignal);
            client.transmit(command);
            try {
                lock.lock();
                //TODO: LocalEventHandler signals other locks\signals => this thread is blocked
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

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        finally{
            client.dropConnection(null);
        }
    }

    
 
}
