package com.minutestilldawn.game.Model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

// Simple static obstacle
public class Tree {
    private Vector2 position;
    private Texture texture;
    private Rectangle bounds;
    public static final int DAMAGE_ON_CONTACT = 1; // Example damage

    public Tree(float x, float y, Texture texture) {
        this.position = new Vector2(x, y);
        this.texture = texture;
        float width = (texture != null) ? texture.getWidth() : 32f;
        float height = (texture != null) ? texture.getHeight() : 64f; // Trees are often taller
        this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
    }

    public Vector2 getPosition() { return position; }
    public Texture getTexture() { return texture; }
    public Rectangle getBounds() { return bounds; }

    public void draw(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        if (texture != null) {
            float width = texture.getWidth();
            float height = texture.getHeight();
            batch.draw(texture, position.x - width / 2, position.y - height / 2);
        }
    }
}
