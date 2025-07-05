package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.minutestilldawn.game.Main;
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


    protected Sound clickSound;
    protected Sound popUpSound;
    protected Sound avatarChoose;
    protected Sound winningSound;
    protected Sound losingSound;
    protected Sound eyeBatDeathSound;
    protected Sound footStepSound;
    protected Sound shotSound;
    protected Sound reloadSound;
    protected Sound eyeBatShotSound;
    protected Sound elderBrainDeathSound;
    protected Sound levelUpSound;
    protected Sound damageSound;
    protected Sound obtainPointSound;
    protected Sound switchSound;
    private static Music backgroundMusic;
    private static String musicFilePath = "musics/Wasteland Combat.mp3";
    private static float musicVolume = 0.5f;

    private Texture[] characterDasherIdle;
    private Texture[] characterDasherActive;
    private Animation<Texture> idleAnimationDasher;
    private Animation<Texture> activeAnimationDasher;

    private Texture[] characterDiamondIdle;
    private Texture[] characterDiamondActive;
    private Animation<Texture> idleAnimationDiamond;
    private Animation<Texture> activeAnimationDiamond;

    private Texture[] characterLilithIdle;
    private Texture[] characterLilithActive;
    private Animation<Texture> idleAnimationLilith;
    private Animation<Texture> activeAnimationLilith;

    private Texture[] characterScarletIdle;
    private Texture[] characterScarletActive;
    private Animation<Texture> idleAnimationScarlet;
    private Animation<Texture> activeAnimationScarlet;

    private Texture[] characterShanaIdle;
    private Texture[] characterShanaActive;
    private Animation<Texture> idleAnimationShana;
    private Animation<Texture> activeAnimationShana;

    private Texture SMGDual;
    private Texture[] SMGDualReload;
    private Animation<Texture> SMGDualReloadAnimation;

    private Texture revolver;
    private Texture[] revolverReload;
    private Animation<Texture> revolverReloadAnimation;

    private Texture shotgun;
    private Texture[] shotgunReload;
    private Animation<Texture> shotgunReloadAnimation;

    private Texture[] treeFrames;
    private Animation<Texture> treeAnimation;

    private Texture[] tentacleMonsterSpawnFrames;
    private Animation<Texture> tentacleMosterSpawnAnimation;
    private Texture[] tentacleMonsterAttackFrames;
    private Animation<Texture> tentacleMonsterAttackAnimation;

    private Texture[] eyeBatFrames;
    private Animation<Texture> eyeBatAnimation;
    private Texture eyeBatBullet;

    private Texture elder;

    private Texture vitalityAbility;
    private Texture damagerAbility;
    private Texture procreaseAbility;
    private Texture amocreaseAbility;
    private Texture speedyAbility;

    private Texture map;
    private Texture bullet;
    private Texture XP;
    private Texture cursor;
    private Texture pixel;
    private Texture circle;

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


        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/UI Click 36.wav"));
        popUpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Pop (3).wav"));
        avatarChoose = Gdx.audio.newSound(Gdx.files.internal("sounds/AvatarChoose.mp3"));
        winningSound = Gdx.audio.newSound(Gdx.files.internal("sounds/You Win (2).wav"));
        losingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/You Lose (4).wav"));
        eyeBatDeathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Bat_Death_02.wav"));
        footStepSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Footsteps_Casual_Grass_01.wav"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx_sounds_impact1.wav"));
        eyeBatShotSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Standard_Weapon_Whoosh_02.wav"));
        reloadSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Weapon_Shotgun_Reload.wav"));
        elderBrainDeathSound = Gdx.audio
                .newSound(Gdx.files.internal("sounds/Monster_2_RecieveAttack_HighIntensity_01.wav"));
        obtainPointSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Obtain_Points_01.wav"));
        damageSound = Gdx.audio.newSound(Gdx.files.internal("sounds/male-hurt-sound-95206.mp3"));
        levelUpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Buff_Power_Up_01.wav"));
        switchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Switch (8).wav"));
        characterDasherIdle = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/idle/Idle_0 #8325.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/idle/Idle_1 #8355.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/idle/Idle_2 #8814.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/idle/Idle_3 #8452.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/idle/Idle_4 #8313.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/idle/Idle_5 #8302.png")),
        };

        characterDiamondIdle = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/idle/Idle_0 #8328.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/idle/Idle_1 #8358.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/idle/Idle_2 #8817.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/idle/Idle_3 #8455.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/idle/Idle_4 #8316.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/idle/Idle_5 #8305.png")),
        };

        characterLilithIdle = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/idle/Idle_0 #8333.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/idle/Idle_1 #8363.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/idle/Idle_2 #8822.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/idle/Idle_3 #8460.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/idle/Idle_4 #8321.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/idle/Idle_5 #8310.png")),
        };

        characterScarletIdle = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/idle/Idle_0 #8327.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/idle/Idle_1 #8357.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/idle/Idle_2 #8816.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/idle/Idle_3 #8454.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/idle/Idle_4 #8315.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/idle/Idle_5 #8304.png")),
        };

        characterShanaIdle = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Shana/idle/Idle_0 #8330.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/idle/Idle_1 #8360.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/idle/Idle_2 #8819.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/idle/Idle_3 #8457.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/idle/Idle_4 #8318.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/idle/Idle_5 #8307.png")),
        };

        idleAnimationDasher = new Animation<>(0.1f, characterDasherIdle);
        idleAnimationDiamond = new Animation<>(0.1f, characterDiamondIdle);
        idleAnimationLilith = new Animation<>(0.1f, characterLilithIdle);
        idleAnimationScarlet = new Animation<>(0.1f, characterScarletIdle);
        idleAnimationShana = new Animation<>(0.1f, characterShanaIdle);

        // Load all run animation frames for all characters (for AssetManager)
        String[] shanaRun = {"pictures/Heros/Shana/run/Run_0 #8762.png", "pictures/Heros/Shana/run/Run_1 #8778.png", "pictures/Heros/Shana/run/Run_2 #8286.png", "pictures/Heros/Shana/run/Run_3 #8349.png"};
        String[] diamondRun = {"pictures/Heros/Diamond/run/Run_0 #8760.png", "pictures/Heros/Diamond/run/Run_1 #8776.png", "pictures/Heros/Diamond/run/Run_2 #8284.png", "pictures/Heros/Diamond/run/Run_3 #8347.png"};
        String[] scarletRun = {"pictures/Heros/Scarlet/run/Run_0 #8759.png", "pictures/Heros/Scarlet/run/Run_1 #8775.png", "pictures/Heros/Scarlet/run/Run_2 #8283.png", "pictures/Heros/Scarlet/run/Run_3 #8346.png"};
        String[] lilithRun = {"pictures/Heros/Lilith/run/Run_0 #8765.png", "pictures/Heros/Lilith/run/Run_1 #8781.png", "pictures/Heros/Lilith/run/Run_2 #8289.png", "pictures/Heros/Lilith/run/Run_3 #8352.png"};
        String[] dasherRun = {"pictures/Heros/Dasher/run/Run_0 #8757.png", "pictures/Heros/Dasher/run/Run_1 #8773.png", "pictures/Heros/Dasher/run/Run_2 #8281.png", "pictures/Heros/Dasher/run/Run_3 #8344.png"};
        for (String path : shanaRun) load(path, Texture.class);
        for (String path : diamondRun) load(path, Texture.class);
        for (String path : scarletRun) load(path, Texture.class);
        for (String path : lilithRun) load(path, Texture.class);
        for (String path : dasherRun) load(path, Texture.class);

        characterDasherActive = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/run/Run_0 #8757.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/run/Run_1 #8773.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/run/Run_2 #8281.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Dasher/run/Run_3 #8344.png")),
        };

        characterDiamondActive = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/run/Run_0 #8760.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/run/Run_1 #8776.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/run/Run_2 #8284.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Diamond/run/Run_3 #8347.png")),
        };

        characterLilithActive = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/run/Run_0 #8765.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/run/Run_1 #8781.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/run/Run_2 #8289.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Lilith/run/Run_3 #8352.png")),
        };

        characterScarletActive = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/run/Run_0 #8759.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/run/Run_1 #8775.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/run/Run_2 #8283.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Scarlet/run/Run_3 #8346.png")),
        };

        characterShanaActive = new Texture[] {
                new Texture(Gdx.files.internal("pictures/Heros/Shana/run/Run_0 #8762.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/run/Run_1 #8778.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/run/Run_2 #8286.png")),
                new Texture(Gdx.files.internal("pictures/Heros/Shana/run/Run_3 #8349.png")),
        };

        activeAnimationDasher = new Animation<>(0.1f, characterDasherActive);
        activeAnimationShana = new Animation<>(0.1f, characterShanaActive);
        activeAnimationDiamond = new Animation<>(0.1f, characterDiamondActive);
        activeAnimationLilith = new Animation<>(0.1f, characterLilithActive);
        activeAnimationScarlet = new Animation<>(0.1f, characterScarletActive);

        SMGDual = new Texture(Gdx.files.internal("pictures/guns/SMGStill/SMGStill.png"));
        SMGDualReload = new Texture[] {
                new Texture(Gdx.files.internal("pictures/guns/SMGReload/SMGReload_0.png")),
                new Texture(Gdx.files.internal("pictures/guns/SMGReload/SMGReload_1.png")),
                new Texture(Gdx.files.internal("pictures/guns/SMGReload/SMGReload_2.png")),
                new Texture(Gdx.files.internal("pictures/guns/SMGReload/SMGReload_3.png")),
        };

        revolver = new Texture(Gdx.files.internal("pictures/guns/RevolverStill/RevolverStill.png"));
        revolverReload = new Texture[] {
                new Texture(Gdx.files.internal("pictures/guns/RevolverReload/RevolverReload_0.png")),
                new Texture(Gdx.files.internal("pictures/guns/RevolverReload/RevolverReload_1.png")),
                new Texture(Gdx.files.internal("pictures/guns/RevolverReload/RevolverReload_2.png")),
                new Texture(Gdx.files.internal("pictures/guns/RevolverReload/RevolverReload_3.png")),
        };

        shotgun = new Texture(Gdx.files.internal("pictures/guns/ShotgunStill/T_Shotgun_SS_0.png"));
        shotgunReload = new Texture[] {
                new Texture(Gdx.files.internal("pictures/guns/ShotgunReload/T_Shotgun_SS_1.png")),
                new Texture(Gdx.files.internal("pictures/guns/ShotgunReload/T_Shotgun_SS_2.png")),
                new Texture(Gdx.files.internal("pictures/guns/ShotgunReload/T_Shotgun_SS_3.png")),
        };

        SMGDualReloadAnimation = new Animation<>(0.1f, SMGDualReload);
        revolverReloadAnimation = new Animation<>(0.1f, revolverReload);
        shotgunReloadAnimation = new Animation<>(0.1f, shotgunReload);

        treeFrames = new Texture[] {
                new Texture(Gdx.files.internal("pictures/monsters/tree/T_TreeMonster_0.png")),
                new Texture(Gdx.files.internal("pictures/monsters/tree/T_TreeMonster_1.png")),
                new Texture(Gdx.files.internal("pictures/monsters/tree/T_TreeMonster_2.png")),
                // new
                // Texture(Gdx.files.internal("pictures/monsters/tree/T_TreeMonsterWalking.png")),
        };
        treeAnimation = new Animation<>(0.1f, treeFrames);

        tentacleMonsterSpawnFrames = new Texture[] {
                new Texture(Gdx.files.internal("pictures/monsters/tentacle/spawn/T_TentacleSpawn_0.png")),
                new Texture(Gdx.files.internal("pictures/monsters/tentacle/spawn/T_TentacleSpawn_1.png")),
                new Texture(Gdx.files.internal("pictures/monsters/tentacle/spawn/T_TentacleSpawn_2.png")),
        };
        tentacleMosterSpawnAnimation = new Animation<>(0.1f, tentacleMonsterSpawnFrames);

        tentacleMonsterAttackFrames = new Texture[] {
                new Texture(Gdx.files.internal("pictures/monsters/tentacle/attack/T_TentacleEnemy_1.png")),
                new Texture(Gdx.files.internal("pictures/monsters/tentacle/attack/T_TentacleEnemy_2.png")),
                new Texture(Gdx.files.internal("pictures/monsters/tentacle/attack/T_TentacleEnemy_3.png")),
        };
        tentacleMonsterAttackAnimation = new Animation<>(0.1f, tentacleMonsterAttackFrames);

        eyeBatFrames = new Texture[] {
                new Texture(Gdx.files.internal("pictures/monsters/eyebat/T_EyeBat_0.png")),
                new Texture(Gdx.files.internal("pictures/monsters/eyebat/T_EyeBat_1.png")),
                new Texture(Gdx.files.internal("pictures/monsters/eyebat/T_EyeBat_2.png")),
                new Texture(Gdx.files.internal("pictures/monsters/eyebat/T_EyeBat_3.png")),
        };
        eyeBatAnimation = new Animation<>(0.1f, eyeBatFrames);
        eyeBatBullet = new Texture(Gdx.files.internal("pictures/monsters/eyebat/T_EyeBat_EM.png"));

        elder = new Texture(Gdx.files.internal("pictures/monsters/elder/ElderBrain.png"));

        XP = new Texture(Gdx.files.internal("pictures/XP_Coin.png"));

        vitalityAbility = new Texture(Gdx.files.internal("pictures/ability/Vitality.png"));
        damagerAbility = new Texture(Gdx.files.internal("pictures/ability/Damager.png"));
        procreaseAbility = new Texture(Gdx.files.internal("pictures/ability/Procrease.png"));
        amocreaseAbility = new Texture(Gdx.files.internal("pictures/ability/Amocrease.png"));
        speedyAbility = new Texture(Gdx.files.internal("pictures/ability/Speedy.png"));

        cursor = new Texture(Gdx.files.internal("pictures/cursor.png"));

        // Create solid white pixel texture
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose(); // Dispose Pixmap after creating the texture

        // Create circle texture
        Pixmap circlePixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
        circlePixmap.setBlending(Pixmap.Blending.None);
        circlePixmap.setColor(0, 0, 0, 0); // Transparent
        circlePixmap.fill();
        circlePixmap.setColor(0, 0, 0, 1); // Black with alpha
        circlePixmap.fillCircle(128, 128, 128);
        circle = new Texture(circlePixmap);
        circlePixmap.dispose(); // Dispose Pixmap after creating the texture

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
     * Retrieves a specific character animation frame as a TextureRegion.
     * @param type           The CharacterType (e.g., SHANA).
     * @param animationState The state of the animation ("idle", "run").
     * @param frameIndex     The index of the frame (for "run", 0-3; for "idle", 0).
     * @return The TextureRegion for the requested animation frame.
     */
    public TextureRegion getCharacterTexture(CharacterType type, String animationState, int frameIndex) {
        // Normalize frameIndex for idle (always 0) and for run (0-3)
        if (animationState.equalsIgnoreCase("idle")) {
            switch (type) {
                case SHANA:
                    if (characterShanaIdle != null && characterShanaIdle.length > 0)
                        return new TextureRegion(characterShanaIdle[0]);
                    break;
                case DIAMOND:
                    if (characterDiamondIdle != null && characterDiamondIdle.length > 0)
                        return new TextureRegion(characterDiamondIdle[0]);
                    break;
                case SCARLET:
                    if (characterScarletIdle != null && characterScarletIdle.length > 0)
                        return new TextureRegion(characterScarletIdle[0]);
                    break;
                case LILITH:
                    if (characterLilithIdle != null && characterLilithIdle.length > 0)
                        return new TextureRegion(characterLilithIdle[0]);
                    break;
                case DASHER:
                    if (characterDasherIdle != null && characterDasherIdle.length > 0)
                        return new TextureRegion(characterDasherIdle[0]);
                    break;
            }
        } else if (animationState.equalsIgnoreCase("run")) {
            int idx = Math.max(0, Math.min(frameIndex, 3));
            switch (type) {
                case SHANA:
                    if (characterShanaActive != null && characterShanaActive.length > idx)
                        return new TextureRegion(characterShanaActive[idx]);
                    break;
                case DIAMOND:
                    if (characterDiamondActive != null && characterDiamondActive.length > idx)
                        return new TextureRegion(characterDiamondActive[idx]);
                    break;
                case SCARLET:
                    if (characterScarletActive != null && characterScarletActive.length > idx)
                        return new TextureRegion(characterScarletActive[idx]);
                    break;
                case LILITH:
                    if (characterLilithActive != null && characterLilithActive.length > idx)
                        return new TextureRegion(characterLilithActive[idx]);
                    break;
                case DASHER:
                    if (characterDasherActive != null && characterDasherActive.length > idx)
                        return new TextureRegion(characterDasherActive[idx]);
                    break;
            }
        }
        // Fallback: return a placeholder or default avatar
        Gdx.app.error("GameAssetManager", "Character texture not loaded for type: " + type + ", state: " + animationState + ", frame: " + frameIndex);
        if (isLoaded(Default_Avatar, Texture.class)) {
            return new TextureRegion(get(Default_Avatar, Texture.class));
        }
        return null;
    }

    /**
     * Returns the Texture for a given character, animation state, and frame index.
     * Used for animation frame loading in Player.
     * @param type CharacterType
     * @param animationState "idle" or "run"
     * @param frameIndex frame index (0 for idle, 0-3 for run)
     * @return Texture for the requested frame, or null if not found
     */
    public Texture getCharacterTextureRaw(CharacterType type, String animationState, int frameIndex) {
        if (animationState.equalsIgnoreCase("idle")) {
            switch (type) {
                case SHANA:
                    if (characterShanaIdle != null && characterShanaIdle.length > frameIndex)
                        return characterShanaIdle[frameIndex];
                    break;
                case DIAMOND:
                    if (characterDiamondIdle != null && characterDiamondIdle.length > frameIndex)
                        return characterDiamondIdle[frameIndex];
                    break;
                case SCARLET:
                    if (characterScarletIdle != null && characterScarletIdle.length > frameIndex)
                        return characterScarletIdle[frameIndex];
                    break;
                case LILITH:
                    if (characterLilithIdle != null && characterLilithIdle.length > frameIndex)
                        return characterLilithIdle[frameIndex];
                    break;
                case DASHER:
                    if (characterDasherIdle != null && characterDasherIdle.length > frameIndex)
                        return characterDasherIdle[frameIndex];
                    break;
            }
        } else if (animationState.equalsIgnoreCase("run")) {
            int idx = Math.max(0, Math.min(frameIndex, 3));
            switch (type) {
                case SHANA:
                    if (characterShanaActive != null && characterShanaActive.length > idx)
                        return characterShanaActive[idx];
                    break;
                case DIAMOND:
                    if (characterDiamondActive != null && characterDiamondActive.length > idx)
                        return characterDiamondActive[idx];
                    break;
                case SCARLET:
                    if (characterScarletActive != null && characterScarletActive.length > idx)
                        return characterScarletActive[idx];
                    break;
                case LILITH:
                    if (characterLilithActive != null && characterLilithActive.length > idx)
                        return characterLilithActive[idx];
                    break;
                case DASHER:
                    if (characterDasherActive != null && characterDasherActive.length > idx)
                        return characterDasherActive[idx];
                    break;
            }
        }
        // Fallback: return null
        Gdx.app.error("GameAssetManager", "Character texture not loaded for type: " + type + ", state: " + animationState + ", frame: " + frameIndex);
        return null;
    }

    public Skin getGameSkin() {
        return get(PIXTHULHU_SKIN, Skin.class);
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

    public Texture[] getCharacterDasherIdle() {
        return characterDasherIdle;
    }

    public Texture[] getCharacterDiamondIdle() {
        return characterDiamondIdle;
    }

    public Texture[] getCharacterLilithIdle() {
        return characterLilithIdle;
    }

    public Texture[] getCharacterScarletIdle() {
        return characterScarletIdle;
    }

    public Texture[] getCharacterShanaIdle() {
        return characterShanaIdle;
    }

    public Animation<Texture> getIdleAnimationDasher() {
        return idleAnimationDasher;
    }

    public Animation<Texture> getIdleAnimationDiamond() {
        return idleAnimationDiamond;
    }

    public Animation<Texture> getIdleAnimationLilith() {
        return idleAnimationLilith;
    }

    public Animation<Texture> getIdleAnimationScarlet() {
        return idleAnimationScarlet;
    }

    public Animation<Texture> getIdleAnimationShana() {
        return idleAnimationShana;
    }

    public Texture[] getCharacterDasherActive() {
        return characterDasherActive;
    }

    public Animation<Texture> getActiveAnimationDasher() {
        return activeAnimationDasher;
    }

    public Texture[] getCharacterDiamondActive() {
        return characterDiamondActive;
    }

    public Animation<Texture> getActiveAnimationDiamond() {
        return activeAnimationDiamond;
    }

    public Texture[] getCharacterLilithActive() {
        return characterLilithActive;
    }

    public Animation<Texture> getActiveAnimationLilith() {
        return activeAnimationLilith;
    }

    public Texture[] getCharacterScarletActive() {
        return characterScarletActive;
    }

    public Animation<Texture> getActiveAnimationScarlet() {
        return activeAnimationScarlet;
    }

    public Texture[] getCharacterShanaActive() {
        return characterShanaActive;
    }

    public Animation<Texture> getActiveAnimationShana() {
        return activeAnimationShana;
    }

    public Texture getSMGDual() {
        return SMGDual;
    }

    public Texture getRevolver() {
        return revolver;
    }

    public Texture getShotgun() {
        return shotgun;
    }

    public Animation<Texture> getSMGDualReloadAnimation() {
        return SMGDualReloadAnimation;
    }

    public Animation<Texture> getRevolverReloadAnimation() {
        return revolverReloadAnimation;
    }

    public Animation<Texture> getShotgunReloadAnimation() {
        return shotgunReloadAnimation;
    }

    public Texture getMap() {
        return map;
    }

    public Texture getBullet() {
        return bullet;
    }

    public Texture[] getSMGDualReload() {
        return SMGDualReload;
    }

    public Texture[] getTreeFrames() {
        return treeFrames;
    }

    public Animation<Texture> getTreeAnimation() {
        return treeAnimation;
    }

    public Texture[] getRevolverReload() {
        return revolverReload;
    }

    public Texture[] getTentacleMonsterSpawnFrames() {
        return tentacleMonsterSpawnFrames;
    }

    public Animation<Texture> getTentacleMosterSpawnAnimation() {
        return tentacleMosterSpawnAnimation;
    }

    public Texture[] getTentacleMonsterAttackFrames() {
        return tentacleMonsterAttackFrames;
    }

    public Animation<Texture> getTentacleMonsterAttackAnimation() {
        return tentacleMonsterAttackAnimation;
    }

    public Texture[] getShotgunReload() {
        return shotgunReload;
    }

    public Texture[] getEyeBatFrames() {
        return eyeBatFrames;
    }

    public Animation<Texture> getEyeBatAnimation() {
        return eyeBatAnimation;
    }

    public Texture getEyeBatBullet() {
        return eyeBatBullet;
    }

    public Texture getElder() {
        return elder;
    }

    public Texture getXP() {
        return XP;
    }

    public Texture getVitalityAbility() {
        return vitalityAbility;
    }

    public Texture getDamagerAbility() {
        return damagerAbility;
    }

    public Texture getProcreaseAbility() {
        return procreaseAbility;
    }

    public Texture getAmocreaseAbility() {
        return amocreaseAbility;
    }

    public Texture getSpeedyAbility() {
        return speedyAbility;
    }

    public Texture getCursor() {
        return cursor;
    }

    public Texture getPixel() {
        return pixel;
    }

    public Texture getCircle() {
        return circle;
    }

    public void playGameSFX(String string) {
        if (!Main.getInstance().isMenuSFXDisabled()) {
            switch (string) {
                case "click" -> clickSound.play();
                case "popUp" -> popUpSound.play();
                case "avatarChoose" -> avatarChoose.play();
                case "winning" -> winningSound.play();
                case "losing" -> losingSound.play();
                case "eyeBatDeath" -> eyeBatDeathSound.play();
                case "walk" -> footStepSound.play(1.0f, 0.5f, 0f);
                case "shoot" -> shotSound.play();
                case "reload" -> reloadSound.play();
                case "eyeBatShoot" -> eyeBatShotSound.play();
                case "elderBrainDeath" -> elderBrainDeathSound.play();
                case "point" -> obtainPointSound.play();
                case "damage" -> damageSound.play();
                case "levelUp" -> levelUpSound.play();
                case "switch" -> switchSound.play();
            }
        }
    }

    /**
     * Returns the TextureRegion for the given EnemyType.
     * This method should be used by GameController to spawn enemies with the correct visuals.
     */
    public TextureRegion getEnemyTexture(Enemy.EnemyType type) {
        switch (type) {
            case TENTACLE_MONSTER:
                // Use the first frame of tentacleMonsterAttackFrames as the main sprite
                if (tentacleMonsterAttackFrames != null && tentacleMonsterAttackFrames.length > 0)
                    return new TextureRegion(tentacleMonsterAttackFrames[0]);
                break;
            case EYEBAT:
                // Use the first frame of eyeBatFrames as the main sprite
                if (eyeBatFrames != null && eyeBatFrames.length > 0)
                    return new TextureRegion(eyeBatFrames[0]);
                break;
            case ELDER_BOSS:
                if (elder != null)
                    return new TextureRegion(elder);
                break;
            default:
                break;
        }
        // Fallback: return a placeholder region or a default
        Gdx.app.error("GameAssetManager", "Enemy texture not found for type: " + type);
        return createPlaceholderRegion();
    }

}