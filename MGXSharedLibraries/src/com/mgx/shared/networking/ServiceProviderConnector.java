/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking;

import com.mgx.shared.commands.Command;

/**
 *
 * @author Asaf
 */
public interface ServiceProviderConnector {
    /**
     * Send request to the service provider server
     * @param action the required action
     * @throws java.lang.InterruptedException when failed to transmit request
     */
    public void request(Command action) throws InterruptedException;
    
   /**
    * Request and wait for response
     * @param request the request to send to the service provider server
     * @throws java.lang.InterruptedException when failed to transmit request
     * or waiting is interrupted 
    */
    public void transmitAndExecute(Command request) throws InterruptedException;
    
    /**
     * Connect to the ServiceProvider server
     */
    public void connect();
}
