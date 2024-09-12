package src.main.java.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.object.Flat;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;
import src.main.java.manager.DatabaseHandler;

import java.sql.SQLException;
import java.util.List;

public class Clear extends Command{
    public static final Logger logger = LoggerFactory.getLogger(Add.class);
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;
    DatabaseHandler dbHandler;
    public Clear(ConsoleInterface console, CollectionManager collectionManager, DatabaseHandler dbHandler){
        super("clear", "очистить коллекцию");
        this.console =console;
        this.collectionManager = collectionManager;
        this.dbHandler = dbHandler;
    }

    public Response apply(Request request) throws SQLException {
        List<Integer> clearList = dbHandler.clearCollection();
         if (clearList != null){
             collectionManager.clearCollection(clearList);
             Flat.ClearId();
             return new Response("Коллекция очищена!");
         } else{
             return new Response("Отсутствуют квартиры с данным логином");
         }
    }
}
