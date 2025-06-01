package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.minutestilldawn.game.Model.Enemy.EnemyType;

public class GameAssetManager extends AssetManager {

    // --- UI Skins ---
    // Choose ONE skin to load. Uncomment the one you want to use.
    // For Pixthulhu (pixel-art):
    public static final String PIXTHULHU_SKIN = "pixthulhu/skin/pixthulhu-ui.json"; // For Terra UI (modern dark):
    // public static final String TERRA_UI_SKIN = "terraui/terra-dark.json";
    // For Flat Earth UI (modern minimalist):
    // public static final String FLAT_EARTH_SKIN = "flatearth/flatearth-ui.json";
    // VisUI is loaded differently, see Main.java
    // public TextureRegion getBulletTexture(String weaponName) { /* logic to return
    // specific bullet texture or default */ return
    // getPixthulhuSkin().getRegion("bullet_default"); }
    // Ensure "bullet_default" or weapon-specific bullet regions exist in your
    // skin/atlas.

    // --- Game Assets (placeholders) ---
    public static final String PLAYER_SPRITE_ATLAS = "sprites/player_atlas.atlas";
    public static final String MENU_BACKGROUND_TEXTURE = "gameAssets/backgrounds/menuBg.png";
    public static final String LOGIN_REGISTER_MENU_BACKGROUND = "gameAssets/backgrounds/LoginBg.png";
    public static final String Default_Avatar = "avatars/default_avatar.png";
    public static final String Avatar1 = "avatars/avatar1.png";
    public static final String Avatar2 = "avatars/avatar2.png";
    public static final String Avatar3 = "avatars/avatar3.png";
    public static final String Avatar4 = "avatars/avatar4.png";
    public static final String Avatar5 = "avatars/avatar5.png";
    public static final String SHOOT_SOUND = "audio/Arrow Fly 3_3.wav";
    public static final String DEFAULT_BULLET_TEXTURE_REGION_NAME = "bullet_default";
    public static final String DEFAULT_ENEMY_TEXTURE_REGION_NAME = "enemy_default";
    public static final String DEFAULT_TREE_TEXTURE_REGION_NAME = "tree_default";
    // Base path for characters
    public static final String CHARACTER_BASE_PATH = "Heros/";
    public static final String Shana_IDLE = CHARACTER_BASE_PATH + "Shana/Idle.png";
    public static final String Shana_RUN_0 = CHARACTER_BASE_PATH + "Shana/Run_0.png";
    public static final String Shana_RUN_1 = CHARACTER_BASE_PATH + "Shana/Run_1.png";
    public static final String Shana_RUN_2 = CHARACTER_BASE_PATH + "Shana/Run_2.png";
    public static final String Shana_RUN_3 = CHARACTER_BASE_PATH + "Shana/Run_3.png";

    public static final String DIAMOND_IDLE = CHARACTER_BASE_PATH + "Diamond/Idle.png";
    public static final String DIAMOND_RUN_0 = CHARACTER_BASE_PATH + "Diamond/Run_0.png";
    public static final String DIAMOND_RUN_1 = CHARACTER_BASE_PATH + "Diamond/Run_1.png";
    public static final String DIAMOND_RUN_2 = CHARACTER_BASE_PATH + "Diamond/Run_2.png";
    public static final String DIAMOND_RUN_3 = CHARACTER_BASE_PATH + "Diamond/Run_3.png";

    // Scarlet
    public static final String SCARLET_IDLE = CHARACTER_BASE_PATH + "Scarlet/Idle.png";
    public static final String SCARLET_RUN_0 = CHARACTER_BASE_PATH + "Scarlet/Run_0.png";
    public static final String SCARLET_RUN_1 = CHARACTER_BASE_PATH + "Scarlet/Run_1.png";
    public static final String SCARLET_RUN_2 = CHARACTER_BASE_PATH + "Scarlet/Run_2.png";
    public static final String SCARLET_RUN_3 = CHARACTER_BASE_PATH + "Scarlet/Run_3.png";

    // Lilith
    public static final String LILITH_IDLE = CHARACTER_BASE_PATH + "Lilith/Idle.png";
    public static final String LILITH_RUN_0 = CHARACTER_BASE_PATH + "Lilith/Run_0.png";
    public static final String LILITH_RUN_1 = CHARACTER_BASE_PATH + "Lilith/Run_1.png";
    public static final String LILITH_RUN_2 = CHARACTER_BASE_PATH + "Lilith/Run_2.png";
    public static final String LILITH_RUN_3 = CHARACTER_BASE_PATH + "Lilith/Run_3.png";

    // Dasher
    public static final String DASHER_IDLE = CHARACTER_BASE_PATH + "Dasher/Idle.png";
    public static final String DASHER_RUN_0 = CHARACTER_BASE_PATH + "Dasher/Run_0.png";
    public static final String DASHER_RUN_1 = CHARACTER_BASE_PATH + "Dasher/Run_1.png";
    public static final String DASHER_RUN_2 = CHARACTER_BASE_PATH + "Dasher/Run_2.png";
    public static final String DASHER_RUN_3 = CHARACTER_BASE_PATH + "Dasher/Run_3.png";

    // --- Enemy Textures (Placeholders) ---
    public static final String TENTACLE_MONSTER_TEXTURE = "gameAssets/enemies/tentacle_monster.png"; // Assuming a path
                                                                                                     // like this
    public static final String EYEBAT_TEXTURE = "gameAssets/enemies/eyebat.png";
    public static final String ELDER_BOSS_TEXTURE = "gameAssets/enemies/elder_boss.png";
    // --- Bullet Textures (Placeholders) ---
    public static final String PLAYER_BULLET_TEXTURE = "gameAssets/bullets/player_bullet.png";
    public static final String ENEMY_BULLET_TEXTURE = "gameAssets/bullets/enemy_bullet.png";

    // --- Obstacle Textures (Placeholders) ---
    public static final String TREE_TEXTURE = "gameAssets/obstacles/tree.png";

    public void loadAssets() {
        // Load the chosen UI Skin
        load(PIXTHULHU_SKIN, Skin.class); // Or TERRA_UI_SKIN, FLAT_EARTH_SKIN
        load(Default_Avatar, Texture.class); // Load a default
        load(Avatar1, Texture.class); // Load a default
        load(Avatar2, Texture.class); // Load a default
        load(Avatar3, Texture.class); // Load a default
        load(Avatar4, Texture.class); // Load a default
        load(Avatar5, Texture.class); // Load a default

        // Load your game-specific assets
        // load(PLAYER_SPRITE_ATLAS, TextureAtlas.class);
        load(LOGIN_REGISTER_MENU_BACKGROUND, Texture.class);
        load(MENU_BACKGROUND_TEXTURE, Texture.class);
        // load(SHOOT_SOUND, Sound.class);

        // Load character textures
        load(Shana_IDLE, Texture.class);
        load(Shana_RUN_0, Texture.class);
        load(Shana_RUN_1, Texture.class);
        load(Shana_RUN_2, Texture.class);
        load(Shana_RUN_3, Texture.class);

        load(DIAMOND_IDLE, Texture.class);
        load(DIAMOND_RUN_0, Texture.class);
        load(DIAMOND_RUN_1, Texture.class);
        load(DIAMOND_RUN_2, Texture.class);
        load(DIAMOND_RUN_3, Texture.class);

        load(SCARLET_IDLE, Texture.class);
        load(SCARLET_RUN_0, Texture.class);
        load(SCARLET_RUN_1, Texture.class);
        load(SCARLET_RUN_2, Texture.class);
        load(SCARLET_RUN_3, Texture.class);

        load(LILITH_IDLE, Texture.class);
        load(LILITH_RUN_0, Texture.class);
        load(LILITH_RUN_1, Texture.class);
        load(LILITH_RUN_2, Texture.class);
        load(LILITH_RUN_3, Texture.class);

        load(DASHER_IDLE, Texture.class);
        load(DASHER_RUN_0, Texture.class);
        load(DASHER_RUN_1, Texture.class);
        load(DASHER_RUN_2, Texture.class);
        load(DASHER_RUN_3, Texture.class);

        // Load enemy textures (placeholders)
        load(TENTACLE_MONSTER_TEXTURE, Texture.class);
        load(EYEBAT_TEXTURE, Texture.class);
        load(ELDER_BOSS_TEXTURE, Texture.class);

        Gdx.app.log("GameAssetManager", "Asset loading queued.");
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
    /**
     * Retrieves a specific character animation frame.
     * 
     * @param type           The CharacterType (e.g., SHANA).
     * @param animationState The state of the animation ("idle", "run", etc.).
     * @param frameIndex     The index of the frame (for "run", 1-4; for "idle", 0
     *                       or any single index).
     * @return The TextureRegion for the requested animation frame.
     */
    public TextureRegion getCharacterTexture(CharacterType type, String animationState, int frameIndex) {
        String path = "";
        String characterName = type.name().toLowerCase(); // e.g., "shana"

        if ("idle".equals(animationState)) {
            path = CHARACTER_BASE_PATH + characterName + "/idle.png";
        } else if ("run".equals(animationState)) {
            path = CHARACTER_BASE_PATH + characterName + "/Run_" + frameIndex + ".png";
        } else {
            Gdx.app.error("GameAssetManager", "Unknown animation state: " + animationState);
            path = Default_Avatar; // Fallback
        }

        if (isLoaded(path)) {
            return new TextureRegion(get(path, Texture.class));
        } else {
            Gdx.app.error("GameAssetManager",
                    "Character texture not loaded: " + path + ". Falling back to default avatar.");
            return new TextureRegion(get(Default_Avatar, Texture.class)); // Fallback to default avatar
        }
    }

    public Skin getGameSkin() {
        return get(PIXTHULHU_SKIN, Skin.class);
    }

    /**
     * Retrieves the texture for a given enemy type.
     * 
     * @param type The EnemyType.
     * @return The TextureRegion for the enemy.
     */
    public TextureRegion getEnemyTexture(EnemyType type) {
        String path = "";
        switch (type) {
            case TENTACLE_MONSTER:
                path = TENTACLE_MONSTER_TEXTURE;
                break;
            case EYEBAT:
                path = EYEBAT_TEXTURE;
                break;
            case ELDER_BOSS:
                path = ELDER_BOSS_TEXTURE;
                break;
            default:
                Gdx.app.error("GameAssetManager", "Unknown enemy type: " + type + ". Falling back to default avatar.");
                path = Default_Avatar; // Fallback
                break;
        }
        if (isLoaded(path)) {
            return new TextureRegion(get(path, Texture.class));
        } else {
            Gdx.app.error("GameAssetManager",
                    "Enemy texture not loaded: " + path + ". Falling back to default avatar.");
            return new TextureRegion(get(Default_Avatar, Texture.class)); // Fallback
        }
    }

    /**
     * Retrieves the texture for a player bullet based on weapon name.
     * For now, returns a generic player bullet. Can be extended for weapon-specific
     * bullets.
     * 
     * @param weaponName The name of the weapon.
     * @return The TextureRegion for the bullet.
     */
    public TextureRegion getBulletTexture(String weaponName) {
        if (isLoaded(PLAYER_BULLET_TEXTURE)) {
            return new TextureRegion(get(PLAYER_BULLET_TEXTURE, Texture.class));
        } else {
            Gdx.app.error("GameAssetManager",
                    "Player bullet texture not loaded: " + PLAYER_BULLET_TEXTURE + ". Falling back to default avatar.");
            return new TextureRegion(get(Default_Avatar, Texture.class)); // Fallback
        }
    }

    public TextureRegion getTreeTexture() {
        Skin skin = getGameSkin();
        if (skin.has("tree", TextureRegion.class)) {
            return skin.getRegion("tree");
        }
        if (skin.has(DEFAULT_TREE_TEXTURE_REGION_NAME, TextureRegion.class)) {
            return skin.getRegion(DEFAULT_TREE_TEXTURE_REGION_NAME);
        }
        Gdx.app.log("GameAssetManager",
                "Tree texture or '" + DEFAULT_TREE_TEXTURE_REGION_NAME + "' not found. Returning null.");
        return createPlaceholderRegion(); // Fallback
    }

    public Texture getAvatarTexture(String avatarPath) {
        if (isLoaded(avatarPath, Texture.class)) {
            return get(avatarPath, Texture.class);
        }
        Gdx.app.log("GameAssetManager", "Avatar texture not loaded: " + avatarPath + ". Attempting default.");
        if (isLoaded(Default_Avatar, Texture.class)) {
            return get(Default_Avatar, Texture.class);
        }
        Gdx.app.error("GameAssetManager", "Default avatar texture also not loaded: " + Default_Avatar);
        return null;
    }

    /**
     * Retrieves the texture for an enemy bullet.
     * 
     * @param type The EnemyType (can be used to differentiate enemy bullets if
     *             needed).
     * @return The TextureRegion for the enemy bullet.
     */
    public TextureRegion getEnemyBulletTexture(EnemyType type) {
        if (isLoaded(ENEMY_BULLET_TEXTURE)) {
            return new TextureRegion(get(ENEMY_BULLET_TEXTURE, Texture.class));
        } else {
            Gdx.app.error("GameAssetManager",
                    "Enemy bullet texture not loaded: " + ENEMY_BULLET_TEXTURE + ". Falling back to default avatar.");
            return new TextureRegion(get(Default_Avatar, Texture.class)); // Fallback
        }
    }

    /**
     * Retrieves the sound for shooting.
     * 
     * @return The Sound object for shooting.
     */
    public Sound getShootSound() {
        if (isLoaded(SHOOT_SOUND)) {
            return get(SHOOT_SOUND, Sound.class);
        } else {
            Gdx.app.error("GameAssetManager", "Shoot sound not loaded: " + SHOOT_SOUND);
            return null;
        }
    }

    private TextureRegion createPlaceholderRegion() {
        // Create a small, noticeable placeholder if a texture is missing
        // This helps in debugging missing assets.
        // This requires a Texture to be loaded, or you can draw a shape.
        // For simplicity, if you have a "white" region in your skin:
        if (getGameSkin().has("white", TextureRegion.class)) {
            return getGameSkin().getRegion("white");
        }
        // If not, this part becomes more complex or you ensure a 1x1 white pixel
        // texture is always loaded.
        return null; // Avoid this if possible
    }

    // Add more specific getters for other assets as needed
}