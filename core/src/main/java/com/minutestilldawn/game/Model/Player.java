package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array; // For managing active abilities/effects

public class Player {
    private User user; // Can be null if guest
    private CharacterType characterType;
    private Vector2 position;
    private float currentSpeed; // Can be modified by abilities
    private int maxHp;
    private int currentHp;
    private int xp;
    private int level;
    private Weapon currentWeapon;

    // Visuals
    private TextureAtlas playerAtlas;
    private TextureRegion currentFrame;

    // Game mechanics
    private float invincibilityTimer;
    private static final float INVINCIBILITY_DURATION = 1.0f; // 1 second invincibility
    // Active ability effects
    // Example: A temporary speed boost or damage boost

    private static class ActiveAbilityEffect {
        Ability ability;
        float durationTimer; // For timed abilities like DAMAGER or SPEEDY

        ActiveAbilityEffect(Ability ability) {
            this.ability = ability;
            switch (ability) {
                case DAMAGER:
                case SPEEDY:
                    this.durationTimer = 10.0f; // 10 seconds duration as per PDF
                    break;
                default:
                    this.durationTimer = -1; // Not timed / permanent until changed
                    break;
            }
        }
    }

    private Array<ActiveAbilityEffect> activeEffects;

    public Player(User user, CharacterType type, TextureAtlas atlas, Weapon startingWeapon) {
        this.user = user;
        this.characterType = type;
        this.playerAtlas = atlas;
        this.currentWeapon = startingWeapon;
        this.activeEffects = new Array<>();

        // Initialize stats based on CharacterType
        this.maxHp = type.getBaseHp();
        this.currentHp = this.maxHp;
        this.currentSpeed = type.getBaseSpeed();

        this.position = new Vector2(400, 300); // Default starting position
        this.xp = 0;
        this.level = 1;
        this.invincibilityTimer = 0;

        // Set initial frame
        this.currentFrame = playerAtlas.findRegion("player_idle"); // TODO: Ensure this region exists
        if (this.currentFrame == null) {
            Gdx.app.error("Player",
                    "Player texture region 'player_idle' not found in atlas! Using first region as fallback.");
            if (playerAtlas.getRegions().size > 0) {
                this.currentFrame = playerAtlas.getRegions().first();
            } else {
                Gdx.app.error("Player", "Player atlas has no regions!");
                // You might want to throw an exception or use a placeholder here
            }
        }
    }

    public void update(float delta) {
        // Update invincibility
        if (invincibilityTimer > 0) {
            invincibilityTimer -= delta;
            if (invincibilityTimer < 0)
                invincibilityTimer = 0;
        }

        // Update current weapon state
        if (currentWeapon != null) {
            currentWeapon.update(delta);
        }

        // Update active ability effects
        for (int i = activeEffects.size - 1; i >= 0; i--) {
            ActiveAbilityEffect effect = activeEffects.get(i);
            if (effect.durationTimer > 0) {
                effect.durationTimer -= delta;
                if (effect.durationTimer <= 0) {
                    Gdx.app.log("Player", "Ability effect " + effect.ability.name() + " expired.");
                    deactivateAbilityEffect(effect.ability); // Method to revert changes
                    activeEffects.removeIndex(i);
                }
            }
        }
    }

    public void move(Vector2 direction, float delta) {
        if (direction.len() > 0) { // Normalize to prevent faster diagonal movement
            position.add(direction.x * currentSpeed * delta, direction.y * currentSpeed * delta);
        }
        // TODO: Add boundary checks to keep player within game area if necessary
    }

    public void takeDamage(int damage) {
        if (invincibilityTimer <= 0) {
            currentHp -= damage;
            Gdx.app.log("Player", "Player took " + damage + " damage. HP: " + currentHp + "/" + maxHp);
            if (currentHp <= 0) {
                currentHp = 0;
                Gdx.app.log("Player", "Player defeated!");
                // Game over state will be set by GameController/GameState
            } else {
                invincibilityTimer = INVINCIBILITY_DURATION;
                Gdx.app.log("Player", "Player invincible for " + INVINCIBILITY_DURATION + "s.");
            }
            // TODO: Trigger damage animation/sound
        }
    }

    public void gainXP(int amount) {
        if (currentHp <= 0)
            return; // No XP if dead

        xp += amount;
        Gdx.app.log("Player", "Gained " + amount + " XP. Total XP: " + xp + "/" + getXpNeededForNextLevel());
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (xp >= getXpNeededForNextLevel() && level < 100) { // Cap level for sanity
            int xpForLevel = getXpNeededForNextLevel();
            levelUp();
            xp -= xpForLevel; // Carry over extra XP
            Gdx.app.log("Player", "XP after level up: " + xp + "/" + getXpNeededForNextLevel());
        }
    }

    private void levelUp() {
        level++;
        Gdx.app.log("Player", "LEVEL UP! New level: " + level);
        // GameState/GameController will handle presenting ability choices.
        // Player might get some base stat increase or full heal on level up if desired.
        // For now, just increasing level.
        // TODO: Play level up animation/sound
    }

    /**
     * Applies a chosen ability. This is called by GameController after player makes
     * a choice.
     * 
     * @param ability The ability to activate.
     */
    public void activateAbility(Ability ability) {
        Gdx.app.log("Player", "Activating ability: " + ability.name());
        activeEffects.add(new ActiveAbilityEffect(ability));

        switch (ability) {
            case VITALITY:
                this.maxHp += 1; // As per PDF
                this.currentHp += 1; // Also heal by 1, or fully heal? PDF says "increase max HP"
                Gdx.app.log("Player", "Max HP increased to " + maxHp);
                break;
            case DAMAGER:
                // Effect applied via getEffectiveDamage()
                Gdx.app.log("Player", "Damage boost active for 10s.");
                break;
            case PROCREASE:
                if (currentWeapon != null) {
                    currentWeapon.increaseProjectiles(1); // PDF: "Increase weapon projectile by 1 unit"
                }
                break;
            case AMOCREASE:
                if (currentWeapon != null) {
                    currentWeapon.increaseMaxAmmo(5); // PDF: "Increase max weapon ammo by 5 units"
                }
                break;
            case SPEEDY:
                // Effect applied via getEffectiveSpeed()
                Gdx.app.log("Player", "Speed boost active for 10s.");
                break;
        }
    }

    /**
     * Reverts changes made by an ability when its duration expires.
     */
    private void deactivateAbilityEffect(Ability ability) {
        Gdx.app.log("Player", "Deactivating ability effect: " + ability.name());
        // For DAMAGER and SPEEDY, their effects are checked dynamically in
        // getEffectiveDamage/Speed.
        // For permanent changes like VITALITY, PROCREASE, AMOCREASE, they are not
        // "deactivated"
        // unless you implement a system for temporary stat buffs that revert.
        // The current implementation of VITALITY, PROCREASE, AMOCREASE are permanent
        // buffs for the game session.
    }

    public float getEffectiveSpeed() {
        float speed = this.characterType.getBaseSpeed(); // Start with base speed
        for (ActiveAbilityEffect effect : activeEffects) {
            if (effect.ability == Ability.SPEEDY && effect.durationTimer > 0) {
                speed *= 2; // PDF: "Double player movement speed"
            }
        }
        return speed;
    }

    public int getEffectiveDamage(int baseDamage) {
        int damage = baseDamage;
        for (ActiveAbilityEffect effect : activeEffects) {
            if (effect.ability == Ability.DAMAGER && effect.durationTimer > 0) {
                damage *= 1.25f; // PDF: "Increase 25% weapon damage"
            }
        }
        return damage;
    }

    public void shoot(Vector2 targetPosition, Array<Bullet> gameBullets, GameAssetManager assetManager) {
        if (currentWeapon == null || !currentWeapon.canShoot()) {
            if (currentWeapon != null && currentWeapon.getCurrentAmmo() <= 0 && !currentWeapon.isReloading()) {
                // Gdx.app.log("Player", "Out of ammo! Reloading automatically or waiting for
                // manual reload.");
                // Auto-reload logic could be here if GameState.isAutoReloadEnabled()
            }
            return;
        }

        // The weapon itself handles reducing ammo.
        // The weapon's damage is now passed to bullet, potentially modified by player's
        // DAMAGER ability.
        int bulletDamage = getEffectiveDamage(currentWeapon.getDamage());

        // Create bullets based on weapon's projectilesPerShot
        for (int i = 0; i < currentWeapon.getProjectilesPerShot(); i++) {
            // For multiple projectiles (like shotgun), you'd add spread logic here
            Vector2 bulletVelocity = new Vector2(targetPosition).sub(position).nor();
            float spreadAngle = 0;
            if (currentWeapon.getProjectilesPerShot() > 1) {
                // Example spread: -15 to +15 degrees for 3 projectiles, adjust as needed
                float totalSpreadArc = 30; // degrees
                spreadAngle = (i - (currentWeapon.getProjectilesPerShot() - 1) / 2.0f)
                        * (totalSpreadArc / (Math.max(1, currentWeapon.getProjectilesPerShot() - 1)));
                if (currentWeapon.getProjectilesPerShot() == 1)
                    spreadAngle = 0; // No spread for single projectile
            }
            bulletVelocity.rotateDeg(spreadAngle);

            // TODO: Get a proper bullet texture from assetManager
            TextureRegion bulletTexture = assetManager.getPixthulhuSkin().getRegion("pixel"); // Placeholder
            if (assetManager.getPixthulhuSkin().has("bullet", TextureRegion.class)) { // Attempt to get a more specific
                                                                                      // texture
                bulletTexture = assetManager.getPixthulhuSkin().getRegion("bullet");
            }

            Bullet newBullet = new Bullet(
                    position.x, position.y, // Start from player center
                    bulletVelocity, // Use the calculated velocity vector
                    bulletDamage,
                    bulletTexture,
                    true // isPlayerBullet = true
            );
            gameBullets.add(newBullet);
        }
        currentWeapon.shoot(); // This will decrement ammo
        // Gdx.app.log("Player", "Player shot. Weapon: " + currentWeapon.getName() + ",
        // Ammo: " + currentWeapon.getCurrentAmmo());
        // TODO: Play shoot sound effect
    }

    public void reloadWeapon() {
        if (currentWeapon != null) {
            currentWeapon.reload();
        }
    }

    /**
     * Resets player stats for a new game.
     */
    public void resetForNewGame() {
        this.currentHp = this.maxHp; // Full HP based on character type (and VITALITY if applied)
        this.currentSpeed = this.characterType.getBaseSpeed(); // Reset speed to base (SPEEDY will re-apply if active)
        this.xp = 0;
        this.level = 1;
        this.invincibilityTimer = 0;
        this.position.set(400, 300); // Reset position

        if (currentWeapon != null) {
            currentWeapon.resetAmmo(); // Full ammo for the weapon
            // Reset projectilesPerShot if it was modified by PROCREASE, or re-apply
            // PROCREASE
            // For simplicity, PROCREASE and AMOCREASE effects from previous games are not
            // carried over unless GameState is saved/loaded.
            // If starting a truly "new" game, weapon should be in its base state.
        }
        this.activeEffects.clear(); // Clear any lingering ability effects
        Gdx.app.log("Player", "Player reset for new game.");
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

    public User getUser() {
        return user;
    }

    public CharacterType getCharacterType() {
        return characterType;
    }

    public boolean isInvincible() {
        return invincibilityTimer > 0;
    }

    public int getXpNeededForNextLevel() {
        // PDF: "برای رفتن از لول i به لول i+1 نیازمند exp 20*i جدید است"
        // This means to go from level 1 to 2, you need 20*1 = 20 XP.
        // To go from level 2 to 3, you need 20*2 = 40 XP *additional to what got you to
        // level 2*.
        return 20 * level;
    }

    // --- Setters (use with caution, prefer methods that encapsulate logic) ---
    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void setCurrentHp(int hp) {
        this.currentHp = Math.max(0, Math.min(hp, this.maxHp));
    }
}