package ru.kpfu.itis.oris.gimaletdinova.server;

import ru.kpfu.itis.oris.gimaletdinova.model.Message;
import ru.kpfu.itis.oris.gimaletdinova.util.MessageConverter;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class GameServer implements Closeable, Runnable {
    private final int port;
    public static final int BUFFER_LENGTH = 512;
    private final byte[] buffer = new byte[BUFFER_LENGTH];
    private final DatagramSocket socket;
    private final List<Client> players = new ArrayList<>();
    public GameServer() {
        try {
            socket = new DatagramSocket();
            port = socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                Message response = parseMessage(packet.getData());

                byte[] data = MessageConverter.convertToBytes(response);
                packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private Message parseMessage(byte[] data) throws IOException, ClassNotFoundException {
        Message message = MessageConverter.convertFromBytes(data);
        switch (message.getMessageType()) {
            case CONNECT -> {
                int number = addPlayer();
                return new Message(String.valueOf(number));
            }
        }
        System.out.print(message.getMessageType());
        System.out.print(" ");
        System.out.print(message.getUsername());
        System.out.print(" ");
        System.out.println(message.getContent());
        return new Message("");
    }

    private int addPlayer() {
        if (players.size() <= 4) {
            Client client = new Client(this);
            players.add(client);
            new Thread(client).start();
        }
        return players.size();
    }

    public int getPort() {
        return port;
    }

    @Override
    public void close() {
        socket.close();
    }

    static class Client implements Runnable {
        private final GameServer server;

        public Client(GameServer gameServer) {
            this.server = gameServer;
        }

        @Override
        public void run() {
            System.out.println("Player added");

//            try {
//                while (true) {
//                    String message = input.readLine();
//                    server.sendMessage(message, this);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        }

    }

}
