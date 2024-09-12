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

public class Add extends Command {
    public static final Logger logger = LoggerFactory.getLogger(Add.class);
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    DatabaseHandler dbHandler;
    int cur_id;

    public Add(ConsoleInterface console, CollectionManager collectionManager, DatabaseHandler dbHandler) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
        collectionManager.saveCollection();
        this.dbHandler =dbHandler;
    }
    @Override
    public Response apply(Request request) throws SQLException {
        var flat = request.getFlatArgument();
        if (flat != null) {
            if (!dbHandler.insertFlat(request.getFlatArgument())){
                return new Response(false, "При добавлении возникла ошибка!");
            }
            int idFlat = dbHandler.getAddIdFlat();
            logger.info(String.valueOf(idFlat));
            flat.setId(idFlat);
            collectionManager.addToCollection(request.getFlatArgument());
            return new Response("Квартира успешно добавлена!");
        }
        return new Response(false, "квартира пустая");
    }
}
