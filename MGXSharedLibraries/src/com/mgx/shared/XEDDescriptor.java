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
public class XEDDescriptor implements Serializable{
    private final String Name;
    private final int UID;
    private final XEDProperty[] properties;

    public XEDDescriptor(
            String Name,
            int UID,
            XEDProperty properties[]
            ) {
        this.Name = Name;
        this.UID = UID;
        this.properties = properties;
        
    }
    
    @Override
    public String toString() {
        StringBuilder result =  new StringBuilder("XED name:"+getName()
                + " XED UID:"+getUID());
                
        if(getProperties() != null && getProperties().length > 0) {
        for(XEDProperty prop : getProperties()) {
            result.append("\n"+prop.toString());
        }
        }else {
            result.append(":: no properties");
        }
        return result.toString();
        
    }

    /**
     * @return the name of this XED
     */
    public String getName() {
        return Name;
    }

    /**
     * @return the UID of this XED
     */
    public int getUID() {
        return UID;
    }

    /**
     * @return the properties of this XED
     */
    public XEDProperty[] getProperties() {
        return properties;
    }
}
