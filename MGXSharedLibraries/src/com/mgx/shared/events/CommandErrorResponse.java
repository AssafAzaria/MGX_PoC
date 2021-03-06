/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.commands.Command;


public class CommandErrorResponse extends Response {

    private final static int eventUID = 9901;
    private final int invokedByCommandUID;
    public String commandName = "";
    private final String commandClassName;
    public CommandErrorResponse(Command command, String dispatcherName, String errMsg) {
        super(command.getSenderUID(), dispatcherName, eventUID, errMsg, CommandErrorResponse.class.getSimpleName());
        this.commandName = command.getName();
        this.invokedByCommandUID = command.getUID();
        this.commandClassName = command.getClass().getName();
        
    }

    public String getInvokerCommandClassName() {
        return commandClassName;
    }
    public int getInvokerCommandUID() {
        return  invokedByCommandUID;
    }
    
    @Override
    public String getData() {
        return (String)this.data;
    }
    @Override
    public String dataToString() {
        return "Error on command "+commandName+" >"+getData();
    }
    
}
