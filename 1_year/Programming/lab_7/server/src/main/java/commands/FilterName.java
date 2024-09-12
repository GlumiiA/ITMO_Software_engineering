package src.main.java.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.object.Flat;
import src.main.java.common.utility.Command;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilterName extends Command {
    public static final Logger logger = LoggerFactory.getLogger(Add.class);
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;

    public FilterName(ConsoleInterface console, CollectionManager collectionManager) {
        super("filterName", "вывести элементы, значение поля name которых начинается с заданной подстроки");
        this.console = console;
        this.collectionManager = collectionManager;
    }


    public Response apply(Request request) {
        var name =  request.getArgs();
        logger.info("Подстрока: " + name);
        Iterator<Flat> iterator = collectionManager.getCollection().iterator();
        List<Flat> filterFlat = new ArrayList<>();
        while (iterator.hasNext()) {
            var e = iterator.next();
            if (e.getName().contains(name)){
                filterFlat.add(e);
            };
        }
        logger.info(filterFlat.toString());
        return new Response(String.valueOf(true));
    }
}
