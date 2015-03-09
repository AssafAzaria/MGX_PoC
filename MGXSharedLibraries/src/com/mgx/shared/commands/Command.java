/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mgx.shared.commands;

import com.mgx.shared.networking.Transmitable;
import java.io.Serializable;

/**
 *
 * @author roey
 */
public abstract class Command implements Serializable, Transmitable {

    private final int commandUID;
    protected final Object data;
    private final CommandType type;
    private final int senderUID;
    private final String senderName;
    private final String commandName;
    private  CommandAction action;
    private Object responseData;

    public enum CommandType {

        XEDCommand("XEDCommand"),
        ControlCommand("ControlCommand"),
        DataRepositoryCommand("DataREpositoryCommand");

        private String typeName;

        private CommandType(String asString) {
            typeName = asString;
        }

        protected String getName() {
            return typeName;
        }
    }

    /**
     * 
     * @param UID commandUID
     * @param commandName string representation of the command
     * @param senderUID sender UID
     * @param senderName sender name
     * @param data data
     * @param type the command type
     */
    public Command(int UID, String commandName, int senderUID,
            String senderName, Object data, CommandType type) {
        this(UID, commandName, senderUID, senderName, data, type, null);
    }

    public Command(int UID, String commandName, int senderUID,
            String senderName, Object data, CommandType type, CommandAction action) {
        this.commandUID = UID;
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.data = data;
        this.type = type;
        this.commandName = commandName;
        this.action = action;
    }

    @Override
    public String toString() {
        return commandName + "(UID=" + commandUID + " commandType  =" + type.getName() + ")\n"
                + " sent by " + senderName + " (senderUID=" + senderUID + ")\n"
                + "data:\n" + dataToString();

    }

    /**
     * get the expected Event Class that this command expected to produce
     * Listeners can wait for this event to emulate synchronous operation NOTE:
     * that other events may arrive, such as CommandErrorResponse
     *
     * @return Class identifier for the expected result
     */
    public abstract Class<?> getResponseClass();

    /**
     * Some times the data object is complicated and need special conversion to
     * String
     *
     * @return
     */
    @Override
    public abstract String dataToString();

    /**
     * command type is like a declaration of responsibility for command handling
     *
     * @return the command type
     */
    public CommandType getType() {
        return type;
    }

    public int getUID() {
        return commandUID;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getSenderUID() {
        return senderUID;
    }

    public String getName() {
        return commandName;
    }

    public void execute() {
        if (action != null) {
            action.executeOnResponse();
        }
    }
    
    public void handleError() {
        action.executeOnError();
    }
    
    public void setAction(CommandAction action) {
        this.action = action;
    }
    public void setResponseData(Object data) {
        this.responseData = data;
    }
    
    public Object getResponseData() {
        return this.responseData;
    }
    public static StringBuffer Serialize() {
        throw new UnsupportedOperationException("not implemented");
    }

    public static Command Deserialize(StringBuffer serealizedCommand) {

        throw new UnsupportedOperationException("not implemented");

    }

}
