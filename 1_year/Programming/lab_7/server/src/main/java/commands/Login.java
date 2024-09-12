package src.main.java.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import src.main.java.common.network.Request;
import src.main.java.common.network.Response;
import src.main.java.common.utility.Command;
import src.main.java.manager.DatabaseHandler;

import java.sql.SQLException;

public class Login extends Command {
    private final DatabaseHandler databaseHandler;
    public static final Logger logger = LoggerFactory.getLogger(Login.class);

    public Login(DatabaseHandler databaseHandler) {
        super("login", "авторизация");
        this.databaseHandler = databaseHandler;
    }

    @Override
    public Response apply(Request request) throws SQLException {
        logger.info("выполняется login");
        if (databaseHandler.validateUser(request.getLogin(), request.getPassword())) {
            return new Response(true, "Вы успешно авторизованы!");
        } else return new Response(false, "Неверные логин или пароль!");
    }
}
