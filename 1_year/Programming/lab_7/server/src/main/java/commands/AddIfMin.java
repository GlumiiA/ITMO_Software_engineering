package src.main.java.commands;

import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;
import src.main.java.manager.DatabaseHandler;

public class AddIfMin extends Command {
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    DatabaseHandler dbHandler;
    public AddIfMin(ConsoleInterface console, CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super("addIfMin", "добавить новый элемент в коллекцию, если его значение не превышает значение наименьшего элемента этой коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
        this.dbHandler =dbHandler;
    }


    public Response apply(Request request) {
        var flat = request.getFlatArgument();
            var minArea = collectionManager.getFirst().getArea();
            if (flat.getArea() < minArea) {
                if (!dbHandler.insertFlat(flat)) {
                    return new Response(false, "При добавлении возникла ошибка!");
                }
                int idFlat = dbHandler.getAddIdFlat();
                flat.setId(idFlat);
                collectionManager.init();
//                collectionManager.addToCollection(request.getFlatArgument());
//                collectionManager.saveCollection();
                return new Response("Квартира успешно добавлена!");
            } else {
                return new Response("Квартира не добавлена, площадь не минимальная(" + flat.getArea() + " > " + minArea +")");
            }
    }
}
