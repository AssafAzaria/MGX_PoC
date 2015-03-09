/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgxtests;

import com.mgx.shared.CFPGADescriptor;
import com.mgx.shared.XEDDescriptor;
import com.mgx.shared.events.CommandOKResponse;
import com.mgx.shared.events.MGXNodesResponse;
import com.mgx.shared.events.Response;
import com.mgx.shared.loggers.ActivityLogger;
import com.mgx.shared.networking.ResponseHandler;
import com.mgx.shared.networking.client.ConnectionBase;
import com.mgx.shared.sequences.SequenceInfo;
import com.mgx.shared.serviceprovider.responses.LoadSequenceResponse;
import com.mgx.shared.serviceprovider.responses.LoadSettingsResponse;
import com.mgx.shared.serviceprovider.responses.StoreSequenceResponse;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Asaf
 */
public class LocalEventHandler implements ResponseHandler{
    private final int num;
    private final ActivityLogger l = new ActivityLogger(LocalEventHandler.class.getName());
    private Lock lock;
    private Condition condition;
    
    public int sequanceID = -1;
    public SequenceInfo sequence=null;
    public XEDDescriptor xeds[];
    
    public CFPGADescriptor[] cFPGAs;
    public CFPGADescriptor loaddedSettings;
    public LocalEventHandler(int num) {
        this.num = num;
        
    }
    @Override
    public void handleResponse(Response event, ConnectionBase connection) {
        l.logI(getName() +"got event > " + event.getName() +": "+event.dataToString());
        switch (event.getName()) {
            case "CommandOKResponse":
                
                l.logD(((CommandOKResponse)event).toString());
                break;
            case "MGXNodesResponse": {
                    MGXNodesResponse response = (MGXNodesResponse)event;
                    cFPGAs = response.getData();
                    
                break;
            }
            case "LoadSettingsResponse": {
                LoadSettingsResponse response = (LoadSettingsResponse)event;
                loaddedSettings = response.getData();
                break;
            }
            case "GetXEDsResponse":
                xeds = (XEDDescriptor[])event.data;
                break;
            case "SequenceIDResponse":
               
                
                break;
            case "LoadSequenceResponse":
                sequence = ((LoadSequenceResponse)event).getSeqeuence();
                break;
            case "StoreSequenceResponse":
                sequanceID = ((StoreSequenceResponse)event).getSequenceID();
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
        return Tests.class.getName() + "::EventsHandler #"+num;
    }
    
}
