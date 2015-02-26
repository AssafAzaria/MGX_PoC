/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.loggers;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
/**
 *
 * @author Asaf
 */
class  BaseLogger {
    
    private Logger l;
    private String loggerName;
    
    // chanel is the name of abstract module, like UI, Server or other key that
    // is not part of the package name
    private String channel = "default";  
    
    BaseLogger(String loggerName){
        this.loggerName = loggerName;
        l = Logger.getLogger(loggerName);
        l.setLevel(Level.INFO);
        LogManager.getLogManager().addLogger(l);
        
    }
    
    BaseLogger(String loggerName, String channel) {    
        this(loggerName);
        this.channel = channel;
    }
    
    public void setLevel(Level level) {
        l.setLevel(level);
    }
    /**
     * Log Sever errors
     * @param msg logging message
     */
    public void logE(String msg) {
        l.severe(loggerName +">"+ msg);
        System.out.println("LogE >  " + msg);
    }
    
    /**
     * Log warnings 
     * @param msg logging message 
     */
    public void logW(String msg){
        l.warning(loggerName +">"+ msg);
        System.out.println("LogW >  " + msg);
    }
    
    /**
     * Log informative messages
     * @param msg logging message
     */
    public void logI(String msg) {
        l.info(loggerName +" > "+ msg);
       // System.out.println("LogI >  " + msg);
    }
    
    public void logC(String msg) {
        l.config(loggerName + " > " + msg);
        System.out.println("LogC >  " + msg);
    }
    
    public void logD(String msg) {
        l.finest(loggerName + " > " + msg);
        System.out.println(loggerName + " > " + "LogD >  " + msg);
    }
    
    
}
