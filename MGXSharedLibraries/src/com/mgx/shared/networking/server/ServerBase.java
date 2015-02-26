/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking.server;

import com.mgx.shared.Configuration;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.ResponseHandler;
import com.mgx.shared.networking.Transmitable;
import com.mgx.shared.networking.client.ConnectionBase;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for Server implementations
 * @author Asaf
 * @param <Inbound> Type of the Inbound Transmitable the server accept
 * @param <Outbound> Type of the Outbound Transmitable the server delivers to clients
 * @param <InboundHandler> The type of a ResponseHandler that can handle Inbound messages
 */
public abstract class ServerBase<
        Inbound extends Transmitable, 
        Outbound extends Transmitable, 
        InboundHandler extends ResponseHandler> implements ResponseHandler<Inbound>{
    
    
    private ServerSocket server;
    private ArrayList<ConnectionBase> connections;
    private ActivityLogger l;
    private Thread serverThread;
    private volatile boolean keepRunnig = true;
    private ConcurrentHashMap<Integer, InboundHandler> handlers = new ConcurrentHashMap<>(Configuration.QueueSize);
    
    private NewConnectionHandler newConnectionHandler = null;
    
    
    
    /**
     * Create a new (IP) server
     * @param port listening port
     * @param maxConnections max connections the server will support
     * @param logger logger for debugging
     */
    
    //TODO: make server support other connections then IP sockets, like SPI
    protected ServerBase(int port, int maxConnections, ActivityLogger logger) {
        l = logger;
        connections = new ArrayList<>();
        
        try {
            l.logI("Init server with port " + port
            + " with " + maxConnections + " connections");
            this.server = new ServerSocket (port, maxConnections);            
        } catch (IOException ex) {
           l.logE(ex.toString());
        }
    }
    
    /**
     * This method is called by the server when new client is connected.
     * the implementing class will need to provide a concrete implementation for
     * ConnectionBase class, which will support the required Inbound/Outbound 
     * Transmisables and the same connection type as the server (default for IP)
     * @param socket
     * @return 
     */
    protected abstract ConnectionBase createNewConnection(Socket socket);
    
    /**
     * When new connection is established with a client a notification sent to 
     * the NewConnectionHandler, which can send a default response 
     * (like Command, or Event). Usually used to indicate the client that the 
     * connection is ready for transmition     
     * @param handler the handle to register
     */
    public void setNewConnectionHandler(NewConnectionHandler handler) {
        this.newConnectionHandler = handler;
    }
    public void start() {
        serverThread = new Thread(this.getName()+" - Clients Listener thread"){
            @Override
            public void run(){
                
                while(keepRunnig) {
                    try {
                        //TODO: change clientBAse implementation to streems instead of taking it out of socket (for SPI)
                        
                        
                        ConnectionBase connection = createNewConnection(server.accept());
                        connection.start();
                        connections.add(connection);
                        
                        
                        if(newConnectionHandler != null) {
                            newConnectionHandler.newConnectionNotification(connection);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ServerBase.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
           
        };
        serverThread.start();
                
    }
    
    
    /**
     * Add a handler for Inbound messages. Each time a Transmisable is received
     * the dispatcher thread will forward it to all the InboundHandlers
     * @param handler the handler to register
     * @return UID of the handler - Inbound messages can be divert to specific 
     * handler recognized by this ID
     */
    protected int addInboundHandler(InboundHandler handler) {
        int UID = handler.hashCode();
        l.logD("Adding Handler " + handler.getName() + "with UID " + UID);
        handlers.putIfAbsent(UID, handler);
        return UID;        
    }

    /**
     * Called by ConnectionBase. Shouldn't be called directly
     * @param response
     * @param connection 
     */
    @Override
    public void handleResponse(Inbound response, ConnectionBase connection) {
        //connections.putIfAbsent(response.getUID(), connection);
        Enumeration elements =  handlers.elements();
        while (elements.hasMoreElements()) {
            InboundHandler handle = (InboundHandler)elements.nextElement();
            handle.handleResponse(response, connection);
        }
        
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    
    public void sendMessageToAllClients(Outbound message) {
        
        connections.stream().forEach((con) -> {
            try {
                con.transmit(message);
            } catch (InterruptedException ex) {
                l.logE("Failed to send message "+message.toString()+" to "+con.toString());
                ex.printStackTrace();
            }
        });
        
        
        
       
        
    
       
    }
}
