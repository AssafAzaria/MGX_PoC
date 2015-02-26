/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.networking;

import java.io.Serializable;

/**
 *
 * @author Asaf
 */
public interface Transmitable{
    public String getName();
    public int getUID();
    public String dataToString();
    public Object getData();
}
