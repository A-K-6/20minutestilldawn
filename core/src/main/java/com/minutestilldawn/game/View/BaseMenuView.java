package com.minutestilldawn.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.minutestilldawn.game.Main;

public abstract class BaseMenuView implements Screen {
    private Stage stage;
    public Table table; // Make sure this is accessible to subclasses or is manipulated by setupUI
    protected Skin skin;

    // This method must be implemented by subclasses to define their unique UI
    protected abstract void setupUI();

    public BaseMenuView(Skin skin) {
        this.skin = skin;
        this.table = new Table(); // Initialize the table here
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true); // Make the table fill the entire stage
        stage.addActor(table);     // Add the table to the stage

        // IMPORTANT: Call the abstract setupUI() method here!
        // This ensures that the concrete implementation (e.g., MainMenuView's setupUI)
        // is executed and populates the 'table' with its specific UI elements.
        setupUI();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen with black
        Main.getBatch().begin();
        // You might draw background elements here if not part of the UI stage
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update actors
        stage.draw(); // Draw actors
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update viewport on resize
    }

    @Override
    public void pause() {
        // Implement pause logic if needed
    }

    @Override
    public void resume() {
        // Implement resume logic if needed
    }

    @Override
    public void hide() {
        // Dispose of the stage when the screen is hidden
        Gdx.input.setInputProcessor(null); // Clear input processor
    }

    @Override
    public void dispose() {
        // Dispose of the stage when the screen is no longer needed
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
    }

    // Removed the private fields and their getters, as they were causing the conflict
    // and are not part of the abstract base class's responsibility to define concrete UI.
    // Subclasses will create their own specific buttons/labels.
}