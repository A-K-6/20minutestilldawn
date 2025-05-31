package com.minutestilldawn.game.Model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.minutestilldawn.game.Model.Enemy.EnemyType;
import com.badlogic.gdx.audio.Sound;

public class GameAssetManager extends AssetManager {

    // --- UI Skins ---
    // Choose ONE skin to load. Uncomment the one you want to use.
    // For Pixthulhu (pixel-art):
    public static final String PIXTHULHU_SKIN = "pixthulhu/skin/pixthulhu-ui.json";    // For Terra UI (modern dark):
    // public static final String TERRA_UI_SKIN = "terraui/terra-dark.json";
    // For Flat Earth UI (modern minimalist):
    // public static final String FLAT_EARTH_SKIN = "flatearth/flatearth-ui.json";
    // VisUI is loaded differently, see Main.java
    // public TextureRegion getBulletTexture(String weaponName) { /* logic to return specific bullet texture or default */ return getPixthulhuSkin().getRegion("bullet_default"); }
    // Ensure "bullet_default" or weapon-specific bullet regions exist in your skin/atlas.


    // --- Game Assets (placeholders) ---
    public static final String PLAYER_SPRITE_ATLAS = "sprites/player_atlas.atlas";
    public static final String MENU_BACKGROUND_TEXTURE = "gameAssets/backgrounds/menuBg.png";
    public static final String LOGIN_REGISTER_MENU_BACKGROUND = "gameAssets/backgrounds/LoginBg.png";
    public static final String SHOOT_SOUND = "audio/Arrow Fly 3_3.wav";


    public void loadAssets() {
        // Load the chosen UI Skin
        load(PIXTHULHU_SKIN, Skin.class); // Or TERRA_UI_SKIN, FLAT_EARTH_SKIN

        // Load your game-specific assets
        // load(PLAYER_SPRITE_ATLAS, TextureAtlas.class);
        load(LOGIN_REGISTER_MENU_BACKGROUND, Texture.class);
        load(MENU_BACKGROUND_TEXTURE, Texture.class);
        // load(SHOOT_SOUND, Sound.class);

        // Add more load() calls for all your other game assets here
    }

    /**
     * Gets the loaded Pixthulhu skin.
     * Ensure this skin was loaded in loadAssets() and loading is complete.
     */
    public Skin getPixthulhuSkin() {
        return get(PIXTHULHU_SKIN, Skin.class);
    }

    /*
     * Add getters for other skins if you decide to load them instead:
     * public Skin getTerraUISkin() { return get(TERRA_UI_SKIN, Skin.class); }
     * public Skin getFlatEarthUISkin() { return get(FLAT_EARTH_SKIN, Skin.class); }
     */

    // Example getter for a game asset
    public TextureAtlas getPlayerSpriteAtlas() {
        return get(PLAYER_SPRITE_ATLAS, TextureAtlas.class);
    }

    public TextureRegion getEnemyBulletTexture(EnemyType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemyBulletTexture'");
    }

    public TextureRegion getTreeTexture() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTreeTexture'");
    }

    public TextureRegion getEnemyTexture(EnemyType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnemyTexture'");
    }

    public TextureRegion getBulletTexture(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBulletTexture'");
    }

    // Add more specific getters for other assets as needed
}