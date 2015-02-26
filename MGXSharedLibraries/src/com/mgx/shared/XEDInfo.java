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
public class XEDInfo implements Serializable{
    public final String Name;
    public final int UID;
    public final XEDPropertyInfo[] properties;

    public XEDInfo(
            String Name,
            int UID,
            XEDPropertyInfo properties[]
            ) {
        this.Name = Name;
        this.UID = UID;
        this.properties = properties;
        
    }
    
    @Override
    public String toString() {
        StringBuilder result =  new StringBuilder("XED name:"+Name
                + " XED UID:"+UID);
                
        if(properties != null && properties.length > 0) {
        for(XEDPropertyInfo prop : properties) {
            result.append("\n"+prop.toString());
        }
        }else {
            result.append(":: no properties");
        }
        return result.toString();
        
    }
}
