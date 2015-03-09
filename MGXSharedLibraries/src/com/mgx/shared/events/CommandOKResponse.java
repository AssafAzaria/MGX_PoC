/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.commands.Command;


public class CommandOKResponse extends Response {

    private final static int eventUID = 9900;
    public String commandName = "";
    private String invokerCommandClassName;
    public CommandOKResponse(Command command, String dispatcherName) {
        
        super(command.getSenderUID(), dispatcherName, eventUID, null, CommandOKResponse.class.getSimpleName());
        this.commandName = command.getName();
        this.invokerCommandClassName = command.getClass().getName();
        
    }
          
    @Override
    public String dataToString() {
        
        return "Command "+commandName+" OK";
        
                
    }
    
    public String getInvokerCommandClassName() {
        return invokerCommandClassName;
    }

    @Override
    public Object getData() {
        return null;
    }
    
}
