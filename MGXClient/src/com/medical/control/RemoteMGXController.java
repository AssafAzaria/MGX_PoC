package com.medical.control;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thales.shared.Configuration;
import com.thales.shared.XEDInfo;
import com.thales.shared.XEDPropertyValueUpdate;
import com.thales.shared.commands.Command;
import com.thales.shared.commands.GetXEDsCommand;
import com.thales.shared.commands.XEDSetPropertyCommand;
import com.thales.shared.events.Event;
import com.thales.shared.loggers.ActivityLogger;
import com.thales.shared.networking.EventsHandler;
import com.thales.shared.networking.client.ConnectionBase;
import com.thales.shared.networking.client.IPConnection;
import com.thales.shared.util.XEDUtils;

public class RemoteMGXController {
	
	// Host property name
	private static final String HOST_PROP = "host";
	
	// Listener id
	private int UID = 0;
	
	// The connection
	private IPConnection connection;
	
	// Logger
	private final ActivityLogger logger = new ActivityLogger(getClass()
			.getName());
	
	// The host address - to set supply a system property 'host'
	private InetAddress hostAddress;
	
	// Singleton instance
	private static RemoteMGXController instance = new RemoteMGXController();
	
	private RemoteMGXController(){
		
		// Get the host address, if none - use local host
		String hostName = System.getProperty("host");
		System.out.println("*********HOST:: " + hostName);
		try
		{
			hostAddress = (hostName == null)? InetAddress.getLocalHost() : 
				InetAddress.getByName(hostName);
			System.out.println(hostAddress);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static RemoteMGXController getInstance()
	{
		return instance;
	}
	
	
	// Send request for XED info
	public void requestXEDInfo(EventsHandler listener) {
		try {
			System.out.println(hostAddress);
			connection = new IPConnection(Configuration.MGXServerPort,
					hostAddress);
			connection.setOwner(getClass().getSimpleName());

			UID = connection.addHandler(listener);

			connection.start();
			logger.logI("client  running");

			Command command = new GetXEDsCommand(UID, "client");
			connection.transmit(command);
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}

	}
	
	// Send update on XEDs
	public void sendXEDPropertyUpdate(String propName, float newVal, String XEDName)
	{
		XEDPropertyValueUpdate prop = new XEDPropertyValueUpdate(propName, newVal, XEDName, UID);
				
		Command command = new XEDSetPropertyCommand(UID, "client", prop);
        try{
        	connection.transmit(command);
        }
		catch (InterruptedException ex)
		{
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
     }

	
}
