package ru.kpfu.itis.oris.gimaletdinova.model.message.messages;

import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MessageType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ConnectMessage extends Message {
    private String username;
    private int port;
    private InetAddress address;
    public ConnectMessage(byte[] content) {
        super(MessageType.CONNECT, Arrays.copyOfRange(content, 1, content.length));
    }

    public ConnectMessage(String username, int port, InetAddress address) {
       super(MessageType.CONNECT);
       this.username = username;
       this.port = port;
       this.address = address;
        setContent();
    }

    @Override
    protected void getContent() {
        ByteBuffer buffer = ByteBuffer.wrap(content);
        StringBuilder builder = new StringBuilder();

        char c;
        while ((c = buffer.getChar()) != Character.MIN_VALUE) {
            builder.append(c);
        }

        username = builder.toString();
        port = buffer.getInt();
        builder = new StringBuilder();
        while ((c = buffer.getChar()) != Character.MIN_VALUE && buffer.hasRemaining()) {
            builder.append(c);
        }
        try {
            address = InetAddress.getByName(builder.toString());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setContent() {
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
