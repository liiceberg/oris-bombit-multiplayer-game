package ru.kpfu.itis.oris.gimaletdinova.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ru.kpfu.itis.oris.gimaletdinova.animation.SpriteAnimation;

import javafx.util.Duration;

public class Character extends Pane {
    private final ImageView imageView;
    private static final int COUNT = 4;
    private static final int COLUMNS = 4;
    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;
    private final SpriteAnimation animation;

    public Character(ImageView imageView, double size) {
        this.imageView = imageView;
        setSettings(this.imageView, size);
        animation = new SpriteAnimation(imageView, Duration.millis(200), COUNT, COLUMNS, 0, 0, WIDTH, HEIGHT);
        getChildren().add(imageView);
    }

    public void moveX(int x) {
        boolean right = x > 0;
        for (int i = 0; i < Math.abs(x); i++) {
            if (right) {
                setTranslateX(getTranslateX() + 1);
            } else {
                setTranslateX(getTranslateX() - 1);
            }
        }
    }

    public void moveY(int y) {
        boolean down = y > 0;
        for (int i = 0; i < Math.abs(y); i++) {
            if (down) {
                setTranslateY(getTranslateY() + 1);
            } else {
                setTranslateY(getTranslateY() - 1);
            }
        }
    }

    public static void setSettings(ImageView imageView, double size) {
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
    }

    public SpriteAnimation getAnimation() {
        return animation;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }

    public double getPlayerTranslateX() {
        return getTranslateX();
    }

    public double getPlayerTranslateY() {
        return getTranslateY();
    }

    public ImageView getImageView() {
        return imageView;
    }
}
