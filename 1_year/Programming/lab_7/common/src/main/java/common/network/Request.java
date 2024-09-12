package src.main.java.common.network;

import src.main.java.common.object.Flat;
import src.main.java.common.object.User;
import src.main.java.common.utility.CommandTypes;
import src.main.java.java.client.utility.Data;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable, Data {
    @Serial
    private static final long serialVersionUID = 1556321L;
    private final CommandTypes commandType;
    private String args;
    private Integer numericArgument;

    private Flat flatArgument;
    private User user;
    private boolean reg;
    private String login;
    private String password;
    public Request(CommandTypes commandType) {
        this.commandType = commandType;
    }

    public Request(CommandTypes commandType, Flat flatArgument) {
        this.commandType = commandType;
        this.flatArgument = flatArgument;
    }
    public Request(CommandTypes commandType, Flat flatArgument, Integer numericArgument) {
        this.commandType = commandType;
        this.numericArgument = numericArgument;
        this.flatArgument = flatArgument;

    }
    public Request(CommandTypes commandType, Integer numericArgument) {
        this.commandType = commandType;
        this.numericArgument = numericArgument;

    }
    public Flat getFlatArgument() {
        return flatArgument;
    }
    public Request(CommandTypes commandType, String args) {
        this.commandType = commandType;
        this.args = args;
    }
    public Request(CommandTypes commandType, User user, boolean command){
        this.commandType = commandType;
        this.user =user;
        this.reg = command;
    }
    public Request(CommandTypes commandType, String login, String password, String s){
        this.commandType = commandType;
        this.login = login;
        this.password = password;
    }

    public CommandTypes getCommandType() {
        return commandType;
    }

    public String getArgs() {
        return args;
    }
    public Integer getNumericArgument() {
        return numericArgument;
    }
    public User getUser() {
        return user;
    }
    public boolean getReq() {
        return reg;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    @Override
    public String getData(){
        return "Имя команды для отправки: " + commandType
                + (flatArgument == null ? "" : ("\nИнформация об организации для отправки:\n " + flatArgument))
                + (numericArgument == null ? "" : ("\nЧисловой аргумент для отправки:\n " + numericArgument));
    }
}
