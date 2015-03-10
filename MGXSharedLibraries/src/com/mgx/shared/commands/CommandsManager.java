/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.events.CommandErrorResponse;
import com.mgx.shared.events.Response;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.ResponseHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import java.util.HashMap;

/**
 *
 * @author Asaf
 */
public class CommandsManager implements ResponseHandler {

    private ActivityLogger l = new ActivityLogger(CommandsManager.class.getSimpleName());
    private final HashMap<Class, Command> commands = new HashMap();
    private ConnectionBase connection;
    
    public CommandsManager(ConnectionBase connection) {
        this.connection = connection;
        
    }
    
    public void sendCommand(Command command, ConnectionBase connection) throws InterruptedException{
        this.commands.put(command.getResponseClass(), command);
        connection.transmit(command);
    }
    
    @Override
    public void handleResponse(Response response, ConnectionBase connectoin) {
        Command command = commands.get(response.getClass());
        if(command == null) {
            l.logE("Couldn't find command to activate for "+response.eventName);
            return;
        }
        if (response instanceof CommandErrorResponse) {
            CommandErrorResponse err = (CommandErrorResponse)response;
            l.logW("Got an error response from command  "+command.getName()+"\n err = "+err.toString());
            command.handleError();
            
        } else {
            command.executeOnResponse();
        }
    }

    @Override
    public String getName() {
        return CommandsManager.class.getSimpleName();
    }
    
}
