package com.minutestilldawn.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minutestilldawn.game.Controller.GameMenuController;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Controller.GameController;
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
    private final TextureRegion treeTexture;

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
        mapTileTexture = assetManager.get(GameAssetManager.MAP_TILE_TEXTURE, Texture.class);
        mapTileTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        treeTexture = assetManager.getTreeTexture();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
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
        batch.end();

        drawHUD(); // Always draw HUD last
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
        int minutes = (int) gameState.getTimeRemainingSeconds() / 60;
        int seconds = (int) gameState.getTimeRemainingSeconds() % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

        Player player = gameState.getPlayerInstance();
        statsLabel.setText("HP: " + player.getCurrentHp() + "/" + player.getMaxHp() +
                " | XP: " + player.getXp() + "/" + player.getXpNeededForNextLevel() +
                " | Kills: " + gameState.getKills());

        hudStage.getViewport().apply();
        hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hudStage.draw();

        // Health bar
        shapeRenderer.setProjectionMatrix(hudStage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(10, Gdx.graphics.getHeight() - 50, (Gdx.graphics.getWidth() - 20) * (player.getCurrentHp() / (float) player.getMaxHp()), 20);
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