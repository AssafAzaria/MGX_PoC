/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.events;

import java.io.Serializable;


public class SequenceExecutionDoneXEDNotification extends XEDNotification {
    static final private int eventUID = 9986;
    
    static public class ExecutionInfo implements Serializable{
        public int XEDUID;
        public int sequenceID;
        
        public ExecutionInfo(int XEDUID, int sequenceID) {
            this.XEDUID = XEDUID;
            this.sequenceID = sequenceID;
            
        }
        
        @Override
        public String toString() {
            return ExecutionInfo.class.getSimpleName()+": XEDUID = "+XEDUID+", sequenceID =  "+sequenceID;
        }
        
        
    }
    public SequenceExecutionDoneXEDNotification(String dispatcherName, int XEDUID, int sequenceID ) {
        super(dispatcherName, eventUID, 
                new ExecutionInfo(XEDUID, sequenceID), 
                SequenceExecutionDoneXEDNotification.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }

    @Override
    public ExecutionInfo getData() {
        return (ExecutionInfo)data;
    }
    
}
