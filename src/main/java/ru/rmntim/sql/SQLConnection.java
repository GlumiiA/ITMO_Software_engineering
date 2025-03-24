package ru.rmntim.sql;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


// url = "dbc:postgresql://localhost:5656/studs"
//    private String user = "s408675";
//    private String password = "j3AVBsamqR8ghKqn";
@Named
@SessionScoped
public class SQLConnection implements Serializable {
    private String url =  "jdbc:postgresql://localhost:5432/dbstud";
    private String user = "user1";
    private String password = "dbstud456";
    private Connection connection;
    public Connection connect() throws ClassNotFoundException, SQLException {

        Class.forName("org.postgresql.Driver");


        Connection connection = DriverManager.getConnection(url, user, password);

        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}