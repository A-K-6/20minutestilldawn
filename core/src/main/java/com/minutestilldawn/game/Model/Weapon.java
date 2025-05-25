package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;

// This will be expanded later with more specific weapon properties (damage, ammo, reload time)
public class Weapon {
    private String name;
    private int damage;

    private int maxAmmo;
    private int currentAmmo;
    private float reloadTime;
    private float reloadTimer;
    private boolean isReloading;

    public Weapon(String name, int damage, int maxAmmo, float reloadTime) {
        this.name = name;
        this.damage = damage;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        this.reloadTime = reloadTime;
        this.reloadTimer = 0;
        this.isReloading = false;
    }

    public void reload() {
        if (!isReloading) {
            Gdx.app.log("Weapon", name + " reloading...");
            isReloading = true;
            reloadTimer = reloadTime;
        }
    }

    public void update(float delta) {
        if (isReloading) {
            reloadTimer -= delta;
            if (reloadTimer <= 0) {
                currentAmmo = maxAmmo;
                isReloading = false;
                Gdx.app.log("Weapon", name + " reloaded.");
            }
        }
    }

    public boolean canShoot() {
        return currentAmmo > 0 && !isReloading;
    }

    public void shoot() {
        if (canShoot()) {
            currentAmmo--;
            Gdx.app.log("Weapon", name + " fired! Ammo left: " + currentAmmo);
        }
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public int getDamage() {
        return damage; 
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}