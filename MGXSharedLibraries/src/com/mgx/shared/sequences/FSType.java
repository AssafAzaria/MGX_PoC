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
public enum FSType implements Serializable{
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large");
    private final String typeName;
    
    private FSType(String typeName) {
        this.typeName = typeName;
    }
    
    @Override
    public String toString() {
        return typeName;
    }
    
}
