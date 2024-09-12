package src.main.java.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;
import src.main.java.manager.DatabaseHandler;

import java.sql.SQLException;

public class Remove extends Command {
    public static final Logger logger = LoggerFactory.getLogger(Remove.class);
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    DatabaseHandler dbHandler;

    public Remove(ConsoleInterface console, CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super("remove", "удалить элемент из коллекции по его id");
        this.console = console;
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
    }


    public Response apply(Request request) {
        try {
            int id = request.getNumericArgument();
            var flatToRemove = collectionManager.getById(id);
            if (flatToRemove == null)
                return new Response(false, "Не существующий ID");
            if (!dbHandler.removeFlatByID(id))
                return new Response(false, "Возникла ошибка. Проверьте права доступа.");
            collectionManager.removeFromCollection(flatToRemove);
            collectionManager.saveCollection();
            logger.info(collectionManager.toString());
            return new Response("Учебная группа успешно удалена!");
        } catch (NumberFormatException e) {
            return new Response(false, "ID не распознан");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
