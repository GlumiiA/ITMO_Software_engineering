package src.main.java.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.object.Flat;
import src.main.java.network.Record;
import src.main.java.network.ServerUDP;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    ServerUDP serverUDP;
    int len;
    CommandManager commandManager;
    static String Command;
    static String args;
    static Integer numericArgument;
    static Flat flat;
    private final ExecutorService requestCashedPool = Executors.newCachedThreadPool();
    private static final ExecutorService responseCashedPool = Executors.newCachedThreadPool();
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    static DatabaseHandler databaseHandler;

    public ServerHandler(ServerUDP serverUDP, int len, CommandManager commandManager, DatabaseHandler databaseHandler) {
        this.serverUDP = serverUDP;
        this.len = len;
        this.commandManager = commandManager;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void run() {
        while (true) {
            Record arr = serverUDP.receiveData(len);
            if (arr != null) {
                requestCashedPool.execute(() -> {
                    Request commandd = (Request) serverUDP.deserialize(arr.getArr());
                    if (commandd != null) {
                        Command = commandd.getCommandType().Type();
                        flat = commandd.getFlatArgument();
                        args = commandd.getArgs();
                        numericArgument = commandd.getNumericArgument();
                        forkJoinPool.execute(() -> {
                            var command = commandManager.getCommands().get(Command);
                            logger.info("команда получена");
                            if (command == null) {
                                Response response = new Response(false, "Команда '" + Command + "' не найдена. Наберите 'help' для справки");
                            } else {
                                try {
                                    Response response;
                                    if (Command.equals("login")) {
                                        if (databaseHandler.validateUser(commandd.getLogin(), commandd.getPassword())) {
                                            response = command.apply(commandd);
                                        } else response = new Response(false, "Неверный логин или пароль");
                                    } else if (Command.equals("register")){
                                        response = command.apply(commandd);
                                    }else {
                                        logger.info("команда готовится к выполннению");
                                        response = command.apply(commandd);
                                        logger.info("команда выполнена");
                                    }
                                    responseCashedPool.execute(() -> {
                                        var data = ServerUDP.serializer(response);
                                        serverUDP.sendData(new Record(data, arr.getAddr()));
                                    });
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            logger.info("Команда обработана!");

                        });

                    }
                });
            }
        }
    }
}
