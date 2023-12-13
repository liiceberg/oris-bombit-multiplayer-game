package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import ru.kpfu.itis.oris.gimaletdinova.model.message.AddBombMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.LoseMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.Message;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MoveMessage;
import ru.kpfu.itis.oris.gimaletdinova.util.*;
import ru.kpfu.itis.oris.gimaletdinova.view.Character;

import java.io.IOException;
import java.util.*;

public class GameController {
    private final FXMLLoader fxmlLoader = new FXMLLoader(GameWaitingViewController.class.getResource("/fxml/game-over-view.fxml"));
    @FXML
    public Label code;
    @FXML
    public VBox infoBox;
    private Character player;
    private int playerPosition;
    private final List<Character> characters = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private int height;
    private KeyCode keyCode;
    private Image bomb;
    private double blockSize;
    private Block[][] blocks;
    private BlockBuilder blockBuilder;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane gridPane;
    private int losersCount = 0;

    @FXML
    public void initialize() {
        Platform.runLater(this::initAll);
        Platform.runLater(this::listen);
    }
    private void listen() {
//        new Thread(new ClientListener()).start();
    }

    private void initAll() {
        String[] users = ControllerHelper.getApplication().getUsers();
        int[] characters = ControllerHelper.getApplication().getCharacters();
        for (int i = 0; i < users.length; i++) {
            Player p = new Player(i + 1, users[i], characters[i]);
            players.add(p);
        }
        initRoom();
        initGameField();
        initActions();
        initPlayers();
        initPlayAttributes();
        initInfoTable();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updatePlayer();
            }
        };
        timer.start();
    }

    public void updatePlayer() {
        if (blocks[getRowIndex()][getColumnIndex()] == Block.FIRE) {
            removeCharacter(playerPosition);
            Map<String, Object> map = new HashMap<>();
            map.put("position", playerPosition);
            LoseMessage loseMessage = new LoseMessage(map);
            ControllerHelper.getApplication().getClientPlayer().send(loseMessage);
            ControllerHelper.getApplication().isWin = false;
        }
        if (keyCode == null) {
            player.getAnimation().stop();
        } else switch (keyCode) {
            case UP -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height * 3);
                if (isField(KeyCode.UP)) {
                    player.moveY(-2);
                    sendMoveMessage(keyCode.getCode());
                }
            }
            case DOWN -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(0);
                if (isField(KeyCode.DOWN)) {
                    player.moveY(2);
                    sendMoveMessage(keyCode.getCode());
                }
            }
            case RIGHT -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height * 2);
                if (isField(KeyCode.RIGHT)) {
                    player.moveX(2);
                    sendMoveMessage(keyCode.getCode());
                }
            }
            case LEFT -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height);
                if (isField(KeyCode.LEFT)) {
                    player.moveX(-2);
                    sendMoveMessage(keyCode.getCode());
                }
            }
        }
    }

    private void sendMoveMessage(int keyCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", keyCode);
        map.put("position", playerPosition);
        MoveMessage moveMessage = new MoveMessage(map);
        ControllerHelper.getApplication().getClientPlayer().send(moveMessage);
    }

    private void sendAddBombMessage(int x, int y) {
        Map<String, Object> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        map.put("position", playerPosition);
        AddBombMessage message = new AddBombMessage(map);
        ControllerHelper.getApplication().getClientPlayer().send(message);
    }

    private void initGameField() {
        blocks = RoomRepository.getGameField(ControllerHelper.getApplication().getRoom());
        blockSize = gridPane.getPrefHeight() / gridPane.getRowCount();
        blockBuilder = new BlockBuilder(blockSize);

        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                gridPane.add(blockBuilder.getView(blocks[i][j]), j, i);
            }
        }
    }

    private void initPlayAttributes() {
        bomb = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tnt.png")));
        height = player.getHEIGHT();
    }

    private void setCharacterOffset(int position, Character character) {
        int x = 0, y = 0;
        switch (position) {
            case 1 -> {
                x = (int) blockSize;
                y = (int) blockSize;
            }
            case 2 -> {
                x = (int) blockSize * (GameFieldRepository.WIDTH - 2);
                y = (int) blockSize;
            }
            case 3 -> {
                x = (int) blockSize * (GameFieldRepository.WIDTH - 2);
                y = (int) blockSize * (GameFieldRepository.HEIGHT - 2);
            }
            case 4 -> {
                x = (int) blockSize;
                y = (int) blockSize * (GameFieldRepository.HEIGHT - 2);
            }
        }
        character.moveX(x);
        character.moveY(y);
    }

    private void initRoom() {
        code.setText(ControllerHelper.getApplication().getRoom());
    }

    private void initActions() {
        anchorPane.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.SPACE)) {
                int x = getColumnIndex();
                int y = getRowIndex();
                sendAddBombMessage(x, y);
                addBomb(x, y);
            } else {
                keyCode = event.getCode();
            }
        });
        anchorPane.getScene().setOnKeyReleased(event -> {
            if (!event.getCode().equals(KeyCode.SPACE)) {
                keyCode = null;
            }
        });
    }

    private void addBomb(int x, int y) {
        ImageView view = new ImageView(bomb);
        view.setFitHeight(blockSize / 1.5);
        view.setFitWidth(blockSize / 1.5);
//        System.out.println(player.getPlayerTranslateY() + " " + player.getPlayerTranslateX());
//        System.out.println();
//        System.out.println("b " + player.getBoundsInLocal() + " " + player.getBoundsInParent());
        gridPane.add(view, x, y);
        PauseTransition pause = new PauseTransition(Duration.millis(3_000));
        pause.setOnFinished(event -> {
            gridPane.getChildren().remove(view);
            for (int i = y - 1; i < y + 2; i++) {
                explode(x, i);
            }
            for (int i = x - 1; i < x + 2; i++) {
                explode(i, y);
            }
        });
        pause.play();
    }

    private void explode(int i, int j) {
        if (blocks[j][i] != Block.WALL) {
            boolean isObstacle = blocks[j][i] == Block.OBSTACLE;
            blocks[j][i] = Block.FIRE;

            ImageView fire = blockBuilder.getView(Block.FIRE);
            gridPane.add(fire, i, j);
            PauseTransition pause = new PauseTransition(Duration.millis(1_000));
            pause.setOnFinished(event -> {
                gridPane.getChildren().remove(fire);
                if (isObstacle) {
                    ImageView field = blockBuilder.getView(Block.FIELD);
                    gridPane.add(field, i, j);
                    for (Character c : characters) {
                        c.toFront();
                    }
                }
                blocks[j][i] = Block.FIELD;
            });
            pause.play();
        }
    }

    private int getRowIndex() {
        return (int) Math.ceil(player.getPlayerTranslateY() / blockSize);
    }

    private int getColumnIndex() {
        return (int) Math.ceil(player.getPlayerTranslateX() / blockSize);
    }

    private boolean isField(KeyCode code) {
        switch (code) {
            case UP -> {
                int row = (int) Math.ceil((player.getPlayerTranslateY() - blockSize - 1) / blockSize);
                return blocks[row][getColumnIndex()] == Block.FIELD || blocks[row][getColumnIndex()] == Block.FIRE;
            }
            case DOWN -> {
                int row = (int) Math.ceil((player.getPlayerTranslateY() + 1) / blockSize);
                return blocks[row][getColumnIndex()] == Block.FIELD || blocks[row][getColumnIndex()] == Block.FIRE;
            }
            case RIGHT -> {
                int column = (int) Math.ceil((player.getPlayerTranslateX() + 1) / blockSize);
                return blocks[getRowIndex()][column] == Block.FIELD || blocks[getRowIndex()][column] == Block.FIRE;
            }
            case LEFT -> {
                int column = (int) Math.ceil((player.getPlayerTranslateX() - blockSize - 1) / blockSize);
                return blocks[getRowIndex()][column] == Block.FIELD || blocks[getRowIndex()][column] == Block.FIRE;
            }

        }
        return false;
    }

    private void addPlayer(int imgNumber, int position) {
        Character character = new Character(CharacterFactory.create(imgNumber), blockSize);
        setCharacterOffset(position, character);
        characters.add(character);
    }

    private void initPlayers() {
        playerPosition = ControllerHelper.getApplication().getUser().getPosition();
        for (Player p: players) {
            addPlayer(p.character, p.position);
            if (p.position == playerPosition) {
                player = characters.get(playerPosition - 1);
            }
        }
        gridPane.getChildren().addAll(this.characters);
    }

    private void initInfoTable() {
        for (Player p: players) {
            ImageView img = CharacterFactory.create(p.character);
            Character.setSettings(img, blockSize);
            Label divisor = new Label(" ");
            infoBox.getChildren().addAll(p.title, p.username, img, divisor);
        }
    }

    public void moveCharacter(int position, int direction) {
        Character character = characters.get(position);
    }
    public void removeCharacter(int position) {
        Player p = players.get(position - 1);
        p.isLose = true;
        p.title.setStyle("-fx-text-fill: red;");
        p.username.setStyle("-fx-text-fill: red;");
        gridPane.getChildren().remove(characters.get(p.position - 1));
        if (++losersCount == players.size() - 1) {
            gameOver();
        }
    }

    private void gameOver() {
        Stage stage = (Stage) code.getScene().getWindow();
        try {
            ControllerHelper.loadAndShowFXML(fxmlLoader, stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ClientListener implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message m;
                if ((m = ControllerHelper.getApplication().getClientPlayer().getServerMessages().poll()) != null) {
                    System.out.println(m);
                    switch (m.getMessageType()) {
                        case MOVE -> moveCharacter(((MoveMessage) m).getPlayerPosition(), ((MoveMessage) m).getCode());
                        case ADD_BOMB -> {
                            int x = ((AddBombMessage) m).getX();
                            int y = ((AddBombMessage) m).getY();
                            System.out.println("evil bomb" + x + " " + y);
                            addBomb(x, y);
                        }
                        case LOSE -> removeCharacter(((LoseMessage) m).getPlayerPosition());
                    }
                }
            }
        }
    }

    private static class Player {
        public int position;
        public int character;
        public Label username;
        public Label title;
        public boolean isLose = false;

        public Player(int position, String username, int character) {
            this.position = position;
            this.character = character;
            this.title = new Label("Player " + position);
            this.username = new Label(username);
        }
    }
}
