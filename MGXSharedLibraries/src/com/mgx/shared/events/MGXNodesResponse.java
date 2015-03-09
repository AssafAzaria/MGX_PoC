/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.CFPGADescriptor;



public class MGXNodesResponse extends Response {

    static public final int eventUID = 565;
    public MGXNodesResponse(int listenerUID, String dispatcherName, CFPGADescriptor controllers[]) {
        super(listenerUID, dispatcherName, eventUID, controllers, MGXNodesResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        CFPGADescriptor[] data = getData();
        StringBuilder result = new StringBuilder("Got "+data.length+" controllers:\n");
        int count =1;
        for (CFPGADescriptor cFPGA : data) {
            result.append("cFPGA #"+(count++)+"\n");
            result.append(cFPGA.toString());
        }
        return result.toString();
    }

    @Override
    public CFPGADescriptor[] getData() {
        return (CFPGADescriptor[])data;
    }
    
}
