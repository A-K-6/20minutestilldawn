package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main;

public class HintMenuController extends BaseMenuController {
    private Main gameInstance;

    public HintMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public void onButtonClicked(String buttonId) {
        switch (buttonId) {
            case "back":
                gameInstance.setMainMenuScreen();
                break;
        }
    }

}
