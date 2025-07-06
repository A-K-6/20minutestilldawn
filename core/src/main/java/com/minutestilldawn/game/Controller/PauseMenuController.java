package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main; // Needs Main instance to switch screens

public class PauseMenuController extends BaseMenuController { // Can extend BaseMenuController if needed
    private Main gameInstance;
    private GameController gameController;

    public PauseMenuController(Main gameInstance, GameController gameController) {
        this.gameInstance = gameInstance;
        this.gameController = gameController;
    }

    public PauseMenuController(Main gameInstance) {
        this(gameInstance, null);
    }

    @Override
    public void onButtonClicked(String buttonId) {
        switch (buttonId) {
            case "resume":
                Gdx.app.log("PauseMenu", "Resume game.");
                if (gameController != null) gameController.resumeGame();
                break;
            case "exit_to_main":
                Gdx.app.log("PauseMenu", "Exiting to main menu.");
                gameInstance.setMainMenuScreen();
                break;
            // Add cases for Restart, Settings (from pause menu), etc.
        }
    }
}