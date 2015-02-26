/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Asaf
 */
public class Configuration {
    static final public int MGXServerPort = 6061;
    static final public int MGXServerMaxConnections = 10;
    static final public int ProxyServerPort = 9090;
    static final public int XEDsMaxNum = 5;

    
    static final public int ServicesProviderServerPort = 5050;
    static final public int ServicesProviderServerMaxConnections = 10;
    
    static public InetAddress getLocalhost() throws UnknownHostException {
        InetAddress  localAddress = null;
        localAddress = InetAddress.getLocalHost();
        return localAddress;
        
    }
    static final public int QueueSize = 10;
}
