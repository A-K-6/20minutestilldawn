package com.minutestilldawn.game.Model;

import com.badlogic.gdx.graphics.g2d.TextureAtlas; // For Player creation
import com.badlogic.gdx.utils.Array; // Using LibGDX Array for efficiency

/**
 * Holds the state of the current game session.
 * This includes player information, game progress, settings chosen for this
 * session,
 * and current status of the game.
 */
public class GameState {

    // User and Player
    private User loggedInUser; // The user playing this game session (can be null for guest)
    private Player playerInstance; // The active player character in the game

    // Pre-Game Selections that define this session
    private CharacterType selectedCharacterType;
    private String selectedWeaponName; // e.g., "Revolver", "Shotgun"
    private float chosenGameDurationSeconds; // Total duration for this game session

    // Session Progress
    private float elapsedTimeSeconds; // Time elapsed since the game started
    private int kills;
    private int score;
    private GameStatus currentStatus;
    private Array<Ability> acquiredAbilities; // Abilities acquired by the player during this session

    // In-Game Dynamic States
    private boolean autoAimActive;
    // Add other dynamic states if needed, e.g., specific boss fight flags

    // Game Settings (relevant for this session, potentially from global settings)
    private boolean autoReloadEnabled; // Copied from global settings at game start
    // Other settings like volume, controls are usually managed globally or by the
    // view/controller directly

    /**
     * Constructor for a new game session.
     *
     * @param loggedInUser            The user playing, or null for a guest.
     * @param characterType           The chosen character type.
     * @param weaponName              The name of the chosen starting weapon.
     * @param gameDurationMinutes     The chosen game duration in minutes.
     * @param playerAtlas             The TextureAtlas for player animations.
     * @param gameAssetManager        The asset manager to get weapon stats or other
     *                                assets.
     * @param globalAutoReloadEnabled The global setting for auto-reload.
     */
    public GameState(User loggedInUser,
            CharacterType characterType,
            String weaponName,
            float gameDurationMinutes,
            TextureAtlas playerAtlas, // Needed to create the Player
            GameAssetManager gameAssetManager, // Potentially needed for weapon details
            boolean globalAutoReloadEnabled) {

        this.loggedInUser = loggedInUser;
        this.selectedCharacterType = characterType;
        this.selectedWeaponName = weaponName;
        this.chosenGameDurationSeconds = gameDurationMinutes * 60;

        // Initialize Player
        // The Player constructor will need the User, CharacterType, and initial Weapon.
        // The Weapon object itself can be created within the Player or here.
        // For simplicity, let's assume Player constructor handles creating its initial
        // weapon
        // based on the name and potentially stats from GameAssetManager or a config
        // file.
        Weapon initialWeapon = createWeaponFromName(weaponName, gameAssetManager); // Helper method
        this.playerInstance = new Player(loggedInUser, characterType, playerAtlas, initialWeapon);

        this.elapsedTimeSeconds = 0;
        this.kills = 0;
        this.score = 0;
        this.currentStatus = GameStatus.PRE_GAME; // Should be set to PLAYING when game actually starts
        this.acquiredAbilities = new Array<>();
        this.autoAimActive = false; // Default, can be toggled
        this.autoReloadEnabled = globalAutoReloadEnabled;
    }

    /**
     * Helper method to create a Weapon instance from its name.
     * This would ideally fetch weapon stats from a configuration or the asset
     * manager.
     */
    private Weapon createWeaponFromName(String name, GameAssetManager assetManager) {
        // Based on AP_Graphics.pdf Table 2: Weapon Specifications
        switch (name) {
            case "Revolver":
                return new Weapon("Revolver", 20, 6, 1.0f); // Name, Damage, MaxAmmo, ReloadTime
            case "Shotgun":
                // The PDF says "Projectile: 4" for shotgun. This needs to be handled in
                // Weapon/Player shooting logic.
                // For now, standard Weapon constructor.
                return new Weapon("Shotgun", 10, 2, 2.0f); // Damage is per projectile, ammo is shells
            case "Dual SMGs":
                return new Weapon("Dual SMGs", 8, 24, 2.0f); // Assuming 'A' meant 8
            default:
                System.err.println("Warning: Unknown weapon name '" + name + "'. Defaulting to Revolver.");
                return new Weapon("Revolver", 20, 6, 1.0f);
        }
        // In a more complex setup, you might load these stats from JSON files
        // managed by GameAssetManager.
    }

    /**
     * Updates the game state over time.
     * Primarily updates the elapsed time and checks for game win condition by time.
     * 
     * @param delta Time since the last frame.
     */
    public void update(float delta) {
        if (currentStatus == GameStatus.PLAYING) {
            elapsedTimeSeconds += delta;
            if (elapsedTimeSeconds >= chosenGameDurationSeconds) {
                currentStatus = GameStatus.GAME_OVER_WIN;
                // Additional logic for game win (e.g., calculating final score)
            }
        }
    }

    // --- Getters ---
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Player getPlayerInstance() {
        return playerInstance;
    }

    public void cheatReduceTime(float seconds) {
        if (this.currentStatus == GameStatus.PLAYING) {
            this.elapsedTimeSeconds = Math.min(this.chosenGameDurationSeconds,
                    this.elapsedTimeSeconds + seconds);
        }
    }

    public CharacterType getSelectedCharacterType() {
        return selectedCharacterType;
    }

    public String getSelectedWeaponName() {
        return selectedWeaponName;
    }

    public float getChosenGameDurationSeconds() {
        return chosenGameDurationSeconds;
    }

    public float getElapsedTimeSeconds() {
        return elapsedTimeSeconds;
    }

    public float getTimeRemainingSeconds() {
        return Math.max(0, chosenGameDurationSeconds - elapsedTimeSeconds);
    }

    public int getKills() {
        return kills;
    }

    public int getScore() {
        return score;
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public Array<Ability> getAcquiredAbilities() {
        return acquiredAbilities;
    }

    public boolean isAutoAimActive() {
        return autoAimActive;
    }

    public boolean isAutoReloadEnabled() {
        return autoReloadEnabled;
    }

    // --- Setters / Modifiers ---
    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void incrementKills() {
        this.kills++;
        // Potentially update score here or have a separate method
        recalculateScore();
    }

    public void addAcquiredAbility(Ability ability) {
        this.acquiredAbilities.add(ability);
        // Apply ability effect to playerInstance
        // This logic might be better placed in Player.addAbility(Ability)
        // or GameController when an ability is chosen.
        // For now, just collecting them here.
    }

    public void toggleAutoAim() {
        this.autoAimActive = !this.autoAimActive;
    }

    public void setPlayerInstance(Player playerInstance) {
        this.playerInstance = playerInstance;
    }

    private void recalculateScore() {
        // As per PDF: score = kills * survival_time(seconds)
        // survival_time should be elapsedTimeSeconds when game ends or at point of
        // scoring
        this.score = (int) (this.kills * this.elapsedTimeSeconds);
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Call this method when the actual gameplay starts after pre-game selections.
     */
    public void startGame() {
        this.elapsedTimeSeconds = 0;
        this.kills = 0;
        this.score = 0;
        this.currentStatus = GameStatus.PLAYING;
        // Reset player stats if necessary (HP, ammo, etc.) though constructor should
        // handle initial state.
        if (playerInstance != null) {
            playerInstance.resetForNewGame(); // You'll need to add this method to Player.java
        }
    }

    // TODO: Add methods for saving and loading the game state.
    // This would involve serializing the relevant fields of GameState and Player.
    // Example:
    // public GameStateData getSaveData() { ... return new GameStateData(this); ...
    // }
    // public void loadFromData(GameStateData data) { ... restore state ... }
    // GameStateData would be a simple class with public fields or getters/setters
    // for serialization.
}

/**
 * --- NOTES FOR OTHER FILES ---
 *
 * 1. Player.java:
 * - Modify constructor: `public Player(User user, CharacterType type,
 * TextureAtlas atlas, Weapon startingWeapon)`
 * - Add `resetForNewGame()`: Resets HP to max, ammo to max for current weapon,
 * clears temporary effects, resets XP for level 1.
 * - Player should manage its own list of *active* ability effects. `GameState`
 * stores the *chosen* abilities.
 * - Weapon class might need a field for `projectilesPerShot` for Shotgun.
 *
 * 2. GameController.java:
 * - Constructor: `public GameController(GameAssetManager assetManager,
 * GameState gameState)`
 * - Remove player initialization from GameController; get it from
 * `gameState.getPlayerInstance()`.
 * `this.player = gameState.getPlayerInstance();`
 * - When an enemy is killed: `gameState.incrementKills();`
 * - When game ends (win/lose/give up):
 * `gameState.setCurrentStatus(GameStatus.GAME_OVER_X);`
 * `gameState.recalculateScore(); // or set explicitly`
 * - Input handling for toggling auto-aim should call
 * `gameState.toggleAutoAim();`
 * - Player shooting logic should check
 * `gameState.getPlayerInstance().getCurrentWeapon().canShoot()` etc.
 *
 * 3. Main.java:
 * - When transitioning to `GameScreenView`, create a `GameState` instance:
 * `CharacterType selectedChar = ...; // from pre-game menu`
 * `String selectedWeap = ...; // from pre-game menu`
 * `float duration = ...; // from pre-game menu`
 * `User currentUser = ...; // from login/registration or null for guest`
 * `boolean autoReload = ...; // from global settings`
 * `TextureAtlas playerAtlas = assetManager.getPlayerSpriteAtlas(); // Ensure
 * this is loaded`
 * `GameState newGameState = new GameState(currentUser, selectedChar,
 * selectedWeap, duration, playerAtlas, assetManager, autoReload);`
 * `newGameState.startGame(); // Set status to PLAYING`
 * `setScreen(new GameScreenView(assetManager, newGameState));`
 *
 * 4. GameScreenView.java:
 * - Constructor: `public GameScreenView(GameAssetManager assetManager,
 * GameState gameState)`
 * - Pass `gameState` to `GameController`.
 * - HUD rendering should get data from `gameState` (e.g.,
 * `gameState.getTimeRemainingSeconds()`, `gameState.getKills()`,
 * `gameState.getPlayerInstance().getCurrentHp()`, etc.)
 *
 * 5. Weapon.java:
 * - Consider adding `int projectilesPerShot` (default to 1). Shotgun would have
 * 4.
 * - The `damage` for shotgun in the PDF (10) is likely *per projectile*.
 *
 * 6. Pre-Game Menu Logic (e.g., in GameMenuController or a new
 * PreGameSetupController):
 * - This controller will gather selections for character, weapon, and duration.
 * - When "Start Game" is clicked, it will trigger the creation of the
 * `GameState` in `Main.java`
 * and the switch to `GameScreenView`.
 */
