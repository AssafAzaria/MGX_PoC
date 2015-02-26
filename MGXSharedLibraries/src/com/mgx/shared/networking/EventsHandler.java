/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking;

import com.mgx.shared.events.Event;
import com.mgx.shared.networking.client.ConnectionBase;

/**
 *
 * @author Asaf
 */
public abstract class EventsHandler implements ResponseHandler {
    public abstract void handleEvent(Event event, ConnectionBase connection);
    @Override
    public void handleResponse(Transmitable response, ConnectionBase connection) {
        handleEvent((Event)response, connection);
    }
}
