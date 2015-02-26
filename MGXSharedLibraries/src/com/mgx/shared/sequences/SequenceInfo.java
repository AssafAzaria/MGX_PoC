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
public class SequenceInfo implements Serializable {

    public int sequenceId;
    public String sequenceName;
    public int preSeqDelay;
    public int seqRepeat;
    public XPulseInfo[] pulses; //list of XPulses;TBD: multiple XEDs operate in a single XPulse?
    public int postSeqDelay;
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(SequenceInfo.class.getSimpleName()+ ":");
        result.append("sequenceId = "+sequenceId+", preSeqDelay = " + preSeqDelay +" seqRepeat = " + seqRepeat+", postSeqDelay = "+postSeqDelay+"\n");
        result.append("Xpulses("+pulses.length+"):\n");
        int i = 1;
        for(XPulseInfo pulse: pulses) {
            result.append("\nPulse # "+(i++)+":\n\n");
            result.append(pulse.toString());
        }
        return result.toString();
    }
}
