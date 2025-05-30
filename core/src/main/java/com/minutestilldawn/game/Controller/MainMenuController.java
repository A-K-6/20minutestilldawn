package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main; // Import your Main game class
import com.minutestilldawn.game.View.MainMenuView; // Unused, but for reference

public class MainMenuController extends BaseMenuController {

    private Main gameInstance; // Reference to the main game class for screen switching

    public MainMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public void onButtonClicked(String buttonId) {
        Gdx.app.log("MainMenuController", "Button clicked: " + buttonId);

        switch (buttonId) {
            case "play":
                Gdx.app.log("MainMenuController", "Switching to Game Screen...");
                // This will initiate your actual gameplay
                gameInstance.setGameScreen(); // Method in Main to switch to GameScreenView
                break;
            case "settings":
                Gdx.app.log("MainMenuController", "Switching to Settings Menu...");
                gameInstance.setSettingsScreen(); // You will create this method in Main
                break;
            case "profile":
                Gdx.app.log("MainMenuController", "Switching to Profile Menu...");
                gameInstance.setProfileScreen(); // You will create this method in Main
                break;
            case "scoreboard":
                Gdx.app.log("MainMenuController", "Switching to Scoreboard...");
                gameInstance.setScoreboardScreen(); // You will create this method in Main
                break;
            case "hint":
                Gdx.app.log("MainMenuController", "Switching to Scoreboard...");
                gameInstance.setScoreboardScreen(); // You will create this method in Main
            case "logout":
                Gdx.app.log("MainMenuController", "Logout button pressed...");
                
            case "exit":
                Gdx.app.log("MainMenuController", "Exiting game...");
                Gdx.app.exit(); // Exit the application
                break;
            default:
                Gdx.app.log("MainMenuController", "Unknown button ID: " + buttonId);
                break;
        }
    }
}