package ru.kpfu.itis.oris.gimaletdinova.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.kpfu.itis.oris.gimaletdinova.GameApplication;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class CharacterFactory {
    private static final Set<Integer> characters = new HashSet<>();
    private static final Random random = new Random();

    public static ImageView create(int number) {
        return new ImageView(new Image(Objects.requireNonNull(GameApplication.class.getResourceAsStream(String.format("/images/characters/Character_00%d.png", number)))));
    }

    public static int getNumber() {
        int number;
        do {
            number = random.nextInt(1, 8);
        } while (characters.contains(number));
        characters.add(number);
        return number;
    }
    public static void remove(Integer i) {
        characters.remove(i);
    }
}
