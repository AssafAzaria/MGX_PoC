/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.events;


import com.mgx.shared.events.Response;

/**
 *
 * @author Asaf
 */
public abstract class XEDResponse extends Response{

    /**
     * Response sent by XEDs only
     * @param XEDName the name of the XED
     * @param eventUID the event unique ID
     * @param Data data associated with the response (can be null)
     * @param responseClassName String representation of the response
     */
    public XEDResponse(String XEDName, int eventUID, Object Data, String responseClassName) {
        super(0/*no need to track sender id, as the commands always come from the server*/
                , XEDName, eventUID, Data, responseClassName);
        
    }
}
