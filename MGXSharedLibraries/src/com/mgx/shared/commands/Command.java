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
        this.commandUID = UID;
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.data = data;
        this.type = type;
        this.commandName = commandName;
    }

    @Override
    public String toString() {
        return commandName + "(UID=" + commandUID + " commandType  =" + type.getName() + ")\n"
                + " sent by " + senderName + " (senderUID=" + senderUID + ")\n"
                + "data:\n" + dataToString();

    }

    public abstract String dataToString();

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

    public static StringBuffer Serialize() {
        return null;
    }

    public static Command Deserialize(StringBuffer serealizedCommand) {

        return null;

    }
}
