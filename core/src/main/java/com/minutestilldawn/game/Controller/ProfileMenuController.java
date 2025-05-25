package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main;

public class ProfileMenuController extends BaseMenuController {
    private Main gameInstance;

    public ProfileMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public void onButtonClicked(String buttonId) {
        switch (buttonId) {
            case "edit_profile":
                // TODO: Edit profile logic
                Gdx.app.log("Profile", "Edit profile button clicked (TODO)");
                break;
            case "back":
                gameInstance.setMainMenuScreen();
                break;
        }
    }
}