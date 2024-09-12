package src.main.java.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;
import src.main.java.manager.DatabaseHandler;

/**
 * Команда 'add_if_max'. Добавляет новый элемент в коллекцию, если его площадь выше максимальной.
 */
public class AddIfMax extends Command {
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    DatabaseHandler dbHandler;
    public static final Logger logger = LoggerFactory.getLogger(AddIfMax.class);

    public AddIfMax(ConsoleInterface console, CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super("addIfMax", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
    }


    public Response apply(Request request) {
        logger.info("выполнение addIfMax ");
        var flat = request.getFlatArgument();
        logger.info("Тааак ");
        var maxArea = collectionManager.getLast().getArea();
        logger.info("Тааааааак ");
        logger.info("maxArea: " + String.valueOf(maxArea));
        if (flat.getArea() > maxArea) {
            if (!dbHandler.insertFlat(flat)) {
                return new Response(false, "При добавлении возникла ошибка!");
            }
            collectionManager.init();
//            int idFlat = dbHandler.getAddIdFlat();
//            logger.info(String.valueOf(idFlat));
//            flat.setId(idFlat);
//            collectionManager.addToCollection(request.getFlatArgument());
            collectionManager.saveCollection();
            return new Response("Квартира успешно добавлена!");
        } else {
            return new Response(false, "Квартира не добавлена, площадь не максимальная :<  (" + flat.getArea() + " < " + maxArea + ")");
        }
    }
}