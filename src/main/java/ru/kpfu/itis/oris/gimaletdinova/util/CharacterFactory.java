package ru.kpfu.itis.oris.gimaletdinova.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class CharacterFactory {
    private final Set<Integer> characters = new HashSet<>();
    private final Random random = new Random();

    public ImageView create() {
        int number;
        do {
            number = random.nextInt(1, 8);
        } while (characters.contains(number));
        characters.add(number);
        return new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(String.format("/images/characters/Character_00%d.png", number)))));
    }
}
