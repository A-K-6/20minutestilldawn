package com.minutestilldawn.game.Model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public enum CharacterType {
    SHANA(4, 4), // Speed 4, HP 4
    DIAMOND(7, 1), // Speed 7, HP 1
    SCARLET(3, 5), // Speed 3, HP 5
    LILITH(5, 3), // Speed 5, HP 3
    DASHER(2, 10); // Speed 2, HP 10

    private final float baseSpeed;
    private final int baseHp;

    private float hitboxWidth = 28f, hitboxHeight = 40f; // Example dimensions

    public Rectangle getHitbox(Vector2 centerPos) {
        return new Rectangle(centerPos.x - hitboxWidth / 2, centerPos.y -
                hitboxHeight / 2, hitboxWidth, hitboxHeight);
    }

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

    String getIdleFrameName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdleFrameName'");
    }
}