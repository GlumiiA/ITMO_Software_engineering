package src.main.java.commands;

import src.main.java.common.network.Request;
import src.main.java.common.utility.Command;
import src.main.java.common.network.Response;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;

public class Info extends Command {
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;

    public Info(ConsoleInterface console, CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    public Response apply(Request response) {
        var s = "Информация о коллекции:\n";
        s += "Тип коллекции: '" + collectionManager.collectionType() + "'\n";
        s += "Дата инициализации: '" + collectionManager.getLastInitTime() + "'\n";
        s += "Количество элементов в коллекции: '" + collectionManager.collectionSize() + "'\n";
        s += "Последнее время сохранения: '" + collectionManager.getLastInitTime() + "'\n";
        return new Response(s);
    }

}
