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
public class XEDPropertyInfo implements Serializable {

    public  String units;
    public  String name;
    public  float mesurementInterval;
    public  float max;
    public  float min;
    public  float value;

    public XEDPropertyInfo(
            String name,
            String units,
            float mesurementInterval,
            float max,
            float min,
            float value) {
        this.units = units;
        this.name = name;
        this.mesurementInterval = mesurementInterval;
        this.max = max;
        this.min = min;
        this.value = value;

    }

    public String toString() {
        return "XEDProperty:" + name
                + " \nunits:" + units
                + " \nmesurement intervals:" + mesurementInterval
                + " \nmax settings:" + max
                + " \nmin settings:" + min
                + " \ncurrent value:" + value+"\n";
    }

}
