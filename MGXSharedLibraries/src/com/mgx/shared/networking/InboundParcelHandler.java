/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking;

import com.mgx.shared.networking.client.ConnectionBase;

/**
 *
 * @author Asaf
 */
public interface InboundParcelHandler {
    public void handleInboundParcel(Transmitable parcel, ConnectionBase connection);
    public String getName();
}
