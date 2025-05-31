package com.minutestilldawn.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Controller.GameController;
import com.minutestilldawn.game.Model.*;

public class GameScreenView extends ScreenAdapter {

    private final Main gameInstance;
    private final SpriteBatch batch;
    private final GameAssetManager assetManager;
    private final GameState gameState;
    private final GameController gameController;

    private final OrthographicCamera gameCamera;
    private final FitViewport gameViewport;

    private final Stage uiStage;
    private final Skin skin;

    // HUD Elements
    private Label hpLabel;
    private Label timeLabel;
    private Label killsLabel;
    private Label ammoLabel;
    private Label levelLabel;
    private Label xpLabel;
    private ProgressBar xpProgressBar;
    private Label fpsLabel; // For debugging

    // UI Tables for Pause and Level Up
    private Table pauseMenuTable;
    private Table levelUpTable;
    private Label autoAimStatusLabel;


    // For "lit area" effect and aiming line
    private ShapeRenderer shapeRenderer;
    private static final float LIT_AREA_RADIUS = 250f; // Example radius

    public GameScreenView(GameAssetManager assetManager, GameState gameState, Main gameInstance) {
        this.assetManager = assetManager;
        this.gameState = gameState;
        this.gameInstance = gameInstance;
        this.batch = Main.getBatch(); // Use shared batch
        this.skin = assetManager.getPixthulhuSkin(); // Or your chosen skin

        // Game Camera & Viewport
        gameCamera = new OrthographicCamera();
        gameViewport = new FitViewport(800, 600, gameCamera); // Example world size
        gameCamera.position.set(gameViewport.getWorldWidth() / 2f, gameViewport.getWorldHeight() / 2f, 0);
        gameCamera.update();

        // UI Stage & Viewport
        uiStage = new Stage(new ScreenViewport(), batch);

        // Game Controller
        gameController = new GameController(gameInstance, gameState, assetManager, gameCamera);

        shapeRenderer = new ShapeRenderer();

        setupHUD();
        setupPauseMenu();
        setupLevelUpUI();
    }

    private void setupHUD() {
        Table hudTable = new Table(skin);
        hudTable.setFillParent(true);
        hudTable.top().left(); // Align to top-left

        hpLabel = new Label("HP: 0/0", skin);
        timeLabel = new Label("Time: 00:00", skin);
        killsLabel = new Label("Kills: 0", skin);
        ammoLabel = new Label("Ammo: 0/0", skin);
        levelLabel = new Label("Lvl: 1", skin);
        xpLabel = new Label("XP: 0/0", skin); // For text display of XP
        xpProgressBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
        fpsLabel = new Label("FPS: 0", skin); // For debugging
        autoAimStatusLabel = new Label("Auto-Aim: OFF", skin);


        hudTable.add(hpLabel).pad(5).left().row();
        hudTable.add(timeLabel).pad(5).left().row();
        hudTable.add(killsLabel).pad(5).left().row();
        hudTable.add(ammoLabel).pad(5).left().row();
        hudTable.add(levelLabel).pad(5).left().row();
        hudTable.add(xpLabel).pad(5).left().row();
        hudTable.add(xpProgressBar).width(150).pad(5).left().row();
        hudTable.add(autoAimStatusLabel).pad(5).left().row();
        hudTable.add(fpsLabel).pad(5).left().bottom().expandY(); // Push FPS to bottom left of its cell

        uiStage.addActor(hudTable);
    }

    private void updateHUD() {
        Player player = gameState.getPlayerInstance();
        hpLabel.setText("HP: " + player.getCurrentHp() + "/" + player.getMaxHp());

        float timeRemaining = gameState.getTimeRemainingSeconds();
        int minutes = (int) (timeRemaining / 60);
        int seconds = (int) (timeRemaining % 60);
        timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));

        killsLabel.setText("Kills: " + gameState.getKills());
        if (player.getCurrentWeapon() != null) {
            ammoLabel.setText("Ammo: " + player.getCurrentWeapon().getCurrentAmmo() + "/" + player.getCurrentWeapon().getMaxAmmo());
        }
        levelLabel.setText("Lvl: " + player.getLevel());
        xpLabel.setText("XP: " + player.getXp() + "/" + player.getXpNeededForNextLevel());
        if (player.getXpNeededForNextLevel() > 0) {
             xpProgressBar.setValue((float) player.getXp() / player.getXpNeededForNextLevel());
        } else {
            xpProgressBar.setValue(1f); // Maxed out or level 0
        }
        autoAimStatusLabel.setText("Auto-Aim: " + (gameState.isAutoAimActive() ? "ON" : "OFF"));
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    private void setupPauseMenu() {
        pauseMenuTable = new Table(skin);
        pauseMenuTable.setFillParent(true);
        pauseMenuTable.center();
        pauseMenuTable.setBackground(skin.newDrawable("white", new Color(0,0,0,0.7f))); // Semi-transparent background

        pauseMenuTable.add(new Label("GAME PAUSED", skin, "title")).padBottom(30).row();
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton saveExitButton = new TextButton("Save & Exit to Main Menu (TODO)", skin); // PDF: (امتیازی)
        TextButton giveUpButton = new TextButton("Give Up & Exit", skin);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                togglePause();
            }
        });
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameInstance.setSettingsScreen(); // Will hide this screen
            }
        });
        saveExitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: Implement gameInstance.saveGame();
                Gdx.app.log("PauseMenu", "Save game clicked (Not Implemented)");
                gameState.setCurrentStatus(GameStatus.GAME_OVER_GIVE_UP); // Or a specific "SAVED_AND_EXITED"
                gameInstance.handleGameEnd(gameState); // Or just gameInstance.setMainMenuScreen();
            }
        });
         giveUpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameState.setCurrentStatus(GameStatus.GAME_OVER_GIVE_UP);
                gameInstance.handleGameEnd(gameState);
            }
        });


        pauseMenuTable.add(resumeButton).width(200).pad(10).row();
        pauseMenuTable.add(settingsButton).width(200).pad(10).row();
        pauseMenuTable.add(saveExitButton).width(300).pad(10).row();
        pauseMenuTable.add(giveUpButton).width(200).pad(10).row();
        pauseMenuTable.setVisible(false);
        uiStage.addActor(pauseMenuTable);
    }

    private void setupLevelUpUI() {
        levelUpTable = new Table(skin);
        levelUpTable.setFillParent(true);
        levelUpTable.center();
        levelUpTable.setBackground(skin.newDrawable("white", new Color(0,0,0,0.85f)));

        levelUpTable.add(new Label("LEVEL UP!", skin, "title")).padBottom(20).colspan(3).row();
        levelUpTable.add(new Label("Choose an Ability:", skin)).padBottom(20).colspan(3).row();
        // Buttons will be added dynamically
        levelUpTable.setVisible(false);
        uiStage.addActor(levelUpTable);
    }

    private void showLevelUpUI() {
        levelUpTable.clearChildren(); // Remove old buttons
        levelUpTable.add(new Label("LEVEL UP!", skin, "title")).padBottom(20).colspan(3).row();
        levelUpTable.add(new Label("Choose an Ability:", skin)).padBottom(20).colspan(3).row();

        Ability[] allAbilities = Ability.values();
        Array<Ability> choices = new Array<>();
        // Get 3 unique random abilities not already fully stacked (if such logic exists)
        // For now, just random unique.
        if (allAbilities.length <= 3) {
            choices.addAll(allAbilities);
        } else {
            Array<Ability> available = new Array<>(allAbilities);
            available.shuffle();
            for (int i = 0; i < Math.min(3, available.size); i++) {
                choices.add(available.get(i));
            }
        }

        for (final Ability ability : choices) {
            TextButton abilityButton = new TextButton(ability.name() + "\n(" + ability.getDescription() + ")", skin);
            abilityButton.getLabel().setWrap(true);
            abilityButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameController.processAbilityChoice(ability);
                    levelUpTable.setVisible(false);
                    // gameState.setCurrentStatus(GameStatus.PLAYING); // Controller does this
                }
            });
            levelUpTable.add(abilityButton).width(300).height(100).pad(10);
        }
        levelUpTable.row();
        levelUpTable.setVisible(true);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage); // UI gets first dibs on input
        multiplexer.addProcessor(gameController); // Then game controller
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        // Handle Pause Key
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && gameState.getCurrentStatus() != GameStatus.LEVEL_UP_CHOICE) {
            togglePause();
        }

        // Update based on game status
        if (gameState.getCurrentStatus() == GameStatus.PLAYING) {
            gameController.update(delta);
            gameState.update(delta); // Update game timer
        } else if (gameState.getCurrentStatus() == GameStatus.LEVEL_UP_CHOICE && !levelUpTable.isVisible()) {
            showLevelUpUI();
        }
        uiStage.act(Math.min(delta, 1 / 30f));


        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera to follow player
        Player player = gameState.getPlayerInstance();
        gameCamera.position.x = player.getPosition().x;
        gameCamera.position.y = player.getPosition().y;
        gameCamera.update();
        batch.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.setProjectionMatrix(gameCamera.combined);

        // --- Render Game World ---
        batch.begin();
        // TODO: Draw background/tilemap if you have one
        for (Tree tree : gameController.getTrees()) {
            batch.draw(tree.getTexture(), tree.getPosition().x - tree.getBounds().width/2, tree.getPosition().y - tree.getBounds().height/2);
        }
        for (Enemy enemy : gameController.getActiveEnemies()) {
            batch.draw(enemy.getTexture(), enemy.getPosition().x - enemy.getBounds().width/2, enemy.getPosition().y - enemy.getBounds().height/2);
        }
        for (Bullet bullet : gameController.getActivePlayerBullets()) {
            batch.draw(bullet.getTexture(), bullet.getPosition().x - bullet.getBounds().width/2, bullet.getPosition().y - bullet.getBounds().height/2);
        }
        for (Bullet bullet : gameController.getActiveEnemyBullets()) {
            batch.draw(bullet.getTexture(), bullet.getPosition().x - bullet.getBounds().width/2, bullet.getPosition().y - bullet.getBounds().height/2);
        }
        batch.draw(player.getCurrentFrame(), player.getPosition().x - player.getCharacterType().getHitbox(player.getPosition()).width/2 , player.getPosition().y - player.getCharacterType().getHitbox(player.getPosition()).height/2);
        // TODO: Draw XP seeds, particle effects for damage/death
        batch.end();

        // --- Render "Lit Area" and Aiming Line (optional) ---
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Simple vignette effect (darken edges)
        // This is a complex effect, for now, just a conceptual placement
        // Or a "torch light" effect around player
        // shapeRenderer.setColor(0,0,0,0.3f); // Example: slightly darken areas NOT lit
        // For a lit area, you might draw a large dark overlay with a transparent circle cut out.
        // This is easier with frame buffers or specific shaders.
        // Simple "aim target" for auto-aim
        if (gameState.isAutoAimActive() && gameController.getAutoAimTarget() != null) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(gameController.getAutoAimTarget().getPosition().x, gameController.getAutoAimTarget().getPosition().y, 15);
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


        // --- Render HUD & UI ---
        updateHUD();
        uiStage.draw();
    }

    private void togglePause() {
        if (gameState.getCurrentStatus() == GameStatus.PLAYING) {
            gameState.setCurrentStatus(GameStatus.PAUSED);
            pauseMenuTable.setVisible(true);
        } else if (gameState.getCurrentStatus() == GameStatus.PAUSED) {
            gameState.setCurrentStatus(GameStatus.PLAYING);
            pauseMenuTable.setVisible(false);
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        gameCamera.update(); // Important after viewport update if not centering camera in resize
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        if (gameState.getCurrentStatus() == GameStatus.PLAYING) {
            gameState.setCurrentStatus(GameStatus.PAUSED); // Or a specific "SYSTEM_PAUSED"
            pauseMenuTable.setVisible(true);
        }
    }

    @Override
    public void resume() {
        // If it was system-paused, might want to go back to PLAYING or keep pause menu
        // For simplicity, if pause menu is up, user has to click resume.
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        uiStage.dispose();
        shapeRenderer.dispose();
        // GameController, GameState, AssetManager are managed by Main or passed in.
    }
}
