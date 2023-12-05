package ru.kpfu.itis.oris.gimaletdinova.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameFieldRepository {
    private static final Random random = new Random();
    private static final int OBSTACLES_COUNT = 78;
    public static final int WIDTH = 17;
    public static final int HEIGHT = 15;
    private static final Block[][] BLOCKS = new Block[HEIGHT][WIDTH];
    public static Block[][] getGameField() {
        for (int i = 0; i < WIDTH; i ++) {
            BLOCKS[0][i] = Block.WALL;
            BLOCKS[HEIGHT - 1][i] = Block.WALL;
        }
        for (int i = 1; i < HEIGHT - 1; i ++) {
            BLOCKS[i][0] = Block.WALL;
            BLOCKS[i][WIDTH - 1] = Block.WALL;
        }
        switch (random.nextInt(3)) {
            case 0 -> init1();
            case 1 -> init2();
            case 2 -> init3();
        }
        generateObstacles();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (BLOCKS[i][j] == null) {
                    BLOCKS[i][j] = Block.FIELD;
                }
            }
        }
        return BLOCKS;
    }

    private static void init1() {
        BLOCKS[1][8] = Block.WALL;
        BLOCKS[2][2] = Block.WALL;
        BLOCKS[2][3] = Block.WALL;
        BLOCKS[2][4] = Block.WALL;
        BLOCKS[2][6] = Block.WALL;
        BLOCKS[2][8] = Block.WALL;
        BLOCKS[2][10] = Block.WALL;
        BLOCKS[2][11] = Block.WALL;
        BLOCKS[2][12] = Block.WALL;
        BLOCKS[2][13] = Block.WALL;
        BLOCKS[2][14] = Block.WALL;
        BLOCKS[3][4] = Block.WALL;
        BLOCKS[3][5] = Block.WALL;
        BLOCKS[3][6] = Block.WALL;
        BLOCKS[3][10] = Block.WALL;
        BLOCKS[3][11] = Block.WALL;
        BLOCKS[3][12] = Block.WALL;
        BLOCKS[4][1] = Block.WALL;
        BLOCKS[4][2] = Block.WALL;
        BLOCKS[4][4] = Block.WALL;
        BLOCKS[4][5] = Block.WALL;
        BLOCKS[4][6] = Block.WALL;
        BLOCKS[4][7] = Block.WALL;
        BLOCKS[4][9] = Block.WALL;
        BLOCKS[4][10] = Block.WALL;
        BLOCKS[4][12] = Block.WALL;
        BLOCKS[4][14] = Block.WALL;
        BLOCKS[4][15] = Block.WALL;
        BLOCKS[6][2] = Block.WALL;
        BLOCKS[6][3] = Block.WALL;
        BLOCKS[6][5] = Block.WALL;
        BLOCKS[6][11] = Block.WALL;
        BLOCKS[6][13] = Block.WALL;
        BLOCKS[6][14] = Block.WALL;
        BLOCKS[6][15] = Block.WALL;
        BLOCKS[7][4] = Block.WALL;
        BLOCKS[7][5] = Block.WALL;
        BLOCKS[7][6] = Block.WALL;
        BLOCKS[7][11] = Block.WALL;
        BLOCKS[7][13] = Block.WALL;
        BLOCKS[7][14] = Block.WALL;
        BLOCKS[7][15] = Block.WALL;
        BLOCKS[8][2] = Block.WALL;
        BLOCKS[8][3] = Block.WALL;
        BLOCKS[8][4] = Block.WALL;
        BLOCKS[8][5] = Block.WALL;
        BLOCKS[8][11] = Block.WALL;
        BLOCKS[8][13] = Block.WALL;
        BLOCKS[8][14] = Block.WALL;
        BLOCKS[8][15] = Block.WALL;
        BLOCKS[10][1] = Block.WALL;
        BLOCKS[10][2] = Block.WALL;
        BLOCKS[10][4] = Block.WALL;
        BLOCKS[10][6] = Block.WALL;
        BLOCKS[10][7] = Block.WALL;
        BLOCKS[10][9] = Block.WALL;
        BLOCKS[10][10] = Block.WALL;
        BLOCKS[10][12] = Block.WALL;
        BLOCKS[10][14] = Block.WALL;
        BLOCKS[10][15] = Block.WALL;
        BLOCKS[11][4] = Block.WALL;
        BLOCKS[11][6] = Block.WALL;
        BLOCKS[11][8] = Block.WALL;
        BLOCKS[11][9] = Block.WALL;
        BLOCKS[11][10] = Block.WALL;
        BLOCKS[11][12] = Block.WALL;
        BLOCKS[12][2] = Block.WALL;
        BLOCKS[12][3] = Block.WALL;
        BLOCKS[12][4] = Block.WALL;
        BLOCKS[12][6] = Block.WALL;
        BLOCKS[12][8] = Block.WALL;
        BLOCKS[12][10] = Block.WALL;
        BLOCKS[12][12] = Block.WALL;
        BLOCKS[12][13] = Block.WALL;
        BLOCKS[12][14] = Block.WALL;
        BLOCKS[13][8] = Block.WALL;

        BLOCKS[6][7] = Block.WALL;
        BLOCKS[7][7] = Block.WALL;
        BLOCKS[8][7] = Block.WALL;
        BLOCKS[6][9] = Block.WALL;
        BLOCKS[7][9] = Block.WALL;
        BLOCKS[8][9] = Block.WALL;
        BLOCKS[6][8] = Block.WALL;
        BLOCKS[8][8] = Block.WALL;
        BLOCKS[7][8] = Block.CENTRAL;
    }

    private static void init2() {
        BLOCKS[1][6] = Block.WALL;
        BLOCKS[1][7] = Block.WALL;
        BLOCKS[1][9] = Block.WALL;
        BLOCKS[1][10] = Block.WALL;
        BLOCKS[2][2] = Block.WALL;
        BLOCKS[2][3] = Block.WALL;
        BLOCKS[2][4] = Block.WALL;
        BLOCKS[2][6] = Block.WALL;
        BLOCKS[2][7] = Block.WALL;
        BLOCKS[2][9] = Block.WALL;
        BLOCKS[2][10] = Block.WALL;
        BLOCKS[2][12] = Block.WALL;
        BLOCKS[2][13] = Block.WALL;
        BLOCKS[2][14] = Block.WALL;
        BLOCKS[3][2] = Block.WALL;
        BLOCKS[3][3] = Block.WALL;
        BLOCKS[3][13] = Block.WALL;
        BLOCKS[3][14] = Block.WALL;
        BLOCKS[4][5] = Block.WALL;
        BLOCKS[4][6] = Block.WALL;
        BLOCKS[4][7] = Block.WALL;
        BLOCKS[4][9] = Block.WALL;
        BLOCKS[4][10] = Block.WALL;
        BLOCKS[4][11] = Block.WALL;
        BLOCKS[5][2] = Block.WALL;
        BLOCKS[5][3] = Block.WALL;
        BLOCKS[5][5] = Block.WALL;
        BLOCKS[5][11] = Block.WALL;
        BLOCKS[5][13] = Block.WALL;
        BLOCKS[5][14] = Block.WALL;
        BLOCKS[6][2] = Block.WALL;
        BLOCKS[6][3] = Block.WALL;
        BLOCKS[6][5] = Block.WALL;
        BLOCKS[6][11] = Block.WALL;
        BLOCKS[6][13] = Block.WALL;
        BLOCKS[6][14] = Block.WALL;
        BLOCKS[8][2] = Block.WALL;
        BLOCKS[8][3] = Block.WALL;
        BLOCKS[8][5] = Block.WALL;
        BLOCKS[8][11] = Block.WALL;
        BLOCKS[8][13] = Block.WALL;
        BLOCKS[8][14] = Block.WALL;
        BLOCKS[9][2] = Block.WALL;
        BLOCKS[9][3] = Block.WALL;
        BLOCKS[9][5] = Block.WALL;
        BLOCKS[9][11] = Block.WALL;
        BLOCKS[9][13] = Block.WALL;
        BLOCKS[9][14] = Block.WALL;
        BLOCKS[10][5] = Block.WALL;
        BLOCKS[10][6] = Block.WALL;
        BLOCKS[10][7] = Block.WALL;
        BLOCKS[10][9] = Block.WALL;
        BLOCKS[10][10] = Block.WALL;
        BLOCKS[10][11] = Block.WALL;
        BLOCKS[11][2] = Block.WALL;
        BLOCKS[11][3] = Block.WALL;
        BLOCKS[11][13] = Block.WALL;
        BLOCKS[11][14] = Block.WALL;
        BLOCKS[12][2] = Block.WALL;
        BLOCKS[12][3] = Block.WALL;
        BLOCKS[12][4] = Block.WALL;
        BLOCKS[12][6] = Block.WALL;
        BLOCKS[12][7] = Block.WALL;
        BLOCKS[12][9] = Block.WALL;
        BLOCKS[12][10] = Block.WALL;
        BLOCKS[12][12] = Block.WALL;
        BLOCKS[12][13] = Block.WALL;
        BLOCKS[12][14] = Block.WALL;
        BLOCKS[13][6] = Block.WALL;
        BLOCKS[13][7] = Block.WALL;
        BLOCKS[13][9] = Block.WALL;
        BLOCKS[13][10] = Block.WALL;

        BLOCKS[6][7] = Block.WALL;
        BLOCKS[7][7] = Block.WALL;
        BLOCKS[8][7] = Block.WALL;
        BLOCKS[6][9] = Block.WALL;
        BLOCKS[7][9] = Block.WALL;
        BLOCKS[8][9] = Block.WALL;
        BLOCKS[6][8] = Block.WALL;
        BLOCKS[8][8] = Block.WALL;
        BLOCKS[7][8] = Block.CENTRAL;
    }

    private static void init3() {
        BLOCKS[1][7] = Block.WALL;
        BLOCKS[1][8] = Block.WALL;
        BLOCKS[1][9] = Block.WALL;
        BLOCKS[2][2] = Block.WALL;
        BLOCKS[2][3] = Block.WALL;
        BLOCKS[2][4] = Block.WALL;
        BLOCKS[2][5] = Block.WALL;
        BLOCKS[2][11] = Block.WALL;
        BLOCKS[2][12] = Block.WALL;
        BLOCKS[2][13] = Block.WALL;
        BLOCKS[2][14] = Block.WALL;
        BLOCKS[3][6] = Block.WALL;
        BLOCKS[3][8] = Block.WALL;
        BLOCKS[3][10] = Block.WALL;
        BLOCKS[4][2] = Block.WALL;
        BLOCKS[4][3] = Block.WALL;
        BLOCKS[4][4] = Block.WALL;
        BLOCKS[4][8] = Block.WALL;
        BLOCKS[4][12] = Block.WALL;
        BLOCKS[4][13] = Block.WALL;
        BLOCKS[4][14] = Block.WALL;
        BLOCKS[5][4] = Block.WALL;
        BLOCKS[5][6] = Block.WALL;
        BLOCKS[5][10] = Block.WALL;
        BLOCKS[5][12] = Block.WALL;
        BLOCKS[6][2] = Block.WALL;
        BLOCKS[6][14] = Block.WALL;
        BLOCKS[7][1] = Block.WALL;
        BLOCKS[7][2] = Block.WALL;
        BLOCKS[7][4] = Block.WALL;
        BLOCKS[7][5] = Block.WALL;
        BLOCKS[7][11] = Block.WALL;
        BLOCKS[7][12] = Block.WALL;
        BLOCKS[7][14] = Block.WALL;
        BLOCKS[7][15] = Block.WALL;
        BLOCKS[8][2] = Block.WALL;
        BLOCKS[8][14] = Block.WALL;
        BLOCKS[9][4] = Block.WALL;
        BLOCKS[9][6] = Block.WALL;
        BLOCKS[9][10] = Block.WALL;
        BLOCKS[9][12] = Block.WALL;
        BLOCKS[10][2] = Block.WALL;
        BLOCKS[10][3] = Block.WALL;
        BLOCKS[10][4] = Block.WALL;
        BLOCKS[10][8] = Block.WALL;
        BLOCKS[10][12] = Block.WALL;
        BLOCKS[10][13] = Block.WALL;
        BLOCKS[10][14] = Block.WALL;
        BLOCKS[11][6] = Block.WALL;
        BLOCKS[11][8] = Block.WALL;
        BLOCKS[11][10] = Block.WALL;
        BLOCKS[12][5] = Block.WALL;
        BLOCKS[12][2] = Block.WALL;
        BLOCKS[12][3] = Block.WALL;
        BLOCKS[12][4] = Block.WALL;
        BLOCKS[12][11] = Block.WALL;
        BLOCKS[12][12] = Block.WALL;
        BLOCKS[12][13] = Block.WALL;
        BLOCKS[12][14] = Block.WALL;
        BLOCKS[13][7] = Block.WALL;
        BLOCKS[13][8] = Block.WALL;
        BLOCKS[13][9] = Block.WALL;

        BLOCKS[6][7] = Block.WALL;
        BLOCKS[7][7] = Block.WALL;
        BLOCKS[8][7] = Block.WALL;
        BLOCKS[6][9] = Block.WALL;
        BLOCKS[7][9] = Block.WALL;
        BLOCKS[8][9] = Block.WALL;
        BLOCKS[6][8] = Block.WALL;
        BLOCKS[8][8] = Block.WALL;
        BLOCKS[7][8] = Block.CENTRAL;
    }

    private static void generateObstacles() {
        int count = 0;
        Set<String> set = new HashSet<>();
        set.add("1 1");
        set.add("1 2");
        set.add("2 1");
        set.add("1 " + (WIDTH - 2));
        set.add("2 " + (WIDTH - 2));
        set.add("1 " + (WIDTH - 3));
        set.add((HEIGHT - 2) + " 1");
        set.add((HEIGHT - 3) + " 1");
        set.add((HEIGHT - 2) + " 2");
        set.add((HEIGHT - 2) + " " + (WIDTH - 2));
        set.add((HEIGHT - 2) + " " + (WIDTH - 3));
        set.add((HEIGHT - 3) + " " + (WIDTH - 2));

        while (count != OBSTACLES_COUNT) {
            int i = random.nextInt(1, HEIGHT);
            int j = random.nextInt(1, WIDTH);
            if (BLOCKS[i][j] != Block.WALL && BLOCKS[i][j] != Block.CENTRAL && !set.contains(i + " " + j)) {
                BLOCKS[i][j] = Block.OBSTACLE;
                count++;
            }
        }
    }
}
