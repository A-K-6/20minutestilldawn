package com.minutestilldawn.game.View;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.minutestilldawn.game.Controller.MainMenuController;

public class MainMenuView extends BaseMenuView {

    private final MainMenuController controller;

    public MainMenuView(MainMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        // The controller needs a reference to this view for specific view interactions if any
        this.controller.setView(this); // BaseMenuController has setView
    }

    @Override
    protected void setupUI() {
        // --- Game Title ---
        Label gameTitle = new Label("20 MINUTES TILL DAWN", skin, "title"); // Use a "title" style from your skin
        if (!skin.has("title", Label.LabelStyle.class)) {
            // Fallback if "title" style is not defined in skin
            gameTitle.setStyle(skin.get("default", Label.LabelStyle.class));
            gameTitle.setFontScale(2.0f); // Make it larger if no specific style
        }


        // --- Menu Buttons ---
        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton profileButton = new TextButton("Profile", skin);
        TextButton scoreboardButton = new TextButton("Scoreboard", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // --- Layout using Table ---
        table.center(); // Center the table in the screen
        table.defaults().pad(10).width(200).height(50); // Default padding and size for buttons

        table.add(gameTitle).padBottom(50).row(); // Title at the top, extra padding below

        table.add(playButton).row();
        table.add(settingsButton).row();
        table.add(profileButton).row();
        table.add(scoreboardButton).row();
        table.add(exitButton).row();

        // --- Add Listeners to Buttons ---
        // Play Button
        playButton.addListener(event -> {
            if (playButton.isPressed()) { // Check if the button is actually pressed down
                controller.onButtonClicked("play");
            }
            return false; // Return false to indicate event is not consumed if you want it to propagate
        });

        // Settings Button
        settingsButton.addListener(event -> {
            if (settingsButton.isPressed()) {
                controller.onButtonClicked("settings");
            }
            return false;
        });

        // Profile Button
        profileButton.addListener(event -> {
            if (profileButton.isPressed()) {
                controller.onButtonClicked("profile");
            }
            return false;
        });

        // Scoreboard Button
        scoreboardButton.addListener(event -> {
            if (scoreboardButton.isPressed()) {
                controller.onButtonClicked("scoreboard");
            }
            return false;
        });

        // Exit Button
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                controller.onButtonClicked("exit");
            }
            return false;
        });
    }
}