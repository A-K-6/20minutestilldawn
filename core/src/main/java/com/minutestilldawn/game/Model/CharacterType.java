package com.minutestilldawn.game.Model;

public enum CharacterType {
    SHANA(4, 4), // Speed 4, HP 4
    DIAMOND(7, 1), // Speed 7, HP 1
    SCARLET(3, 5), // Speed 3, HP 5
    LILITH(5, 3), // Speed 5, HP 3
    DASHER(2, 10); // Speed 2, HP 10

    private final float baseSpeed;
    private final int baseHp;

    CharacterType(float baseSpeed, int baseHp) {
        this.baseSpeed = baseSpeed;
        this.baseHp = baseHp;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public int getBaseHp() {
        return baseHp;
    }
}