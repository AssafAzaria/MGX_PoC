/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.sequences;

import java.io.Serializable;

/**
 *
 * @author Asaf
 */
public class XPulseInfo implements Serializable{
    public int pulseID = 0;
    public int XEDNum = 0;
    public FSType fs;
    public int tubeCurrent = 0;
    public int pulseVoltage = 0;
    public int postPulseVoltage = 0;
    
    /**
     * Repeat info
     */
    public int repeat = 0;
    public int prePulseDelay = 0;
    public int pulseWidth = 0;
    public int postPulseDelay = 0;
    
    @Override
    public String toString() {
        return XPulseInfo.class.getSimpleName()+":\n"
                +"pulseID = "+pulseID+" XEDNum = "+XEDNum+" \n"
                +"FSType = "+fs.name()+" \n"
                +"tubeCurrent = "+tubeCurrent + ", pulseVoltage = "+pulseVoltage+ ", postPulseVoltage = "+postPulseVoltage+"\n"
                +"Repeat information:\n"+
                "repeat = "+repeat+", prePulseDelay = "+ prePulseDelay + ", pulseWidth = "+pulseWidth+", postPulseDelay =  "+postPulseDelay;
    }
    
}
