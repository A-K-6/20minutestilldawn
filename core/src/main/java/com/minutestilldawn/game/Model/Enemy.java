package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private Vector2 position;
    private int hp;
    private float speed;
    private TextureRegion texture; // For simplicity, assume one texture for now

    public Enemy(float x, float y, int hp, float speed, TextureRegion texture) {
        this.position = new Vector2(x, y);
        this.hp = hp;
        this.speed = speed;
        this.texture = texture;
    }

    public void update(float delta, Vector2 targetPosition) {
        // Move towards target (player) [cite: 65]
        Vector2 direction = new Vector2(targetPosition).sub(position).nor();
        position.add(direction.scl(speed * delta));
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            Gdx.app.log("Enemy", "Enemy defeated!");
            // Handle enemy death (e.g., drop seed[cite: 67], play animation [cite: 100])
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHp() {
        return hp;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}