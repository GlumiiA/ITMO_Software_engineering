package src.main.java.manager;


import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import src.main.java.common.utility.Command;

public class CommandManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 132319L;
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}