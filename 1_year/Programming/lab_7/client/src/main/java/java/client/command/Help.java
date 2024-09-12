package src.main.java.java.client.command;

import src.main.java.common.network.Request;
import src.main.java.common.utility.Command;
import src.main.java.common.utility.CommandTypes;
import src.main.java.common.network.Response;
import src.main.java.java.client.utility.ConsoleInterface;
import java.util.stream.Collectors;

import java.util.Map;

public class Help extends Command {
    final ConsoleInterface console;
    private final Map<CommandTypes,String[]> commands;

    public Help(ConsoleInterface console, Map<CommandTypes, String[]> commands) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commands = commands;
    }

    public Response apply(String args[]) {
        return new Response(commands.keySet().stream().map(command -> String.format(" %-35s%-1s%n", commands.get(command)[0], commands.get(command)[1])).collect(Collectors.joining("\n")));
    }

    @Override
    public Response apply(Request request) {
        return null;
    }
}