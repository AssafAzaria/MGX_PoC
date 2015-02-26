/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared;

import java.util.Hashtable;

/**
 *
 * @author Asaf
 */
public class XEDGeneratorAcquisitionProperties {
        private Hashtable<String, XEDProperty> properties;
        
        public class XPulse {
            public XEDProperty width = new XEDProperty("Width", 1, 300000, 1, "msec", 1);
            public XEDProperty idle = new XEDProperty("Idle", 0, 1000, 0, "msec", 1);
            public XEDProperty prePulseDelay = new XEDProperty("Pre Pulse Delay", 0, 1000, 0, "msec", 0.5f);
            public XEDProperty postPulseDelay = new XEDProperty("Post Pulse Delay", 0, 1000, 0, "msec", 0.5f);
        }
        
        
        public XEDGeneratorAcquisitionProperties() {
            
        }
        
        /**
         * 
         * @param property 
         */
        public void setProperty(XEDProperty property) {
            if (properties.putIfAbsent(property.getName(), property) == null) {
                properties.replace(property.getName(), property);
            }
                    
        }
        
        /**
         * 
         * @param propertyName
         * @return 
         */
        public XEDProperty getProperty(String propertyName) {
            return properties.get(propertyName);
        }
        
}
