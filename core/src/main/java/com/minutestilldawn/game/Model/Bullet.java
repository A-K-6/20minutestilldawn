package com.minutestilldawn.game.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool; // For object pooling

public class Bullet implements Pool.Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private int damage;
    private Texture texture;
    private boolean active; // For pooling
    private boolean isPlayerBullet;
    private float speed = 500.0f; // Default speed, can be adjusted
    private Rectangle bounds; // For collision detection

    private static final float DEFAULT_WIDTH = 8f; // Example width
    private static final float DEFAULT_HEIGHT = 8f; // Example height


    // Public no-arg constructor for pooling
    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.bounds = new Rectangle();
        this.active = false;
    }

    /**
     * Initializes a bullet.
     * @param startX Starting X position.
     * @param startY Starting Y position.
     * @param directionNormalized Normalized direction vector for the bullet.
     * @param damage Damage the bullet will inflict.
     * @param texture TextureRegion for the bullet.
     * @param isPlayerBullet True if fired by player, false if by enemy.
     * @param bulletSpeed Speed of the bullet.
     */
    public void init(float startX, float startY, Vector2 directionNormalized, int damage, Texture texture, boolean isPlayerBullet, float bulletSpeed) {
        this.position.set(startX, startY);
        this.speed = bulletSpeed;
        this.velocity.set(directionNormalized).scl(this.speed);
        this.damage = damage;
        this.texture = texture;
        this.isPlayerBullet = isPlayerBullet;
        this.active = true;
        // Adjust width/height if texture is available and has meaningful size
        float width = (texture != null) ? texture.getWidth() : DEFAULT_WIDTH;
        float height = (texture != null) ? texture.getHeight() : DEFAULT_HEIGHT;
        this.bounds.set(startX - width / 2, startY - height / 2, width, height);
    }


    public void update(float delta) {
        if (!active) return;

        position.mulAdd(velocity, delta);
        bounds.setPosition(position.x - bounds.width / 2, position.y - bounds.height / 2);

        // Deactivate if out of a reasonable game area (e.g., screen bounds + margin)
        // This should be checked in GameController against camera view or world limits
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        if (texture != null && active) {
            float width = texture.getWidth();
            float height = texture.getHeight();
            batch.draw(texture, position.x - width / 2, position.y - height / 2);
        }
    }

    @Override
    public void reset() {
        position.setZero();
        velocity.setZero();
        texture = null;
        damage = 0;
        active = false;
        isPlayerBullet = false;
        speed = 500.0f;
        bounds.set(0,0,0,0);
    }
}
