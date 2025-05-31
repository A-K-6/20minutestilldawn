package com.minutestilldawn.game.View;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Controller.PreGameMenuController;
import com.minutestilldawn.game.Model.CharacterType;
import com.minutestilldawn.game.Model.GameAssetManager;

public class PreGameMenuView extends BaseMenuView {

    private final PreGameMenuController controller;

    private SelectBox<CharacterType> characterSelectBox;
    private SelectBox<String> weaponSelectBox;
    private SelectBox<Float> durationSelectBox;

    // Labels to display current selection (optional, if not using SelectBox prompt)
    private Label selectedCharacterLabel;
    private Label selectedWeaponLabel;
    private Label selectedDurationLabel;


    public PreGameMenuView(PreGameMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        this.controller.setView(this); // Set the view in the controller
    }

    @Override
    protected void setupUI() {
        // Optional: Set background for this menu
        try {
            Texture backgroundTexture = Main.getInstance().getAssetManager()
                    .get(GameAssetManager.MENU_BACKGROUND_TEXTURE, Texture.class); // Or a specific pregame menu background
            Drawable background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
            table.setBackground(background);
        } catch (Exception e) {
            System.err.println("Error loading background for PreGameMenuView: " + e.getMessage());
        }


        table.center();
        table.defaults().pad(15);

        Label titleLabel = new Label("Prepare for Battle!", skin, "title");
        if (!skin.has("title", Label.LabelStyle.class)) { // Fallback style
            titleLabel.setStyle(skin.get("default", Label.LabelStyle.class));
            titleLabel.setFontScale(1.6f);
        }
        table.add(titleLabel).colspan(2).padBottom(30).row();

        // --- Character Selection ---
        table.add(new Label("Choose Hero:", skin)).right();
        characterSelectBox = new SelectBox<>(skin);
        characterSelectBox.setItems(controller.getAvailableCharacters());
        characterSelectBox.setSelected(controller.getSelectedCharacter()); // Set initial
        table.add(characterSelectBox).width(250).row();

        // --- Weapon Selection ---
        table.add(new Label("Choose Weapon:", skin)).right();
        weaponSelectBox = new SelectBox<>(skin);
        weaponSelectBox.setItems(controller.getAvailableWeapons());
        weaponSelectBox.setSelected(controller.getSelectedWeapon()); // Set initial
        table.add(weaponSelectBox).width(250).row();

        // --- Duration Selection ---
        table.add(new Label("Game Duration:", skin)).right();
        durationSelectBox = new SelectBox<>(skin);
        // Prepare durations as Strings for display
        Float[] durations = controller.getAvailableDurations();
        String[] durationStrings = new String[durations.length];
        for (int i = 0; i < durations.length; i++) {
            durationStrings[i] = ((int)durations[i].floatValue()) + " min";
        }
        SelectBox<String> durationSelectBoxStr = new SelectBox<>(skin);
        durationSelectBoxStr.setItems(durationStrings);
        // Set initial selection
        int selectedIndex = 0;
        for (int i = 0; i < durations.length; i++) {
            if (durations[i].equals(controller.getSelectedDurationMinutes())) {
                selectedIndex = i;
                break;
            }
        }
        durationSelectBoxStr.setSelected(durationStrings[selectedIndex]);
        this.durationSelectBox = null; // Not used anymore, but keep field for compatibility if needed
        table.add(durationSelectBoxStr).width(250).row();


        // --- Start Game Button ---
        TextButton startGameButton = new TextButton("Start Game", skin);
        table.add(startGameButton).colspan(2).width(250).height(60).padTop(30).row();

        // --- Back Button ---
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        table.add(backButton).colspan(2).width(250).height(50).padTop(10).row();

        // --- Add Listeners ---
        durationSelectBoxStr.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int selectedIdx = durationSelectBoxStr.getSelectedIndex();
                controller.durationSelected(durations[selectedIdx]);
            }
        });

        weaponSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.weaponSelected(weaponSelectBox.getSelected());
            }
        });

        durationSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.durationSelected(durationSelectBox.getSelected());
            }
        });

        startGameButton.addListener(event -> {
            if (startGameButton.isPressed()) {
                controller.onButtonClicked("start_game");
            }
            return false;
        });

        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                controller.onButtonClicked("back_to_main_menu");
            }
            return false;
        });
    }

    // Methods for controller to update labels if not using SelectBox prompt directly
    public void updateSelectedCharacterLabel(String text) {
        // if (selectedCharacterLabel != null) selectedCharacterLabel.setText("Hero: " + text);
    }
    public void updateSelectedWeaponLabel(String text) {
        // if (selectedWeaponLabel != null) selectedWeaponLabel.setText("Weapon: " + text);
    }
    public void updateSelectedDurationLabel(String text) {
        // if (selectedDurationLabel != null) selectedDurationLabel.setText("Duration: " + text);
    }
}
