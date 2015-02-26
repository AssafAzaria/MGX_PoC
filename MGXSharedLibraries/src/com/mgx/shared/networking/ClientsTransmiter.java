/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking;

import com.mgx.shared.events.Notification;
import com.mgx.shared.events.Response;

/**
 *
 * @author Asaf
 */
public interface ClientsTransmiter {
    public void notifyAllClients(Notification notification);
    public void notifyClient(String clientName);
    public void commandResponse(Response response);
    public void dispatchErrorMessage();
}
