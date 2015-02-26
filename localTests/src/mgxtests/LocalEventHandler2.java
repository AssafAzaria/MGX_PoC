/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgxtests;

import com.mgx.shared.events.Event;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.client.ConnectionBase;


public class LocalEventHandler2 extends LocalEventHandler {
    private final ActivityLogger l = new ActivityLogger(LocalEventHandler.class.getName());
    public LocalEventHandler2() {
        super(2);
    }
    
    @Override
    public void handleEvent(Event event, ConnectionBase connection) {
        l.logI(getName() +"got event > " + event.toString());
    }
    
}
