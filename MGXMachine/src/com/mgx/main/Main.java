/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.main;

import com.mgx.manager.MGXManager;
import com.mgx.shared.loggers.ActivityLogger;
import java.io.IOException;

/**
 *
 * @author Asaf
 */
public class Main {
    static private ActivityLogger l = new ActivityLogger(Main.class.getName());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        MGXManager manager = MGXManager.getInstance();
        manager.startSystem();
        
        
        
    }
   
}
