package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main;

public class GameMenuController extends BaseMenuController {
    private Main gameInstance;

    public GameMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public void onButtonClicked(String buttonId) {
        switch (buttonId) {
            case "start_game":
                // TODO: Handle character selection, difficulty, then start game
                Gdx.app.log("GameMenu", "Start Game button clicked (TODO)");
                gameInstance.setGameScreen(); // Example: Directly start game for now
                break;
            case "back":
                gameInstance.setMainMenuScreen();
                break;
        }
    }
}