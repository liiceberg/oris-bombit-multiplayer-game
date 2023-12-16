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
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.AddBombMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.LoseMessage;
import ru.kpfu.itis.oris.gimaletdinova.model.message.messages.MoveMessage;
import ru.kpfu.itis.oris.gimaletdinova.util.*;
import ru.kpfu.itis.oris.gimaletdinova.view.Character;

import java.io.IOException;
import java.util.*;

public class GameController implements Controller {
    @FXML
    public Label code;
    @FXML
    public VBox infoBox;
    private Character player;
    private int playerPosition;
    private final List<Character> characters = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private AnimationTimer updateTimer;
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
    }

    private void initAll() {
        String[] users = ApplicationUtil.getApplication().getUsers();
        int[] characters = ApplicationUtil.getApplication().getCharacters();
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
        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updatePlayer();
            }
        };
        updateTimer.start();
    }

    public void updatePlayer() {
        if (blocks[getRowIndex(player)][getColumnIndex(player)] == Block.FIRE) {
            updateTimer.stop();
            removeCharacter(playerPosition);
            LoseMessage loseMessage = new LoseMessage(playerPosition);
            ApplicationUtil.getApplication().getClientPlayer().send(loseMessage);
            ApplicationUtil.getApplication().isWin = false;
            return;
        }
        Integer direction = keyCode == null ? Integer.MIN_VALUE : keyCode.getCode();
        moveCharacter(playerPosition, direction);
        sendMoveMessage(direction);
    }

    public void moveCharacter(int position, Integer direction) {
        Character character = characters.get(position - 1);
        if (direction == Integer.MIN_VALUE) {
            character.getAnimation().stop();
        } else {
            character.getAnimation().play();
            if (direction == KeyCode.RIGHT.getCode()) {
                character.getAnimation().setOffsetY(height * 2);
                if (isField(KeyCode.RIGHT, character)) {
                    character.moveX(2);
                }
                return;
            }
            if (direction == KeyCode.LEFT.getCode()) {
                character.getAnimation().setOffsetY(height);
                if (isField(KeyCode.LEFT, character)) {
                    character.moveX(-2);
                }
                return;
            }

        }
        if (direction == KeyCode.UP.getCode()) {
            character.getAnimation().setOffsetY(height * 3);
            if (isField(KeyCode.UP, character)) {
                character.moveY(-2);
            }
            return;
        }
        if (direction == KeyCode.DOWN.getCode()) {
            character.getAnimation().setOffsetY(0);
            if (isField(KeyCode.DOWN, character)) {
                character.moveY(2);
            }
        }
    }

    private void sendMoveMessage(Integer keyCode) {
        MoveMessage moveMessage = new MoveMessage(keyCode, playerPosition);
        ApplicationUtil.getApplication().getClientPlayer().send(moveMessage);
    }

    private void sendAddBombMessage(int x, int y) {
        AddBombMessage message = new AddBombMessage(x, y, playerPosition);
        ApplicationUtil.getApplication().getClientPlayer().send(message);
    }

    private void initGameField() {
        blocks = RoomRepository.getGameField(ApplicationUtil.getApplication().getRoom());
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
        code.setText(ApplicationUtil.getApplication().getRoom());
    }

    private void initActions() {
        anchorPane.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.SPACE)) {
                int x = getColumnIndex(player);
                int y = getRowIndex(player);
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

    public void addBomb(int x, int y) {
        ImageView view = new ImageView(bomb);
        view.setFitHeight(blockSize / 1.5);
        view.setFitWidth(blockSize / 1.5);
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

    private int getRowIndex(Character character) {
        return (int) Math.ceil(character.getPlayerTranslateY() / blockSize);
    }

    private int getColumnIndex(Character character) {
        return (int) Math.ceil(character.getPlayerTranslateX() / blockSize);
    }

    private boolean isField(KeyCode code, Character character) {
        int inaccuracy = 6;
        switch (code) {
            case UP -> {
                int row = (int) Math.ceil((character.getPlayerTranslateY() - blockSize) / blockSize);
                return verticalCheck(row, character, inaccuracy);
            }
            case DOWN -> {
                int row = (int) Math.ceil((character.getPlayerTranslateY() + 1) / blockSize);
                return verticalCheck(row, character, inaccuracy);
            }
            case RIGHT -> {
                int column = (int) Math.ceil((character.getPlayerTranslateX() + 1) / blockSize);
                return horizontalCheck(column, character, inaccuracy);
            }
            case LEFT -> {
                int column = (int) Math.ceil((character.getPlayerTranslateX() - blockSize) / blockSize);
                return horizontalCheck(column, character, inaccuracy);            }
        }
        return false;
    }

    private boolean verticalCheck(int row, Character character, int inaccuracy) {
        int column1 = getColumnIndex(character);
        int column2 = (int) Math.ceil((character.getPlayerTranslateX() - blockSize + inaccuracy) / blockSize);
        return blocks[row][column1] != Block.OBSTACLE && blocks[row][column1] != Block.WALL
                && blocks[row][column2] != Block.OBSTACLE && blocks[row][column2] != Block.WALL;

    }

    private boolean horizontalCheck(int column, Character character, int inaccuracy) {
        int row1 = getRowIndex(character);
        int row2 = (int) Math.ceil((character.getPlayerTranslateY() - blockSize + inaccuracy) / blockSize);
        return blocks[row1][column] != Block.OBSTACLE && blocks[row1][column] != Block.WALL
                && blocks[row2][column] != Block.OBSTACLE && blocks[row2][column] != Block.WALL;
    }


    private void addPlayer(int imgNumber, int position) {
        int difference = 4;
        Character character = new Character(CharacterFactory.create(imgNumber), blockSize - difference);
        setCharacterOffset(position, character);
        characters.add(character);
    }

    private void initPlayers() {
        playerPosition = ApplicationUtil.getApplication().getUser().getPosition();
        for (Player p : players) {
            addPlayer(p.character, p.position);
            if (p.position == playerPosition) {
                player = characters.get(playerPosition - 1);
            }
        }
        gridPane.getChildren().addAll(this.characters);
    }

    private void initInfoTable() {
        for (Player p : players) {
            ImageView img = CharacterFactory.create(p.character);
            Character.setSettings(img, blockSize);
            Label divisor = new Label(" ");
            infoBox.getChildren().addAll(p.title, p.username, img, divisor);
        }
    }

    public void removeCharacter(int position) {
        Player p = players.get(position - 1);
        p.isLose = true;
        p.title.setStyle("-fx-text-fill: red;");
        p.username.setStyle("-fx-text-fill: red;");
        gridPane.getChildren().remove(characters.get(p.position - 1));
        if (++losersCount == players.size() - 1) {
            updateTimer.stop();
            gameOver();
        }
    }

    private void gameOver() {
        try {
            ApplicationUtil.loadAndShowFXML("/fxml/game-over-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
