package ru.kpfu.itis.oris.gimaletdinova.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://ep-fragrant-union-a5lqrlgg.us-east-2.aws.neon.tech/bombit_game";
    private static final String USER = "player";
    private static final String PASSWORD = "WJU4RlwNZh5b";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(
                        URL,
                        USER,
                        PASSWORD
                );
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
