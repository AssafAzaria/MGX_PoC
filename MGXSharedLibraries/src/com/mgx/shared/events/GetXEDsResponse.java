/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.events;

import com.mgx.shared.XEDInfo;


public class GetXEDsResponse extends Response {

    static public final int eventUID = 565;
    public GetXEDsResponse(int listenerUID, String dispatcherName, XEDInfo info[]) {
        super(listenerUID, dispatcherName, eventUID, info, GetXEDsResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        XEDInfo [] data = this.getData();
        StringBuilder result = new StringBuilder("Got "+data.length+" XEDs:\n");
        int count =1;
        for (XEDInfo xed : data) {
            result.append("XED #"+(count++));
            result.append(xed.toString());
        }
        return result.toString();
    }

    @Override
    public XEDInfo[] getData() {
        return (XEDInfo[])data;
    }
    
}
