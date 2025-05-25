package com.minutestilldawn.game.View;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.minutestilldawn.game.Controller.GameMenuController; // Needs to be created

public class GameMenuView extends BaseMenuView {
    private final GameMenuController controller;

    public GameMenuView(GameMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        this.controller.setView(this);
    }

    @Override
    protected void setupUI() {
        // TODO: Implement "New Game" UI (e.g., character selection, difficulty)
        table.center();
        table.add(new Label("New Game/Character Select Screen (TODO)", skin)).pad(20).row();
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton backButton = new TextButton("Back", skin);
        table.add(startButton).pad(10).row();
        table.add(backButton).pad(10).row();
        startButton.addListener(event -> {
            if (startButton.isPressed()) controller.onButtonClicked("start_game"); return false;
        });
        backButton.addListener(event -> {
            if (backButton.isPressed()) controller.onButtonClicked("back"); return false;
        });
    }
}