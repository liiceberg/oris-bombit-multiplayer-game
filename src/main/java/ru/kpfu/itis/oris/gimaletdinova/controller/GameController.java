package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import ru.kpfu.itis.oris.gimaletdinova.model.message.AddBombMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.MoveMessage;
import ru.kpfu.itis.oris.gimaletdinova.util.*;
import ru.kpfu.itis.oris.gimaletdinova.view.Character;

import java.util.*;

public class GameController {

    public Label code;
    private Character player;
    private int playerPosition;
    private final List<Character> players = new ArrayList<>();
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

    @FXML
    public void initialize() {
        Platform.runLater(this::initAll);
    }
    private void initAll() {
        initRoom();
        initGameField();
        initActions();
        initPlayers();
        initPlayAttributes();
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
            System.out.println("DEAD");
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
        System.out.println(player.getPlayerTranslateY() + " " + player.getPlayerTranslateX());
        System.out.println();
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
                    for (Character c: players) {
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
        Character character =  new Character(CharacterFactory.create(imgNumber), blockSize);
        setCharacterOffset(position, character);
        players.add(character);
    }
    private void initPlayers() {
        int[] characters = ControllerHelper.getApplication().getCharacters();
        playerPosition = ControllerHelper.getApplication().getUser().getPosition();
        for (int i = 0; i < characters.length; i++) {
            addPlayer(characters[i], i + 1);
            if (i + 1 == playerPosition) {
                player = players.get(i);
            }
        }
        gridPane.getChildren().addAll(players);
    }

    public void moveCharacter(int position, int direction) {
        Character character = players.get(position);
    }
    private static class ClientListener implements Runnable {
        @Override
        public void run() {

        }
    }
}
