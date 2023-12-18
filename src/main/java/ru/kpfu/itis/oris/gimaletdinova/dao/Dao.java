package ru.kpfu.itis.oris.gimaletdinova.dao;

import ru.kpfu.itis.oris.gimaletdinova.util.DatabaseConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Dao {
    private final Connection connection = DatabaseConnection.getConnection();
    private final String SELECT_SQL = "select * from rooms;";
    private final String SAVE_SQL = "insert into rooms(code, port, address) values(?, ?, ?);";
    private final String DELETE_SQL = "delete from rooms where port=?;";
    private final String EXIST_SQL = "select 1 from rooms where code=?;";

    public Map<String, Object[]> getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_SQL);
        Map<String, Object[]> map = new HashMap<>();
            while (resultSet.next()) {
                Object[] data = new Object[2];
                data[0] = resultSet.getInt("port");
                try {
                    data[1] = InetAddress.getByName(resultSet.getString("address"));
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                map.put(resultSet.getString("code"), data);
            }
        return map;
    }

    public void save(String code, Integer port, InetAddress address) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SAVE_SQL);
        statement.setString(1, code);
        statement.setInt(2, port);
        statement.setString(3, address.getHostAddress());
        statement.executeUpdate();
    }

    public void delete(Integer port) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setInt(1, port);
        statement.executeUpdate();
    }

    public boolean isRoomExist(String code) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(EXIST_SQL);
        statement.setString(1, code);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

}
