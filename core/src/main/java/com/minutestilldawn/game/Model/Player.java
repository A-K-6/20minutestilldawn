package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Player {
    private User user;
    private CharacterType characterType;
    private Vector2 position;
    private float currentSpeed;
    private int maxHp;
    private int currentHp;
    private int xp;
    private int level;
    private Weapon currentWeapon;
    private TextureAtlas playerAtlas;
    private TextureRegion currentFrame;
    private float invincibilityTimer;
    private static final float INVINCIBILITY_DURATION = 1.0f;

    private static class ActiveAbilityEffect {
        Ability ability;
        float durationTimer;
        ActiveAbilityEffect(Ability ability) {
            this.ability = ability;
            switch (ability) {
                case DAMAGER: case SPEEDY: this.durationTimer = 10.0f; break;
                default: this.durationTimer = -1; break;
            }
        }
    }
    private Array<ActiveAbilityEffect> activeEffects;
    private Vector2 aimDirection = new Vector2(1, 0); // Default aim right for auto-aim

    public Player(User user, CharacterType type, TextureAtlas atlas, Weapon startingWeapon) {
        this.user = user;
        this.characterType = type;
        this.playerAtlas = atlas;
        this.currentWeapon = startingWeapon;
        this.activeEffects = new Array<>();
        this.maxHp = type.getBaseHp();
        this.currentHp = this.maxHp;
        this.currentSpeed = type.getBaseSpeed(); // Initial speed
        this.position = new Vector2(400, 300);
        this.xp = 0;
        this.level = 1;
        this.invincibilityTimer = 0;

        this.currentFrame = playerAtlas.findRegion(characterType.getIdleFrameName());
        if (this.currentFrame == null) {
            Gdx.app.error("Player", "Texture region '" + characterType.getIdleFrameName() + "' not found! Using fallback.");
            if (playerAtlas.getRegions().size > 0) this.currentFrame = playerAtlas.getRegions().first();
            else Gdx.app.error("Player", "Player atlas is empty!");
        }
    }

    public void update(float delta) {
        if (invincibilityTimer > 0) {
            invincibilityTimer -= delta;
            if (invincibilityTimer < 0) invincibilityTimer = 0;
        }
        if (currentWeapon != null) currentWeapon.update(delta);

        // Update speed based on effects
        this.currentSpeed = getEffectiveSpeed();

        for (int i = activeEffects.size - 1; i >= 0; i--) {
            ActiveAbilityEffect effect = activeEffects.get(i);
            if (effect.durationTimer > 0) {
                effect.durationTimer -= delta;
                if (effect.durationTimer <= 0) {
                    activeEffects.removeIndex(i);
                    Gdx.app.log("Player", "Ability " + effect.ability.name() + " expired.");
                }
            }
        }
    }

    public void move(Vector2 direction, float delta) {
        if (direction.len() > 0) {
            position.mulAdd(direction.nor(), currentSpeed * delta); // Use currentSpeed
        }
    }
    
    public void setAimDirection(Vector2 direction) {
        if (direction.len2() > 0) { // Check if direction is not zero vector
            this.aimDirection.set(direction).nor();
        }
    }

    public void shoot(Vector2 mouseTargetPos, boolean isAutoAiming, Vector2 autoAimTargetPos, Pool<Bullet> bulletPool, Array<Bullet> activeBullets, GameAssetManager assetManager) {
        if (currentWeapon == null || !currentWeapon.canShoot()) {
            // Auto-reload logic can be triggered here if (gameState.isAutoReloadEnabled() && currentWeapon.getCurrentAmmo() == 0)
            return;
        }

        Vector2 shotDirection = new Vector2();
        if (isAutoAiming && autoAimTargetPos != null) {
            shotDirection.set(autoAimTargetPos).sub(position).nor();
        } else {
            shotDirection.set(mouseTargetPos).sub(position).nor();
        }
        
        if (shotDirection.len2() == 0) shotDirection.set(aimDirection); // Fallback if target is on player

        int bulletDamage = getEffectiveDamage(currentWeapon.getDamage());
        TextureRegion bulletTexture = assetManager.getBulletTexture(currentWeapon.getName()); // Assumes method in AssetManager

        for (int i = 0; i < currentWeapon.getProjectilesPerShot(); i++) {
            Bullet bullet = bulletPool.obtain();
            Vector2 projectileDir = new Vector2(shotDirection); // Copy base direction

            if (currentWeapon.getProjectilesPerShot() > 1) { // Apply spread for multi-shot weapons
                float baseSpreadAngle = 15f; // Max spread for shotgun (e.g., +/- 7.5 deg for 2 proj, +/- 15 for 3+)
                float spreadAnglePerProjectile = (currentWeapon.getProjectilesPerShot() > 1) ? baseSpreadAngle / (currentWeapon.getProjectilesPerShot() -1) : 0;
                float angleOffset = (i - (currentWeapon.getProjectilesPerShot() - 1) / 2.0f) * spreadAnglePerProjectile;
                if(currentWeapon.getName().equalsIgnoreCase("Shotgun")) { // Shotgun specific spread
                     angleOffset = (float)(Math.random() * 20.0 - 10.0); // Random spread +/-10 degrees for shotgun
                }
                projectileDir.rotateDeg(angleOffset);
            }
            // Calculate bullet start position slightly in front of player in shot direction
            float bulletSpawnOffset = 20f; // How far in front of player bullets spawn
            Vector2 bulletStartPosition = new Vector2(position).mulAdd(projectileDir, bulletSpawnOffset);

            bullet.init(bulletStartPosition.x, bulletStartPosition.y, projectileDir, bulletDamage, bulletTexture, true, currentWeapon.getBulletSpeed());
            activeBullets.add(bullet);
        }
        currentWeapon.shoot(); // Decrements ammo
    }


    public void takeDamage(int damage) {
        if (invincibilityTimer <= 0) {
            currentHp -= damage;
            Gdx.app.log("Player", "Player took " + damage + " damage. HP: " + currentHp + "/" + maxHp);
            if (currentHp <= 0) {
                currentHp = 0;
                Gdx.app.log("Player", "Player defeated!");
            } else {
                invincibilityTimer = INVINCIBILITY_DURATION;
            }
        }
    }

    public void gainXP(int amount) {
        if (currentHp <= 0) return;
        xp += amount;
        Gdx.app.log("Player", "Gained " + amount + " XP. Total XP: " + xp + "/" + getXpNeededForNextLevel());
    }

    public boolean canLevelUp() {
        return xp >= getXpNeededForNextLevel();
    }

    public void levelUp() { // Call this AFTER ability is chosen
        if (!canLevelUp()) return;
        int xpNeeded = getXpNeededForNextLevel();
        xp -= xpNeeded;
        level++;
        Gdx.app.log("Player", "LEVEL UP! New level: " + level + ". XP: " + xp + "/" + getXpNeededForNextLevel());
        // Potentially heal player or other benefits on level up
    }

    public void activateAbility(Ability ability) {
        Gdx.app.log("Player", "Activating ability: " + ability.name());
        activeEffects.add(new ActiveAbilityEffect(ability));
        switch (ability) {
            case VITALITY: this.maxHp += 1; this.currentHp += 1; break;
            case PROCREASE: if (currentWeapon != null) currentWeapon.increaseProjectiles(1); break;
            case AMOCREASE: if (currentWeapon != null) currentWeapon.increaseMaxAmmo(5); break;
            default: break; // DAMAGER and SPEEDY are handled by getEffective...
        }
    }

    public float getEffectiveSpeed() {
        float speed = this.characterType.getBaseSpeed();
        for (ActiveAbilityEffect effect : activeEffects) {
            if (effect.ability == Ability.SPEEDY && effect.durationTimer > 0) speed *= 2;
        }
        return speed;
    }

    public int getEffectiveDamage(int baseDamage) {
        int damage = baseDamage;
        for (ActiveAbilityEffect effect : activeEffects) {
            if (effect.ability == Ability.DAMAGER && effect.durationTimer > 0) damage *= 1.25f;
        }
        return damage;
    }

    public void reloadWeapon() { if (currentWeapon != null) currentWeapon.reload(); }
    public void resetForNewGame() {
        this.maxHp = characterType.getBaseHp(); // Reset to base, VITALITY will be re-applied if chosen
        this.currentHp = this.maxHp;
        this.currentSpeed = characterType.getBaseSpeed();
        this.xp = 0; this.level = 1; this.invincibilityTimer = 0;
        this.position.set(400, 300);
        if (currentWeapon != null) currentWeapon.resetToInitialStats();
        this.activeEffects.clear();
        Gdx.app.log("Player", "Player reset for new game.");
    }

    public Vector2 getPosition() { return position; }
    public TextureRegion getCurrentFrame() { return currentFrame; }
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getXp() { return xp; }
    public int getLevel() { return level; }
    public Weapon getCurrentWeapon() { return currentWeapon; }
    public User getUser() { return user; }
    public CharacterType getCharacterType() { return characterType; }
    public boolean isInvincible() { return invincibilityTimer > 0; }
    public int getXpNeededForNextLevel() { return 20 * level; }
    public void setCurrentHp(int hp) { this.currentHp = Math.max(0, Math.min(hp, this.maxHp)); }
    public Vector2 getAimDirection() { return aimDirection; }
}
