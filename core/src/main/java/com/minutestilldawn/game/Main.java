package com.minutestilldawn.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI; // Assuming you're using VisUI based on your dispose()
import com.minutestilldawn.game.Controller.MainMenuController;
import com.minutestilldawn.game.Model.GameAssetManager;
import com.minutestilldawn.game.View.MainMenuView;
import com.minutestilldawn.game.View.GameScreenView;

// Import all new controllers and views
import com.minutestilldawn.game.Controller.SettingsMenuController;
import com.minutestilldawn.game.View.SettingsMenuView;
import com.minutestilldawn.game.Controller.ProfileMenuController;
import com.minutestilldawn.game.View.ProfileMenuView;
import com.minutestilldawn.game.Controller.ScoreboardController;
import com.minutestilldawn.game.View.ScoreboardView;
import com.minutestilldawn.game.Controller.LoginMenuController; // NEW
import com.minutestilldawn.game.View.LoginMenuView; // NEW
import com.minutestilldawn.game.Controller.RegistrationMenuController; // NEW
import com.minutestilldawn.game.View.RegistrationMenuView; // NEW

public class Main extends Game {
    private static Main instance;
    private static SpriteBatch batch;
    public static final String TITLE = "20 Minutes Till Dawn";

    private GameAssetManager assetManager;
    private Skin uiSkin;

    // Controllers
    private MainMenuController mainMenuController;
    private SettingsMenuController settingsMenuController;
    private ProfileMenuController profileMenuController;
    private ScoreboardController scoreboardController;
    private LoginMenuController loginMenuController; // NEW
    private RegistrationMenuController registrationMenuController; // NEW

    @Override
    public void create() {
        if (instance == null) {
            instance = this;
        }

        batch = new SpriteBatch();

        assetManager = new GameAssetManager();
        assetManager.loadAssets();
        assetManager.finishLoading(); // Load all assets immediately for now
        uiSkin = assetManager.getPixthulhuSkin(); // Get the loaded skin

        // Initialize controllers
        mainMenuController = new MainMenuController(this);
        settingsMenuController = new SettingsMenuController(this);
        profileMenuController = new ProfileMenuController(this);
        scoreboardController = new ScoreboardController(this);
        loginMenuController = new LoginMenuController(this); // NEW
        registrationMenuController = new RegistrationMenuController(this); // NEW

        // Set the initial screen to the Login screen, as per typical flow
        setScreen(new LoginMenuView(loginMenuController, uiSkin)); // Changed initial screen
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (loginMenuController != null) { // Dispose DAOs
            loginMenuController.dispose();
        }
        if (registrationMenuController != null) { // Dispose DAOs
            registrationMenuController.dispose();
        }
        // Dispose other controllers if they hold disposable resources
        VisUI.dispose(); // Always dispose VisUI if you load it statically (if used)
    }

    public static Main getInstance() {
        return instance;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public GameAssetManager getAssetManager() {
        return assetManager;
    }

    // --- Screen Switching Methods ---
    public void setGameScreen() {
        setScreen(new GameScreenView(assetManager)); // Assuming GameScreenView exists
    }

    public void setSettingsScreen() {
        setScreen(new SettingsMenuView(settingsMenuController, uiSkin));
    }

    public void setProfileScreen() {
        setScreen(new ProfileMenuView(profileMenuController, uiSkin));
    }

    public void setScoreboardScreen() {
        setScreen(new ScoreboardView(scoreboardController, uiSkin));
    }

    public void setMainMenuScreen() {
        setScreen(new MainMenuView(mainMenuController, uiSkin));
    }

    // NEW: Login and Registration Screens
    public void setLoginScreen() {
        setScreen(new LoginMenuView(loginMenuController, uiSkin));
    }

    public void setRegistrationScreen() {
        setScreen(new RegistrationMenuView(registrationMenuController, uiSkin));
    }
}