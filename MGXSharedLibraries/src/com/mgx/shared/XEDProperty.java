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
public class XEDProperty {

    public String name;
    public float minValue;
    public float maxValue;
    public float value;
    public String units;
    public float increments;
    

    /**
     * Create representation of XED property
     * @param name the name of the property
     * @param minValue the min value this property can have
     * @param maxValue the max value this property can have
     * @param value the current value of the property
     * @param units units of this properties (example: msec)
     * @param increments the change a value can have 
     */
    public XEDProperty(String name, float minValue, float maxValue, float value, String units, float increments) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;
        this.units = units;
        this.increments = increments;
        
    }
    /**
     * @return the minValue
     */
    public float getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the maxValue
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the increments
     */
    public float getIncrements() {
        return increments;
    }

    /**
     * @param increments the increments to set
     */
    public void setIncrements(float increments) {
        this.increments = increments;
    }

    /**
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(String units) {
        this.units = units;
    }
    
}
