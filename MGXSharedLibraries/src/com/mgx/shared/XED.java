/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared;

/**
 *
 * @author Asaf
 */
public class XED {
    public int UID;
    
    public XED(int UID) {
        this.UID = UID;
    }
    public XEDGeneratorAcquisitionProperties generator = new XEDGeneratorAcquisitionProperties();
    public XEDVoltageProperties voltage = new XEDVoltageProperties();
    public XEDCurrentProperties current = new XEDCurrentProperties();
    
    
}
