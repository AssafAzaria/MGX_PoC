/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xeds.mockup;

import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.CommandHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import java.io.IOException;

/**
 *
 * @author Asaf
 */
public class XedClient extends ConnectionBase<Command, Event, CommandHandler> implements CommandHandler{
    private ActivityLogger l = new ActivityLogger(XedClient.class.getName());

    public XedClient() throws IOException {
        super(Configuration.ProxyServerPort, Configuration.getLocalhost());
        setOwner("as standalone");
    }

    @Override
    protected String getInstanceClassName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void handleCommand(Command command, ConnectionBase connection) {
       l.logI("got command "+command.toString());
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    
    
    
}
