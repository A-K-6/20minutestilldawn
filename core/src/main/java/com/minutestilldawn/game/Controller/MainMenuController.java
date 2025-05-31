package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Model.GameState; // For accessing current game state if needed for "Continue"
import com.minutestilldawn.game.Model.User;     // For displaying user info

public class MainMenuController extends BaseMenuController {

    private Main gameInstance;

    public MainMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public void onButtonClicked(String buttonId) {
        Gdx.app.log("MainMenuController", "Button clicked: " + buttonId);

        switch (buttonId) {
            case "play": // Changed from "play" to "new_game" or similar if you have "Continue"
                Gdx.app.log("MainMenuController", "Switching to Pre-Game Menu...");
                gameInstance.setPreGameMenuScreen(); // Navigate to the new Pre-Game Menu
                break;
            case "continue_saved_game":
                Gdx.app.log("MainMenuController", "Attempting to continue saved game...");
                gameInstance.continueSavedGame(); // Implement this in Main.java
                break;
            case "settings":
                Gdx.app.log("MainMenuController", "Switching to Settings Menu...");
                gameInstance.setSettingsScreen();
                break;
            case "profile":
                Gdx.app.log("MainMenuController", "Switching to Profile Menu...");
                // Ensure a user is logged in, or disable this button / show guest profile
                if (gameInstance.getCurrentUser() != null || gameInstance.isGuestSession()) {
                     gameInstance.setProfileScreen();
                } else {
                    Gdx.app.log("MainMenuController", "No user logged in. Cannot go to profile.");
                    // Optionally, show a message to the user via the view
                }
                break;
            case "scoreboard":
                Gdx.app.log("MainMenuController", "Switching to Scoreboard...");
                gameInstance.setScoreboardScreen();
                break;
            case "hint": // Assuming "hint" button leads to Talent Tree/Hint Menu
                Gdx.app.log("MainMenuController", "Switching to Hint/Talent Menu...");
                gameInstance.setHintMenuScreen(); // Create this screen and method in Main.java
                break;
            case "logout":
                Gdx.app.log("MainMenuController", "Logging out...");
                gameInstance.logoutUser(); // Implement this in Main.java
                break;
            case "exit":
                Gdx.app.log("MainMenuController", "Exiting game...");
                Gdx.app.exit();
                break;
            default:
                Gdx.app.log("MainMenuController", "Unknown button ID: " + buttonId);
                break;
        }
    }

    // Methods for the View to get data
    public User getCurrentUser() {
        return gameInstance.getCurrentUser();
    }

    public boolean isGuestSession() {
        return gameInstance.isGuestSession();
    }

    public int getCurrentUserTotalScore() {
        User user = gameInstance.getCurrentUser();
        if (user != null) {
            // Assuming User.java will have a method like getTotalScore()
            // which you'll add when updating UserDao/SqliteUserDao
            // return user.getTotalScore();
            return gameInstance.getUserDao().getUserTotalScore(user.getUsername()); // Fetch from DAO
        }
        return 0;
    }
     public String getCurrentUserAvatarPath() {
        User user = gameInstance.getCurrentUser();
        if (user != null && user.getAvatarId() > 0) {
            // This is a placeholder. You'll need a mapping from avatarId to actual image path
            // or TextureRegion in your GameAssetManager or another utility class.
            // For example: "avatars/avatar_" + user.getAvatarId() + ".png"
            return "avatars/avatar_" + user.getAvatarId() + ".png"; // Example path
        }
        return "avatars/default_avatar.png"; // Default if no avatar or guest
    }
}
