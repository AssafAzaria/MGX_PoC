/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared;

import java.io.Serializable;

/**
 *
 * @author Asaf
 */
public class CFPGADescriptor implements Serializable{
    /**
     * The name of the cFPGA
     */
    private String name;
    
    /**
     * The unique ID of the cFPGA
     */
    private int UID;
    
    private XEDDescriptor xeds[];
    
    /**
     * A descriptor of control FPGA (cFPGA)
     * @param name the name of the cFPGS
     * @param UID the unique ID of the cFPGA
     * @param xeds the XEDs that the cFPGA controls/manage
     */
    public CFPGADescriptor(String name, int UID, XEDDescriptor xeds[]) {
        this.name = name;
        this.xeds = xeds;
        this.UID = UID;
        
    }

    /**
     * @return the name of this cFPGA
     */
    public String getName() {
        return name;
    }

    /**
     * @return the UID of the cFPGA
     */
    public int getUID() {
        return UID;
    }


    /**
     * @return the XEDs this cFPGA controls/manage
     */
    public XEDDescriptor[] getXEDs() {
        return xeds;
    }
    
    /**
     * Set new XEDs list to this controller 
     * (commonly used when updating properties values for XEDs)
     * @param XEDs set a new, or specific list of XEDs for this cFPGA
     */
    public void setXEDs(XEDDescriptor[] XEDs) {
        this.xeds = XEDs;
    }

    @Override
    public String toString() {
        
        StringBuilder result = new StringBuilder("cFPGA " + this.name + " (UID=" + this.UID + ") ");
        result.append("with " + this.xeds.length + " XEDs:\n");
        for (XEDDescriptor xed : this.xeds) {
            result.append("XED "+xed.getName()+" UID = "+xed.getUID()+"\n");
            
            for (XEDProperty prop : xed.getProperties()) {
                result.append(prop.toString());
                result.append("\n");
            }
        }
        return result.toString();
    }
    
}
