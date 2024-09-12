package src.main.java.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;
import src.main.java.manager.DatabaseHandler;

public class Update extends Command {
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    DatabaseHandler dbHandler;
    public static final Logger logger = LoggerFactory.getLogger(Update.class);

    public Update(ConsoleInterface console, CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному");
        this.console = console;
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
    }

    @Override
    public Response apply(Request request) {
        try {
            int id = request.getNumericArgument();
            var flatToOld  = collectionManager.getById(id);
            if (flatToOld  == null)
                return new Response(false, "Не существующий ID");
            var flatNew = request.getFlatArgument();
//            flatToOld.update(flatNew);
//            collectionManager.removeFromCollection(flatToOld);
//            logger.info(collectionManager.toString());
//            var flatNew = request.getFlatArgument();
            flatNew.setId(id);
            if (!dbHandler.updateFlat(flatNew))
                return new Response(false, "Возникла ошибка. Проверьте права доступа.");
            collectionManager.init();
            collectionManager.saveCollection();
//            collectionManager.addToCollection(flatNew);
            return new Response("Обновлено!");
        } catch (NumberFormatException e) {
            return new Response(false, "ID не распознан");
        }
    }
}

