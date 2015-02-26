/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking.server;

import com.mgx.shared.networking.client.ConnectionBase;

/**
 *
 * @author Asaf
 */
public interface NewConnectionHandler {
    
    /**
     *will be called by the server when new connection with a client is established.
     * Used to send the client either Command or Event upon connection.
     * @param connection the connection with the new client
     * @return true if notification process OK
     */
    public boolean newConnectionNotification(ConnectionBase connection);
}
