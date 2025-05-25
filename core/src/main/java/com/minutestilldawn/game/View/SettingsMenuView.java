package com.minutestilldawn.game.View;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox; // For language selection
import com.badlogic.gdx.scenes.scene2d.ui.Slider;    // For volume control
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.minutestilldawn.game.Controller.SettingsMenuController;

public class SettingsMenuView extends BaseMenuView {
    private final SettingsMenuController controller;

    private SelectBox<String> languageSelectBox;
    private Slider musicVolumeSlider;
    private Slider sfxVolumeSlider;

    public SettingsMenuView(SettingsMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        this.controller.setView(this); // Ensure controller has reference to this view
    }

    @Override
    protected void setupUI() {
        table.center(); // Center the table on the screen
        table.defaults().pad(10); // Default padding for all elements

        // --- Screen Title ---
        Label titleLabel = new Label("Settings", skin, "title"); // Use a "title" style from your skin
        if (!skin.has("title", Label.LabelStyle.class)) {
            titleLabel.setStyle(skin.get("default", Label.LabelStyle.class));
            titleLabel.setFontScale(1.8f); // Fallback if no specific title style
        }
        table.add(titleLabel).colspan(2).padBottom(40).row(); // Spans two columns for better alignment

        // --- Language Setting (as per PDF) ---
        table.add(new Label("Language:", skin)).right();
        languageSelectBox = new SelectBox<>(skin);
        languageSelectBox.setItems("English", "فارسی (Persian)"); // Add more languages as needed
        languageSelectBox.setSelected("English"); // Set default
        table.add(languageSelectBox).width(200).row();

        // --- Music Volume ---
        table.add(new Label("Music Volume:", skin)).right();
        musicVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        musicVolumeSlider.setValue(controller.getMusicVolume()); // Get initial value from controller
        table.add(musicVolumeSlider).width(300).row();

        // --- SFX Volume ---
        table.add(new Label("SFX Volume:", skin)).right();
        sfxVolumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        sfxVolumeSlider.setValue(controller.getSfxVolume()); // Get initial value from controller
        table.add(sfxVolumeSlider).width(300).row();

        // --- Back Button ---
        TextButton backButton = new TextButton("Back", skin);
        table.add(backButton).colspan(2).padTop(40).width(200).height(50).row();

        // --- Add Listeners ---
        languageSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onLanguageSelected(languageSelectBox.getSelected());
            }
        });

        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onMusicVolumeChanged(musicVolumeSlider.getValue());
            }
        });

        sfxVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onSfxVolumeChanged(sfxVolumeSlider.getValue());
            }
        });

        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                controller.onButtonClicked("back"); // This calls the method in SettingsMenuController
            }
            return false;
        });
    }

    // You might add methods to update UI elements if settings change externally
    public void setMusicVolume(float volume) {
        if (musicVolumeSlider != null) {
            musicVolumeSlider.setValue(volume);
        }
    }

    public void setSfxVolume(float volume) {
        if (sfxVolumeSlider != null) {
            sfxVolumeSlider.setValue(volume);
        }
    }
}