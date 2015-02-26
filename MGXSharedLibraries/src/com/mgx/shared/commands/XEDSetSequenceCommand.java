/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;


public class XEDSetSequenceCommand extends Command {
    private final static int UID = 543;
    public XEDSetSequenceCommand( int senderUID, String senderName, Object data) {
        super(UID, XEDSetSequenceCommand.class.getSimpleName(), senderUID, senderName, data, CommandType.XEDCommand);
    }

    @Override
    public String dataToString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //TODO: implement
    }
    
    @Override
    public Object getData() {
        //TODO: implement
        throw new UnsupportedOperationException(" not implemented");
        //return null;
    }
    
}
