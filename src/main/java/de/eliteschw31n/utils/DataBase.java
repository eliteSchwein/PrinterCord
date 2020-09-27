package de.eliteschw31n.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBase {

    private Connection connection;
    private String dbPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "database.db";

    public DataBase() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC not found!");
            e.printStackTrace();
        }
        initDataBase();
    }

    private void initDataBase() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            if (!connection.isClosed())
                System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (!connection.isClosed() && connection != null) {
                        connection.close();
                        if (connection.isClosed())
                            System.out.println("Connection to Database closed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setup() {
        executeCommand("CREATE TABLE IF NOT EXISTS GuildAdmins (GuildId VARCHAR(100),AdminId VARCHAR(100),IsRole BOOLEAN)");
    }

    public Connection getConnection() {
        if (connection == null) {
            initDataBase();
        }
        try {
            if (connection.isClosed()) {
                initDataBase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void executeCommand(String command) {
        getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(command)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
