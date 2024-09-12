package src.main.java.common.utility;

import java.io.Serializable;

public enum CommandTypes implements Serializable {

    Add("add"),
    AddIfMin("addIfMin"),
    Clear("clear"),
    Exit("exit"),
    Info("info"),
    Remove("remove"),
    RemoveLower("removeLower"),
    Save("save"),
    Show("show"),
    Update("update"),
    Help("help"),
    AddIfMax("addIfMax"),
    FilterName("filterName"),
    PrintFiledNumberOfBathrooms("printFiledNumberOfBathrooms"),
    PrintUniqueNumberOfBathrooms("printUniqueNumberOfBathrooms"),
    ExecuteScript("executeScript"),
    AuthUser("authUser"),
    Register("register"),
    Login("login");
    private final String type;

    CommandTypes(String type) {
        this.type = type;
    }

    public String Type() {
        return type;
    }

    private static final long serialVersionUID = 18754L;

    public static CommandTypes getByString(String string) {
        try {
            return  CommandTypes.valueOf(string.toUpperCase().charAt(0) + string.substring(1));
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
    }
}