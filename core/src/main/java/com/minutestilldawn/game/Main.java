package com.minutestilldawn.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
// import com.kotcrab.vis.ui.VisUI; // Only if you are using VisUI

import com.minutestilldawn.game.Controller.*;
import com.minutestilldawn.game.Model.*;
import com.minutestilldawn.game.View.*;

public class Main extends Game {
    private static Main instance;
    private static SpriteBatch batch;
    public static final String TITLE = "20 Minutes Till Dawn";
    private static final String PREFS_NAME = "MinutesTillDawnSettings"; // Preferences file name
    private static final String SAVED_GAME_PREF_KEY = "savedGameState";

    private GameAssetManager assetManager;
    private Skin uiSkin;
    private UserDao userDao; // DAO for user data

    // Controllers
    private MainMenuController mainMenuController;
    private SettingsMenuController settingsMenuController;
    private ProfileMenuController profileMenuController;
    private ScoreboardController scoreboardController;
    private LoginMenuController loginMenuController;
    private RegistrationMenuController registrationMenuController;
    private PreGameMenuController preGameMenuController; // New
    private HintMenuController hintMenuController; // New (if you create one)
    // GameController is typically instantiated by GameScreenView with a GameState

    // Current User State
    private User currentUser; // Currently logged-in user
    private boolean isGuest = false; // Flag for guest session
    private GameState currentGameState; // Holds the state of an active or paused game

    @Override
    public void create() {
        if (instance == null) {
            instance = this;
        }

        batch = new SpriteBatch();
        userDao = new SqliteUserDao(); // Initialize DAO
        userDao.initialize();

        assetManager = new GameAssetManager();
        // It's good practice to load essential UI assets first, like skins and fonts
        // For avatar placeholders in MainMenu:
        assetManager.load("avatars/default_avatar.png", Texture.class); // Load a default
        // Potentially load other common avatars if paths are known and fixed
        // assetManager.load("avatars/avatar_1.png", Texture.class);
        // assetManager.load("avatars/avatar_2.png", Texture.class);
        assetManager.loadAssets(); // Load all other assets
        assetManager.finishLoading();
        uiSkin = assetManager.getPixthulhuSkin();

        // Initialize controllers
        mainMenuController = new MainMenuController(this);
        settingsMenuController = new SettingsMenuController(this);
        profileMenuController = new ProfileMenuController(this);
        scoreboardController = new ScoreboardController(this);
        loginMenuController = new LoginMenuController(this, userDao); // Pass DAO
        registrationMenuController = new RegistrationMenuController(this, userDao); // Pass DAO
        preGameMenuController = new PreGameMenuController(this);
        hintMenuController = new HintMenuController(this); // Assuming you have this controller

        // Start with Login Screen
        setScreen(new LoginMenuView(loginMenuController, uiSkin));
    }

    @Override
    public void render() {
        super.render(); // Important: This calls render on the current screen
    }

    @Override
    public void dispose() {
        if (batch != null)
            batch.dispose();
        if (assetManager != null)
            assetManager.dispose();
        if (userDao != null)
            userDao.dispose(); // Dispose DAO
        if (uiSkin != null)
            uiSkin.dispose(); // Skin can also be disposable if it owns resources

        // if (VisUI.isLoaded()) VisUI.dispose(); // If using VisUI
        Gdx.app.log("Main", "Disposed all resources.");
    }

    // --- User Management ---
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.isGuest = false;
        if (user != null) {
            Gdx.app.log("Main", "User set: " + user.getUsername());
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setGuestSession(boolean isGuest) {
        this.isGuest = isGuest;
        this.currentUser = null; // No specific user for guest
        if (isGuest)
            Gdx.app.log("Main", "Guest session started.");
    }

    public boolean isGuestSession() {
        return isGuest;
    }

    public void logoutUser() {
        this.currentUser = null;
        this.isGuest = false;
        // Clear any session-specific game state if necessary
        this.currentGameState = null; // Or reset it
        Gdx.app.log("Main", "User logged out.");
        setLoginScreen(); // Go back to login screen
    }

    public UserDao getUserDao() {
        return userDao;
    }

    // --- Game Session Management ---
    public void startNewGameSession(CharacterType characterType, String weaponName, float durationMinutes) {
        Gdx.app.log("Main", "Starting new game session. Hero: " + characterType.name() +
                ", Weapon: " + weaponName + ", Duration: " + durationMinutes + "m");

        TextureAtlas playerAtlas = null;
        try {
            // Ensure player atlas is loaded. It should be in
            // GameAssetManager.PLAYER_SPRITE_ATLAS
            if (assetManager.isLoaded(GameAssetManager.PLAYER_SPRITE_ATLAS)) {
                playerAtlas = assetManager.get(GameAssetManager.PLAYER_SPRITE_ATLAS, TextureAtlas.class);
            } else {
                Gdx.app.error("Main", "Player sprite atlas not loaded: " + GameAssetManager.PLAYER_SPRITE_ATLAS);
                // Handle error: maybe go back to menu or use a placeholder
                setMainMenuScreen(); // Go back if critical asset is missing
                return;
            }
        } catch (Exception e) {
            Gdx.app.error("Main", "Exception loading player atlas: " + e.getMessage());
            setMainMenuScreen();
            return;
        }

        boolean autoReload = Gdx.app.getPreferences(PREFS_NAME).getBoolean("autoReload", false); // Get from settings

        currentGameState = new GameState(
                currentUser, // Can be null for guest
                characterType,
                weaponName,
                durationMinutes,
                playerAtlas,
                assetManager,
                autoReload);
        currentGameState.startGame(); // Set status to PLAYING and reset timers/stats

        setScreen(new GameScreenView(assetManager, currentGameState, this));
    }

    public void continueSavedGame() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        if (prefs.contains(SAVED_GAME_PREF_KEY)) {
            String savedGameJson = prefs.getString(SAVED_GAME_PREF_KEY);
            // TODO: Implement deserialization of GameState from JSON
            // GameState loadedState = JsonUtil.fromJson(GameState.class, savedGameJson);
            // if (loadedState != null) {
            // this.currentGameState = loadedState;
            // // Need to re-initialize non-serializable parts like TextureAtlas for player
            // TextureAtlas playerAtlas =
            // assetManager.get(GameAssetManager.PLAYER_SPRITE_ATLAS, TextureAtlas.class);
            // // Potentially re-create player instance or update it
            // // loadedState.getPlayerInstance().setTextureAtlas(playerAtlas); // Player
            // needs this method
            // Gdx.app.log("Main", "Continuing saved game.");
            // setScreen(new GameScreenView(assetManager, currentGameState, this));
            // return;
            // }
            Gdx.app.log("Main", "Failed to load saved game data.");
        }
        Gdx.app.log("Main", "No saved game found to continue.");
        // Optionally show a message to the user
    }

    public void saveGame() {
        if (currentGameState != null && currentGameState.getCurrentStatus() == GameStatus.PAUSED) {
            // TODO: Implement serialization of GameState to JSON
            // String savedGameJson = JsonUtil.toJson(currentGameState);
            // Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
            // prefs.putString(SAVED_GAME_PREF_KEY, savedGameJson);
            // prefs.flush();
            Gdx.app.log("Main", "Game state saved (TODO: Implement JSON serialization).");
        } else {
            Gdx.app.log("Main", "Cannot save game. No active/paused game state.");
        }
    }

    public void handleGameEnd(GameState endedGameState) {
        Gdx.app.log("Main", "Game ended. Status: " + endedGameState.getCurrentStatus() +
                ", Score: " + endedGameState.getScore() +
                ", Kills: " + endedGameState.getKills() +
                ", Time: " + endedGameState.getElapsedTimeSeconds());

        if (currentUser != null && endedGameState.getScore() > 0) { // Only update if logged in and score > 0
            userDao.updateUserStats(
                    currentUser.getUsername(),
                    endedGameState.getScore(),
                    endedGameState.getKills(),
                    endedGameState.getElapsedTimeSeconds());
            // Refresh current user data if it holds aggregated stats
            // this.currentUser = userDao.getUserByUsername(currentUser.getUsername());
        }

        // TODO: Transition to a Game Over Screen
        // For now, just go back to the main menu
        // You would pass `endedGameState` to the GameOverScreen to display stats
        // setScreen(new GameOverScreen(this, uiSkin, endedGameState));
        setMainMenuScreen(); // Placeholder
        this.currentGameState = null; // Clear the ended game state
    }

    // --- Screen Switching Methods ---
    public void setGameScreen() { // This might be deprecated if always starting via startNewGameSession
        if (currentGameState != null) {
            setScreen(new GameScreenView(assetManager, currentGameState, this));
        } else {
            Gdx.app.error("Main", "Cannot set game screen: currentGameState is null. Use startNewGameSession.");
            setPreGameMenuScreen(); // Go to pre-game setup if no game state
        }
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

    public void setLoginScreen() {
        setScreen(new LoginMenuView(loginMenuController, uiSkin));
    }

    public void setRegistrationScreen() {
        setScreen(new RegistrationMenuView(registrationMenuController, uiSkin));
    }

    public void setPreGameMenuScreen() { // New
        setScreen(new PreGameMenuView(preGameMenuController, uiSkin));
    }

    public void setHintMenuScreen() { // New
        // Make sure HintMenuView and HintMenuController are created
        // For now, assuming it exists and is similar to other menu screens
        if (hintMenuController == null)
            hintMenuController = new HintMenuController(this);{
                setScreen(new HintMenuView(hintMenuController, uiSkin)); // Assuming HintMenuView exists
            }
    }

    // --- Getters ---
    public static Main getInstance() {
        return instance;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public GameAssetManager getAssetManager() {
        return assetManager;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }
}
