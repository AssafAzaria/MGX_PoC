/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.commands.Command;


public class CommandOKResponse extends Event {

    private final static int eventUID = 9900;
    public String commandName = "";
    
    public CommandOKResponse(Command command, String dispatcherName) {
        super(command.getSenderUID(), dispatcherName, eventUID, null, CommandOKResponse.class.getSimpleName(), EventType.RESPONSE);
        this.commandName = command.getName();
        
    }
          
    @Override
    public String dataToString() {
        
        return "Command "+commandName+" OK";
        
                
    }

    @Override
    public Object getData() {
        return null;
    }
    
}
