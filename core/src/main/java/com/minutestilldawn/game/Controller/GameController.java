package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.minutestilldawn.game.Model.CharacterType; // To create player
import com.minutestilldawn.game.Model.GameAssetManager;
import com.minutestilldawn.game.Model.GameState;
import com.minutestilldawn.game.Model.Player;
import com.minutestilldawn.game.Model.Weapon;
import com.minutestilldawn.game.Model.Bullet; // To manage bullets
import com.minutestilldawn.game.Model.Enemy; // To manage enemies
import java.util.ArrayList;
import java.util.List;

public class GameController extends InputAdapter { // Extends InputAdapter for input events

    private GameAssetManager assetManager;
    private GameState gameState; 
    private Player player;
    private List<Bullet> bullets; // List to hold active bullets
    private List<Enemy> enemies; // List to hold active enemies

    public GameController(GameAssetManager assetManager, GameState gameState) {
        this.assetManager = assetManager;
        // Initialize player with a default character type and loaded atlas
        // Make sure "player_atlas" is loaded in your GameAssetManager
        this.gameState = gameState; 
        this.assetManager = assetManager; 
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();

        // Add some dummy enemies for testing movement/shooting
        enemies.add(new Enemy(100, 100, 25, 50, assetManager.getPixthulhuSkin().getRegion("player_idle"))); // Example texture
        enemies.add(new Enemy(700, 500, 25, 50, assetManager.getPixthulhuSkin().getRegion("player_idle"))); // Example texture
    }

    /**
     * Called every frame to update game logic.
     * @param delta The time in seconds since the last frame.
     */
    public void update(float delta) {
        // --- Handle Player Movement (Polling) --- [cite: 60]
        Vector2 movementDirection = new Vector2();
        if (Gdx.input.isKeyPressed(Keys.W)) {
            movementDirection.y += 1;
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            movementDirection.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Keys.A)) {
            movementDirection.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            movementDirection.x += 1;
        }

        // Normalize movement to prevent faster diagonal movement [cite: 60]
        if (movementDirection.len() > 0) {
            player.move(movementDirection.nor(), delta);
        }

        // --- Update Player ---
        player.update(delta);

        // --- Update Bullets ---
        // Iterate backwards to safely remove bullets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(delta);
            // Basic boundary check (remove if out of screen)
            if (bullet.getPosition().x < 0 || bullet.getPosition().x > Gdx.graphics.getWidth() ||
                bullet.getPosition().y < 0 || bullet.getPosition().y > Gdx.graphics.getHeight()) {
                bullets.remove(i);
            }
            // Add collision detection with enemies here later
        }

        // --- Update Enemies --- [cite: 65]
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta, player.getPosition()); // Enemies move towards player
            // Basic collision with player (for damage) [cite: 79]
            // (You'll need more accurate collision detection for actual game)
            if (player.getPosition().dst(enemy.getPosition()) < 30) { // Simple distance check
                player.takeDamage(1); // Example damage
                // Handle enemy death if HP drops to 0 after collision (e.g. from player's touch)
            }
        }
        // Add enemy spawning logic here [cite: 64, 67]
        // Add bullet-enemy collision detection here [cite: 66]

        // --- Game State Checks ---
        // Check for game over (player HP <= 0) [cite: 87]
        // Check for game win (time runs out) [cite: 87]
    }

    // --- Event-Driven Input Handling ---
    @Override
    public boolean keyDown(int keycode) {
        // Reload weapon on R key [cite: 63]
        if (keycode == Keys.R) {
            player.reload();
            return true;
        }
        // Auto-aim toggle on Space (optional) [cite: 62]
        if (keycode == Keys.SPACE) {
            // Toggle auto-aim feature
            Gdx.app.log("GameController", "Auto-aim toggle (not implemented yet)");
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Shoot with left mouse click [cite: 61]
        if (button == Buttons.LEFT) {
            player.shoot(screenX, screenY); // Player attempts to shoot
            // If player shoots, create a bullet instance and add to bullets list
            if (player.getCurrentWeapon().canShoot() && !player.getCurrentWeapon().isReloading()) {
                 // Example: create bullet from player position towards mouse
                 Bullet newBullet = new Bullet(
                     player.getPosition().x,
                     player.getPosition().y,
                     screenX, screenY,
                     player.getCurrentWeapon().getDamage(),
                     assetManager.getPixthulhuSkin().getRegion("pixel") // Placeholder bullet texture
                 );
                 bullets.add(newBullet);
            }
            return true;
        }
        return false;
    }

    // --- Getters for views to draw ---
    public Player getPlayer() {
        return player;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}