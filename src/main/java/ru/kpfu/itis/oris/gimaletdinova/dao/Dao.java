package ru.kpfu.itis.oris.gimaletdinova.dao;

import ru.kpfu.itis.oris.gimaletdinova.util.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Dao {
    private final Connection connection = DatabaseConnection.getConnection();
    private final String SELECT_SQL = "Select * from rooms;";
    private final String SAVE_SQL = "insert into rooms(code, port) values(?, ?);";
    private final String DELETE_SQL = "delete from rooms where code=?";

    public Map<String, Integer> getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_SQL);
        Map<String, Integer> map = new HashMap<>();
            while (resultSet.next()) {
                map.put(resultSet.getString(1), resultSet.getInt(2));
            }
        return map;
    }

    public void save(String code, Integer port) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SAVE_SQL);
        statement.setString(1, code);
        statement.setInt(2, port);
        statement.executeUpdate();
    }

    public void delete(String code) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setString(1, code);
        statement.executeUpdate();
    }
}
