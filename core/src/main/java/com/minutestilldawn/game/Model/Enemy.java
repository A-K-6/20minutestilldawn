package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Array; // For Eyebat bullets

public class Enemy implements Pool.Poolable {
    public enum EnemyType {
        TENTACLE_MONSTER, EYEBAT, ELDER_BOSS
    }

    private Vector2 position;
    private Vector2 velocity;
    private int maxHp;
    private int currentHp;
    private float speed;
    private TextureRegion texture;
    private boolean active;
    private EnemyType type;
    private Rectangle bounds;

    // For Eyebat
    private float shootCooldown;
    private float shootTimer;
    private static final float EYEBAT_BULLET_SPEED = 300f;
    private static final int EYEBAT_BULLET_DAMAGE = 1; // As per PDF

    // For Elder Boss (basic dash mechanics)
    private float dashCooldown;
    private float dashTimer;
    private boolean isDashing;
    private Vector2 dashTarget;
    private static final float ELDER_DASH_SPEED_MULTIPLIER = 3.0f;
    private static final float ELDER_DASH_DURATION = 0.5f; // How long the dash itself lasts
    private float currentDashTime;

    public Enemy() {
        position = new Vector2();
        velocity = new Vector2();
        bounds = new Rectangle();
        dashTarget = new Vector2();
        active = false;
    }

    public void init(EnemyType type, float x, float y, TextureRegion texture, GameAssetManager assetManager) {
        this.type = type;
        this.position.set(x, y);
        this.texture = texture;
        this.active = true;
        this.isDashing = false;
        this.currentDashTime = 0f;

        switch (type) {
            case TENTACLE_MONSTER:
                this.maxHp = 25; // PDF
                this.speed = 60f; // Adjust as needed
                this.shootCooldown = -1; // Doesn't shoot
                break;
            case EYEBAT:
                this.maxHp = 50; // PDF
                this.speed = 40f; // Slower, as it shoots
                this.shootCooldown = 3.0f; // PDF: shoots every 3 seconds
                this.shootTimer = (float) (Math.random() * shootCooldown); // Randomize first shot
                break;
            case ELDER_BOSS:
                this.maxHp = 400; // PDF
                this.speed = 50f; // Normal speed
                this.dashCooldown = 5.0f; // PDF: dashes every 5 seconds
                this.dashTimer = dashCooldown;
                break;
        }
        this.currentHp = this.maxHp;
        float width = (texture != null) ? texture.getRegionWidth() : 32f;
        float height = (texture != null) ? texture.getRegionHeight() : 32f;
        this.bounds.set(x - width / 2, y - height / 2, width, height);
    }

    public void update(float delta, Vector2 playerPosition, Pool<Bullet> bulletPool, Array<Bullet> activeEnemyBullets,
            GameAssetManager assetManager) {
        if (!active)
            return;

        // Movement
        if (type == EnemyType.ELDER_BOSS && isDashing) {
            velocity.set(dashTarget).sub(position).nor().scl(speed * ELDER_DASH_SPEED_MULTIPLIER);
            currentDashTime += delta;
            if (currentDashTime >= ELDER_DASH_DURATION || position.dst(dashTarget) < 10f) {
                isDashing = false;
                dashTimer = dashCooldown; // Reset dash cooldown after dash finishes
            }
        } else {
            velocity.set(playerPosition).sub(position).nor().scl(speed);
        }
        position.mulAdd(velocity, delta);
        bounds.setPosition(position.x - bounds.width / 2, position.y - bounds.height / 2);

        // Shooting for Eyebat
        if (type == EnemyType.EYEBAT) {
            shootTimer -= delta;
            if (shootTimer <= 0) {
                shootTimer = shootCooldown;
                // Shoot a bullet towards the player
                Bullet bullet = bulletPool.obtain();
                Vector2 direction = new Vector2(playerPosition).sub(position).nor();
                // TODO: Get a proper enemy bullet texture
                TextureRegion bulletTexture = assetManager.getEnemyBulletTexture(type);
                bullet.init(position.x, position.y, direction, EYEBAT_BULLET_DAMAGE, bulletTexture, false,
                        EYEBAT_BULLET_SPEED);
                activeEnemyBullets.add(bullet);
            }
        }

        // Dashing for Elder Boss
        if (type == EnemyType.ELDER_BOSS && !isDashing) {
            dashTimer -= delta;
            if (dashTimer <= 0) {
                isDashing = true;
                dashTarget.set(playerPosition); // Target player's current position for the dash
                currentDashTime = 0f;
                Gdx.app.log("ElderBoss", "Starting dash towards " + dashTarget);
            }
        }
    }

    public void takeDamage(int damage) {
        if (!active)
            return;
        currentHp -= damage;
        Gdx.app.log(type.name(), "took " + damage + " damage. HP: " + currentHp + "/" + maxHp);
        if (currentHp <= 0) {
            active = false;
            Gdx.app.log(type.name(), "defeated!");
            // GameController will handle XP drop, etc.
        }
    }

    @Override
    public void reset() {
        position.setZero();
        velocity.setZero();
        texture = null;
        active = false;
        currentHp = 0;
        maxHp = 0;
        speed = 0;
        bounds.set(0, 0, 0, 0);
        shootTimer = 0;
        dashTimer = 0;
        isDashing = false;
        currentDashTime = 0f;
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public EnemyType getType() {
        return type;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }
}
