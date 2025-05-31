package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;

public class Weapon {
    private String name;
    private int baseDamage; // Base damage of the weapon
    private int maxAmmo;
    private int currentAmmo;
    private float reloadTime; // Time in seconds
    private float reloadTimer;
    private boolean isReloading;
    private int projectilesPerShot; // Number of bullets fired at once (e.g., 1 for Revolver, 4 for Shotgun)
    private int initialMaxAmmo; // To store the original max ammo for reset/AMOCREASE logic
    private int initialProjectilesPerShot; // To store original projectiles for reset/PROCREASE
    private float bulletSpeed = 500f; // Add this field
    // public Weapon(String name, int baseDamage, int maxAmmo, float reloadTime, int
    // projectilesPerShot, float bulletSpeed) { ... this.bulletSpeed = bulletSpeed;
    // ...}
    // public float getBulletSpeed() { return bulletSpeed; }
    // Example: new Weapon("Revolver", 20, 6, 1.0f, 1, 600f);

    public Weapon(String name, int baseDamage, int maxAmmo, float reloadTime) {
        this(name, baseDamage, maxAmmo, reloadTime, 1, 500f); // Default to 1 projectile per shot
        // TODO: BULLET SPEED NEED TO BE DONE : )
    }

    public Weapon(String name, int baseDamage, int maxAmmo, float reloadTime, int projectilesPerShot,
            float bulletSpeed) {
        this.name = name;
        this.baseDamage = baseDamage;
        this.maxAmmo = maxAmmo;
        this.initialMaxAmmo = maxAmmo; // Store initial
        this.currentAmmo = maxAmmo;
        this.reloadTime = reloadTime;
        this.reloadTimer = 0;
        this.isReloading = false;
        this.projectilesPerShot = projectilesPerShot;
        this.initialProjectilesPerShot = projectilesPerShot; // Store initial
        this.bulletSpeed = bulletSpeed;
    }

    public void update(float delta) {
        if (isReloading) {
            reloadTimer -= delta;
            if (reloadTimer <= 0) {
                currentAmmo = maxAmmo; // Reload to current maxAmmo
                isReloading = false;
                reloadTimer = 0;
                Gdx.app.log("Weapon", name + " reloaded. Ammo: " + currentAmmo + "/" + maxAmmo);
            }
        }
    }

    public boolean canShoot() {
        return currentAmmo > 0 && !isReloading;
    }

    /**
     * Called when the weapon fires. Reduces ammo.
     * Actual bullet creation is handled by Player/GameController.
     */
    public void shoot() {
        if (canShoot()) {
            currentAmmo--;
            // Gdx.app.log("Weapon", name + " fired! Ammo left: " + currentAmmo);
            if (currentAmmo == 0 && !isReloading) {
                // Gdx.app.log("Weapon", name + " empty. Consider reloading.");
                // Auto-reload could be triggered here by GameState/Player if enabled
            }
        }
    }

    public void reload() {
        if (!isReloading && currentAmmo < maxAmmo) {
            Gdx.app.log("Weapon", name + " reloading... Current ammo: " + currentAmmo + "/" + maxAmmo);
            isReloading = true;
            reloadTimer = reloadTime;
        } else if (isReloading) {
            // Gdx.app.log("Weapon", name + " is already reloading.");
        } else if (currentAmmo == maxAmmo) {
            // Gdx.app.log("Weapon", name + " is already full.");
        }
    }

    /**
     * Resets ammo to full capacity. Does not reset maxAmmo if modified by
     * abilities.
     */
    public void resetAmmo() {
        this.currentAmmo = this.maxAmmo;
        this.isReloading = false;
        this.reloadTimer = 0;
    }

    /**
     * Resets the weapon to its initial state (max ammo, projectiles per shot).
     * Used when starting a completely new game, not for loading a saved game
     * where abilities might have modified these.
     */
    public void resetToInitialStats() {
        this.maxAmmo = this.initialMaxAmmo;
        this.projectilesPerShot = this.initialProjectilesPerShot;
        resetAmmo();
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public int getDamage() {
        return baseDamage;
    } // Player's getEffectiveDamage will use this

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public int getProjectilesPerShot() {
        return projectilesPerShot;
    }

    public float getReloadProgress() {
        if (!isReloading)
            return 0f;
        return Math.max(0f, 1f - (reloadTimer / reloadTime));
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    // --- Modifiers for Abilities ---
    public void increaseMaxAmmo(int amount) {
        this.maxAmmo += amount;
        this.currentAmmo += amount; // Also add to current ammo, or just increase capacity? PDF implies capacity.
                                    // Let's add to current as well for immediate benefit.
        if (this.currentAmmo > this.maxAmmo)
            this.currentAmmo = this.maxAmmo;
        Gdx.app.log("Weapon", name + " max ammo increased by " + amount + ". New max: " + this.maxAmmo);
    }

    public void increaseProjectiles(int amount) {
        this.projectilesPerShot += amount;
        Gdx.app.log("Weapon",
                name + " projectiles per shot increased by " + amount + ". New count: " + this.projectilesPerShot);
    }
}
