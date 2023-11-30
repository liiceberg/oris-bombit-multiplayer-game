package ru.kpfu.itis.oris.gimaletdinova.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import ru.kpfu.itis.oris.gimaletdinova.util.Block;
import ru.kpfu.itis.oris.gimaletdinova.util.BlockBuilder;
import ru.kpfu.itis.oris.gimaletdinova.util.GameFieldRepository;
import ru.kpfu.itis.oris.gimaletdinova.view.Character;

import java.util.Objects;

public class GameController {
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

    @FXML
    public void initialize() {
        initPlayAttributes(1);
        initGameField();
        gridPane.getChildren().add(player);
        Platform.runLater(this::initActions);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updatePlayer();
            }
        };
        timer.start();
    }

    public void updatePlayer() {
        if (keyCode == null) {
            player.getAnimation().stop();
        } else switch (keyCode) {
            case UP -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height * 3);
                player.moveY(-2);
            }
            case DOWN -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(0);
                player.moveY(2);
            }
            case RIGHT -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height * 2);
                player.moveX(2);
            }
            case LEFT -> {
                player.getAnimation().play();
                player.getAnimation().setOffsetY(height);
                player.moveX(-2);
            }
        }
    }

    private void initGameField() {
        blocks = GameFieldRepository.getGameField();
        blockSize = gridPane.getPrefHeight() / gridPane.getRowCount();
        blockBuilder = new BlockBuilder(blockSize);

        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                gridPane.add(blockBuilder.getView(blocks[i][j]), j, i);
            }
        }
    }

    private void initPlayAttributes(int number) {
        ImageView playerView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(String.format("/images/characters/Character_00%d.png", number)))));
        bomb = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/tnt.png")));
        player = new Character(playerView);
        height = player.getHEIGHT();
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
        gridPane.add(view, column, row);
        PauseTransition pause = new PauseTransition(Duration.millis(3_000));
        pause.setOnFinished(event -> {
            gridPane.getChildren().remove(view);
            for (int i = row - 1; i < row + 2; i++) {
                if (blocks[i][column] != Block.WALL) {
                    explode(column, i);
                }
            }
            for (int i = column - 1; i < column + 2; i++) {
                if (blocks[row][i] != Block.WALL) {
                    explode(i, row);
                }
            }
        });
        pause.play();
    }
    private void explode(int i, int j) {
        ImageView fire = blockBuilder.getView(Block.FIRE);
        gridPane.add(fire, i, j);
        PauseTransition pause = new PauseTransition(Duration.millis(1_000));
        pause.setOnFinished(event -> {
            gridPane.getChildren().remove(fire);
            if (blocks[j][i] == Block.OBSTACLE) {
                ImageView field = blockBuilder.getView(Block.FIELD);
                gridPane.add(field, i, j);
//              TODO all players
                player.toFront();
            }
        });
        pause.play();
    }

    private int getRowIndex() {
        return (int) Math.ceil(player.getPlayerTranslateY() / blockSize);
    }

    private int getColumnIndex() {
        return (int) Math.ceil(player.getPlayerTranslateX() / blockSize);
    }
}
