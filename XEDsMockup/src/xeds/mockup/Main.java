/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xeds.mockup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new Thread("xeds mockup") {
            @Override
            public void run() {
                if (true) {
                    try {
                        TubeXED xed1 = new TubeXED();
                        xed1.start();
                        //xed1.runEngine();
                        GeneratorXED xed2 = new GeneratorXED();
                        xed2.start();
                        //xed2.runEngine();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               
            }
        }).start();// TODO code application logic here
    }
    
}
