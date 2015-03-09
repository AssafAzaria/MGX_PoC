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
    public float tubeCurrent = 0;
    public float pulseVoltage = 0;
    public float postPulseVoltage = 0;
    
    /**
     * Repeat info
     */
    public int repeat = 0;
    public float prePulseDelay = 0;
    public float pulseWidth = 0;
    public float postPulseDelay = 0;
    
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
