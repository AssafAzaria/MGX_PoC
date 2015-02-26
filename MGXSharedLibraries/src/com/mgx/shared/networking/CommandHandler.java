/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking;
import com.mgx.shared.commands.Command;
import com.mgx.shared.networking.client.ConnectionBase;

public abstract class CommandHandler<ResponseType extends Command> implements ResponseHandler<ResponseType> {

    protected abstract void handleCommand(Command command, ConnectionBase connection);
    
    @Override
    public void handleResponse(Command response, ConnectionBase connection) {
        handleCommand(response, connection);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
    
}
