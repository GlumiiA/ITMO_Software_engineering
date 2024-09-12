package src.main.java.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;

/**
 * Класс команды для вывода коллекции
 *
 */
public class Show extends Command  {
    public static final Logger logger = LoggerFactory.getLogger(Show.class);
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    public Show(ConsoleInterface console, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    @Override
    public Response apply(Request request) {
        logger.info(collectionManager.toString());
        logger.info(String.valueOf(collectionManager.collectionSize()));
        return new Response(collectionManager.toString());
    }
}
