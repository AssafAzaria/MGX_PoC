/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import java.io.Serializable;

/**
 *
 * @author Asaf
 */
public interface CommandAction extends Serializable {
    public void executeOnResponse();
    public void executeOnError();
   
    
}
