/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.main;

import com.mgx.manager.GenArrayManager;
import com.mgx.servicesProvider.ServicesProviderManager;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.commands.GetXEDsCommand;
import com.mgx.shared.events.Event;
import com.mgx.shared.events.EventType;
import com.mgx.shared.commands.Command;
import com.mgx.shared.networking.client.IPConnection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class Main {
    static private ActivityLogger l = new ActivityLogger(Main.class.getName());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GenArrayManager manager = GenArrayManager.getInstance();
        manager.startSystem();
        
        
        
    }
   
}
