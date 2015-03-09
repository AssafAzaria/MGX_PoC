/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.networking.server;

import com.mgx.manager.XedArrayProxy;
import com.mgx.shared.networking.server.ServerBase;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.ResponseHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import java.io.IOException;
import java.net.Socket;
import com.mgx.shared.Configuration;


public class XEDsProxyServer<
        Inbound extends Event, Outbound extends Command, Handler extends ResponseHandler> extends ServerBase {

    private static ActivityLogger l = new ActivityLogger(XEDsProxyServer.class.getSimpleName());
    
    public XEDsProxyServer(){
        super(Configuration.ProxyServerPort, Configuration.XEDsMaxNum, l);
    }
    @Override
    protected ConnectionBase createNewConnection(Socket socket) {
        try {
            SPIServerConnection client =  new SPIServerConnection(socket);
            client.setOwner(this.getClass().getSimpleName());
            client.addHandler(this);
            return client;
        } catch (IOException ex) {
            l.logE("failed to create connection");
            ex.printStackTrace();
        }
        
        return null;
    }

    public int addEventHandler(ResponseHandler hanndler) {
        return super.addInboundHandler(hanndler);
    }

    
    
}
