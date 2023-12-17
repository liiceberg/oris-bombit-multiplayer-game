package ru.kpfu.itis.oris.gimaletdinova.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.kpfu.itis.oris.gimaletdinova.model.Block;
import ru.kpfu.itis.oris.gimaletdinova.model.Mode;

import java.util.Objects;
import java.util.Random;

public class BlockBuilder {
    private final Random random = new Random();
    private final double BLOCK_SIZE;
    private final Image fire;
    private final Image wall;
    private final Image field;
    private final Image central;
    private final Image obstacle;
    private final Image[] firstModeObstacles;
    private final Image[] secondModeObstacles;
    private final Image[] thirdModeObstacles;

    public BlockBuilder(double blockSize) {
        BLOCK_SIZE = blockSize;
        field = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/grass.png")));
        wall = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/stone.jpeg")));
        fire = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fire.jpg")));
        central = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/creeper.png")));
        obstacle = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/dirt.jpg")));

        Image obstacleBlueWool = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/blue_wool.jpg")));
        Image obstacleOrangeWool = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/orange_wool.png")));
        Image obstaclePinkWool = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/pink_wool.png")));
        Image obstacleGlass = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/glass.png")));
        Image obstacleIce = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/ice.png")));
        Image obstacleSand = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/sand.png")));
        Image obstacleBook = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/book.jpg")));
        Image obstacleWood = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/wood.jpg")));
        Image obstacleWood2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/obstacles/wood_2.jpg")));

        firstModeObstacles = new Image[]{obstacleBook, obstacleWood, obstacleWood2};
        secondModeObstacles = new Image[]{obstacleIce, obstacleSand, obstacleGlass};
        thirdModeObstacles = new Image[]{obstaclePinkWool, obstacleOrangeWool, obstacleBlueWool};
    }

    public ImageView getView(Block block, Mode mode) {
        if (block == Block.OBSTACLE) {
            ImageView view = new ImageView();
            int i = random.nextInt(3);
            switch (mode) {
                case FIRST -> view.setImage(firstModeObstacles[i]);
                case SECOND -> view.setImage(secondModeObstacles[i]);
                case THIRD -> view.setImage(thirdModeObstacles[i]);
            }
            view.setFitWidth(BLOCK_SIZE);
            view.setFitHeight(BLOCK_SIZE);
            return view;
        }
        return getView(block);
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
