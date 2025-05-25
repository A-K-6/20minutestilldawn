package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private CharacterType characterType;
    private Vector2 position;
    private float speed;
    private int maxHp;
    private int currentHp;
    private int xp;
    private int level;
    private Weapon currentWeapon; // Player has a weapon [cite: 84]

    // Visuals
    private TextureAtlas playerAtlas; // Atlas for player animations
    private TextureRegion currentFrame; // Current frame for drawing

    // Game mechanics
    private float invincibilityTimer;
    private static final float INVINCIBILITY_DURATION = 1.0f; // 1 second invincibility [cite: 80]

    public Player(CharacterType type, TextureAtlas atlas) {
        this.characterType = type;
        this.playerAtlas = atlas;
        this.position = new Vector2(400, 300); // Starting position (center of a 800x600 screen)
        this.speed = type.getBaseSpeed();
        this.maxHp = type.getBaseHp();
        this.currentHp = maxHp;
        this.xp = 0;
        this.level = 1;
        this.currentWeapon = new Weapon("Revolver", 20, 6, 1.0f); // Default weapon [cite: 85]

        this.invincibilityTimer = 0;

        // Set initial frame (you'll need more sophisticated animation logic later)
        this.currentFrame = playerAtlas.findRegion("player_idle"); // Assuming you have an "player_idle" region
        if (this.currentFrame == null) {
            Gdx.app.error("Player", "player_idle texture region not found in atlas!");
            // Fallback to any region if idle not found
            this.currentFrame = playerAtlas.getRegions().first();
        }
    }

    public void update(float delta) {
        // Update player position based on speed (handled by controller input)
        // Check for invincibility
        if (invincibilityTimer > 0) {
            invincibilityTimer -= delta;
            if (invincibilityTimer <= 0) {
                Gdx.app.log("Player", "Invincibility ended.");
            }
        }

        // Update current weapon state (e.g., reloading)
        currentWeapon.update(delta);

        // More complex update logic (animation, ability durations, etc.)
    }

    public void move(Vector2 direction, float delta) {
        // Character movement (W-A-S-D, diagonal) [cite: 60]
        position.add(direction.x * speed * delta, direction.y * speed * delta);
    }

    public void takeDamage(int damage) {
        // Damage [cite: 79]
        if (invincibilityTimer <= 0) {
            currentHp -= damage;
            Gdx.app.log("Player", "Player took " + damage + " damage. HP: " + currentHp);
            invincibilityTimer = INVINCIBILITY_DURATION; // Start invincibility [cite: 80]
            // Trigger damage animation [cite: 99]
            if (currentHp <= 0) {
                Gdx.app.log("Player", "Player defeated!");
                // Handle game over [cite: 87]
            }
        } else {
            Gdx.app.log("Player", "Player is invincible!");
        }
    }

    public void gainXP(int amount) {
        xp += amount;
        Gdx.app.log("Player", "Gained " + amount + " XP. Total XP: " + xp);
        checkLevelUp(); // Check for level up after gaining XP [cite: 81]
    }

    private void checkLevelUp() {
        int xpNeededForNextLevel = 20 * level; // XP needed to go from current level to next [cite: 83]
        if (xp >= xpNeededForNextLevel) {
            levelUp();
            xp -= xpNeededForNextLevel; // Reset XP for the new level
            Gdx.app.log("Player", "Leveled up! Remaining XP: " + xp);
            // After level up, new XP needed is 20 * new_level
        }
    }

    private void levelUp() {
        level++;
        Gdx.app.log("Player", "LEVEL UP! New level: " + level);
        // Handle ability acquisition (random or choice) [cite: 81, 82]
        // Play level up animation [cite: 92]
    }

    public void shoot(float targetX, float targetY) {
        if (currentWeapon.canShoot()) {
            currentWeapon.shoot();
            // Create and add a new bullet to a list in GameController
            // (The actual bullet creation will be in GameController based on player position and target)
            Gdx.app.log("Player", "Player attempting to shoot towards (" + targetX + ", " + targetY + ")");
            // Play shoot sound effect [cite: 104]
        } else if (currentWeapon.getCurrentAmmo() <= 0 && !currentWeapon.isReloading()) {
            Gdx.app.log("Player", "Out of ammo! Reloading automatically or waiting for manual reload.");
            // Optionally trigger auto-reload here if enabled in settings [cite: 37]
        }
    }

    public void reload() {
        // Reload weapon [cite: 63]
        currentWeapon.reload();
    }

    // --- Getters ---
    public Vector2 getPosition() {
        return position;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    // Used for level progress bar [cite: 98]
    public int getXpNeededForNextLevel() {
        return 20 * level;
    }
}