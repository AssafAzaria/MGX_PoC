/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xeds.mockup;

import com.mgx.shared.XEDPropertyInfo;
import com.mgx.xeds.commands.XEDCommand;
import com.mgx.xeds.events.SendInfoXEDResponse;
import com.mgx.shared.commands.Command;
import com.mgx.shared.loggers.ActivityLogger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asaf
 */
final public class TubeXED extends XEDBase {
    
     
    
    private static final int UID = 1;
    

    public TubeXED() throws IOException {
        super(UID, "TubeXED", "wow");
        setProperties(new XEDPropertyInfo[]  {
        new XEDPropertyInfo("Tube Voltage", "kV", 0.5f, 100, 0, 50),
        new XEDPropertyInfo("Tube Curent", "Amp", 0.01f, 10, 0, 3),
        new XEDPropertyInfo(("Temprature"), "C", 0.2f, 245.3f, -25.4f, 123f)});
        
    }

   
    
    
    
    @Override
    protected XEDCommand[] supportedCommands() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void handleCustomCommand(Command command) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
