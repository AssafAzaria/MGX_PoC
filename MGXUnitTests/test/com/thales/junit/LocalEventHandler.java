package com.thales.junit;

import com.thales.shared.XEDInfo;
import com.thales.shared.events.Event;
import com.thales.shared.loggers.ActivityLogger;
import com.thales.shared.networking.EventsHandler;
import com.thales.shared.networking.client.ConnectionBase;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Asaf
 */
public class LocalEventHandler extends EventsHandler{
    private final int num;
    private final ActivityLogger l = new ActivityLogger(LocalEventHandler.class.getName());
    private Lock lock;
    private Condition condition;
    
    public XEDInfo xeds[];
    public LocalEventHandler(int num) {
        this.num = num;
        
    }
    @Override
    public void handleEvent(Event event, ConnectionBase connection) {
        l.logI(getName() +"got event > " + event.toString());
        switch (event.getName()) {
            case "GetXEDsResponse":
                xeds = (XEDInfo[])event.data;
                break;
        }
        //if (event.eventName)
        if(lock != null) {
            try {
                lock.lock();
                condition.signal();
            }finally {
                lock.unlock();
                lock = null;
                condition = null;
            }
        }
    }
    
    public void setSignal(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public String getName() {
        return MGXTestSuite.class.getName() + "::EventsHandler #"+num;
    }
    
}
