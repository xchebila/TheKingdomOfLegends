package com.tkol.game.game;

public class BossProgressManager {
    private static int defeatCount = 0;

    public static int getDefeatCount() {
        return defeatCount;
    }

    public static void incrementDefeatCount() {
        defeatCount++;
    }

    public static void resetDefeatCount() {
        defeatCount = 0;
    }
}

