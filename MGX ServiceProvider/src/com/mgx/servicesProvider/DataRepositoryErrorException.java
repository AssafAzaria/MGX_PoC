/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.servicesProvider;

/**
 *
 * @author Asaf
 */
public class DataRepositoryErrorException extends Exception{
    public DataRepositoryErrorException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public DataRepositoryErrorException(String msg) {
        super(msg);
    }
    
}
