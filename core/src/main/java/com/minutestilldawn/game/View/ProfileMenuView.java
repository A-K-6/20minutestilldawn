package com.minutestilldawn.game.View;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.minutestilldawn.game.Controller.ProfileMenuController; // Needs to be created

public class ProfileMenuView extends BaseMenuView {
    private final ProfileMenuController controller;

    public ProfileMenuView(ProfileMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        this.controller.setView(this);
    }

    @Override
    protected void setupUI() {
        // TODO: Implement Profile UI (Display user stats, unlocked characters/abilities)
        table.center();
        table.add(new Label("Profile Screen (TODO)", skin)).pad(20).row();
        TextButton backButton = new TextButton("Back", skin);
        table.add(backButton).pad(10).row();
        backButton.addListener(event -> {
            if (backButton.isPressed()) controller.onButtonClicked("back"); return false;
        });
    }
}