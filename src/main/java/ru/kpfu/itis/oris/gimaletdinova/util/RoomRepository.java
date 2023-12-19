package ru.kpfu.itis.oris.gimaletdinova.util;

import ru.kpfu.itis.oris.gimaletdinova.dao.Dao;
import ru.kpfu.itis.oris.gimaletdinova.server.GameServer;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.*;

public class RoomRepository {
    private static final int CODE_LENGTH = 4;
    public static final Dao dao = new Dao();
    private static final Random random = new Random();
    private static final Map<String, Object[]> rooms;

    static {
        try {
            rooms = dao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getPort(String room) {
        if (exist(room)) {
            return (Integer) rooms.get(room)[0];
        } else {
            update(room);
            return getPort(room);
        }
    }

    public static void update(String code) {
        try {
            rooms.put(code, dao.get(code));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static InetAddress getAddress(String room) {
        if (exist(room)) {
            return (InetAddress) rooms.get(room)[1];
        } else {
            update(room);
            return getAddress(room);
        }
    }

    private static boolean exist(String room) {
        return rooms.containsKey(room);
    }

    public static String createRoom() {
        GameServer server = new GameServer();
        String code = generateCode();
        rooms.put(code, new Object[]{server.getPort(), server.getAddress()});
        try {
            dao.save(code, server.getPort(), server.getAddress());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return code;
    }

    private static String generateCode() {
        StringBuilder builder;
        String code;
        do {
            builder = new StringBuilder();
            for (int i = 0; i < CODE_LENGTH; i++) {
                builder.append(getSymbol());
            }
            code = builder.toString();
        } while (rooms.containsKey(code));
        return code;
    }

    private static char getSymbol() {
        return (char) random.nextInt('A', 'Z' + 1);
    }

}
