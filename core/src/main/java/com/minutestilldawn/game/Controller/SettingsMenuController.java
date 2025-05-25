package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main; // Import your Main game class
import com.minutestilldawn.game.View.BaseMenuView;
import com.minutestilldawn.game.View.SettingsMenuView; // Import the view

public class SettingsMenuController extends BaseMenuController {
    private Main gameInstance;
    private SettingsMenuView view; // Reference to the view

    // In a real game, you'd load/save these from Preferences or a settings file
    private float musicVolume = 0.5f; // Default volume
    private float sfxVolume = 0.7f;   // Default volume
    private String currentLanguage = "English"; // Default language

    public SettingsMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    // Override setView to cast it to SettingsMenuView
    @Override
    public void setView(BaseMenuView view) {
        super.setView(view);
        this.view = (SettingsMenuView) view;
    }

    @Override
    public void onButtonClicked(String buttonId) {
        Gdx.app.log("SettingsMenuController", "Button clicked: " + buttonId);
        switch (buttonId) {
            case "back":
                gameInstance.setMainMenuScreen(); // Go back to the main menu
                // TODO: Save settings when going back if they haven't been saved implicitly
                break;
            // Add other button handlers if you add more buttons (e.g., Apply, Reset)
        }
    }

    public void onMusicVolumeChanged(float volume) {
        this.musicVolume = volume;
        Gdx.app.log("SettingsMenuController", "Music Volume changed to: " + volume);
        // TODO: Apply volume change (e.g., Gdx.app.getPreferences().putFloat("musicVolume", volume);)
        // TODO: Update your audio manager to reflect this volume
    }

    public void onSfxVolumeChanged(float volume) {
        this.sfxVolume = volume;
        Gdx.app.log("SettingsMenuController", "SFX Volume changed to: " + volume);
        // TODO: Apply volume change (e.g., Gdx.app.getPreferences().putFloat("sfxVolume", volume);)
        // TODO: Update your audio manager to reflect this volume
    }

    public void onLanguageSelected(String language) {
        this.currentLanguage = language;
        Gdx.app.log("SettingsMenuController", "Language selected: " + language);
        // TODO: Implement actual language switching logic
        // This would involve loading different asset bundles (e.g., for labels, dialogues)
        // and potentially restarting relevant parts of the UI or game.
        // Gdx.app.getPreferences().putString("language", language);
    }

    // Methods to provide current settings values to the view
    public float getMusicVolume() {
        // In a real scenario, you'd load this from Preferences or a settings object
        return musicVolume;
    }

    public float getSfxVolume() {
        // In a real scenario, you'd load this from Preferences or a settings object
        return sfxVolume;
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }
}