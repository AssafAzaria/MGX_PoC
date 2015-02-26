/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.xeds.events;

import com.mgx.shared.XEDInfo;


public class SendInfoXEDResponse extends XEDResponse {

    private static final int eventUID = 123;
    public SendInfoXEDResponse(String XEDName, XEDInfo info) {
        super(XEDName, eventUID, info, SendInfoXEDResponse.class.getSimpleName());
    }

    @Override
    public String dataToString() {
        return getData().toString();
    }

    @Override
    public XEDInfo getData() {
        return (XEDInfo)this.data;
    }
    
    
                
    
    
}
