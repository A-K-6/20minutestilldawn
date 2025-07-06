package com.minutestilldawn.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minutestilldawn.game.Controller.GameMenuController;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Controller.GameController;
import com.minutestilldawn.game.Controller.PauseMenuController;
import com.minutestilldawn.game.Model.*;

public class GameMenuView extends BaseMenuView {

    private final Main gameInstance;
    private final GameState gameState;
    private final GameAssetManager assetManager;
    private final GameController gameController;

    private final SpriteBatch batch;
    private final OrthographicCamera gameCamera;
    private final Viewport gameViewport;
    private final ShapeRenderer shapeRenderer;

    // HUD elements
    private final Stage hudStage;
    private final Label timerLabel;
    private final Label statsLabel;

    // Textures
    private final Texture mapTileTexture;

    // Pause menu overlay
    private PauseMenuView pauseMenuView;
    private boolean pauseMenuShown = false;

    // Font for HUD
    private final BitmapFont hudFont = new BitmapFont();

    public GameMenuView(GameMenuController controller, Skin skin) {
        super(skin);
        this.gameInstance = Main.getInstance();
        this.gameState = gameInstance.getCurrentGameState();
        this.assetManager = gameInstance.getAssetManager();
        this.batch = Main.getBatch();

        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), gameCamera);
        gameCamera.setToOrtho(false, gameViewport.getWorldWidth(), gameViewport.getWorldHeight());

        this.gameController = new GameController(gameInstance, gameState, assetManager, gameCamera);
        Gdx.input.setInputProcessor(gameController);

        shapeRenderer = new ShapeRenderer();

        // HUD
        hudStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        timerLabel = new Label("", skin);
        statsLabel = new Label("", skin);
        table.top().left();
        table.add(timerLabel).pad(10).row();
        table.add(statsLabel).pad(10);
        hudStage.addActor(table);

        // Load textures
        mapTileTexture = new Texture(Gdx.files.internal("pictures/MapTile.png"));
        mapTileTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // Pause menu logic
        if (gameState.getCurrentStatus() == GameStatus.PAUSED) {
            if (!pauseMenuShown) {
                if (pauseMenuView == null) {
                    // Pass gameController to PauseMenuController for resume
                    pauseMenuView = new PauseMenuView(new PauseMenuController(gameInstance, gameController), skin);
                }
                hudStage.addActor(pauseMenuView.getTable());
                pauseMenuShown = true;
            }
            // Draw game world faded out
            batch.setProjectionMatrix(gameCamera.combined);
            batch.begin();
            drawBackground();
            drawGameElements();
            // Draw gun in player's hand if possible
            Player player = gameState.getPlayerInstance();
            if (player != null && player.getCurrentWeapon() != null) {
                Texture gunTexture = null;
                String weaponName = player.getCurrentWeapon().getName();
                if (weaponName.equalsIgnoreCase("Revolver")) {
                    gunTexture = assetManager.getRevolver();
                } else if (weaponName.equalsIgnoreCase("Shotgun")) {
                    gunTexture = assetManager.getShotgun();
                } else if (weaponName.equalsIgnoreCase("Dual SMGs")) {
                    gunTexture = assetManager.getSMGDual();
                }
                if (gunTexture != null) {
                    float px = player.getPosition().x;
                    float py = player.getPosition().y;
                    float gw = gunTexture.getWidth();
                    float gh = gunTexture.getHeight();
                    float angle = player.getAimDirection().angleDeg();
                    batch.draw(gunTexture, px - gw/2, py - gh/2, gw/2, gh/2, gw, gh, 1, 1, angle, 0, 0, (int)gw, (int)gh, false, false);
                }
            }
            // Draw user info HUD (avatar, username, stats)
            drawUserInfoHUD();
            batch.end();
            // Draw HUD stage (timer, stats, pause menu)
            hudStage.getViewport().apply();
            hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            hudStage.draw();
            // Draw health bar
            drawHealthBar();
            return;
        } else if (pauseMenuShown) {
            if (pauseMenuView != null) {
                pauseMenuView.getTable().remove();
            }
            pauseMenuShown = false;
        }

        gameController.update(delta);

        // Center camera on player
        Player player = gameState.getPlayerInstance();
        if (player != null) {
            gameCamera.position.set(player.getPosition().x, player.getPosition().y, 0);
        }
        gameCamera.update();

        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        drawBackground();
        drawGameElements();
        // Draw gun in player's hand if possible
        if (player != null && player.getCurrentWeapon() != null) {
            Texture gunTexture = null;
            String weaponName = player.getCurrentWeapon().getName();
            if (weaponName.equalsIgnoreCase("Revolver")) {
                gunTexture = assetManager.getRevolver();
            } else if (weaponName.equalsIgnoreCase("Shotgun")) {
                gunTexture = assetManager.getShotgun();
            } else if (weaponName.equalsIgnoreCase("Dual SMGs")) {
                gunTexture = assetManager.getSMGDual();
            }
            if (gunTexture != null) {
                float px = player.getPosition().x;
                float py = player.getPosition().y;
                float gw = gunTexture.getWidth();
                float gh = gunTexture.getHeight();
                float angle = player.getAimDirection().angleDeg();
                batch.draw(gunTexture, px - gw/2, py - gh/2, gw/2, gh/2, gw, gh, 1, 1, angle, 0, 0, (int)gw, (int)gh, false, false);
            }
        }
        // Draw user info HUD (avatar, username, stats)
        drawUserInfoHUD();
        batch.end();

        drawHUD(); // Always draw HUD last
    }

    private void drawUserInfoHUD() {
        Player player = gameState.getPlayerInstance();
        User user = player.getUser();
        boolean isGuest = (user == null);
        String usernameText = isGuest ? "Guest" : user.getUsername();
        String avatarPath = isGuest ? GameAssetManager.Default_Avatar : "avatars/avatar" + user.getAvatarId() + ".png";
        Texture avatarTexture = assetManager.getAvatarTexture(avatarPath);
        if (avatarTexture != null) {
            batch.draw(avatarTexture, 10, Gdx.graphics.getHeight() - 110, 96, 96);
        }
        hudFont.setColor(Color.WHITE);
        hudFont.getData().setScale(1.1f);
        hudFont.draw(batch, usernameText, 110, Gdx.graphics.getHeight() - 30);
        if (!isGuest && user != null) {
            String stats = "Highest Score: " + user.getHighestScore() +
                    "\nTotal Kills: " + user.getTotalKills() +
                    "\nLongest Survival: " + String.format("%.1f s", user.getLongestSurvivalTimeSeconds());
            hudFont.getData().setScale(0.9f);
            hudFont.draw(batch, stats, 110, Gdx.graphics.getHeight() - 60);
        }
    }

    private void drawHealthBar() {
        Player player = gameState.getPlayerInstance();
        shapeRenderer.setProjectionMatrix(hudStage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        float hpPercent = player.getCurrentHp() / (float) player.getMaxHp();
        shapeRenderer.rect(10, Gdx.graphics.getHeight() - 50, (Gdx.graphics.getWidth() - 20) * hpPercent, 20);
        shapeRenderer.end();
    }

    private void drawBackground() {
        int mapSize = 2048;
        batch.draw(mapTileTexture, -mapSize, -mapSize, mapSize * 2, mapSize * 2);
    }

    private void drawGameElements() {
        Player player = gameState.getPlayerInstance();
        if (player != null) player.draw(batch);

        for (Tree tree : gameController.getTrees()) {
            if (tree != null) tree.draw(batch);
        }

        for (Enemy enemy : gameController.getActiveEnemies()) {
            if (enemy != null) enemy.draw(batch);
        }

        for (Bullet bullet : gameController.getActivePlayerBullets()) {
            if (bullet != null) bullet.draw(batch);
        }

        for (Bullet bullet : gameController.getActiveEnemyBullets()) {
            if (bullet != null) bullet.draw(batch);
        }
    }

    private void drawHUD() {
        // Improved HUD: timer, HP, XP, kills, weapon, ammo
        Player player = gameState.getPlayerInstance();
        int minutes = (int) gameState.getTimeRemainingSeconds() / 60;
        int seconds = (int) gameState.getTimeRemainingSeconds() % 60;
        String weaponName = player.getCurrentWeapon() != null ? player.getCurrentWeapon().getName() : "-";
        int ammo = player.getCurrentWeapon() != null ? player.getCurrentWeapon().getCurrentAmmo() : 0;
        int maxAmmo = player.getCurrentWeapon() != null ? player.getCurrentWeapon().getMaxAmmo() : 0;
        timerLabel.setText("Time: " + String.format("%02d:%02d", minutes, seconds));
        statsLabel.setText(
                "HP: " + player.getCurrentHp() + "/" + player.getMaxHp() +
                " | XP: " + player.getXp() + "/" + player.getXpNeededForNextLevel() +
                " | Kills: " + gameState.getKills() +
                " | Weapon: " + weaponName +
                " | Ammo: " + ammo + "/" + maxAmmo
        );

        // --- USER INFO HUD ---
        User user = player.getUser();
        boolean isGuest = (user == null);
        String usernameText = isGuest ? "Guest" : user.getUsername();
        String avatarPath = isGuest ? GameAssetManager.Default_Avatar : "avatars/avatar" + user.getAvatarId() + ".png";
        Texture avatarTexture = assetManager.getAvatarTexture(avatarPath);
        batch.begin();
        if (avatarTexture != null) {
            batch.draw(avatarTexture, 10, Gdx.graphics.getHeight() - 110, 96, 96);
        }
        hudFont.setColor(Color.WHITE);
        hudFont.getData().setScale(1.1f);
        hudFont.draw(batch, usernameText, 110, Gdx.graphics.getHeight() - 30);
        if (!isGuest && user != null) {
            String stats = "Highest Score: " + user.getHighestScore() +
                    "\nTotal Kills: " + user.getTotalKills() +
                    "\nLongest Survival: " + String.format("%.1f s", user.getLongestSurvivalTimeSeconds());
            hudFont.getData().setScale(0.9f);
            hudFont.draw(batch, stats, 110, Gdx.graphics.getHeight() - 60);
        }
        batch.end();
        // --- END USER INFO HUD ---

        hudStage.getViewport().apply();
        hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hudStage.draw();

        // Health bar
        shapeRenderer.setProjectionMatrix(hudStage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        float hpPercent = player.getCurrentHp() / (float) player.getMaxHp();
        shapeRenderer.rect(10, Gdx.graphics.getHeight() - 50, (Gdx.graphics.getWidth() - 20) * hpPercent, 20);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameViewport.update(width, height, true);
        hudStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
        hudStage.dispose();
    }

    @Override
    protected void setupUI() {
        // UI is now handled by the HUD
    }
}