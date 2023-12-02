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
import ru.kpfu.itis.oris.gimaletdinova.util.*;
import ru.kpfu.itis.oris.gimaletdinova.view.Character;

import java.util.Objects;

public class GameController {
    private boolean isPlayersReady = false;
    public Label code;
    private Character player;
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
    private final CharacterFactory characterFactory = new CharacterFactory();

    @FXML
    public void initialize() {
        Platform.runLater(this::initAll);
    }
    private void initAll() {
        initRoom();
        initGameField();
        initActions();
        initPlayAttributes();
        gridPane.getChildren().add(player);
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
                }
            }
            case DOWN -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(0);
                if (isField(KeyCode.DOWN)) {
                    player.moveY(2);
                }
            }
            case RIGHT -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height * 2);
                if (isField(KeyCode.RIGHT)) {
                    player.moveX(2);
                }
            }
            case LEFT -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height);
                if (isField(KeyCode.LEFT)) {
                    player.moveX(-2);
                }
            }
        }
    }

    private void initGameField() {
        String room = ControllerHelper.getApplication().getRoom();
        System.out.println(room);
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
        player = new Character(characterFactory.create(), blockSize);
        player.moveX((int) blockSize);
        player.moveY((int) blockSize);
        height = player.getHEIGHT();
    }

    private void initRoom() {
        code.setText(ControllerHelper.getApplication().getRoom());
    }

    private void initActions() {
        anchorPane.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.SPACE)) {
                addBomb();
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

    private void addBomb() {
        ImageView view = new ImageView(bomb);
        view.setFitHeight(blockSize / 1.5);
        view.setFitWidth(blockSize / 1.5);
        int column = getColumnIndex();
        int row = getRowIndex();
        System.out.println(row + " " + column);
        System.out.println(player.getPlayerTranslateY() + " " + player.getPlayerTranslateX());
        System.out.println();
        gridPane.add(view, column, row);
        PauseTransition pause = new PauseTransition(Duration.millis(3_000));
        pause.setOnFinished(event -> {
            gridPane.getChildren().remove(view);
            for (int i = row - 1; i < row + 2; i++) {
                explode(column, i);
            }
            for (int i = column - 1; i < column + 2; i++) {
                explode(i, row);
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
//              TODO all players
                    player.toFront();
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
}
