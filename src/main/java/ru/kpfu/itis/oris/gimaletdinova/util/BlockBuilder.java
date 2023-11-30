package ru.kpfu.itis.oris.gimaletdinova.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class BlockBuilder {
    private final double BLOCK_SIZE;
    private final Image fire;
    private final Image wall;
    private final Image field;
    private final Image obstacle;
    private final Image central;

    public BlockBuilder(double blockSize) {
        BLOCK_SIZE = blockSize;
        field = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grass.png")));
        wall = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/stone.jpeg")));
        fire = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fire.jpg")));
        obstacle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/dirt.jpg")));
        central = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/creeper.png")));
    }

    public ImageView getView(Block block) {
        ImageView view = new ImageView();
        switch (block) {
            case WALL -> view.setImage(wall);
            case FIRE -> view.setImage(fire);
            case FIELD -> view.setImage(field);
            case OBSTACLE -> view.setImage(obstacle);
            case CENTRAL -> view.setImage(central);
        }
        view.setFitWidth(BLOCK_SIZE);
        view.setFitHeight(BLOCK_SIZE);
        return view;
    }
}
