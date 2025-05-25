package com.minutestilldawn.game.Model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private int damage;
    private TextureRegion texture;
    private float speed = 500.0f; // Example speed

    public Bullet(float x, float y, float targetX, float targetY, int damage, TextureRegion texture) {
        this.position = new Vector2(x, y);
        this.damage = damage;
        this.texture = texture;

        // Calculate velocity towards target
        this.velocity = new Vector2(targetX - x, targetY - y).nor().scl(speed);
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
    }

    public Vector2 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public int getDamage() {
        return damage;
    }
}