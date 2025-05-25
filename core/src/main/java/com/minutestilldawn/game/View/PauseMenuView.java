package com.minutestilldawn.game.View;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.minutestilldawn.game.Controller.PauseMenuController; // Needs to be created
import com.badlogic.gdx.scenes.scene2d.Stage; // For in-game overlays

// For pause menu, it's often a Table that gets added/removed from GameScreenView's Stage
// rather than a full Screen, but can be a Screen if you pause the entire game.
public class PauseMenuView { // Not extending BaseMenuView if it's just a Table overlay
    private Table table;
    private Skin skin;
    private PauseMenuController controller;

    public PauseMenuView(PauseMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        this.table = new Table();
        this.table.setFillParent(true); // Fills parent stage
        setupUI();
    }

    private void setupUI() {
        // TODO: Implement Pause Menu UI (Resume, Restart, Settings, Main Menu, Exit)
        table.center();
        table.add(new Label("Game Paused", skin, "default-font", "white")).pad(20).row();
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton exitToMainButton = new TextButton("Exit to Main Menu", skin);
        table.add(resumeButton).pad(10).row();
        table.add(exitToMainButton).pad(10).row();

        resumeButton.addListener(event -> {
            if (resumeButton.isPressed()) controller.onButtonClicked("resume"); return false;
        });
        exitToMainButton.addListener(event -> {
            if (exitToMainButton.isPressed()) controller.onButtonClicked("exit_to_main"); return false;
        });
    }

    public Table getTable() {
        return table;
    }
}