/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking.client;

import com.mgx.shared.Configuration;
import com.mgx.shared.commands.Command;
import com.mgx.shared.events.Event;
import com.mgx.shared.networking.InboundParcelHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class IPConnection<Inbound extends Event,
        Outbound extends Command,
        Handler extends InboundParcelHandler> extends ConnectionBase {

    public IPConnection() throws IOException {
        super(Configuration.MGXServerPort, Configuration.getLocalhost());
    }
    public IPConnection(Socket socket) throws IOException {
        super(socket);
    }
    public IPConnection(int port, InetAddress serverAddress) throws IOException {
        super(port, serverAddress);
    }

    @Override
    protected String getInstanceClassName() {
       return this.getClass().getSimpleName();
    }
    
}
