package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main; // Needs Main instance to switch screens

public class PauseMenuController extends BaseMenuController { // Can extend BaseMenuController if needed
    private Main gameInstance;
    // You might also need a reference to the GameScreenView to manage the pause state
    // private GameScreenView gameScreenView;

    public PauseMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public void onButtonClicked(String buttonId) {
        switch (buttonId) {
            case "resume":
                Gdx.app.log("PauseMenu", "Resume game.");
                // TODO: Hide pause menu overlay, unpause game logic
                // if (gameScreenView != null) gameScreenView.resumeGame();
                break;
            case "exit_to_main":
                Gdx.app.log("PauseMenu", "Exiting to main menu.");
                gameInstance.setMainMenuScreen();
                break;
            // Add cases for Restart, Settings (from pause menu), etc.
        }
    }
}