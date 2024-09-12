package src.main.java.common.utility;

import src.main.java.common.network.Request;
import src.main.java.common.network.Response;

import java.io.Serializable;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Абстрактный класс команды
 *
 * @author Andrew Schmunk
 * @version 1.0
 */
public abstract class Command implements Serializable {
    private static final long serialVersionUID = 231L;
    CommandTypes commandType;
    private final String name;
    private final String description;
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Command command = (Command) obj;
        return name.equals(command.name) && description.equals(command.description);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    public CommandTypes getCommandType() {return commandType;}

    public abstract Response apply(Request request) throws SQLException;
}