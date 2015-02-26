/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xeds.mockup;

import com.mgx.shared.XEDPropertyInfo;
import com.mgx.xeds.commands.XEDCommand;
import com.mgx.shared.commands.Command;
import com.mgx.shared.loggers.ActivityLogger;
import java.io.IOException;

public class GeneratorXED extends XEDBase {

    private static final int UID = 2;
        

    public GeneratorXED() throws IOException {
        super(UID, GeneratorXED.class.getSimpleName(), "Generate power to XEDs array");
        setProperties(new XEDPropertyInfo []{new XEDPropertyInfo("Output Voltage", "kV", 0.5f, 300, 0, 75.3f),
        new XEDPropertyInfo("Output Curent", "Amp", 0.5f, 72, 0, 15),
        new XEDPropertyInfo(("Temprature"), "C", 0.2f, 175f, -30.4f, 72.1f)});
        
        l.logI("^^^^ GeneratorXED::ctor called ^^^^^");
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
