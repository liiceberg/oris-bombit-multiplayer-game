package ru.kpfu.itis.oris.gimaletdinova.dao;

import ru.kpfu.itis.oris.gimaletdinova.util.Block;
import ru.kpfu.itis.oris.gimaletdinova.util.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Dao {
    private final Connection connection = DatabaseConnection.getConnection();
    private final String SELECT_SQL = "Select * from rooms;";
    private final String SAVE_SQL = "insert into rooms(code, port, field) values(?, ?, ?);";
    private final String DELETE_SQL = "delete from rooms where code=?;";
    private final String GET_GAME_FIELD = "Select field from rooms where code=?;";

    public Map<String, Integer> getAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_SQL);
        Map<String, Integer> map = new HashMap<>();
            while (resultSet.next()) {
                map.put(resultSet.getString(1), resultSet.getInt(2));
            }
        return map;
    }

    public static void main(String[] args) throws SQLException {
        Block[][] b = new Dao().getField("KUZYYP");
        for (Block[] blocks: b) {
            for (Block bl: blocks) System.out.println(bl);
        }

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

    public void save(String code, Integer port, Block[][] field) throws SQLException {
        Array array = connection.createArrayOf("varchar", field);
        PreparedStatement statement = connection.prepareStatement(SAVE_SQL);
        statement.setString(1, code);
        statement.setInt(2, port);
        statement.setArray(3, array);
        statement.executeUpdate();
    }

    public void delete(String code) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_SQL);
        statement.setString(1, code);
        statement.executeUpdate();
    }

}
