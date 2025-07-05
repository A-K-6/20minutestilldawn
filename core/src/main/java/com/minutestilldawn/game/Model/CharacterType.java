package com.minutestilldawn.game.Model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public enum CharacterType {
    SHANA(4, 4, "Shana"), // Speed 4, HP 4
    DIAMOND(7, 1, "Diamond"), // Speed 7, HP 1
    SCARLET(3, 5, "Scarlet"), // Speed 3, HP 5
    LILITH(5, 3, "Lilith"), // Speed 5, HP 3
    DASHER(2, 10, "Dasher"); // Speed 2, HP 10

    private final float baseSpeed;
    private final int baseHp;
    private final String assetFolderName;
    private float hitboxWidth = 28f, hitboxHeight = 40f; // Example dimensions

    CharacterType(float baseSpeed, int baseHp, String assetFolderName) {
        this.baseSpeed = baseSpeed;
        this.baseHp = baseHp;
        this.assetFolderName = assetFolderName;
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

    public String getAssetFolderName() {
        return assetFolderName;
    }

    /**
     * Returns the list of run animation frame paths for this character.
     * Assumes frames are named Run_0.png, Run_1.png, ... in the character's folder.
     */
    public String[] getRunTexturePaths() {
        // Most characters have 4 run frames: Run_0.png to Run_3.png
        int numFrames = 4;
        String[] hashes = new String[numFrames];
        switch (this) {
            case SHANA:
                hashes = new String[]{"8762","8778","8286","8349"};
                break;
            case DIAMOND:
                hashes = new String[]{"8760","8776","8284","8347"};
                break;
            case SCARLET:
                hashes = new String[]{"8759","8775","8283","8346"};
                break;
            case LILITH:
                hashes = new String[]{"8765","8781","8289","8352"};
                break;
            case DASHER:
                hashes = new String[]{"8757","8773","8281","8344"};
                break;
            // If you add characters with different frame counts, handle here
        }
        String[] paths = new String[numFrames];
        for (int i = 0; i < numFrames; i++) {
            paths[i] = "pictures/Heros/" + assetFolderName + "/run/Run_" + i + " #" + hashes[i] + ".png";
        }
        return paths;
    }
}
