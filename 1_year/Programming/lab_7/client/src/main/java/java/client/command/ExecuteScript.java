package src.main.java.java.client.command;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;

public class ExecuteScript extends Command {
    private final ConsoleInterface console;

    public ExecuteScript(ConsoleInterface console) {
        super("execute_script", "выполненние скрипта");
        this.console = console;
    }

    public Response apply(String[] arguments) {
        if (arguments[1].isEmpty()) return new Response(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");

        return new Response(true, "Выполнение скрипта '" + arguments[1] + "'...");
    }

    @Override
    public Response apply(Request request) {
        return null;
    }
}