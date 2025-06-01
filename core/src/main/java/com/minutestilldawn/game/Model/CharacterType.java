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

    public Rectangle getHitbox(Vector2 centerPos) {
        return new Rectangle(centerPos.x - hitboxWidth / 2, centerPos.y -
                hitboxHeight / 2, hitboxWidth, hitboxHeight);
    }

    /**
     * Returns the file path for the idle animation texture of this character type.
     * @return The string path to the idle texture.
     */
    public String getIdleTexturePath() {
        return GameAssetManager.CHARACTER_BASE_PATH + this.name().toLowerCase() + "/idle.png";
    }

    /**
     * Returns an array of file paths for the running animation textures of this character type.
     * Assumes 4 frames for the running animation (run_1.png to run_4.png).
     * @return An array of string paths to the running textures.
     */
    public String[] getRunTexturePaths() {
        String base = GameAssetManager.CHARACTER_BASE_PATH + this.name().toLowerCase() + "/Run_";
        return new String[] { base + "1.png", base + "2.png", base + "3.png", base + "4.png" };
    }
}
