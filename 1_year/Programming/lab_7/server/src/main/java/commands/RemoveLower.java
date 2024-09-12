package src.main.java.commands;

import src.main.java.common.network.Request;
import src.main.java.common.object.Flat;
import src.main.java.common.utility.Command;
import src.main.java.common.network.Response;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;
import src.main.java.manager.DatabaseHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RemoveLower extends Command {
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    DatabaseHandler dbHandler;

    public RemoveLower(ConsoleInterface console, CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super("remove_lower","удалить из коллекции все элементы, меньше, чем заданный");
        this.console = console;
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
    }

    public Response apply(Request request) {
        try {
            int id = request.getNumericArgument();
            Iterator<Flat> iterator = collectionManager.getCollection().iterator();
            List<Integer> lower = new ArrayList<>();
            while (iterator.hasNext()) {
                var e = iterator.next();
                if (e.getId() >= id) break;
                else {
                    lower.add(e.getId());
                }
            }
            for (var e : lower) {
                var flatToRemove = collectionManager.getById(id);
                dbHandler.removeFlatByID(e);
                collectionManager.removeFromCollection(flatToRemove);
                collectionManager.saveCollection();
            }
            collectionManager.init();
            return new Response("Все меньшие текущего пользователя квартиры удалены!");
        } catch (NumberFormatException e) {
            return new Response(false, "ID не распознан");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
