/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.util;

import com.mgx.shared.XEDInfo;
import com.mgx.shared.XEDPropertyInfo;
import com.mgx.shared.XEDPropertyValueUpdate;

/**
 *
 * @author Asaf
 */
public class XEDUtils {
    
    /**
     * Utility function that creates XEDPropertyValueUpdate from XEDInfo
     * @param XED the XED to take information from
     * @param propertyIndex the index of the property in the XED you want to use for creating XEDPropertyValueUpdate
     * @param newValue the new value to assign to the newly created XEDPropertyValueUpdate
     * @return newly created XEDPropertyValueUpdate
     */
    public static final XEDPropertyValueUpdate getXEDPropertyUpdateFromXEDInfo(XEDInfo XED, int propertyIndex, float newValue){
        return new XEDPropertyValueUpdate(XED.properties[propertyIndex].name, newValue, 
                XED.Name, XED.UID );
    }
    
    /**
     * Utility function to convert XEDPropertyInfo into XEDPropertyValueUpdate
     * @param ownerXED the XED that owns this property
     * @param propertyInfo the property to convert
     * @return 
     */
     public static final XEDPropertyValueUpdate convertPropertyInfoToPropertyValueUpdate(XEDInfo ownerXED, XEDPropertyInfo propertyInfo){
        return new XEDPropertyValueUpdate(propertyInfo.name, propertyInfo.value, 
                ownerXED.Name, ownerXED.UID );
    }
    /**
     * Creates XEDPropertyValueUpdate[] from all XED.properties
 NOTE:XED.properties.length must be equal to values.length
     * @param XED the XED to take information from
     * @param values array of new values to assign to the created XEDPropertyValueUpdate
     * @return array of XEDPropertyValueUpdate objects
     */
    public static final XEDPropertyValueUpdate[] getXEDPropertiesUpdateFromXEDInfo(XEDInfo XED, float values[]) {
        XEDPropertyValueUpdate properties[] = new XEDPropertyValueUpdate[XED.properties.length];
        
        for (int i = 0; i < XED.properties.length; i++) {
            properties[i] = getXEDPropertyUpdateFromXEDInfo(XED, i, values[i]);
        }
        
        return properties;
        
    }
}
