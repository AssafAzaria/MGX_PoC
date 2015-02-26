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
public class XEDPropertyValueUpdate implements Serializable{
    /**
     * Name of the property to change
     */
    public  String name;
    
    /**
     * The name of the XED this property belongs to 
     */
    public  String XEDName;
    /**
     * The XED UID
     */
    public  int XEDUID;
    
    /**
     * The new value for the the property
     */
    public  float newValue = Float.MIN_VALUE;
    
    /**
     * a new max property value allowed 
     */
    public float newMin = Float.MIN_VALUE;
    /**
     * a new min property value allowed 
     */
    public float newMax = Float.MIN_VALUE;

    /**
     * Create information structure for updating existing property in a given XED
     * @param propertyName  Name of the property to change
     * @param XEDName The name of the XED this property belongs to
     * @param XEDUID The XED UID which this property belongs to 
     */
    public XEDPropertyValueUpdate(String propertyName, String XEDName, int XEDUID){
        this.name = propertyName;
        this.XEDName = XEDName;
        this.XEDUID = XEDUID;
        
    }

    public XEDPropertyValueUpdate(String propertyNAme, float newValue, String XEDName, int XEDUID) {
        this(propertyNAme, XEDName, XEDUID);
        this.newValue = newValue;
    }
    
    /**
     * update the min possible value of this property.
     * Default value is set to Float.MIN_VALUE, which 
     * means no update required for this field
     * @param newMin the new min value for this property
     */
    public void setMinValue(float newMin){
        this.newMin = newMin;
    }
    
    /**
     * update the max possible value of this property.
     * Default value is set to Float.MIN_VALUE, which 
     * means no update required for this field
     * @param newMax the new max value for this property
     */
    public void setMaxValue(float newMax) {
        this.newMax = newMax;
    }
    /**
     * update the current value  of this property.
     * Default value is set to Float.MIN_VALUE, which 
     * means no update required for this field
     * @param newValue the new value for this property
     */
    public void setNewValue(float newValue) {
        this.newValue = newValue;
    }
    
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("XEDPropertyUpdate:\n");
        string.append("property name:"+name+" (XED:"+XEDName+"("+XEDUID+"))\n");
        string.append("Max:");
        
        string.append(newMax != Float.MIN_VALUE? newMax: "no change");
        string.append("\n");
        
        string.append(newMin != Float.MIN_VALUE? newMin: "no change");
        string.append("\n");
        
        string.append(newValue != Float.MIN_VALUE? newValue: "no change");
        string.append("\n");
                
        return string.toString();
    }
}
