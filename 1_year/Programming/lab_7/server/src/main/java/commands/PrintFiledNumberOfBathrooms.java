package src.main.java.commands;

import src.main.java.common.network.Request;
import src.main.java.common.object.Flat;
import src.main.java.common.utility.Command;
import src.main.java.common.network.Response;
import src.main.java.java.client.utility.ConsoleInterface;
import src.main.java.manager.CollectionManager;

import java.util.*;

public class PrintFiledNumberOfBathrooms extends Command{
    private final ConsoleInterface console;
    private final CollectionManager collectionManager;

    public PrintFiledNumberOfBathrooms(ConsoleInterface console, CollectionManager collectionManager) {
        super("printFiledNumberOfBathrooms", "вывести значения поля numberOfBathrooms всех элементов в порядке убывания");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    public Response apply(Request request) {
        Iterator<Flat> iterator = collectionManager.getCollection().iterator();
        List<Integer> array = new ArrayList<>();
        while (iterator.hasNext()) {
            var e = iterator.next();
            array.add(e.getNumberOfBathrooms());
        }
        StringBuilder info = new StringBuilder();
        for (Integer element : array) {
            info.append(element + "\n");
        }
        return new Response(info.toString().trim());
    }
}