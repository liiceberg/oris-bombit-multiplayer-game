package ru.kpfu.itis.oris.gimaletdinova.model.message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConnectMessage extends Message {
    private String username;
    private int port;
    private InetAddress address;

    public ConnectMessage(Map<String, Object> content) {
        super(MessageType.CONNECT, content);
    }

    public ConnectMessage(byte[] content) {
        super(MessageType.CONNECT, Arrays.copyOfRange(content, 1, content.length));
        Map<String, Object> map = getContent();
        username = (String) map.get("username");
        port = (int) map.get("port");
        address = (InetAddress) map.get("address");
    }

//    public ConnectMessage(String username, int port, InetAddress address) {
//        super(MessageType.CONNECT, map);
//        Map<String, Object> map = new HashMap<>();
//        this.username = username;
//        this.port = port;
//        this.address = address;
//
//    }

    @Override
    public Map<String, Object> getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        Map<String, Object> map = new HashMap<>();
        StringBuilder builder = new StringBuilder();

        char c;
        while ((c = buffer.getChar()) != Character.MIN_VALUE) {
            builder.append(c);
        }

        map.put("username", builder.toString());
        map.put("port", buffer.getInt());
        builder = new StringBuilder();
        while ((c = buffer.getChar()) != Character.MIN_VALUE && buffer.hasRemaining()) {
            builder.append(c);
        }
        try {
            map.put("address", InetAddress.getByName(builder.toString()));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public void setContent(Map<String, Object> map) {
        username = (String) map.get("username");
        port = (int) map.get("port");
        address = (InetAddress) map.get("address");
        ByteBuffer buffer = ByteBuffer.allocate(username.length() * 2 + 2 + 4 + address.getHostAddress().length() * 2);
        for (char c: username.toCharArray()) {
            buffer.putChar(c);
        }
        buffer.putChar(Character.MIN_VALUE);
        buffer.putInt(port);
        for (char c: address.getHostAddress().toCharArray()) {
            buffer.putChar(c);
        }
        content = buffer.array();
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }
}
