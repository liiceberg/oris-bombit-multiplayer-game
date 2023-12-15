package ru.kpfu.itis.oris.gimaletdinova.dao;

import ru.kpfu.itis.oris.gimaletdinova.util.Block;
import ru.kpfu.itis.oris.gimaletdinova.util.DatabaseConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Dao {
    private final Connection connection = DatabaseConnection.getConnection();
    private final String SELECT_SQL = "select code, port, address from rooms;";
    private final String SAVE_SQL = "insert into rooms(code, port, address, field) values(?, ?, ?, ?);";
    private final String DELETE_SQL = "delete from rooms where port=?;";
    private final String GET_GAME_FIELD = "select field from rooms where code=?;";
    private final String UPDATE_FIELD = "update rooms set field=? where code=?;";

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

    public Block[][] getField(String room) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_GAME_FIELD);
        statement.setString(1, room);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Array array = resultSet.getArray("field");
        String[][] a = (String[][]) array.getArray();
        int height = a.length;
        int width = a[0].length;
        Block[][] field = new Block[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = Block.valueOf(a[i][j]);
            }
        }
        return field;
    }

    public void updateField(String code, Block[][] field) throws SQLException {
        Array array = connection.createArrayOf("varchar", field);
        PreparedStatement statement = connection.prepareStatement(UPDATE_FIELD);
        statement.setArray(1, array);
        statement.setString(2, code);
        statement.executeUpdate();
    }

    public void save(String code, Integer port, InetAddress address, Block[][] field) throws SQLException {
        Array array = connection.createArrayOf("varchar", field);
        PreparedStatement statement = connection.prepareStatement(SAVE_SQL);
        statement.setString(1, code);
        statement.setInt(2, port);
        statement.setString(3, address.getHostAddress());
        statement.setArray(4, array);
        statement.executeUpdate();
    }

    public void delete(Integer port) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setInt(1, port);
        statement.executeUpdate();
    }

}
