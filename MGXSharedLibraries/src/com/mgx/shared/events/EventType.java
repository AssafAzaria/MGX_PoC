/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

/**
 *
 * @author Asaf
 */
public enum EventType {

    /**
     * Direct response for command.
     * i.e GetXEDTempCommand will return the temp only to the caller that issued
     * the command
     */
    RESPONSE("RESPONSE"),
    /**
     * Notification is sent to all clients. Usually used for updates,
     * like temp increase in some module;
     */
    NOTIFICATION("NOTIFICATION"),
    
    /**
     * Error generated from a command. For example a command with illegal values
     */
    COMMAND_ERROR_RESPONSE("COMMAND_ERROR_RESPONSE"),
    
    /**
     * Notification of an error in the system. For example sequence abort due overheating 
     */
    SYSTEM_EROR_NOTIFICATION("SYSTEM_EROR_NOTIFICATION");

    private final String asString;

    private EventType(String asString) {
        this.asString = asString;
    }

    public String asString() {
        return asString;
    }
}

