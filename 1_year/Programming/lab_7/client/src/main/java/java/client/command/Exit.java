package src.main.java.java.client.command;

import src.main.java.common.network.Request;
import src.main.java.common.utility.Command;
import src.main.java.common.network.Response;
import src.main.java.java.client.utility.ConsoleInterface;

public class Exit extends Command {
    /**
     * Консоль
     */
    private final ConsoleInterface console;

    public Exit(ConsoleInterface console) {
        super("exit", "завершить программу");
        this.console = console;
    }
    public Response apply(String[] arguments) {
        return new Response("exit");
    }

    @Override
    public Response apply(Request request) {
        return null;
    }
}