package com.minutestilldawn.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.minutestilldawn.game.Main; // Import your Main game class
import com.minutestilldawn.game.Controller.GameController; // Import your GameController
import com.minutestilldawn.game.Model.Bullet;
import com.minutestilldawn.game.Model.Enemy;
import com.minutestilldawn.game.Model.GameAssetManager;
import com.minutestilldawn.game.Model.GameState;
import com.badlogic.gdx.scenes.scene2d.Stage; // For HUD

public class GameScreenView implements Screen {

    private SpriteBatch batch;
    private GameAssetManager assetManager;
    private GameState gameState; 
    private GameController gameController; // The game logic controller

    private OrthographicCamera camera;
    private FitViewport viewport;

    // Optional: for in-game HUD or pause menu (using Scene2D.UI)
    private Stage uiStage; // For drawing UI elements like HP bar, level, etc. [cite: 98]

    public GameScreenView(GameAssetManager assetManager,  GameState currentGameState, Main main) {
        this.assetManager = assetManager;
        this.batch = Main.getBatch(); // Use the shared batch from Main

        this.gameController = new GameController(assetManager, gameState); // Instantiate your GameController

        camera = new OrthographicCamera();
        // Set up camera to center on character [cite: 96]
        // For simplicity, let's start with fixed world size, player will be centered
        viewport = new FitViewport(800, 600, camera); // World units
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); // Center camera
        camera.update();

        // Initialize HUD stage
        // uiStage = new Stage(new ScreenViewport(), batch); // Pass the same batch
        // Add UI elements to uiStage here (HP bar, level, time, etc.) [cite: 98]
    }

    @Override
    public void show() {
        // Set input processor for game control
        InputMultiplexer multiplexer = new InputMultiplexer();
        // If you have a UI stage for HUD, add it first so it consumes events if needed
        // if (uiStage != null) multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(gameController); // Add your game controller for keyboard/mouse input
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a dark color
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1);

        // Update your game logic
        gameController.update(delta);

        // --- Update Camera Position ---
        // Center camera on player [cite: 96]
        camera.position.set(gameController.getPlayer().getPosition().x, gameController.getPlayer().getPosition().y, 0);
        camera.update(); // Important to update after changing position

        // Set up the batch for drawing game elements
        batch.setProjectionMatrix(camera.combined);

        // Begin drawing game world
        batch.begin();
        // Draw background (if scrolling)
        // Draw player [cite: 101]
        batch.draw(gameController.getPlayer().getCurrentFrame(),
                   gameController.getPlayer().getPosition().x - gameController.getPlayer().getCurrentFrame().getRegionWidth() / 2,
                   gameController.getPlayer().getPosition().y - gameController.getPlayer().getCurrentFrame().getRegionHeight() / 2);

        // Draw bullets [cite: 97]
        for (Bullet bullet : gameController.getBullets()) {
            batch.draw(bullet.getTexture(), bullet.getPosition().x, bullet.getPosition().y);
        }

        // Draw enemies [cite: 69]
        for (Enemy enemy : gameController.getEnemies()) {
            batch.draw(enemy.getTexture(), enemy.getPosition().x, enemy.getPosition().y);
        }
        batch.end();

        // Optional: Draw in-game UI (HUD)
        // if (uiStage != null) {
        //     uiStage.act(delta);
        //     uiStage.draw();
        // }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        // if (uiStage != null) uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); // Clear input processor when screen is hidden
    }

    @Override
    public void dispose() {
        // Dispose any resources specific to this screen
        // if (uiStage != null) uiStage.dispose();
    }
}