package src.main.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.commands.*;
import src.main.java.java.client.utility.Console;
import src.main.java.manager.*;
import src.main.java.network.ServerUDP;

import java.net.UnknownHostException;
import java.sql.SQLException;

public class App {
    public static final Logger logger = LoggerFactory.getLogger(App.class);
    static String[] userCommand = new String[2];
    static byte arr[] = new byte[8000];
    static int len = arr.length;

    public static void main(String[] args) throws SQLException, UnknownHostException {
        Console console = new Console();
        String URL = "jdbc:postgresql://localhost:5432/dbstud";
        UserManager userManager = new UserManager();
        userManager.Credentials();
        DatabaseHandler dbHandler = new DatabaseHandler(URL, userManager.getUsername(), userManager.getPassword() );
        dbHandler.connetToDataBase();
        CollectionManager collectionManager = new CollectionManager(dbHandler);
        collectionManager.init();
        var serverUDP = new ServerUDP(5874);
        while (!serverUDP.init()) {
            logger.info("Менеджер сетевого взаимодействия инициализирован!");
        }
        var commandManager = new CommandManager() {{
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("clear", new Clear(console, collectionManager, dbHandler));
            register("add", new Add(console, collectionManager, dbHandler));
            register("remove", new Remove(console, collectionManager, dbHandler));
            register("update", new Update(console, collectionManager,dbHandler));
            register("addIfMax", new AddIfMax(console, collectionManager, dbHandler));
            register("addIfMin", new AddIfMin(console, collectionManager, dbHandler));
            register("removeLower", new RemoveLower(console, collectionManager, dbHandler));
            register("filterName", new FilterName(console, collectionManager));
            register("printFiledNumberOfBathrooms", new PrintFiledNumberOfBathrooms(console, collectionManager));
            register("printUniqueNumberOfBathrooms", new PrintUniqueNumberOfBathrooms(console, collectionManager));
            register("login", new Login(dbHandler));
            register("register", new Register(dbHandler));
        }};
        ServerHandler serverHandler = new ServerHandler(serverUDP, len, commandManager, dbHandler);
        serverHandler.run();
        System.out.println("&K");
    }

}
