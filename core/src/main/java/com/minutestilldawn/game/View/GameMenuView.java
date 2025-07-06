package com.minutestilldawn.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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
    private final TextButton pauseButton;
    private final Table hudTable;

    // Textures
    private final Texture mapTileTexture;

    // Pause menu overlay
    private PauseMenuView pauseMenuView;
    private boolean pauseMenuShown = false;

    private Table overlayTable;

    // UI elements
    private Table userInfoTable;

    public GameMenuView(Main gameInstance, GameState gameState, GameAssetManager assetManager, Skin skin) {
        super(skin);
        this.gameInstance = gameInstance;
        this.gameState = gameState;
        this.assetManager = assetManager;
        this.batch = Main.getBatch();

        // Camera and viewport
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(1280, 720, gameCamera);
        gameCamera.setToOrtho(false, 1280, 720);

        shapeRenderer = new ShapeRenderer();

        // Load textures
        mapTileTexture = new Texture(Gdx.files.internal("pictures/MapTile.png"));
        mapTileTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // HUD
        this.hudStage = new Stage(new FitViewport(1280, 720));
        this.timerLabel = new Label("00:00", skin);
        this.statsLabel = new Label("", skin);
        this.pauseButton = new TextButton("II", skin);
        this.hudTable = new Table();
        this.hudTable.setFillParent(true);
        // Top bar: timer (left), pause (right)
        Table topRow = new Table();
        topRow.top().left();
        topRow.add(timerLabel).left().padLeft(16).padTop(8);
        topRow.add().expandX();
        topRow.add(pauseButton).right().padRight(16).padTop(8).size(36, 36);
        hudTable.add(topRow).expandX().fillX().row();
        // Stats label below
        hudTable.add(statsLabel).left().padLeft(16).padTop(5).row();
        hudStage.addActor(hudTable);

        // Pause button logic (fix size, always clickable)

        userInfoTable = new Table(skin);
        userInfoTable.setTransform(true); // Enable transformations for scaling
        userInfoTable.setScale(0.5f); // Apply a scale to the entire table and its contents
        userInfoTable.top().left();
        updateUserInfoTable(); // Fill content

        overlayTable = new Table();
        overlayTable.setFillParent(true);
        overlayTable.top().left();
        // Add userInfoTable to overlayTable with padding to position it in the top-left
        // The expand().top().left() ensures it's pushed to the corner within the overlayTable
        overlayTable.add(userInfoTable).padTop(10).padLeft(10).expand().top().left(); 
        
        hudStage.addActor(overlayTable);

        pauseButton.addListener(event -> {
                if (gameState.getCurrentStatus() == GameStatus.PLAYING) {
                    gameState.setCurrentStatus(GameStatus.PAUSED);
                    pauseMenuShown = false;
                }
                return false; 
        });

        // --- GameController creation (with camera) ---
        this.gameController = new GameController(gameInstance, gameState, assetManager, gameCamera);
        Gdx.input.setInputProcessor(gameController);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameViewport.update(width, height, true);
        hudStage.getViewport().update(width, height, true);
        // Removed manual positioning of userInfoTable here.
        // It's now handled by the overlayTable's layout constraints and userInfoTable's scale.
    }

    private void updateUserInfoTable() {
        userInfoTable.clear();

        Player player = gameState.getPlayerInstance();
        if (player == null)
            return;

        User user = player.getUser();
        boolean isGuest = (user == null);
        String usernameText = isGuest ? "Guest" : user.getUsername();
        String avatarPath = isGuest ? GameAssetManager.Default_Avatar : "avatars/avatar" + user.getAvatarId() + ".png";
        Texture avatarTexture = assetManager.getAvatarTexture(avatarPath);

        if (avatarTexture != null) {
            com.badlogic.gdx.scenes.scene2d.ui.Image avatarImg = new com.badlogic.gdx.scenes.scene2d.ui.Image(
                    avatarTexture);
            avatarImg.setSize(12, 12); // Further reduced avatar size
            userInfoTable.add(avatarImg).colspan(2).padBottom(0).row(); // Reduced padding to 0
        }

        Label userLabel = new Label("User: " + usernameText, userInfoTable.getSkin());
        userLabel.setFontScale(0.7f); // Scale font for this label
        userInfoTable.add(userLabel).left().colspan(2).padBottom(0).row(); // Reduced padding to 0

        if (!isGuest && user != null) {
            Label scoreLabel = new Label("Score:", userInfoTable.getSkin());
            scoreLabel.setFontScale(0.7f); // Scale font for this label
            userInfoTable.add(scoreLabel).left().padBottom(0); // Reduced padding to 0
            Label highestScoreLabel = new Label(String.valueOf(user.getHighestScore()), userInfoTable.getSkin());
            highestScoreLabel.setFontScale(0.7f); // Scale font for this label
            userInfoTable.add(highestScoreLabel).right().padBottom(0).row(); // Reduced padding to 0

            Label killsLabel = new Label("Kills:", userInfoTable.getSkin());
            killsLabel.setFontScale(0.7f); // Scale font for this label
            userInfoTable.add(killsLabel).left().padBottom(0); // Reduced padding to 0
            Label totalKillsLabel = new Label(String.valueOf(user.getTotalKills()), userInfoTable.getSkin());
            totalKillsLabel.setFontScale(0.7f); // Scale font for this label
            userInfoTable.add(totalKillsLabel).right().padBottom(0).row(); // Reduced padding to 0
        }

        Label weaponLabel = new Label("Weapon:", userInfoTable.getSkin());
        weaponLabel.setFontScale(0.7f); // Scale font for this label
        userInfoTable.add(weaponLabel).left().padBottom(0); // Reduced padding to 0
        Label currentWeaponLabel = new Label(
                player.getCurrentWeapon() != null ? player.getCurrentWeapon().getName() : "-",
                userInfoTable.getSkin());
        currentWeaponLabel.setFontScale(0.7f); // Scale font for this label
        userInfoTable.add(currentWeaponLabel).right().padBottom(0).row(); // Reduced padding to 0

        Label ammoLabel = new Label("Ammo:", userInfoTable.getSkin());
        ammoLabel.setFontScale(0.7f); // Scale font for this label
        userInfoTable.add(ammoLabel).left().padBottom(0); // Reduced padding to 0
        Label currentAmmoLabel = new Label(
                player.getCurrentWeapon() != null
                        ? (player.getCurrentWeapon().getCurrentAmmo() + "/" + player.getCurrentWeapon().getMaxAmmo())
                        : "-",
                userInfoTable.getSkin());
        currentAmmoLabel.setFontScale(0.7f); // Scale font for this label
        userInfoTable.add(currentAmmoLabel).right().padBottom(0).row(); // Reduced padding to 0

        Label xpLabel = new Label("XP:", userInfoTable.getSkin());
        xpLabel.setFontScale(0.7f); // Scale font for this label
        userInfoTable.add(xpLabel).left().padBottom(0); // Reduced padding to 0
        Label currentXpLabel = new Label(
                player.getXp() + "/" + player.getXpNeededForNextLevel(),
                userInfoTable.getSkin());
        currentXpLabel.setFontScale(0.7f); // Scale font for this label
        userInfoTable.add(currentXpLabel).right().padBottom(0).row(); // Reduced padding to 0
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // Only update timer/game state if not paused
        if (gameState.getCurrentStatus() != GameStatus.PAUSED) {
            gameState.update(delta);
        }
        float elapsedSeconds = gameState.getElapsedTimeSeconds();
        timerLabel.setText(getFormattedTime(elapsedSeconds));
        updateUserInfoTable(); // Call updateUserInfoTable in render to ensure it's always up-to-date
        // Pause menu logic
        if (gameState.getCurrentStatus() == GameStatus.PAUSED) {
            if (!pauseMenuShown) {
                if (pauseMenuView == null) {
                    pauseMenuView = new PauseMenuView(new PauseMenuController(gameInstance, gameController) {
                        @Override
                        public void onButtonClicked(String buttonId) {
                            super.onButtonClicked(buttonId);
                            if ("resume".equals(buttonId)) {
                                gameState.setCurrentStatus(GameStatus.PLAYING);
                                Gdx.input.setInputProcessor(gameController);
                            }
                        }
                    }, skin);
                }
                hudStage.addActor(pauseMenuView.getTable());
                pauseMenuShown = true;
                Gdx.input.setInputProcessor(hudStage);
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
                    batch.draw(gunTexture, px - gw / 2, py - gh / 2, gw / 2, gh / 2, gw, gh, 1, 1, angle, 0, 0,
                            (int) gw, (int) gh, false, false);
                }
            }
            batch.end();
            // Draw HUD stage (timer, stats, pause menu, user info)
            hudStage.getViewport().apply();
            hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            hudStage.draw();
            drawHealthBar();
            return;
        } else if (pauseMenuShown) {
            if (pauseMenuView != null) {
                pauseMenuView.getTable().remove();
            }
            pauseMenuShown = false;
            Gdx.input.setInputProcessor(gameController);
        }

        // Handle ESC key for pause/resume
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            if (gameState.getCurrentStatus() == GameStatus.PLAYING) {
                gameState.setCurrentStatus(GameStatus.PAUSED);
                pauseMenuShown = false;
            } else if (gameState.getCurrentStatus() == GameStatus.PAUSED) {
                gameState.setCurrentStatus(GameStatus.PLAYING);
                Gdx.input.setInputProcessor(gameController);
            }
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
                batch.draw(gunTexture, px - gw / 2, py - gh / 2, gw / 2, gh / 2, gw, gh, 1, 1, angle, 0, 0, (int) gw,
                        (int) gh, false, false);
            }
        }
        batch.end();

        // Draw HUD stage (timer, stats, user info box)
        hudStage.getViewport().apply();
        hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hudStage.draw();
        drawHealthBar(); // HP bar at the bottom
    }

    private String getFormattedTime(float elapsedSeconds) {
        int totalSeconds = (int) elapsedSeconds;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void drawHealthBar() {
        Player player = gameState.getPlayerInstance();
        shapeRenderer.setProjectionMatrix(hudStage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        float hpPercent = player.getCurrentHp() / (float) player.getMaxHp();
        float barWidth = Math.min(hudStage.getViewport().getWorldWidth() * 0.5f, 600f);
        float barX = (hudStage.getViewport().getWorldWidth() - barWidth) / 2f;
        float barY = 10f;
        shapeRenderer.rect(barX, barY, barWidth * hpPercent, 24);
        shapeRenderer.end();
    }

    private void drawBackground() {
        int mapSize = 2048;
        batch.draw(mapTileTexture, -mapSize, -mapSize, mapSize * 2, mapSize * 2);
    }

    private void drawGameElements() {
        Player player = gameState.getPlayerInstance();
        if (player != null)
            player.draw(batch);

        for (Tree tree : gameController.getTrees()) {
            if (tree != null)
                tree.draw(batch);
        }

        for (Enemy enemy : gameController.getActiveEnemies()) { // Changed from getActiveEnemies() to getEnemies() to ensure all enemies are drawn
            if (enemy != null)
                enemy.draw(batch);
        }

        for (Bullet bullet : gameController.getActivePlayerBullets()) {
            if (bullet != null)
                bullet.draw(batch);
        }

        for (Bullet bullet : gameController.getActiveEnemyBullets()) {
            if (bullet != null)
                bullet.draw(batch);
        }
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
